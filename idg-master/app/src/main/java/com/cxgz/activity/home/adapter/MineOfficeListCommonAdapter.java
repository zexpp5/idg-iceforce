package com.cxgz.activity.home.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 *
 */
public abstract class MineOfficeListCommonAdapter<T> extends CommMethodAdapter<T>
{
    protected int mItemLayoutId;
    private int VIEW_COUNT = 50;
    private int index = 0;
    private int dataPage = 0;

    public MineOfficeListCommonAdapter(Context context, List<T> mDatas, int itemLayoutId)
    {
        super(context, mDatas);
        this.mItemLayoutId = itemLayoutId;
    }

    public int getDataPage()
    {
        return dataPage;
    }

    public void dataPageAdd()
    {
        dataPage++;
    }

    public void dataPageSub()
    {
        dataPage--;
    }

    public int getIndex()
    {
        return index;
    }

    public void indexAdd()
    {
        index++;
    }

    public void indexSub()
    {
        index = index != 0 ? index-- : index;
    }

    public int getViewCount()
    {
        return VIEW_COUNT;
    }

    @Override
    public int getCount()
    {
        //ori表示到目前为止的前几页的总共的个数。
        int ori = VIEW_COUNT * index;
        //值的总个数-前几页的个数就是这一页要显示的个数，如果比默认的值小，说明这是最后一页，只需显示这么多就可以了
        if (null != mDatas)
        {
            if (mDatas.size() - ori < VIEW_COUNT)
            {
                return mDatas.size() - ori;
            }
            //如果比默认的值还要大，说明一页显示不完，还要用换一页显示，这一页用默认的值显示满就可以了。
            else
            {
                return VIEW_COUNT;
            }
        } else
        {
            return VIEW_COUNT;
        }

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

    public int getVIEW_COUNT()
    {
        return VIEW_COUNT;
    }

    public void setVIEW_COUNT(int VIEW_COUNT)
    {
        this.VIEW_COUNT = VIEW_COUNT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final ViewHolder viewHolder = getViewHolder(position, convertView, parent);
        try
        {
            convert(viewHolder, getItem(position + index * VIEW_COUNT), position + index * VIEW_COUNT);
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