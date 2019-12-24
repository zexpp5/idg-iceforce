package newProject.company.project_manager.investment_project.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.injoy.idg.R;

import java.util.List;

import newProject.company.project_manager.investment_project.bean.FollowonListBean;

/**
 * Created by zsz on 2019/4/28.
 */

public class HorizonAdapter extends BaseQuickAdapter<FollowonListBean.DataBeanX.DataBean,BaseViewHolder> {
    public HorizonAdapter(@Nullable List<FollowonListBean.DataBeanX.DataBean> data) {
        super(R.layout.adapter_horizon, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FollowonListBean.DataBeanX.DataBean item) {
        View viewStart = helper.getView(R.id.view_start);
        View viewEnd = helper.getView(R.id.view_end);
        View viewCircle = helper.getView(R.id.view_circle);
        TextView textView = helper.getView(R.id.tv_state);

        if (helper.getAdapterPosition() == 0){
            viewStart.setVisibility(View.INVISIBLE);
        }else {
            viewStart.setVisibility(View.VISIBLE);
        }

        if (item.getIsShow() == 1){
            viewCircle.setBackground(mContext.getResources().getDrawable(R.drawable.shape_view_bg));
            viewStart.setBackgroundColor(mContext.getResources().getColor(R.color.top_bg));
            textView.setTextColor(mContext.getResources().getColor(R.color.top_bg));
        }else {
            viewCircle.setBackground(mContext.getResources().getDrawable(R.drawable.shape_view_bg_gray));
            viewStart.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
            textView.setTextColor(mContext.getResources().getColor(R.color.gray));
        }

        if (helper.getAdapterPosition() + 1  < getData().size() && getData().get(helper.getAdapterPosition() + 1).getIsShow() == 0){
            viewEnd.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
        }else {
            viewEnd.setBackgroundColor(mContext.getResources().getColor(R.color.top_bg));
        }

        if (getData().size()-1 == helper.getAdapterPosition()){
            viewEnd.setVisibility(View.INVISIBLE);
        }else {
            viewEnd.setVisibility(View.VISIBLE);
        }

        textView.setText(item.getStatusCodeName());
    }
}
