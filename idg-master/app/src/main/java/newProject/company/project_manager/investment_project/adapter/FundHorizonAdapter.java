package newProject.company.project_manager.investment_project.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.utils.ImageUtils;
import com.chaoxiang.base.utils.StringUtils;
import com.injoy.idg.R;

import java.util.List;

import newProject.company.project_manager.investment_project.bean.NameListsBean;
import newProject.company.project_manager.investment_project.bean.PIMFundListBean;
import yunjing.utils.DisplayUtil;

/**
 * Created by zsz on 2019/5/24.
 */

public class FundHorizonAdapter extends BaseQuickAdapter<PIMFundListBean.DataBeanX.DataBean, BaseViewHolder>
{
    Context mContext;

    public FundHorizonAdapter(Context mContext, @Nullable List<PIMFundListBean.DataBeanX.DataBean> data)
    {
        super(R.layout.adapter_fund_horizon, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, PIMFundListBean.DataBeanX.DataBean item)
    {
//        String myNickName = DisplayUtil.getUserInfo(mContext, 1);
//        if (StringUtils.notEmpty(myNickName))
//        {
//            ImageUtils.waterMarking(mContext, true, false, helper.getView(R.id.ll_water), myNickName);
//        }

        helper.setText(R.id.tv_name, item.getFundName());
        TextView textView = helper.getView(R.id.tv_name);
        View view = helper.getView(R.id.view_line);
        if (item.isChoose())
        {
            textView.setTextColor(mContext.getResources().getColor(R.color.main_red));
            view.setBackgroundColor(mContext.getResources().getColor(R.color.main_red));
        } else
        {
            textView.setTextColor(mContext.getResources().getColor(R.color.text_black_s));
            view.setBackgroundColor(mContext.getResources().getColor(R.color.bg_gray));
        }
        helper.addOnClickListener(R.id.tv_name);
    }
}
