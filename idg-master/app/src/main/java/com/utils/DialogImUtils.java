package com.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.BaseApplication;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.imsdk.pushuser.IMUserManager;
import com.cxgz.activity.cx.utils.ScreenUtils;
import com.injoy.idg.R;
import com.ui.SDLoginActivity;
import com.ui.activity.guide.GuideActivity;

import newProject.finger.FingerActivity;
import newProject.view.DialogTextFilter;
import yunjing.utils.DisplayUtil;


/**
 * Date: 2017-04-08
 * FIXME  通用的DialogUtils 方法：showCommonDialog（）
 */
public class DialogImUtils
{
    private static class DialogHolder
    {
        private static DialogImUtils instance = new DialogImUtils();
    }

    private DialogImUtils()
    {

    }

    public static DialogImUtils getInstance()
    {
        return DialogHolder.instance;
    }

    public interface OnYesOrNoListener
    {
        void onYes();

        void onNo();
    }

    /**
     * 单个确定按钮的
     */
    public void dialogCommom(final Context context, final String titleString,
                             final String contentString, final String yesString,
                             final String noString, final OnYesOrNoListener onYesOrNoListener)
    {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_function_pay_finish, null);
        dialog.setContentView(contentView);

        dialog.setCancelable(false);
//        dialog.setCanceledOnTouchOutside(false);
        TextView content = (TextView) contentView.findViewById(R.id.texthint);
        content.setText(contentString);
        TextView tv_open = (TextView) contentView.findViewById(R.id.tv_open);//确定
        if (!TextUtils.isEmpty(yesString))
        {
            tv_open.setText(yesString);
            tv_open.setCompoundDrawables(null, null, null, null);
        }
        tv_open.setOnClickListener(new View.OnClickListener()
        {//确定
            @Override
            public void onClick(View v)
            {
                if (onYesOrNoListener != null)
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

    /**
     * 通用的Dialog小框 提示各种信息等 列表dialog用FragmentDiaglog
     * @param context
     */
    public void showCommonDialog(final Context context, DialogTextFilter dialogTextFilter, final OnYesOrNoListener
            onYesOrNoListener)
    {
        final Dialog mDialog = new Dialog(context);
//        if (mDialog.isShowing() && null != mDialog)
//        {
//            mDialog.dismiss();
//        }
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_prompt_common, null);
        mDialog.setContentView(contentView);
        mDialog.setCanceledOnTouchOutside(false);  //为了防止他点别的地方 不进行跳转。
        // 可以禁掉这里。一般都是禁掉的。

        TextView tv_title = (TextView) contentView.findViewById(R.id.tv_title);
        TextView tv_open = (TextView) contentView.findViewById(R.id.tv_open);
        TextView tv_cancel = (TextView) contentView.findViewById(R.id.tv_cancel);
        TextView tv_content = (TextView) contentView.findViewById(R.id.tv_content);
        LinearLayout ll_bottom = (LinearLayout) contentView.findViewById(R.id.ll_bottom);

        View view = (View) contentView.findViewById(R.id.view);

        if (StringUtils.notEmpty(dialogTextFilter.getTitleString()))
        {
            tv_title.setText(dialogTextFilter.getTitleString() + "");
        }
        if (StringUtils.notEmpty(dialogTextFilter.getContentString()))
        {
            tv_content.setText(dialogTextFilter.getContentString() + "");
        }
        if (StringUtils.notEmpty(dialogTextFilter.getYesString()))
        {
            tv_open.setText(dialogTextFilter.getYesString() + "");
        }
        if (StringUtils.notEmpty(dialogTextFilter.getNoString()))
        {
            tv_cancel.setText(dialogTextFilter.getNoString() + "");
        }
        if (StringUtils.notEmpty(dialogTextFilter.getYesColor()))
        {
            tv_open.setTextColor(Color.parseColor(dialogTextFilter.getYesColor() + ""));
        }
        if (StringUtils.notEmpty(dialogTextFilter.getNoColor()))
        {
            tv_cancel.setTextColor(Color.parseColor(dialogTextFilter.getNoColor() + ""));
        }
        if (StringUtils.notEmpty(dialogTextFilter.isNeedBottom()))
        {
            if (dialogTextFilter.isNeedBottom())
            {
                ll_bottom.setVisibility(View.GONE);
            }
        }
        if (StringUtils.notEmpty(dialogTextFilter.getHaveBtn()))
        {
            if (dialogTextFilter.getHaveBtn() == 0)
            {

            } else if (dialogTextFilter.getHaveBtn() == 1) //传1的时候，只有一个确定按钮
            {
                tv_cancel.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
            } else if (dialogTextFilter.getHaveBtn() == 2)
            {

            } else if (dialogTextFilter.getHaveBtn() == 2)
            {

            }

        }

        tv_open.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //完成
                mDialog.dismiss();
                onYesOrNoListener.onYes();
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //清除
                mDialog.dismiss();
                onYesOrNoListener.onNo();
            }
        });
        mDialog.show();

