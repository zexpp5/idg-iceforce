package com.superdata.im.entity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;

import com.chaoxiang.base.config.Config;
import com.chaoxiang.base.utils.BitmapUtils;
import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.SPUtils;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.entity.IMDaoManager;
import com.chaoxiang.imrestful.OkHttpUtils;
import com.google.gson.Gson;
import com.im.client.MediaType;
import com.im.client.core.ChatManager;
import com.im.client.struct.IMMessage;
import com.im.client.struct.IMMessageProtos;
import com.superdata.im.callback.CxFileUploadCallback;
import com.superdata.im.callback.CxSendMsgCallback;
import com.superdata.im.constants.CxIMMessageStatus;
import com.superdata.im.constants.CxIMMessageType;
import com.superdata.im.constants.CxSPIMKey;
import com.superdata.im.handle.CxSendingMsgHandle;
import com.superdata.im.manager.CxServerManager;
import com.superdata.im.utils.CXMessageUtils;
import com.superdata.im.utils.CxSendMsgWatcher;
import com.superdata.im.utils.Observable.CxIMObservable;
import com.superdata.im.utils.Observable.CxNetWorkObservable;

import java.io.File;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-01-05
 * @desc 消息类型
 */
public class CxMessage extends CxSendMsgCallback implements Parcelable
{
    private long id;
    private IMMessage imMessage;
    /**
     * 消息类型(单聊,群聊等类型),对应IMMessageType
     */
    private int type;
    /**
     * 消息状态
     */
    private int status;
    /**
     * 用于监听消息状态
     */
    private CxSendMsgWatcher sendMsgWatcher;
    /**
     * 接收还是发送,对应IMMessageDirect,默认未发送
     */
    private int direct;
    /**
     * 外部监听消息状态回调
     */
    private CxSendMsgCallback callback;

    private SDGson mGson;
    /**
     * 从发次数
     */
    private int reSendCount = 1;
    private int reSendFailCount = 1;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 是否已读
     */
    private boolean isReader;

    /**
     * 是否阅后即焚
     */
    private boolean isHotChat;

    /**
     * 是否阅后即焚可见
     */
    private boolean isHotChatVisible;

    /**
     * 阅后即焚倒计时
     */
    private int hotTime;

    private boolean isReaderStatus;

    public CxMessage()
    {
    }

    public CxMessage(IMMessage imMessage)
    {
        this.imMessage = imMessage;
    }

    public CxMessage(IMMessage imMessage, int mediaType)
    {
        this.imMessage = imMessage;
        setMediaType(mediaType);
    }

    public CxMessage(IMMessage imMessage, int mediaType, CxSendMsgCallback callback)
    {
        this.imMessage = imMessage;
        setMediaType(mediaType);
        this.callback = callback;
    }

    protected CxMessage(Parcel in)
    {
        id = in.readLong();
        type = in.readInt();
        status = in.readInt();
        direct = in.readInt();
        reSendCount = in.readInt();
        reSendFailCount = in.readInt();
        createTime = in.readString();
        isReader = in.readByte() != 0;
        isHotChat = in.readByte() != 0;
        isHotChatVisible = in.readByte() != 0;
        hotTime = in.readInt();
    }


    @Override
    public void socketTimeOut(String msgId)
    {
        if (reSendFailCount >= Config.RESEND_COUNT / 2)
        {
            reSendFailCount = 0;
            CxNetWorkObservable.getInstance().notifyUpdate(CxNetworkStatus.DISCONNECTION_SERVER);
        }

        if (reSendCount >= Config.RESEND_COUNT && this.status != CxIMMessageStatus.FAIL)
        {
            SDLogUtil.debug("update====>socketTimeOut");
            updateStatus(msgId, getCreateTimeMillisecond(), CxIMMessageStatus.TIME_OUT);
            if (callback != null)
            {
                callback.socketTimeOut(msgId);
            }
            reSendCount = 1;
        } else
        {
            reSendCount++;
            reSendFailCount++;
            try
            {
                IMMessageProtos.IMMessage textMsg;
                if (getType() == CxIMMessageType.SINGLE_CHAT.getValue())
                {
                    textMsg = ChatManager.getInstance().buildSingleChatReq(imMessage.getHeader().getTo(), getBody(),
                            getMediaType(), msgId);
                } else
                {
                    textMsg = ChatManager.getInstance().buildGroupChatReq(imMessage.getHeader().getGroupId(), getBody(),
                            getMediaType(), msgId);
                }
                ChatManager.getInstance().sendMessage(textMsg);
            } catch (Exception e)
            {
                e.printStackTrace();
                if (reSendCount >= Config.RESEND_COUNT)
                {
                    updateStatus(msgId, getCreateTimeMillisecond(), CxIMMessageStatus.FAIL);
                }
            }
        }
    }

