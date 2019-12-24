package com.superdata.im.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.chaoxiang.base.utils.FileUtils;
import com.superdata.im.manager.CxServerManager;
import com.superdata.im.server_idg.CxBaseServer;
import com.superdata.im.server_idg.CxCoreServer_IDG;
import com.superdata.im.utils.CxServerUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by selson on 2017/12/1.
 */
public class TimeTickReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        String tmpString = "im-广播-每分钟-";
        for (Map.Entry<Boolean, Object> entry : CxBaseServer.serverMap.entrySet())
        {
            Class clazz = (Class) entry.getValue();
            if (!CxServerUtils.isServiceRunning(context, clazz.getName()))
            {
                if (entry.getKey())
                {
                    tmpString += "重新BindServer：" + clazz.getName();
                    CxServerUtils.startBindServer(context, CxServerManager.getInstance().plushServerConn, clazz);
                } else

                {
                    tmpString += "重新startServer：" + clazz.getName();
                    CxServerUtils.startServer(context, (Class) entry.getValue());
                }
            } else
            {
                tmpString += "CxCoreServer_IDG-服务" + " 存在的 ";
                if (!CxServerUtils.isServiceRunning(context, "com.superdata.im.receiver.CxDoImReceiver"))
                {
                    String tmpString2 = "TimeTickReceiver-CxDoImReceiver-服务" + " 存在的 ";
                    FileUtils.getInstance().writeFileToSDCard(tmpString2.getBytes(), null, "app_im_time.txt", true, true);
                }
            }
        }

        FileUtils.getInstance().writeFileToSDCard(tmpString.getBytes(), null, "app_im_time.txt", true, true);

    }
}
