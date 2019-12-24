package com.utils;

import com.cxgz.activity.db.dao.SDUserEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import newProject.bean.CCBean;

/**
 * Created by selson on 2017/10/25.
 */

public class ToolUtils
{
    //转化user成arrayString
    public static String userListToStringArray(List<SDUserEntity> sdUserEntityList)
    {
        JSONArray array = new JSONArray();
        if (sdUserEntityList == null)
        {
            return null;
        }
        if (sdUserEntityList.size() > 0)
        {
            for (int i = 0; i < sdUserEntityList.size(); i++)
            {
                JSONObject object = new JSONObject();
                try
                {
                    object.put("eid", sdUserEntityList.get(i).getEid());
                    object.put("name", sdUserEntityList.get(i).getName());
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
                array.put(object);
            }
        } else
        {
            return null;
        }

        return array.toString();
    }
}
