package newProject.company.project_manager.investment_project.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.injoy.idg.R;
import com.view.SwitchView;

import java.util.List;

import newProject.company.project_manager.investment_project.bean.PublicUserListBean;

/**
 * Created by zsz on 2019/8/1.
 */

public class ItemScoringAdapter extends BaseQuickAdapter<PublicUserListBean.DataBeanX.DataBean,BaseViewHolder> {
    public ItemScoringAdapter(@Nullable List<PublicUserListBean.DataBeanX.DataBean> data) {
        super(R.layout.adapter_item_scoring, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PublicUserListBean.DataBeanX.DataBean item) {

        helper.setText(R.id.tv_name,item.getUserName());

        ImageView ivRadio = helper.getView(R.id.iv_radio);
        if (item.isFlag()){
            ivRadio.setBackgroundResource(R.mipmap.check_pressed);
        }else {
            ivRadio.setBackgroundResource(R.mipmap.check_normal);
        }

        final TextView tvText = helper.getView(R.id.tv_text);
        final SwitchView sv = helper.getView(R.id.sv_toggle);
        sv.setOpened(false);
        sv.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView view) {
                tvText.setText("跟进");
                sv.setOpened(true);
            }

            @Override
            public void toggleToOff(SwitchView view) {
                tvText.setText("不跟进");
                sv.setOpened(false);
            }
        });

        helper.addOnClickListener(R.id.iv_radio);
    }


}
