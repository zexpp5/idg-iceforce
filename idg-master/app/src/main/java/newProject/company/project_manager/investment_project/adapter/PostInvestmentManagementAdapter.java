package newProject.company.project_manager.investment_project.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.utils.ImageUtils;
import com.chaoxiang.base.utils.StringUtils;
import com.injoy.idg.R;
import com.utils.SPUtils;

import java.util.List;

import newProject.company.project_manager.investment_project.bean.PostInvestmentListBean;
import yunjing.utils.DisplayUtil;

/**
 * Created by zsz on 2019/5/9.
 */

public class PostInvestmentManagementAdapter extends BaseQuickAdapter<PostInvestmentListBean.DataBeanX.DataBean, BaseViewHolder>
{
    Context mContext;

    public PostInvestmentManagementAdapter(Context mContext, @Nullable List<PostInvestmentListBean.DataBeanX.DataBean> data)
    {
        super(R.layout.adapter_post_investment_management, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, PostInvestmentListBean.DataBeanX.DataBean item)
    {
        String myNickName = DisplayUtil.getUserInfo(mContext, 1);
        if (StringUtils.notEmpty(myNickName))
        {
            ImageUtils.waterMarking(mContext, true, false, helper.getView(R.id.ll_water), myNickName);
        }

        helper.setText(R.id.tv_name, item.getProjName());
        helper.setText(R.id.tv_reportFrequency, item.getReportFrequency());
        helper.setText(R.id.tv_actualIncome, item.getActualIncome());
        helper.setText(R.id.tv_actualNetProfit, item.getActualNetProfit());
        helper.setText(R.id.tv_appraisement, item.getValuation());


        TextView tvIncomeRate = helper.getView(R.id.tv_income_rate);
        if (StringUtils.notEmpty(item.getChainGrowthOfActualIncome()) && !item.getChainGrowthOfActualIncome().equals("0"))
        {
            if (item.getChainGrowthOfActualIncome().contains("-"))
            {
                tvIncomeRate.setTextColor(mContext.getResources().getColor(R.color.green));
                helper.setText(R.id.tv_income_rate, item.getChainGrowthOfActualIncome() + "\u2193");
            } else
            {
                tvIncomeRate.setTextColor(mContext.getResources().getColor(R.color.red));
                helper.setText(R.id.tv_income_rate, item.getChainGrowthOfActualIncome() + "\u2191");
            }

        } else
        {
            helper.setText(R.id.tv_income_rate, "-");
        }

        TextView tvProfitRate = helper.getView(R.id.tv_profit_rate);
        if (StringUtils.notEmpty(item.getChainGrowthOfActualNetProfit()) && !item.getChainGrowthOfActualNetProfit().equals("0"))
        {
            if (item.getChainGrowthOfActualNetProfit().contains("-"))
            {
                tvProfitRate.setTextColor(mContext.getResources().getColor(R.color.green));
                helper.setText(R.id.tv_profit_rate, item.getChainGrowthOfActualNetProfit() + "\u2193");
            } else
            {
                tvProfitRate.setTextColor(mContext.getResources().getColor(R.color.red));
                helper.setText(R.id.tv_profit_rate, item.getChainGrowthOfActualNetProfit() + "\u2191");
            }

        } else
        {
            helper.setText(R.id.tv_profit_rate, "-");
        }

        //估值比例
        TextView tvAppraisementRate = helper.getView(R.id.tv_appraisement_rate);
        if (StringUtils.notEmpty(item.getMonthToMonth()) && !item.getMonthToMonth().equals("0"))
        {
            if (item.getMonthToMonthFlag().equals("-1"))
            {
                tvAppraisementRate.setTextColor(mContext.getResources().getColor(R.color.green));
                helper.setText(R.id.tv_appraisement_rate, item.getMonthToMonth() + "\u2193");
            } else
            {
                tvAppraisementRate.setTextColor(mContext.getResources().getColor(R.color.red));
                helper.setText(R.id.tv_appraisement_rate, item.getMonthToMonth() + "\u2191");
            }
        } else
        {
            helper.setText(R.id.tv_appraisement_rate, "-");
        }

        helper.setText(R.id.tv_all_investment, item.getInvTotal());

        if (SPUtils.get(mContext, SPUtils.ROLE_FLAG, "0").equals("207") || SPUtils.get(mContext, SPUtils.ROLE_FLAG, "0").equals
                ("10") || SPUtils.get(mContext, SPUtils.ROLE_FLAG, "0").equals("10"))
        {
            helper.getView(R.id.ll_add).setVisibility(View.GONE);
        } else
        {
            helper.getView(R.id.ll_add).setVisibility(View.VISIBLE);
            helper.addOnClickListener(R.id.ll_add);
        }

        helper.addOnClickListener(R.id.ll_item);

    }
}
