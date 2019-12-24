package com.view_erp;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.injoy.idg.R;


/**
 * Created by Admin on 2016/4/14.
 */
public class ErpSecondLinnerLayout extends LinearLayout {

    private TextView leftView, centerView, unread_msg_number,name_second_left_setting;
    private LinearLayout rightLl,menu_left_ll,second_main_ll_dialog;
    private View centerAndRightView;
    private int mScreenWidth;
    private ImageView img_right_set;

    public ErpSecondLinnerLayout(Context context) {
        super(context);
    }

    public ErpSecondLinnerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        View view = View.inflate(context, R.layout.secod_main, this);
        leftView = (TextView) view.findViewById(R.id.name_second_left);
        name_second_left_setting = (TextView) view.findViewById(R.id.name_second_left_setting);
        centerView = (TextView) view.findViewById(R.id.name_second_center);
        rightLl = (LinearLayout) view.findViewById(R.id.name_second_right_ll);
        menu_left_ll= (LinearLayout) view.findViewById(R.id.menu_left_ll);
        second_main_ll_dialog= (LinearLayout) view.findViewById(R.id.second_main_ll_dialog);
        centerAndRightView = view.findViewById(R.id.name_second_center_and_right);
        unread_msg_number = (TextView) view.findViewById(R.id.unread_msg_number);
        img_right_set=(ImageView)view.findViewById(R.id.img_right_set);
    }


    public void setRightImgVisible(){
        img_right_set.setVisibility(View.VISIBLE);
    }

    public  void setAllBackGroundColor(int color){
        second_main_ll_dialog.setBackgroundResource(color);
    }

    public void setLeftLinnearLayoutColor(int color){
        menu_left_ll.setBackgroundResource(color);
    }

    public void setLeftSettingText(String text){
        name_second_left_setting.setVisibility(View.VISIBLE);
        leftView.setVisibility(View.GONE);
        name_second_left_setting.setText(text);
    }

    /**
     * 设置除了左边之外的颜色
     *
     * @param color
     */
    public void setCenterAndRightBackground(int color) {
        if (null != centerAndRightView && color != 0) {
            centerAndRightView.setBackgroundResource(color);
        }
    }

    //設置左邊顏色和背景
    public void setLeftTextAndColor(String text, int color) {
        if (null != leftView) {
            leftView.setText(text);

        }
        if (color != 0) {
            leftView.setBackgroundResource(color);

        }
    }

    //設置左邊顏色和背景
    public void setCenterTextAndColor(String text, int color) {
        if (null != centerView) {
            centerView.setText(text);
        }
        if (color != 0) {
            centerView.setBackgroundResource(color);
        }
    }

    public void addTextViewToRight(String text, float size) {
        TextView Btn = new TextView(getContext());
        Btn.setText(text);
        Btn.setTextColor(Color.WHITE);
//        Btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.abc_text_size_title_material_toolbar));
        Btn.setTextSize(size);
        Btn.setTextColor(Color.BLACK);
        Btn.setPadding(5, 5, 0, 0);
        Btn.setGravity(Gravity.CENTER_VERTICAL);
        //Btn.setBackgroundResource(R.drawable.tab_right_bg);
        Btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
        rightLl.removeAllViews();
        rightLl.addView(Btn, 0);
    }

    //显示
    public void setUnreadVisible() {
        unread_msg_number.setVisibility(View.VISIBLE);
    }

    //隐藏
    public void setUnreadInVisible() {
        unread_msg_number.setVisibility(View.INVISIBLE);
    }
}
