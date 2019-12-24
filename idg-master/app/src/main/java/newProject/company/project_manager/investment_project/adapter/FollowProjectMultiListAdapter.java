package newProject.company.project_manager.investment_project.adapter;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.workCircle.widgets.ExpandTextView;
import com.injoy.idg.R;

import java.util.List;

import newProject.company.project_manager.investment_project.bean.FollowProjectMultiBean;

/**
 * Created by zsz on 2019/4/10.
 */

public class FollowProjectMultiListAdapter extends BaseMultiItemQuickAdapter<FollowProjectMultiBean,BaseViewHolder>{
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public FollowProjectMultiListAdapter(List<FollowProjectMultiBean> data) {
        super(data);

        addItemType(1, R.layout.adapter_follow_project_item_1);
        addItemType(2, R.layout.adapter_follow_project_item_2);
        addItemType(3, R.layout.adapter_follow_project_item_3);
    }

    @Override
    protected void convert(BaseViewHolder helper, FollowProjectMultiBean item) {
        switch (helper.getItemViewType()){
            case 1:
                //helper.setText(R.id.text_name,"#" + item.getData().getProjName() + "#");
                String str = item.getData().getProjName()+" " + item.getData().getShowDesc();
                SpannableStringBuilder ssbuilder = new SpannableStringBuilder(str);
                //ForegroundColorSpan--文字前景色，BackgroundColorSpan--文字背景色
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.blue));
                ssbuilder.setSpan(colorSpan, 0, item.getData().getProjName().length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ExpandTextView expandTextView = helper.getView(R.id.text_content);
                expandTextView.setText(ssbuilder);
                //helper.setText(R.id.text_content,item.getData().getShowDesc());
                break;
            case 2:
                helper.setText(R.id.text_name, item.getData().getProjName() + " ");
                helper.setText(R.id.text_content,item.getData().getShowDesc());
                helper.setText(R.id.text_state,"#" + item.getData().getProjState() + "#");
                break;
            case 3:
                helper.setText(R.id.text_name,item.getData().getProjName());
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
        helper.addOnClickListener(R.id.text_name);
        //helper.addOnLongClickListener(R.id.text_content);
    }
}
