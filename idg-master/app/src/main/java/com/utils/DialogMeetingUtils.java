package com.utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.StringUtils;
import com.injoy.idg.R;

import tablayout.view.textview.FontEditext;

/**
 */
public class DialogMeetingUtils
{
    private static class DialogUtilsHelper
    {
        private static DialogMeetingUtils dialogUtils = new DialogMeetingUtils();
    }

    public static DialogMeetingUtils getInstance()
    {
        return DialogUtilsHelper.dialogUtils;
    }

    /**
     * 私有的构造函数
     */
    private DialogMeetingUtils()
    {

    }

    public void showEditSomeThingDialog(Activity activity, final onTitleClickListener onTitleClickListener)
    {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View contentView = LayoutInflater.from(activity).inflate(R.layout.prompt_send_add_friend_info, null);
        dialog.setContentView(contentView);
        dialog.setCanceledOnTouchOutside(true);

        final FontEditext et_verify_info = (FontEditext) contentView.findViewById(R.id.et_verify_info);
        et_verify_info.setHint("请输入会议名称");
        TextView title = (TextView) contentView.findViewById(R.id.title);
        title.setText("语音会议名称");

        TextView tv_open = (TextView) contentView.findViewById(R.id.tv_open);
        TextView tv_cancel = (TextView) contentView.findViewById(R.id.tv_cancel);

        tv_open.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //完成
                if (StringUtils.notEmpty(et_verify_info.getText().toString()))
                {
                    onTitleClickListener.setTitle(et_verify_info.getText().toString());
                    dialog.dismiss();
                }
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void showEditSomeThingDialog(final Activity activity, String titleString, String hintString, final onTitleClickListener
            onTitleClickListener)
    {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View contentView = LayoutInflater.from(activity).inflate(R.layout.prompt_send_add_friend_info, null);
        dialog.setContentView(contentView);
        dialog.setCanceledOnTouchOutside(true);

        final FontEditext et_verify_info = (FontEditext) contentView.findViewById(R.id.et_verify_info);
        et_verify_info.setHint(hintString);
        TextView title = (TextView) contentView.findViewById(R.id.title);
        title.setText(titleString);

        TextView tv_open = (TextView) contentView.findViewById(R.id.tv_open);
        TextView tv_cancel = (TextView) contentView.findViewById(R.id.tv_cancel);

        tv_open.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //完成
                if (StringUtils.notEmpty(et_verify_info.getText().toString()))
                {
                    onTitleClickListener.setTitle(et_verify_info.getText().toString());
                    dialog.dismiss();
                } else
                {
                    MyToast.showToast(activity, activity.getResources().getString(R.string.edit_approval_reason));
                }
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * @param activity
     * @param titleString
     * @param hintString
     * @param yesStr
     * @param noStr
     * @param tipString            //吐司 要提示的信息
     * @param onTitleClickListener
     */
    public void showEditSomeThingDialog(final Activity activity, String titleString, String hintString, String yesStr, String
            noStr, final String tipString, final onTitleClickListener
                                                onTitleClickListener)
    {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View contentView = LayoutInflater.from(activity).inflate(R.layout.prompt_send_add_friend_info, null);
        dialog.setContentView(contentView);
        dialog.setCanceledOnTouchOutside(true);

        final FontEditext et_verify_info = (FontEditext) contentView.findViewById(R.id.et_verify_info);

        if (StringUtils.notEmpty(hintString))
            et_verify_info.setHint(hintString);

        TextView title = (TextView) contentView.findViewById(R.id.title);
        if (StringUtils.notEmpty(titleString))
            title.setText(titleString);

        TextView tv_open = (TextView) contentView.findViewById(R.id.tv_open);
        if (StringUtils.notEmpty(yesStr))
            tv_open.setText(yesStr);
        TextView tv_cancel = (TextView) contentView.findViewById(R.id.tv_cancel);
        if (StringUtils.notEmpty(noStr))
            tv_cancel.setText(noStr);

        tv_open.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //完成
                if (StringUtils.notEmpty(et_verify_info.getText().toString()))
                {
                    onTitleClickListener.setTitle(et_verify_info.getText().toString());
                    dialog.dismiss();
                } else
                {
                    MyToast.showToast(activity, tipString);
                }
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public interface onTitleClickListener
    {
        void setTitle(String s);
    }
}
