package com.superdata.im.processor;

import android.content.Context;

import com.superdata.im.entity.CxMessage;
import com.superdata.im.utils.Observable.CxAddUnReadObservale;
import com.superdata.im.utils.Observable.CxWebRtcObservable;

/**
 * @auth lwj
 * @date 2016-01-30
 * @desc
 */
public class CxWebrtcProcessor extends CxBaseProcessor
{
    @Override
    public boolean doErrMsg(CxMessage cxMessage)
    {
        return false;
    }

    public CxWebrtcProcessor(Context context)
    {
        super(context);
    }

    @Override
    public void doSuccessMsg(CxMessage cxMessage)
    {
        CxWebRtcObservable.getInstance().notifyUpdate(cxMessage);
        CxAddUnReadObservale.getInstance().sendAddUnRead(1);
    }
}
