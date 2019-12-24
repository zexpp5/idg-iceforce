package com.cxgz.activity.cxim.ui.voice.list;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.view.BitmapManager;
import com.facebook.drawee.view.SimpleDraweeView;
import com.injoy.idg.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by selson on 2017/10/21.
 */
public class VoiceMeetingAdapter extends BaseQuickAdapter<VoiceListBean.DataBean, BaseViewHolder>
{
    Context mContext;

    public VoiceMeetingAdapter(Context context, @Nullable List<VoiceListBean.DataBean> data)
    {
        super(R.layout.chat_voice_meeting_list_item, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, VoiceListBean.DataBean item)
    {
        helper.setText(R.id.name, item.getTitle());
        helper.setText(R.id.num, "(" + item.getCount() + ")");
        helper.setText(R.id.date, DateUtils.getDate("yyyy-MM-dd", item.getCreateTime()));

        String iconName = "meetingName_" + item.getEid();
//        if (StringUtils.notEmpty(item.getIcon()))
//            Glide.with(mContext)
//                    .load(item.getIcon())
//                    .fitCenter()
//                    .placeholder(R.mipmap.group_icon)
//                    .error(R.mipmap.group_icon)
//                    .crossFade()
//                    .into((ImageView) helper.getView(R.id.icon));
        if (StringUtils.notEmpty(item.getUseId()))
        {
            String[] sourceStrArray = item.getUseId().split(",");
            List<String> userIdList = Arrays.asList(sourceStrArray);
            helper.getView(R.id.icon).setTag(helper.getLayoutPosition());
            BitmapManager.createVoiceMeetingIcon((Application) mContext.getApplicationContext(), (SimpleDraweeView) helper.getView
                    (R.id.icon), userIdList, iconName, helper
                    .getLayoutPosition());

            if (((SimpleDraweeView) helper.getView(R.id.icon)).getDrawable() == null)
            {
                helper.getView(R.id.icon).setBackgroundResource(R.mipmap.group_icon);
            }
        } else
        {
            helper.getView(R.id.icon).setBackgroundResource(R.mipmap.group_icon);
        }

    }
}
