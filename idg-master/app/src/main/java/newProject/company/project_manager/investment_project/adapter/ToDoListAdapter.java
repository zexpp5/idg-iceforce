package newProject.company.project_manager.investment_project.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.injoy.idg.R;

import java.util.List;

import newProject.company.project_manager.investment_project.bean.ToDoListBean;

/**
 * Created by zsz on 2019/4/9.
 */

public class ToDoListAdapter extends BaseQuickAdapter<ToDoListBean.DataBeanX.DataBean,BaseViewHolder> {
    public ToDoListAdapter(@Nullable List<ToDoListBean.DataBeanX.DataBean> data) {
        super(R.layout.adapter_to_do_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final ToDoListBean.DataBeanX.DataBean item) {
       /* String str = "#" + item.getProjName() + "#" + item.getShowDesc();
        SpannableStringBuilder ssbuilder = new SpannableStringBuilder(str);
        //ForegroundColorSpan--文字前景色，BackgroundColorSpan--文字背景色
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PotentialProjectsDetailActivity.class);
                intent.putExtra("projName",item.getProjName());
                intent.putExtra("projId", item.getBusId());
                mContext.startActivity(intent);
            }
        };
        ssbuilder.setSpan(clickableSpan, str.indexOf("#"), str.lastIndexOf("#") + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.blue));
        ssbuilder.setSpan(colorSpan, str.indexOf("#"), str.lastIndexOf("#") + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        helper.setText(R.id.tv_content,ssbuilder);*/
        helper.setText(R.id.tv_name,item.getProjName() + " ");
        helper.setText(R.id.tv_content,item.getShowDesc());
        helper.addOnClickListener(R.id.tv_content);
        helper.addOnClickListener(R.id.tv_name);
    }

}
