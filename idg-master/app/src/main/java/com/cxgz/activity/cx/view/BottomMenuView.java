package com.cxgz.activity.cx.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.injoy.idg.R;
import com.superdata.marketing.view.percent.PercentLinearLayout;

import static android.widget.LinearLayout.LayoutParams.MATCH_PARENT;
import static android.widget.LinearLayout.LayoutParams.WRAP_CONTENT;


/**
 *
 */
public class BottomMenuView extends LinearLayout implements View.OnClickListener
{


    private boolean isLeftHide;
    private boolean isRightHide;
    private ImageView leftImg;
    private ImageView rightImg;
    private int rightIcon;
    private int leftIcon;
    private View view;
    private String leftName;
    private String rightName;
    private TextView leftNameTv;
    private TextView rightNameTv;
    private LinearLayout leftLy;
    private LinearLayout rightly;

    public void setListener(BottomMenuListener listener)
    {
        this.listener = listener;
    }

    /**
     * 左边按钮名称
     *
     * @param leftName
     */
    public void setLeftName(String leftName)
    {
        this.leftName = leftName;
        leftNameTv.setText(leftName);
    }

    /**
     * 右边按钮名称
     *
     * @param rightName
     */
    public void setRightName(String rightName)
    {
        this.rightName = rightName;
        rightNameTv.setText(rightName);
    }


    /**
     * 右边按钮图片
     *
     * @param rightIcon
     */
    public void setRightIcon(int rightIcon)
    {
        this.rightIcon = rightIcon;
        rightImg.setBackgroundResource(rightIcon);
    }

    /**
     * 左边按钮图片
     *
     * @param leftIcon
     */
    public void setLeftIcon(int leftIcon)
    {
        this.leftIcon = leftIcon;
        leftImg.setBackgroundResource(leftIcon);
    }

    public void setIsLeftHide(boolean isLeftHide)
    {
        this.isLeftHide = isLeftHide;
        if (isLeftHide)
        {
            leftLy.setVisibility(View.INVISIBLE);
        } else
        {
            leftLy.setVisibility(View.VISIBLE);
        }
    }

    public void setIsRightHide(boolean isRightHide)
    {
        this.isRightHide = isRightHide;
        if (isRightHide)
        {
            rightly.setVisibility(View.INVISIBLE);
        } else
        {
            rightly.setVisibility(View.VISIBLE);
        }
    }

    private BottomMenuListener listener;

    public BottomMenuView(final Context context, AttributeSet attrs)
    {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.BottomMenuView);
        leftIcon = array.getResourceId(R.styleable.BottomMenuView_left_icon, R.mipmap.action_person);
        rightIcon = array.getResourceId(R.styleable.BottomMenuView_right_icon, R.mipmap.send_range);
        leftName = array.getString(R.styleable.BottomMenuView_left_name);
        rightName = array.getString(R.styleable.BottomMenuView_right_name);
        isRightHide = array.getBoolean(R.styleable.BottomMenuView_right_hide, false);
        isLeftHide = array.getBoolean(R.styleable.BottomMenuView_left_hide, false);
        array.recycle();
        view = View.inflate(context, R.layout.bottom_menu, null);
        LayoutParams params = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        addView(view, params);
        leftImg = (ImageView) findViewById(R.id.left_icon);
        rightImg = (ImageView) findViewById(R.id.right_icon);
        leftNameTv = (TextView) view.findViewById(R.id.left_tv);
        rightNameTv = (TextView) view.findViewById(R.id.right_tv);
        leftLy = (PercentLinearLayout) view.findViewById(R.id.left_ly);
        rightly = (PercentLinearLayout) view.findViewById(R.id.right_ly);
        setIsLeftHide(isLeftHide);
        setIsRightHide(isRightHide);
        leftImg.setBackgroundResource(leftIcon);
        rightImg.setBackgroundResource(rightIcon);
        leftImg.setOnClickListener(BottomMenuView.this);
        rightImg.setOnClickListener(BottomMenuView.this);
        leftNameTv.setOnClickListener(BottomMenuView.this);
        rightNameTv.setOnClickListener(BottomMenuView.this);
        leftNameTv.setText(leftName == null ? "审阅人" : leftName);
        rightNameTv.setText(rightName == null ? "抄送" : rightName);

    }

    public void leftLyVisibility(int vsibility)
    {
        leftLy.setVisibility(vsibility);
    }

    public void rightlyVisibility(int vsibility)
    {
        rightly.setVisibility(vsibility);
    }

    @Override
    public void onClick(View view)
    {
        if (listener == null)
        {
            return;
        }
        switch (view.getId())
        {
            case R.id.left_icon:
                listener.leftBtnClick(view);
                break;
            case R.id.right_icon:
                listener.rightBtnClick(view);
                break;
            case R.id.left_tv:
                listener.leftBtnClick(view);
                break;
            case R.id.right_tv:
                listener.rightBtnClick(view);
                break;
        }

    }

    public interface BottomMenuListener
    {
        void leftBtnClick(View view);

        void rightBtnClick(View view);
    }


}
