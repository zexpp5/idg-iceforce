package newProject.company.project_manager.investment_project.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.injoy.idg.R;

import java.util.List;

import newProject.company.project_manager.investment_project.bean.PublicUserListBean;

/**
 * Created by zsz on 2019/4/17.
 */

public class PublicUserListAdapter extends BaseQuickAdapter<PublicUserListBean.DataBeanX.DataBean,BaseViewHolder> {

    public PublicUserListAdapter(@Nullable List<PublicUserListBean.DataBeanX.DataBean> data) {
        super(R.layout.adapter_public_user_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PublicUserListBean.DataBeanX.DataBean item) {
        ImageView ivRadio = helper.getView(R.id.iv_radio);
        if (item.isFlag()){
            ivRadio.setBackgroundResource(R.mipmap.check_pressed);
        }else {
            ivRadio.setBackgroundResource(R.mipmap.check_normal);
        }
        Glide.with(mContext)
                .load(item.getCreateByPhoto())
                .error(R.mipmap.temp_user_head)
                .into((ImageView)helper.getView(R.id.iv_head));
        helper.setText(R.id.tv_name,item.getUserName());
        helper.addOnClickListener(R.id.ll_item);
    }


}
