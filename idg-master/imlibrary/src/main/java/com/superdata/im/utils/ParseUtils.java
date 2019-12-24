package com.superdata.im.utils;

import com.chaoxiang.base.utils.SDLogUtil;
import com.superdata.im.entity.PushModelEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author zjh
 */
public class ParseUtils
{
    /**
     * @param json
     * @return String数组
     */
    public static PushModelEntity parsePlushJson(String json)
    {
        try
        {
            SDLogUtil.debug("json=" + json);
            PushModelEntity pushModelEntity = new PushModelEntity();
            pushModelEntity.setCount(1);
            JSONObject jsonObject = new JSONObject(json);
            Iterator<String> iterator = jsonObject.keys();
            Map<String, String> map = new HashMap<>();
            while (iterator.hasNext())
            {
                String key = iterator.next();
                map.put(key, jsonObject.getString(key));
            }
            pushModelEntity.setData(map);
            return pushModelEntity;
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 把数据源HashMap转换成json
     * @param map
     */
    public static String hashMapToJson(HashMap map)
    {
        if (map != null)
        {

            String string = "{";
            for (Iterator it = map.entrySet().iterator(); it.hasNext(); )
            {
                Entry e = (Entry) it.next();
                string += "'" + e.getKey() + "':";
                string += "'" + e.getValue() + "',";
            }
            string = string.substring(0, string.lastIndexOf(","));
            string += "}";
            return string;
        } else
        {
            return "{}";
        }
    }


    public static String pushJson(String type, HashMap map)
    {
        String json = "{'" + type + "':" + hashMapToJson(map) + "}";
        SDLogUtil.debug("send push = " + json);
        return json;
    }

    public static String pushJson(String type)
    {
        String json = "{'" + type + "':" + hashMapToJson(null) + "}";
        SDLogUtil.debug("send push = " + json);
        return json;
    }
}
