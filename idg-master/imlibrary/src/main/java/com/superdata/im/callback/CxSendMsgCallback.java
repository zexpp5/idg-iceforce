package com.superdata.im.callback;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-01-04
 * @desc 发送消息回调
 */
public abstract class CxSendMsgCallback implements CxIMCallback
{
    public abstract void socketTimeOut(String msgId);

    public void onProgress(long progress, long total)
    {
    }
    public abstract void reConSuccessAndReSendMsg(String msgId);
}
