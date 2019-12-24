package newProject.company.project_manager.investment_project.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.injoy.idg.R;

import java.util.List;

import newProject.company.project_manager.investment_project.bean.NameListsBean;

/**
 * Created by zsz on 2019/5/24.
 */

public class WorkSummaryDetailHorizonAdapter extends BaseQuickAdapter<NameListsBean.DataBeanX.DataBean,BaseViewHolder> {
    public WorkSummaryDetailHorizonAdapter(@Nullable List<NameListsBean.DataBeanX.DataBean> data) {
        super(R.layout.adapter_work_summary_detail_horizon, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NameListsBean.DataBeanX.DataBean item) {
        helper.setText(R.id.tv_name,item.getUserName());
        TextView textView = helper.getView(R.id.tv_name);
        if (item.isChoose()){
            textView.setTextColor(mContext.getResources().getColor(R.color.blue));
        }else {
            textView.setTextColor(mContext.getResources().getColor(R.color.text_black_s));
        }
        helper.addOnClickListener(R.id.tv_name);
    }
}
