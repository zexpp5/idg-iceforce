package newProject.company.investment.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.injoy.idg.R;

import java.util.List;

import newProject.company.investment.bean.ApproveListBean;

/**
 * Created by zsz on 2019/8/26.
 */

public class ApproveAdapter extends BaseQuickAdapter<ApproveListBean.DataBeanX.DataBean,BaseViewHolder> {
    public ApproveAdapter(@Nullable List<ApproveListBean.DataBeanX.DataBean> data) {
        super(R.layout.adapter_approve, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ApproveListBean.DataBeanX.DataBean item) {
        helper.setText(R.id.tv_title,item.getType());
        helper.setText(R.id.tv_content,item.getDesc());
    }
}
