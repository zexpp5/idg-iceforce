package newProject.company.expense;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chaoxiang.base.utils.NumberUtil;
import com.injoy.idg.R;

import newProject.company.expense.bean.ExpenseDetailBean;
import newProject.company.expense.bean.ExpensePayDetailBean;

/**
 * Created by Administrator on 2018/3/16.
 */

public class ExpenseItemView extends LinearLayout
{
    private LayoutInflater mInflater;
    private View mView;
    private TextView mOne, mTwo, mThree, mFour, mOneA;
    private boolean isShowLine = true;
    private View view01;

    public ExpenseItemView(Context context)
    {
        super(context);
        initView(context);
    }

    public ExpenseItemView(Context context, boolean isShow)
    {
        super(context);
        initView(context);
    }

    public ExpenseItemView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        initView(context);
    }

    public ExpenseItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context)
    {
        mInflater = LayoutInflater.from(context);
        mView = mInflater.inflate(R.layout.expensed_item_layout, null, false);
        addView(mView);

        mOne = (TextView) mView.findViewById(R.id.one_content);
        mTwo = (TextView) mView.findViewById(R.id.two_content);
        mThree = (TextView) mView.findViewById(R.id.three_content);
        mFour = (TextView) mView.findViewById(R.id.four_content);
        mOneA = (TextView) mView.findViewById(R.id.one_content_a);
        view01 = (View) mView.findViewById(R.id.view01);
        if (!isShowLine)
        {
            view01.setVisibility(View.GONE);
        }
    }

    public void setInfo(ExpenseDetailBean.DataBean.FeeListBean bean)
    {
        if (bean == null)
        {
            return;
        }
        if (bean.getBaseType() != null)
        {
            mOne.setText(bean.getBaseType());
        }
        if (bean.getSubType() != null)
        {
            mOneA.setText(bean.getSubType());
        }
        if (bean.getSummary() != null)
        {
            mTwo.setText(bean.getSummary());
        }
        if (bean.getAmt() != null)
        {
            mThree.setText(NumberUtil.reTurnNumber(bean.getAmt()));
        }
        if (bean.getRmbAmt() != null)
        {
            mFour.setText(bean.getRmbAmt());
        }

    }


    public void setInfo(ExpensePayDetailBean.DataBean.FeeListBean bean)
    {
        if (bean == null)
        {
            return;
        }
        if (bean.getBaseType() != null)
        {
            mOne.setText(bean.getBaseType());
        }
        if (bean.getSubType() != null)
        {
            mOneA.setText(bean.getSubType());
        }
        if (bean.getSummary() != null)
        {
            mTwo.setText(bean.getSummary());
        }
        if (bean.getAmt() != null)
        {
            mThree.setText(bean.getAmt());
        }
        if (bean.getRmbAmt() != null)
        {
            mFour.setText(bean.getRmbAmt());
        }
    }
}
