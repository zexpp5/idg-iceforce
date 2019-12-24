package newProject.ui.system_msg;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.StringUtils;
import com.injoy.idg.R;

import java.util.List;

/**
 * @author: Created by Freeman on 2018/7/24
 */

public class SystemMsgAdapter extends BaseQuickAdapter<SystemMsgListBean.DataBean, BaseViewHolder>
{

    public SystemMsgAdapter(@Nullable List<SystemMsgListBean.DataBean> data)
    {
        super(R.layout.adapter_system_msg, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SystemMsgListBean.DataBean item)
    {

        helper.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_desc, item.getContent());

        if (StringUtils.notEmpty(item.getCreateTime()))
        {
            helper.getView(R.id.tv_time).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_time, item.getCreateTime());
        } else
        {
            helper.getView(R.id.tv_time).setVisibility(View.GONE);
        }
//        int adapterPosition = helper.getAdapterPosition();
//        if (adapterPosition > 0 && mData.get(adapterPosition - 1).getCreateTime().equals(item.getCreateTime())) {
//            helper.setVisible(R.id.tv_time, false);
//        } else {
//            helper.setVisible(R.id.tv_time, true);
//        }

        if (item.getTitle().contains("完成")) {
        helper.setVisible(R.id.rl_detail, false);
        } else {
            helper.setVisible(R.id.rl_detail, true);

        }
    }
}
