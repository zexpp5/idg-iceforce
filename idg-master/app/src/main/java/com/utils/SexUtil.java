package com.utils;

import android.content.Context;
import android.text.TextUtils;

import com.injoy.idg.R;

/**
 * Created by injoy-pc on 2016/5/6.
 */
public class SexUtil
{

    private static final String MAN = "1";

    public static boolean isMan(String sex)
    {
        if (TextUtils.isEmpty(sex))
        {
            return false;
        }
        return sex.equals(MAN);
    }

    public static String getSexStr(Context context, int code)
    {
        return code == 1 ?
                context.getResources().getString(R.string.man)
                : context.getResources().getString(R.string.woman);
    }

    public static String getSexCode(Context context, String sex)
    {
        String sexString = "1";

        if (sex.equals(context.getResources().getString(R.string.man)))
        {
            return sexString = "1";
        }
        if (sex.equals(context.getResources().getString(R.string.woman)))
        {
            return sexString = "2";
        }

        return sexString;
    }
}
