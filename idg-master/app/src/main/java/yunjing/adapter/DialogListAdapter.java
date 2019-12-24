package yunjing.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.injoy.idg.R;

import java.util.List;

import yunjing.bean.ListDialogBean;

/**
 * Created by tujingwu on 2017/8/1.
 * 列表dialog适配器,主要是人事事务申请xxx弹窗适配器
 */

public class DialogListAdapter extends BaseQuickAdapter<ListDialogBean, BaseViewHolder> {
    public DialogListAdapter(@LayoutRes int layoutResId, @Nullable List<ListDialogBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, ListDialogBean item) {
        holder.setText(R.id.tvContent, item.getContent())
                .setChecked(R.id.rbnCheck, item.isCheck());
    }
}
