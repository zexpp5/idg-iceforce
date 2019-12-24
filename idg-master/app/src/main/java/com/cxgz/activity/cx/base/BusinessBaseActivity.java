package com.cxgz.activity.cx.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.injoy.idg.R;


/**
 * @author zjh
 */
public class BusinessBaseActivity extends AppCompatActivity
{
    public static final String SHARE_TYPE = "share_type";
    public static final String BID = "bid";
    protected int shareType;
    protected long bid;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        shareType = getIntent().getIntExtra(SHARE_TYPE, 0);
        bid = getIntent().getLongExtra(BID, -1);
    }

    // Press the back button in mobile phone
    @Override
    public void onBackPressed()
    {
        overridePendingTransition(R.anim.slide_right_in,
                R.anim.slide_right_out);
        super.onBackPressed();
    }

}
