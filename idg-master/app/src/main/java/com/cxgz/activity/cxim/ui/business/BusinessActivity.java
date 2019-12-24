package com.cxgz.activity.cxim.ui.business;


import android.view.View;

import com.cxgz.activity.cxim.base.BaseFragmentActivity;
import com.injoy.idg.R;


/**
 * User: Selson
 * Date: 2016-11-17
 * FIXME  工作提交主Activity
 */
public class BusinessActivity extends BaseFragmentActivity implements View.OnClickListener
{
    private int index;

    @Override
    protected void init()
    {
        super.init();
        index = this.getIntent().getIntExtra("index", -1);
        initView();
    }

    private void initView()
    {
        if (index != -1)
            switch (index)
            {
                case 0:
                    replaceFragment(BusinessWorkSubmitFragment.newInstance(""));
                    break;
                case 1:
                    replaceFragment(BusinessBorrowMoneySubmitFragment.newInstance(""));
                    break;
                case 2:
                    replaceFragment(BusinessAchievementSubmitFragment.newInstance(""));
                    break;
                case 3:
                    replaceFragment(BusinessLeaveSubmitFragment.newInstance(""));
                    break;
                case 4:
                    replaceFragment(BusinessWorkJournalFragment.newInstance(""));
                    break;
            }

    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.erp_activity_for_fragment;
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {

        }
    }
}