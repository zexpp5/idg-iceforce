package com.superdata.im.constants;

import java.io.Serializable;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-01-04
 * @desc 消息类型
 */
public enum CxIMMessageType implements Serializable
{
    //推送
    PUSH(1),
    //登陆
    LOGIN(2),
    //单聊
    SINGLE_CHAT(3),
    //群聊
    GROUP_CHAT(4),
    //应答
    ANSWER(5),
    //点对点音视频聊天
    SINGLE_VIDEO_OR_AUDIO_CHAT(6),
    //离线推送
    OFFLINE_PUSH(7),
    //语音会议
    MEETING_CHAT(8),
    //已读未读
    READ_STATUS(10),
    //通知公告
    CHAT_NOTICE(888),
    //公众号
    CHAT_COMMON(999),
    //系统消息
    CHAT_SYSTEM_MESSAGE(777),
    //newsletter
    CHAT_NEWS_LETTER(6666),
    //新闻公告
    CHAT_NEWS_LIST(7777);

    private int value;

    CxIMMessageType(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
}
