package newProject.company.project_manager.investment_project.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.injoy.idg.R;

import java.util.List;

import newProject.company.project_manager.investment_project.bean.IDGTpyeBaseBean;

/**
 * Created by zsz on 2019/5/22.
 */

public class TrackProgressAddAdapter extends BaseQuickAdapter<IDGTpyeBaseBean.DataBeanX.DataBean,BaseViewHolder> {
    public TrackProgressAddAdapter(@Nullable List<IDGTpyeBaseBean.DataBeanX.DataBean> data) {
        super(R.layout.adapter_track_progress_add, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, IDGTpyeBaseBean.DataBeanX.DataBean item) {
        helper.setText(R.id.tv_content,item.getCodeNameZhCn());
        TextView textView = helper.getView(R.id.tv_content);
        if (item.isFlag()){
            textView.setBackground(mContext.getResources().getDrawable(R.drawable.tv_bg_top_bg));
            textView.setTextColor(mContext.getResources().getColor(R.color.white));
        }else {
            textView.setBackground(mContext.getResources().getDrawable(R.drawable.tv_bg_white));
            textView.setTextColor(mContext.getResources().getColor(R.color.text_black_l));
        }
        helper.addOnClickListener(R.id.tv_content);
    }
}
