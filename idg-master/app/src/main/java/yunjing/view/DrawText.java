package yunjing.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.cxgz.activity.cx.utils.DensityUtil;
import com.injoy.idg.R;
import com.utils.AppUtils;

/**
 * author: Created by aniu on 2018/6/15.
 */

public class DrawText extends View {
    Paint paint;
    int text_color;
    String text;

    public DrawText(Context context) {
        super(context);
        init();
    }

    public DrawText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(50);
        text_color = ContextCompat.getColor(getContext(), R.color.draw_text_blue);
        paint.setColor(text_color);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP)
            setElevation(DensityUtil.dip2px(getContext(), 5));
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setColor(int color) {
        text_color = color;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        int center_x = getWidth() / 2;
        int center_y = getHeight() / 2;

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(center_x, center_y, center_x, paint);

        if (TextUtils.isEmpty(text))
            return;
        paint.setColor(text_color);
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);

        int text_width = rect.width();
        int text_height = rect.height();


        canvas.drawText(text, center_x - (text_width / 2), center_y + (text_height / 2), paint);

    }
}
