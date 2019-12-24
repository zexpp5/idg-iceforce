package newProject.company.project_manager.investment_project.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.injoy.idg.R;

import java.util.List;

import newProject.company.project_manager.investment_project.bean.Top3ListBean;

/**
 * Created by zsz on 2019/5/5.
 */

public class MyInvestmentAdapter extends BaseQuickAdapter<Top3ListBean.DataBeanX.DataBean,BaseViewHolder> {
    public MyInvestmentAdapter(@Nullable List<Top3ListBean.DataBeanX.DataBean> data) {
        super(R.layout.adapter_my_investment, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Top3ListBean.DataBeanX.DataBean item) {
        helper.setText(R.id.tv_name,item.getProjName());
        helper.setText(R.id.tv_valuation,item.getValuation());
        helper.setText(R.id.tv_date,item.getValueDate());
        /*if (item.getMonthToMonth() == 0){
            helper.setText(R.id.tv_month_rate,"--");
        }else {*/
        String month = item.getMonthToMonth();
        if (item.getMonthToMonthFlag().equals("1")){
            month += "\u2191";
        }else if (item.getMonthToMonthFlag().equals("-1")){
            month += "\u2193";
        }
        helper.setText(R.id.tv_month_rate,month);
        //}

        /*if (item.getYearToYear() == 0){
            helper.setText(R.id.tv_year_rate,"--");
        }else {*/
        String year = item.getYearToYear();
        if (item.getYearToYearFlag().equals("1")){
            year += "\u2191";
        }else if (item.getYearToYearFlag().equals("-1")){
            year += "\u2193";
        }
        helper.setText(R.id.tv_year_rate,year);
        //}

    }
}
