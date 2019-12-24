package com.cxgz.activity.cx.utils.dialog;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.injoy.idg.R;

/**
 * @auth lwj
 * @date 2016-03-04
 * @desc
 */
public class HomeTab extends RelativeLayout
{
    private ImageView imageView;
    private TextView name;
    private TextView unreadcount, unreadcount2;
    private boolean checked;
    private int tv_color;

    public HomeTab(Context context)
    {
        super(context);
    }

    public HomeTab(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initView();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HomeTab);
        String text = typedArray.getString(R.styleable.HomeTab_tabtext);
        checked = typedArray.getBoolean(R.styleable.HomeTab_tabfocusable, false);
        setChecked(checked);
        Drawable src = typedArray.getDrawable(R.styleable.HomeTab_tabsrc);
        imageView.setImageDrawable(src);
        name.setText(text);
        typedArray.recycle();
    }

    public boolean isChecked()
    {
        return checked;
    }

    public void setChecked(boolean checked)
    {
        this.checked = checked;
        imageView.setSelected(checked);
        name.setSelected(checked);
    }

    private void initView()
    {
        View btn = LayoutInflater.from(getContext()).inflate(R.layout.homt_tab, this, false);
        imageView = (ImageView) btn.findViewById(R.id.icon);
        name = (TextView) btn.findViewById(R.id.name);
        unreadcount = (TextView) btn.findViewById(R.id.unreadcount);
        unreadcount2 = (TextView) btn.findViewById(R.id.unreadcount2);

        addView(btn);
    }

    public void setTextStr(String str){
        name.setText(str);
    }

    public void setUnreadCount(int count)
    {
        if (count > 0)
        {
            if (count > 99)
            {
                unreadcount.setText("99+");
            } else
            {
                unreadcount.setText(String.valueOf(count));
            }
            unreadcount.setVisibility(View.VISIBLE);
            unreadcount2.setVisibility(View.INVISIBLE);
        } else
        {
            unreadcount.setVisibility(View.INVISIBLE);
        }
    }

    public void setUnreadCount(boolean count)
    {
        if (count)
        {
            unreadcount2.setVisibility(View.VISIBLE);
            unreadcount.setVisibility(View.INVISIBLE);
        } else
        {
            unreadcount2.setVisibility(View.INVISIBLE);
        }
    }
}
