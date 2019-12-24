package com.cxgz.activity.cx.utils.filter;

import com.chaoxiang.base.utils.SDLogUtil;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.cxgz.activity.home.adapter.ABaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zjh
 *         Created by root on 15-7-22.
 */
public class UserFilter extends SDCommFilter<SDUserEntity>
{

    public UserFilter(ABaseAdapter<SDUserEntity> adapter, String filterAttrName)
    {
        super(adapter, filterAttrName);
    }

    @Override
    public List<SDUserEntity> addCustomFiltering(List<SDUserEntity> list, CharSequence constraint)
    {
        if ("".equals(constraint))
        {
            return null;
        }
        List<SDUserEntity> filterList = new ArrayList<>();
        for (SDUserEntity userEntity : list)
        {
            String header = String.valueOf(constraint.charAt(0)).trim();
            SDLogUtil.debug("header==" + header);
            SDLogUtil.debug("UserHeader=" + userEntity.getName());
            if (userEntity.getName().contains(header))
            {
                filterList.add(userEntity);
            }
        }
        return filterList;
    }
}
