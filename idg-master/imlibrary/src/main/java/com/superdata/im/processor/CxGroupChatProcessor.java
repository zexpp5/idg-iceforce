package com.superdata.im.processor;

import android.content.Context;

import com.chaoxiang.base.utils.SDLogUtil;
import com.superdata.im.constants.CxIMMessageType;
import com.superdata.im.entity.CxMessage;
import com.superdata.im.utils.Observable.CxAddUnReadObservale;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2015-12-30
 * @desc 群聊处理器
 */
public class CxGroupChatProcessor extends CxChatCxBaseProcessor
{
    public CxGroupChatProcessor(Context context)
    {
        super(context);
    }

    @Override
    public void doSuccessMsg(final CxMessage msg)
    {
        msg.setType(CxIMMessageType.GROUP_CHAT.getValue());
        super.doSuccessMsg(msg);
    }

    public boolean doErrMsg(CxMessage msg)
    {
        return super.doErrMsg(msg);
    }
}
