package tablayout.view.textview;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.injoy.idg.R;

/**
 * 微软雅黑字体
 * Created by cx on 2016/8/10.
 */
public class FontTextView extends android.support.v7.widget.AppCompatTextView
{

    public FontTextView(Context context)
    {
        super(context);
    }

    public FontTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
//        super.setTypeface(BaseApplication.getInstance().createWRYHTypeFace());
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }


}
