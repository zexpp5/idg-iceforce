package com.superdata.im.processor;

import android.content.Context;
import android.os.PowerManager;

import com.chaoxiang.base.utils.SDLogUtil;
import com.superdata.im.entity.CxMessage;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2015-12-30
 * @desc 处理器基类
 */
public abstract class CxBaseProcessor implements CxIProcessor
{
    PowerManager pm;
    PowerManager.WakeLock wl;
    protected Context context;
    protected DealMsgCallback dealMsgCallback;

    public CxBaseProcessor(Context context)
    {
        pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "cx_processor_wake");
        this.context = context;
    }

    @Override
    public void doMsg(Object msg)
    {
        if (!(msg instanceof CxMessage))
        {
            return;
        }
        SDLogUtil.debug("im_processor==>"+((CxMessage) msg).toString());
        if (dealMsgCallback != null)
        {
            dealMsgCallback.successBefore((CxMessage) msg);
        }

        //iM推送的
        doSuccessMsg((CxMessage) msg);
        //外层推送的
        // doCxAppSuccessMsg((CxMessage) msg);

        if (dealMsgCallback != null)
        {
            dealMsgCallback.successAfter((CxMessage) msg);
        }
    }

    @Override
    public boolean doErrorMsg(Object msg)
    {
        if (!(msg instanceof CxMessage))
        {
            return false;
        }
        SDLogUtil.debug("error processor ==>" + this.getClass().getSimpleName() + ":" + ((CxMessage) msg).getImMessage().getBody());
        if (dealMsgCallback != null)
        {
            dealMsgCallback.errorBefore((CxMessage) msg);
        }

        boolean isDestroy = doErrMsg((CxMessage) msg); //是否销毁消息不继续传递给下一个处理器
        //doCxAppErrMsg((CxMessage) msg);//外层推送的
        if (dealMsgCallback != null)
        {
            dealMsgCallback.errorAfter((CxMessage) msg);
        }

        return isDestroy;
    }

    public abstract boolean doErrMsg(CxMessage cxMessage);

    public abstract void doSuccessMsg(CxMessage cxMessage);

//    public abstract boolean doCxAppErrMsg(CxMessage cxMessage);
//
//    public abstract void doCxAppSuccessMsg(CxMessage cxMessage);

    public interface DealMsgCallback
    {
        void successBefore(CxMessage cxMessage);

        void successAfter(CxMessage cxMessage);

        void errorBefore(CxMessage cxMessage);

        void errorAfter(CxMessage cxMessage);
    }

    public DealMsgCallback getDealMsgCallback()
    {
        return dealMsgCallback;
    }

    public void setDealMsgCallback(DealMsgCallback dealMsgCallback)
    {
        this.dealMsgCallback = dealMsgCallback;
    }
}
