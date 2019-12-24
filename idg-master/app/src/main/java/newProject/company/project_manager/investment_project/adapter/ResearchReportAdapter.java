package newProject.company.project_manager.investment_project.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.Html;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.utils.StringUtils;
import com.injoy.idg.R;

import java.util.List;

import newProject.company.project_manager.investment_project.bean.ResearchReportItemListBean;
import yunjing.view.DrawText;

/**
 * Created by zsz on 2019/5/7.
 */

public class ResearchReportAdapter extends BaseQuickAdapter<ResearchReportItemListBean.DataBeanX.DataBean,BaseViewHolder>{
    public ResearchReportAdapter(@Nullable List<ResearchReportItemListBean.DataBeanX.DataBean> data) {
        super(R.layout.adapter_research_report_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ResearchReportItemListBean.DataBeanX.DataBean item) {
        helper.setText(R.id.tv_name, Html.fromHtml(item.getDocName()));
        helper.setText(R.id.tv_user,item.getAuthorName());
        helper.setText(R.id.tv_desc,item.getSummary());
        if (StringUtils.notEmpty(item.getDocDate())) {
            helper.setText(R.id.tv_date, item.getDocDate().split(" ")[0]);
        }

        DrawText draw_text = helper.getView(R.id.draw_text);
        draw_text.setColor(Color.parseColor("#AE1129"));
        draw_text.setText(item.getIndusGroup());
    }
}
