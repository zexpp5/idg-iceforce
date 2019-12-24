package com.cxgz.activity.cxim.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by selson on 2018/1/19.
 */
public class NoScroollView extends ScrollView
{
    public NoScroollView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    protected boolean myValue()
    {
//        return ABCActivity.temp; //记录的临时变量
        return false; //记录的临时变量
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        // TODO Auto-generated method stub
        if (true == myValue())
        {
            return super.onTouchEvent(ev);
        } else
        {
            return false;
        }
    }
}
