package com.superdata.im.utils.Observable;

import com.superdata.im.callback.CxUpdateUICallback;
import com.superdata.im.constants.CxIMMessageType;
import com.superdata.im.entity.CxMessage;

import java.util.Observable;
import java.util.Observer;

/**
 * @authon zjh
 * @date 2016-01-04
 * @desc 推送订阅者
 * @version v1.0.0
 */
public class CxPushSubscribe implements Observer{
    private CxUpdateUICallback callback;

    public CxPushSubscribe(CxUpdateUICallback callback){
        this.callback = callback;
    }

    @Override
    public void update(Observable observable, Object data) {
        if(data instanceof CxMessage){
            CxMessage cxMessage = (CxMessage) data;
            if(cxMessage.getType() == CxIMMessageType.PUSH.getValue()){
                dealData();
                if(callback != null){
                    callback.updateUI(cxMessage);
                }
            }
        }
    }

    /**
     * 处理数据
     */
    private void dealData() {
        //TODO 处理数据
    }
}
