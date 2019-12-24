package com.cxgz.activity.cx.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.entity.SDDepartmentEntity;
import com.injoy.idg.R;
import com.cxgz.activity.cx.msg.SelectedContactFragment;
import com.cxgz.activity.cxim.adapter.ViewHolder;
import com.cxgz.activity.cxim.utils.SDBaseSortAdapter;
import com.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zjh
 */
public class SDSelectedDpAdapter extends SDBaseSortAdapter<SDDepartmentEntity>
{

    private List<SDDepartmentEntity> selectedData = new ArrayList<>();
    private SelectedContactFragment.OnSelectedDataListener listener;

    public SDSelectedDpAdapter(Context context, List<SDDepartmentEntity> list, List<SDDepartmentEntity> selectedData, SelectedContactFragment.OnSelectedDataListener listener)
    {
        super(context, list);
        mDatas = list;
        if (selectedData != null)
        {
            this.selectedData = selectedData;
        }
        this.listener = listener;
    }

    public void addAllCompany()
    {
        SDDepartmentEntity allCompany = new SDDepartmentEntity();
        allCompany.setDpId(-1);
        allCompany.setDpName(StringUtil.getResourceString(mContext, R.string.all_company));
        allCompany.setHeader("☆");
        mDatas.add(0, allCompany);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = ViewHolder.get(mContext, convertView, null, R.layout.sd_address_list_item, position);
        final SDDepartmentEntity departmentEntity = mDatas.get(position);
        holder.setSingleSelected(R.id.cbAddress, selectedData.contains(departmentEntity) ? true : false);
        holder.setOnclickListener(R.id.cbAddress, new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CheckBox cb = (CheckBox) v;
                if (cb.isChecked())
                {
                    if (!selectedData.contains(departmentEntity))
                    {
                        selectedData.add(departmentEntity);
                    }
                } else
                {
                    selectedData.remove(departmentEntity);
                }
                if (listener != null)
                {
                    listener.onSelectedDpData(selectedData);
                }
            }
        });
        holder.setVisibility(R.id.iv_header_img, View.GONE);
        holder.setText(R.id.tv_name, departmentEntity.getDpName());

        String header = departmentEntity.getHeader();
        holder.setVisibility(R.id.tv_header, View.GONE);
        if (position == 0 || header != null && !header.equals((getItem(position - 1)).getHeader()))
        {
            if ("".equals(header) || "↑".equals(header))
            {
                holder.setVisibility(R.id.tv_header, View.GONE);
            } else
            {
                holder.setVisibility(R.id.tv_header, View.VISIBLE);
                holder.setText(R.id.tv_header, header);
            }
        } else
        {
            holder.setVisibility(R.id.tv_header, View.GONE);
        }

        return holder.getConvertView();
    }

    public List<SDDepartmentEntity> getSelectedData()
    {
        return selectedData;
    }
}
