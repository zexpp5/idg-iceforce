package com.superdata.im.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.chaoxiang.base.utils.FileUtils;
import com.superdata.im.manager.CxReconnManager;
import com.superdata.im.manager.CxServerManager;
import com.superdata.im.manager.CxSocketManager;
import com.superdata.im.server_idg.CxBaseServer;
import com.chaoxiang.base.utils.SDLogUtil;
import com.superdata.im.utils.CxServerUtils;
import com.superdata.im.utils.SPUtilsTool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author zjh
 * @version v1.0.0
 * @date 2015-1-4
 * @desc 检测服务是否被杀死是则重启
 */
public class CxCheckServerReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        String tmpString = "";

        for (Map.Entry<Boolean, Object> entry : CxBaseServer.serverMap.entrySet())
        {
            Class clazz = (Class) entry.getValue();
            if (!CxServerUtils.isServiceRunning(context, clazz.getName()))
            {
                if (entry.getKey())
                {
                    tmpString = "im-检测到服务要重新bind和start！";
                    CxServerUtils.startBindServer(context, CxServerManager.getInstance().plushServerConn, clazz);
                } else
                {
                    tmpString = "im-检测到服务要重新start！";
                    CxServerUtils.startServer(context, (Class) entry.getValue());
                }
            } else
            {
                tmpString = "im-检测到-IDGServer-服务正在运行！";
            }
        }

        FileUtils.getInstance().writeFileToSDCard(tmpString.getBytes(), null, "app_im_time.txt", true, true);

        //这里判断重连
        if (!CxSocketManager.getInstance().isConn())
        {
            String tmpString2 = "im-广播-检测到未连接，需要重新连接";
            FileUtils.getInstance().writeFileToSDCard(tmpString2.getBytes(), null, "app_im_time.txt", true, true);
//            //连接不上服务器时发起重连或者有网未连接上服务器
            if (SPUtilsTool.getIsLoginOut(context))
                CxReconnManager.getInstance(context).triggerReconnEvent();
        } else
        {
            String tmpString2 = "im-广播-检测到连接";
            FileUtils.getInstance().writeFileToSDCard(tmpString2.getBytes(), null, "app_im_time.txt", true, true);
        }
    }
}
