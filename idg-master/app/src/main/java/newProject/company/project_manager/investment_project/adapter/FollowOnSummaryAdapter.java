package newProject.company.project_manager.investment_project.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.injoy.idg.R;

import java.util.List;

import newProject.company.project_manager.investment_project.bean.FollowOnSummaryListBean;

/**
 * Created by zsz on 2019/5/5.
 */

public class FollowOnSummaryAdapter extends BaseQuickAdapter<FollowOnSummaryListBean.DataBeanX.DataBean,BaseViewHolder> {
    public FollowOnSummaryAdapter(@Nullable List<FollowOnSummaryListBean.DataBeanX.DataBean> data) {
        super(R.layout.adapter_follow_on_summary, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FollowOnSummaryListBean.DataBeanX.DataBean item) {
        helper.setText(R.id.tv_busType,"跟踪进展");
        helper.setText(R.id.tv_name,item.getProjName());
        helper.addOnClickListener(R.id.tv_name);
        helper.addOnClickListener(R.id.ll_item);
    }
}