    @Override
    public void reConSuccessAndReSendMsg(String msgId)
    {
        try
        {
            SDLogUtil.debug("im_进入重新发送信息的回调方法中！");
            com.chaoxiang.entity.chat.IMMessage imMessageTmp = IMDaoManager.getInstance().getDaoSession().getIMMessageDao()
                    .findMessageByMessageId(msgId);

            if (imMessageTmp != null)
            {
                SDLogUtil.debug("im_进入重新发送信息的回调方法中！====重发前的MsgStatus(状态)！" + imMessageTmp.getMsgStatus());
                if (imMessageTmp.getMsgStatus() != CxIMMessageStatus.SUCCESS && imMessageTmp.getMsgStatus() !=
                        CxIMMessageStatus.SENDING && imMessageTmp.getMsgStatus() != CxIMMessageStatus.FILE_SENDING)
                {
                    SDLogUtil.debug("im_进入重新发送信息的回调方法中！======重发！");
                    CxServerManager.getInstance().pushBinder.reSendChatMsg(CXMessageUtils.covertIMMessage(imMessageTmp));
                }
            }
//            IMMessageProtos.IMMessage textMsg;
//            if (getType() == CxIMMessageType.SINGLE_CHAT.getValue())
//            {
//                textMsg = ChatManager.getInstance().buildSingleChatReq(imMessage.getHeader().getTo(), getBody(), getMediaType
// (), msgId);
//            } else
//            {
//                textMsg = ChatManager.getInstance().buildGroupChatReq(imMessage.getHeader().getGroupId(), getBody(),
// getMediaType(), msgId);
//            }
//            ChatManager.getInstance().sendMessage(textMsg);

        } catch (Exception e)
        {
            e.printStackTrace();
            if (reSendCount >= Config.RESEND_COUNT)
            {
                updateStatus(msgId, getCreateTimeMillisecond(), CxIMMessageStatus.FAIL);
            }
        }
    }

    /**
     * 取消监听
     */
    public void cancelWatcher()
    {
        if (sendMsgWatcher != null)
        {
            sendMsgWatcher.cancel();
        }
    }

    /**
     * 更新状态
     */
    public void updateStatus(String msgId, long timeMillis, int status)
    {
        this.status = status;
        imMessage.getHeader().setMessageId(msgId);
        setMsgTime(timeMillis);
        cancelWatcher();
        IMDaoManager.getInstance().getDaoSession().getIMMessageDao().updateMsgByMsgId(CXMessageUtils.convertCXMessage(this));
        CxIMObservable.getInstance().notifyUpdate(this);
        CxSendingMsgHandle.getInstance().createAndSendMsg(this, CxSendingMsgHandle.REMOVE);
    }

    @Override
    public void success(String msgId, long timeMillis)
    {
        SDLogUtil.debug("update====>success");
        updateStatus(msgId, timeMillis, CxIMMessageStatus.SUCCESS);
        if (callback != null)
        {
            callback.success(msgId, timeMillis);
        }
    }

    @Override
    public void error(String msgId)
    {
        SDLogUtil.debug("update====>error");
        updateStatus(msgId, getCreateTimeMillisecond(), CxIMMessageStatus.FAIL);
        if (callback != null)
        {
            callback.error(msgId);
        }
    }

    private void setMsgTime(long timeMillis)
    {
        setCreateTimeMillisecond(timeMillis);
        setCreateTime(DateUtils.getTimestampString(timeMillis));
    }

