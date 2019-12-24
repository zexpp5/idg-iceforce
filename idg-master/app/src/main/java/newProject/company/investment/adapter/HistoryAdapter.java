package newProject.company.investment.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.injoy.idg.R;

import java.util.List;

import newProject.company.investment.bean.HistoryListBean;

/**
 * Created by zsz on 2019/8/27.
 */

public class HistoryAdapter extends BaseQuickAdapter<HistoryListBean.DataBeanX.DataBean,BaseViewHolder> {
    public HistoryAdapter( @Nullable List<HistoryListBean.DataBeanX.DataBean> data) {
        super(R.layout.adapter_history, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HistoryListBean.DataBeanX.DataBean item) {
        helper.setText(R.id.tv_item,item.getItem());
        helper.setText(R.id.tv_date,item.getDate());
        helper.setText(R.id.tv_desc,item.getDesc());
        helper.setText(R.id.tv_result,item.getResult());
    }
}
