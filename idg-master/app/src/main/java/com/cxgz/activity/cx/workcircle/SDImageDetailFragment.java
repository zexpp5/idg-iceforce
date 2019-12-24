package com.cxgz.activity.cx.workcircle;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.chaoxiang.base.utils.StringUtils;
import com.injoy.idg.R;
import com.chaoxiang.base.utils.SDLogUtil;
import com.cxgz.activity.cx.view.photodraweeview.PhotoDraweeView;
import com.chaoxiang.base.config.Constants;

import static com.injoy.idg.R.id.pb_loading;

public class SDImageDetailFragment extends Fragment
{
    private PhotoDraweeView mImageView;
    private ProgressBar progressBar;
    private String smallUri;
    private String bigUri;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        smallUri = bundle.getString(Constants.EXTRA_SMALL_IMG_URI);
        bigUri = bundle.getString(Constants.EXTRA_BIG_IMG_URI);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View v = inflater.inflate(R.layout.sd_tool_big_img_detail, container, false);
        mImageView = (PhotoDraweeView) v.findViewById(R.id.iv);
        progressBar = (ProgressBar) v.findViewById(pb_loading);
        progressBar.setVisibility(View.GONE);

        if (StringUtils.notEmpty(bigUri))
        {
            if (bigUri.equals("icon"))
            {
                Glide.with(getActivity())
                        .load(R.mipmap.temp_user_head)
                        .crossFade()
                        .into(mImageView);
            } else
            {
                Glide.with(getActivity())
                        .load(bigUri)
                        .crossFade()
                        .into(mImageView);
            }

        }

//        SDImgHelper.getInstance(getActivity()).loadBigImage(getActivity(), bigUri, smallUri, mImageView, progressBar);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        SDLogUtil.debug("fragmentState=onDestroy");
    }

    @Override
    public void onPause()
    {
        super.onPause();
        SDLogUtil.debug("fragmentState=onPause");
    }

    @Override
    public void onResume()
    {
        super.onResume();
        SDLogUtil.debug("fragmentState=onResume");
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        SDLogUtil.debug("fragmentState=onDestroyView");
    }

    @Override
    public void onStart()
    {
        super.onStart();
        SDLogUtil.debug("fragmentState=onStart");
    }

    @Override
    public void onStop()
    {
        super.onStop();
        SDLogUtil.debug("fragmentState=onStop");
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        SDLogUtil.debug("fragmentState=onAttach");
    }
}
