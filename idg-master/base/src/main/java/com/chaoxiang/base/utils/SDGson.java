package com.chaoxiang.base.utils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * Gson 类
 */
public class SDGson implements Serializable
{
    private Gson gson;

    public SDGson()
    {
        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(String.class, new StringConverter());
        gson = gb.create();
    }

    public Gson getGson()
    {
        return gson;
    }

    /**
     * josn转对象
     *
     * @param json
     * @param classOfT
     * @param <T>
     * @return
     */
    public <T> T fromJson(String json, Class<T> classOfT)
    {
        try
        {
            T result = gson.fromJson(json, classOfT);
            return result;
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * josn转对象1
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public <T> T fromJson(String json, Type type)
    {
        try
        {
            T result = (T) gson.fromJson(json, type);
            return result;
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * josn转对象2
     *
     * @param json
     * @return
     */
    public static <T> T toObject(String json, Type type)
    {
        try
        {
            GsonBuilder gb = new GsonBuilder();
            gb.registerTypeAdapter(String.class, new StringConverter());
            Gson gson = gb.create();
            T result = gson.fromJson(json, type);
            return result;
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 对象转josn
     *
     * @param jsonElement
     * @return
     */
    public static String toJson(Object jsonElement)
    {
        try
        {
            GsonBuilder gb = new GsonBuilder();
            gb.registerTypeAdapter(String.class, new StringConverter());
            Gson gson = gb.create();
            return gson.toJson(jsonElement);
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }


}
