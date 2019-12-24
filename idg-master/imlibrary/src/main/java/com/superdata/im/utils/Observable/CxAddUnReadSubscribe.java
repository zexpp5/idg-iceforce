package com.superdata.im.utils.Observable;

import java.util.Observable;
import java.util.Observer;

/**
 * @des
 */
public class CxAddUnReadSubscribe implements Observer
{
    private AddUnReadListener addUnReadListener;

    public CxAddUnReadSubscribe(AddUnReadListener listener)
    {
        this.addUnReadListener = listener;
    }

    @Override
    public void update(Observable observable, Object data)
    {
        if (data != null)
        {
            if (data instanceof Integer)
            {
                if (addUnReadListener != null)
                {
                    addUnReadListener.acceptAddUnReadInfo((int) data);
                }
            }
        }
    }

    public interface AddUnReadListener
    {
        void acceptAddUnReadInfo(int addUnReadStatus);
    }
}
