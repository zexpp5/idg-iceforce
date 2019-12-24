package com.cxgz.activity.cxim.workCircle.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.entity.chat.IMWorkCircle;
import com.cxgz.activity.cx.base.ArrayListAdapter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.injoy.idg.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import tablayout.view.textview.FontTextView;

/**
 * @author selson
 */
public class WorkCircleCommentListAdapter extends ArrayListAdapter<IMWorkCircle>
{

    private Activity context;

    public WorkCircleCommentListAdapter(Activity context)
    {
        super(context);
    }

    public WorkCircleCommentListAdapter(Activity context, List<IMWorkCircle> mList)
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
            convertView = layoutInflater.inflate(R.layout.item_im_work_circle_comment, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        IMWorkCircle imWorkCircle = mList.get(position);

        //渲染数据
        if (StringUtils.notEmpty(imWorkCircle.getTitle()))
            viewHolder.tvTitle.setText(imWorkCircle.getTitle());
        if (StringUtils.notEmpty(imWorkCircle.getRemark()))
            viewHolder.tvContent.setText(imWorkCircle.getRemark());
        if (StringUtils.notEmpty(imWorkCircle.getUserName()))
            viewHolder.tvUserName.setText(imWorkCircle.getUserName());
        if (StringUtils.notEmpty(imWorkCircle.getCreateTime()))
            viewHolder.tvDate.setText(DateUtils.getFormatDate("M月d日 HH:mm", Long.parseLong(imWorkCircle.getCreateTime())));

        Glide.with(context)
                .load(imWorkCircle.getIcon())
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.temp_user_head)
                .error(R.mipmap.temp_user_head)
                .into(viewHolder.imgIcon);


        return convertView;
    }

    static class ViewHolder
    {
        @Bind(R.id.img_icon)
        SimpleDraweeView imgIcon;
        @Bind(R.id.tv_user_name)
        FontTextView tvUserName;
        @Bind(R.id.tv_content)
        FontTextView tvContent;
        @Bind(R.id.tv_date)
        FontTextView tvDate;
        @Bind(R.id.tv_title)
        TextView tvTitle;

        public ViewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }

    }

}
