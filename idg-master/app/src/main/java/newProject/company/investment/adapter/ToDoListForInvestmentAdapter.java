package newProject.company.investment.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.injoy.idg.R;

import java.util.List;

import newProject.company.project_manager.investment_project.bean.ToDoListBean;

/**
 * Created by zsz on 2019/4/9.
 */

public class ToDoListForInvestmentAdapter extends BaseQuickAdapter<ToDoListBean.DataBeanX.DataBean,BaseViewHolder> {
    public ToDoListForInvestmentAdapter(@Nullable List<ToDoListBean.DataBeanX.DataBean> data) {
        super(R.layout.adapter_to_do_list_investment, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final ToDoListBean.DataBeanX.DataBean item) {

        helper.setText(R.id.tv_type,item.getBusTypeStr());
        helper.setText(R.id.tv_name,item.getProjName() + " ");
        helper.setText(R.id.tv_content,item.getShowDesc());
        helper.addOnClickListener(R.id.tv_content);
        helper.addOnClickListener(R.id.tv_name);
    }

}
