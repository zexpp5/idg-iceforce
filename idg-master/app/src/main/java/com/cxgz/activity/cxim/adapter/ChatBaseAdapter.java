package com.cxgz.activity.cxim.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.entity.conversation.IMConversation;
import com.chaoxiang.imsdk.conversation.IMConversaionManager;
import com.cxgz.activity.cxim.BigCountDownImgActivity;
import com.cxgz.activity.cxim.person.NotFriendPersonalInfoActivity;
import com.cxgz.activity.cxim.PersonalInfoActivity;
import com.cxgz.activity.cxim.manager.SocketManager;
import com.cxgz.activity.cxim.utils.InfoUtils;
import com.cxgz.activity.cxim.utils.SDTimerTask;
import com.cxgz.activity.db.dao.SDAllConstactsDao;
import com.cxgz.activity.db.dao.SDAllConstactsEntity;
import com.im.client.struct.IMMessage;
import com.injoy.idg.R;
import com.bumptech.glide.Glide;
import com.cc.emojilibrary.EmojiconTextView;
import com.chaoxiang.base.config.Config;
import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.SPUtils;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.entity.IMDaoManager;
import com.chaoxiang.entity.chat.IMKeFu;
import com.chaoxiang.imrestful.OkHttpUtils;
import com.chaoxiang.imrestful.callback.FileCallBack;
import com.chaoxiang.imsdk.chat.CXChatManager;
import com.chaoxiang.imsdk.chat.NoticeType;
import com.cx.webrtc.WebRtcClient;
import com.cxgz.activity.cxim.BaiduMapActivity;
import com.cxgz.activity.cxim.BigImgActivity;
import com.cxgz.activity.cxim.LocationActiviy;
import com.cxgz.activity.cxim.camera.BitmapUtil;
import com.cxgz.activity.cxim.camera.FileUtil;
import com.cxgz.activity.cxim.camera.main.PlayVideoActiviy;
import com.cxgz.activity.cxim.manager.AudioPlayManager;
import com.cxgz.activity.cxim.utils.FileUtill;
import com.cxgz.activity.cxim.utils.ParseNoticeMsgUtil;
import com.cxgz.activity.cxim.utils.SmileUtils;
import com.cxgz.activity.db.dao.SDUserDao;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.im.client.MediaType;
import com.squareup.okhttp.Request;
import com.superdata.im.constants.CxIMMessageDirect;
import com.superdata.im.constants.CxIMMessageStatus;
import com.superdata.im.constants.CxIMMessageType;
import com.superdata.im.constants.CxSPIMKey;
import com.superdata.im.entity.CxBaiDuEntity;
import com.superdata.im.entity.CxFileMessage;
import com.superdata.im.entity.CxGeoMessage;
import com.superdata.im.entity.CxMessage;
import com.superdata.im.entity.ShareDicEntity;
import com.superdata.im.handle.CxSendingMsgHandle;
import com.superdata.im.utils.CXMessageUtils;
import com.superdata.im.utils.CxGreenDaoOperateUtils;

import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import yunjing.utils.DisplayUtil;


/**
 * @version v1.0.0
 * @date 2016-02-18
 * @desc
 */
public abstract class ChatBaseAdapter<T extends CxMessage> extends BaseAdapter
{
    private List<CxMessage> cxMessagesUnread;
    //图片、语音、小视频点击已阅推送
    private List<CxMessage> cxMessagesReadList;

    protected List<T> datas;
    protected Context context;
    protected Activity mActivity;
    protected String currentAccount;

    /**
     * 所有的语音位置集合
     */
    protected ArrayList<Integer> audioPositionList = new ArrayList();
    /**
     * 当前语音播放位置
     */

    protected int currentPlayAduioPosition;
    /**
     * 是否自动播放语音
     */
    protected boolean isAutoPlayAudio;
    protected Map<Integer, View> audioPlayViewMap = new LinkedHashMap<>();

    protected int screenWidth;//屏幕宽度
    protected int maxVoiceWidth;//语音最大宽度
    protected int minVoiceWidth;//语音最小宽度
    protected float voicerate;//宽度比例

    protected OnAdapterListener onAdapterListener;

    private SDUserDao userDao;

    public ChatBaseAdapter(Context context, List<T> datas)
    {
        this.context = context;
        this.datas = datas;
        checkMsgStatus();
        currentAccount = (String) SPUtils.get(context, CxSPIMKey.STRING_ACCOUNT, "");
        userDao = new SDUserDao(context);
    }

    /**
     * 检测消息状态
     */
    public void checkMsgStatus()
    {
        List<CxMessage> sendingMsgList = CxSendingMsgHandle.getInstance().getMsgList();

        if (datas != null)
            for (CxMessage cxMessage : datas)
            {
                if (cxMessage.getStatus() == CxIMMessageStatus.FILE_SENDING)
                {
                    if (!sendingMsgList.contains(cxMessage))
                    {
                        cxMessage.setStatus(CxIMMessageStatus.FILE_UPLOAD_FAIL);
                    }
                } else if (cxMessage.getStatus() == CxIMMessageStatus.SENDING)
                {
                    if (!sendingMsgList.contains(cxMessage))
                    {
                        cxMessage.setStatus(CxIMMessageStatus.FAIL);
                    }
                }
            }
    }

    /**
     * 有多少个不同的布局
     */
    protected final int VIEW_COUNT = 19;
    /**
     * 文本发送类型
     */
    protected final int MESSAGE_TYPE_TEXT_SEND = 0;
    /**
     * 文本接收类型
     */
    protected final int MESSAGE_TYPE_TEXT_RECEIVER = 1;
    /**
     * 图片发送类型
     */
    protected final int MESSAGE_TYPE_IMAGE_SEND = 2;
    /**
     * 图片接收类型
     */
    protected final int MESSAGE_TYPE_IMAGE_RECEIVER = 3;
    /**
     * 地理位置发送类型
     */
    protected final int MESSAGE_TYPE_POSITION_SEND = 4;
    /**
     * 地理位置接收类型
     */
    protected final int MESSAGE_TYPE_POSITION_RECEIVER = 5;
    /**
     * 文件发送类型
     */
    protected final int MESSAGE_TYPE_FILE_SEND = 6;
    /**
     * 文件接收类型
     */
    protected final int MESSAGE_TYPE_FILE_RECEIVER = 7;
    /**
     * 语音接收类型
     */
    protected final int MESSAGE_TYPE_VOICE_SEND = 8;
    /**
     * 语音发送类型
     */
    protected final int MESSAGE_TYPE_VOICE_RECEIVER = 9;
    /**
     * 小视频发送类型
     */
    protected final int MESSAGE_TYPE_VIDEO_SEND = 10;
    /**
     * 小视频接收类型
     */
    protected final int MESSAGE_TYPE_VIDEO_RECEIVER = 11;
    /**
     * 消息通知类型
     */
    protected final int MESSAGE_TYPE_NOTICE = 12;
    /**
     * 语音聊天
     */
    protected final int MESSAGE_TYPE_VOICE_CHAT_SEND = 13;

    protected final int MESSAGE_TYPE_VOICE_CHAT_RECEIVER = 14;

    /**
     * 视频聊天
     */
    protected final int MESSAGE_TYPE_VIDEO_CHAT_SEND = 15;
    protected final int MESSAGE_TYPE_VIDEO_CHAT_RECEIVER = 16;

    /*
     *分享
     */
    protected final int MESSAGE_TYPE_SHARE_SEND = 17;
    protected final int MESSAGE_TYPE_SHARE_RECEIVER = 18;

    protected boolean isShareType(CxMessage cxMessage)
    {
        Map<String, String> attachmentMap = new HashMap<String, String>();
        attachmentMap = cxMessage.getImMessage().getHeader().getAttachment();
        boolean isShareType = false;
        try
        {
            if (attachmentMap != null)
                if (attachmentMap.get("shareDic") != null)
                {
                    if (attachmentMap.size() > 0 && attachmentMap.get("shareDic").length() > 0)
                    {
                        final ShareDicEntity shareDicEntity = ShareDicEntity.parse(attachmentMap.get("shareDic"));
                        if (StringUtils.notEmpty(shareDicEntity))
                        {
                            isShareType = true;
                        }
                    }
                }
        } catch (Exception e)
        {
            SDLogUtil.debug("im_分享判断异常！");
        }
        return isShareType;
    }

    @Override
    public int getItemViewType(int position)
    {
        if (datas != null && !datas.isEmpty())
        {
            CxMessage cxMessage = datas.get(position);
            //因为分享，所有增加代码复杂度
            if (isShareType(cxMessage))
            {
                if (cxMessage.getDirect() == CxIMMessageDirect.SEND)
                {
                    return MESSAGE_TYPE_SHARE_SEND;
                } else
                {
                    return MESSAGE_TYPE_SHARE_RECEIVER;
                }
            }

            int mediaType = cxMessage.getMediaType();
            if (mediaType == MediaType.TEXT.value())
            {
                //文本类型
                if (cxMessage.getDirect() == CxIMMessageDirect.SEND)
                {
                    return MESSAGE_TYPE_TEXT_SEND;
                } else
                {
                    return MESSAGE_TYPE_TEXT_RECEIVER;
                }
            } else if (mediaType == MediaType.PICTURE.value())
            {
                //图片类型
                if (cxMessage.getDirect() == CxIMMessageDirect.SEND)
                {
                    return MESSAGE_TYPE_IMAGE_SEND;
                } else
                {
                    return MESSAGE_TYPE_IMAGE_RECEIVER;
                }
            } else if (mediaType == MediaType.POSITION.value())
            {
                //位置类型
                if (cxMessage.getDirect() == CxIMMessageDirect.SEND)
                {
                    return MESSAGE_TYPE_POSITION_SEND;
                } else
                {
                    return MESSAGE_TYPE_POSITION_RECEIVER;
                }
            } else if (mediaType == MediaType.FILE.value())
            {
                //文件类型
                if (cxMessage.getDirect() == CxIMMessageDirect.SEND)
                {
                    return MESSAGE_TYPE_FILE_SEND;
                } else
                {
                    return MESSAGE_TYPE_FILE_RECEIVER;
                }
            } else if (mediaType == MediaType.AUDIO.value())
            {
                //语音类型
                if (cxMessage.getDirect() == CxIMMessageDirect.SEND)
                {
                    return MESSAGE_TYPE_VOICE_SEND;
                } else
                {
                    return MESSAGE_TYPE_VOICE_RECEIVER;
                }
            } else if (mediaType == MediaType.VIDEO.value())
            {
                if (cxMessage.getDirect() == CxIMMessageDirect.SEND)
                {
                    return MESSAGE_TYPE_VIDEO_SEND;
                } else
                {
                    return MESSAGE_TYPE_VIDEO_RECEIVER;
                }
            } else if (mediaType == NoticeType.NORMAL_TYPE)
            {
                //消息通知类型
                return MESSAGE_TYPE_NOTICE;
            } else if (mediaType == WebRtcClient.AUDIO_MEDIATYPE)
            {
                //语音聊天
                if (cxMessage.getDirect() == CxIMMessageDirect.SEND)
                {
                    return MESSAGE_TYPE_VOICE_CHAT_SEND;
                } else
                {
                    return MESSAGE_TYPE_VOICE_CHAT_RECEIVER;
                }
            } else if (mediaType == WebRtcClient.VIDEO_MEDIATYPE)
            {
                //视频聊天
                if (cxMessage.getDirect() == CxIMMessageDirect.SEND)
                {
                    return MESSAGE_TYPE_VIDEO_CHAT_SEND;
                } else
                {
                    return MESSAGE_TYPE_VIDEO_CHAT_RECEIVER;
                }
            }
        }
        return -1;
    }

