package com.cxgz.activity.cx.utils.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.injoy.idg.R;
import com.cxgz.activity.cx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import tablayout.view.dialog.SDNoTileDialog;

/**
 * style dialog
 *
 * @author zjh
 */
public class SDStyleDialog extends SDNoTileDialog implements OnClickListener
{
    public static final int STYLE_ONE_TYPE = 0;
    public static final int STYLE_TWO_TYPE = 1;
    private Context context;
    protected TextView tvTitle;
    private LinearLayout cancelBtn;
    private OnStyle1DialogListener onStyle1DialogListener;
    private LinearLayout content;
    private String[] strings;
    private int[] unreads;
    private List<TextView> textViews = new ArrayList<>();
    private int dialogType;

    public SDStyleDialog(Context context, String[] strings)
    {
        super(context);
        this.context = context;
        this.strings = strings;
        init();
    }

    public SDStyleDialog(Context context, String[] strings, int dialogType)
    {
        super(context);
        this.context = context;
        this.strings = strings;
        this.dialogType = dialogType;
        init();
    }

    public SDStyleDialog(Context context, String[] strings, OnStyle1DialogListener onStyle1DialogListener)
    {
        super(context);
        this.context = context;
        this.strings = strings;
        this.onStyle1DialogListener = onStyle1DialogListener;
        init();
    }

    public SDStyleDialog(Context context, String[] strings, int dialogType, OnStyle1DialogListener onStyle1DialogListener)
    {
        super(context);
        this.context = context;
        this.strings = strings;
        this.dialogType = dialogType;
        this.onStyle1DialogListener = onStyle1DialogListener;
        init();
    }

    public SDStyleDialog(Context context, String[] strings, int[] unreads, int dialogType, OnStyle1DialogListener onStyle1DialogListener)
    {
        super(context);
        this.context = context;
        this.strings = strings;
        this.unreads = unreads;
        this.dialogType = dialogType;
        this.onStyle1DialogListener = onStyle1DialogListener;
        init();
    }

    private void init()
    {
        View contentView = null;
        if (dialogType == STYLE_ONE_TYPE)
        {
            contentView = LayoutInflater.from(context).inflate(R.layout.sd_style1_dialog, null);
        } else if (dialogType == STYLE_TWO_TYPE)
        {
            contentView = LayoutInflater.from(context).inflate(R.layout.sd_style2_dialog, null);
        } else
        {
            throw new RuntimeException("dialogType not find");
        }

        tvTitle = (TextView) contentView.findViewById(R.id.tvTitle);

        cancelBtn = (LinearLayout) contentView.findViewById(R.id.ll_cancel);
        cancelBtn.setOnClickListener(this);

        content = (LinearLayout) contentView.findViewById(R.id.ll_content);
        if (strings != null)
        {
            for (int i = 0; i < strings.length; i++)
            {
                final int j = i;
                final View contentChild = LayoutInflater.from(context).inflate(R.layout.sd_style1_dialog_item, null);
                TextView tv = (TextView) contentChild.findViewById(R.id.tv);
                TextView tv_red_icon = (TextView) contentChild.findViewById(R.id.tv_red_icon);
                if (unreads != null)
                {
                    if (unreads[i] > 0)
                    {
                        tv_red_icon.setVisibility(View.VISIBLE);
                        tv_red_icon.setText(String.valueOf(unreads[i]));
                    } else
                    {
                        tv_red_icon.setVisibility(View.GONE);
                    }
                }
                textViews.add(tv);
                tv.setText(strings[i]);
                if (dialogType == STYLE_TWO_TYPE)
                {
                    tv.setTextColor(context.getResources().getColor(R.color.black));
                }
                contentChild.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (onStyle1DialogListener != null)
                        {
                            onStyle1DialogListener.itemClickListener(content, contentChild, j, v.getId());
                            dismiss();
                        }
                    }
                });
                content.addView(contentChild);
            }
        }

        setContentView(contentView);

    }

    public TextView getTvTitle()
    {
        return tvTitle;
    }

    public void setTvTitle(TextView tvTitle)
    {
        this.tvTitle = tvTitle;
    }

    public LinearLayout getCancelBtn()
    {
        return cancelBtn;
    }

    public void setCancelBtn(LinearLayout cancelBtn)
    {
        this.cancelBtn = cancelBtn;
    }

    public OnStyle1DialogListener getOnStyle1DialogListener()
    {
        return onStyle1DialogListener;
    }

    public void setOnStyle1DialogListener(
            OnStyle1DialogListener onStyle1DialogListener)
    {
        this.onStyle1DialogListener = onStyle1DialogListener;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ll_cancel:
                if (onStyle1DialogListener != null)
                {
                    onStyle1DialogListener.clickCancelListener(v);
                    dismiss();
                }
                break;
        }
    }

    public void showBottom()
    {
        getWindow().getAttributes().width = (int) (ScreenUtils.getScreenWidth(context) * 0.7);
        getWindow().setGravity(Gravity.BOTTOM);
        this.show();
    }

    public interface OnStyle1DialogListener extends OnSDDialogListener
    {
        void itemClickListener(View parent, View view, int position, long id);
    }

    public LinearLayout getContent()
    {
        return content;
    }

    public void setContent(LinearLayout content)
    {
        this.content = content;
    }

    public List<TextView> getTextViews()
    {
        return textViews;
    }

    public void setTextViews(List<TextView> textViews)
    {
        this.textViews = textViews;
    }
}
