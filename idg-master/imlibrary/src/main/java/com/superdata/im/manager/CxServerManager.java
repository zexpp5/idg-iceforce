package com.superdata.im.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.chaoxiang.base.utils.SDLogUtil;
import com.superdata.im.server_idg.CxCoreServer_IDG;
import com.superdata.im.utils.CxServerUtils;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-01-13
 * @desc
 */
public class CxServerManager
{
    public ServiceConnection plushServerConn;
    public CxCoreServer_IDG.PushBinder pushBinder;

    private CxServerManager()
    {
    }

    private static Context context;

    private static CxServerManager manager;

    private onBindServerListener onBindServerListener;

    public static void initContext(Context context)
    {
        CxServerManager.context = context;
    }

    public static CxServerManager getInstance()
    {
        if (manager == null)
        {
            manager = new CxServerManager();
        }
        return manager;
    }

    public interface onBindServerListener
    {
        void onBindSuccess();
    }

    /**
     * 启动服务
     */
    public void startServer()
    {
        SDLogUtil.debug("start server");
        plushServerConn = new ServiceConnection()
        {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service)
            {
                SDLogUtil.debug("binder server success");
                if (service instanceof CxCoreServer_IDG.PushBinder)
                {
                    pushBinder = (CxCoreServer_IDG.PushBinder) service;
                    if (onBindServerListener != null)
                    {
                        onBindServerListener.onBindSuccess();
                    }
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name)
            {
                pushBinder = null;
            }
        };
        CxServerUtils.startBindServer(context, plushServerConn, CxCoreServer_IDG.class);
    }

    public CxServerManager.onBindServerListener getOnBindServerListener()
    {
        return onBindServerListener;
    }

    public void setOnBindServerListener(CxServerManager.onBindServerListener onBindServerListener)
    {
        this.onBindServerListener = onBindServerListener;
    }
}
