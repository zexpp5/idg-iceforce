package tablayout.view.textview;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.injoy.idg.R;

/**
 * Created by cx on 2016/8/15.
 */
public class FontEditext extends android.support.v7.widget.AppCompatEditText
{

    public FontEditext(Context context) {
        super(context);
    }

    public FontEditext(Context context, AttributeSet attrs) {
        super(context, attrs);
//        super.setTypeface(BaseApplication.getInstance().createWRYHTypeFace());
        setHintTextColor(ContextCompat.getColor(getContext() , R.color.hint_text_color));
    }
}
