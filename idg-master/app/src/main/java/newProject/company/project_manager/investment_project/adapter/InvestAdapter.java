package newProject.company.project_manager.investment_project.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.injoy.idg.R;

import java.math.BigDecimal;
import java.util.List;

import newProject.company.project_manager.investment_project.bean.FundInvestListBean;
import yunjing.view.DrawText;

/**
 * Created by zsz on 2019/4/28.
 */

public class InvestAdapter extends BaseQuickAdapter<FundInvestListBean.DataBeanX.DataBean,BaseViewHolder> {
    public InvestAdapter(@Nullable List<FundInvestListBean.DataBeanX.DataBean> data) {
        super(R.layout.adapter_invest, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FundInvestListBean.DataBeanX.DataBean item) {
        helper.setText(R.id.tv_fundName,item.getFundName());
        String moneyType;
        if ("USD".equals(item.getCurrency())){
            moneyType = "$";
        }else {
            moneyType = "￥";
        }
        BigDecimal total = item.getInvTotal().divide(new BigDecimal(1000000));
        BigDecimal fund = item.getValuationOfFund().divide(new BigDecimal(1000000));
        helper.setText(R.id.tv_invTotal, moneyType + String.valueOf(total.setScale(2,BigDecimal.ROUND_HALF_UP)) + "百万");
        helper.setText(R.id.tv_valuationOfFund, moneyType + String.valueOf(fund.setScale(2,BigDecimal.ROUND_HALF_UP))+ "百万");
        helper.setText(R.id.tv_ownership,item.getOwnership());
        DrawText draw_text = helper.getView(R.id.draw_text);
        draw_text.setText(item.getAbbr());
    }
}
