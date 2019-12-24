package com.superdata.im.processor;

import android.content.Context;

import com.chaoxiang.base.config.Config;
import com.chaoxiang.base.config.Constants;
import com.chaoxiang.base.other.VoiceUtils;
import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.entity.IMDaoManager;
import com.chaoxiang.entity.chat.IMKeFu;
import com.chaoxiang.entity.chat.IMMessage;
import com.chaoxiang.entity.conversation.IMConversation;
import com.chaoxiang.entity.dao.IMAudioFailDao;
import com.chaoxiang.entity.dao.IMConversationDao;
import com.chaoxiang.entity.dao.IMGroupDao;
import com.chaoxiang.entity.dao.IMMessageDao;
import com.chaoxiang.entity.group.IMGroup;
import com.chaoxiang.imrestful.OkHttpUtils;
import com.chaoxiang.imrestful.callback.FileCallBack;
import com.chaoxiang.imrestful.callback.JsonCallback;
import com.google.gson.reflect.TypeToken;
import com.im.client.MediaType;
import com.squareup.okhttp.Request;
import com.superdata.im.callback.CxReceiverMsgCallback;
import com.superdata.im.constants.CxIMMessageDirect;
import com.superdata.im.constants.CxIMMessageStatus;
import com.superdata.im.constants.CxIMMessageType;
import com.superdata.im.constants.CxSPIMKey;
import com.superdata.im.entity.BaseBean;
import com.superdata.im.entity.CxFileMessage;
import com.superdata.im.entity.CxMessage;
import com.superdata.im.entity.CxUserInfoToKefuEntity;
import com.superdata.im.entity.CxWrapperConverstaion;
import com.superdata.im.entity.Members;
import com.superdata.im.entity.MessageCallBactStatus;
import com.superdata.im.entity.SendConMsg;
import com.superdata.im.utils.CXMessageUtils;
import com.superdata.im.utils.CxCommUtils;
import com.superdata.im.utils.CxFixLinkHashMap;
import com.superdata.im.utils.Observable.CxAddUnReadObservale;
import com.superdata.im.utils.Observable.CxConversationObserable;
import com.superdata.im.utils.Observable.CxIMObservable;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

import static com.chaoxiang.base.utils.SPUtils.get;
import static com.superdata.im.constants.CxIMMessageType.GROUP_CHAT;


/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-01-28
 * @desc 聊天处理器基类
 */
public abstract class CxChatCxBaseProcessor extends CxBaseProcessor implements Runnable
{
    public static CxFixLinkHashMap<String, IMConversation> fixLinkHashMap;
    private static IMConversationDao imConversationDao;
    private static IMGroupDao groupDao;
    private static IMAudioFailDao imAudioFailDao;
    private static IMMessageDao imMessageDao;
    private CxReceiverMsgCallback CxReceiverMsgCallback;

    /**
     * 当前用户
     */
    private String currentAccount = "";

    /**
     * 失败队列
     */
    private static List<CxMessage> failQueue;
    /**
     * 后台下载线程
     */
    private static Thread backgroundThread;

