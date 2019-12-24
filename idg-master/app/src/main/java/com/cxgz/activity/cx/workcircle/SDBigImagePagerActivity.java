/**
 * @title:BigImageActivity.java TODO包含类名列表
 * Copyright (C) oa.cn All right reserved.
 * @version：v3.0,2015年4月6日
 */
package com.cxgz.activity.cx.workcircle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.injoy.idg.R;
import com.chaoxiang.base.utils.SDLogUtil;
import com.cxgz.activity.cx.base.BaseActivity;
import com.cxgz.activity.cx.view.photoview.scrollerproxy.HackyViewPager;
import com.chaoxiang.base.config.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zjh
 * @name BigImagePagerActivity
 * @description 查看大图
 * @date 2015年4月6日
 */
public class SDBigImagePagerActivity extends BaseActivity
{
    private HackyViewPager mPager;
    private int pagerPosition;
    private TextView indicator;
    private LinearLayout ll_delete;
    private LinearLayout ll_return;
    /**
     * 被删除的图片集合
     */
//	private List<String> delImgPaths = new ArrayList<String>();
    private ArrayList<Integer> delImgPosition = new ArrayList<>();
    private List<String> bigImgPaths = new ArrayList<String>();
    private List<String> smallImgPaths = new ArrayList<String>();
    private String currentImgPath;

