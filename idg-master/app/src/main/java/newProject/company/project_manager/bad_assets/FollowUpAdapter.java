package newProject.company.project_manager.bad_assets;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.injoy.idg.R;

import java.util.List;

/**
 * @author: Created by Freeman on 2018/7/23
 */

public class FollowUpAdapter extends BaseQuickAdapter<FollowUpBean.DataBean, BaseViewHolder> {
    public FollowUpAdapter(@Nullable List<FollowUpBean.DataBean> data) {
        super(R.layout.adapter_follow_up, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FollowUpBean.DataBean item) {
        helper.setText(R.id.tv_date, item.getActionDate())
                .setText(R.id.tv_desc, item.getActionContent());
        View line = helper.getView(R.id.line);

        if (helper.getAdapterPosition() == mData.size() - 1) {
            line.setVisibility(View.INVISIBLE);
        } else {
            line.setVisibility(View.VISIBLE);
        }

    }
}
