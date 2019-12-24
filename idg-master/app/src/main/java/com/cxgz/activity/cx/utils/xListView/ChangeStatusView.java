package com.cxgz.activity.cx.utils.xListView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.injoy.idg.R;
import com.superdata.marketing.view.percent.PercentLinearLayout;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by root on 15-11-30.
 */
public class ChangeStatusView extends LinearLayout
{
    private String left_name;
    private String right_name;
    private View view;
    private PercentLinearLayout ly_view;
    private TextView left_tv;
    private TextView right_tv;
    private int status;
    public static final int STATUS_RED = 0;
    public static final int STATUS_BLUE = 1;
    public static final int STATUS_GREEN = 2;
    public static final int STATUS_ORANGE = 3;

    public ChangeStatusView(Context context)
    {
        super(context);
    }

    public void setLeftName(String left_name)
    {
        this.left_name = left_name;
        left_tv.setText(left_name);
    }

    public void setLeftColor(int color)
    {
        left_tv.setTextColor(color);
    }

    public void setRightName(String right_name)
    {
        this.right_name = right_name;
        right_tv.setText(right_name);
    }

    public void setStatus(int status)
    {
        this.status = status;
        changeStatus(status);
    }

    public ChangeStatusView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.statusView);
        left_name = array.getString(R.styleable.statusView_left_statusName);
        right_name = array.getString(R.styleable.statusView_right_statusName);
        status = array.getInt(R.styleable.statusView_statusColor, 0);
        array.recycle();
        view = View.inflate(context, R.layout.status_view, null);
        LayoutParams params = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        addView(view, params);
        ly_view = (PercentLinearLayout) findViewById(R.id.ly_view);
        left_tv = (TextView) findViewById(R.id.left_tv);
        right_tv = (TextView) findViewById(R.id.right_tv);
        left_tv.setText(left_name == null ? "" : left_name);
        right_tv.setText(right_name == null ? "" : right_name);
        changeStatus(status);

    }

    /**
     * @param status
     */
    private void changeStatus(int status)
    {
        switch (status)
        {
            case STATUS_RED:
                left_tv.setBackgroundResource(R.drawable.change_left_red_border);
                right_tv.setBackgroundResource(R.drawable.change_right_red_border);
                break;
            case STATUS_GREEN:
                left_tv.setBackgroundResource(R.drawable.change_left_green_border);
                right_tv.setBackgroundResource(R.drawable.change_right_green_border);
                break;
            case STATUS_BLUE:
                left_tv.setBackgroundResource(R.drawable.change_left_blue_border);
                right_tv.setBackgroundResource(R.drawable.change_right_blue_border);
                break;
            case STATUS_ORANGE:
                left_tv.setBackgroundResource(R.drawable.change_left_orange_border);
                right_tv.setBackgroundResource(R.drawable.change_right_orange_border);
                break;
        }
    }


}
