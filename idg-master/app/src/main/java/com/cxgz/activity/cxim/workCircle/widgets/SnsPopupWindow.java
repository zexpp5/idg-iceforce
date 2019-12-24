package com.cxgz.activity.cxim.workCircle.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cxgz.activity.cxim.workCircle.bean.ActionItem;
import com.cxgz.activity.cxim.workCircle.utils.DensityUtil;
import com.injoy.idg.R;

import java.util.ArrayList;


/**
 */
public class SnsPopupWindow extends PopupWindow implements OnClickListener
{
    private TextView disagreeBtn;
    private TextView agreeBtn;
    private TextView commentBtn;
    private View view_01, view_02;
    private int mScreenWidth;
    private Context mContext;

    // 实例化一个矩形
    private Rect mRect = new Rect();
    // 坐标的位置（x、y）
    private final int[] mLocation = new int[2];
    // 弹窗子类项选中时的监听
    private OnItemClickListener mItemClickListener;
    // 定义弹窗子类项列表
    private ArrayList<ActionItem> mActionItems = new ArrayList<ActionItem>();

    public void setmItemClickListener(OnItemClickListener mItemClickListener)
    {
        this.mItemClickListener = mItemClickListener;
    }

    public ArrayList<ActionItem> getmActionItems()
    {
        return mActionItems;
    }

    public void setmActionItems(ArrayList<ActionItem> mActionItems)
    {
        this.mActionItems = mActionItems;
    }

    private boolean isShowBtn = false;

    public void setShowBtn(boolean showBtn)
    {
        isShowBtn = showBtn;
    }

    public boolean isShowBtn()
    {
        return isShowBtn;
    }

    public SnsPopupWindow(Context context, int intType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.work_circle_social_sns_popupwindow, null);
        disagreeBtn = (TextView) view.findViewById(R.id.disagreeBtn);
        agreeBtn = (TextView) view.findViewById(R.id.agreeBtn);
        commentBtn = (TextView) view.findViewById(R.id.commentBtn);

        view_01 = (View) view.findViewById(R.id.view_01);
        view_02 = (View) view.findViewById(R.id.view_02);

        disagreeBtn.setOnClickListener(this);
        agreeBtn.setOnClickListener(this);
        commentBtn.setOnClickListener(this);

        DisplayMetrics dm = new DisplayMetrics();// 获取屏幕分辨率宽度
        mScreenWidth = dm.widthPixels;

        this.mContext = context;
//        (float) (mScreenWidth * 0.6))
        this.setContentView(view);
        this.setWidth(DensityUtil.dip2px(context, 250));
        this.setHeight(DensityUtil.dip2px(context, 30));
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        this.setAnimationStyle(R.style.social_pop_anim);

//        if (intType == 0)
//        {
//            initItemData2();
//        } else
//        {
            initItemData();
//        }
    }

    private void initItemData()
    {
        addAction(new ActionItem("同意"));
        addAction(new ActionItem("不同意"));
        addAction(new ActionItem("意见"));
    }

    private void initItemData2()
    {
        addAction(new ActionItem("意见"));
    }

    public void showPopupWindow(View parent)
    {
        //为false 则隐藏
        parent.getLocationOnScreen(mLocation);

        if (isShowBtn)
        {
            disagreeBtn.setVisibility(View.GONE);
            agreeBtn.setVisibility(View.GONE);

            view_01.setVisibility(View.GONE);
            view_02.setVisibility(View.GONE);

            this.setWidth(DensityUtil.dip2px(mContext, 80));
            this.setHeight(DensityUtil.dip2px(mContext, 30));
            this.setFocusable(true);
            this.setOutsideTouchable(true);
            this.update();
        }
        mRect.set(mLocation[0], mLocation[1], mLocation[0] + parent.getWidth(), mLocation[1] + parent.getHeight());
//        }
        // 设置矩形的大小
        agreeBtn.setText(mActionItems.get(0).mTitle);
        if (!this.isShowing())
        {
            showAtLocation(parent, Gravity.NO_GRAVITY, mLocation[0] - this.getWidth()
                    , mLocation[1] - ((this.getHeight() - parent.getHeight()) / 2));
        } else
        {
            dismiss();
        }
    }

    @Override
    public void onClick(View view)
    {
        dismiss();
        switch (view.getId())
        {
            case com.injoy.idg.R.id.agreeBtn:
                mItemClickListener.onItemClick(mActionItems.get(0), 0);
                break;
            case R.id.disagreeBtn:
                mItemClickListener.onItemClick(mActionItems.get(1), 1);
                break;
            case R.id.commentBtn:
                mItemClickListener.onItemClick(mActionItems.get(2), 2);
                break;
            default:
                break;
        }
    }

    /**
     * 添加子类项
     */
    public void addAction(ActionItem action)
    {
        if (action != null)
        {
            mActionItems.add(action);
        }
    }

    /**
     * 功能描述：弹窗子类项按钮监听事件
     */
    public static interface OnItemClickListener
    {
        public void onItemClick(ActionItem item, int position);
    }
}
