package com.ui.activity.guide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chaoxiang.base.utils.StringUtils;
import com.injoy.idg.R;

import yunjing.utils.DisplayUtil;

public class GuideFragment extends Fragment
{
    private static final String RES_ID = "res_id";
    private int mResId;

    ImageView im_cx_logo;

    public GuideFragment()
    {
        // Required empty public constructor
    }

    public static GuideFragment newInstance(int resId)
    {
        GuideFragment fragment = new GuideFragment();
        Bundle args = new Bundle();
        args.putInt(RES_ID, resId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mResId = getArguments().getInt(RES_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(mResId, container, false);
//        im_cx_logo = (ImageView) view.findViewById(R.id.im_cx_logo);

//        if (DisplayUtil.getYD(getActivity()))
//        {
//            String imageUrl = DisplayUtil.getCustomizeInfo(getActivity(), 4);
//            if (StringUtils.notEmpty(imageUrl))
//            {
//        Glide.with(getActivity())
//                .load(R.mipmap.pic_guide)
//                .fitCenter()
//                .placeholder(R.mipmap.pic_guide)
//                .error(R.mipmap.pic_guide)
//                .crossFade()
//                .into(im_cx_logo);
//            }
//        } else
//        {
//            view = inflater.inflate(R.layout.fragment_guide_0213, container, false);
//        }

        return view;
    }

    private void initData()
    {

    }
}