    public static final String DELETE_DATA = "delete_data";
    public static final String IS_NEED_DEL = "is_need_del";
    public static final String IS_HIDE_INDICATOR = "is_hide_indicator";
    private ImagePagerAdapter mAdapter;
    /**
     * 是否需要删除按钮
     */
    private boolean isNeedDel;
    private int currentSelectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init()
    {
        mPager = (HackyViewPager) findViewById(R.id.pager);
        indicator = (TextView) findViewById(R.id.tv_indicator);
        ll_delete = (LinearLayout) findViewById(R.id.ll_delete);
        ll_delete.setOnClickListener(this);
        ll_return = (LinearLayout) findViewById(R.id.ll_return);
        ll_return.setOnClickListener(this);
        mPager.setOffscreenPageLimit(1);

        isNeedDel = getIntent().getBooleanExtra(IS_NEED_DEL, true);
        if (!isNeedDel)
        {
            ll_delete.setVisibility(View.GONE);
        }

        boolean isHideIndicator = getIntent().getBooleanExtra(IS_HIDE_INDICATOR, false);
        if (isHideIndicator)
        {
            indicator.setVisibility(View.GONE);
            ll_return.setVisibility(View.GONE);
        }

        pagerPosition = getIntent().getIntExtra(Constants.EXTRA_IMAGE_INDEX, 0);
        String[] bigUrls = getIntent().getStringArrayExtra(Constants.EXTRA_BIG_IMG_URIS);
        String[] smallUrls = getIntent().getStringArrayExtra(Constants.EXTRA_SMALL_IMG_URIS);
        bigImgPaths = arrayToList(bigUrls);
        if (smallUrls != null)
        {
            smallImgPaths = arrayToList(smallUrls);
        }
        mAdapter = new ImagePagerAdapter(
                getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
        CharSequence text = getString(R.string.viewpager_indicator, 1, mPager
                .getAdapter().getCount());
        indicator.setText(text);
        currentImgPath = bigImgPaths.get(pagerPosition);
        // 更新下标
        mPager.setOnPageChangeListener(new OnPageChangeListener()
        {

            @Override
            public void onPageScrollStateChanged(int arg0)
            {

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                currentImgPath = bigImgPaths.get(position);
                CharSequence text = getString(R.string.viewpager_indicator,
                        position + 1, mPager.getAdapter().getCount());
                indicator.setText(text);
            }

        });
        mPager.setCurrentItem(pagerPosition);

//        mPager.setOnTouchListener(new View.OnTouchListener()
//        {
//            @Override
//            public boolean onTouch(View v, MotionEvent event)
//            {
//                SDLogUtil.debug("触发点击事件！");
//                return true;
//            }
//        });

        mPager.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent data = new Intent();
//		data.putExtra(DELETE_DATA, (Serializable)delImgPaths);
                SDLogUtil.syso("currentSelectedPosition=----ddddd---BackPressed--==" + currentSelectedPosition);
                data.putIntegerArrayListExtra(DELETE_DATA, delImgPosition);
                data.putExtra(Constants.EXTRA_BIG_IMG_URIS_DELETE, bigImgPaths.toArray(new String[bigImgPaths.size()]));
                setResult(RESULT_OK, data);
                finish();
                overridePendingTransition(R.anim.look_big_img_in, R.anim.look_big_img_out);
            }
        });
    }

    float xUp = 0;
    float xDown = 0;

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//        switch (event.getAction()){
//            case MotionEvent.ACTION_MOVE:
//                break;
//            case MotionEvent.ACTION_UP:
//                xUp = event.getX();
//                if (xUp == xDown) {
//
//                    Intent data = new Intent();
////		data.putExtra(DELETE_DATA, (Serializable)delImgPaths);
//                    SDLogUtil.syso("currentSelectedPosition=----ddddd---BackPressed--==" + currentSelectedPosition);
//                    data.putIntegerArrayListExtra(DELETE_DATA, delImgPosition);
//                    data.putExtra(Constants.EXTRA_BIG_IMG_URIS_DELETE, bigImgPaths.toArray(new String[bigImgPaths.size()]));
//                    setResult(RESULT_OK, data);
//                    finish();
//                    overridePendingTransition(R.anim.look_big_img_in, R.anim.look_big_img_out);
//                }
//                break;
//            case MotionEvent.ACTION_DOWN:
//                xDown = event.getX();
//                break;
//            case MotionEvent.ACTION_CANCEL:
//                break;
//        }
//        return true && super.onTouchEvent(event);
//    }


    private List<String> arrayToList(String[] smallUrls)
    {
        List<String> resList = new ArrayList<String>();
        for (String smallUrl : smallUrls)
        {
            resList.add(smallUrl);
        }
        return resList;
    }


    @Override
    protected int getContentLayout()
    {
        return R.layout.sd_tool_big_img;
    }

    private boolean lock;

    @Override
    public void onClick(View view)
    {
        super.onClick(view);
        switch (view.getId())
        {
            case R.id.ll_delete:
                //TODO 删除图片
                currentSelectedPosition = mPager.getCurrentItem();
                SDLogUtil.syso("currentImgPath===" + currentImgPath);
                SDLogUtil.syso("getCurrentItem=---------==" + mPager.getCurrentItem());

                if (currentImgPath != null)
                {
                    delImgPosition.add(currentSelectedPosition);
                    bigImgPaths.remove(mPager.getCurrentItem());
                    SDLogUtil.syso("currentSelectedPosition=---------==" + currentSelectedPosition);
                    mPager.setCurrentItem(currentSelectedPosition);
                    mAdapter.notifyDataSetChanged();
                    CharSequence text = getString(R.string.viewpager_indicator,
                            currentSelectedPosition >= mPager.getAdapter().getCount() ? currentSelectedPosition : currentSelectedPosition + 1,
                            mPager.getAdapter().getCount());
                    indicator.setText(text);
                }

                if (mAdapter.getCount() > 0)
                {
                    lock = true;
                    Intent data = new Intent();
//			data.putExtra(DELETE_DATA, (Serializable)delImgPaths);
                    data.putIntegerArrayListExtra(DELETE_DATA, delImgPosition);
                    SDLogUtil.syso("currentSelectedPosition=-------ll_return--==" + currentSelectedPosition);
                    data.putExtra(Constants.EXTRA_BIG_IMG_URIS_DELETE, bigImgPaths.toArray(new String[bigImgPaths.size()]));
                    setResult(RESULT_OK, data);
//                    finish();
                }


                if (mAdapter.getCount() <= 0)
                {
                    Intent data = new Intent();
//				data.putExtra(DELETE_DATA, (Serializable)delImgPaths);
                    data.putExtra(DELETE_DATA, (Serializable) delImgPosition);
                    data.putExtra(Constants.EXTRA_BIG_IMG_URIS_DELETE, bigImgPaths.toArray(new String[bigImgPaths.size()]));
                    SDLogUtil.syso("currentSelectedPosition=-------meiyou--==" + currentSelectedPosition);
                    setResult(RESULT_OK, data);
                    finish();
                }
                break;
            case R.id.ll_return:
                if (!lock)
                {
                    Intent data = new Intent();
//			data.putExtra(DELETE_DATA, (Serializable)delImgPaths);
                    data.putIntegerArrayListExtra(DELETE_DATA, delImgPosition);
                    SDLogUtil.syso("currentSelectedPosition=-------ll_return--==" + currentSelectedPosition);
                    data.putExtra(Constants.EXTRA_BIG_IMG_URIS_DELETE, bigImgPaths.toArray(new String[bigImgPaths.size()]));
                    setResult(RESULT_OK, data);
                }
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent data = new Intent();
//		data.putExtra(DELETE_DATA, (Serializable)delImgPaths);
        SDLogUtil.syso("currentSelectedPosition=-------BackPressed--==" + currentSelectedPosition);
        data.putIntegerArrayListExtra(DELETE_DATA, delImgPosition);
        data.putExtra(Constants.EXTRA_BIG_IMG_URIS_DELETE, bigImgPaths.toArray(new String[bigImgPaths.size()]));
        setResult(RESULT_OK, data);
        finish();
        overridePendingTransition(R.anim.look_big_img_in, R.anim.look_big_img_out);
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter
    {

        public ImagePagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object)
        {
            return POSITION_NONE;
        }

        @Override
        public int getCount()
        {
            return bigImgPaths == null ? 0 : bigImgPaths.size();
        }

        @Override
        public Fragment getItem(int position)
        {
            return getFragment(position);
        }

        private Fragment getFragment(int position)
        {
            String bigImgUri = bigImgPaths.get(position);
            String smallImgUri = null;
            if (smallImgPaths != null && !smallImgPaths.isEmpty())
            {
                smallImgUri = smallImgPaths.get(position);
            }
            SDImageDetailFragment detailFragment = new SDImageDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.EXTRA_BIG_IMG_URI, bigImgUri);
            bundle.putString(Constants.EXTRA_SMALL_IMG_URI, smallImgUri);
            detailFragment.setArguments(bundle);
            return detailFragment;
        }

        @Override
        public void finishUpdate(ViewGroup container)
        {
            SDLogUtil.syso("finishUpdate");
            super.finishUpdate(container);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        setResult(RESULT_OK,null);
        finish();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        setResult(RESULT_OK,null);
        finish();
        return true;
    }
}

