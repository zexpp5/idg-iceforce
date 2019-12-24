package com.superdata.im.utils.Observable;

import com.chaoxiang.entity.chat.IMAddFriend;

import java.util.Observable;
import java.util.Observer;

/**
 * @des
 */
public class CxAddFriendSubscribe implements Observer
{
    private AddFriendListener addFriendListener;
    
    public CxAddFriendSubscribe(AddFriendListener listener)
    {
        this.addFriendListener = listener;
    }

    @Override
    public void update(Observable observable, Object data)
    {
        if (data != null)
        {
            if (data instanceof Integer)
            {
                if (addFriendListener != null)
                {
                    addFriendListener.acceptAddFriendInfo((int) data);
                }
            }
        }
    }

    public interface AddFriendListener
    {
        void acceptAddFriendInfo(int addFriendStatus);
    }
}
