package newProject.company.project_manager.investment_project;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.injoy.idg.R;

/**
 * Created by Administrator on 2018/2/9.
 */

public class LabelItemView extends LinearLayout {
    private LayoutInflater mInflater;
    private View mView;
    private RelativeLayout mOuterLayout;
    private ImageView mLabelImage;
    private TextView mLabelText;
    private boolean mIsSelect = false;
    private int mLabelId;

    public LabelItemView(Context context) {
        super(context);
        initViews(context);
    }


    public LabelItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public LabelItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }


    private void initViews(Context context) {
        mInflater = LayoutInflater.from(context);
        mView = mInflater.inflate(R.layout.search_item_layout, null);
        addView(mView);

        mOuterLayout = (RelativeLayout) mView.findViewById(R.id.search_outer_layout);
        mLabelImage = (ImageView) mView.findViewById(R.id.search_label_image);
        mLabelText = (TextView) mView.findViewById(R.id.search_label_text);

        setSelect(false);
    }

    public void setOuterBg(boolean isSelect){
        if (isSelect){
            mOuterLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.search_radius_red));
        }else{
            mOuterLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.search_radius_gray));
        }
    }


    public void setLabelTextColor(int color){
        mLabelText.setTextColor(color);
    }

    public void setLabelText(String text){
        mLabelText.setText(text);
    }

    public String getLabelText(){
        return mLabelText.getText().toString();
    }

    public void setLabelId(int labelId){
        mLabelId=labelId;
    }

    public int getLabelId(){
        return mLabelId;
    }

    public void setLabelImageVisible(int visibilty){
        mLabelImage.setVisibility(visibilty);
    }

    public void setSelect(boolean isSelect){
        mIsSelect=isSelect;
        setOuterBg(isSelect);
        if (isSelect){
            setLabelImageVisible(VISIBLE);
            setLabelTextColor(0xffec4849);
        }else {
            setLabelImageVisible(GONE);
            setLabelTextColor(0xff333333);
        }
    }

    public boolean getIsSelect(){
        return mIsSelect;
    }

}

