package com.superdata.im.manager;

import android.content.Context;

import com.superdata.im.entity.CxNetworkStatus;
import com.superdata.im.utils.Observable.CxNetWorkObservable;
import com.superdata.im.utils.Observable.CxNetWorkSubscribe;
import com.superdata.im.utils.SPUtilsTool;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-02-22
 * @desc 网络状态管理
 */
public class CxNetworkStatusManager
{
    /**
     * 当前网络状态
     */
    private CxNetworkStatus currentStatus = CxNetworkStatus.DISCONNECTION;
    private static CxNetworkStatusManager cxNetworkStatusManager;
    private CxNetWorkSubscribe netWorkSubscribe;

    public static CxNetworkStatusManager getInstance()
    {
        if (cxNetworkStatusManager == null)
        {
            cxNetworkStatusManager = new CxNetworkStatusManager();
        }
        return cxNetworkStatusManager;
    }

    public void init(final Context context)
    {
        netWorkSubscribe = new CxNetWorkSubscribe(new CxNetWorkSubscribe.NetWorkChangeCallback()
        {
            @Override
            public void netWorkChange(CxNetworkStatus status)
            {
                currentStatus = status;
                if (currentStatus == CxNetworkStatus.DISCONNECTION_SERVER || currentStatus == CxNetworkStatus.CONNECTION)
                {
                    if (!CxReconnManager.getInstance(context).isReconn())
                    {
                        //连接不上服务器时发起重连或者有网未连接上服务器
                        if (SPUtilsTool.getIsLoginOut(context))
                            CxReconnManager.getInstance(context).triggerReconnEvent();
                    }
                }
            }
        });
        CxNetWorkObservable.getInstance().addObserver(netWorkSubscribe);
    }

    public CxNetworkStatus getCurrentStatus()
    {
        return currentStatus;
    }

}
