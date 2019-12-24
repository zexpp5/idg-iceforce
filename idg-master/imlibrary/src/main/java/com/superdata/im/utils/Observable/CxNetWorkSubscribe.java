package com.superdata.im.utils.Observable;

import com.superdata.im.entity.CxNetworkStatus;

import java.util.Observable;
import java.util.Observer;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-02-18
 * @desc
 */
public class CxNetWorkSubscribe implements Observer
{

    private NetWorkChangeCallback callback;

    public CxNetWorkSubscribe(NetWorkChangeCallback callback)
    {
        this.callback = callback;
    }

    @Override
    public void update(Observable observable, Object data)
    {
        if (data instanceof CxNetworkStatus)
        {
            if (callback != null)
            {
                callback.netWorkChange((CxNetworkStatus) data);
            }
        }
    }


    public interface NetWorkChangeCallback
    {
        void netWorkChange(CxNetworkStatus status);
    }
}