        int screenWidth = ScreenUtils.getScreenWidth(context); //设置宽度
        int screenHeight = ScreenUtils.getScreenHeight(context);
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = (int) (screenWidth * 0.8); // 宽度
        mDialog.getWindow().setGravity(Gravity.CENTER);
        mDialog.getWindow().setAttributes(lp);
    }

    /**
     * 框-文件类型设置-私用
     *
     * @param context
     */
    public void showFileTypeDialog(final Context context, DialogTextFilter dialogTextFilter, final OnYesOrNoListener
            onYesOrNoListener)
    {
        final Dialog mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_prompt_common, null);
        mDialog.setContentView(contentView);
        mDialog.setCanceledOnTouchOutside(true);

        TextView tv_title = (TextView) contentView.findViewById(R.id.tv_title);
        TextView tv_open = (TextView) contentView.findViewById(R.id.tv_open);
        TextView tv_cancel = (TextView) contentView.findViewById(R.id.tv_cancel);
        TextView tv_content = (TextView) contentView.findViewById(R.id.tv_content);
        LinearLayout ll_bottom = (LinearLayout) contentView.findViewById(R.id.ll_bottom);

        View view = (View) contentView.findViewById(R.id.view);

        if (StringUtils.notEmpty(dialogTextFilter.getTitleString()))
        {
            tv_title.setText(dialogTextFilter.getTitleString() + "");
        }
        if (StringUtils.notEmpty(dialogTextFilter.getContentString()))
        {
            tv_content.setText(dialogTextFilter.getContentString() + "");
        }
        if (StringUtils.notEmpty(dialogTextFilter.getProjName()))
        {
            tv_content.setText(Html.fromHtml("将文件关联至" + "<font color='#159FE7'>" + "#" + dialogTextFilter.getProjName() +
                    "#" + "</font>" + "并设置为" + dialogTextFilter.getProjFileType()));
        }

        if (StringUtils.notEmpty(dialogTextFilter.getYesString()))
        {
            tv_open.setText(dialogTextFilter.getYesString() + "");
        }
        if (StringUtils.notEmpty(dialogTextFilter.getNoString()))
        {
            tv_cancel.setText(dialogTextFilter.getNoString() + "");
        }
        if (StringUtils.notEmpty(dialogTextFilter.getYesColor()))
        {
            tv_open.setTextColor(Color.parseColor(dialogTextFilter.getYesColor() + ""));
        }
        if (StringUtils.notEmpty(dialogTextFilter.getNoColor()))
        {
            tv_cancel.setTextColor(Color.parseColor(dialogTextFilter.getNoColor() + ""));
        }
        if (StringUtils.notEmpty(dialogTextFilter.isNeedBottom()))
        {
            if (dialogTextFilter.isNeedBottom())
            {
                ll_bottom.setVisibility(View.GONE);
            }
        }
        if (StringUtils.notEmpty(dialogTextFilter.getHaveBtn()))
        {
            if (dialogTextFilter.getHaveBtn() == 0)
            {

            } else if (dialogTextFilter.getHaveBtn() == 1)
            {
                tv_cancel.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
            } else if (dialogTextFilter.getHaveBtn() == 2)
            {

            } else if (dialogTextFilter.getHaveBtn() == 2)
            {

            }

        }

        tv_open.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //完成
                mDialog.dismiss();
                onYesOrNoListener.onYes();
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //清除
                mDialog.dismiss();
                onYesOrNoListener.onNo();
            }
        });
        mDialog.show();

        int screenWidth = ScreenUtils.getScreenWidth(context); //设置宽度
        int screenHeight = ScreenUtils.getScreenHeight(context);
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = (int) (screenWidth * 0.8); // 宽度
        mDialog.getWindow().setGravity(Gravity.CENTER);
        mDialog.getWindow().setAttributes(lp);
    }
}