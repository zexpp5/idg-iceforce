package com.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.base.BaseApplication;
import com.chaoxiang.base.utils.MyToast;
import com.bean.RegCompanyResp;
import com.chaoxiang.base.utils.MD5Util;
import com.cxgz.activity.cx.base.BaseActivity;
import com.http.HttpURLUtil;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.superdata.im.constants.CxSPIMKey;
import com.ui.http.BasicDataHttpHelper;
import com.utils.SDToast;
import com.utils.SPUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;


/**
 * 设置密码
 */
public class UpdatePasswordActivity extends BaseActivity
{
    private EditText etNewPassword;
    private EditText etSureNewPassword;

    private Button btnCompleteUpdate;
    private String phone = "";
    private boolean isForget = true;
    //隐藏叮当响
    protected LinearLayout llRight;

    @Override
    protected void init()
    {

        initView();

    }

    private void initView()
    {
        llRight = (LinearLayout) findViewById(R.id.ll_bottom_page_left);//叮当响btn
        etNewPassword = (EditText) findViewById(R.id.etNewPassword);
        etSureNewPassword = (EditText) findViewById(R.id.etSureNewPassword);
        btnCompleteUpdate = (Button) findViewById(R.id.btnCompleteUpdate);
        Bundle bundle = getIntent().getExtras();
        phone = bundle.getString("phone");
        isForget = bundle.getBoolean("isForget");

        btnCompleteUpdate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (checkInput())
                {
                    if (isForget)
                        forgetPass();
                    else
                        postModifyPassword();
                }
            }
        });

        if (isForget)
            setTitle("忘记密码");
        else
            setTitle("修改密码");
        setLeftBack(R.mipmap.icon_back);//返回的

        //隐藏叮当响
        llRight.setVisibility(View.INVISIBLE);
    }

    private void postModifyPassword()
    {
        String tmpPaasWordString = MD5Util.MD5(MD5Util.MD5(etNewPassword.getText().toString().trim()));
        BasicDataHttpHelper.postModifyPersonalPassword(UpdatePasswordActivity.this, tmpPaasWordString, new SDRequestCallBack()
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        MyToast.showToast(UpdatePasswordActivity.this, msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        MyToast.showToast(UpdatePasswordActivity.this, "密码修改成功，稍后请重新登录");
                        SPUtils.put(UpdatePasswordActivity.this, SPUtils.AES_PWD, "");
                        logout();
                    }
                }
        );
    }

    private void logout()
    {
        com.chaoxiang.base.utils.SPUtils.put(UpdatePasswordActivity.this, CxSPIMKey.IS_LOGIN, false);
        BaseApplication.getInstance().logout();
        startActivity(new Intent(UpdatePasswordActivity.this, SDLoginActivity.class));
        UpdatePasswordActivity.this.finish();
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_update_password;
    }

    protected List<NameValuePair> pairs = new ArrayList<>();

    protected void forgetPass()
    {
        String url = HttpURLUtil.newInstance().append("sysuser/forgetPass")
                .toString();
        pairs.clear();

        if (!TextUtils.isEmpty(phone))
        {
            pairs.add(new BasicNameValuePair("account", phone));
        }

        if (!TextUtils.isEmpty(etNewPassword.getText().toString().trim()))
        {
            String passWord = etNewPassword.getText().toString().trim();
            passWord = MD5Util.MD5(MD5Util.MD5(passWord));
            pairs.add(new BasicNameValuePair("password", passWord));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, new SDRequestCallBack(RegCompanyResp.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(UpdatePasswordActivity.this, "密码设置失败，请稍候再试");
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                RegCompanyResp info = (RegCompanyResp) responseInfo.result;
                if (info.getStatus().equals("200"))
                {
                    MyToast.showToast(UpdatePasswordActivity.this, "密码修改成功，稍后请重新登录");
                    SPUtils.put(UpdatePasswordActivity.this, SPUtils.AES_PWD, "");
                    logout();
                } else
                {
                    SDToast.showShort(info.getMsg());
                }

            }
        });
    }

    private boolean checkInput()
    {
        if (TextUtils.isEmpty(etNewPassword.getText().toString().trim()))
        {
            SDToast.showLong("请输入密码！");
            return false;
        }
        if (etNewPassword.getText().toString().trim().length() < 8)
        {
            SDToast.showLong("密码长度不能小于8位！");
            return false;
        }
        if (TextUtils.isEmpty(etSureNewPassword.getText().toString().trim()))
        {
            SDToast.showLong("请再次输入密码！");
            return false;
        }

        if (!etNewPassword.getText().toString().trim().equals(etSureNewPassword.getText().toString().trim()))
        {
            SDToast.showLong("两次密码不一致！");
            return false;
        }

        return true;
    }


}
