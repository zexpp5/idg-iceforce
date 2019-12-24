package newProject.company.project_manager.investment_project.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.injoy.idg.R;

import java.util.List;

import newProject.company.project_manager.investment_project.bean.WorkCircleIndusBean;

/**
 * Created by zsz on 2019/4/24.
 */

public class WorkCircleInduAdapter extends BaseQuickAdapter<WorkCircleIndusBean,BaseViewHolder> {
    public WorkCircleInduAdapter(@Nullable List<WorkCircleIndusBean> data) {
        super(R.layout.adapter_work_circle_indus, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WorkCircleIndusBean item) {

        helper.setText(R.id.tv_str,item.getDesc());
        if (item.isFlag()){
            ((TextView)  helper.getView(R.id.tv_str)).setTextColor(mContext.getResources().getColor(R.color.red));
        }else{
            ((TextView)  helper.getView(R.id.tv_str)).setTextColor(mContext.getResources().getColor(R.color.text_black_l));
        }

    }
}
