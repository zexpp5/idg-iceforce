package newProject.company.project_manager.investment_project.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.injoy.idg.R;

import java.util.List;

import newProject.company.project_manager.investment_project.bean.ReSearchReportTabsBean;

/**
 * Created by zsz on 2019/5/7.
 */

public class TabsAdapter extends BaseQuickAdapter<ReSearchReportTabsBean.DataBeanX.DataBean,BaseViewHolder>{

    public TabsAdapter(@Nullable List<ReSearchReportTabsBean.DataBeanX.DataBean> data) {
        super(R.layout.adapter_tabs, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReSearchReportTabsBean.DataBeanX.DataBean item) {

        TextView tvName = helper.getView(R.id.tv_name);
        View view = helper.getView(R.id.view_line);
        if (item.isFlag()){
            tvName.setTextColor(mContext.getResources().getColor(R.color.main_red));
            view.setBackgroundColor(mContext.getResources().getColor(R.color.main_red));
        }else{
            tvName.setTextColor(mContext.getResources().getColor(R.color.text_black_s));
            view.setBackgroundColor(mContext.getResources().getColor(R.color.bg_gray));
        }
        helper.setText(R.id.tv_name,item.getDeptName());

    }
}
