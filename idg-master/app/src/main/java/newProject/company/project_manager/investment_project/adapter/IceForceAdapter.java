package newProject.company.project_manager.investment_project.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.injoy.idg.R;

import java.util.List;

import newProject.company.project_manager.investment_project.bean.PotentialProjectsPersonalBean;

/**
 * Created by zsz on 2019/7/1.
 */

public class IceForceAdapter extends BaseQuickAdapter<PotentialProjectsPersonalBean.DataBeanX.DataBean,BaseViewHolder> {
    public IceForceAdapter(@Nullable List<PotentialProjectsPersonalBean.DataBeanX.DataBean> data) {
        super(R.layout.adapter_iceforce, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PotentialProjectsPersonalBean.DataBeanX.DataBean item) {
        helper.setText(R.id.tv_name,item.getProjName()+" ");
        helper.setText(R.id.tv_content,item.getZhDesc());
    }
}
