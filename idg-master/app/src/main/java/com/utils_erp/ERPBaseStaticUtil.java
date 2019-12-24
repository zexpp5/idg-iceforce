package com.utils_erp;

import android.content.Context;
import android.text.TextUtils;

import com.bean_erp.EPRBaseStaticBean;
import com.chaoxiang.base.utils.SDGson;
import com.utils.SPUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 把静态数据转化为实体类bean
 * Created by cx on 2016/11/2.
 */
public class ERPBaseStaticUtil
{
    private static List<EPRBaseStaticBean.DataBean> mList = new ArrayList<>();
    private static Map<String, List<EPRBaseStaticBean.DataBean>> mMap = new HashMap<>();

    public static List<EPRBaseStaticBean.DataBean> getListStaticValues(Context context, String code)
    {
        if (context == null || TextUtils.isEmpty(code))
        {
            return null;
        }
        return getStaticMap(context).get(code);
    }

    public static Map<String, List<EPRBaseStaticBean.DataBean>> getStaticMap(Context context)
    {
        List<EPRBaseStaticBean.DataBean> tempList = getStaticValues(context);
        if (mList == null || mList.isEmpty() || mMap == null || mMap.isEmpty())
        {
            mList = tempList;
            buildMap(mList);
            return mMap;
        }

        if (tempList == null || tempList.isEmpty())
        {
            mMap.clear();
            mList.clear();
            return mMap;
        }

        if (tempList.size() != mList.size())
        {
            mList = tempList;
            buildMap(mList);
            return mMap;
        }

        return mMap;
    }

    public static List<EPRBaseStaticBean.DataBean> getStaticValues(Context context)
    {
        String values = (String) SPUtils.get(context, SPUtils.EPR_ALL_STATE_VALUE, "");
        EPRBaseStaticBean allStaticEntity = new SDGson().fromJson(values, EPRBaseStaticBean.class);//静态数据
        List<EPRBaseStaticBean.DataBean> data = allStaticEntity.getData();
        return data;
    }

    private static void buildMap(List<EPRBaseStaticBean.DataBean> list)
    {
        mMap.clear();
        if (list == null || list.isEmpty())
        {
            return;
        }
        for (EPRBaseStaticBean.DataBean v : list)
        {
            List<EPRBaseStaticBean.DataBean> values = null;
            if (mMap.containsKey(v.getCode()))
            {
                values = mMap.get(v.getCode());
                if (values == null)
                {
                    values = new ArrayList<>();
                }
            } else
            {
                values = new ArrayList<>();
            }
            values.add(v);
            mMap.put(v.getCode(), values);
        }
    }


    public static List<EPRBaseStaticBean.DataBean> turnStaticDataToList(Context context)
    {
        String values = (String) SPUtils.get(context, SPUtils.EPR_ALL_STATE_VALUE, "");
        EPRBaseStaticBean bean = new SDGson().fromJson(values, EPRBaseStaticBean.class);
        return bean.getData();
    }

    //查找相应的type
    public static String turnToName(Context context,String code, String  values) {
        String str = "";
        List<EPRBaseStaticBean.DataBean> entities = getListStaticValues(context,code);
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).getValue() .equals(values)) {
                str = entities.get(i).getName();
            }
        }
        return str;
    }
}
