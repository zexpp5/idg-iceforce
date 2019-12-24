package com.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.injoy.idg.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by injoy-pc on 2016/5/11.
 */
public class TotalMoneyView extends LinearLayout {

    private static final String TAG = "TotalMoneyView";

    private LayoutInflater mInflater;

    //小数点之前，从左到右排
    private int[] beforePointId = {
            R.id.before_point_1,
            R.id.before_point_2,
            R.id.before_point_3,
            R.id.before_point_4,
            R.id.before_point_5,
            R.id.before_point_6/*,
            R.id.before_point_7,
            R.id.before_point_8,
           R.id.before_point_9*/
    };

    //小数点之后，从左到右排
    private int[] afterPointId = {
            R.id.after_point_1,
            R.id.after_point_2
    };

    //小数点之前，从左到右排
    private List<TextView> beforePointTv = new ArrayList<>(beforePointId.length);
    //小数点之后，从左到右排
    private List<TextView> afterPointTv = new ArrayList<>(afterPointId.length);

    private double totalMoney;

    public TotalMoneyView(Context context) {
        this(context, null);
    }

    public TotalMoneyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TotalMoneyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.mInflater = LayoutInflater.from(context);

        initViews();
    }

    private void initViews() {
        View totalView = mInflater.inflate(R.layout.view_money_table, null);

        beforePointTv.clear();
        for (int bPId : beforePointId) {
            beforePointTv.add((TextView) totalView.findViewById(bPId));
        }

        afterPointTv.clear();
        for (int aPId : afterPointId) {
            afterPointTv.add((TextView) totalView.findViewById(aPId));
        }


        addView(totalView);
    }

    public void bindTotalMoney(double totalMoney) {
        setTotalMoney(totalMoney);
        bindView();
    }

    private void bindView(){
        String totalMoneyStr =  new DecimalFormat("#000000.00").format(this.totalMoney);
        String[] totalMoneyArr = totalMoneyStr.split("\\.");

        //整数部分
        for (int i = 0; i < beforePointTv.size(); i++) {
            TextView bTv = beforePointTv.get(i);
            char c = totalMoneyArr[0].toCharArray()[i];

            bTv.setText(c+"");
        }

        //小数部分
        for (int i = 0; i < afterPointTv.size(); i++) {
            TextView aTv = afterPointTv.get(i);

            aTv.setText(String.valueOf(totalMoneyArr[1].toCharArray()[i]));
        }
    }

    public String getTotalMoney() {
        //高精度类型转换
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        return   nf.format(totalMoney);
    }

    public Double getDoubleTotalMoney() {
        return totalMoney;
    }



    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }
}
