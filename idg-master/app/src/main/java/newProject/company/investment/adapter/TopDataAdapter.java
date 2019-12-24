package newProject.company.investment.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.injoy.idg.R;

import java.util.List;

import newProject.company.investment.bean.TopDataBean;

/**
 * Created by zsz on 2019/12/4.
 */

public class TopDataAdapter extends BaseQuickAdapter<TopDataBean.DataBeanX.DataBean ,BaseViewHolder> {
    public TopDataAdapter(@Nullable List<TopDataBean.DataBeanX.DataBean> data) {
        super(R.layout.adapter_top_data, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TopDataBean.DataBeanX.DataBean item) {
        if (helper.getLayoutPosition() > 5){
            helper.setBackgroundColor(R.id.ll_item,mContext.getResources().getColor(R.color.top_data_bg));
        }else {
            helper.setBackgroundColor(R.id.ll_item,mContext.getResources().getColor(R.color.white));
        }
        helper.setText(R.id.tv_name,item.getProjName());
        helper.setText(R.id.tv_date,item.getValueDate());
        helper.setText(R.id.tv_gz,item.getValuation());
        helper.setText(R.id.tv_bs,item.getTotalReturnMultiple());
        helper.setText(R.id.tv_ljtz,item.getInvCost());
    }
}
