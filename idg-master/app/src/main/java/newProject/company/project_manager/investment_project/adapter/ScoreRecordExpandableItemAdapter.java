package newProject.company.project_manager.investment_project.adapter;

import android.view.View;
import android.widget.RatingBar;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chaoxiang.base.utils.StringUtils;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.SDToast;

import java.util.List;

import newProject.api.ListHttpHelper;
import newProject.company.project_manager.investment_project.bean.Level0Item;
import newProject.company.project_manager.investment_project.bean.ScoreItemBaseBean;
import newProject.company.project_manager.investment_project.bean.ScoreRecordItemList;

/**
 * Created by zsz on 2019/4/12.
 */

public class ScoreRecordExpandableItemAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity,BaseViewHolder> {

    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ScoreRecordExpandableItemAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_LEVEL_0, R.layout.adapter_expand_item_0);
        addItemType(TYPE_LEVEL_1, R.layout.adapter_expand_item_1);
    }

    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()){
            case TYPE_LEVEL_0:
                final Level0Item level0Item = (Level0Item) item;
                helper.setText(R.id.tv_date,level0Item.getData().getScoreDate());
                helper.setText(R.id.tv_desc,"已有" + level0Item.getData().getScoreCount() + "人打分");
                helper.setImageResource(R.id.iv_arrow,level0Item.isExpanded() ? R.mipmap.search_down_arrow : R.mipmap.search_up_arrow);
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (level0Item.isExpanded()){
                            collapse(helper.getAdapterPosition());
                        }else{
                            expand(helper.getAdapterPosition());
                        }
                    }
                });
                break;
            case TYPE_LEVEL_1:
                ScoreItemBaseBean bean = (ScoreItemBaseBean) item;
                if (getItem(helper.getAdapterPosition() - 1) instanceof Level0Item){
                    helper.getView(R.id.ll_top).setVisibility(View.VISIBLE);
                }else{
                    helper.getView(R.id.ll_top).setVisibility(View.GONE);
                }

                helper.setText(R.id.tv_name,bean.getScoreName());
                RatingBar rb = helper.getView(R.id.rb_1);
                if (!StringUtils.isEmpty(bean.getTeamScore())) {
                    rb.setRating(Float.parseFloat(bean.getTeamScore()));
                    helper.setText(R.id.tv_score_1,bean.getTeamScore() + "分");
                }else {
                    rb.setRating(0);
                    helper.setText(R.id.tv_score_1,"0分");
                }
                RatingBar rb2 = helper.getView(R.id.rb_2);
                if (!StringUtils.isEmpty(bean.getTeamScore())) {
                    rb2.setRating(Float.parseFloat(bean.getBusinessScore()));
                    helper.setText(R.id.tv_score_2,bean.getBusinessScore() + "分");
                }else {
                    rb2.setRating(0);
                    helper.setText(R.id.tv_score_2,"0分");
                }

                break;
        }
    }
}
