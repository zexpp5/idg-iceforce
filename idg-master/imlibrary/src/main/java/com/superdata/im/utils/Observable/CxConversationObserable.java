package com.superdata.im.utils.Observable;

import com.superdata.im.entity.CxWrapperConverstaion;

import java.util.Observable;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-01-12
 * @desc 会话发布者
 */
public class CxConversationObserable extends Observable
{
    private CxConversationObserable()
    {

    }

    private static CxConversationObserable conversationObserable;

    public static CxConversationObserable getInstance()
    {
        if (conversationObserable == null)
        {
            conversationObserable = new CxConversationObserable();
        }
        return conversationObserable;
    }


    /**
     * 通知订阅者,有新消息
     *
     * @param conversation
     */
    public void notifyUpdate(CxWrapperConverstaion conversation)
    {
        setChanged();
        notifyObservers(conversation);
    }
}
