package yunjing.table;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cxgz.activity.home.adapter.ViewHolder;

import java.util.List;

/**
 * @author selson
 * @time 2016/11/16
 * 收藏界面
 */
public abstract class OpenOrderDetailAdapter<T> extends BaseAdapter {

    private Context mContext;
    protected int mItemLayoutId;
    protected List<T> mDatas;

    public OpenOrderDetailAdapter(Context context, List<T> mDatas, int itemLayoutId) {
        this.mContext = context;
        this.mItemLayoutId = itemLayoutId;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
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
            convert(viewHolder, getItem(position), position);
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
