package newProject.company.project_manager.investment_project.adapter;

import android.support.annotation.Nullable;
import android.widget.RatingBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.camera.javacv.FrameFilter;
import com.injoy.idg.R;

import java.util.List;

import newProject.company.project_manager.investment_project.bean.ScoreItemBaseBean;

/**
 * Created by zsz on 2019/4/23.
 */

public class ScoreRecordAdapter extends BaseQuickAdapter<ScoreItemBaseBean,BaseViewHolder> {
    public ScoreRecordAdapter(@Nullable List<ScoreItemBaseBean> data) {
        super(R.layout.adapter_score_record, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ScoreItemBaseBean item) {
        helper.setText(R.id.tv_name,item.getScoreName());
        RatingBar rb = helper.getView(R.id.rb_1);
        if (!StringUtils.isEmpty(item.getTeamScore())) {
            rb.setRating(Float.parseFloat(item.getTeamScore()));
            helper.setText(R.id.tv_score_1,item.getTeamScore() + "分");
        }else {
            rb.setRating(0);
            helper.setText(R.id.tv_score_1,"0分");
        }
        RatingBar rb2 = helper.getView(R.id.rb_2);
        if (!StringUtils.isEmpty(item.getTeamScore())) {
            rb2.setRating(Float.parseFloat(item.getBusinessScore()));
            helper.setText(R.id.tv_score_2,item.getBusinessScore() + "分");
        }else {
            rb2.setRating(0);
            helper.setText(R.id.tv_score_2,"0分");
        }

    }
}
