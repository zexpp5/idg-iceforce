package com.chaoxiang.base.utils;


import android.renderscript.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Created by selson on 2018/6/11.
 */
public class StringConverter implements JsonSerializer<String>,
        JsonDeserializer<String>
{
    @Override
    public String deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws
            JsonParseException
    {
        return json.getAsJsonPrimitive().getAsString();
    }

    @Override
    public JsonElement serialize(String src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context)
    {
        if (src == null)
        {
            return new JsonPrimitive("");
        } else
        {
            return new JsonPrimitive(src.toString());
        }
    }
}

