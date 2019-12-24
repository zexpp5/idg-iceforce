package com.superdata.im.utils;

import com.chaoxiang.base.utils.SDLogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author zjh
 * @date 2016/1/4
 * @desc 推送数据解析工具类
 */
public class CxParseUtils
{
    /**
     * @param jsonData
     * @return String数组
     */
    public static Map<String,String> parsePlushJson(String jsonData) {
        try {
            SDLogUtil.debug("jsonData="+jsonData);
            Map<String,String> result = new HashMap<>();
            JSONObject jsonObject = new JSONObject(jsonData);
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = jsonObject.getString(key);
                result.put(key,value);
            }
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
