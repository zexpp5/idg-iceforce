package com.cxgz.activity.cxim.workCircle.spannable;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.base.BaseApplication;
import com.injoy.idg.R;

/**
 * @author yiw
 * @Description:
 * @date 16/1/2 16:32
 */
public abstract class SpannableClickable extends ClickableSpan implements View.OnClickListener
{
    private int DEFAULT_COLOR_ID = R.color.color_content;
    /**
     * text颜色
     */
    private int textColor;

    public SpannableClickable()
    {
        this.textColor = BaseApplication.getContext().getResources().getColor(DEFAULT_COLOR_ID);
    }

    public SpannableClickable(int textColor)
    {
        this.textColor = textColor;
    }

    @Override
    public void updateDrawState(TextPaint ds)
    {
        super.updateDrawState(ds);

        ds.setColor(textColor);
        ds.setUnderlineText(false);
        ds.clearShadowLayer();
    }
}
