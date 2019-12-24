package com.superdata.im.utils.Observable;

import java.util.Observable;
import java.util.Observer;

/**
 * @des 工作圈的订阅者
 */
public class CxWorkCircleSubscribe implements Observer
{
    private WorkCircleListener workCircleListener;

    public CxWorkCircleSubscribe(WorkCircleListener listener)
    {
        this.workCircleListener = listener;
    }

    @Override
    public void update(Observable observable, Object data)
    {
        if (data != null)
        {
            if (data instanceof Integer)
            {
                if (workCircleListener != null)
                {
                    workCircleListener.acceptWorkCircleInfo((int) data);
                }
            }
        }
    }

    public interface WorkCircleListener
    {
        void acceptWorkCircleInfo(int workCircleStatus);
    }
}
