package com.superdata.im.callback;

import com.superdata.im.entity.CxFileMessage;

/**
 * @version v1.0.0
 * @authon 修改了类名
 * @desc
 */
public interface CxFileUploadCallback
{
    void onSuccess(CxFileMessage CxFileMessage);

    void onError();
}
