package com.cxgz.activity.cxim.manager;

import com.superdata.im.manager.CxServerManager;

/**
 * @author zjh
 */
public class SocketManager
{
    private static SocketManager socketManager;

    private SocketManager()
    {

    }

    public static SocketManager getInstance()
    {
        if (socketManager == null)
        {
            socketManager = new SocketManager();
        }
        return socketManager;
    }

    /**
     * @param to
     * @param msg
     */
    public void sendPlushMsg(final String to, final String msg)
    {
        if (to == null || "".equals(to))
        {
            return;
        }
        if (CxServerManager.getInstance().pushBinder != null)
        {
            try
            {
                CxServerManager.getInstance().pushBinder.sendPlushMsg(to, msg);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 推送已读未读。
     * @param to
     * @param msg
     */
    public void sendPushReadMsg(final String to, final String msg)
    {
        if (to == null || "".equals(to))
        {
            return;
        }
        if (CxServerManager.getInstance().pushBinder != null)
        {
            try
            {
                CxServerManager.getInstance().pushBinder.sendPushReadMsg(to, msg);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void loginOut()
    {
        if (CxServerManager.getInstance().pushBinder != null)
        {
            CxServerManager.getInstance().pushBinder.loginOut();
        }
    }
}
