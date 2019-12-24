package com.superdata.im.constants;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-02-17
 * @desc 聊天管理类型
 */
public enum CxChatManageType
{

    CREATE_GROUP(1), //创建群
    DELETE_GROUP(2), //删除群
    GROUP_ADD_USER(3), //群主加人
    GROUP_REMOVE_USER(4),//群主删人
    GROUP_ACTIVITY_REMOVE(5),//主动退群
    AV_CANCEL(6), // 音视频取消
    AV_DISCONNED(7),// 音视频无响应
    AV_REFUSE(8),// 音视频拒绝接听
    AV_FINISH(9); // 音视频通话结束

    private int value;

    CxChatManageType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}

