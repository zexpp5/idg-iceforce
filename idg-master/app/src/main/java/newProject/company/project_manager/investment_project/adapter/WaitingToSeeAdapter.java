package newProject.company.project_manager.investment_project.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.utils.StringUtils;
import com.injoy.idg.R;
import com.utils.SPUtils;

import java.util.List;

import newProject.company.project_manager.investment_project.bean.PotentialProjectsPersonalBean;
import yunjing.view.DrawText;

/**
 * Created by zsz on 2019/4/25.
 */

public class WaitingToSeeAdapter extends BaseQuickAdapter<PotentialProjectsPersonalBean.DataBeanX.DataBean,BaseViewHolder> {
    public WaitingToSeeAdapter(@Nullable List<PotentialProjectsPersonalBean.DataBeanX.DataBean> data) {
        super(R.layout.adapter_waiting_to_see, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PotentialProjectsPersonalBean.DataBeanX.DataBean item) {
        if(item.getAllocatingButton().equals("0")){
            if (StringUtils.notEmpty(item.getProjManagerNames())) {
                helper.getView(R.id.tv_dist).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_dist, item.getProjManagerNames());
            }else {
                helper.getView(R.id.tv_dist).setVisibility(View.GONE);
            }
        }else {
            helper.getView(R.id.tv_dist).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_dist, "分配待看");
            helper.addOnClickListener(R.id.tv_dist);
        }

        helper.setText(R.id.tv_name,item.getProjName());
        helper.setText(R.id.tv_desc,item.getTag());
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

        helper.getView(R.id.tv_state).setVisibility(View.GONE);
        /*if (!StringUtils.isEmpty(item.getStsIdStr())){
            helper.getView(R.id.tv_state).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_state,item.getStsIdStr());
        }else{
            helper.getView(R.id.tv_state).setVisibility(View.GONE);
        }*/

        if (!StringUtils.isEmpty(item.getComInduStr())){
            helper.getView(R.id.tv_state2).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_state2,item.getComInduStr());
        }else{
            helper.getView(R.id.tv_state2).setVisibility(View.GONE);
        }

        draw_text.setText(item.getIndusGroupStr());
        helper.addOnClickListener(R.id.ll_item);
        //helper.addOnClickListener(R.id.tv_state);
        helper.addOnClickListener(R.id.tv_state2);
    }
}
