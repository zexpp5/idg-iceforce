package com.superdata.im.utils.Observable;

import com.chaoxiang.entity.chat.IMAddFriend;

import java.util.Observable;

/**
 *会话发布者
 */
public class CxAddFriendObservale extends Observable
{
    private CxAddFriendObservale()
    {

    }

    private static CxAddFriendObservale cxAddFriendObservale;

    public static CxAddFriendObservale getInstance()
    {
        if (cxAddFriendObservale == null)
        {
            cxAddFriendObservale = new CxAddFriendObservale();
        }
        return cxAddFriendObservale;
    }

    public void sendAddFriend(int addFriendStatus)
    {
        //该方法必须调用，否则观察者无效
        setChanged();
        //触发该方法通知观察者更新UI
        notifyObservers(addFriendStatus);
    }
}
