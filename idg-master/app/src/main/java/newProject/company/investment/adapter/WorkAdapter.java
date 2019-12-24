package newProject.company.investment.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.injoy.idg.R;

import java.util.List;

import newProject.company.project_manager.investment_project.bean.NameListsBean;

/**
 * Created by zsz on 2019/9/2.
 */

public class WorkAdapter extends BaseQuickAdapter<NameListsBean.DataBeanX.DataBean,BaseViewHolder> {

    public WorkAdapter(@Nullable List<NameListsBean.DataBeanX.DataBean> data) {
        super(R.layout.adapter_my_work_detail, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NameListsBean.DataBeanX.DataBean item) {
        helper.setText(R.id.tv_name,item.getUserName());
        if (item.isChoose()){
            helper.setBackgroundColor(R.id.tv_name,mContext.getResources().getColor(R.color.bg_gray));
        }else {
            helper.setBackgroundColor(R.id.tv_name,mContext.getResources().getColor(R.color.white));
        }
        helper.addOnClickListener(R.id.tv_name);
    }
}
