package com.cxgz.activity.cx.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.Hashtable;

/**
 */
public class FixGridLayout extends RelativeLayout
{
    int mLeft, mRight, mTop, mBottom, currentBottom;
    Hashtable<View, Position> map = new Hashtable<View, Position>();

    public FixGridLayout(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public FixGridLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public FixGridLayout(Context context)
    {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        // TODO Auto-generated method stub
        int count = getChildCount();
        for (int i = 0; i < count; i++)
        {
            View child = getChildAt(i);
            Position pos = map.get(child);
            if (pos != null)
            {
                child.layout(pos.left, pos.top, pos.right, pos.bottom);
            } else
            {
            }
        }

    }

    public int getPosition(int IndexInRow, int childIndex)
    {
        if (IndexInRow > 0)
        {
            return getPosition(IndexInRow - 1, childIndex - 1) + getChildAt(childIndex - 1).getMeasuredWidth() + 10;
        }
        return 0;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        mLeft = 0;
        mRight = 0;
        mTop = 0;
        mBottom = 0;
        int j = 0;
        int count = getChildCount();
        for (int i = 0; i < count; i++)
        {
            Position position = new Position();
            View view = getChildAt(i);
            mLeft = getPosition(i - j, i);
            mRight = mLeft + view.getMeasuredWidth();
            if (mRight > width)
            {
                j = i;
                mLeft = getPosition(i - j, i);
                mRight = mLeft + view.getMeasuredWidth();
                mTop += getChildAt(i).getMeasuredHeight() + 15;
            }
            mBottom = mTop + view.getMeasuredHeight();
            position.left = mLeft;
            position.top = mTop;
            position.right = mRight;
            position.bottom = mBottom;
            map.put(view, position);
        }
        setMeasuredDimension(width, mBottom);
    }

    private class Position
    {
        int left, top, right, bottom;
    }

    public int getLastWidth()
    {
        int sum = 0;
        if (map.size() == 1)
            return 0;

        int top = map.get(getChildAt(map.size() - 2)).top;
        for (int i = map.size() - 2; i >= 0; i--)
        {
            int cur = map.get(getChildAt(i)).top;
            if (cur == top)
            {
                sum += (map.get(getChildAt(i)).right - map.get(getChildAt(i)).left);
            }
            Position p = map.get(getChildAt(i));
        }
        return sum;
    }
}