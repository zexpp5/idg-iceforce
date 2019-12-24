package com.cxgz.activity.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * BusinessFileAdapter
 */
public abstract class ABaseAdapter<T> extends BaseAdapter
{
    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<T> mDatas = new ArrayList<>();

    public ABaseAdapter(Context context, List<T> mDatas)
    {
        try
        {
            this.mContext = context;
            this.mInflater = LayoutInflater.from(mContext);
            this.mDatas = mDatas;
        } catch (Exception e)
        {

        }
    }

    @Override
    public int getCount()
    {
        return mDatas.size();
    }

    public void remove(int pos)
    {
        mDatas.remove(pos);
    }

    public void addAll(List<T> list)
    {
        mDatas.addAll(list);
    }

    public void add(T t, int pos)
    {
        mDatas.add(pos, t);
    }

    public void clear()
    {
        mDatas.clear();
    }

    public List<T> getAll()
    {
        return mDatas;
    }

    public void setAll(List<T> mDatas)
    {
        this.mDatas = mDatas;
    }

    @Override
    public T getItem(int position)
    {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);

}
