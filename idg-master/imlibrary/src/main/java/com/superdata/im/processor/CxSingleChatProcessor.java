package com.superdata.im.processor;

import android.content.Context;

import com.chaoxiang.base.utils.SDLogUtil;
import com.superdata.im.constants.CxIMMessageType;
import com.superdata.im.entity.CxMessage;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2015-12-30
 * @desc 单聊处理器
 */
public class CxSingleChatProcessor extends CxChatCxBaseProcessor
{
    public CxSingleChatProcessor(Context context)
    {
        super(context);
    }

    @Override
    public boolean doErrMsg(CxMessage msg)
    {
        return super.doErrMsg(msg);
    }

    @Override
    public void doSuccessMsg(final CxMessage msg)
    {
        msg.setType(CxIMMessageType.SINGLE_CHAT.getValue());
        super.doSuccessMsg(msg);
        SDLogUtil.debug("推送---CxSingleChatProcessor:" + msg.toString());
    }
}
