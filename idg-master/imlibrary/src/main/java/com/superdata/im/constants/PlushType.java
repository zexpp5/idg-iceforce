package com.superdata.im.constants;

/**
 * @author zjh
 *         推送的类型
 */
public class PlushType
{
    /**
     * 通讯录
     */
    public final static String PLUSH_CONTACT = "contact";

    /**
     * 消息通知
     */
    public final static String PLUSH_NOTIFY = "notice";

    /**
     * 语音会议
     */
    public final static String PLUSH_VOICEMEETING = "voicemeeting";

    /**
     * 语音会议的所有信息接收
     */
    public final static String PLUSH_VOICEMEETING_GROUP_TYPE = "2";

    /**
     * 被动的接收到添加好友的信息 别人扫我。
     */
    public final static int PLUSH_ADD_FRIEND_PASSIVE = 1119;

    /**
     * 主动添加好友的时候推送 我扫别人
     */
    public final static String PLUSH_ADD_FRIEND_INITAITIVE = "addFriend";

    /**
     * 新的工作圈推送-工作圈接收人
     */
    public final static String PLUSH_NEW_WORK_CIRCLE = "370";
    /**
     * 新的评论-工作圈提交评论
     */
    public final static String PLUSH_NEW_WORK_COMMENT = "36";



    //云境-批审推送等-在线
    public final static int PUSH_YUNJING_ONLINE = 1118;
    //云境-批审推送等-离线
    public final static int PUSH_YUNJING_OFF_LINE = 15;
}
