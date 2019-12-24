package newProject.company.project_manager.investment_project.adapter;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
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

public class ProjectLibraryAdapter extends BaseQuickAdapter<PotentialProjectsPersonalBean.DataBeanX.DataBean,BaseViewHolder> {
    public ProjectLibraryAdapter(@Nullable List<PotentialProjectsPersonalBean.DataBeanX.DataBean> data) {
        super(R.layout.adapter_project_library, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PotentialProjectsPersonalBean.DataBeanX.DataBean item) {
        helper.setText(R.id.tv_name,item.getProjName());
        if (!StringUtils.isEmpty(item.getProjInvestedStatus())){
            helper.getView(R.id.tv_word).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_word,item.getProjInvestedStatus());
            TextView tvWord = helper.getView(R.id.tv_word);
            if ("S".equals(item.getProjInvestedStatus())) {
                tvWord.setBackgroundColor(mContext.getResources().getColor(R.color.blue));
            } else if ("A".equals(item.getProjInvestedStatus())) {
                tvWord.setBackgroundColor(mContext.getResources().getColor(R.color.green));
            } else if ("B".equals(item.getProjInvestedStatus())) {
                tvWord.setBackgroundColor(mContext.getResources().getColor(R.color.yellow));
            } else if ("C".equals(item.getProjInvestedStatus())) {
                tvWord.setBackgroundColor(mContext.getResources().getColor(R.color.red));
            } else if ("X".equals(item.getProjInvestedStatus())) {
                tvWord.setBackgroundColor(mContext.getResources().getColor(R.color.black));
            }else {
                tvWord.setBackground(null);
            }
        }else{
            helper.getView(R.id.tv_word).setVisibility(View.GONE);
        }
        

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

        draw_text.setText(item.getIndusGroupStr());
        helper.addOnClickListener(R.id.ll_item);
        helper.addOnClickListener(R.id.tv_word);
        helper.addOnClickListener(R.id.tv_person_in_charge);
        helper.addOnClickListener(R.id.tv_state);
        helper.addOnClickListener(R.id.tv_state2);
    }
}