    @Override
    public int getViewTypeCount()
    {
        return VIEW_COUNT;
    }

    public int getCount()
    {
        return datas != null ? datas.size() : 0;

    }

    @Override
    public Object getItem(int position)
    {
        if (datas != null && !datas.isEmpty())
        {
            if (position > datas.size() - 1 || position < 0)
            {
                return null;
            }
            return datas.get(position);
        }
        return null;
    }

    protected View createConverView(int position)
    {
        int itemType = getItemViewType(position);
        switch (itemType)
        {
            case MESSAGE_TYPE_TEXT_SEND:
                //文本
                return LayoutInflater.from(context).inflate(R.layout.cxim_row_sent_message, null);
            case MESSAGE_TYPE_TEXT_RECEIVER:
                return LayoutInflater.from(context).inflate(R.layout.cxim_row_received_message, null);
            case MESSAGE_TYPE_IMAGE_SEND:
                //图片
                return LayoutInflater.from(context).inflate(R.layout.cxim_row_sent_picture, null);
            case MESSAGE_TYPE_IMAGE_RECEIVER:
                return LayoutInflater.from(context).inflate(R.layout.cxim_row_received_picture, null);
            case MESSAGE_TYPE_POSITION_SEND:
                //位置
                return LayoutInflater.from(context).inflate(R.layout.cxim_row_sent_location, null);
            case MESSAGE_TYPE_POSITION_RECEIVER:
                return LayoutInflater.from(context).inflate(R.layout.cxim_row_received_location, null);
            case MESSAGE_TYPE_FILE_SEND:
                //文件类型
                return LayoutInflater.from(context).inflate(R.layout.cxim_row_sent_file, null);
            case MESSAGE_TYPE_FILE_RECEIVER:
                return LayoutInflater.from(context).inflate(R.layout.cxim_row_received_file, null);
            //语音类型
            case MESSAGE_TYPE_VOICE_SEND:
                return LayoutInflater.from(context).inflate(R.layout.cxim_row_sent_voice, null);
            case MESSAGE_TYPE_VOICE_RECEIVER:
                return LayoutInflater.from(context).inflate(R.layout.cxim_row_received_voice, null);
            //视频发送
            case MESSAGE_TYPE_VIDEO_SEND:
                return LayoutInflater.from(context).inflate(R.layout.cxim_row_sent_video, null);
            //视频接收
            case MESSAGE_TYPE_VIDEO_RECEIVER:
                return LayoutInflater.from(context).inflate(R.layout.cxim_row_received_video, null);
            //消息通知
            case MESSAGE_TYPE_NOTICE:
                return LayoutInflater.from(context).inflate(R.layout.cxim_row_notice_message, null);
            //语音通话和视频通话（VOICE-VIDEO）
            case MESSAGE_TYPE_VOICE_CHAT_SEND:
            case MESSAGE_TYPE_VIDEO_CHAT_SEND:
                return LayoutInflater.from(context).inflate(R.layout.cxim_row_sent_message_v, null);
            case MESSAGE_TYPE_VOICE_CHAT_RECEIVER:
            case MESSAGE_TYPE_VIDEO_CHAT_RECEIVER:
                return LayoutInflater.from(context).inflate(R.layout.cxim_row_received_message_v, null);
            //分享
            case MESSAGE_TYPE_SHARE_SEND:
                return LayoutInflater.from(context).inflate(R.layout.cxim_row_sent_share_message, null);
            case MESSAGE_TYPE_SHARE_RECEIVER:
                return LayoutInflater.from(context).inflate(R.layout.cxim_row_received_share_message, null);
        }
        return null;
    }

    protected class ViewHolder
    {
        /**
         * 发送时间/接收时间
         */
        public TextView timestamp;
        /**
         * 头像
         */
        public ImageView iv_userhead;
        /**
         * 内容
         */
        public EmojiconTextView tv_chatcontent;
        /**
         * 倒计时
         */
        public TextView tv_countdown;
        /**
         * 布局
         */
        public LinearLayout ll_container;
        /**
         * 已阅
         */
        public TextView tv_ack;
        /**
         * 发送中的进度条
         */
        public ProgressBar pb_sending;
        /**
         * 消息状态,失败时显示
         */
        public ImageView msg_status;
        /**
         * 图片
         */
        public ImageView iv_sendPicture;

        public ImageView iv_hot_see;
        public RelativeLayout row_recv_pic;
        public RelativeLayout row_send_pic;
        /**
         * 位置信息
         */
        public TextView tv_location;
        /**
         * 文件名
         */
        public TextView tv_file_name;
        /**
         * 文件大小
         */
        public TextView tv_file_size;
        /**
         * 文件发送状态
         */
        public TextView tv_file_state;
        public LinearLayout ll_file_container;
        /**
         * 文件发送进度
         */
        public TextView percentage;
        /**
         * 语音
         */
        public ImageView iv_voice;

        public RelativeLayout iv_voice_ly;

        /**
         * 语音长度
         */
        public TextView tv_length;
        /**
         * 视频图片
         */
        public ImageView chatting_content_iv;

        public TextView tv_notice_message;

        public TextView tv_userid;
        /**
         * 语音是否已阅
         */
        public ImageView iv_unread_voice;

        public RelativeLayout rl_content;
        public LinearLayout ll_hot_message_layout;

        //百度定位小图标
        public ImageView icon_baidu;

        public ImageView hot_not_lock_bg_img;
        public ImageView hot_lock_bg_img;

        //分享
        public ImageView img_icon_share;
        public TextView tv_share_title;
        public TextView tv_share_content;
        public LinearLayout ll_content;

        //聊天内容复制
        public RelativeLayout rl_content_new;

    }


    public List<T> getCxMessages()
    {
        return datas;
    }

    public void addCXMessage(T cxMessage)
    {
        datas.add(cxMessage);
    }

    public void addCXMessage(List<T> cxMessage)
    {
        this.datas.clear();
        this.datas.addAll(cxMessage);
    }

    /**
     * 获取最后一条消息
     *
     * @return
     */
    public CxMessage getLastMsg()
    {
        if (datas != null && !datas.isEmpty())
        {
            return (CxMessage) datas.get(datas.size() - 1);
        } else
        {
            return null;
        }
    }

    public CxMessage getFirstMsg()
    {
        if (datas != null && !datas.isEmpty())
        {
            return (CxMessage) datas.get(0);
        } else
        {
            return null;
        }
    }

    /**
     * 删除一条消息
     *
     * @param position
     * @return
     */
    public CxMessage removeMsg(int position)
    {
        CxMessage cxMessage = null;
        if (datas != null)
        {
            cxMessage = datas.remove(position);
        }
        return cxMessage;
    }

    public void removeMsgByMessage(CxMessage cxMessage)
    {
        if (null != cxMessage)
        {
            datas.remove(cxMessage);
        }
    }

    //更改开始读数状态
    public void updateMsgByMessage(CxMessage tmpCxMessage)
    {
        if (datas != null && datas.size() > 0)
        {
            for (CxMessage cxMessage : datas)
            {
                if (cxMessage.getImMessage().getHeader().getMessageId().equals(tmpCxMessage.getImMessage().getHeader()
                        .getMessageId()))
                {
                    cxMessage.setHotChatVisible(true);
                }
            }
        }
    }

    /**
     * 填充了发送信息方的个人数据。
     */
    protected void fillUserInfo(final CxMessage cxMessage, ViewHolder viewHolder)
    {
        if (cxMessage != null && viewHolder.iv_userhead != null)
        {
//            final SDUserEntity userInfo = userDao.findUserByImAccount(cxMessage.getImMessage().getHeader().getFrom());
            //替换全局的通讯录
            final SDAllConstactsEntity userInfo = SDAllConstactsDao.getInstance()
                    .findAllConstactsByImAccount(cxMessage.getImMessage().getHeader().getFrom());
            if (userInfo != null)
            {
                Glide.with(context)
                        .load(userInfo.getIcon())
                        .fitCenter()
                        .placeholder(R.mipmap.temp_user_head)
                        .error(R.mipmap.temp_user_head)
                        .crossFade()
                        .into(viewHolder.iv_userhead);

                viewHolder.iv_userhead.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (judgeIsFriend(String.valueOf(userInfo.getImAccount())))
                        {
                            //跳转到个人信息
                            Intent intent = new Intent(context, PersonalInfoActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(com.utils.SPUtils.USER_ID, userInfo.getImAccount());
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        } else
                        {
                            String str = getParams(String.valueOf(""),
                                    String.valueOf(userInfo.getImAccount()),
                                    userInfo.getName());

                            Intent intent = new Intent(context, NotFriendPersonalInfoActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(com.utils.SPUtils.USER_ACCOUNT, str);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
                    }
                });
            } else
            {
                IMKeFu userByImAccount = IMDaoManager.getInstance().getDaoSession().getIMKeFuDao().findUserByImAccount
                        (cxMessage.getImMessage().getHeader().getFrom());
                if (userByImAccount != null)
                {
                    if (userByImAccount.getIcon() != null)
                        Glide.with(context)
                                .load(userByImAccount.getIcon())
                                .fitCenter()
                                .placeholder(R.mipmap.temp_user_head)
                                .error(R.mipmap.temp_user_head)
                                .crossFade()
                                .into(viewHolder.iv_userhead);
                } else
                {
                    Glide.with(context)
                            .load("")
                            .fitCenter()
                            .placeholder(R.mipmap.temp_user_head)
                            .error(R.mipmap.temp_user_head)
                            .crossFade()
                            .into(viewHolder.iv_userhead);
                }
            }
        }
    }

    private boolean judgeIsFriend(String imAccount)
    {
        boolean isExist = false;
//        SDUserEntity userInfo = userDao.findUserByImAccount(imAccount);
        //替换全局的通讯录
        SDAllConstactsEntity userInfo = SDAllConstactsDao.getInstance()
                .findAllConstactsByImAccount(imAccount);
        if (StringUtils.notEmpty(userInfo))
            isExist = true;
        return isExist;
    }

    /**
     * 设置参数
     */
    private String getParams(String companyId, String imAccount, String userName)
    {
        String cxid = "cx:injoy365.cn";
        String urlString = appendString(companyId) + appendString(imAccount) + appendString(userName) + cxid;
        return urlString;
    }

