package com.cxgz.activity.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.PersonalInfoActivity;
import com.cxgz.activity.cxim.person.SDPersonalAlterActivity;
import com.injoy.idg.R;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.utils.FileDownloadUtil;

import java.util.List;

import yunjing.utils.IntentUtils;

/**
 *
 */
public abstract class CommMethodAdapter<T> extends ABaseAdapter<T>
{
    protected SDGson mGson;

    public CommMethodAdapter(Context context, List<T> mDatas)
    {
        super(context, mDatas);
        try
        {
            mGson = new SDGson();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 显示头像
     *
     * @param holder
     * @param userEntity
     */
    protected void showCricleHeadImg(final boolean isShow, ViewHolder holder, final SDUserEntity userEntity)
    {
        SimpleDraweeView draweeView = holder.getView(R.id.iv_header_img);
        draweeView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (isShow)
                {
                    Intent intent = new Intent(mContext, SDPersonalAlterActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(com.utils.SPUtils.USER_ID, userEntity.getImAccount() + "");
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            }
        });

        Glide.with(mContext)
                .load(userEntity.getIcon())
                .error(R.mipmap.temp_user_head)
                .into(draweeView);
    }

}
