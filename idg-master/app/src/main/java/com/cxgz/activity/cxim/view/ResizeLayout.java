package com.cxgz.activity.cxim.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

import com.chaoxiang.base.utils.DpOrSp2PxUtil;

/**
 * Created by selson on 2018/1/20.
 */
public class ResizeLayout extends RelativeLayout
{
    private static int count = 0;
    private static int oldHight = 0;
    Context mContext;

    public ResizeLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.mContext = context;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e("Resize-onSizeChanged " + count++, "=>onResize called! w=" + w + ",h=" + h + ",oldw=" + oldw + ",oldh=" + oldh);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        super.onLayout(changed, l, t, r, b);
        Log.e("Resize-onLayout " + count++, "=>OnLayout called! l=" + l + ", t=" + t + ",r=" + r + ",b=" + b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int hight = MeasureSpec.getSize(heightMeasureSpec);
        if (oldHight < hight)
        {
            oldHight = hight - DpOrSp2PxUtil.dp2pxConvertInt(mContext, 50);
            Log.e("Resize-oldHight:", oldHight + "");
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(oldHight - DpOrSp2PxUtil.dp2pxConvertInt(mContext, 50), MeasureSpec
                .EXACTLY);
        Log.e("Resize-hight-before:", hight + "");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e("Resize-hight-after:", hight + "");
//        Log.e("Resize-onMeasure " + count++, "=>onMeasure called! widthMeasureSpec=" + widthMeasureSpec + ",
// heightMeasureSpec=" +
//                heightMeasureSpec);
    }
}
