package com.cxgz.activity.cxim.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.chaoxiang.imsdk.chat.NoticeType;
import com.cx.webrtc.WebRtcClient;
import com.im.client.MediaType;
import com.injoy.idg.R;
import com.superdata.im.entity.CxFileMessage;
import com.superdata.im.entity.CxMessage;

import java.io.File;
import java.util.List;


/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-01-07
 * @promble 自动播放语音效率较低（后期需做修改）
 */
public class ChatAdapter extends ChatBaseAdapter
{
    public ChatAdapter(Context context, List<CxMessage> cxMessages)
    {
        super(context, cxMessages);
        this.context = context;
        init();
    }

    private void init()
    {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        maxVoiceWidth = (int) (screenWidth * 0.55);
        minVoiceWidth = (int) (screenWidth * 0.20);
        voicerate = (float) minVoiceWidth / (float) maxVoiceWidth;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final CxMessage cxMessage = (CxMessage) getItem(position);
        ViewHolder viewHolder = null;

        int mediaType = cxMessage.getMediaType();
        if (isShareType(cxMessage))
        {
            mediaType = NoticeType.TYPE_SHARE;
        }
        if (convertView == null)
        {
            convertView = createConverView(position);
            viewHolder = new ViewHolder();
            if (mediaType == MediaType.TEXT.value())
            {
                viewHolder.tv_countdown = (TextView) convertView.findViewById(R.id.tv_countdown);
                viewHolder.ll_container = (LinearLayout) convertView.findViewById(R.id.ll_container);
                //文本类型
//                findTextPulicView(convertView, viewHolder);
            } else if (mediaType == MediaType.PICTURE.value())
            {
                viewHolder.tv_countdown = (TextView) convertView.findViewById(R.id.tv_countdown);
                //图片类型
                viewHolder.iv_sendPicture = (ImageView) convertView.findViewById(R.id.iv_sendPicture);
                //图片类型
                viewHolder.iv_hot_see = (ImageView) convertView.findViewById(R.id.iv_hot_see);
                viewHolder.row_recv_pic=(RelativeLayout) convertView.findViewById(R.id.row_recv_pic);
                viewHolder.row_send_pic=(RelativeLayout) convertView.findViewById(R.id.row_send_pic);

            } else if (mediaType == MediaType.POSITION.value())
            {
                //位置类型
                viewHolder.tv_location = (TextView) convertView.findViewById(R.id.tv_location);
            } else if (mediaType == MediaType.FILE.value())
            {
                //文件类型
                viewHolder.tv_file_name = (TextView) convertView.findViewById(R.id.tv_file_name);
                viewHolder.tv_file_size = (TextView) convertView.findViewById(R.id.tv_file_size);
                viewHolder.tv_file_state = (TextView) convertView.findViewById(R.id.tv_file_state);
                viewHolder.percentage = (TextView) convertView.findViewById(R.id.percentage);
                viewHolder.ll_file_container = (LinearLayout) convertView.findViewById(R.id.ll_file_container);
            } else if (mediaType == MediaType.AUDIO.value())
            {
                //语音类型
                viewHolder.iv_voice = (ImageView) convertView.findViewById(R.id.iv_voice);
                viewHolder.tv_length = (TextView) convertView.findViewById(R.id.tv_length);
                viewHolder.iv_voice_ly = (RelativeLayout) convertView.findViewById(R.id.iv_voice_ly);
                viewHolder.iv_unread_voice = (ImageView) convertView.findViewById(R.id.iv_unread_voice);
                viewHolder.tv_countdown = (TextView) convertView.findViewById(R.id.tv_countdown);
                viewHolder.hot_lock_bg_img = (ImageView) convertView.findViewById(R.id.hot_lock_bg_img);
                viewHolder.hot_not_lock_bg_img = (ImageView) convertView.findViewById(R.id.hot_not_lock_bg_img);
            } else if (mediaType == MediaType.VIDEO.value())
            {
                //视频
                viewHolder.percentage = (TextView) convertView.findViewById(R.id.percentage);
                viewHolder.msg_status = (ImageView) convertView.findViewById(R.id.msg_status);
                viewHolder.pb_sending = (ProgressBar) convertView.findViewById(R.id.pb_sending);
                viewHolder.chatting_content_iv = (ImageView) convertView.findViewById(R.id.chatting_content_iv);
                viewHolder.tv_countdown = (TextView) convertView.findViewById(R.id.tv_countdown);
                viewHolder.hot_lock_bg_img = (ImageView) convertView.findViewById(R.id.hot_lock_bg_img);
                viewHolder.hot_not_lock_bg_img = (ImageView) convertView.findViewById(R.id.hot_not_lock_bg_img);
            } else if (mediaType == NoticeType.NORMAL_TYPE)
            {
                //通知
                viewHolder.tv_notice_message = (TextView) convertView.findViewById(R.id.tv_notice_msg);
            } else if (mediaType == MediaType.AUDIO.value())
            {
                //语音聊天
//                findTextPulicView(convertView, viewHolder);
            } else if (mediaType == MediaType.VIDEO.value())
            {
                //视频聊天
//                findTextPulicView(convertView, viewHolder);
            } else if (mediaType == NoticeType.TYPE_SHARE)
            {
                viewHolder.img_icon_share = (ImageView) convertView.findViewById(R.id.img_icon_share);
                viewHolder.tv_share_title = (TextView) convertView.findViewById(R.id.tv_share_title);
                viewHolder.tv_share_content = (TextView) convertView.findViewById(R.id.tv_share_content);
                viewHolder.ll_content = (LinearLayout) convertView.findViewById(R.id.ll_content);
            }

            findSendPulicView(convertView, viewHolder);
            findTextPulicView(convertView, viewHolder);
            convertView.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (mediaType == MediaType.TEXT.value())
        {
            //文本类型
            fillPulbicTextChat(cxMessage, viewHolder);
        } else if (mediaType == MediaType.POSITION.value())
        {
            //位置信息
            fillPosition(cxMessage, viewHolder);
        } else if (mediaType == WebRtcClient.AUDIO_MEDIATYPE)
        {
            //语音聊天
            fillAudioChat(cxMessage, viewHolder);
        } else if (mediaType == WebRtcClient.VIDEO_MEDIATYPE)
        {
            //视频聊天
            fillVideoChat(cxMessage, viewHolder);
        } else if (mediaType == NoticeType.NORMAL_TYPE)
        {
            //通知
            fillNotice(context, cxMessage, viewHolder);
        } else if (mediaType == NoticeType.TYPE_SHARE)
        {
            //分享
            findShareData(cxMessage, viewHolder);
        } else
        {
            final CxFileMessage CxFileMessage = cxMessage.getFileMessage();
            if (CxFileMessage != null)
            {
                final File localFile = new File(CxFileMessage.getLocalUrl());
                if (mediaType == MediaType.PICTURE.value())
                {
                    //图片类型
                    fillPicture(viewHolder, cxMessage, CxFileMessage, localFile);
                } else if (mediaType == MediaType.FILE.value())
                {
                    //文件
                    fillFile(viewHolder, CxFileMessage, localFile, cxMessage.getDirect());
                } else if (mediaType == MediaType.AUDIO.value())
                {
                    //语音类型
                    if (localFile.exists())
                    {
                        fillAudio(position, viewHolder, cxMessage, localFile);
                    }
                } else if (mediaType == MediaType.VIDEO.value())
                {
                    //视频
                    fillVideo(viewHolder, cxMessage, localFile, cxMessage.getDirect(), position);
                }
            }
        }

        fillUserInfo(cxMessage, viewHolder);
        fillTimestamp(cxMessage, (CxMessage) getItem(position - 1), position, viewHolder);
        fillName(cxMessage, viewHolder);
        fillMsgStatus(cxMessage, viewHolder, position);
        fillListener(viewHolder, position);

        return convertView;
    }

    public void clearAllMsg()
    {
        getCxMessages().clear();
        audioPositionList.clear();
        audioPlayViewMap.clear();
    }

}
