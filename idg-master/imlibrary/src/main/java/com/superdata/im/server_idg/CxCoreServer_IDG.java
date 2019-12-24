package com.superdata.im.server_idg;

import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.text.TextUtils;

import com.chaoxiang.base.utils.AESUtils;
import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.FileUtils;
import com.chaoxiang.base.utils.MD5Util;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.SPUtils;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.entity.IMDaoManager;
import com.im.client.CallbackType;
import com.im.client.LocationType;
import com.im.client.MediaType;
import com.im.client.callback.ClientCallback;
import com.im.client.callback.DefaultClientCallback;
import com.im.client.compoment.ConnectionParameter;
import com.im.client.core.ChatManager;
import com.im.client.core.IMClient;
import com.im.client.struct.IMMessage;
import com.im.client.struct.IMMessageProtos;
import com.im.client.util.UUID;
import com.superdata.im.callback.CxFileUploadCallback;
import com.superdata.im.constants.CxBroadcastAction;
import com.superdata.im.constants.CxIMMessageStatus;
import com.superdata.im.constants.CxIMMessageType;
import com.superdata.im.constants.CxSPIMKey;
import com.superdata.im.entity.BoyEntity;
import com.superdata.im.entity.CxFileMessage;
import com.superdata.im.entity.CxMessage;
import com.superdata.im.entity.CxNotifyEntity;
import com.superdata.im.manager.CxHeartBeatManager;
import com.superdata.im.manager.CxServerManager;
import com.superdata.im.processor.CxLoginProcessor;
import com.superdata.im.receiver.CxDoImReceiver;
import com.superdata.im.receiver.CxScreenBroadcastListener;
import com.superdata.im.utils.CXMessageUtils;
import com.superdata.im.utils.CxBroadcastUtils;
import com.superdata.im.utils.CxCommUtils;
import com.superdata.im.utils.CxNotificationUtils;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;

/**
 * @author zjh
 * @version v1.0.0
 * @date 2016/1/4
 * @desc 主要处理后台推送过来的消息
 */
public class CxCoreServer_IDG extends CxBaseServer
{
    private SDGson mGson;
    private ConnectionParameter parameter;
    private String account;
    private String password;
    private String userName;

    /**
     * 定时任务,监听链路是否可用
     */

    private PushBinder mBinder = new PushBinder();
    private final String OFFLINE_SINGLE_CHAT = "singleChat";
    private final String OFFLINE_GROUP_CHAT = "groupChat";

    public CxDoImReceiver doImReceiver; //消息广播接收者

    private PowerManager pm;
    private PowerManager.WakeLock wl;
    private IMClient imClient;
    private CxScreenBroadcastListener screenListener;
    private CxScreenBroadcastListener.ScreenStateListener screenStateListener = new CxScreenBroadcastListener
            .ScreenStateListener()
    {
        @Override
        public void onScreenOn()
        {
            SDLogUtil.info("ScreenOn ==> stopForeground");
            SDLogUtil.showLog("屏幕状态", "屏幕亮起！");
            stopForeground(true);
        }

        @Override
        public void onScreenOff()
        {
            SDLogUtil.info("ScreenOff ==> startForeground");
            SDLogUtil.showLog("屏幕状态", "屏幕熄灭！");
//            startForeground(FOREGROUND_NOTIFY_ID, CxNotificationUtils.
//                    getInstance(CxCoreServer_IDG.this).getBuilder(new CxNotifyEntity(0, "", "", null, SPUtils.class, "", 0, 0))
//                    .build()); //SPUtils.class 可随意传class,但不能为空
        }

        @Override
        public void onUserPresent()
        {


        }
    };

    private final int FOREGROUND_NOTIFY_ID = 3;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        String tmpString = "im-服务onStartCommand";
        FileUtils.getInstance().writeFileToSDCard(tmpString.getBytes(), null, "app_im_IDGServer.txt", true,
                true);

