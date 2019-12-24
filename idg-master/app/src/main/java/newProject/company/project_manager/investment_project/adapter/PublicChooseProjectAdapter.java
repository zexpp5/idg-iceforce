package newProject.company.project_manager.investment_project.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.injoy.idg.R;

import java.util.List;

import newProject.company.project_manager.investment_project.bean.PotentialProjectsPersonalBean;
import newProject.company.project_manager.investment_project.bean.PublicUserListBean;

/**
 * Created by zsz on 2019/4/17.
 */

public class PublicChooseProjectAdapter extends BaseQuickAdapter<PotentialProjectsPersonalBean.DataBeanX.DataBean,BaseViewHolder> {

    public PublicChooseProjectAdapter(@Nullable List<PotentialProjectsPersonalBean.DataBeanX.DataBean> data) {
        super(R.layout.adapter_public_project_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PotentialProjectsPersonalBean.DataBeanX.DataBean item) {
        ImageView ivRadio = helper.getView(R.id.iv_radio);
        if (item.isFlag()){
            ivRadio.setBackgroundResource(R.mipmap.check_pressed);
        }else {
            ivRadio.setBackgroundResource(R.mipmap.check_normal);
        }
        helper.setText(R.id.tv_name,item.getProjName());
        helper.setText(R.id.tv_str,item.getProjName().substring(0,1));
        helper.addOnClickListener(R.id.ll_item);
    }


}
