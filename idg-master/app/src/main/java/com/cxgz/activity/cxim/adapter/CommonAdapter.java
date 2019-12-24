package com.cxgz.activity.cxim.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.cxgz.activity.home.adapter.ABaseAdapter;

import java.util.List;

/**
 * @author omg
 */
public abstract class CommonAdapter<T> extends ABaseAdapter<T>
{
    protected int mItemLayoutId;

    public CommonAdapter(Context context, List<T> mDatas, int itemLayoutId)
    {
        super(context, mDatas);
        this.mItemLayoutId = itemLayoutId;
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

    public void remove(Object o)
    {
        mDatas.remove(o);
    }

    public void addAll(List<T> list)
    {
        mDatas.addAll(list);
    }

    public void add(T t, int pos)
    {
        mDatas.add(pos, t);
    }

    public void add(T t)
    {
        mDatas.add(t);
    }

    public void clear()
    {
        mDatas.clear();
    }

    public List<T> getAll()
    {
        return mDatas;
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
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final ViewHolder viewHolder = getViewHolder(position, convertView, parent);
        try
        {
            convert(viewHolder, getItem(position), position);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return viewHolder.getConvertView();
    }

    public abstract void convert(ViewHolder helper, T item, int position);

    private ViewHolder getViewHolder(int position, View convertView, ViewGroup parent)
    {
        return ViewHolder.get(mContext, convertView, parent, mItemLayoutId, position);
    }

}