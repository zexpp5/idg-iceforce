package newProject.company.project_manager.investment_project.adapter;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.webkit.WebView;

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

public class FollowOnDetailMultiAdapter extends BaseMultiItemQuickAdapter<FollowProjectMultiBean,BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public FollowOnDetailMultiAdapter(List<FollowProjectMultiBean> data) {
        super(data);
        addItemType(0,R.layout.adapter_follow_on_detail_multi_0);
        addItemType(1,R.layout.adapter_track_progress_multi_1_del);
        addItemType(2,R.layout.adapter_track_progress_multi_2);
        addItemType(3,R.layout.adapter_track_progress_iceforce_multi_3);
    }

    @Override
    protected void convert(BaseViewHolder helper, FollowProjectMultiBean item) {
        SpannableStringBuilder ssbuilder;
        switch (helper.getItemViewType()){
            case 0:
                helper.setText(R.id.tv_title,item.getTitle());
                break;
            case 1:
                helper.getView(R.id.iv_del).setVisibility(View.GONE);
                helper.setText(R.id.tv_user,item.getData().getFollowName());
                helper.setText(R.id.tv_date,item.getData().getCreateTime().split(" ")[0]);
                String str;
                if (StringUtils.notEmpty(item.getData().getFollowStatusStr())) {
                    str = "#" + item.getData().getFollowStatusStr() + "#" + item.getData().getShowDesc();
                    ssbuilder = new SpannableStringBuilder(str);
                    //ForegroundColorSpan--文字前景色，BackgroundColorSpan--文字背景色
                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.blue));
                    ssbuilder.setSpan(colorSpan, str.indexOf("#"), str.lastIndexOf("#") + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ExpandTextView expandTextView = helper.getView(R.id.text_content);
                    expandTextView.setText(ssbuilder);
                }else {
                    str = item.getData().getShowDesc();
                    ExpandTextView expandTextView = helper.getView(R.id.text_content);
                    expandTextView.setText(str);
                }
                /*ssbuilder = new SpannableStrinBuilder(str);
                //ForegroundColorSpan--文字前景色，BackgroundColorSpan--文字背景色
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.blue));
                ssbuilder.setSpan(colorSpan, str.indexOf("#"), str.lastIndexOf("#") + 1, Spannable.SPAN _EXCLUSIVE_EXCLUSIVE);
                helper.setText(R.id.tv_content,ssbuilder);*/
                /*WebView webView = helper.getView(R.id.webView);
                webView.loadData("<span style=\"color: #42516D;padding: 2px 6px  ;border: 1px solid #42516D;border-radius: 3px\">"+item.getData().getFollowStatusStr()+"</span>\n" +
                        "<span style=\"color: #01AAED;\"></span>\n" +
                        "<span>"+item.getData().getShowDesc()+"</span>","text/html","utf-8");*/
                //String str = "#" + item.getData().getStatusStr() + "# " + item.getData().getNote();


                break;
            case 2:
                helper.setText(R.id.tv_user,item.getData().getFollowName());
                helper.setText(R.id.tv_date,item.getData().getCreateTime().split(" ")[0]);
                String str1;
                if (StringUtils.notEmpty(item.getData().getFollowStatusStr())) {
                    str1 = "#" + item.getData().getFollowStatusStr() + "#" + item.getData().getShowDesc()  + "#" + item.getData().getProjState() +"#";
                    ssbuilder = new SpannableStringBuilder(str1);
                    //ForegroundColorSpan--文字前景色，BackgroundColorSpan--文字背景色
                    ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(mContext.getResources().getColor(R.color.blue));
                    int secondIndex = str1.indexOf("#",str1.indexOf("#")+1) + 1;
                    ssbuilder.setSpan(colorSpan1, str1.indexOf("#"), secondIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(mContext.getResources().getColor(R.color.top_bg));
                    ssbuilder.setSpan(colorSpan2, str1.indexOf("#",secondIndex+1), str1.lastIndexOf("#") + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }else {
                    str1 = item.getData().getShowDesc()  + "#" + item.getData().getProjState() +"#";
                    ssbuilder = new SpannableStringBuilder(str1);
                    //ForegroundColorSpan--文字前景色，BackgroundColorSpan--文字背景色
                    ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(mContext.getResources().getColor(R.color.top_bg));
                    int secondIndex = str1.indexOf("#",str1.indexOf("#")+1) + 1;
                    ssbuilder.setSpan(colorSpan1, str1.indexOf("#"), secondIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }


                helper.setText(R.id.tv_content,ssbuilder);
                break;
            case 3:
                helper.setText(R.id.tv_user,item.getData().getFollowName());
                helper.setText(R.id.tv_date,item.getData().getCreateTime().split(" ")[0]);
                if (StringUtils.notEmpty(item.getData().getFollowStatusStr())) {
                    helper.setText(R.id.tv_content,"#"+item.getData().getFollowStatusStr()+"#");
                }else {
                    helper.setText(R.id.tv_content,"");
                }
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
    }
}
