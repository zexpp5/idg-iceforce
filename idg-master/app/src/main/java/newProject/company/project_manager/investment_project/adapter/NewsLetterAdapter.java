package newProject.company.project_manager.investment_project.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.utils.StringUtils;
import com.injoy.idg.R;

import java.util.List;

import newProject.company.project_manager.investment_project.bean.NewsLetterItemListBean;
import newProject.company.project_manager.investment_project.bean.ResearchReportItemListBean;
import yunjing.view.DrawText;

/**
 * Created by zsz on 2019/5/7.
 */

public class NewsLetterAdapter extends BaseQuickAdapter<NewsLetterItemListBean.DataBeanX.DataBean,BaseViewHolder>{
    public NewsLetterAdapter(@Nullable List<NewsLetterItemListBean.DataBeanX.DataBean> data) {
        super(R.layout.adapter_news_letter_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewsLetterItemListBean.DataBeanX.DataBean item) {
        helper.setText(R.id.tv_name,item.getDocName());
        if (StringUtils.isEmpty(item.getIndusGroupName())){
            helper.getView(R.id.tv_industry).setVisibility(View.GONE);
        }else {
            helper.getView(R.id.tv_industry).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_industry,item.getIndusGroupName());
        }
        helper.setText(R.id.tv_date,item.getNewsDate().split(" ")[0]);
        helper.setText(R.id.tv_desc,item.getSummary());

    }
}
