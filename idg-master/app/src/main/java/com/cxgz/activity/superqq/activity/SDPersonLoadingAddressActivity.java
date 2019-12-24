package com.cxgz.activity.superqq.activity;

import android.widget.ImageView;

import com.injoy.idg.R;
import com.cxgz.activity.cx.base.BaseActivity;

public class SDPersonLoadingAddressActivity extends BaseActivity
{
    private ImageView iv_loadingaddress;

    @Override
    protected void init()
    {
        setLeftBack(R.drawable.folder_back);
        iv_loadingaddress = (ImageView) findViewById(R.id.iv_loadingaddress);
        setTitle("扫码下载客户端");
        iv_loadingaddress.setImageResource(R.mipmap.logo_download);
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.sd_loading_address;
    }


}
