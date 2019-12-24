package com.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Administrator on 2016/4/18 0018.
 */
public class BroadcastUtil
{

    public static final String MAIN = "MAIN"; // 一种状态
    public static final String MESSAGE = "MESSAGE"; // 相当于过滤器
    public static final String CODE = "CODE"; // 传递码
    public static final String OBJECT = "OBJECT"; // 消息对象(内容)


    public static void senMainBroadcast(Context context, String action,
                                        int code, Bundle bundle) {
        Intent mIntent = new Intent(action);
        if (bundle == null) {
            mIntent.putExtra(CODE, code);
            mIntent.putExtra(OBJECT, bundle);
        } else {
            bundle.putInt(CODE, code);
            mIntent.putExtras(bundle);
        }
        context.sendBroadcast(mIntent);
    }

    public class SendStateCode {

        public static final int MYSOHOTAB_VIEWPAGE = 1001;

        public static final int MYSOHOTAB_SPINNER = 1002;

    }

}
