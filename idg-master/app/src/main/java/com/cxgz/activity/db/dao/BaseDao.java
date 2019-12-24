package com.cxgz.activity.db.dao;


import android.content.Context;
import android.database.Cursor;

import com.lidroid.xutils.DbUtils;
import  com.cxgz.activity.db.help.SDDbHelp;

public class BaseDao
{
    protected DbUtils db;

    public BaseDao(Context context)
    {
        db = SDDbHelp.createDbUtils(context);
    }

    public BaseDao()
    {
    }

    protected void closeCusor(Cursor cursor)
    {
        if (cursor != null)
        {
            cursor.close();
            cursor = null;
        }
    }
}
