package com.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.base.BaseApplication;
import com.chaoxiang.base.utils.SDLogUtil;
import com.ui.SDLoginActivity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @Description: 异常崩溃处理类  当程序发生未捕获异常时，由该类来接管程序并记录发送错误报告。
 */
public class CrashHandler implements UncaughtExceptionHandler
{


    /**
     * 系统默认的UncaughtException处理类
     */
    private UncaughtExceptionHandler mDefaultHandler;

    BaseApplication application;

    private AlertDialog dialog;

    /**
     * @param application 上下文
     * @brief 构造函数
     * @details 获取系统默认的UncaughtException处理器，设置该CrashHandler为程序的默认处理器 。
     */
    public CrashHandler(BaseApplication application)
    {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        this.application = application;
    }

    /**
     * @brief 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex)
    {
        SDLogUtil.debug("uncaughtException");
        if (!handleException(ex) && mDefaultHandler != null)
        {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else
        {
            // Sleep一会后结束程序
            // 来让线程停止一会是为了显示Toast信息给用户，然后Kill程序
            try
            {
                Thread.sleep(3000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            Intent intent = new Intent(application.getApplicationContext(), SDLoginActivity.class);
            PendingIntent restartIntent = PendingIntent.getActivity(
                    application.getApplicationContext(), 0, intent,
                    0);
            AlarmManager mgr = (AlarmManager) application.getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 10,
                    restartIntent);
            application.finishActivity();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
//        new Thread() {
//            public void run() {
//                Looper.prepare();
////                Toast.makeText(application.getApplicationContext(), "好像有个BUG，您别着急，我们马上处理！", Toast.LENGTH_SHORT  ).show();
//                AlertDialog.Builder builder = new AlertDialog.Builder(
//                        application.getTopActivity());
//                builder.setMessage("好像有个BUG，您别着急，我们马上处理！");
//                builder.setCancelable(true);
//                builder.setPositiveButton("是",
//                        new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                                int which) {
//
//                            }
//                        });
//                builder.create().show();
//                Looper.loop();
//            }
//        }.start();

//        }

    }

    /**
     * @param ex 异常
     * @return true：如果处理了该异常信息；否则返回false。
     * @brief 自定义错误处理，收集错误信息
     * @details 发送错误报告等操作均在此完成
     */
    private boolean handleException(final Throwable ex)
    {
        if (ex == null)
        {
            return true;
        }
        try
        {
            //导出异常信息到SD卡中
            dumpExceptionToSDCard(ex);
            //这里可以通过网络上传异常信息到服务器，便于开发人员分析日志从而解决bug
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        ex.printStackTrace();
        // 提示错误消息
        new Thread()
        {
            @Override
            public void run()
            {
                // Toast 显示需要出现在一个线程的消息队列中
                Looper.prepare();
                Toast.makeText(application, "好像有个BUG，您别着急，我们马上处理！", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();
        // 保存错误报告文件
//        saveCrashInfoToFile(ex);
        return true;
    }

    private static String path = FileUtil.getSDFolder() + "/" + CachePath.ROOT_FOLDER + "/log";
    public static String filePath = path + "/" + "log.trace";

    private void dumpExceptionToSDCard(Throwable ex) throws IOException
    {
        //如果SD卡不存在或无法使用，则无法把异常信息写入SD卡
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            SDLogUtil.debug("sdcard unmounted,skip dump exception");
            return;
        }

        File dir = new File(path);
        if (!dir.exists())
        {
            dir.mkdirs();
        }
        long current = System.currentTimeMillis();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(current));
        //以当前时间创建log文件
        File file = new File(filePath);

        try
        {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            //导出发生异常的时间
            pw.println(time);

            //导出手机信息
            dumpPhoneInfo(pw);
            pw.println();
            pw.println("companyId:" + SPUtils.get(BaseApplication.getInstance().getApplicationContext(), SPUtils.COMPANY_ID, ""));
            pw.println("user:" + SPUtils.get(BaseApplication.getInstance().getApplicationContext(), SPUtils.USER_ID, ""));
            //导出异常的调用栈信息
            ex.printStackTrace(pw);
            pw.println("\n");
            pw.println("-----------------------end----------------------------");
            pw.println();
            pw.close();
        } catch (Exception e)
        {
            SDLogUtil.debug("dump crash info failed");
        }
    }

    private void dumpPhoneInfo(PrintWriter pw) throws PackageManager.NameNotFoundException
    {
        //应用的版本名称和版本号
        PackageManager pm = application.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(application.getPackageName(), PackageManager.GET_ACTIVITIES);
        pw.print("App Version name: ");
        pw.print(pi.versionName);
        pw.println("App Version code: ");
        pw.println(pi.versionCode);

        //android版本号
        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);

        //手机制造商
        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);

        //手机型号
        pw.print("Model: ");
        pw.println(Build.MODEL);

        //cpu架构
        pw.print("CPU ABI: ");
        pw.println(Build.CPU_ABI);
    }

}
