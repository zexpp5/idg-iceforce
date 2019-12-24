package com.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.WindowManager;

import com.lidroid.xutils.util.LogUtils;
import com.utils.SPUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import tablayout.view.dialog.AlarmDialog;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String title = intent.getStringExtra("title");
            String content = intent.getStringExtra("content");
            int id = intent.getIntExtra("id",0);
            String className = intent.getStringExtra("className");
            LogUtils.e(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            if ((Boolean) SPUtils.get(context, SPUtils.LOGIN, false)) {
                AlarmDialog dialog = new AlarmDialog(context,title,content,id,className);
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                dialog.show();
            }
        }
    }
}