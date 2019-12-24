package newProject.company.project_manager.investment_project.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.utils.StringUtils;
import com.injoy.idg.R;

import java.util.List;

import newProject.company.project_manager.investment_project.bean.PotentialProjectsPersonalBean;
import yunjing.view.DrawText;

/**
 * Created by zsz on 2019/4/25.
 */

public class FollowProjectListAdapter extends BaseQuickAdapter<PotentialProjectsPersonalBean.DataBeanX.DataBean,BaseViewHolder> {
    public FollowProjectListAdapter(@Nullable List<PotentialProjectsPersonalBean.DataBeanX.DataBean> data) {
        super(R.layout.adapter_follow_project_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PotentialProjectsPersonalBean.DataBeanX.DataBean item) {
        helper.setText(R.id.tv_name,item.getProjName());
        helper.setText(R.id.tv_person_in_charge,item.getProjManagerNames());
        helper.setText(R.id.tv_desc,item.getZhDesc());
        TextView textView =  helper.getView(R.id.tv_state);
        DrawText draw_text = helper.getView(R.id.draw_text);
        if ("PASS".equals(item.getStsIdStr ())){
            draw_text.setColor(Color.parseColor("#aaaaaa"));
            textView.setBackgroundResource(R.drawable.tv_bg_gray);
            textView.setTextColor(Color.parseColor("#aaaaaa"));
        }else{
            draw_text.setColor(Color.parseColor("#AE1129"));
            textView.setTextColor(Color.parseColor("#0000FF"));
            textView.setBackgroundResource(R.drawable.tv_bg_p_p);
        }

        if (!StringUtils.isEmpty(item.getStsIdStr())){
            helper.getView(R.id.tv_state).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_state,item.getStsIdStr());
        }else{
            helper.getView(R.id.tv_state).setVisibility(View.GONE);
        }

        if (!StringUtils.isEmpty(item.getComInduStr())){
            helper.getView(R.id.tv_state2).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_state2,item.getComInduStr());
        }else{
            helper.getView(R.id.tv_state2).setVisibility(View.GONE);
        }

        ImageView ivStar = helper.getView(R.id.iv_star);
        ivStar.setVisibility(View.GONE);
        if (StringUtils.notEmpty(item.getFollowUpStatus())) {

            if (item.getFollowUpStatus().equals("2")) {
                ivStar.setImageResource(R.mipmap.icon_star_all);
            } else if (item.getFollowUpStatus().equals("1")) {
                ivStar.setImageResource(R.mipmap.icon_star_half);
            } else {
                ivStar.setImageResource(R.mipmap.icon_star_hollow);
            }
        }else{
            ivStar.setImageResource(R.mipmap.icon_star_hollow);
        }

        draw_text.setText(item.getIndusGroupStr());
        helper.addOnClickListener(R.id.ll_item);
        helper.addOnClickListener(R.id.tv_state);
        helper.addOnClickListener(R.id.tv_state2);
        helper.addOnClickListener(R.id.iv_star);
    }
}
