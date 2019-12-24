package com.superdata.im.callback;

import com.superdata.im.entity.CxMessage;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-02-17
 * @desc
 */
public interface CxReceiverMsgCallback
{
    void onReceiver(CxMessage cxMessage);
}