    /**
     * 开启监听
     */
    public void startWatcher()
    {
        if (status == CxIMMessageStatus.SENDING)
        {
            if (getMediaType() == MediaType.TEXT.value() || getMediaType() == MediaType.POSITION.value())
            {
                CxSendingMsgHandle.getInstance().createAndSendMsg(this, CxSendingMsgHandle.ADD);
            }

            if (StringUtils.notEmpty(imMessage.getHeader().getGroupId()))
            {
                sendMsgWatcher = new CxSendMsgWatcher(imMessage.getHeader().getGroupId(), imMessage.getHeader().getMessageId(),
                        this);
            } else
            {
                sendMsgWatcher = new CxSendMsgWatcher(imMessage.getHeader().getMessageId(), this);
            }
            sendMsgWatcher.startTimer();
        }
    }

    /**
     * 上传附件类型的消息
     */
    public void updateFileMsg(final Context context, final String path, final CxFileUploadCallback fileUploadCallback)
    {
        //上传附件到服务器
        CxSendingMsgHandle.getInstance().createAndSendMsg(this, CxSendingMsgHandle.ADD);
        if (getMediaType() == MediaType.PICTURE.value())
        {
            //图片先压缩再上传
            AsyncTask task = new AsyncTask()
            {
                @Override
                protected Object doInBackground(Object[] params)
                {
                    String tempPath = Config.CACHE_TEMP_IMG_DIR + File.separator + "cx_" + System.currentTimeMillis() + ".jpg";
                    BitmapUtils.compressImageAndSave(context, path, tempPath);
                    return tempPath;
                }

                @Override
                protected void onPostExecute(Object o)
                {
                    String path = (String) o;
                    uploadFile(context, fileUploadCallback, path);
                }
            };
            task.execute();
        } else
        {
            //上传不是图片的文件
            uploadFile(context, fileUploadCallback, path);
        }
    }

    /**
     * @param fileUploadCallback
     * @param path
     * @param视频发送请求（所有文件的上传方法、仅限于上传到微软云）
     */
    private void uploadFile(Context context, final CxFileUploadCallback fileUploadCallback, String path)
    {
        String uid = (String) SPUtils.get(context, CxSPIMKey.STRING_ACCOUNT, "");
        //调用微软云的上传接口
        OkHttpUtils.AzureClient.getInstance().uploadFile(uid, path, new OkHttpUtils.UploadFileListener()
        {
            @Override
            public void onSuccess(String remoteUrl, String localUrl, String fileName, long fileSize)
            {
                if (fileUploadCallback != null)
                {
                    //remoteUrl 特指微软云的存放地址。
                    fileUploadCallback.onSuccess(new CxFileMessage(fileName, fileSize, remoteUrl, localUrl, 0));
                }
            }

            @Override
            public void onError()
            {
                error(imMessage.getHeader().getMessageId());
                fileUploadCallback.onError();
            }
        });
    }


    public void setCallback(CxSendMsgCallback callback)
    {
        this.callback = callback;
    }

