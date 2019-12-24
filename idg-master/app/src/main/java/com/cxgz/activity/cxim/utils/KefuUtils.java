package com.cxgz.activity.cxim.utils;

import com.cxgz.activity.db.dao.SDUserEntity;
import com.chaoxiang.base.config.Constants;

import java.util.Iterator;
import java.util.List;

/**
 * User: Selson
 * Date: 2016-12-15
 * FIXME
 */
public class KefuUtils
{
    public static List<SDUserEntity> reTurnNoKefu(List<SDUserEntity> userEntities)
    {
        Iterator<SDUserEntity> chk_it = userEntities.iterator();
        while (chk_it.hasNext())
        {
            SDUserEntity checkWork = chk_it.next();
            if (checkWork.getUserType() == Constants.USER_TYPE_KEFU)
            {
                chk_it.remove();
            }
        }
        System.out.println("移除后：" + userEntities.size() + "数量！");
        return userEntities;
    }
} 