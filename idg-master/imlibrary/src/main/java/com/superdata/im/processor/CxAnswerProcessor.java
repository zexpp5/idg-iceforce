package com.superdata.im.processor;

import android.content.Context;

import com.superdata.im.entity.CxMessage;
import com.superdata.im.utils.Observable.CxIMObservable;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-01-04
 * @desc 应答处理器
 */
public class CxAnswerProcessor extends CxBaseProcessor
{

    public CxAnswerProcessor(Context context)
    {
        super(context);
    }

    @Override
    public boolean doErrMsg(CxMessage msg)
    {
        return false;
    }

    @Override
    public void doSuccessMsg(CxMessage msg)
    {
        CxIMObservable.getInstance().notifyUpdate(msg);
    }
}