    public void setImMessage(IMMessage imMessage)
    {
        this.imMessage = imMessage;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getMediaType()
    {
        return imMessage.getHeader().getMediaType();
    }

    public void setMediaType(int mediaType)
    {
        imMessage.getHeader().setMediaType(mediaType);
    }

    public int getDirect()
    {
        return direct;
    }

    public void setDirect(int direct)
    {
        this.direct = direct;
    }

    public IMMessage getImMessage()
    {
        return imMessage;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public long getCreateTimeMillisecond()
    {
        return imMessage.getHeader().getCreateTime();
    }

    public void setCreateTimeMillisecond(long createTime)
    {
        imMessage.getHeader().setCreateTime(createTime);
    }

    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    public boolean isHotChat()
    {
        return isHotChat;
    }

    public void setHotChat(boolean hotChat)
    {
        isHotChat = hotChat;
    }

    public boolean isHotChatVisible()
    {
        return isHotChatVisible;
    }

    public void setHotChatVisible(boolean hotChatVisible)
    {
        isHotChatVisible = hotChatVisible;
    }

    public int getHotTime()
    {
        return hotTime;
    }

    public void setHotTime(int hotTime)
    {
        this.hotTime = hotTime;
    }

    public String getBody()
    {
        if (imMessage != null)
        {
            return imMessage.getBody();
        }
        return "";
    }

    public CxFileMessage getFileMessage()
    {
        if (getMediaType() != MediaType.TEXT.value() && getMediaType() != MediaType.POSITION.value())
        {
            if (mGson == null)
            {
                mGson = new SDGson();
            }
            return mGson.fromJson(getBody(), CxFileMessage.class);
        }
        return null;
    }

    public CxGeoMessage getGeoMessage()
    {
        if (getMediaType() == MediaType.POSITION.value())
        {
            if (mGson == null)
            {
                mGson = new SDGson();
            }
            return mGson.fromJson(getBody(), CxGeoMessage.class);
        }
        return null;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CxMessage cxMessage = (CxMessage) o;

        if (imMessage != null && imMessage.getHeader() != null
                && cxMessage.getImMessage() != null
                && cxMessage.getImMessage().getHeader() != null
                && imMessage.getHeader().getMessageId() == cxMessage.getImMessage().getHeader().getMessageId())
        {
            return true;
        } else
        {
            return false;
        }
    }

//    @Override
//    public int hashCode()
//    {
//        int result = (int) (id ^ (id >>> 32));
//        result = (int) (31 * result + imMessage.getHeader().getMessageId());
//        return result;
//    }

    public boolean isReader()
    {
        return isReader;
    }

    public void setReader(boolean reader)
    {
        isReader = reader;
    }

    public boolean isReaderStatus()
    {
        return isReaderStatus;
    }

    public void setReaderStatus(boolean readerStatus)
    {
        isReaderStatus = readerStatus;
    }


    /**
     * 根据实体类返回一串json字符串
     */
    public static String returnJsonString(CxMessage cxMessage)
    {
        String jsonObjectString = new Gson().toJson(cxMessage).toString();
        return jsonObjectString;
    }

    @Override
    public String toString()
    {
        return "CxMessage{" +
                "id=" + id +
                ", imMessage=" + imMessage +
                ", type=" + type +
                ", status=" + status +
                ", sendMsgWatcher=" + sendMsgWatcher +
                ", direct=" + direct +
                ", callback=" + callback +
                ", mGson=" + mGson +
                ", reSendCount=" + reSendCount +
                ", reSendFailCount=" + reSendFailCount +
                ", createTime='" + createTime + '\'' +
                ", isReader=" + isReader +
                ", isHotChat=" + isHotChat +
                ", isHotChatVisible=" + isHotChatVisible +
                ", hotTime=" + hotTime +
                '}';
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        out.writeLong(id);
        out.writeInt(type);
        out.writeInt(status);
        out.writeValue(imMessage);
        out.writeInt(direct);
        out.writeByte((byte) (isReader ? 1 : 0));
        out.writeByte((byte) (isHotChat ? 1 : 0));
        out.writeByte((byte) (isHotChatVisible ? 1 : 0));
        out.writeInt(hotTime);
        out.writeString(createTime);
        out.writeInt(reSendFailCount);
        out.writeInt(reSendCount);
    }

    public static final Creator<CxMessage> CREATOR = new Creator<CxMessage>()
    {
        @Override
        public CxMessage createFromParcel(Parcel in)
        {
            CxMessage cxMessage = new CxMessage();
            cxMessage.id = in.readLong();
            cxMessage.type = in.readInt();
            cxMessage.status = in.readInt();
            cxMessage.imMessage = (IMMessage) in.readValue(IMMessage.class.getClassLoader());
            cxMessage.direct = in.readInt();
            cxMessage.isReader = (in.readByte() == 1 ? true : false);
            cxMessage.isHotChat = (in.readByte() == 1 ? true : false);
            cxMessage.isHotChatVisible = (in.readByte() == 1 ? true : false);
            cxMessage.hotTime = in.readInt();
            cxMessage.createTime = in.readString();
            cxMessage.reSendFailCount = in.readInt();
            cxMessage.reSendCount = in.readInt();
            return cxMessage;
        }

        @Override
        public CxMessage[] newArray(int size)
        {
            return new CxMessage[size];
        }
    };
}
