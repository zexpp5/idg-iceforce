package newProject.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.StringUtils;
import com.injoy.idg.R;

import tablayout.view.textview.FontTextView;


/**
 * Created by Administrator on 2017/10/24.
 */

public class NewCommonDialog
{
    private Dialog dialog;
    private Context context;
    private DialogPositiveListener positiveListener;
    private DialogNegativeListener negativeListener;
    private String mComment = "1";

    public NewCommonDialog(Context context)
    {
        this.context = context;
    }

    public void setPositiveListener(DialogPositiveListener positiveListener)
    {
        this.positiveListener = positiveListener;
    }

    public void setNegativeListener(DialogNegativeListener negativeListener)
    {
        this.negativeListener = negativeListener;
    }

    //批审用
    public Dialog initDialog()
    {
        View view = LayoutInflater.from(context).inflate(R.layout.approval_dialog_layout, null);
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        dialog = creatAlertDialog(context, view);
        dialog.setCanceledOnTouchOutside(true);
        final EditText editText = (EditText) dialog.findViewById(R.id.dialog_input_content);
        LinearLayout firstLayout = (LinearLayout) dialog.findViewById(R.id.first_layout);
        LinearLayout secondLayout = (LinearLayout) dialog.findViewById(R.id.second_layout);
        RadioGroup comment = (RadioGroup) dialog.findViewById(R.id.comment_group);
        TextView agreeText = (TextView) dialog.findViewById(R.id.agree);
        TextView noAgreeText = (TextView) dialog.findViewById(R.id.no_agree);
        final RadioButton agree = (RadioButton) dialog.findViewById(R.id.agree_button);
        final RadioButton disagree = (RadioButton) dialog.findViewById(R.id.disagree_button);

        comment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                if (agree.getId() == checkedId)
                {
                    mComment = "1";
                }
                if (disagree.getId() == checkedId)
                {
                    mComment = "2";
                }
            }
        });

        //取消
        secondLayout.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (negativeListener != null)
                {
                    negativeListener.onClick();
                }
                dialog.dismiss();
            }
        });
        //确定
        firstLayout.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (StringUtils.notEmpty(editText.getText().toString()))
                {
                    if (positiveListener != null)
                    {
                        String content = editText.getText().toString();
                        positiveListener.onClick(content, mComment);
                    }
                    dialog.dismiss();
                } else
                {
                    MyToast.showToast(context,"请输入内容");
                }
            }
        });
        return dialog;
    }

    //批审用
    public Dialog initDialog(String yes, String no)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.approval_dialog_layout, null);
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        dialog = creatAlertDialog(context, view);
        dialog.setCanceledOnTouchOutside(true);
        final EditText editText = (EditText) dialog.findViewById(R.id.dialog_input_content);
        LinearLayout firstLayout = (LinearLayout) dialog.findViewById(R.id.first_layout);
        LinearLayout secondLayout = (LinearLayout) dialog.findViewById(R.id.second_layout);
        RadioGroup comment = (RadioGroup) dialog.findViewById(R.id.comment_group);
        TextView agreeText = (TextView) dialog.findViewById(R.id.agree);
        TextView noAgreeText = (TextView) dialog.findViewById(R.id.no_agree);
        final RadioButton agree = (RadioButton) dialog.findViewById(R.id.agree_button);
        final RadioButton disagree = (RadioButton) dialog.findViewById(R.id.disagree_button);
        agreeText.setText(yes);
        noAgreeText.setText(no);

        comment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                if (agree.getId() == checkedId)
                {
                    mComment = "1";
                }
                if (disagree.getId() == checkedId)
                {
                    mComment = "2";
                }
            }
        });

        //取消
        secondLayout.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (negativeListener != null)
                {
                    negativeListener.onClick();
                }
                dialog.dismiss();
            }
        });
        //确定
        firstLayout.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (positiveListener != null)
                {
                    String content = editText.getText().toString();
                    positiveListener.onClick(content, mComment);
                }
                dialog.dismiss();
            }
        });
        return dialog;
    }


    //批审用隐藏输入框
    public Dialog initDialog(boolean show)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.approval_dialog_layout, null);
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        dialog = creatAlertDialog(context, view);
        dialog.setCanceledOnTouchOutside(true);
        final EditText editText = (EditText) dialog.findViewById(R.id.dialog_input_content);
        LinearLayout firstLayout = (LinearLayout) dialog.findViewById(R.id.first_layout);
        LinearLayout secondLayout = (LinearLayout) dialog.findViewById(R.id.second_layout);
        LinearLayout remarkLayout = (LinearLayout) dialog.findViewById(R.id.remark_layout);
        RadioGroup comment = (RadioGroup) dialog.findViewById(R.id.comment_group);
        TextView agreeText = (TextView) dialog.findViewById(R.id.agree);
        TextView noAgreeText = (TextView) dialog.findViewById(R.id.no_agree);
        final RadioButton agree = (RadioButton) dialog.findViewById(R.id.agree_button);
        final RadioButton disagree = (RadioButton) dialog.findViewById(R.id.disagree_button);

        if (show)
        {
            remarkLayout.setVisibility(View.GONE);
        } else
        {
            remarkLayout.setVisibility(View.VISIBLE);
        }

        comment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                if (agree.getId() == checkedId)
                {
                    mComment = "1";
                }
                if (disagree.getId() == checkedId)
                {
                    mComment = "2";
                }
            }
        });

        //取消
        secondLayout.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (negativeListener != null)
                {
                    negativeListener.onClick();
                }
                dialog.dismiss();
            }
        });
        //确定
        firstLayout.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (positiveListener != null)
                {
                    String content = editText.getText().toString();
                    positiveListener.onClick(content, mComment);
                }
                dialog.dismiss();
            }
        });
        return dialog;
    }

    //搜索用
    public Dialog initDialogSearch(Activity activity)
    {

        View view = LayoutInflater.from(context).inflate(R.layout.search_dialog_layout, null);
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        dialog = creatAlertDialog(context, view);
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) ((w_screen / 2.8) * 2); // 宽度

        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);
        dialog.setCanceledOnTouchOutside(true);
        final EditText editText = (EditText) dialog.findViewById(R.id.dialog_input_content);
        RelativeLayout firstLayout = (RelativeLayout) dialog.findViewById(R.id.first_layout);
        RelativeLayout secondLayout = (RelativeLayout) dialog.findViewById(R.id.second_layout);

        //确定
        firstLayout.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (positiveListener != null)
                {
                    String content = editText.getText().toString();
                    if (TextUtils.isEmpty(content))
                    {
                        MyToast.showToast(context, "请输入搜索内容！");
                        return;
                    }
                    positiveListener.onSearchClick(content);
                }
                dialog.dismiss();
            }
        });
        secondLayout.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    //提示用
    public Dialog initDialogTips(Activity activity, String title, String tips)
    {

        View view = LayoutInflater.from(context).inflate(R.layout.tips_dialog_layout, null);
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        dialog = creatAlertDialog(context, view);
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) ((w_screen / 2.8) * 2); // 宽度

        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);
        dialog.setCanceledOnTouchOutside(true);
        final FontTextView titleText = (FontTextView) dialog.findViewById(R.id.warehouse_reason);
        final TextView editText = (TextView) dialog.findViewById(R.id.dialog_input_content);
        RelativeLayout firstLayout = (RelativeLayout) dialog.findViewById(R.id.first_layout);
        RelativeLayout secondLayout = (RelativeLayout) dialog.findViewById(R.id.second_layout);
        editText.setText(tips);
        //确定
        firstLayout.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (positiveListener != null)
                {
                    String content = editText.getText().toString();
                    positiveListener.onSearchClick(content);
                }
                dialog.dismiss();
            }
        });
        secondLayout.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public interface DialogPositiveListener
    {
        void onClick(String inputText, String select);

        void onSearchClick(String content);
    }

    public interface DialogNegativeListener
    {
        void onClick();
    }

    public static Dialog creatAlertDialog(Context context, View view)
    {
        Dialog loading = new Dialog(context, R.style.commonDialog);
        loading.setCanceledOnTouchOutside(true);
        loading.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                (LinearLayout.LayoutParams.MATCH_PARENT)));
        return loading;
    }

}
