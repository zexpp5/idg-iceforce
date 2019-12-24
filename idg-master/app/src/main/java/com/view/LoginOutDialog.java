package com.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.BaseApplication;
import com.chaoxiang.entity.IMDaoManager;
import com.injoy.idg.R;
import com.superdata.im.constants.CxSPIMKey;
import com.ui.SDLoginActivity;

import newProject.finger.FingerActivity;
import tablayout.view.textview.FontTextView;
import yunjing.utils.DisplayUtil;

/**
 * Created by selson on 2017/8/31.
 */
public class LoginOutDialog
{
    public static void logoutDialog(Context context, final loginOutListener loginOutListener)
    {
        int screenWidth, screenHeight;
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_exit, null);
        dialog.setContentView(contentView);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) ((screenWidth / 2.5) * 2); // 宽度
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);

//        dialog.setCanceledOnTouchOutside(false);
        TextView content = (TextView) contentView.findViewById(R.id.content);
        content.setText("是否确认退出");
        TextView tv_open = (TextView) contentView.findViewById(R.id.tv_open);//确定
        tv_open.setText(context.getResources().getString(R.string.choose_sex_sure));
        TextView tv_cancel = (TextView) contentView.findViewById(R.id.tv_cancel);//取消
        tv_cancel.setText(context.getResources().getString(R.string.choose_sex_cancel));
        TextView et_verify_info = (TextView) contentView.findViewById(R.id.et_verify_info);
        tv_open.setOnClickListener(new View.OnClickListener()
        {
            //确定
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                loginOutListener.setTrue();
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener()
        {//取消
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                loginOutListener.setCancle();
            }
        });
        dialog.show();
    }

    public static void deleteDialog(Context context, final loginOutListener loginOutListener,String title, String contentStr)
    {
        int screenWidth, screenHeight;
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_exit, null);
        dialog.setContentView(contentView);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) ((screenWidth / 2.5) * 2); // 宽度
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);

//        dialog.setCanceledOnTouchOutside(false);
        FontTextView titleStr = (FontTextView) contentView.findViewById(R.id.warehouse_reason);
        titleStr.setText(title);
        FontTextView content = (FontTextView) contentView.findViewById(R.id.content);
        content.setText(contentStr);
        TextView tv_open = (TextView) contentView.findViewById(R.id.tv_open);//确定
        tv_open.setText(context.getResources().getString(R.string.choose_sex_sure));
        TextView tv_cancel = (TextView) contentView.findViewById(R.id.tv_cancel);//取消
        tv_cancel.setText(context.getResources().getString(R.string.choose_sex_cancel));
        TextView et_verify_info = (TextView) contentView.findViewById(R.id.et_verify_info);
        tv_open.setOnClickListener(new View.OnClickListener()
        {
            //确定
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                loginOutListener.setTrue();
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener()
        {//取消
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                loginOutListener.setCancle();
            }
        });
        dialog.show();
    }
    public interface loginOutListener
    {
        void setTrue();

        void setCancle();
    }

    public static void logout(Activity activity)
    {
        com.chaoxiang.base.utils.SPUtils.put(activity, CxSPIMKey.IS_LOGIN, false);
        BaseApplication.getInstance().logout();
        //关闭
        IMDaoManager.getInstance().close();

        if (DisplayUtil.getOpenZW(activity))
        {
            activity.startActivity(new Intent(activity, FingerActivity.class));
            activity.finish();
        } else
        {
            activity.startActivity(new Intent(activity, SDLoginActivity.class));
            activity.finish();
        }

    }

    public static void TispDialog(Context context, final loginOutListener loginOutListener, String title, String contentStr)
    {
        int screenWidth, screenHeight;
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_exit, null);
        dialog.setContentView(contentView);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) ((screenWidth / 2.5) * 2); // 宽度
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);

//        dialog.setCanceledOnTouchOutside(false);
        FontTextView titleStr = (FontTextView) contentView.findViewById(R.id.warehouse_reason);
        titleStr.setText(title);
        FontTextView content = (FontTextView) contentView.findViewById(R.id.content);
        content.setText(contentStr);
        FontTextView tv_open = (FontTextView) contentView.findViewById(R.id.tv_open);//确定
        tv_open.setText(context.getResources().getString(R.string.choose_sex_sure));
        RelativeLayout cancelLayout = (RelativeLayout) contentView.findViewById(R.id.delete_bottom_rl);
        cancelLayout.setVisibility(View.GONE);
        FontTextView tv_cancel = (FontTextView) contentView.findViewById(R.id.tv_cancel);//取消
        tv_cancel.setText(context.getResources().getString(R.string.choose_sex_cancel));
        TextView et_verify_info = (TextView) contentView.findViewById(R.id.et_verify_info);
        tv_open.setOnClickListener(new View.OnClickListener()
        {//确定
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                loginOutListener.setTrue();
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener()
        {//取消
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                loginOutListener.setCancle();
            }
        });
        dialog.show();
    }
}
