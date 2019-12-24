package newProject.company.project_manager.investment_project.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.utils.StringUtils;
import com.injoy.idg.R;

import java.util.List;

import newProject.company.project_manager.investment_project.bean.WorkSummaryListBean;

/**
 * Created by zsz on 2019/5/5.
 */

public class WorkSummaryTabAdapter extends BaseQuickAdapter<WorkSummaryListBean.DataBeanX.DataBean,BaseViewHolder> {
    private int flag;
    public WorkSummaryTabAdapter(@Nullable List<WorkSummaryListBean.DataBeanX.DataBean> data,int flag) {
        super(R.layout.adapter_work_summary_tab, data);
        this.flag = flag;
    }

    @Override
    protected void convert(BaseViewHolder helper, WorkSummaryListBean.DataBeanX.DataBean item) {
        if (flag == 1) {
            helper.setText(R.id.tv_busType, "新建项目");
        }else if (flag == 2){
            helper.setText(R.id.tv_busType, "推荐合伙人");
        }else {
            helper.setText(R.id.tv_busType, "行业小组讨论");
        }

        helper.setText(R.id.tv_name,item.getProjName());

        helper.addOnClickListener(R.id.ll_item);
    }
}
