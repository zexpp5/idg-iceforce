package com.superdata.im.processor;

import android.content.Context;

import com.chaoxiang.base.utils.SDLogUtil;
import com.superdata.im.callback.CxReadMessageCallBack;
import com.superdata.im.entity.CxMessage;

/**
 * User: Selson
 * Date: 2016-09-18
 * FIXME
 */
public class CxReadProcessor extends CxBaseProcessor
{
    private CxReadMessageCallBack cxReadMessageCallBack;

    public CxReadProcessor(Context context)
    {
        super(context);
    }

    @Override
    public boolean doErrMsg(CxMessage cxMessage)
    {
        return false;
    }

    @Override
    public void doSuccessMsg(CxMessage msg)
    {
        SDLogUtil.debug("im_processor_read==>" + ((CxMessage) msg).toString());
//        PushModelEntity pushModelEntity = ParseUtils.parsePlushJson(msg.getImMessage().getBody());
//        String messageId = msg.getImMessage().getHeader().getMessageId();
//        收到对方已阅读。
//        if (map.get(type).equals(PlushType.PUSH_READ_FROM_MESSAGE_STATE))
//        {
        //解析结构
//               SDGson.toJson();
        cxReadMessageCallBack.cxMessgaeReadStatus(msg);
        //更新数据库
        //更新UI
//        }
    }

    public CxReadMessageCallBack getCxReadMessageCallBack()
    {
        return this.cxReadMessageCallBack;
    }

    public void setCxReadMessageCallBack(CxReadMessageCallBack cxReadMessageCallBack)
    {
        this.cxReadMessageCallBack = cxReadMessageCallBack;
    }
}