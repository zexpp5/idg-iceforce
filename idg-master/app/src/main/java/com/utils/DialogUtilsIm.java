package com.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;

import com.base.BaseApplication;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.imsdk.pushuser.IMUserManager;
import com.cxgz.activity.cx.utils.ScreenUtils;
import com.entity.update.UpdateEntity;
import com.injoy.idg.R;
import com.ui.SDLoginActivity;
import com.ui.activity.guide.GuideActivity;

import newProject.finger.FingerActivity;
import yunjing.utils.DisplayUtil;

/**
 * Date: 2017-04-08
 */
public class DialogUtilsIm
{
    public static void isLoginIM(Context context, final OnYesOrNoListener onYesOrNoListener)
    {
        boolean isLoginIm = (boolean) com.utils.SPUtils.get(context, com.utils.SPUtils.IS_LOGIN_IM, false);
        if (isLoginIm)
        {
            logoutDialog(context, new OnYesOrNoListener()
            {
                @Override
                public void onYes()
                {
                    onYesOrNoListener.onYes();
                }

                @Override
                public void onNo()
                {
                    onYesOrNoListener.onNo();
                }
            });
        } else
        {
            onYesOrNoListener.onYes();
        }
    }

    /**
     * 重新登录 提示框
     */
    public static void logoutDialog(final Context context, final OnYesOrNoListener onYesOrNoListener)
    {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_exit, null);
        dialog.setContentView(contentView);

        dialog.setCancelable(false);
//        dialog.setCanceledOnTouchOutside(false);
        TextView warehouse_reason = (TextView) contentView.findViewById(R.id.warehouse_reason);
        warehouse_reason.setText("提    示");
        TextView content = (TextView) contentView.findViewById(R.id.content);
        content.setText("当前账号在别处登录，重新登录方可聊天！");
        TextView tv_open = (TextView) contentView.findViewById(R.id.tv_open);//确定
        tv_open.setText(context.getResources().getString(R.string.choose_sex_sure));
        TextView tv_cancel = (TextView) contentView.findViewById(R.id.tv_cancel);//取消
        tv_cancel.setText(context.getResources().getString(R.string.choose_sex_cancel));
        tv_open.setOnClickListener(new View.OnClickListener()
        {//确定
            @Override
            public void onClick(View v)
            {
                IMUserManager.removeAutoLogin(context);
                BaseApplication.getInstance().removeAllActivity();

                if (DisplayUtil.getOpenZW(context))
                {
                    context.startActivity(new Intent(context, FingerActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else
                {
                    context.startActivity(new Intent(context, SDLoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener()
        {//取消
            @Override
            public void onClick(View v)
            {
                onYesOrNoListener.onNo();
                dialog.dismiss();
            }
        });
        dialog.show();

        int screenWidth = ScreenUtils.getScreenWidth(context); //设置宽度
        int screenHeight = ScreenUtils.getScreenHeight(context);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (screenWidth * 0.8); // 宽度
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setAttributes(lp);
    }

    public interface OnYesOrNoListener2
    {
        void onYes();
    }

    public interface OnYesOrNoListener
    {
        void onYes();

        void onNo();
    }

    public interface OnYesOrNoAndCKListener
    {
        void onYes();

        void onNo();

        void onCheck(boolean isCheck);
    }

    public static void dialogPayFinish(final Context context, final String titleString,
                                       final String contentString, final String yesString,
                                       final String noString, final OnYesOrNoListener2 onYesOrNoListener)
    {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_function_pay_finish, null);
        dialog.setContentView(contentView);

        dialog.setCancelable(false);
        TextView content = (TextView) contentView.findViewById(R.id.texthint);
        content.setText(contentString);
        TextView tv_open = (TextView) contentView.findViewById(R.id.tv_open);//确定
        tv_open.setOnClickListener(new View.OnClickListener()
        {//确定
            @Override
            public void onClick(View v)
            {
                onYesOrNoListener.onYes();
                dialog.dismiss();
            }
        });

        dialog.show();

        int screenWidth = ScreenUtils.getScreenWidth(context); //设置宽度
        int screenHeight = ScreenUtils.getScreenHeight(context);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (screenWidth * 0.8); // 宽度
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setAttributes(lp);
    }

    public static void showUpdataVersion(Context mContext, String description, final OnYesOrNoAndCKListener onYesOrNoListener)
    {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setBackgroundDrawableResource(android.R.color.transparent);

        View contentView = LayoutInflater.from(mContext).inflate(R.layout.update_info_show, null);
        dialog.setContentView(contentView);
        dialog.setCanceledOnTouchOutside(false);

        TextView tv_open = (TextView) contentView.findViewById(R.id.tv_open);
        TextView tv_cancel = (TextView) contentView.findViewById(R.id.tv_cancel);
        TextView et_verify_info = (TextView) contentView.findViewById(R.id.et_verify_info);

        final CheckBox cb_forget = (CheckBox) contentView.findViewById(R.id.cb_forget);

        et_verify_info.setText(description);
        tv_open.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                onYesOrNoListener.onYes();
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                onYesOrNoListener.onNo();
                if (cb_forget.isChecked())
                {
                    onYesOrNoListener.onCheck(true);
                }
            }
        });

        dialog.show();

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        int width = (int) (ScreenUtils.getScreenWidth(mContext)); //设置宽度
        lp.width = (int) (width * 0.8);
        dialog.getWindow().setAttributes(lp);
    }
}