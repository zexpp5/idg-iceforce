package newProject.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * author: Created by yinck on 2018/6/5.
 * email : 823309241@qq.com
 */

public class MyHorizontalScrollView extends HorizontalScrollView {
    private View mView;

    public MyHorizontalScrollView(Context context) {
        super(context);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private boolean mCanScroll = true;
    private int mParentWhidth;
    private float mDownX;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mDownX = ev.getX();
        }

        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            int scrollx = getScrollX();
            if ((scrollx == 0 && mDownX - ev.getX() <= -10)
                    || (getChildAt(0).getMeasuredWidth() <= (scrollx + mParentWhidth) && mDownX
                    - ev.getX() >= 10)) {
                mCanScroll = false;
            }

        }

        if (ev.getAction() == MotionEvent.ACTION_UP
                || ev.getAction() == MotionEvent.ACTION_CANCEL) {
            mCanScroll = true;
        }

        if (this.mCanScroll) {
            getParent().requestDisallowInterceptTouchEvent(true);
            return super.onTouchEvent(ev);
        } else {
            getParent().requestDisallowInterceptTouchEvent(false);
            return false;
        }
    }


    public void setmParentWhidth(int mParentWhidth) {
        this.mParentWhidth = mParentWhidth;
    }


    protected void onScrollChanged(int l, int t, int oldl, int oldt) {

        super.onScrollChanged(l, t, oldl, oldt);
        if (mView != null) {
            mView.scrollTo(l, t);
        }
    }

    public void setScrollView(View view) {
        mView = view;
    }
}