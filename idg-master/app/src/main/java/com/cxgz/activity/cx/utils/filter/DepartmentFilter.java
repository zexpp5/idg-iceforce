package com.cxgz.activity.cx.utils.filter;

import com.cxgz.activity.home.adapter.ABaseAdapter;
import com.entity.SDDepartmentEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 15-7-22.
 */
public class DepartmentFilter extends SDCommFilter<SDDepartmentEntity>
{

    public DepartmentFilter(ABaseAdapter<SDDepartmentEntity> adapter, String filterAttrName)
    {
        super(adapter, filterAttrName);
    }

    @Override
    public List<SDDepartmentEntity> addCustomFiltering(List<SDDepartmentEntity> list, CharSequence constraint)
    {
        if ("".equals(constraint))
        {
            return null;
        }
        List<SDDepartmentEntity> filterList = new ArrayList<>();
        for (SDDepartmentEntity departmentEntity : list)
        {
            String header = String.valueOf(constraint.charAt(0)).trim();
            if (header.equalsIgnoreCase(departmentEntity.getHeader()))
            {
                filterList.add(departmentEntity);
            }
        }
        return filterList;
    }
}
