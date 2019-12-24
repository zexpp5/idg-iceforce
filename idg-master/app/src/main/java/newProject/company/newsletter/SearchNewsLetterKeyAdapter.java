package newProject.company.newsletter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.injoy.idg.R;

import java.util.List;

import newProject.company.newsletter.bean.NewsLetterKeyListBean;

/**
 * @author: Created by Freeman on 2018/8/10
 */

public class SearchNewsLetterKeyAdapter extends BaseQuickAdapter<NewsLetterKeyListBean.DataBeanX.DataBean, BaseViewHolder>
{
    private Context mContext;

    public SearchNewsLetterKeyAdapter(Context context, @Nullable List<NewsLetterKeyListBean.DataBeanX.DataBean> data)
    {
        super(R.layout.search_item_layout, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, NewsLetterKeyListBean.DataBeanX.DataBean item)
    {
        helper.setText(R.id.search_label_text, item.getDeptName());
        if (item.isChoose())
        {
            setOuterBg(helper, true);
        } else
        {
            setOuterBg(helper, false);
        }
    }

    public void setOuterBg(BaseViewHolder holder, boolean isSelect)
    {
        if (isSelect)
        {
            holder.getView(R.id.search_outer_layout).setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable
                    .search_radius_red));
            holder.getView(R.id.search_label_image).setVisibility(View.VISIBLE);
            ((TextView) holder.getView(R.id.search_label_text)).setTextColor(Color.parseColor("#ec4849"));
        } else
        {
            holder.getView(R.id.search_outer_layout).setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable
                    .search_radius_gray));
            holder.getView(R.id.search_label_image).setVisibility(View.GONE);
            ((TextView) holder.getView(R.id.search_label_text)).setTextColor(Color.parseColor("#333333"));
        }
    }


}
