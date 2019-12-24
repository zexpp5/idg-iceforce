package com.superdata.im.callback;

import com.superdata.im.entity.CxMessage;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-01-04
 * @desc 用于观察者接收到消息后更新UI
 */
public interface CxUpdateUICallback
{
    void updateUI(CxMessage cxMessage);
}
