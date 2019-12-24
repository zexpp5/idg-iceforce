package com.chaoxiang.base.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chaoxiang.base.R;

/**
 * User: Selson
 * Date: 2016-07-13
 * FIXME 系统带的AlertDialog
 */
public class DialogUtils
{

    /**
     * 内部类实现单例模式
     * 延迟加载，减少内存开销
     *
     * @author xuzhaohu
     */
    private static class DialogHolder
    {
        private static DialogUtils instance = new DialogUtils();
    }

    /**
     * 私有的构造函数
     */
    private DialogUtils()
    {

    }

    public static DialogUtils getInstance()
    {
        return DialogHolder.instance;
    }

    protected void method()
    {
        System.out.println("DialogUtils");
    }

    public void showDialog(Context context, String promptString, final OnYesOrNoListener onYesOrNoListener)
    {
        AlertDialog.Builder mLogoutTipsDialog = null;
        if (mLogoutTipsDialog == null)
        {
            mLogoutTipsDialog = new AlertDialog.Builder(context);
        }
//        View v = View.inflate(context, R.layout.dialog_test, null);
//        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.liwyDialog);
//        builder.setView(v);
//        AlertDialog dialog = builder.create();
////设置dialog的弹窗类型
//        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        dialog.show();
        mLogoutTipsDialog.setCancelable(false);
        mLogoutTipsDialog.setMessage(promptString);
        final AlertDialog.Builder finalMLogoutTipsDialog = mLogoutTipsDialog;
        mLogoutTipsDialog.setPositiveButton("是", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                finalMLogoutTipsDialog.create().dismiss();
                onYesOrNoListener.onYes();
            }
        });

//            mLogoutTipsDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener()
//            {
//                @Override
//                public void onClick(DialogInterface dialog, int which)
//                {
//
//                }
//            });
        mLogoutTipsDialog.create().show();
    }

    public void showDialog(Context context, String promptString)
    {
        AlertDialog.Builder mLogoutTipsDialog = null;
        if (mLogoutTipsDialog == null)
        {
            mLogoutTipsDialog = new AlertDialog.Builder(context);
        }

        mLogoutTipsDialog.setMessage(promptString);
        final AlertDialog.Builder finalMLogoutTipsDialog = mLogoutTipsDialog;
        mLogoutTipsDialog.setPositiveButton("是", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                finalMLogoutTipsDialog.create().dismiss();
            }
        });
//            mLogoutTipsDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener()
//            {
//                @Override
//                public void onClick(DialogInterface dialog, int which)
//                {
//
//                }
//            });
        mLogoutTipsDialog.create().show();
    }

    public interface OnYesOrNoListener
    {
        void onYes();

        void onNo();
    }

}