package com.superdata.im.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.chaoxiang.base.utils.SDLogUtil;
import com.superdata.im.entity.CxNetworkStatus;
import com.superdata.im.manager.CxNetworkStatusManager;
import com.superdata.im.utils.Observable.CxNetWorkObservable;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-01-16
 * @desc 网络状态监听
 */
public class CxNetWorkChangeReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        NetworkInfo.State wifiState = null;
        NetworkInfo.State mobileState = null;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if (wifiState != null && mobileState != null
                && NetworkInfo.State.CONNECTED != wifiState
                && NetworkInfo.State.CONNECTED == mobileState)
        {
            // 手机网络连接成功
            SDLogUtil.debug("手机网络", "手机网络连接成功");
            if (CxNetworkStatusManager.getInstance().getCurrentStatus() != CxNetworkStatus.CONNECTION)
            {
                CxNetWorkObservable.getInstance().notifyUpdate(CxNetworkStatus.CONNECTION);
            }
        } else if (wifiState != null && mobileState != null
                && NetworkInfo.State.CONNECTED != wifiState
                && NetworkInfo.State.CONNECTED != mobileState)
        {
            // 手机没有任何的网络
            SDLogUtil.debug("手机网络", "手机没有任何的网络");
            CxNetWorkObservable.getInstance().notifyUpdate(CxNetworkStatus.DISCONNECTION);

        } else if (wifiState != null && NetworkInfo.State.CONNECTED == wifiState)
        {
            // 无线网络连接成功
            SDLogUtil.debug("手机网络", "无线网络连接成功");
            if (CxNetworkStatusManager.getInstance().getCurrentStatus() != CxNetworkStatus.CONNECTION)
            {
                CxNetWorkObservable.getInstance().notifyUpdate(CxNetworkStatus.CONNECTION);
            }
        }
    }
}
