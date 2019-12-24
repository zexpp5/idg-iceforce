package com.superdata.im.utils;

import android.content.Context;

import com.chaoxiang.base.utils.SPUtils;
import com.superdata.im.constants.CxSPIMKey;

import static com.chaoxiang.base.utils.SPUtils.get;

/**
 * Created by selson on 2018/9/27.
 */

public class SPUtilsTool
{
    //返回是否被挤退
    public static boolean getIsLoginOut(Context context)
    {
        return (Boolean) get(context, CxSPIMKey.IS_LOGINOUT_MYSELF, false);
    }

    //返回是否提示版本更新  false提示 true不提示
    public static boolean getVersionUpdate(Context context)
    {
        return (Boolean) get(context, SPUtils.VERSION_UPDATE, false);
    }

    public static boolean getVersionUpdate(Context context, int serviceVersion)
    {
        int localVersionCode = (int) SPUtils.get(context, SPUtils.VERSION, 0);
        if (serviceVersion > localVersionCode)
        {
            SPUtils.put(context, SPUtils.VERSION, serviceVersion);
            return false;
        } else
        {
            return (Boolean) SPUtils.get(context, SPUtils.VERSION_UPDATE, false);
        }
    }
}
