package yunjing.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by zy on 2017/7/26.
 */

public abstract class ListCommonAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {


    private Context mContext;
    protected int mItemLayoutId;



    public ListCommonAdapter(Context context, @Nullable List<T>  data,@LayoutRes int layoutResId) {
        super(layoutResId, data);
        mContext = context;
        this.mItemLayoutId = layoutResId;

    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, T item) {

        convert(baseViewHolder,item,0);

    }


    public abstract void convert(BaseViewHolder helper, T item,int position);



}
