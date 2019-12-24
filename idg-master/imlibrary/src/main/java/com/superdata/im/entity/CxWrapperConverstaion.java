package com.superdata.im.entity;

import com.chaoxiang.entity.conversation.IMConversation;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-01-13
 * @desc
 */
public class CxWrapperConverstaion
{

    public CxWrapperConverstaion(OperationType operationType, IMConversation conversation) {
        this.operationType = operationType;
        this.conversation = conversation;
    }

    /**
     * 会话操作类型
     */
    public enum OperationType {
        ADD_CONVERSTAION(1), //添加会话
        UPDATE_CONVERSTAION(2), //更新会话
        DELETE_CONVERSTAION(3); //删除会话
        private int value;

        OperationType(int value){
            this.value = value;
        }

        public int getValue(){
            return value;
        }
    }
    private OperationType operationType;
    private IMConversation conversation;

    public OperationType getOperationType() {
        return operationType;
    }

    public IMConversation getConversation() {
        return conversation;
    }
}
