//package com.cxgz.activity.cxim.ui.utils;
//
//import android.view.View;
//
//import com.base.BaseFragmentActivity;
//import com.injoy.ddx.R;
//import com.ui.erp.base_data.work_office.fragment.ERPWorkOfficeSubmitFragment;
//
///**
// * User: Selson
// * Date: 2016-11-05
// * FIXME
// */
//public class TestOnlyFgActivity extends BaseFragmentActivity implements View.OnClickListener
//{
//    @Override
//    protected void init()
//    {
//        super.init();
//
//        setTitle(getResources().getString(R.string.basic_work_office_title));
//        setLeftBack(R.mipmap.back_back);//返回的
//
//        initView();
//    }
//
//    private void initView()
//    {
//        replaceSelect(0);
//        replaceFragment(ERPWorkOfficeSubmitFragment.newInstance(""));
//    }
//
//    @Override
//    protected int getContentLayout()
//    {
//        return R.layout.erp_activity_basic_work_office_main;
//    }
//
//
//    @Override
//    public void onClick(View v)
//    {
//        switch (v.getId())
//        {
//
//        }
//    }
//}