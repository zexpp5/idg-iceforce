package com.cxgz.activity.superqq.adapter;

import android.content.Context;

import com.cxgz.activity.home.adapter.CommMethodAdapter;

import java.util.List;

public abstract class SDWorkCircleBaseAdapter<T> extends CommMethodAdapter<T>
{

    public SDWorkCircleBaseAdapter(Context context, List<T> mDatas)
    {
        super(context, mDatas);
        mContext = context;
    }
}
