package newProject.company.project_manager.investment_project.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.injoy.idg.R;

import java.util.List;

import newProject.company.project_manager.investment_project.bean.WorkSummaryDetailMultiBean;

/**
 * Created by zsz on 2019/5/24.
 */

public class WorkSummaryDetailMultiAdapter extends BaseMultiItemQuickAdapter<WorkSummaryDetailMultiBean,BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public WorkSummaryDetailMultiAdapter(List<WorkSummaryDetailMultiBean> data) {
        super(data);
        addItemType(1, R.layout.adapter_work_summary_detial_type_1);
        addItemType(2, R.layout.adapter_work_summary_detial_type_2);
    }

    @Override
    protected void convert(BaseViewHolder helper, WorkSummaryDetailMultiBean item) {
        switch (helper.getItemViewType()){
            case 1:
                helper.setText(R.id.tv_title,item.getTitle());
                break;
            case 2:
                helper.setText(R.id.tv_busType,item.getTitle());
                helper.setText(R.id.tv_name,"#" + item.getData().getProjName() + "#");
                helper.addOnClickListener(R.id.tv_name);
                helper.addOnClickListener(R.id.ll_item);
                break;
        }
    }
}