    private String appendString(String appString)
    {
        String goUrl;
        StringBuilder stringInfo = new StringBuilder();
        goUrl = stringInfo.append(appString) + "&";
        return goUrl;
    }

    /**
     * 填充文字聊天
     */
    protected void fillPulbicTextChat(final CxMessage cxMessage, final ViewHolder viewHolder, final int position)
    {
        final Message message = delHandlerMessage.obtainMessage();
        viewHolder.tv_chatcontent.setText(SmileUtils.getSmiledText(context, cxMessage.getBody()));

        if (cxMessage.getDirect() == CxIMMessageDirect.SEND)
        {
            //发送
            if (cxMessage.isHotChat())
            {//如果是阅后即焚
                viewHolder.tv_chatcontent.setBackgroundResource(R.drawable.chatto_hot_bg);
            } else
            {
                viewHolder.tv_chatcontent.setBackgroundResource(R.drawable.chatto_bg);
            }
        } else
        {
            //接收
            IMMessage msg = cxMessage.getImMessage();
            if (getValueByKey("isBurnAfterRead", msg.getHeader().getAttachment()).equals("1"))
            {   //
                //停止计时器
                timerTask = taskHashMap.get(position);
                if (timerTask != null)
                {
                    timerTask.stop();
                    taskHashMap.remove(timerTask);
                }

                boolean isStart = cxMessage.isHotChatVisible();
                //判断是否已经开始过倒计时
                if (isStart)
                {
                    int countTime = cxMessage.getHotTime();
                    if (countTime > 0)
                    {//如果大于0就继续倒计时
                        initMessageTimeTask(cxMessage, countTime, viewHolder, position, message);
                    } else
                    {//等于0那么就直接删除
                        CxMessage delCxMessage = (CxMessage) getItem(position);
                        message.obj = delCxMessage;
                        message.arg1 = position;
                        delHandlerMessage.sendMessage(message);
                    }
                } else
                {
                    //如果是阅后即焚
                    viewHolder.tv_countdown.setVisibility(View.GONE);
                    viewHolder.ll_hot_message_layout.setVisibility(View.VISIBLE);
                    viewHolder.rl_content.setVisibility(View.GONE);
                    viewHolder.ll_hot_message_layout.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            initMessageTimeTask(cxMessage, 10, viewHolder, position, message);
                        }
                    });
                }
            } else
            {
                viewHolder.tv_countdown.setVisibility(View.GONE);
                viewHolder.ll_hot_message_layout.setVisibility(View.GONE);
                viewHolder.rl_content.setVisibility(View.VISIBLE);
            }
        }
        //同时 检测是否需要填充地址。
    }

    /**
     * 填充文字聊天
     */
    protected void fillPulbicTextChat(final CxMessage cxMessage, ViewHolder viewHolder)
    {
        viewHolder.tv_chatcontent.setText(SmileUtils.getSmiledText(context, cxMessage.getBody()));
        //同时 检测是否需要填充地址。

        if (viewHolder.tv_chatcontent != null)
        {
            viewHolder.tv_chatcontent.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    if (onAdapterListener != null)
                    {
                        onAdapterListener.onChatContentLongClickListener(0, cxMessage);
                    }
                    return true;
                }
            });
        }
    }

    private HashMap<Integer, SDTimerTask> taskHashMap = new HashMap<>();
    private SDTimerTask timerTask = null;

    /**
     * 创建文本信息倒计时
     */
    private void initMessageTimeTask(final CxMessage cxMessage, final int countTime,
                                     final ViewHolder holder, final int position, final Message msg)
    {
        timerTask = new SDTimerTask(countTime, 1, new SDTimerTask.SDTimerTasKCallback()
        {
            @Override
            public void startTask()
            {//开始倒计时
                int mediaType = cxMessage.getMediaType();
                if (mediaType == MediaType.TEXT.value())
                {//如果是文字
                    //布局
                    holder.ll_hot_message_layout.setVisibility(View.GONE);
                    holder.rl_content.setVisibility(View.VISIBLE);
                }

                if (mediaType == MediaType.VIDEO.value())
                {//如果是视频{
                    if (countTime < 10)
                    {
                        //倒计时的红点
                        holder.tv_countdown.setText(String.valueOf(countTime));
                        holder.tv_countdown.setVisibility(View.VISIBLE);
                    }
                } else
                {
                    //倒计时的红点
                    holder.tv_countdown.setText(String.valueOf(countTime));
                    holder.tv_countdown.setVisibility(View.VISIBLE);
                }
                CxMessage cxMsg = (CxMessage) getItem(position);
                //删除
                msg.obj = cxMsg;
                msg.arg1 = position;

                cxMessage.setHotChatVisible(true);//开始倒计时了

            }

            @Override
            public void stopTask()
            {

            }

            @Override
            public void currentTime(int countdownTime)
            {
                int mediaType = cxMessage.getMediaType();
                //设置倒计时时间
                if (mediaType == MediaType.VIDEO.value())
                {//如果是视频{
                    if (countdownTime < 10)
                    {
                        //设置倒计时时间
                        holder.tv_countdown.setText(String.valueOf(countdownTime));
                        holder.tv_countdown.setVisibility(View.VISIBLE);
                    } else
                    {
                        holder.tv_countdown.setVisibility(View.GONE);
                    }
                } else
                {
                    //设置倒计时时间
                    holder.tv_countdown.setText(String.valueOf(countdownTime));
                }
                cxMessage.getImMessage().getHeader().getAttachment().put("burnAfterReadTime", countdownTime + "");
                cxMessage.setHotTime(countdownTime);
                CXChatManager.updateMsg(cxMessage);

            }

            @Override
            public void finishTask()
            {
                int mediaType = cxMessage.getMediaType();
                if (mediaType == MediaType.TEXT.value())
                {//如果是文字
                    holder.rl_content.setVisibility(View.GONE);
                    holder.ll_hot_message_layout.setVisibility(View.GONE);
                }

                if (mediaType == MediaType.TEXT.value())
                {//如果是文字
                    delHandlerMessage.sendMessage(msg);
                } else if (mediaType == MediaType.PICTURE.value())
                {//如果是图片
                    delHandlerPic.sendMessage(msg);
                } else if (mediaType == MediaType.AUDIO.value())
                {//如果是语音
                    delHandlerVoice.sendMessage(msg);
                } else
                {
                    delHandlerMessage.sendMessage(msg);
                }

                holder.tv_countdown.setVisibility(View.GONE);
            }
        });

        timerTask.start();
        taskHashMap.put(position, timerTask);

        cxMessage.setHotChatVisible(true);//开始倒计时了
        CXChatManager.updateMsg(cxMessage);
        CxMessage cxMessage2 = new CxMessage();
        cxMessage2 = CXChatManager.loadChatMsgByMsgId(cxMessage.getImMessage().getHeader().getMessageId());
    }

    //删除该条数据库 1111
    private Handler delHandlerMessage = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            CxMessage cxMsg = (CxMessage) msg.obj;
            CXChatManager.delteMsgByMsgIdNoSql(cxMsg);
            //删除该条记录
            removeMsgByMessage(cxMsg);
            ChatBaseAdapter.this.notifyDataSetChanged();
            if (null != cxMsg)
            {
                //更新会话列表
                updateConversationMsg(cxMsg.getImMessage().getHeader().getMessageId());
            }
        }
    };

    //如果删除了，那么必须更新会话列表，谢谢NM
    private void updateConversationMsg(String msgId)
    {
        if (datas != null)
        {
            if (datas.size() > 0)
            {
                IMConversation conversationUpdate = IMConversaionManager.getInstance().loadByMsgId(msgId);
                if (StringUtils.notEmpty(conversationUpdate))
                {
                    conversationUpdate.setMessageId(datas.get(datas.size() - 1).getImMessage().getHeader().getMessageId());
                    IMConversaionManager.getInstance().updateConversation(conversationUpdate);
                }
            } else
            {//就删除
                //IMConversaionManager.getInstance().delteMsgByMsgId(msgId);
                IMConversation conversationUpdate = IMConversaionManager.getInstance().loadByMsgId(msgId);
                if (null != conversationUpdate)
                {
                    IMConversaionManager.getInstance().delteMsgByMsgId(conversationUpdate.getUsername());
                }
            }
        }
    }

    //删除该条数据库 22222
    private Handler delHandlerPic = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            CxMessage cxMsg = (CxMessage) msg.obj;
            CXChatManager.delteMsgByMsgIdNoSql(cxMsg);
            //删除该条记录
            removeMsgByMessage(cxMsg);
            ChatBaseAdapter.this.notifyDataSetChanged();
            if (null != cxMsg)
            {
                //更新会话列表
                updateConversationMsg(cxMsg.getImMessage().getHeader().getMessageId());
            }
        }
    };

    //删除该条数据库 3333
    private Handler delHandlerVoice = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            CxMessage cxMsg = (CxMessage) msg.obj;
            CXChatManager.delteMsgByMsgIdNoSql(cxMsg);
            //删除该条记录
            removeMsgByMessage(cxMsg);
            ChatBaseAdapter.this.notifyDataSetChanged();
            if (null != cxMsg)
            {
                //更新会话列表
                updateConversationMsg(cxMsg.getImMessage().getHeader().getMessageId());
            }

        }
    };

    /**
     * 填充视频聊天
     */
    protected void fillVideoChat(CxMessage cxMessage, ViewHolder viewHolder)
    {
        try
        {
            viewHolder.tv_chatcontent.setText(ParseNoticeMsgUtil.parseVideoAUdioMsg(cxMessage.getBody()));
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 填充语音聊天
     */
    protected void fillAudioChat(CxMessage cxMessage, ViewHolder viewHolder)
    {
        try
        {
            viewHolder.tv_chatcontent.setText(ParseNoticeMsgUtil.parseVideoAUdioMsg(cxMessage.getBody()));
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 填充消息状态
     *
     * @param cxMessage
     * @param viewHolder
     */
    protected void fillMsgStatus(final CxMessage cxMessage, final ViewHolder viewHolder, final int position)
    {
        int mediaType = cxMessage.getMediaType();
        try
        {
            if (cxMessage.getDirect() == CxIMMessageDirect.SEND)
            {
//              if (viewHolder.pb_sending != null && viewHolder.msg_status != null) {
                if (cxMessage.getStatus() == CxIMMessageStatus.SUCCESS)
                {
                    viewHolder.pb_sending.setVisibility(View.GONE);
                    viewHolder.msg_status.setVisibility(View.GONE);
                    //未阅的标志
                    if (cxMessage.getType() == CxIMMessageType.SINGLE_CHAT.getValue())
                    {
                        if (cxMessage.getMediaType() != 82 && cxMessage.getMediaType() != 81)
                            if (StringUtils.notEmpty(cxMessage.isReaderStatus()))
                            {
                                if (cxMessage.isReaderStatus())
                                {
                                    viewHolder.tv_ack.setText("已阅");
                                } else
                                {
                                    viewHolder.tv_ack.setText("送达");
                                }
                                if (DisplayUtil.getRead(context))
                                {
                                    viewHolder.tv_ack.setVisibility(View.VISIBLE);
                                } else
                                {
                                    viewHolder.tv_ack.setVisibility(View.GONE);
                                }
                            } else
                            {
                                viewHolder.tv_ack.setVisibility(View.GONE);
                            }

                        toJumpBaidu(cxMessage, viewHolder);
                    }

                } else if (cxMessage.getStatus() == CxIMMessageStatus.SENDING || cxMessage.getStatus() == CxIMMessageStatus
                        .FILE_SENDING)
                {
                    viewHolder.tv_ack.setVisibility(View.GONE);
                    viewHolder.pb_sending.setVisibility(View.VISIBLE);
                    viewHolder.msg_status.setVisibility(View.GONE);
                    viewHolder.icon_baidu.setVisibility(View.GONE);
                } else
                {
                    viewHolder.tv_ack.setVisibility(View.GONE);
                    viewHolder.pb_sending.setVisibility(View.GONE);
                    viewHolder.msg_status.setVisibility(View.VISIBLE);
                    viewHolder.msg_status.setTag(viewHolder.pb_sending);
                    viewHolder.msg_status.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            //重发
                            CXChatManager.reSendChatMsg(cxMessage);
                            v.setVisibility(View.GONE);
                            ((View) v.getTag()).setVisibility(View.VISIBLE);
                            //更新
//                            notifyDataSetChanged();
                        }
                    });

                    if (cxMessage.getMediaType() != MediaType.POSITION.value())
                        viewHolder.icon_baidu.setVisibility(View.GONE);
                }

                //阅后即焚，填充开始。
                if (mediaType == MediaType.PICTURE.value())//如果是图片
                {
                    IMMessage msg = cxMessage.getImMessage();
                    if (getValueByKey("isBurnAfterRead", msg.getHeader().getAttachment()).equals("1"))
                    {
                        viewHolder.iv_sendPicture.setVisibility(View.GONE);
                        viewHolder.iv_hot_see.setVisibility(View.VISIBLE);
                        viewHolder.row_send_pic.setBackgroundColor(context
                                .getResources().getColor(android.R.color.transparent));
                    } else
                    {
//                        viewHolder.iv_sendPicture.setVisibility(View.VISIBLE);
//                        viewHolder.iv_hot_see.setVisibility(View.GONE);
//                        viewHolder.row_send_pic.setBackgroundResource(R.drawable.chatto_pic_bg);
                    }


                } else if (mediaType == MediaType.AUDIO.value())//语音
                {
                    IMMessage msg = cxMessage.getImMessage();
                    if (getValueByKey("isBurnAfterRead", msg.getHeader().getAttachment()).equals("1"))
                    {
                        viewHolder.iv_voice_ly.setBackgroundResource(R.drawable.chatto_hot_bg);
                    } else
                    {
//                        viewHolder.iv_voice_ly.setBackgroundResource(R.drawable.chatto_bg);
                    }
                } else if (mediaType == MediaType.TEXT.value())//文字
                {
                    IMMessage msg = cxMessage.getImMessage();
                    if (getValueByKey("isBurnAfterRead", msg.getHeader().getAttachment()).equals("1"))
                    {
                        viewHolder.tv_chatcontent.setBackgroundResource(R.drawable.chatto_hot_bg);
                    } else
                    {
                        viewHolder.tv_chatcontent.setBackgroundResource(R.drawable.chatto_bg);
                    }
                } else if (mediaType == MediaType.VIDEO.value())//小视频
                {
                    IMMessage msg = cxMessage.getImMessage();
                    if (getValueByKey("isBurnAfterRead", msg.getHeader().getAttachment()).equals("1"))
                    {
                        viewHolder.hot_lock_bg_img.setVisibility(View.VISIBLE);
                    } else
                    {
//                        viewHolder.hot_lock_bg_img.setVisibility(View.INVISIBLE);
                    }
                }

                //阅后即焚，发送方如果是已阅，那么直接删除
                if (cxMessage.getMediaType() != 82 && cxMessage.getMediaType() != 81)
                    if (StringUtils.notEmpty(cxMessage.isReaderStatus()))
                    {
                        IMMessage msg = cxMessage.getImMessage();
                        if (getValueByKey("isBurnAfterRead", msg.getHeader().getAttachment()).equals("1"))
                        {
                            if (cxMessage.isReaderStatus())
                            {
                                viewHolder.tv_ack.setVisibility(View.VISIBLE);
                                viewHolder.tv_ack.setText("已阅");
                                viewHolder.tv_ack.setVisibility(View.GONE);
                                Message message = delHandlerMessage.obtainMessage();
                                message.obj = cxMessage;
                                message.arg1 = position;
                                delHandlerMessage.sendMessage(message);
                            }
//                            else
//                            {
//                                Message message = delHandlerMessage.obtainMessage();
//                                message.obj = cxMessage;
//                                message.arg1 = position;
//                                delHandlerMessage.sendMessage(message);
//                            }
                        }
                    }
//            }
            }
            //接收对方的时候
            else
            {
                if (mediaType == MediaType.TEXT.value()) //
                {
                    final Message message = delHandlerMessage.obtainMessage();
                    IMMessage msg = cxMessage.getImMessage();
                    if (getValueByKey("isBurnAfterRead", msg.getHeader().getAttachment()).equals("1"))
                    {
                        //停止计时器
                        timerTask = taskHashMap.get(position);
                        if (timerTask != null)
                        {
                            timerTask.stop();
                        }
                        boolean isStart = cxMessage.isHotChatVisible();
                        //判断是否已经开始过倒计时
                        if (isStart)
                        {
                            //int countTime = cxMessage.getHotTime();
                            int countTime = Integer.parseInt(getValueByKey("burnAfterReadTime", cxMessage.getImMessage()
                                    .getHeader().getAttachment()));
                            if (countTime > 0)
                            {//如果大于0就继续倒计时
                                initMessageTimeTask(cxMessage, countTime, viewHolder, position, message);
                            } else
                            {//等于0那么就直接删除
                                CxMessage delCxMessage = (CxMessage) getItem(position);
                                message.obj = delCxMessage;
                                message.arg1 = position;
                                delHandlerMessage.sendMessage(message);
                            }
                        } else
                        {
                            //如果是阅后即焚
                            viewHolder.tv_countdown.setVisibility(View.GONE);
                            viewHolder.ll_hot_message_layout.setVisibility(View.VISIBLE);
                            viewHolder.rl_content.setVisibility(View.GONE);
                            viewHolder.ll_hot_message_layout.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    //标记为已阅
                                    cxMessagesUnread = new ArrayList<CxMessage>();
                                    cxMessagesUnread.add(cxMessage);
                                    setMessageRead(cxMessagesUnread, cxMessage.getImMessage().getHeader().getFrom());
                                    initMessageTimeTask(cxMessage, 10, viewHolder, position, message);

                                    //更改数据源
                                    updateMsgByMessage(cxMessage);
                                    SDLogUtil.debug("数据源为：" + datas);

                                }
                            });
                        }
                    } else
                    {
//                        viewHolder.tv_countdown.setVisibility(View.GONE);
//                        viewHolder.ll_hot_message_layout.setVisibility(View.GONE);
//                        viewHolder.rl_content.setVisibility(View.VISIBLE);
                    }
                } else if (mediaType == MediaType.PICTURE.value())//如果是图片
                {
                    final Message message = delHandlerPic.obtainMessage();
                    IMMessage msg = cxMessage.getImMessage();
                    if (getValueByKey("isBurnAfterRead", msg.getHeader().getAttachment()).equals("1"))
                    {
                        //阅后即焚
                        //停止计时器
                        timerTask = taskHashMap.get(position);
                        if (timerTask != null)
                        {
                            timerTask.stop();
                        }
                        boolean isStart = cxMessage.isHotChatVisible();
                        //判断是否已经开始过倒计时
                        if (isStart)
                        {
                            // int countTime = cxMessage.getHotTime();
                            int countTime = Integer.parseInt(getValueByKey("burnAfterReadTime", cxMessage.getImMessage()
                                    .getHeader().getAttachment()));
                            if (countTime > 0)
                            {//如果大于0就继续倒计时
                                initMessageTimeTask(cxMessage, countTime, viewHolder, position, message);
                            } else
                            {//等于0那么就直接删除
                                CxMessage delCxMessage = (CxMessage) getItem(position);
                                message.obj = delCxMessage;
                                message.arg1 = position;
                                delHandlerPic.sendMessage(message);
                            }
                        } else
                        {//没有就可以点击
                            final String imgPath;
                            final CxFileMessage CxFileMessage = cxMessage.getFileMessage();
                            final File localFile = new File(CxFileMessage.getLocalUrl());
                            if (localFile.exists())
                            {
                                imgPath = CxFileMessage.getLocalUrl();
                            } else
                            {
                                imgPath = CxFileMessage.getRemoteUrl();
                            }
                            viewHolder.tv_countdown.setVisibility(View.GONE);
                            viewHolder.iv_hot_see.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    //标记为已阅
                                    cxMessagesUnread = new ArrayList<CxMessage>();
                                    cxMessagesUnread.add(cxMessage);
                                    setMessageRead(cxMessagesUnread, cxMessage.getImMessage().getHeader().getFrom());
                                    String countTime = getValueByKey("burnAfterReadTime", cxMessage.getImMessage().getHeader()
                                            .getAttachment());
                                    initMessageTimeTask(cxMessage, Integer.parseInt(countTime), viewHolder, position, message);
                                    //开始倒计时
                                    Intent intent = new Intent(context, BigCountDownImgActivity.class);
                                    intent.putExtra(BigCountDownImgActivity.EXTR_MSG, cxMessage);
                                    Bundle bundle = new Bundle();
                                    bundle.putString(BigCountDownImgActivity.EXTR_PATH, imgPath);
                                    bundle.putInt(BigCountDownImgActivity.EXTR_POSITION, position);
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);

                                }
                            });
                        }
                        viewHolder.iv_sendPicture.setVisibility(View.GONE);
                        viewHolder.iv_hot_see.setVisibility(View.VISIBLE);
                        viewHolder.row_recv_pic.setBackgroundColor(context
                                .getResources().getColor(android.R.color.transparent));
                    } else
                    {
//                        viewHolder.iv_sendPicture.setVisibility(View.VISIBLE);
//                        viewHolder.iv_hot_see.setVisibility(View.GONE);
//                        viewHolder.row_recv_pic.setBackgroundResource(R.drawable.chatfrom_pic_bg);
                    }
                } else if (mediaType == MediaType.AUDIO.value())//语音
                {
                    //停止计时器
                    timerTask = taskHashMap.get(position);
                    if (timerTask != null)
                    {
                        timerTask.stop();
                    }
                    IMMessage msg = cxMessage.getImMessage();
                    if (AudioPlayManager.getInstance().getAudioPlayerHandler() != null && AudioPlayManager.getInstance()
                            .isPlaying())
                    {
                        if (getValueByKey("isBurnAfterRead", msg.getHeader().getAttachment()).equals("1"))
                        {
                            viewHolder.iv_voice.setImageResource(R.drawable.voice_hot_from_icon);
                            viewHolder.tv_countdown.setVisibility(View.GONE);
                        } else
                        {
//                            viewHolder.iv_voice.setImageResource(R.drawable.play_receiver_hot_voice);
                        }
                    } else
                    {
                        if (getValueByKey("isBurnAfterRead", msg.getHeader().getAttachment()).equals("1"))
                        {
                            viewHolder.iv_voice_ly.setBackgroundResource(R.drawable.chatting_hot_received_message_bg);
                            viewHolder.iv_voice.setImageResource(R.drawable.chatfrom_hot_voice_playing_f3);
                            viewHolder.tv_length.setTextColor(Color.RED);
                            viewHolder.tv_countdown.setVisibility(View.GONE);

                            boolean isStart = cxMessage.isHotChatVisible();
                            //判断是否已经开始过倒计时
                            if (isStart)
                            {
                                viewHolder.tv_countdown.setVisibility(View.VISIBLE);
                            } else
                            {
                                viewHolder.tv_countdown.setVisibility(View.GONE);
                            }
                        } else
                        {
//                            viewHolder.tv_length.setTextColor(Color.parseColor("#818181"));
//                            viewHolder.iv_voice.setImageResource(R.drawable.chatfrom_voice_playing);
                        }
                    }
                    //如果是阅后即焚的话,yue
                    if (getValueByKey("isBurnAfterRead", msg.getHeader().getAttachment()).equals("1"))
                    {
                        //每次播放完成都从新倒计时
                        final Message message = delHandlerVoice.obtainMessage();
                        final CxFileMessage CxFileMessage = cxMessage.getFileMessage();
                        final File localFile = new File(CxFileMessage.getLocalUrl());
                        boolean isStart = cxMessage.isHotChatVisible();
                        //判断是否已经开始过倒计时
                        if (isStart)
                        {
                            int countTime = Integer.parseInt(getValueByKey("burnAfterReadTime", cxMessage.getImMessage()
                                    .getHeader().getAttachment()));
                            if (countTime > 0)
                            {//如果大于0就继续倒计时
                                initMessageTimeTask(cxMessage, countTime, viewHolder, position, message);
                            } else
                            {//等于0那么就直接删除
                                CxMessage delCxMessage = (CxMessage) getItem(position);
                                message.obj = delCxMessage;
                                message.arg1 = position;
                                delHandlerPic.sendMessage(message);
                            }
                        }
                        viewHolder.iv_voice_ly.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {

                                //标记为已阅
                                cxMessagesUnread = new ArrayList<CxMessage>();
                                cxMessagesUnread.add(cxMessage);
                                setMessageRead(cxMessagesUnread, cxMessage.getImMessage().getHeader().getFrom());
                                //停止计时器
                                timerTask = taskHashMap.get(position);
                                if (timerTask != null)
                                {
                                    timerTask.stop();
                                }
                                viewHolder.tv_countdown.setVisibility(View.GONE);
                                isAutoPlayAudio = true;
                                currentPlayAduioPosition = (int) v.getTag();
                                //如果已经开始倒计时了，那么重新开始
                                boolean isStart = cxMessage.isHotChatVisible();
                                if (isStart)
                                {
                                    if (timerTask != null)
                                    {
                                        timerTask.stop();
                                    }
                                    cxMessage.getImMessage().getHeader().getAttachment().put("burnAfterReadTime", 10 + "");
                                    cxMessage.setHotTime(10);
                                    CXChatManager.updateMsg(cxMessage);
                                }

                                AudioPlayManager.getInstance().setOnVoiceListener(new AudioPlayManager.OnVoiceListener()
                                {
                                    @Override
                                    public void playPositionChange(int currentPosition)
                                    {

                                    }

                                    @Override
                                    public void playCompletion()
                                    {


                                        initMessageTimeTask(cxMessage, 10, viewHolder, position, message);
                                    }

                                    @Override
                                    public void playDuration(int duration)
                                    {

                                    }

                                    @Override
                                    public void playException(Exception e)
                                    {
                                        e.printStackTrace();
                                    }

                                    @Override
                                    public void playStopVioce()
                                    {
                                        SDLogUtil.debug("im_voice_ChatBaseAdapter——停止的语音播放！");
                                    }

                                });

                                if (AudioPlayManager.getInstance().getAudioPlayerHandler() != null && AudioPlayManager
                                        .getInstance().isPlaying())
                                {
                                    SDLogUtil.debug("im_log----准备播放下一条语音！");
                                    if (AudioPlayManager.getInstance().getPlayPath().equals(localFile.getAbsolutePath()))
                                    {
                                        isAutoPlayAudio = false;
                                        SDLogUtil.debug("im_log----暂停播放下一条语音！");
                                        AudioPlayManager.getInstance().stop();
                                        AudioPlayManager.getInstance().close();
                                        return;
                                    }
                                }
                                RelativeLayout rl = (RelativeLayout) v;
                                ImageView iv = (ImageView) rl.getChildAt(0);

                                AudioPlayManager.getInstance().play(context, localFile.getAbsolutePath(), iv);

                                ImageView unreadImgView = (ImageView) iv.getTag();
                                if (unreadImgView != null)
                                {
                                    unreadImgView.setVisibility(View.GONE);
                                    cxMessage.setReader(true);
                                    CXChatManager.updateMsg(cxMessage);
                                }
                            }

                        });


                    }

                } else if (mediaType == MediaType.VIDEO.value())//小视频
                {
                    viewHolder.tv_countdown.setVisibility(View.GONE);
                    final Message message = delHandlerPic.obtainMessage();
                    IMMessage msg = cxMessage.getImMessage();
                    if (getValueByKey("isBurnAfterRead", msg.getHeader().getAttachment()).equals("1"))
                    {
                        //阅后即焚
                        //停止计时器
                        timerTask = taskHashMap.get(position);
                        if (timerTask != null)
                        {
                            timerTask.stop();
                        }
                        boolean isStart = cxMessage.isHotChatVisible();
                        //判断是否已经开始过倒计时
                        if (isStart)
                        {
                            // int countTime = cxMessage.getHotTime();
                            int countTime = Integer.parseInt(getValueByKey("burnAfterReadTime", cxMessage.getImMessage()
                                    .getHeader().getAttachment()));
                            if (countTime > 0)
                            {//如果大于0就继续倒计时
                                initMessageTimeTask(cxMessage, countTime, viewHolder, position, message);
                            } else
                            {//等于0那么就直接删除
                                CxMessage delCxMessage = (CxMessage) getItem(position);
                                message.obj = delCxMessage;
                                message.arg1 = position;
                                delHandlerPic.sendMessage(message);
                            }
                        }
                    }
                    if (getValueByKey("isBurnAfterRead", msg.getHeader().getAttachment()).equals("1"))
                    {//
                        viewHolder.hot_not_lock_bg_img.setVisibility(View.VISIBLE);
                    } else
                    {
//                        viewHolder.hot_not_lock_bg_img.setVisibility(View.INVISIBLE);
                    }
                }
                toJumpBaidu(cxMessage, viewHolder);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 设置信息为已阅，且发送推送-阅后即焚用
     *
     * @param list
     */
    private void setMessageRead(List<CxMessage> list, String toAccount)
    {
        if (StringUtils.notEmpty(list) && list.size() > 0)
        {
            String[] messageId = new String[list.size()];
            for (int i = 0; i < list.size(); i++)
            {
//                list.get(i).setReader(true);
                list.get(i).setReaderStatus(true);
                messageId[i] = list.get(i).getImMessage().getHeader().getMessageId();
            }
            try
            {
                CxGreenDaoOperateUtils.getInstance().updateMessageBatch(CXMessageUtils.covertCxMessage(list));
                SocketManager.getInstance().sendPushReadMsg(toAccount, SDGson.toJson(messageId));
                SDLogUtil.debug("IM_已阅信息-" + SDGson.toJson(messageId));
            } catch (Exception e)
            {
                e.printStackTrace();
            }


        }

    }


    /**
     * 跳转到百度
     */
    protected void toJumpBaidu(final CxMessage cxMessage, ViewHolder viewHolder)
    {
        //这里加入一个定位图标
        Map<String, String> baiduMap = new HashMap<String, String>();
        baiduMap = cxMessage.getImMessage().getHeader().getAttachment();
        if (baiduMap != null)
            if (baiduMap.size() > 0 && baiduMap.get("location").length() > 0)
            {
                final CxBaiDuEntity baiduInfo = CxBaiDuEntity.parse(baiduMap.get("location"));
                SDLogUtil.debug("接收的百度定位：" + baiduInfo.getAddress());
                SDLogUtil.debug("接收的百度定位Latitude：" + baiduInfo.getLatitude());
                SDLogUtil.debug("接收的百度定位Longitude：" + baiduInfo.getLongitude());
                //根据定位那里的nullnullnull.不多做字段用于判断了。
                if (StringUtils.notEmpty(baiduInfo.getAddress()))
                    if (!baiduInfo.getAddress().equals("nullnullnull"))
                    {
                        viewHolder.icon_baidu.setBackgroundResource(R.mipmap.up_class_pos);
                        if (DisplayUtil.getLocatin(context))
                        {
                            if (cxMessage.getDirect() == CxIMMessageDirect.RECEIVER)
                            {
                                if (InfoUtils.getInstance().getRank(context, cxMessage.getImMessage().getHeader().getFrom()))
                                    viewHolder.icon_baidu.setVisibility(View.VISIBLE);
                                else
                                    viewHolder.icon_baidu.setVisibility(View.GONE);
                            } else
                            {
//                                viewHolder.icon_baidu.setVisibility(View.GONE);
                                viewHolder.icon_baidu.setVisibility(View.VISIBLE);
                            }
                        } else
                        {
                            viewHolder.icon_baidu.setVisibility(View.GONE);
                        }
                        //当为可见时候。显示小图标。可点击
                        viewHolder.icon_baidu.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                Intent intent = new Intent(context, LocationActiviy.class);
                                Bundle bl = new Bundle();
                                bl.putString("longitude", baiduInfo.getLongitude());
                                // 纬度latitude
                                bl.putString("latitude", baiduInfo.getLatitude());
                                bl.putString("address", baiduInfo.getAddress());
                                bl.putString("userId", cxMessage.getImMessage().getHeader().getFrom() + "");
                                intent.putExtras(bl);
                                context.startActivity(intent);
                            }
                        });
                    }
            }
    }

    /**
     * 填充群组接收消息人姓名、及用户头像！
     *
     * @param cxMessage
     * @param viewHolder
     */
    protected void fillName(final CxMessage cxMessage, ViewHolder viewHolder)
    {
        if (cxMessage.getDirect() == CxIMMessageDirect.RECEIVER && cxMessage.getType() == CxIMMessageType.GROUP_CHAT.getValue()
                && cxMessage.getMediaType() != NoticeType.NORMAL_TYPE)
        {
            try
            {
                viewHolder.tv_userid.setVisibility(View.VISIBLE);
//                SDUserEntity userInfo = userDao.findUserByImAccount(cxMessage.getImMessage().getHeader().getFrom());
                //替换全局的通讯录
                SDAllConstactsEntity userInfo = SDAllConstactsDao.getInstance()
                        .findAllConstactsByImAccount(cxMessage.getImMessage().getHeader().getFrom());
                viewHolder.tv_userid.setText(userInfo.getName());

            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 填充消息通知
     *
     * @param cxMessage
     * @param viewHolder
     */
    protected void fillNotice(Context context, CxMessage cxMessage, ViewHolder viewHolder)
    {
        try
        {
            viewHolder.tv_notice_message.setVisibility(View.VISIBLE);
//            viewHolder.tv_notice_message.setTextScaleX(1.25f);
            viewHolder.tv_notice_message.setText(
                    ParseNoticeMsgUtil.parseNoticeMsg_New(context,
                            currentAccount, CXMessageUtils.convertCXMessage(cxMessage)));
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 填充视频
     *
     * @param cxMessage
     * @param viewHolder 视频缓存逻辑:先获取本地路径,
     *                   查看是否存在视频,是直接打开,
     *                   否则从视频缓存文件目录找,存在直
     *                   接打开,否则获取远程视频地址进行下载并打开
     */
//    protected void fillVideo(final ViewHolder viewHolder, final CxMessage cxMessage,
//                             final File localFile, int direct, final int position)
//    {
//        // final String videoPath = cxMessage.getFileMessage().getLocalUrl();
//        //        viewHolder.chatting_content_iv.setImageBitmap(BitmapUtil.getVideoThumbnail(videoPath));
//        //        viewHolder.chatting_content_iv.setOnClickListener(new View.OnClickListener()
//        //        {
//        //            @Override
//        //            public void onClick(View v)
//        //            {
//        //                context.startActivity(new Intent(context, PlayVideoActiviy.class).putExtra(PlayVideoActiviy
// .KEY_FILE_PATH, videoPath));
//        //            }
//        //        });
//
//        //下载百分比-需要判断视频的状态，是否为发送或者接受方，-的各种状态
//        //        viewHolder.percentage.setVisibility(View.GONE);
//        viewHolder.percentage.setVisibility(View.VISIBLE);
//        viewHolder.msg_status.setVisibility(View.GONE);
//
//        final File cacheFile = new File(Config.CACHE_VIDEO_DIR, cxMessage.getFileMessage().getName());
//        final String videoFileName = FileUtil.getLocalUrl2(cxMessage.getFileMessage().getName());
//
//        if (!cxMessage.getFileMessage().getRemoteUrl().equals(""))
//        {
//            viewHolder.chatting_content_iv.setImageResource(R.mipmap.smallvideoreastayplay);
//
//            if (cacheFile.exists())
//            {
//                viewHolder.chatting_content_iv.setImageBitmap(BitmapUtil.getVideoThumbnail(videoFileName));
//                viewHolder.percentage.setVisibility(View.GONE);
//                viewHolder.pb_sending.setVisibility(View.GONE);
//            } else
//            {
//              /*  viewHolder.chatting_content_iv.setImageResource(R.mipmap.smallvideoreastayplay);*/
//                viewHolder.pb_sending.setVisibility(View.GONE);
//                //                viewHolder.msg_status.setVisibility(View.VISIBLE);
//                //                viewHolder.msg_status.setBackgroundResource(R.drawable.msg_state_failed_resend);
//                viewHolder.msg_status.setTag(viewHolder.pb_sending);
//            }
//
//            viewHolder.chatting_content_iv.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View v)
//                {
//                    if (localFile.exists())
//                    {
//                        viewHolder.chatting_content_iv.setImageBitmap(BitmapUtil.getVideoThumbnail(videoFileName));
//                        context.startActivity(new Intent(context, PlayVideoActiviy.class).
//                                putExtra(PlayVideoActiviy.KEY_FILE_PATH, videoFileName));
//                    } else
//                    {
//                        if (cacheFile.exists())
//                        {
//                            viewHolder.chatting_content_iv.setImageBitmap(BitmapUtil.getVideoThumbnail(videoFileName));
//                            context.startActivity(new Intent(context, PlayVideoActiviy.class).putExtra(PlayVideoActiviy
// .KEY_FILE_PATH, videoFileName));
//                        } else
//                        {
//                            viewHolder.percentage.setVisibility(View.VISIBLE);
//                            viewHolder.chatting_content_iv.setImageResource(R.mipmap.smallvideoreastayplay);
//                            OkHttpUtils.get().url(cxMessage.getFileMessage().getRemoteUrl()).build().execute(new FileCallBack
// (Config.CACHE_VIDEO_DIR, cxMessage.getFileMessage().getName())
//                            {
//                                @Override
//                                public void inProgress(float progress)
//                                {
//                                    float b = (float) (Math.round(progress * 100)) / 100;
//                                    viewHolder.percentage.setText(b + "%");
//                                }
//
//                                @Override
//                                public void onError(Request request, Exception e) throws Exception
//                                {
//                                    viewHolder.msg_status.setVisibility(View.VISIBLE);
//                                    viewHolder.msg_status.setBackgroundResource(R.drawable.msg_state_failed_resend);
//                                    viewHolder.percentage.setVisibility(View.GONE);
//                                }
//
//                                @Override
//                                public void onResponse(File response) throws Exception
//                                {
//                                    viewHolder.chatting_content_iv.setImageBitmap(BitmapUtil.getVideoThumbnail(videoFileName));
//                                    if (!cxMessage.getFileMessage().getLocalUrl().contains("cxim"))
//                                    {
//                                        context.startActivity(new Intent(context, PlayVideoActiviy.class)
//                                                .putExtra(PlayVideoActiviy.KEY_FILE_PATH, cacheFile.getAbsolutePath()));
//                                    } else
//                                    {
//                                        context.startActivity(new Intent(context, PlayVideoActiviy.class)
//                                                .putExtra(PlayVideoActiviy.KEY_FILE_PATH, cxMessage.getFileMessage()
// .getLocalUrl()));
//                                    }
//                                    viewHolder.pb_sending.setVisibility(View.GONE);
//                                    viewHolder.msg_status.setVisibility(View.GONE);
//                                    viewHolder.percentage.setVisibility(View.GONE);
//                                }
//                            });
//                        }
//                    }
//                }
//            });
//        } else
//        {
//            viewHolder.pb_sending.setVisibility(View.GONE);
//            viewHolder.msg_status.setVisibility(View.GONE);
//            viewHolder.percentage.setVisibility(View.GONE);
//
//            final String videoPath = cxMessage.getFileMessage().getLocalUrl();
//
//            viewHolder.chatting_content_iv.setImageBitmap(BitmapUtil.getVideoThumbnail(videoPath));
//         /*   viewHolder.chatting_content_iv.setImageResource(R.mipmap.smallvideoreastayplay);*/
//            viewHolder.chatting_content_iv.setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View v)
//                {
//                    /*viewHolder.chatting_content_iv.setImageBitmap(BitmapUtil.getVideoThumbnail(videoPath));*/
//
//                    context.startActivity(new Intent(context, PlayVideoActiviy.class).putExtra(PlayVideoActiviy
// .KEY_FILE_PATH, videoPath));
//                }
//            });
//        }
//    }
    protected void fillVideo(final ViewHolder viewHolder, final CxMessage cxMessage,
                             final File localFile, int direct, final int position)
    {
        // final String videoPath = cxMessage.getFileMessage().getLocalUrl();
        //        viewHolder.chatting_content_iv.setImageBitmap(BitmapUtil.getVideoThumbnail(videoPath));
        //        viewHolder.chatting_content_iv.setOnClickListener(new View.OnClickListener()
        //        {
        //            @Override
        //            public void onClick(View v)
        //            {
        //                context.startActivity(new Intent(context, PlayVideoActiviy.class)
        // .putExtra(PlayVideoActiviy.KEY_FILE_PATH, videoPath));
        //            }
        //        });

        //下载百分比-需要判断视频的状态，是否为发送或者接受方，-的各种状态
        //        viewHolder.percentage.setVisibility(View.GONE);
        viewHolder.percentage.setVisibility(View.VISIBLE);
        viewHolder.msg_status.setVisibility(View.GONE);

        final File cacheFile = new File(Config.CACHE_VIDEO_DIR, cxMessage.getFileMessage().getName());
        final String videoFileName = FileUtil.getLocalUrl2(cxMessage.getFileMessage().getName());

        if (!cxMessage.getFileMessage().getRemoteUrl().equals(""))
        {
            if (cxMessage.getDirect() != 0)
            {
                if (cxMessage.isReaderStatus())
                {
                    if (cacheFile.exists())
                    {
                        viewHolder.chatting_content_iv.setImageBitmap(BitmapUtil.getVideoThumbnail(videoFileName));
                        viewHolder.percentage.setVisibility(View.GONE);
                        viewHolder.pb_sending.setVisibility(View.GONE);
                    } else
                    {
                        viewHolder.pb_sending.setVisibility(View.GONE);
                        viewHolder.msg_status.setTag(viewHolder.pb_sending);
                    }
                } else
                {
//                    if (cxMessage.getType() == CxIMMessageType.SINGLE_CHAT.getValue())
//                    {
//                        cxMessagesReadList = new ArrayList<CxMessage>();
//                        cxMessagesReadList.add(cxMessage);
//                        setMessageRead(cxMessagesReadList, cxMessage.getImMessage().getHeader().getFrom());
//                    }
                    viewHolder.chatting_content_iv.setImageResource(R.mipmap.smallvideoreastayplay);
                }
            } else
            {
                if (cacheFile.exists())
                {
                    viewHolder.chatting_content_iv.setImageBitmap(BitmapUtil.getVideoThumbnail(videoFileName));
                    viewHolder.percentage.setVisibility(View.GONE);
                    viewHolder.pb_sending.setVisibility(View.GONE);
                } else
                {
                    viewHolder.pb_sending.setVisibility(View.GONE);
                    viewHolder.msg_status.setTag(viewHolder.pb_sending);
                }
            }

            viewHolder.chatting_content_iv.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (localFile.exists())
                    {
                        viewHolder.chatting_content_iv.setImageBitmap(BitmapUtil.getVideoThumbnail(videoFileName));
                        context.startActivity(new Intent(context, PlayVideoActiviy.class)
                                .putExtra(PlayVideoActiviy.KEY_FILE_PATH, videoFileName)
                                .putExtra(BigCountDownImgActivity.EXTR_MSG, cxMessage)
                                .putExtra(BigCountDownImgActivity.EXTR_POSITION, position));

                        if (cxMessage.getDirect() != 0)
                        {
                            if (!cxMessage.isReaderStatus())
                            {
                                if (cxMessage.getType() == CxIMMessageType.SINGLE_CHAT.getValue())
                                {
                                    cxMessagesReadList = new ArrayList<CxMessage>();
                                    cxMessagesReadList.add(cxMessage);
                                    setMessageRead(cxMessagesReadList, cxMessage.getImMessage().getHeader().getFrom());
                                }
                            }
                        }
                    } else
                    {
                        if (cacheFile.exists())
                        {
                            viewHolder.chatting_content_iv.setImageBitmap(BitmapUtil.getVideoThumbnail(videoFileName));
                            context.startActivity(new Intent(context, PlayVideoActiviy.class)
                                    .putExtra(PlayVideoActiviy.KEY_FILE_PATH, videoFileName)
                                    .putExtra(BigCountDownImgActivity.EXTR_MSG, cxMessage)
                                    .putExtra(BigCountDownImgActivity.EXTR_POSITION, position));
                        } else
                        {
                            viewHolder.percentage.setVisibility(View.VISIBLE);
                            viewHolder.chatting_content_iv.setImageResource(R.mipmap.smallvideoreastayplay);
                            OkHttpUtils.get().url(cxMessage.getFileMessage().getRemoteUrl()).build()
                                    .execute(new FileCallBack(Config.CACHE_VIDEO_DIR, cxMessage.getFileMessage().getName())
                                    {
                                        @Override
                                        public void inProgress(float progress)
                                        {
                                            float b = (float) (Math.round(progress * 100)) / 100;
                                            viewHolder.percentage.setText(b + "%");
                                        }

                                        @Override
                                        public void onError(Request request, Exception e) throws Exception
                                        {
                                            viewHolder.msg_status.setVisibility(View.VISIBLE);
                                            viewHolder.msg_status.setBackgroundResource(R.drawable.msg_state_failed_resend);
                                            viewHolder.percentage.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onResponse(File response) throws Exception
                                        {
//                                    viewHolder.chatting_content_iv.setImageBitmap(BitmapUtil.getVideoThumbnail(videoFileName));
//                                    if (!cxMessage.getFileMessage().getLocalUrl().contains("cxim"))
//                                    {
//                                        context.startActivity(new Intent(context, PlayVideoActiviy.class)
//                                                .putExtra(PlayVideoActiviy.KEY_FILE_PATH, cacheFile.getAbsolutePath()));
//                                    } else
//                                    {
//                                        context.startActivity(new Intent(context, PlayVideoActiviy.class)
//                                                .putExtra(PlayVideoActiviy.KEY_FILE_PATH, cxMessage.getFileMessage()
// .getLocalUrl()));
//                                    }
//                                    viewHolder.pb_sending.setVisibility(View.GONE);
//                                    viewHolder.msg_status.setVisibility(View.GONE);
//                                    viewHolder.percentage.setVisibility(View.GONE);
                                            viewHolder.chatting_content_iv.setImageBitmap(BitmapUtil.getVideoThumbnail
                                                    (videoFileName));
                                            viewHolder.pb_sending.setVisibility(View.GONE);
                                            viewHolder.msg_status.setVisibility(View.GONE);
                                            viewHolder.percentage.setVisibility(View.GONE);
//                                            if (cxMessage.getType() == CxIMMessageType.SINGLE_CHAT.getValue())
//                                            {
//                                                cxMessagesReadList = new ArrayList<CxMessage>();
//                                                cxMessagesReadList.add(cxMessage);
                                            // setMessageRead(cxMessagesReadList, cxMessage.getImMessage().getHeader().getFrom());
//                                            }

                                        }
                                    });
                        }
                    }
                }
            });
        } else
        {
            viewHolder.pb_sending.setVisibility(View.GONE);
            viewHolder.msg_status.setVisibility(View.GONE);
            viewHolder.percentage.setVisibility(View.GONE);

            final String videoPath = cxMessage.getFileMessage().getLocalUrl();
            viewHolder.chatting_content_iv.setImageBitmap(BitmapUtil.getVideoThumbnail(videoPath));
            viewHolder.chatting_content_iv.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(context, PlayVideoActiviy.class);
                    Bundle bundle = new Bundle();
                    intent.putExtra(PlayVideoActiviy.KEY_FILE_PATH, videoPath);
                    bundle.putInt(BigCountDownImgActivity.EXTR_POSITION, position);
                    intent.putExtra(BigCountDownImgActivity.EXTR_MSG, cxMessage);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }
    }

    /**
     * 填充时间
     *
     * @param cxMessage
     * @param prevMessage
     * @param position
     * @param viewHolder
     */
    //时间
    protected void fillTimestamp(CxMessage cxMessage, CxMessage prevMessage, int position, ViewHolder viewHolder)
    {
        if (position == 0)
        {
            if (StringUtils.notEmpty(cxMessage.getCreateTime()))
            {
                viewHolder.timestamp.setVisibility(View.VISIBLE);
                viewHolder.timestamp.setText(cxMessage.getCreateTime());
            }

        } else
        {
            if (prevMessage != null && DateUtils.isCloseEnough(cxMessage.getCreateTimeMillisecond(),
                    prevMessage.getCreateTimeMillisecond()))
            {
                viewHolder.timestamp.setVisibility(View.GONE);
            } else
            {
                viewHolder.timestamp.setText(cxMessage.getCreateTime());
                viewHolder.timestamp.setVisibility(View.VISIBLE);
            }
        }
    }

    public void addAudioPosition(int position)
    {
        if (!audioPositionList.contains(position))
        {
            audioPositionList.add(position);
            Collections.sort(audioPositionList);
        }
    }

    /**
     * 填充语音
     *
     * @param viewHolder
     * @param cxMessage
     * @param localFile
     * @param position
     * @desc 语音自动播放逻辑:从单前播放语音开始寻找下一条未阅的语音进行播放,依次列推直到找不到下一条语音
     */
    protected void fillAudio(final int position, ViewHolder viewHolder, final CxMessage cxMessage, final File localFile)
    {
        audioPlayViewMap.put(position, viewHolder.iv_voice_ly);
        addAudioPosition(position);
        final CxFileMessage CxFileMessage = cxMessage.getFileMessage();
        int audioLength = (int) CxFileMessage.getLength();

        viewHolder.tv_length.setText(String.valueOf(audioLength) + "''");

        viewHolder.iv_voice_ly.setTag(position);

        Drawable drawable = viewHolder.iv_voice.getDrawable();
        if (drawable instanceof AnimationDrawable)
        {
            ((AnimationDrawable) drawable).stop();
        }

        /** 根据语音长度来动态设置iv_voice_ly的长度**/
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.iv_voice_ly.getLayoutParams();
        float lengthRate = (float) audioLength / 15.0f;

        SDLogUtil.info("voicerate:" + voicerate);
        SDLogUtil.info("lengthRate:" + lengthRate);
        SDLogUtil.info("maxVoiceWidth:" + maxVoiceWidth);
        SDLogUtil.info("minVoiceWidth:" + minVoiceWidth);
        SDLogUtil.info("maxVoiceWidth*lengthRate:" + maxVoiceWidth * lengthRate);
        //小于3秒
        if (audioLength < 4)
        {
            layoutParams.width = minVoiceWidth;
        }
        //大于3秒的。
        else if (audioLength > 3)
        {
            layoutParams.width = minVoiceWidth + audioLength * ((maxVoiceWidth - minVoiceWidth) / 57);
        }

        viewHolder.iv_voice_ly.setLayoutParams(layoutParams);

        if (viewHolder.iv_unread_voice != null)
        {
            if (cxMessage.isReader())
            {
                viewHolder.iv_unread_voice.setVisibility(View.GONE);
            } else
            {
                viewHolder.iv_unread_voice.setVisibility(View.VISIBLE);
            }
        }
        viewHolder.iv_voice.setTag(viewHolder.iv_unread_voice);

        viewHolder.iv_voice_ly.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (cxMessage.getDirect() != 0)
                {
                    if (!cxMessage.isReaderStatus())
                    {
                        if (cxMessage.getType() == CxIMMessageType.SINGLE_CHAT.getValue())
                        {
                            cxMessagesReadList = new ArrayList<CxMessage>();
                            cxMessagesReadList.add(cxMessage);
                            setMessageRead(cxMessagesReadList, cxMessage.getImMessage().getHeader().getFrom());
                        }
                    }
                } else
                {

                }

                isAutoPlayAudio = true;
                currentPlayAduioPosition = (int) v.getTag();
                AudioPlayManager.getInstance().setOnVoiceListener(new AudioPlayManager.OnVoiceListener()
                {
                    @Override
                    public void playPositionChange(int currentPosition)
                    {

                    }

                    @Override
                    public void playCompletion()
                    {
//                        int size = audioPositionList.size();
//                        int audioPosition = audioPositionList.indexOf(currentPlayAduioPosition);
//                        audioPosition = getNextUnReaderAudioPosition(audioPosition);
//                        if (audioPosition < size)
//                        { //播放下一条语音
//                            int playPosition = audioPositionList.get(audioPosition);
//                            if (isAutoPlayAudio)
//                            {
//                                audioPlayViewMap.get(playPosition).performClick();
//                            }
//                        }
                    }

                    @Override
                    public void playDuration(int duration)
                    {

                    }

                    @Override
                    public void playException(Exception e)
                    {
                        e.printStackTrace();
                    }

                    @Override
                    public void playStopVioce()
                    {
                        SDLogUtil.debug("im_voice_ChatBaseAdapter——停止的语音播放！");
                    }

                    public int getNextUnReaderAudioPosition(int position)
                    {
                        int nextPosition = -1;
                        for (int i = position + 1; i < audioPositionList.size(); i++)
                        {
                            CxMessage tempMsg = datas.get(i);
                            if (tempMsg != null)
                            {
                                if (!tempMsg.isReader())
                                {
                                    nextPosition = i;
                                    break;
                                }
                            }
                        }
                        if (nextPosition == -1)
                        {
                            nextPosition = audioPositionList.size();
                        }
                        return nextPosition;
                    }
                });

                if (AudioPlayManager.getInstance().getAudioPlayerHandler() != null && AudioPlayManager.getInstance().isPlaying())
                {
                    if (AudioPlayManager.getInstance().getPlayPath().equals(localFile.getAbsolutePath()))
                    {
                        isAutoPlayAudio = false;
                        AudioPlayManager.getInstance().stop();
                        AudioPlayManager.getInstance().close();
                        return;
                    }
                }

                RelativeLayout rl = (RelativeLayout) v;
                ImageView iv = (ImageView) rl.getChildAt(0);

                AudioPlayManager.getInstance().play(context, localFile.getAbsolutePath(), iv);

                ImageView unreadImgView = (ImageView) iv.getTag();
                if (unreadImgView != null)
                {
                    unreadImgView.setVisibility(View.GONE);
                    cxMessage.setReader(true);
                    CXChatManager.updateMsg(cxMessage);
                }
            }

        });
    }

    /**
     * 填充图片
     *
     * @param viewHolder
     * @param CxFileMessage
     * @param localFile
     */
    protected void fillPicture(final ViewHolder viewHolder, final CxMessage cxMessage,
                               CxFileMessage CxFileMessage, File localFile)
    {
        final String imgPath;
        if (localFile.exists())
        {
            imgPath = CxFileMessage.getLocalUrl();
        } else
        {
            imgPath = CxFileMessage.getRemoteUrl();
        }

        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        final int scalX = dm.widthPixels;

        Glide.with(context)
                .load(imgPath)
                .fitCenter()
                .override(scalX / 2, scalX / 2)
                .placeholder(R.mipmap.load_img_init)
                .crossFade()
                .into(viewHolder.iv_sendPicture);

//        if (cxMessage.getType() == CxIMMessageType.SINGLE_CHAT.getValue())
//        {
//            if (cxMessage.getDirect() != 0)
//            {
////                if (cxMessage.isReaderStatus())
////                {
//                Glide.with(context)
//                        .load(imgPath)
//                        .fitCenter()
//                        .override(scalX / 2, scalX / 2)
//                        .placeholder(R.mipmap.load_img_init)
//                        .crossFade()
//                        .into(viewHolder.iv_sendPicture);
////                } else
////                {
////                    Glide.with(context).load(R.mipmap.smallvideoreastayplay).fitCenter()
////                            .override(scalX / 3, scalX / 3).into(viewHolder.iv_sendPicture);
////                }
//            } else
//            {
//                Glide.with(context)
//                        .load(imgPath)
//                        .fitCenter()
//                        .override(scalX / 2, scalX / 2)
//                        .placeholder(R.mipmap.load_img_init)
//                        .crossFade()
//                        .into(viewHolder.iv_sendPicture);
//            }
//        }
//
//        if (cxMessage.getType() == CxIMMessageType.GROUP_CHAT.getValue())
//        {
//            if (cxMessage.getDirect() != 0)
//            {
////                if (cxMessage.isReaderStatus())
////                {
//                Glide.with(context)
//                        .load(imgPath)
//                        .fitCenter()
//                        .override(scalX / 2, scalX / 2)
//                        .placeholder(R.mipmap.load_img_init)
//                        .crossFade()
//                        .into(viewHolder.iv_sendPicture);
////                } else
////                {
////                    Glide.with(context).load(R.mipmap.smallvideoreastayplay).fitCenter()
////                            .override(scalX / 3, scalX / 3).into(viewHolder.iv_sendPicture);
////                }
//            } else
//            {
//                Glide.with(context)
//                        .load(imgPath)
//                        .fitCenter()
//                        .override(scalX / 2, scalX / 2)
//                        .placeholder(R.mipmap.load_img_init)
//                        .crossFade()
//                        .into(viewHolder.iv_sendPicture);
//            }
//        }

        viewHolder.iv_sendPicture.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                if (cxMessage.isReaderStatus())
//                {
                Intent intent = new Intent(context, BigImgActivity.class);
                intent.putExtra(BigImgActivity.EXTR_PATH, imgPath);
                context.startActivity(intent);
//                } else
//                {
//                    if (cxMessage.getType() == CxIMMessageType.SINGLE_CHAT.getValue())
//                    {
//                        cxMessagesReadList = new ArrayList<CxMessage>();
//                        cxMessagesReadList.add(cxMessage);
//                        setMessageRead(cxMessagesReadList, cxMessage.getImMessage().getHeader().getFrom());
//                    }
//
//                    Glide.with(context)
//                            .load(imgPath)
//                            .fitCenter()
//                            .override(scalX / 2, scalX / 2)
//                            .placeholder(R.mipmap.load_img_init)
//                            .crossFade()
//                            .into(viewHolder.iv_sendPicture);
//
//                }
            }
        });

    }

    private static String getValueByKey(String key, Map<String, String> map)
    {
        boolean isBurnAfterRead = map.containsKey(key);
        if (!isBurnAfterRead)
        {
            return "0";
        } else
        {
            return map.get(key);
        }

    }

    /**
     * 填充附件内容
     *
     * @param viewHolder
     * @param CxFileMessage
     * @param localFile
     * @desc 文件缓存逻辑:先获取本地路径,
     * 查看是否存在文件,是直接打开,
     * 否则从文件缓存目录找,存在直
     * 接打开,否则获取远程文件地址进行下载并打开
     */
    protected void fillFile(final ViewHolder viewHolder, final CxFileMessage CxFileMessage, final File localFile, int direct)
    {
        viewHolder.percentage.setVisibility(View.GONE);
        final File cacheFile = new File(Config.CACHE_FILE_DIR, CxFileMessage.getName());
        if (cacheFile.exists())
        {
            viewHolder.tv_file_state.setText("已下载");
        } else
        {
            viewHolder.tv_file_state.setText("未下载");
        }

        viewHolder.ll_file_container.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (localFile.exists())
                {
                    FileUtill.openFile(localFile, context);
                } else
                {
                    if (cacheFile.exists())
                    {
                        FileUtill.openFile(cacheFile, context);
                    } else
                    {
                        viewHolder.percentage.setVisibility(View.VISIBLE);
                        OkHttpUtils.get().url(CxFileMessage.getRemoteUrl()).build().execute(new FileCallBack(Config
                                .CACHE_FILE_DIR, CxFileMessage.getName())
                        {
                            @Override
                            public void inProgress(float progress)
                            {
                                float b = (float) (Math.round(progress * 100)) / 100;
                                viewHolder.percentage.setText(b + "%");
                            }

                            @Override
                            public void onError(Request request, Exception e) throws Exception
                            {
                                viewHolder.tv_file_state.setText("下载失败");
                                viewHolder.percentage.setVisibility(View.GONE);
                            }

                            @Override
                            public void onResponse(File response) throws Exception
                            {
                                FileUtill.openFile(cacheFile, context);
                                viewHolder.tv_file_state.setText("已下载");
                                viewHolder.percentage.setVisibility(View.GONE);
                            }
                        });
                    }
                }
            }
        });

        viewHolder.tv_file_name.setText(CxFileMessage.getName());
        viewHolder.tv_file_size.setText(FileUtill.formetFileSize(CxFileMessage.getSize()));
        if (direct == CxIMMessageDirect.SEND)
        {
            viewHolder.tv_file_state.setVisibility(View.GONE);
        } else
        {
            viewHolder.tv_file_state.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 填充位置信息
     *
     * @param cxMessage
     * @param viewHolder
     */
    protected void fillPosition(CxMessage cxMessage, ViewHolder viewHolder)
    {
        final CxGeoMessage CxGeoMessage = cxMessage.getGeoMessage();

        viewHolder.tv_location.setText(CxGeoMessage.getAddress());

        viewHolder.tv_location.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, BaiduMapActivity.class);
                intent.putExtra(BaiduMapActivity.RESULT_LATITUDE, CxGeoMessage.getLatitude());
                intent.putExtra(BaiduMapActivity.RESULT_LONGITUDE, CxGeoMessage.getLongitude());
                intent.putExtra(BaiduMapActivity.RESULT_ADDRESS, CxGeoMessage.getAddress());
                context.startActivity(intent);
            }
        });
    }

    /**
     * 填充发送的view
     *
     * @param convertView
     * @param viewHolder
     */
    protected void findSendPulicView(View convertView, ViewHolder viewHolder)
    {
        viewHolder.msg_status = (ImageView) convertView.findViewById(R.id.msg_status);
        viewHolder.pb_sending = (ProgressBar) convertView.findViewById(R.id.pb_sending);
        viewHolder.icon_baidu = (ImageView) convertView.findViewById(R.id.icon_baidu);
    }

    protected void findTextPulicView(View convertView, ViewHolder viewHolder)
    {
        viewHolder.rl_content = (RelativeLayout) convertView.findViewById(R.id.rl_content);
        viewHolder.rl_content_new = (RelativeLayout) convertView.findViewById(R.id.rl_content_new);
        viewHolder.ll_hot_message_layout = (LinearLayout) convertView.findViewById(R.id.ll_hot_message_layout);
        viewHolder.timestamp = (TextView) convertView.findViewById(R.id.timestamp);
        viewHolder.iv_userhead = (ImageView) convertView.findViewById(R.id.img_user_icon);
        viewHolder.tv_chatcontent = (EmojiconTextView) convertView.findViewById(R.id.tv_chatcontent);
        viewHolder.tv_ack = (TextView) convertView.findViewById(R.id.tv_ack);
        viewHolder.tv_userid = (TextView) convertView.findViewById(R.id.tv_user_name);

    }

    //分享的
    protected void findShareData(final CxMessage cxMessage, ViewHolder viewHolder)
    {
        Map<String, String> attachmentMap = new HashMap<String, String>();
        attachmentMap = cxMessage.getImMessage().getHeader().getAttachment();
        boolean isShareType = false;
        if (attachmentMap.get("shareDic") != null)
        {
            if (attachmentMap.size() > 0 && attachmentMap.get("shareDic").length() > 0)
            {
                final ShareDicEntity shareDicEntity = ShareDicEntity.parse(attachmentMap.get("shareDic"));
                if (StringUtils.notEmpty(shareDicEntity))
                {
                    Glide.with(context)
                            .load(shareDicEntity.getShareIconUrl())
                            .fitCenter()
                            .placeholder(R.mipmap.ic_launcher)
                            .error(R.mipmap.ic_launcher)
                            .crossFade()
                            .into(viewHolder.img_icon_share);

                    viewHolder.tv_share_title.setText(shareDicEntity.getShareTitle());
                    viewHolder.tv_share_content.setText(shareDicEntity.getShareContent());
                }
                viewHolder.ll_content.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Uri uri = Uri.parse(shareDicEntity.getShareUrl());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        context.startActivity(intent);
                    }
                });
            }
        }
    }

    protected void fillListener(ViewHolder viewHolder, int postion)
    {
        View view = null;
        if (viewHolder.rl_content_new != null)
        {
            view = viewHolder.rl_content_new;
        } else if (viewHolder.iv_sendPicture != null)
        {
            view = viewHolder.iv_sendPicture;
        } else if (viewHolder.tv_location != null)
        {
            view = viewHolder.tv_location;
        } else if (viewHolder.ll_file_container != null)
        {
            view = viewHolder.ll_file_container;
        } else if (viewHolder.iv_voice_ly != null)
        {
            view = viewHolder.iv_voice_ly;
        }

        final int tempPostion = postion;
        if (view != null)
        {
            view.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    if (onAdapterListener != null)
                    {
                        SDLogUtil.debug("data size ==> " + datas.size());
                        SDLogUtil.debug("delete position ==> " + tempPostion);
                        onAdapterListener.onItemLongClickListener(v, tempPostion);
                    }
                    return true;
                }
            });
        }
    }

    public interface OnAdapterListener
    {
        void onItemLongClickListener(View view, final int position);

        //0是复制的，1是图片转发的。
        void onChatContentLongClickListener(int i, CxMessage cxMessage);
    }

    public OnAdapterListener getOnAdapterListener()
    {
        return onAdapterListener;
    }

    public void setOnAdapterListener(OnAdapterListener onAdapterListener)
    {
        this.onAdapterListener = onAdapterListener;
    }
}
