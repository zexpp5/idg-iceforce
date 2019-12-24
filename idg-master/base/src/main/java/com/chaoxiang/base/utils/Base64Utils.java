package com.chaoxiang.base.utils;

import android.util.Base64;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-03-15
 * @desc
 */
public class Base64Utils {
    public static String encode(String str){
        return new String(Base64.decode(str,Base64.DEFAULT));
    }

    public static String decode(String str){
        return new String(Base64.encode(str.getBytes(),Base64.DEFAULT));
    }
}
