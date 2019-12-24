package com.superdata.im.callback;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-01-04
 * @desc IM回调
 */
public interface CxIMCallback
{
    void success(String msgId, long timeMillis);

    void error(String msgId);
}
