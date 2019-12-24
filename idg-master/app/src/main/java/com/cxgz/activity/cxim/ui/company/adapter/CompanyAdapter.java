package com.cxgz.activity.cxim.ui.company.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cxgz.activity.cxim.ui.company.bean.DeptBeanList;
import com.injoy.idg.R;

import java.util.List;

/**
 * Created by selson on 2017/8/21.
 * 公司部门list
 */
public class CompanyAdapter extends BaseQuickAdapter<DeptBeanList.Data, BaseViewHolder>
{
    public CompanyAdapter(@Nullable List<DeptBeanList.Data> data)
    {
        super(R.layout.item_im_company_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DeptBeanList.Data item)
    {
        helper.setText(R.id.tv_dept, item.getDeptName());
        helper.setText(R.id.tv_dept_num, "(" + item.getNum() + ")");
    }
}
