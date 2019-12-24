package newProject.company.newsletter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.utils.DateUtils;
import com.injoy.idg.R;

import java.util.List;

import newProject.company.newsletter.bean.NewsLetterListBean;
import newProject.company.project_manager.investment_project.bean.NewsLetterItemListBean;

/**
 * @author: Created by Freeman on 2018/8/10
 */

public class NewsLetterAdapter extends BaseQuickAdapter<NewsLetterItemListBean.DataBeanX.DataBean, BaseViewHolder>
{


    public NewsLetterAdapter(@Nullable List<NewsLetterItemListBean.DataBeanX.DataBean> data)
    {
        super(R.layout.adapter_news_letter, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewsLetterItemListBean.DataBeanX.DataBean item)
    {

        try
        {
            String formatDate = DateUtils.getData(item.getNewsDate());
            helper.setText(R.id.tv_time, formatDate);
        } catch (Exception e)
        {
            helper.setText(R.id.tv_time, " ");
        }

        helper.setText(R.id.tv_title, item.getDocName())
                .setText(R.id.tv_indusgroup_name, item.getIndusGroupName())
                .setText(R.id.tv_desc, item.getSummary())
        ;
    }
}
