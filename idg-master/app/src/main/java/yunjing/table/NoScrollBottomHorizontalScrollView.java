package yunjing.table;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class NoScrollBottomHorizontalScrollView extends HorizontalScrollView {
    public NoScrollBottomHorizontalScrollView(Context context) {
        super(context);
    }

    public NoScrollBottomHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollBottomHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}