package newProject.company.project_manager.investment_project.adapter;

import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chaoxiang.base.utils.StringUtils;
import com.injoy.idg.R;

import java.util.List;

import newProject.company.project_manager.investment_project.bean.IAFIItem0;
import newProject.company.project_manager.investment_project.bean.IAFIItem1;

import static com.injoy.idg.R.string.add;

/**
 * Created by zsz on 2019/5/7.
 */

public class InvestmentAndFinancingInformationAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>
{

    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public InvestmentAndFinancingInformationAdapter(List<MultiItemEntity> data)
    {
        super(data);
        addItemType(TYPE_LEVEL_0, R.layout.adapter_investment_and_financing_information);
        addItemType(TYPE_LEVEL_1, R.layout.adapter_investment_and_financing_information_expand);
    }


    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item)
    {

        switch (helper.getItemViewType())
        {
            case TYPE_LEVEL_0:
                final IAFIItem0 level0Item = (IAFIItem0) item;
                helper.setText(R.id.tv_name, level0Item.getProjName());
                if (StringUtils.notEmpty(level0Item.getRound())){
                    helper.setText(R.id.tv_round, level0Item.getRound());
                    helper.getView(R.id.tv_round).setVisibility(View.VISIBLE);
                }else {
                    helper.getView(R.id.tv_round).setVisibility(View.GONE);
                }
                if (StringUtils.notEmpty(level0Item.getIndustry())){
                    helper.setText(R.id.tv_industry, level0Item.getIndustry());
                    helper.getView(R.id.tv_industry).setVisibility(View.VISIBLE);
                }else {
                    helper.getView(R.id.tv_industry).setVisibility(View.GONE);
                }
                helper.setText(R.id.tv_money, level0Item.getFinancingAmt());
                helper.setText(R.id.tv_desc, level0Item.getDesc());
                helper.setText(R.id.tv_date, level0Item.getFinancingDate());
                final TextView textView = helper.getView(R.id.tv_agency);
                textView.setText(Html.fromHtml(level0Item.getAgencyStr()));
                ImageView imageView = helper.getView(R.id.iv_arrow);
                View viewLine = helper.getView(R.id.view_line);
                if (level0Item.isExpanded())
                {
                    imageView.setVisibility(View.GONE);
                    viewLine.setVisibility(View.GONE);
                } else
                {
                    imageView.setVisibility(View.VISIBLE);
                    viewLine.setVisibility(View.VISIBLE);
                }

                helper.getView(R.id.rl_bottom).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (level0Item.isExpanded())
                        {
                            collapse(helper.getAdapterPosition());
                        } else
                        {
                            expand(helper.getAdapterPosition());
                        }
                    }
                });

                helper.addOnClickListener(R.id.tv_round);
                helper.addOnClickListener(R.id.tv_industry);
                break;
            case TYPE_LEVEL_1:
                final IAFIItem1 level1Item = (IAFIItem1) item;
                TextView tv1 = helper.getView(R.id.tv_1);
                TextView tv2 = helper.getView(R.id.tv_2);
                TextView tv3 = helper.getView(R.id.tv_3);
                if (level1Item.getStatus() == 1)
                {
                    tv1.setBackground(mContext.getResources().getDrawable(R.drawable.tv_bg_expand_blue));
                    tv2.setBackground(mContext.getResources().getDrawable(R.drawable.tv_bg_expand_gray));
                    tv3.setBackground(mContext.getResources().getDrawable(R.drawable.tv_bg_expand_gray));
                } else if (level1Item.getStatus() == 2)
                {
                    tv1.setBackground(mContext.getResources().getDrawable(R.drawable.tv_bg_expand_gray));
                    tv2.setBackground(mContext.getResources().getDrawable(R.drawable.tv_bg_expand_blue));
                    tv3.setBackground(mContext.getResources().getDrawable(R.drawable.tv_bg_expand_gray));
                } else if (level1Item.getStatus() == 3)
                {
                    tv1.setBackground(mContext.getResources().getDrawable(R.drawable.tv_bg_expand_gray));
                    tv2.setBackground(mContext.getResources().getDrawable(R.drawable.tv_bg_expand_gray));
                    tv3.setBackground(mContext.getResources().getDrawable(R.drawable.tv_bg_expand_blue));
                } else
                {
                    tv1.setBackground(mContext.getResources().getDrawable(R.drawable.tv_bg_expand_gray));
                    tv2.setBackground(mContext.getResources().getDrawable(R.drawable.tv_bg_expand_gray));
                    tv3.setBackground(mContext.getResources().getDrawable(R.drawable.tv_bg_expand_gray));
                }
                helper.addOnClickListener(R.id.tv_1);
                helper.addOnClickListener(R.id.tv_2);
                helper.addOnClickListener(R.id.tv_3);
                break;
        }
    }

}
