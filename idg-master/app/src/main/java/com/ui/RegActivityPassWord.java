package com.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SPUtils;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cx.base.BaseActivity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.superdata.im.constants.CxSPIMKey;
import com.ui.http.BasicDataHttpHelper;
import com.ui.utils.LoginUtils;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import tablayout.view.textview.FontEditext;
import yunjing.view.CustomNavigatorBar;

/**
 * linjp 20160605
 * 注册 页面1
 */
public class RegActivityPassWord extends BaseActivity
{
    @Bind(R.id.register_input_edt)
    FontEditext registerInputEdt;
    @Bind(R.id.register_input_edt2)
    FontEditext registerInputEdt2;
    @Bind(R.id.register_input_finish_btn)
    Button registerInputFinishBtn;
    @Bind(R.id.edt_register_name)
    FontEditext edtRegisterName;
    @Bind(R.id.title_bar)
    CustomNavigatorBar mTopBar;

    String phone = "";

    @Override
    protected void init()
    {
        ButterKnife.bind(this);
        setTitle(getResources().getString(R.string.register_set_password));
        setLeftBack(R.mipmap.icon_public_back);//返回的
        initView();
        initTop();

        Bundle bundle = getIntent().getExtras();
        phone = bundle.getString("phone");
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_reg2;
    }

    Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
    }

    private void initView()
    {
        registerInputFinishBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (StringUtils.empty(edtRegisterName.getText().toString()))
                {
                    MyToast.showToast(RegActivityPassWord.this, R.string.cn_hint_register_company_name);
                    return;
                }

                if (StringUtils.empty(registerInputEdt.getText().toString().trim())
                        && StringUtils.empty(registerInputEdt2.getText().toString().trim()))
                {
                    MyToast.showToast(RegActivityPassWord.this, R.string.register_password_null);
                    return;
                }
                if (registerInputEdt.getText().toString().length() < 6 && registerInputEdt.getText().toString().length() < 20)
                {
                    MyToast.showToast(RegActivityPassWord.this, R.string.register_password_length_error);
                    return;
                }
                if (!registerInputEdt.getText().toString().trim().equals(registerInputEdt2.getText().toString().trim()))
                {
                    MyToast.showToast(RegActivityPassWord.this, R.string.pawinputwrongtwice);
                    return;
                }

                getData("");
            }
        });
    }

    private void initTop()
    {
        mTopBar.setMidText(getResources().getString(R.string.register_set_password));
        mTopBar.setLeftTextVisible(false);
        mTopBar.setRightTextVisible(false);
        View.OnClickListener topBarListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (v == mTopBar.getLeftImageView())
                    finish();

            }
        };
        mTopBar.setLeftImageOnClickListener(topBarListener);
    }

    protected List<NameValuePair> pairs = new ArrayList<>();

    //设置密码
    private void getData(String versionNum)
    {
        final String passWord = registerInputEdt.getText().toString().trim();
        BasicDataHttpHelper.postRegister(RegActivityPassWord.this, phone, passWord, edtRegisterName.getText().toString(),
                phone, new SDRequestCallBack()
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        MyToast.showToast(RegActivityPassWord.this, msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        //RegCompanyResp info = (RegCompanyResp) responseInfo.result;
                        SPUtils.put(getApplication(), CxSPIMKey.IS_REGISTER, false);
                        /*注册成功之后直接跳转到  主界面也就是说要实现一次隐式的登陆*/
                        String account = phone;
                        String pwd = passWord;
                         /*登陆的逻辑*/
                        LoginUtils.login(RegActivityPassWord.this, mHttpHelper, userDao, account, pwd, "", false);
                    }
                });
    }
}
