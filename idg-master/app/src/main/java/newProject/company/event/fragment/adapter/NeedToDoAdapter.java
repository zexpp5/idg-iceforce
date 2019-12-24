package newProject.company.event.fragment.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.injoy.idg.R;

import java.util.List;

import newProject.company.project_manager.investment_project.bean.ToDoListBean;

/**
 * Created by zsz on 2019/4/11.
 */

public class NeedToDoAdapter extends BaseQuickAdapter<ToDoListBean.DataBeanX.DataBean,BaseViewHolder> {
    public NeedToDoAdapter(@Nullable List<ToDoListBean.DataBeanX.DataBean> data) {
        super(R.layout.adapter_need_to_do, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ToDoListBean.DataBeanX.DataBean item) {
        helper.setText(R.id.tv_busType,item.getBusTypeStr());
        helper.setText(R.id.tv_name,item.getProjName());
        helper.setText(R.id.tv_desc,item.getShowDesc());
        if ("ICE_FOLLOW_ON_SCORE".equals(item.getBusType()) || "ICE_FOLLOW_ON_PROJ".equals(item.getBusType())){
            helper.setBackgroundRes(R.id.iv_img,R.mipmap.icon_need_to_do);
        }else{
            helper.setBackgroundRes(R.id.iv_img,R.mipmap.icon_tips);
        }
        helper.addOnClickListener(R.id.ll_item);
        helper.addOnClickListener(R.id.tv_name);
    }
}
