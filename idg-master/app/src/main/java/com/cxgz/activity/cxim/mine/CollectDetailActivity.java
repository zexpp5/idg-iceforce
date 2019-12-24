package com.cxgz.activity.cxim.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cxgz.activity.cxim.base.BaseActivity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.injoy.idg.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tablayout.view.textview.FontTextView;

/**
 * User: Selson
 * Date: 2016-11-16
 * FIXME
 */
public class CollectDetailActivity extends BaseActivity
{
    @Bind(R.id.collect_detail_user_icon_img)
    SimpleDraweeView collectDetailUserIconImg;
    @Bind(R.id.collect_detail_user_name_tv)
    FontTextView collectDetailUserNameTv;
    @Bind(R.id.collect_detail_user_reason_type_tv)
    FontTextView collectDetailUserReasonTypeTv;
    @Bind(R.id.collect_detail_user_add_tag_tv)
    FontTextView collectDetailUserAddTagTv;
    @Bind(R.id.collect_detail_user_add_tag_ll)
    LinearLayout collectDetailUserAddTagLl;
    @Bind(R.id.collect_detail_user_reason_icon_img)
    ImageView collectDetailUserReasonIconImg;
    @Bind(R.id.collect_detail_user_reason_title_tv)
    TextView collectDetailUserReasonTitleTv;
    @Bind(R.id.collect_detail_user_reason_content_tv)
    TextView collectDetailUserReasonContentTv;
    @Bind(R.id.collect_detail_user_time_tv)
    TextView collectDetailUserTimeTv;

    @Override
    protected void init()
    {

    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.im_activity_collect_detail_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.collect_detail_user_icon_img, R.id.collect_detail_user_add_tag_ll})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            //点击头像
            case R.id.collect_detail_user_icon_img:
                break;
            //
            case R.id.collect_detail_user_add_tag_ll:
                break;
        }
    }
}