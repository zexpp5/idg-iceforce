package newProject.company.event.fragment.adapter;

import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.injoy.idg.R;

import java.util.List;

import newProject.company.project_manager.investment_project.bean.ToDoListBean;

/**
 * Created by zsz on 2019/4/11.
 */

public class RemindAdapter extends BaseQuickAdapter<ToDoListBean.DataBeanX.DataBean,BaseViewHolder> {
    public RemindAdapter(@Nullable List<ToDoListBean.DataBeanX.DataBean> data) {
        super(R.layout.adapter_remind, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ToDoListBean.DataBeanX.DataBean item) {
        helper.setText(R.id.tv_sourceUser,item.getSourceUser());
        String str = item.getShowDesc();
        if(str.contains("@")){
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder(str);
            //ForegroundColorSpan--文字前景色，BackgroundColorSpan--文字背景色
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.blue));
            ssbuilder.setSpan(colorSpan, str.indexOf("@"), str.indexOf("@") + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            helper.setText(R.id.tv_desc,ssbuilder);
        }else {
            helper.setText(R.id.tv_desc,str);
        }


        helper.setText(R.id.tv_name,item.getProjName());
        helper.addOnClickListener(R.id.tv_name);
    }
}
