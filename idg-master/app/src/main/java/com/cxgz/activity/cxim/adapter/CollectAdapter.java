package com.cxgz.activity.cxim.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cxgz.activity.cx.base.ArrayListAdapter;
import com.cxgz.activity.cxim.bean.BusinessFileBean;
import com.facebook.drawee.view.SimpleDraweeView;
import com.injoy.idg.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tablayout.view.textview.FontTextView;

/**
 * @author selson
 * @time 2016/11/16
 * 收藏界面
 */
public class CollectAdapter extends ArrayListAdapter<BusinessFileBean>
{

    private Activity context;

    public CollectAdapter(Activity context)
    {
        super(context);
    }

    public CollectAdapter(Activity context, List<BusinessFileBean> mList)
    {
        super(context);
        this.context = context;
        this.mList = mList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder;
        if (convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.item_mine_collect, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        BusinessFileBean businessFileBean = mList.get(position);
        viewHolder.collectUserNameTv.setText(businessFileBean.getFileType());
        return convertView;
    }

    //头像点击事件
    @OnClick(R.id.collect_user_icon_img)
    public void onClick()
    {

    }

    static class ViewHolder
    {
        @Bind(R.id.collect_user_icon_img)
        SimpleDraweeView collectUserIconImg;
        @Bind(R.id.collect_user_unread_count)
        FontTextView collectUserUnreadCount;
        @Bind(R.id.collect_user_name_tv)
        FontTextView collectUserNameTv;
        @Bind(R.id.collect_user_reason_type_tv)
        FontTextView collectUserReasonTypeTv;
        @Bind(R.id.collect_date_tv)
        FontTextView collectDateTv;
        @Bind(R.id.collect_user_reason_icon_img)
        ImageView collectUserReasonIconImg;
        @Bind(R.id.collect_user_reason_title_tv)
        TextView collectUserReasonTitleTv;
        @Bind(R.id.collect_user_reason_content_tv)
        TextView collectUserReasonContentTv;

        public ViewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }

    }

}