        return START_STICKY;
    }

    public class PushBinder extends Binder
    {
        public synchronized void conn(final String imAccount, final String pwd)
        {
            if (CxCommUtils.isNetWorkConnected(CxCoreServer_IDG.this))
            {
                SDLogUtil.info("network connect");
                SDLogUtil.showLog("IM-coreserver网络", "有网络！");
            } else
            {
                SDLogUtil.info("network disconnect");
                SDLogUtil.showLog("IM-coreserver网络", "无网络！");
            }

            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        SDLogUtil.debug("IM-Login-start");
                        CxCoreServer_IDG.this.userName = userName;
                        password = pwd;
                        account = imAccount;
                        parameter.setFrom(account);
                        //加密两次
                        parameter.setPassword(MD5Util.MD5(MD5Util.MD5(password)));
//                        parameter.setPassword("CF3851E8692EF6A2BB1A45DF703EDC64");
                        parameter.setSocketType(LocationType.ANDROID.value());
                        DefaultClientCallback defaultClientCallback = DefaultClientCallback.getInstance();
                        defaultClientCallback.registerCallback(CallbackType.PUSH_CALLBACK, new SuccessMsgCallback
                                (CxIMMessageType.PUSH));
                        defaultClientCallback.registerCallback(CallbackType.ERROR_CALLBACK, new ErrorMsgCallback());
                        defaultClientCallback.registerCallback(CallbackType.LOGIN_CALLBACK, new SuccessMsgCallback
                                (CxIMMessageType.LOGIN));
                        defaultClientCallback.registerCallback(CallbackType.ANSWER_CALLBACK, new SuccessMsgCallback
                                (CxIMMessageType.ANSWER));
                        defaultClientCallback.registerCallback(CallbackType.SINGLE_CHAT_CALLBACK, new SuccessMsgCallback
                                (CxIMMessageType.SINGLE_CHAT));
                        defaultClientCallback.registerCallback(CallbackType.GROUP_CHAT_CALLBACK, new SuccessMsgCallback
                                (CxIMMessageType.GROUP_CHAT));
                        defaultClientCallback.registerCallback(CallbackType.OFFLINE_CALLBACK, new SuccessOfflineCallback());
                        defaultClientCallback.registerCallback(CallbackType.VIDEO_VOICE_CALLBACK, new SuccessMsgCallback
                                (CxIMMessageType.SINGLE_VIDEO_OR_AUDIO_CHAT));
                        defaultClientCallback.registerCallback(CallbackType.READ_CALLBACK, new SuccessMsgCallback
                                (CxIMMessageType.READ_STATUS));
                        defaultClientCallback.registerCallback(CallbackType.ANSWER_CALLBACK, new SuccessMsgCallback
                                (CxIMMessageType.ANSWER));

                        imClient.connect();

                        SDLogUtil.debug("IM-Login-end");
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                        SDLogUtil.debug("IM-conn exception");
                        sendConnExceptionMsg();
                    }
                }

            }).start();
        }

        /**
         * 发送推送消息
         *
         * @param to
         * @param msg
         */
        public void sendPushMsg(String to, String msg)
        {
            try
            {
                SDLogUtil.debug("to=" + to + ",msg=" + msg);
                com.im.client.struct.IMMessageProtos.IMMessage imMessage = ChatManager.getInstance().buildSinglePushMsg(to,
                        msg, UUID.next());
                if (ChatManager.getInstance() != null)
                {
                    ChatManager.getInstance().sendMessage(imMessage);
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        /**
         * 发送推送消息
         *
         * @param to
         * @param msg
         */
        public void sendPushMsg(String to, String msg, Map<String, String> attrs)
        {
            try
            {
                SDLogUtil.debug("to=" + to + ",msg=" + msg);
                com.im.client.struct.IMMessageProtos.IMMessage imMessage = ChatManager.getInstance().buildSinglePushMsg(to,
                        msg, UUID.next(), attrs);
                if (ChatManager.getInstance() != null)
                {
                    ChatManager.getInstance().sendMessage(imMessage);
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        /**
         * 是否连接
         *
         * @return
         */
        public boolean isConn()
        {
            return ChatManager.getInstance().isConnect();
        }

        /**
         * 关闭连接
         */
        public void closeConn()
        {
            ChatManager.getInstance().close();
        }

        public String sendVideoOrAudioMsg(String to, String msg)
        {
            String msgId = UUID.next();
            try
            {
                com.im.client.struct.IMMessageProtos.IMMessage imMessage = ChatManager.getInstance().buildSingleVideoReq(to,
                        msg, msgId);
                ChatManager.getInstance().sendMessage(imMessage);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            return msgId;
        }

        public String sendVideoOrAudioMsg(String to, String msg, Map<String, String> attrs)
        {
            String msgId = UUID.next();
            try
            {
                com.im.client.struct.IMMessageProtos.IMMessage imMessage = ChatManager.getInstance().buildSingleVideoReq(to,
                        msg, msgId, attrs);
                ChatManager.getInstance().sendMessage(imMessage);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            return msgId;
        }

        /**
         * 重新连接
         */
        public void reConn()
        {
            SDLogUtil.info("IM-CxCoreServer_IDG-正在重连！");
            if (StringUtils.empty(account) || StringUtils.empty(password))
            {
                account = (String) SPUtils.get(CxCoreServer_IDG.this, CxSPIMKey.STRING_ACCOUNT, "");
                password = AESUtils.des((String) SPUtils.get(CxCoreServer_IDG.this, CxSPIMKey.STRING_PWD_AES_SEEND, "")
                        , (String) SPUtils.get(CxCoreServer_IDG.this, CxSPIMKey.STRING_AES_PWD, ""), Cipher.DECRYPT_MODE);
            }
            parameter.setIsReconnect(true);
            conn(account, password);
        }

        /**
         * 退出登录
         */
        public void loginOut()
        {
            try
            {
                ChatManager.getInstance().sendMessage(ChatManager.getInstance().buildLoginOUtReq());
                closeConn();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        /**
         *
         */
        public CxMessage sendSingleChat(final CxMessage cxMessage, final long audioOrVideoLength)
        {
            String msgId = UUID.next();
            try
            {
                //设置为单聊类型
                cxMessage.setType(CxIMMessageType.SINGLE_CHAT.getValue());
                IMMessageProtos.IMMessage textMsg = ChatManager.getInstance().buildSingleChatReq(cxMessage.getImMessage()
                                .getHeader().getTo(),
                        cxMessage.getImMessage().getBody(), cxMessage.getMediaType(), msgId, cxMessage.getImMessage().getHeader
                                ().getAttachment());

                SDLogUtil.debug("Im-发送信息:  " + cxMessage.getImMessage().getBody());
                SDLogUtil.debug("Im-发送信息:  " + cxMessage.toString());
                SDLogUtil.debug("send_mediatype==" + cxMessage.getMediaType());

                sendMsg(cxMessage, audioOrVideoLength, textMsg, msgId);
            } catch (Exception e)
            {
                e.printStackTrace();
                SDLogUtil.debug("sendSingleChat==>exception");
                saveToDb(cxMessage, msgId);
                cxMessage.startWatcher();
            }
            return cxMessage;
        }

        /**
         * 重发聊天消息
         *
         * @param cxMessage
         * @return
         */
        public void reSendChatMsg(CxMessage cxMessage)
        {
            if (cxMessage.getStatus() == CxIMMessageStatus.FILE_UPLOAD_FAIL)
            {
                cxMessage.setStatus(CxIMMessageStatus.FILE_SENDING);
                IMDaoManager.getInstance().getDaoSession().getIMMessageDao().updateMsgByMsgId(CXMessageUtils.convertCXMessage
                        (cxMessage)); //更新数据库消息状态
                CxFileMessage CxFileMessage = mGson.fromJson(cxMessage.getBody(), CxFileMessage.class);
//                try
//                {
                uploadMsgFile(cxMessage, CxFileMessage.getLength(), cxMessage.getImMessage().getHeader().getMessageId(), true);
//                }
//                catch (Exception e)
//                {
//                    SDLogUtil.debug(e.toString());
//                }

            } else
            {
                cxMessage.setStatus(CxIMMessageStatus.SENDING);
                IMDaoManager.getInstance().getDaoSession().getIMMessageDao().updateMsgByMsgId(CXMessageUtils.convertCXMessage
                        (cxMessage));//更新数据库消息状态
                IMMessageProtos.IMMessage textMsg = null;
                try
                {
                    if (cxMessage.getType() == CxIMMessageType.SINGLE_CHAT.getValue())
                    {
                        textMsg = ChatManager.getInstance().buildSingleChatReq(cxMessage.getImMessage().getHeader().getTo(),
                                cxMessage.getImMessage().getBody(), cxMessage.getMediaType(), cxMessage.getImMessage()
                                        .getHeader().getMessageId());
                    } else
                    {
                        textMsg = ChatManager.getInstance().buildGroupChatReq(cxMessage.getImMessage().getHeader().getGroupId(),
                                cxMessage.getImMessage().getBody(), cxMessage.getMediaType(), cxMessage.getImMessage()
                                        .getHeader().getMessageId());
                    }
                    ChatManager.getInstance().sendMessage(textMsg);
                    cxMessage.startWatcher();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 发送消息
         *
         * @param cxMessage
         * @param audioOrVideoLength
         * @param textMsg
         */
        private void sendMsg(final CxMessage cxMessage, final long audioOrVideoLength, IMMessageProtos.IMMessage textMsg, final
        String msgId)
        {
            SDLogUtil.info("message send ==>body=" + textMsg.getBody().toStringUtf8()
                    + ",from=" + textMsg.getHeader().getFrom() + ",to=" + textMsg.getHeader().getTo() + ",msgId=" + msgId);
            long currenTimeMillis = System.currentTimeMillis();
            cxMessage.setCreateTime(DateUtils.getTimestampString(currenTimeMillis));
            cxMessage.setCreateTimeMillisecond(currenTimeMillis);
            if (cxMessage.getMediaType() == MediaType.TEXT.value() || cxMessage.getMediaType() == MediaType.POSITION.value())
            {
                saveToDb(cxMessage, textMsg.getHeader().getMessageId());
                cxMessage.startWatcher();
                ChatManager.getInstance().sendMessage(textMsg);
            } else
            {
                //附件
                if (cxMessage.getMediaType() == MediaType.AUDIO.value())
                {
                    cxMessage.setReader(true);
                }
                uploadMsgFile(cxMessage, audioOrVideoLength, msgId, false);
            }
        }

        /**
         * 上传附件
         *
         * @param cxMessage
         * @param audioOrVideoLength
         * @param msgId
         */
        private void uploadMsgFile(final CxMessage cxMessage, final long audioOrVideoLength, final String msgId, final boolean
                isResend)
        {
            String path = "";
            if (isResend)
            {
                path = cxMessage.getFileMessage().getLocalUrl();
            } else
            {
                path = cxMessage.getBody();
                File file = new File(path);
                cxMessage.getImMessage().setBody(mGson.toJson(new CxFileMessage(file.getName(), file.length(), "", file
                        .getAbsolutePath(), audioOrVideoLength)));
                saveToDb(cxMessage, msgId);
            }

            cxMessage.updateFileMsg(CxCoreServer_IDG.this, path, new CxFileUploadCallback()
            {
                @Override
                public void onSuccess(CxFileMessage CxFileMessage)
                {
                    try
                    {
                        if (StringUtils.notEmpty(CxFileMessage.getRemoteUrl()))
                            cxMessage.setStatus(CxIMMessageStatus.SUCCESS);
                        else
                            cxMessage.setStatus(CxIMMessageStatus.SENDING);
                        CxFileMessage.setLength(audioOrVideoLength);
                        String jsonData = mGson.toJson(CxFileMessage);
                        cxMessage.getImMessage().setBody(jsonData);
                        if (StringUtils.notEmpty(CxFileMessage.getRemoteUrl()))
                        {
                            saveToDb(cxMessage, msgId);
                        }
                        final IMMessageProtos.IMMessage fileMsg;
                        if (cxMessage.getType() == CxIMMessageType.SINGLE_CHAT.getValue())
                        {
                            fileMsg = ChatManager.getInstance().buildSingleChatReq(cxMessage.getImMessage().getHeader().getTo(),
                                    jsonData, cxMessage.getMediaType(), msgId, cxMessage.getImMessage().getHeader()
                                            .getAttachment());
                        } else
                        {
                            fileMsg = ChatManager.getInstance().buildGroupChatReq(cxMessage.getImMessage().getHeader()
                                            .getGroupId(),
                                    jsonData, cxMessage.getMediaType(), msgId, cxMessage.getImMessage().getHeader()
                                            .getAttachment());
                        }

                        new Handler().postDelayed(new Runnable()
                        {
                            public void run()
                            {
                                //发送的信息body.
                                ChatManager.getInstance().sendMessage(fileMsg);
                            }
                        }, 1000);
                        cxMessage.startWatcher();

                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError()
                {
                    cxMessage.updateStatus(msgId, cxMessage.getCreateTimeMillisecond(), CxIMMessageStatus.FILE_UPLOAD_FAIL);
                }
            });
        }

        private void saveToDb(CxMessage cxMessage, String msgId)
        {
            //保存到数据库
            if (account == null || "".equals(account))
            {
                account = (String) SPUtils.get(CxCoreServer_IDG.this, CxSPIMKey.STRING_ACCOUNT, "");
            }
            cxMessage.getImMessage().getHeader().setFrom(account);
            cxMessage.getImMessage().getHeader().setMessageId(msgId);
            try
            {
                IMDaoManager.getInstance().getDaoSession().getIMMessageDao().insertOrReplace(CXMessageUtils.convertCXMessage
                        (cxMessage));
            } catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        /**
         * 发送群消息
         *
         * @param cxMessage
         * @return
         */
        public CxMessage sendGroupChat(final CxMessage cxMessage, final long audioOrVideoLength)
        {
            String msgId = UUID.next();
            try
            {
                cxMessage.setType(CxIMMessageType.GROUP_CHAT.getValue());
                IMMessageProtos.IMMessage textMsg = ChatManager.getInstance().buildGroupChatReq(cxMessage.getImMessage()
                                .getHeader().getGroupId(),
                        cxMessage.getImMessage().getBody(), cxMessage.getMediaType(), msgId, cxMessage.getImMessage().getHeader
                                ().getAttachment());
                SDLogUtil.debug("send_mediatype==" + cxMessage.getMediaType());
                sendMsg(cxMessage, audioOrVideoLength, textMsg, msgId);
            } catch (Exception e)
            {
                e.printStackTrace();
                SDLogUtil.debug("sendGroupChat==>exception");
                saveToDb(cxMessage, msgId);
                cxMessage.startWatcher();
            }

            return cxMessage;
        }

        /**
         * 发送群消息
         *
         * @param cxMessage
         * @return
         */
        public CxMessage sendGroupChat(final CxMessage cxMessage)
        {
            return sendGroupChat(cxMessage, 0);
        }

        /**
         * 发送心跳
         */
        public void sendHeatbeatMsg()
        {
            try
            {
                ChatManager.getInstance().sendMessage(ChatManager.getInstance().buildHeatBeatReq());
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        //发送群主ID
        public void sendGroupPlushMsg(String to, String groupId)
        {
            Map<String, String> hashMap = new HashMap<>();
            hashMap.put("groupId", groupId);
            com.im.client.struct.IMMessageProtos.IMMessage imMessage = null;
            try
            {
                imMessage = ChatManager.getInstance().buildSinglePushMsg(to, "voicemeeting", UUID.next());

                if (ChatManager.getInstance() != null)
                {
                    ChatManager.getInstance().sendMessage(imMessage);
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        //推送一条信息，一般以封装的map-并转换为jsonString的信息
        public synchronized void sendPlushMsg(String to, String msg)
        {
            try
            {
                com.im.client.struct.IMMessageProtos.IMMessage imMessage = ChatManager.getInstance().buildSinglePushMsg(to,
                        msg, UUID.next());
                if (ChatManager.getInstance() != null)
                {
                    ChatManager.getInstance().sendMessage(imMessage);
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        /**
         * 用于已读未读的推送
         *
         * @param to
         * @param msg
         */
        public synchronized void sendPushReadMsg(String to, String msg)
        {
            try
            {
                com.im.client.struct.IMMessageProtos.IMMessage imMessage = ChatManager.getInstance().buildReadReq(to, msg, UUID
                        .next());
                if (ChatManager.getInstance() != null)
                {
                    ChatManager.getInstance().sendMessage(imMessage);
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }

    /**
     * 发送连接异常消息
     */
    private void sendConnExceptionMsg()
    {
        SDLogUtil.debug("send error msg");
        IMMessage imMessage = new IMMessage();
        imMessage.setBody(CxLoginProcessor.LOGIN_EXCEPTION_STATUS);
        sendBroadcastMsg(imMessage, CxBroadcastUtils.PLUSH_ERROR);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        SDLogUtil.debug("onBind");

        String tmpString = "im-im-CxCoreServer_IDG==>onBind";
        FileUtils.getInstance().writeFileToSDCard(tmpString.getBytes(), null, "app_im_IDGServer.txt", true,
                true);

        return mBinder;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        SDLogUtil.debug("CxCoreServer_IDG==>oncreate");
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "cx_processor_wake");
        wl.acquire();
        //注册接收消息广播
        doImReceiver = new CxDoImReceiver();

        String tmpString = "im-广播CxDoImReceiver=创建= ";
        FileUtils.getInstance().writeFileToSDCard(tmpString.getBytes(), null, "app_im_IDGServer.txt", true,
                true);

        CxBroadcastUtils.registerBroadcast(this, CxBroadcastAction.ACTION_PLUSH_MSG, doImReceiver);


        imClient = IMClient.getClient();
        parameter = ConnectionParameter.getInstance();

        mGson = new SDGson();

        screenListener = new CxScreenBroadcastListener(this);
        screenListener.begin(screenStateListener);

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (true)
                {
                    String tmpString = "im-线程 执行了==22222==" + Thread.currentThread().getId();
                    FileUtils.getInstance().writeFileToSDCard(tmpString.getBytes(), null, "app_im_IDGServer.txt", true,
                            true);
                    try
                    {
                        Thread.sleep(60000);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public boolean isBindServer()
    {
        return true;
    }

    @Override
    public boolean onUnbind(Intent intent)
    {
        SDLogUtil.debug("CxCoreServer_IDG==>onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent)
    {
        SDLogUtil.debug("im-CxCoreServer_IDG==>onRebind");

        String tmpString = "im-CxCoreServer_IDG==>onRebind";
        FileUtils.getInstance().writeFileToSDCard(tmpString.getBytes(), null, "app_im_IDGServer.txt", true,
                true);
        super.onRebind(intent);
    }

    @Override
    public void onDestroy()
    {
        SDLogUtil.debug("CxCoreServer_IDG==>onDestroy");
        stopForeground(true);
        CxServerManager.getInstance().startServer();

        super.onDestroy();
        wl.release();
    }

    abstract class ReceiverMsgCallback implements ClientCallback
    {
        //重设
        @Override
        public void resetCount()
        {
            SDLogUtil.info("clearn heart count");
            CxHeartBeatManager.getInstance(CxCoreServer_IDG.this).count.getAndSet(0);
            CxHeartBeatManager.getInstance(CxCoreServer_IDG.this).triggerReconnCount.getAndSet(0);
        }
    }

    class SuccessMsgCallback extends ReceiverMsgCallback
    {
        private CxIMMessageType type;

        public SuccessMsgCallback(CxIMMessageType type)
        {
            this.type = type;
        }

        @Override
        public void process(IMMessage imMessage)
        {
            SDLogUtil.debug("Im_消息接收:" + "发送方：" + imMessage.getHeader().getFrom());
            SDLogUtil.debug("内容为：" + imMessage.getBody());
            SDLogUtil.debug("附加的信息：" + imMessage.getHeader().getAttachment());

            boolean isRefuse = false;
            //在这里加入一个屏蔽群组信息的方法！
            if (StringUtils.notEmpty(imMessage.getHeader().getGroupId()))
            {
                isRefuse = judgeGroup(CxCoreServer_IDG.this, imMessage.getHeader().getGroupId());
            }
            if (!isRefuse)
            {
                SDLogUtil.debug("message success==> body=" + imMessage.getBody() + ",from=" + imMessage.getHeader().getFrom() +
                        ",to=" + imMessage.getHeader().getTo() + ",type=" + type.getValue());
                sendBroadcastMsg(imMessage, CxBroadcastUtils.PLUSH_SUCCESS, type.getValue());
            }
        }
    }

    /**
     * 判断是否需要屏蔽群组
     * 转入一个群组ID
     */
    private boolean judgeGroup(Context mContext, String groupId)
    {
        boolean isRefuse = false;
        //获取用户ID
        String currentAccount = (String) SPUtils.get(mContext, CxSPIMKey.STRING_ACCOUNT, "");
        //用户ID不为空的情况下。
        if (StringUtils.notEmpty(currentAccount))
        {
            //获取群组屏蔽列表
            List<String> refuseGroupList = SPUtils.getSPListRefuseGroupNum(mContext, SPUtils.KEY_GROUP_REFUSE, currentAccount);
            //群组列表不为空的情况下。
            if (StringUtils.notEmpty(refuseGroupList))
            {
                for (int i = 0; i < refuseGroupList.size(); i++)
                {
                    if (groupId.equals(refuseGroupList.get(i).toString()))
                    {
                        //存在返回屏蔽 true
                        isRefuse = true;
                    }
                }
            }
        }
        SDLogUtil.debug("CxCoreServer_IDG--- " + isRefuse + "----to---拒绝该" + groupId + "的群聊！");
        return isRefuse;
    }

    class ErrorMsgCallback extends ReceiverMsgCallback
    {
        @Override
        public void process(IMMessage imMessage)
        {
            SDLogUtil.info("message error==> body=" + imMessage.getBody() + ",from=" + imMessage.getHeader().getFrom() + ",to"
                    + imMessage.getHeader().getTo());
            sendBroadcastMsg(imMessage, CxBroadcastUtils.PLUSH_ERROR);
        }
    }

    class SuccessOfflineCallback extends ReceiverMsgCallback
    {

        @Override
        public void process(IMMessage imMessage)
        {
            SDLogUtil.debug("Im_离线推送:" + "从---" + imMessage.getHeader().getFrom() + "---来,内容为：" + imMessage.getBody());
            //离线消息
            SDLogUtil.info("message offline==> body=" + imMessage.getBody() + ",from=" + imMessage.getHeader().getFrom() + "," +
                    "to=" + imMessage.getHeader().getTo());
            String body = imMessage.getBody();
            if (TextUtils.isEmpty(body))
            {
                return;
            }
            try
            {
                JSONObject jsonObject = new JSONObject(body);
                Iterator<String> iterator = jsonObject.keys();
                while (iterator.hasNext())
                {
                    String key = iterator.next();
                    imMessage.getHeader().setMessageId(key);
                    JSONObject offlineObj = jsonObject.getJSONObject(key);
                    if (offlineObj.getString("type").equals(OFFLINE_SINGLE_CHAT))
                    {
                        imMessage.setBody(offlineObj.getString("body"));
                        //单聊
                        BoyEntity boyEntity = BoyEntity.parse(offlineObj.toString());
                        if (StringUtils.notEmpty(boyEntity.getAttachment()))
                        {
                            Map<String, String> attachment = new HashMap();
                            if (StringUtils.notEmpty(boyEntity.getAttachment().getBurnAfterReadTime()))
                                attachment.put("burnAfterReadTime", boyEntity.getAttachment().getBurnAfterReadTime());
                            else
                                attachment.put("burnAfterReadTime", "");

                            if (StringUtils.notEmpty(boyEntity.getAttachment().getImageDimensions()))
                                attachment.put("imageDimensions", boyEntity.getAttachment().getImageDimensions());
                            else
                                attachment.put("imageDimensions", "");

                            if (StringUtils.notEmpty(boyEntity.getAttachment().getIsBurnAfterRead()))
                                attachment.put("isBurnAfterRead", boyEntity.getAttachment().getIsBurnAfterRead());
                            else
                                attachment.put("isBurnAfterRead", "");

                            if (StringUtils.notEmpty(boyEntity.getAttachment().getLocation()))
                                attachment.put("location", boyEntity.getAttachment().getLocation());
                            else
                                attachment.put("location", "");
                            if (StringUtils.notEmpty(boyEntity.getAttachment().getShareDic()))
                            {
                                imMessage.setBody("");
                                attachment.put("shareDic", boyEntity.getAttachment().getShareDic());
                            } else
                            {
                                attachment.put("shareDic", "");
                            }

                            imMessage.getHeader().setAttachment(attachment);
                        }

                        imMessage.getHeader().setCreateTime(offlineObj.getLong("createTime"));
                        imMessage.getHeader().setFrom(offlineObj.getString("from"));
                        imMessage.getHeader().setMediaType(offlineObj.getInt("mediaType"));
                        imMessage.getHeader().setTo(offlineObj.getString("to"));

                        sendBroadcastMsg(imMessage, CxBroadcastUtils.PLUSH_SUCCESS, CxIMMessageType.SINGLE_CHAT.getValue());
                    } else if (offlineObj.getString("type").equals(OFFLINE_GROUP_CHAT))
                    {
                        //群聊
                        imMessage.setBody(offlineObj.getString("body"));

                        BoyEntity boyEntity = BoyEntity.parse(offlineObj.toString());
                        if (StringUtils.notEmpty(boyEntity.getAttachment()))
                        {
                            Map<String, String> attachment = new HashMap();
                            if (StringUtils.notEmpty(boyEntity.getAttachment().getBurnAfterReadTime()))
                                attachment.put("burnAfterReadTime", boyEntity.getAttachment().getBurnAfterReadTime());
                            else
                                attachment.put("burnAfterReadTime", "");

                            if (StringUtils.notEmpty(boyEntity.getAttachment().getImageDimensions()))
                                attachment.put("imageDimensions", boyEntity.getAttachment().getImageDimensions());
                            else
                                attachment.put("imageDimensions", "");

                            if (StringUtils.notEmpty(boyEntity.getAttachment().getIsBurnAfterRead()))
                                attachment.put("isBurnAfterRead", boyEntity.getAttachment().getIsBurnAfterRead());
                            else
                                attachment.put("isBurnAfterRead", "");

                            if (StringUtils.notEmpty(boyEntity.getAttachment().getLocation()))
                                attachment.put("location", boyEntity.getAttachment().getLocation());
                            else
                                attachment.put("location", "");
                            if (StringUtils.notEmpty(boyEntity.getAttachment().getShareDic()))
                            {
                                imMessage.setBody("");
                                attachment.put("shareDic", boyEntity.getAttachment().getShareDic());
                            } else
                            {
                                attachment.put("shareDic", "");
                            }

                            imMessage.getHeader().setAttachment(attachment);
                        }

                        imMessage.getHeader().setCreateTime(offlineObj.getLong("createTime"));
                        imMessage.getHeader().setFrom(offlineObj.getString("from"));
                        imMessage.getHeader().setMediaType(offlineObj.getInt("mediaType"));
                        imMessage.getHeader().setGroupId(offlineObj.getString("groupId"));
                        if (StringUtils.notEmpty(offlineObj.getString("to")))

                            sendBroadcastMsg(imMessage, CxBroadcastUtils.PLUSH_SUCCESS, CxIMMessageType.GROUP_CHAT.getValue());
                    } else
                    {
                        //推送
                        String pushData = offlineObj.toString();
                        imMessage.setBody(pushData);
                        sendBroadcastMsg(imMessage, CxBroadcastUtils.PLUSH_SUCCESS, CxIMMessageType.OFFLINE_PUSH.getValue());
                    }
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送成功广播
     *
     * @param imMessage
     * @param plushStatus
     * @param type
     */
    private void sendBroadcastMsg(IMMessage imMessage, String plushStatus, int type)
    {
        Map<String, Object> data = new HashMap<>();
        data.put(CxBroadcastUtils.EXTR_PLUSH_DATA, imMessage);
        data.put(CxBroadcastUtils.EXTR_PLUSH_STATUS, plushStatus);
        data.put(CxBroadcastUtils.EXTR_PLUSH_TYPE, type);
        CxBroadcastUtils.sendBroadcast(CxCoreServer_IDG.this, CxBroadcastAction.ACTION_PLUSH_MSG, data);
    }

    /**
     * 发送失败广播
     *
     * @param imMessage
     * @param plushStatus
     */
    private void sendBroadcastMsg(IMMessage imMessage, String plushStatus)
    {
        Map<String, Object> data = new HashMap<>();
        data.put(CxBroadcastUtils.EXTR_PLUSH_DATA, imMessage);
        data.put(CxBroadcastUtils.EXTR_PLUSH_STATUS, plushStatus);
        CxBroadcastUtils.sendBroadcast(CxCoreServer_IDG.this, CxBroadcastAction.ACTION_PLUSH_MSG, data);
    }

}