    public CxChatCxBaseProcessor(Context context)
    {
        super(context);
        if (backgroundThread == null)
        {
            backgroundThread = new Thread(this);
            backgroundThread.start();
        }
    }

    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                if (failQueue != null && !failQueue.isEmpty() && CxCommUtils.isNetWorkConnected(context))
                {
                    CxMessage cxMessage = failQueue.remove(0);
                    downLoadAudio(cxMessage);
                }
                Thread.sleep(1000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean doErrMsg(CxMessage cxMessage)
    {
        return false;
    }

    @Override
    public void doSuccessMsg(final CxMessage cxMessage)
    {
        SDLogUtil.debug("推送---CxChatBaseProcessor:" + cxMessage.toString());

        cxMessage.setStatus(CxIMMessageStatus.SUCCESS);
        cxMessage.setCreateTime(DateUtils.getTimestampString(cxMessage.getImMessage().getHeader().getCreateTime()));

        if ("".equals(currentAccount))
        {
            currentAccount = (String) get(context, CxSPIMKey.STRING_ACCOUNT, "");
        }
        if (cxMessage.getImMessage().getHeader().getFrom().equals(currentAccount))
        {
            cxMessage.setDirect(CxIMMessageDirect.SEND);
            //两个是否为空！
            if (cxMessage.getImMessage().getHeader().getMessageId() != null && cxMessage.getImMessage().getBody() != null)
            {
                MessageCallBactStatus messageCallBactStatus = SDGson.toObject(cxMessage.getImMessage().getBody(), new
                        TypeToken<MessageCallBactStatus>()
                        {
                        }.getType());
                String messageId = "";
                if (messageCallBactStatus != null)
                    messageId = messageCallBactStatus.getMessageId();
                else
                    messageId = cxMessage.getImMessage().getHeader().getMessageId();
                IMMessage imMessage = null;
                if (messageId != null)
                    imMessage = IMDaoManager.getInstance().getDaoSession().getIMMessageDao().loadMsgByMessageId(messageId);
                if (null != imMessage)
                {
                    cxMessage.getImMessage().getHeader().setTo(imMessage.get_to());
                }
                if (imMessage != null)
                {
                    imMessage.setMessageId(cxMessage.getImMessage().getHeader().getMessageId());
                    imMessage.setMsgStatus(cxMessage.getStatus());
                    IMDaoManager.getInstance().getDaoSession().getIMMessageDao().update(imMessage);
                }
                if (null != imMessage)
                {
                    EventBus.getDefault().post(new SendConMsg(cxMessage, cxMessage.getImMessage().getHeader().getMessageId(),
                            imMessage.getType()));
                }
                if (null != imMessage)
                {
                    int mediaTypeInt = imMessage.getMsgChatType();
                    if (mediaTypeInt == MediaType.TEXT.value() ||
                            mediaTypeInt == MediaType.PICTURE.value() ||
                            mediaTypeInt == MediaType.AUDIO.value() ||
                            mediaTypeInt == MediaType.VIDEO.value() ||
                            mediaTypeInt == MediaType.FILE.value() ||
                            mediaTypeInt == MediaType.POSITION.value() ||
                            mediaTypeInt == MediaType.VOICE.value())
                    {
                        String stringTmp = imMessage.getMessage();
                        if (mediaTypeInt != MediaType.TEXT.value() && mediaTypeInt != MediaType.POSITION.value())
                        {
                            CxFileMessage CxFileMessage = SDGson.toObject(stringTmp, CxFileMessage.class);
                            stringTmp = CxFileMessage.getRemoteUrl();
                        } else
                        {
                            stringTmp = imMessage.getMessage();
                        }
                        if (imMessage.getType() == CxIMMessageType.SINGLE_CHAT.getValue())
                        {
                            sendIos(1, mediaTypeInt, stringTmp, cxMessage.getImMessage().getHeader().getTo());
                        } else if (imMessage.getType() == CxIMMessageType.GROUP_CHAT.getValue())
                        {
                            IMGroup imGroup = IMDaoManager.getInstance().getDaoSession().getIMGroupDao().
                                    loadGroupFromGroupId(imMessage.getGroupId());
                            if (StringUtils.notEmpty(imGroup))
                            {
                                List<Members> memberStringList = Members.parseMemberList(imGroup.getMemberStringList());
                                StringBuffer sb = new StringBuffer();
                                for (int i = 0; i < memberStringList.size(); i++)
                                {
                                    if (i != (memberStringList.size() - 1))
                                    {
                                        sb.append(memberStringList.get(i)
                                                .getUserId());
                                        sb.append(",");
                                    } else
                                    {
                                        sb.append(memberStringList.get(i)
                                                .getUserId());
                                    }
                                }

                                sendIos(2, mediaTypeInt, stringTmp, sb.toString());
                            }
                        }
                    }
                }
            }
            //聊天监听
            CxIMObservable.getInstance().notifyUpdate(cxMessage);

            CxAddUnReadObservale.getInstance().sendAddUnRead(-1);
            com.chaoxiang.base.utils.SPUtils.setUnReadMessageVisable(context, false);
        } else
        {
            VoiceUtils.startVoice(context);
            VoiceUtils.startVibrator(context, -1);
            //红点
            CxAddUnReadObservale.getInstance().sendAddUnRead(1);
            com.chaoxiang.base.utils.SPUtils.setUnReadMessageVisable(context, true);

            cxMessage.setDirect(CxIMMessageDirect.RECEIVER);
            //保存到数据库
            saveMsg(cxMessage);

            Map<String, String> attachmentMap = new HashMap<String, String>();
            attachmentMap = cxMessage.getImMessage().getHeader().getAttachment();
            if (attachmentMap.get("userInfo") != null)
                if (attachmentMap.size() > 0 && attachmentMap.get("userInfo").length() > 0)
                {
                    final CxUserInfoToKefuEntity CxUserInfo = CxUserInfoToKefuEntity.parse(attachmentMap.get("userInfo"));
                    if (StringUtils.notEmpty(CxUserInfo))
                    {
                        saveKeFuContact(CxUserInfo);
                    }
                }
            if (cxMessage.getMediaType() == MediaType.AUDIO.value())
            {
                downLoadAudio(cxMessage);
            } else
            {
                CxIMObservable.getInstance().notifyUpdate(cxMessage);
            }
        }
        if (imConversationDao != null && cxMessage.getMediaType() != MediaType.AUDIO.value())
        {
            //ifNotExistCreatConversation(cxMessage);
            ifNotExistCreatConversation2(cxMessage);
        }
    }

    protected void saveKeFuContact(CxUserInfoToKefuEntity CxUserInfo)
    {
        //这里加入一个定位图标
        IMKeFu imKeFu = new IMKeFu();
        imKeFu.setUserId(CxUserInfo.getUserId());
        imKeFu.setIcon(CxUserInfo.getIcon());
        imKeFu.setRealName(CxUserInfo.getRealName());
        imKeFu.setImAccount(CxUserInfo.getHxAccount());
        imKeFu.setUserType(CxUserInfo.getUserType());
        imKeFu.setEmail(CxUserInfo.getEmail());
        imKeFu.setTelephone(CxUserInfo.getTelephone());
        imKeFu.setJob(CxUserInfo.getJob());
        imKeFu.setSex(CxUserInfo.getSex());
        imKeFu.setDpName(CxUserInfo.getDpName());
        imKeFu.setAccount(CxUserInfo.getAccount());
        imKeFu.setAttachment(CxUserInfo.getAttachment());
        if (IMDaoManager.getInstance().getDaoSession().getIMKeFuDao().findUserByImAccount(CxUserInfo.getHxAccount()) == null)
            IMDaoManager.getInstance().getDaoSession().getIMKeFuDao().insertOrReplace(imKeFu);
    }

    private void downLoadAudio(final CxMessage cxMessage)
    {
        String url = cxMessage.getFileMessage().getRemoteUrl();
        String fileName = cxMessage.getFileMessage().getName();
        if (url != null && url.startsWith("http"))
        {
            OkHttpUtils
                    .get()
                    .url(url)
                    .build()
                    .execute(new FileCallBack(Config.CACHE_VOICE_DIR, fileName)
                    {
                        @Override
                        public void inProgress(float progress)
                        {

                        }

                        @Override

                        public void onError(Request request, Exception e) throws Exception
                        {
                            SDLogUtil.debug("downloadError");
                            failQueue.add(0, cxMessage);
                        }

                        @Override
                        public void onResponse(File response) throws Exception
                        {
                            JSONObject jsonObject = new JSONObject(cxMessage.getBody());
                            if (jsonObject.has("localUrl"))
                            {
                                jsonObject.remove("localUrl");
                                jsonObject.put("localUrl", response.getAbsolutePath());
                            }
                            cxMessage.getImMessage().setBody(jsonObject.toString());

                            updateMsg(cxMessage);

                            CxIMObservable.getInstance().notifyUpdate(cxMessage);
                            if (imConversationDao != null)
                            {
                                //ifNotExistCreatConversation(cxMessage);
                                ifNotExistCreatConversation2(cxMessage);
                            }
                        }
                    });
        }
    }

    /**
     * 保存单聊数据到数据库
     */
    private void saveMsg(CxMessage msg)
    {
        try
        {
            IMDaoManager.getInstance().getDaoSession().getIMMessageDao().insert(CXMessageUtils.convertCXMessage(msg));
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void updateMsg(CxMessage msg)
    {
        IMDaoManager.getInstance().getDaoSession().getIMMessageDao().updateMsgByMsgId(CXMessageUtils.convertCXMessage(msg));
    }

    private void ifNotExistCreatConversation2(CxMessage cxMessage)
    {
        if (cxMessage.getDirect() == CxIMMessageDirect.RECEIVER)
        {
            if (fixLinkHashMap.isEmpty())
            {
                //addToConversation(cxMessage);
                toCreateOrUpdate(cxMessage);
            } else
            {
                if (fixLinkHashMap.isLtMaxSize())
                {
                    if (cxMessage.getType() == CxIMMessageType.SINGLE_CHAT.getValue())
                    {
                        if (fixLinkHashMap.containsKey(cxMessage.getImMessage().getHeader().getFrom()))
                        {//比较下缓存里面是否有，
                            toCreateOrUpdate(cxMessage);
                        } else
                        {
                            toCreateOrUpdate(cxMessage);
                        }
                    } else if (cxMessage.getType() == GROUP_CHAT.getValue())
                    {
                        IMGroup imGroup = IMDaoManager.getInstance().getDaoSession().getIMGroupDao().
                                loadGroupFromGroupId(cxMessage.getImMessage().getHeader().getGroupId());
                        if (StringUtils.notEmpty(imGroup))
                        {
                            if (fixLinkHashMap.containsKey(String.valueOf(cxMessage.getImMessage().getHeader().getGroupId())))
                            {
                                updateConversationLastMsg(cxMessage);
                            } else
                            {
                                addToConversation(cxMessage);
                            }
                        }
                    }
                } else
                {
                    if (cxMessage.getType() == CxIMMessageType.SINGLE_CHAT.getValue())
                    {
                        if (fixLinkHashMap.containsKey(cxMessage.getImMessage().getHeader().getFrom()))
                        {
                            // updateConversationLastMsg(cxMessage);
                            toCreateOrUpdate(cxMessage);
                        } else
                        {
                            updateOrAddByDb(cxMessage);
                        }
                    } else if (cxMessage.getType() == GROUP_CHAT.getValue())
                    {
                        if (fixLinkHashMap.containsKey(String.valueOf(cxMessage.getImMessage().getHeader().getGroupId())))
                        {
                            updateConversationLastMsg(cxMessage);
                        } else
                        {
                            updateOrAddByDb(cxMessage);
                        }
                    }
                }
            }

            if (CxReceiverMsgCallback != null)
            {
                CxReceiverMsgCallback.onReceiver(cxMessage);
            }
        }
    }

    private void toCreateOrUpdate(CxMessage cxMessage)
    {
        IMConversation imConversation = IMDaoManager.getInstance().getDaoSession()
                .getIMConversationDao().loadByUserName(cxMessage.getImMessage().getHeader().getFrom());
        if (StringUtils.notEmpty(imConversation))
        {
            updateConversationLastMsg(cxMessage);
        } else
        {
            addToConversation(cxMessage);
        }
    }


    /**
     * 会话不存在则创建
     *
     * @param cxMessage
     */
//    private void ifNotExistCreatConversation(CxMessage cxMessage)
//    {
//        if (cxMessage.getDirect() == CxIMMessageDirect.RECEIVER)
//        {
//
//            if (fixLinkHashMap.isEmpty())
//            {
//                addToConversation(cxMessage);
//            }
////            else if (cxMessage.getType() == CxIMMessageType.SINGLE_CHAT.getValue())
////            {
////                if (!fixLinkHashMap.containsKey(cxMessage.getImMessage().getHeader().getFrom()))
////                {
////                    addToConversation(cxMessage);
////                }
////            }
//            else
//            {
//                if (fixLinkHashMap.isLtMaxSize())
//                {
//                    if (cxMessage.getType() == CxIMMessageType.SINGLE_CHAT.getValue())
//                    {
//                        if (fixLinkHashMap.containsKey(cxMessage.getImMessage().getHeader().getFrom()))
//                        {//比较下缓存里面是否有，
//                            IMConversation imConversation = IMDaoManager.getInstance().getDaoSession()
//                                    .getIMConversationDao().loadByUserName(cxMessage.getImMessage().getHeader().getFrom());
//                            if (StringUtils.notEmpty(imConversation))
//                            {
//                                updateConversationLastMsg(cxMessage);
//                            } else
//                            {
//                                addToConversation(cxMessage);
//                            }
//                        } else
//                        {
//                            addToConversation(cxMessage);
//                        }
//                    } else if (cxMessage.getType() == CxIMMessageType.GROUP_CHAT.getValue())
//                    {
//                        IMVoiceGroup imVoiceGroup = IMDaoManager.getInstance().getDaoSession().getIMVoiceGroupDao().
//                                loadVoiceGroupFromGroupId(cxMessage.getImMessage().getHeader().getGroupId());
//                        if (StringUtils.empty(imVoiceGroup))
//                        {
//                            if (fixLinkHashMap.containsKey(String.valueOf(cxMessage.getImMessage().getHeader().getGroupId())))
//                            {
//                                updateConversationLastMsg(cxMessage);
//                            } else
//                            {
//                                addToConversation(cxMessage);
//                            }
//                        }
//                    }
//
//                } else
//                {
//                    if (cxMessage.getType() == CxIMMessageType.SINGLE_CHAT.getValue())
//                    {
//                        if (fixLinkHashMap.containsKey(cxMessage.getImMessage().getHeader().getFrom()))
//                        {
//                            updateConversationLastMsg(cxMessage);
//                        } else
//                        {
//                            updateOrAddByDb(cxMessage);
//                        }
//                    } else if (cxMessage.getType() == CxIMMessageType.GROUP_CHAT.getValue())
//                    {
//                        if (fixLinkHashMap.containsKey(String.valueOf(cxMessage.getImMessage().getHeader().getGroupId())))
//                        {
//                            updateConversationLastMsg(cxMessage);
//                        } else
//                        {
//                            updateOrAddByDb(cxMessage);
//                        }
//                    }
//                }
//            }
//
//            if (CxReceiverMsgCallback != null)
//            {
//                CxReceiverMsgCallback.onReceiver(cxMessage);
//            }
//        }
//    }

    /**
     * 通过数据库查询是否存在进行添加或更新会话
     *
     * @param cxMessage
     */
    private void updateOrAddByDb(CxMessage cxMessage)
    {
        IMConversation conversation = loadConversation(cxMessage);
        if (conversation == null)
        {
            //数据库不存在
            addToConversation(cxMessage);
        } else
        {
            //数据库存在
            updateConversationLastMsg(conversation, cxMessage);
        }
    }

    /**
     * 初始化数据
     */
    public static void initData()
    {
        if (fixLinkHashMap != null)
        {
            return;
        } else
        {
            //----------------初始化会话数据----------
            SDLogUtil.debug("=====initData=====");
            fixLinkHashMap = new CxFixLinkHashMap<>(Config.CONVERSATION_MAX_SIZE);
            //--------------初始化语音下载失败数据
            failQueue = new ArrayList<>();
            try
            {
                SDLogUtil.debug("im_的IMDaoManager.getInstance().getDaoSession().getIMConversationDao()：" + IMDaoManager
                        .getInstance().getDaoSession().getIMConversationDao());
                imConversationDao = IMDaoManager.getInstance().getDaoSession().getIMConversationDao();
                groupDao = IMDaoManager.getInstance().getDaoSession().getIMGroupDao();
                List<IMConversation> imConversations = imConversationDao.loadAll();
                if (imConversations != null)
                {
                    for (IMConversation conversation : imConversations)
                    {
                        fixLinkHashMap.put(conversation.getUsername(), conversation);
                    }
                }

                imAudioFailDao = IMDaoManager.getInstance().getDaoSession().getIMAudioFailDao();
                imMessageDao = IMDaoManager.getInstance().getDaoSession().getIMMessageDao();
                List<IMMessage> imMessages = imMessageDao.findAllAudioFailMsg(imAudioFailDao.loadAll());
                if (imMessages != null)
                {
                    List<CxMessage> audioFailMsg = CXMessageUtils.covertIMMessage(imMessages);
                    failQueue.addAll(audioFailMsg);
                }
            } catch (Exception e)
            {
                SDLogUtil.debug("Im_会话初始化_出错。");
            }

        }
    }

    private IMConversation loadConversation(CxMessage cxMessage)
    {
        IMConversation conversation = null;
        if (cxMessage.getType() == CxIMMessageType.SINGLE_CHAT.getValue())
        {
            conversation = imConversationDao.loadByUserName(cxMessage.getImMessage().getHeader().getFrom());
        } else if (cxMessage.getType() == GROUP_CHAT.getValue())
        {
            conversation = imConversationDao.loadByUserName(String.valueOf(cxMessage.getImMessage().getHeader().getGroupId()));
        }
        return conversation;
    }

    /**
     * 更新会话最后一条消息
     *
     * @param cxMessage
     */
    private void updateConversationLastMsg(CxMessage cxMessage)
    {
        IMConversation conversation = null;
        if (cxMessage.getType() == CxIMMessageType.SINGLE_CHAT.getValue())
        {
            conversation = fixLinkHashMap.get(cxMessage.getImMessage().getHeader().getFrom());
            //这里搜索会话的列表找出对应的对象
            IMConversation imConversation = IMDaoManager.getInstance().getDaoSession()
                    .getIMConversationDao().loadByUserName(cxMessage.getImMessage().getHeader().getFrom());
            if (StringUtils.notEmpty(imConversation))
            {
                if (StringUtils.notEmpty(conversation))
                    conversation.setUnReadMsg(imConversation.getUnReadMsg() + 1);
            }
        } else if (cxMessage.getType() == GROUP_CHAT.getValue())
        {
            conversation = fixLinkHashMap.get(String.valueOf(cxMessage.getImMessage().getHeader().getGroupId()));
            IMConversation imConversation = IMDaoManager.getInstance().getDaoSession()
                    .getIMConversationDao().loadByUserName(cxMessage.getImMessage().getHeader().getGroupId());
            if (StringUtils.notEmpty(imConversation))
            {
                if (StringUtils.notEmpty(conversation))
                    conversation.setUnReadMsg(imConversation.getUnReadMsg() + 1);
            }
        }

        if (conversation != null)
        {
            if (cxMessage.getType() == CxIMMessageType.SINGLE_CHAT.getValue())
            {
                updateConversationLastMsg(conversation, cxMessage);
            } else if (cxMessage.getType() == GROUP_CHAT.getValue())
            {
                updateConversationLastMsgGroup(conversation, cxMessage);
            }
        }
    }

    /**
     * 更新会话最后一条消息
     *
     * @param conversation
     */
    private void updateConversationLastMsg(IMConversation conversation, CxMessage cxMessage)
    {
        if (conversation == null)
        {
            return;
        }
        conversation.setIMMessage(CXMessageUtils.convertCXMessage(cxMessage));
        conversation.setMessageId(cxMessage.getImMessage().getHeader().getMessageId());
        conversation.setUnReadMsg(conversation.getUnReadMsg());
        conversation.setUpdateTime(new Date(cxMessage.getCreateTimeMillisecond()));
        saveConversation(null, cxMessage.getImMessage().getHeader().getFrom(), cxMessage.getImMessage().getHeader().getFrom(),
                cxMessage.getImMessage().getHeader().getMessageId(), conversation.getUnReadMsg(), cxMessage.getType(), new Date
                        (cxMessage.getCreateTimeMillisecond()));
        CxConversationObserable.getInstance().notifyUpdate(new CxWrapperConverstaion(CxWrapperConverstaion.OperationType
                .UPDATE_CONVERSTAION, conversation));
    }

    private void updateConversationLastMsgGroup(IMConversation conversation, CxMessage cxMessage)
    {
        if (conversation == null)
        {
            return;
        }
        conversation.setIMMessage(CXMessageUtils.convertCXMessage(cxMessage));
        conversation.setMessageId(cxMessage.getImMessage().getHeader().getMessageId());
        conversation.setUnReadMsg(conversation.getUnReadMsg());
        conversation.setUpdateTime(new Date(cxMessage.getCreateTimeMillisecond()));
        //imConversationDao.update(conversation);
        saveConversation(null, conversation.getUsername(), conversation.getTitle(), cxMessage.getImMessage().getHeader()
                        .getMessageId(),
                conversation.getUnReadMsg(), cxMessage.getType(), new Date(cxMessage.getCreateTimeMillisecond()));
        CxConversationObserable.getInstance().notifyUpdate(new CxWrapperConverstaion(CxWrapperConverstaion.OperationType
                .UPDATE_CONVERSTAION, conversation));
    }

    /**
     * 添加一个新的会话
     *
     * @param cxMessage
     */
    private void addToConversation(CxMessage cxMessage)
    {
        try
        {
//            IMConversation conversation = createConversation(cxMessage);
//            imConversationDao.insertOrReplace(conversation);
//            fixLinkHashMap.put(conversation.getUsername(), conversation);
            //新开
            String tmpUserName = cxMessage.getImMessage().getHeader().getFrom();
            Map<String, String> attachmentMap = new HashMap<String, String>();
            attachmentMap = cxMessage.getImMessage().getHeader().getAttachment();
            if (attachmentMap.get("MyInfo") != null)
                if (attachmentMap.size() > 0 && attachmentMap.get("MyInfo").length() > 0)
                {
                    final CxUserInfoToKefuEntity CxUserInfo = CxUserInfoToKefuEntity.parse(attachmentMap.get("MyInfo"));
                    if (StringUtils.notEmpty(CxUserInfo))
                    {
                        if (StringUtils.notEmpty(CxUserInfo.getRealName()))
                            tmpUserName = CxUserInfo.getRealName();
                    }
                }

            IMConversation conversation = null;
            if (cxMessage.getType() == CxIMMessageType.SINGLE_CHAT.getValue())
            {
                conversation = saveConversation(null, cxMessage.getImMessage().getHeader().getFrom(),
                        tmpUserName, cxMessage.getImMessage().getHeader().getMessageId(), 1, cxMessage.getType(), null);
            } else if (cxMessage.getType() == GROUP_CHAT.getValue())
            {
                IMGroup imGroup = IMDaoManager.getInstance().getDaoSession().getIMGroupDao().
                        loadGroupFromGroupId(cxMessage.getImMessage().getHeader().getGroupId());
                if (StringUtils.notEmpty(imGroup))
                {
                    conversation = saveConversation(null, imGroup.getGroupId(),
                            imGroup.getGroupName(), cxMessage.getImMessage().getHeader().getMessageId(), 1, cxMessage.getType()
                            , null);
                }
            }
            if (conversation != null)
                CxConversationObserable.getInstance().notifyUpdate(new CxWrapperConverstaion(CxWrapperConverstaion
                        .OperationType.ADD_CONVERSTAION, conversation));
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 保存会话
     * @param identity
     */
    public IMConversation saveConversation(IMConversation conversation, String identity, String title,
                                           String lastMsgId, int unReadMsg, int type, Date updateTime)
    {
        if (conversation == null)
        {
            conversation = new IMConversation();
        }
        if (updateTime == null)
        {
            updateTime = new Date();
        }

        conversation.setUnReadMsg(unReadMsg);
        conversation.setMessageId(lastMsgId);
        conversation.setCreateTime(new Date());
        conversation.setTitle(title);
        conversation.setType(type);
        conversation.setUsername(identity);
        conversation.setUpdateTime(updateTime);
        conversation.setId(IMDaoManager.getInstance().getDaoSession().getIMConversationDao().insertOrReplace(conversation));
        if (CxChatCxBaseProcessor.fixLinkHashMap != null)
        {
            CxChatCxBaseProcessor.fixLinkHashMap.put(conversation.getUsername(), conversation);
        }
        return conversation;
    }

    private IMConversation createConversation(CxMessage cxMessage)
    {
        IMConversation conversation = new IMConversation();
        conversation.setMessageId(cxMessage.getImMessage().getHeader().getMessageId());
        conversation.setCreateTime(new Date());
        conversation.setUpdateTime(new Date());
        conversation.setUnReadMsg(1);
        if (cxMessage.getType() == CxIMMessageType.SINGLE_CHAT.getValue())
        {
            conversation.setUsername(cxMessage.getImMessage().getHeader().getFrom());
            conversation.setTitle(cxMessage.getImMessage().getHeader().getFrom());
        } else if (cxMessage.getType() == GROUP_CHAT.getValue())
        {
            String groupId = cxMessage.getImMessage().getHeader().getGroupId();
            conversation.setUsername(groupId);
            IMGroup imGroup = groupDao.load(groupId);
            if (imGroup != null)
            {
                conversation.setTitle(imGroup.getGroupName());
            }
        }
        conversation.setType(cxMessage.getType());
        return conversation;
    }

    public void setCurrentAccount(String currentAccount)
    {
        this.currentAccount = currentAccount;
    }

    public CxReceiverMsgCallback getCxReceiverMsgCallback()
    {
        return CxReceiverMsgCallback;
    }

    public void setCxReceiverMsgCallback(CxReceiverMsgCallback cxReceiverMsgCallback)
    {
        this.CxReceiverMsgCallback = cxReceiverMsgCallback;
    }


    private void sendIos(int imType, int type, String text, String toImAccount)
    {
        StringBuffer buffer = new StringBuffer(Constants.API_SERVER_URL);
        String url = "";
        if (imType == 1)
        {
            url = buffer
                    .append("/im/chat/push.json")
                    .toString();
        } else if (imType == 2)
        {
            url = buffer
                    .append("/im/chat/group/push.json")
                    .toString();
        }

        String token = com.chaoxiang.base.utils.SPUtils.get(context, com.chaoxiang.base.utils.SPUtils.ACCESS_TOKEN2, "")
                .toString();

        OkHttpUtils
                .post()//
                .addParams("type", type + "")
                .addParams("text", text)
                .addParams("toImAccount", toImAccount)
                .addHeader("token", token)
                .url(url)//
                .build()//
                .execute(new JsonCallback()
                {
                    @Override
                    public void onError(Request request, Exception e)
                    {

                    }

                    @Override
                    public void onResponse(JSONObject response) throws Exception
                    {
                        try
                        {
                            BaseBean baseBean = SDGson.toObject(response.toString(), BaseBean.class);
                            SDLogUtil.debug("ok-http:" + baseBean.getMsg());
                        } catch (Exception e)
                        {

                        }
                    }
                });
    }

}
