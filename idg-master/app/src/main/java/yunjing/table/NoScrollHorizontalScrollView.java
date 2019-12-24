package yunjing.table;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class NoScrollHorizontalScrollView extends HorizontalScrollView {
    private NoScrollHorizontalScrollViewListener listener;
    public boolean isScroll = true;

    public NoScrollHorizontalScrollView(Context context) {
        super(context);
    }
 
    public NoScrollHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    public NoScrollHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
 
    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        return true;
    }
 
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    public void setMyScrollChangeListener(NoScrollHorizontalScrollViewListener listener){
        this.listener = listener;
    }
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(null != listener)
            listener.onscroll(this, l, t, oldl, oldt);
    }


    float x1;
    float y1;
    float x2;
    float y2;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(isScroll) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {//按下
                x1 = event.getX();

                y1 = event.getY();

            }

            if (event.getAction() == MotionEvent.ACTION_UP) {//起来
                x2 = event.getX();

                y2 = event.getY();

                if (y1 - y2 > 50) {

                    Log.e("lzk", "向上滑动啦");
                } else if (y2 - y1 > 50) {
                    Log.e("lzk", "向下滑动啦");

                } else if (x1 - x2 > 50) {

                    Log.e("lzk", "向左滑动啦");

                } else if (x2 - x1 > 50) {
                    Log.e("lzk", "向右滑动啦");
                }

            }

            return super.onTouchEvent(event);

        }else{
            return false;
        }
    }

    /**
     * 控制滑动速度
     */
    @Override
    public void fling(int velocityY) {
        super.fling(velocityY / 2);
    }
    public  interface NoScrollHorizontalScrollViewListener {
        void onscroll(NoScrollHorizontalScrollView view, int l, int t, int oldl, int oldt);
    }

}