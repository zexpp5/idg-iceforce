package newProject.company.project_manager.investment_project.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.injoy.idg.R;

import java.util.List;

import newProject.company.project_manager.investment_project.bean.PotentialProjectsDetailBean;

/**
 * Created by zsz on 2019/4/28.
 */

public class SegmentAdapter extends BaseQuickAdapter<PotentialProjectsDetailBean.DataBeanX.DataBean.SegmentListBean,BaseViewHolder> {
    public SegmentAdapter(@Nullable List<PotentialProjectsDetailBean.DataBeanX.DataBean.SegmentListBean> data) {
        super(R.layout.adapter_segment, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PotentialProjectsDetailBean.DataBeanX.DataBean.SegmentListBean item) {
        helper.setText(R.id.tv_name,item.getName());
        helper.setText(R.id.tv_desc,item.getDesc());
    }
}
