package com.cxgz.activity.cxim;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.injoy.idg.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseActivity;

/**
 * @authon zjh
 * @date 2016-01-06
 * @desc 查看大图
 */
public class BigImgActivity extends BaseActivity
{
    private ImageView iv;
    public final static String EXTR_PATH = "extr_path";
    private ProgressBar pb;

    String path;

    @Override
    protected void init()
    {
        addLeftBtn(R.drawable.folder_back, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                BigImgActivity.this.finish();
            }
        });

        setTitle("图片");

        path = getIntent().getStringExtra(EXTR_PATH);

        iv = (ImageView) findViewById(R.id.iv);
        pb = (ProgressBar) findViewById(R.id.pb);

        if (StringUtils.notEmpty(path))
        {
            getData();
        } else
        {
            MyToast.showToast(this, "图片地址为空！");
            finish();
        }


    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_big_img;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    private void getData()
    {
        Glide.with(this)
                .load(path)
                .fitCenter()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<String, GlideDrawable>()
                {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource)
                    {
                        pb.setVisibility(View.GONE);
                        showToast("加载失败");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource)
                    {
                        pb.setVisibility(View.GONE);
                        return false;
                    }


                })
                .into(iv);
    }
}
