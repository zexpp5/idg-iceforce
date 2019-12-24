package newProject.company.project_manager.investment_project.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.injoy.idg.R;

import java.util.List;

import newProject.company.project_manager.investment_project.bean.MyPartnerGroupBean;

/**
 * Created by zsz on 2019/5/23.
 */

public class MyPartnerGroupAdapter extends BaseQuickAdapter<MyPartnerGroupBean.DataBeanX.DataBean,BaseViewHolder> {
    public MyPartnerGroupAdapter(@Nullable List<MyPartnerGroupBean.DataBeanX.DataBean> data) {
        super(R.layout.adapter_my_partner_group, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyPartnerGroupBean.DataBeanX.DataBean item) {
        helper.setText(R.id.tv_name,item.getUserName());
        helper.setText(R.id.tv_num,item.getNum() + "");
    }
}
