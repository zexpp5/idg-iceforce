package com.cxgz.activity.cx.view.photoview.scrollerproxy;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Found at http://stackoverflow.com/questions/7814017/is-it-possible-to-disable-scrolling-on-a-viewpager.
 * Convenient way to temporarily disable ViewPager navigation while interacting with ImageView.
 * <p/>
 * Julia Zudikova
 */

/**
 * Hacky fix for Issue #4 and
 * http://code.google.com/p/android/issues/detail?id=18990
 * <p/>
 * ScaleGestureDetector seems to mess up the touch events, which means that
 * ViewGroups which make use of onInterceptTouchEvent throw a lot of
 * IllegalArgumentException: pointerIndex out of range.
 * <p/>
 * There's not much I can do in my code for now, but we can mask the result by
 * just catching the problem and ignoring it.
 *
 * @author Chris Banes
 */
public class HackyViewPager extends ViewPager
{

    private boolean isLocked;

    public HackyViewPager(Context context)
    {
        super(context);
        isLocked = false;
    }

    public HackyViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        isLocked = false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        if (!isLocked)
        {
            try
            {
                return super.onInterceptTouchEvent(ev);
            } catch (IllegalArgumentException e)
            {
                e.printStackTrace();
                return false;
            }

//            int action = ev.getAction();
//            switch (action & MotionEvent.ACTION_MASK) {
//                case MotionEvent.ACTION_DOWN: {
//
//                    if (touchMode == TOUCH_MODE_FLING) {
//                        return true;  //fling状态，截获touch，因为在滑动状态，不让子view处理
//                    }
//                    break;
//                }
//
//                case MotionEvent.ACTION_MOVE: {
//                    switch (mTouchMode) {
//                        case TOUCH_MODE_DOWN:
//                            final int pointerIndex = ev.findPointerIndex(mActivePointerId);
//                            final int y = (int) ev.getY(pointerIndex);
//                            if (startScrollIfNeeded(y - mMotionY)) {
//                                return true;//开始滑动状态，截获touch事件，不让子view处理
//                            }
//                            break;
//                    }
//                    break;
//                }
        }
        return false;
    }



    float xUp = 0;
    float xDown = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                xUp = event.getX();
                if (xUp == xDown) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    return true;
                }
                break;
            case MotionEvent.ACTION_DOWN:
                xDown = event.getX();
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return !isLocked && super.onTouchEvent(event);
    }

    public void toggleLock()
    {
        isLocked = !isLocked;
    }

    public void setLocked(boolean isLocked)
    {
        this.isLocked = isLocked;
    }

    public boolean isLocked()
    {
        return isLocked;
    }

}