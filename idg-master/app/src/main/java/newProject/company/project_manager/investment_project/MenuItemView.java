package newProject.company.project_manager.investment_project;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.injoy.idg.R;

/**
 * Created by Administrator on 2018/3/7.
 */

public class MenuItemView  extends LinearLayout{
    private LayoutInflater mInflater;
    private View mView;
    private RelativeLayout mMenuLayout;
    private ImageView mMenuImage;
    private TextView mMenuText;
    private String mMenuInfo;

    public MenuItemView(Context context) {
        super(context);
        initViews(context);
    }

    public MenuItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MenuItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initViews(Context context) {
        mInflater = LayoutInflater.from(context);
        mView = mInflater.inflate(R.layout.menu_item_layout, null, false);
        addView(mView);

        mMenuLayout = (RelativeLayout) findViewById(R.id.menu_layout);
        mMenuImage = (ImageView) findViewById(R.id.menu_item_image);
        mMenuText = (TextView) findViewById(R.id.menu_item_text);

    }

    public void setMenuImage(int resouce){
        mMenuImage.setImageResource(resouce);
    }

    public void setMenuText(String text){
        mMenuText.setText(text);
    }

    public TextView getmMenuText() {
        return mMenuText;
    }

    public String getMenuInfo() {
        return mMenuInfo;
    }

    public void setMenuInfo(String mMenuInfo) {
        this.mMenuInfo = mMenuInfo;
    }
}
