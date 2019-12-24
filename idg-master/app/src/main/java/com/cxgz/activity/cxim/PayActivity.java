package com.cxgz.activity.cxim;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.injoy.idg.R;
import com.cxgz.activity.cxim.base.BaseActivity;

/**
 * User: Selson
 * Date: 2016-06-26
 * FIXME
 */
public class PayActivity extends BaseActivity
{
    private TextView tv_pay_receive;
    private TextView tv_pay_give;

    @Override
    protected void init()
    {
//        setTitle(R.string.pay_title);

        setTitle("收付款");

        addLeftBtn(R.drawable.folder_back, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        tv_pay_receive = (TextView) findViewById(R.id.tv_pay_receive);
        tv_pay_receive.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                showDialog("收款功能即将开通。");
            }
        });

        tv_pay_give = (TextView) findViewById(R.id.tv_pay_give);

        tv_pay_give.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDialog("付款功能即将开通。");
            }
        });
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_pay_main;
    }

    /**
     * 清空所有消息的dialog
     */
    private AlertDialog.Builder mLogoutTipsDialog;

    private void showDialog(String promptString)
    {
        if (mLogoutTipsDialog == null)
        {
            mLogoutTipsDialog = new AlertDialog.Builder(this);
        }

        mLogoutTipsDialog.setMessage(promptString);

        mLogoutTipsDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                mLogoutTipsDialog.create().dismiss();
            }
        });

        mLogoutTipsDialog.create().show();
    }
}