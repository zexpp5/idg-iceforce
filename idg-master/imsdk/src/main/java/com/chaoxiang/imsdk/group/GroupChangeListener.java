package com.chaoxiang.imsdk.group;

import com.chaoxiang.entity.chat.IMMessage;
import com.chaoxiang.entity.group.IMGroup;
import com.chaoxiang.imsdk.IMListener;

import java.util.List;

/**
 * 群组状态变化监听
 */
public interface GroupChangeListener extends IMListener
{
    /**
     * 移除用户
     */
    void onUserRemoved(List<IMMessage> messages);

    /**
     * 被踢出群
     */
    void onGroupDestroy(List<GroupChangeListener.Key> groups);

    /**
     * 群组修改
     */
    void onGroupChange(List<IMGroup> groups);


    /**
     * 邀请的用户
     */
    void onInvitationReceived(List<IMMessage> messages);

    class Key
    {

        private Key()
        {
        }

        public Key(String operator, String groupId)
        {
            this.operator = operator;
            this.groupId = groupId;
        }

        private String operator;
        private String groupId;

        public String getGroupId()
        {
            return groupId;
        }

        public void setGroupId(String groupId)
        {
            this.groupId = groupId;
        }

        public String getOperator()
        {
            return operator;
        }

        public void setOperator(String operator)
        {
            this.operator = operator;
        }
    }
}