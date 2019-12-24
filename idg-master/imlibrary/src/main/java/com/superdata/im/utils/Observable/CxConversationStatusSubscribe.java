package com.superdata.im.utils.Observable;

import com.superdata.im.entity.CxMessage;

import java.util.Observable;
import java.util.Observer;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-01-15
 * @desc 会话状态订阅者,用于监听接收消息时,是否需要创建会话和更新会话最后一条消息
 */
public class CxConversationStatusSubscribe implements Observer {

    public CxConversationStatusSubscribe(){

    }



    @Override
    public void update(Observable observable, Object data) {
        if(data instanceof CxMessage){
            CxMessage cxMessage = (CxMessage) data;

        }
    }



}
