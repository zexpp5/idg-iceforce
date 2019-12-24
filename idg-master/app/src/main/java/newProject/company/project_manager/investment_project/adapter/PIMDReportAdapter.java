package newProject.company.project_manager.investment_project.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.injoy.idg.R;

import java.util.List;

import newProject.company.project_manager.investment_project.bean.PIMDReportListBean;

/**
 * Created by zsz on 2019/5/10.
 */

public class PIMDReportAdapter extends BaseQuickAdapter<PIMDReportListBean.DataBeanX.DataBean.ListBean,BaseViewHolder> {
    public PIMDReportAdapter(@Nullable List<PIMDReportListBean.DataBeanX.DataBean.ListBean> data) {
        super(R.layout.adapter_pimd_report, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PIMDReportListBean.DataBeanX.DataBean.ListBean item) {
        helper.setText(R.id.tv_name,item.getIndexName()+":");
        helper.setText(R.id.tv_value,item.getIndexValue());
    }
}
