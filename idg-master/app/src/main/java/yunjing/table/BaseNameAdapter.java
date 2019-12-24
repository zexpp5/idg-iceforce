package yunjing.table;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cxgz.activity.home.adapter.ViewHolder;

import java.util.List;


public abstract class BaseNameAdapter<T> extends BaseAdapter {
    private Context mContext;
    protected int mItemLayoutId;
    protected List<T> mDatas;

    private int VIEW_COUNT = 15;
    private int index = 0;

    public BaseNameAdapter(Context context, List<T> mDatas, int itemLayoutId) {
        this.mContext = context;
        this.mItemLayoutId = itemLayoutId;
        this.mDatas = mDatas;
    }
    public int getViewCount() {
        return VIEW_COUNT;
    }
    @Override
    public int getCount() {
        //ori表示到目前为止的前几页的总共的个数。
        int ori = VIEW_COUNT * index;
        //值的总个数-前几页的个数就是这一页要显示的个数，如果比默认的值小，说明这是最后一页，只需显示这么多就可以了
        if (null != mDatas) {
            if (mDatas.size() - ori < VIEW_COUNT) {
                return mDatas.size() - ori;
            }
            //如果比默认的值还要大，说明一页显示不完，还要用换一页显示，这一页用默认的值显示满就可以了。
            else {
                return VIEW_COUNT;
            }
        } else {
            return VIEW_COUNT;
        }
    }


    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final com.cxgz.activity.home.adapter.ViewHolder viewHolder = getViewHolder(position, convertView, parent);
        try {
            convert(viewHolder, getItem(position + index * VIEW_COUNT), position + index * VIEW_COUNT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return viewHolder.getConvertView();
    }

    public abstract void convert(ViewHolder helper, T item, int position);

    private ViewHolder getViewHolder(int position, View convertView, ViewGroup parent) {
        return ViewHolder.get(mContext, convertView, parent, mItemLayoutId, position, null);
    }

}