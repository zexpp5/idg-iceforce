package newProject.company.project_manager.investment_project.adapter;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.utils.StringUtils;
import com.injoy.idg.R;

import java.util.List;

import newProject.company.project_manager.investment_project.bean.FollowProjectMultiBean;
import yunjing.view.RoundedImageView;

/**
 * Created by zsz on 2019/4/24.
 */

public class WorkCircleAdapter extends BaseMultiItemQuickAdapter<FollowProjectMultiBean, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public WorkCircleAdapter(List<FollowProjectMultiBean> data) {
        super(data);
        addItemType(1, R.layout.adapter_track_progress_iceforce_multi_1_2);
        addItemType(2, R.layout.adapter_track_progress_multi_2);
        addItemType(3, R.layout.adapter_track_progress_iceforce_multi_3);
    }

    @Override
    protected void convert(BaseViewHolder helper, FollowProjectMultiBean item) {
        Glide.with(mContext)
                .load(item.getData().getCreateByPhoto())
                .error(R.mipmap.temp_user_head)
                .into((RoundedImageView)helper.getView(R.id.head_bar_icon));
        SpannableStringBuilder ssbuilder;
        switch (helper.getItemViewType()) {
            case 1:
                helper.setText(R.id.tv_user, item.getData().getFollowName());
                helper.setText(R.id.tv_date, item.getData().getCreateTime().split(" ")[0]);
                String str = item.getData().getProjName()+ " " + item.getData().getShowDesc();
                ssbuilder = new SpannableStringBuilder(str);
                //ForegroundColorSpan--文字前景色，BackgroundColorSpan--文字背景色
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.blue));
                ssbuilder.setSpan(colorSpan, 0, item.getData().getProjName().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                helper.setText(R.id.tv_content, ssbuilder);

                break;
            case 2:
                helper.setText(R.id.tv_user, item.getData().getFollowName());
                helper.setText(R.id.tv_date, item.getData().getCreateTime().split(" ")[0]);
                String str1 = item.getData().getProjName()+" "+ item.getData().getShowDesc() + "#" + item.getData().getProjState() + "#";
                ssbuilder = new SpannableStringBuilder(str1);
                //ForegroundColorSpan--文字前景色，BackgroundColorSpan--文字背景色
                ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(mContext.getResources().getColor(R.color.blue));
                int secondIndex = str1.indexOf("#", str1.indexOf("#") + 1) + 1;
                ssbuilder.setSpan(colorSpan1, str1.indexOf("#"), secondIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(mContext.getResources().getColor(R.color.blue));
                ssbuilder.setSpan(colorSpan2, 0, item.getData().getProjName().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                helper.setText(R.id.tv_content, ssbuilder);
                break;
            case 3:
                helper.setText(R.id.tv_user, item.getData().getFollowName());
                helper.setText(R.id.tv_date, item.getData().getCreateTime().split(" ")[0]);
                helper.setText(R.id.tv_content,item.getData().getProjName());
                if (StringUtils.notEmpty(item.getData().getAudioTime())) {
                    if (!item.getData().getAudioTime().contains(",")) {
                        helper.setText(R.id.tv_second, item.getData().getAudioTime() + "″");
                    } else {
                        helper.setText(R.id.tv_second, "");
                    }
                }else {
                    helper.setText(R.id.tv_second, "");
                }
                helper.addOnClickListener(R.id.ll_voice);
                break;
        }
        if (StringUtils.notEmpty(item.getData().getFollowStatusStr())) {
            helper.getView(R.id.tv_status_str).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_status_str,item.getData().getFollowStatusStr());
        }else {
            helper.getView(R.id.tv_status_str).setVisibility(View.GONE);
        }
        helper.addOnClickListener(R.id.tv_content);
    }
}
