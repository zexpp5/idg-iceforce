package com.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chaoxiang.base.utils.AESUtils;
import com.chaoxiang.base.utils.BackUtils;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cx.base.BaseActivity;
import com.http.HttpURLUtil;
import com.injoy.idg.R;
import com.ui.utils.LoginUtils;
import com.utils.DialogImUtils;
import com.utils.SPUtils;

import javax.crypto.Cipher;

import newProject.company.invested_project.ActivityQxb;
import newProject.view.DialogTextFilter;

import static com.injoy.idg.R.id.imageView;
import static com.injoy.idg.R.id.view;
import static com.utils.SPUtils.AES_PWD;
import static com.utils.SPUtils.USER_ACCOUNT;

/**
 * 登录界面
 *
 * @author selson
 */
public class SDLoginActivity extends BaseActivity
{
    /**
     * 账号输入框
     */
    private EditText et_account;
    /**
     * 密码登入框
     */
    private EditText et_pwd;


    /**
     * 登录按钮
     */
    private Button ll_login;
    /**
     * 注册按钮
     */
    private TextView tv_register;
    /**
     * 记住密码按钮
     */
    private ImageView iv_remember_pwd;

    /**
     * 用户是否发生改变
     */
    private boolean autoLogin = false;
    /**
     * 是否需要记住密码
     */
    private boolean isNeedRememberPwd;

    private String seed;

    private TextView tv_forget_pwd;

    private String beforeAccount;

    private LinearLayout ll_logo, ll_login_info;

    @Override
    protected void init()
    {
        if (Build.VERSION.SDK_INT >= 21)
        {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT); //也可以设置成灰色透明的，比较符合Material Design的风格
        }

        ll_logo = (LinearLayout) findViewById(R.id.ll_logo);
        ll_login_info = (LinearLayout) findViewById(R.id.ll_login_info);

        et_account = (EditText) findViewById(R.id.et_account);
        et_account.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                SDLogUtil.debug("before=" + charSequence.toString());
                beforeAccount = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                SDLogUtil.debug("Changed=" + charSequence.toString());
                if (!"".equals(beforeAccount))
                {
                    if (!"".equals(et_pwd.getText().toString()))
                    {
                        et_pwd.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                if (editable.toString().equals(""))
                {
                    setEmpty();
                }
            }
        });
        et_pwd = (EditText) findViewById(R.id.et_pwd);

        ll_login = (Button) findViewById(R.id.ll_login);
        ll_login.setOnClickListener(this);

        tv_register = (TextView) findViewById(R.id.tv_register);
        tv_register.setOnClickListener(this);

        iv_remember_pwd = (ImageView) findViewById(R.id.iv_remember_pwd);
        iv_remember_pwd.setOnClickListener(this);

        String userName = SPUtils.get(this, USER_ACCOUNT, "").toString();
        et_account.setText(userName);

        isNeedRememberPwd = (boolean) SPUtils.get(this, SPUtils.IS_REMEMBER_PWD, true);
        isNeedRememberPwd = true;
        if (isNeedRememberPwd)
        {
            iv_remember_pwd.setSelected(true);
            seed = (String) SPUtils.get(this, SPUtils.AES_SEED, "");
            if (StringUtils.notEmpty((String) SPUtils.get(this, AES_PWD, "")))
            {
                String pwd = AESUtils.des(seed, (String) SPUtils.get(this, AES_PWD, ""), Cipher.DECRYPT_MODE);
                et_pwd.setText(pwd);
            } else
            {
                et_pwd.setText("");
            }
        } else
        {
            iv_remember_pwd.setSelected(false);
        }

        tv_forget_pwd = (TextView) findViewById(R.id.tv_forget_pwd);
        tv_forget_pwd.setOnClickListener(SDLoginActivity.this);

        et_account.setSelection(et_account.length());
        et_pwd.setSelection(et_pwd.length());
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        setAnimate1(ll_logo, -388.0f);
        ll_login_info.setVisibility(View.VISIBLE);
        setAnimate2(ll_login_info, -320.0f);
    }

    private void setAnimate1(View view, float toY)
    {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationY", 0f, toY);
        objectAnimator.setDuration(1000);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.start();
    }

    private void setAnimate2(View view, float toY)
    {
        ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(view, "translationY", 0f, toY);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimatorY, objectAnimator);
        animatorSet.setDuration(1000);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.start();

    }

    private void setEmpty()
    {
        SPUtils.put(SDLoginActivity.this, SPUtils.IS_AUTO_LOGIN, false);
        SPUtils.put(SDLoginActivity.this, SPUtils.USER_ACCOUNT, "");
        SPUtils.put(SDLoginActivity.this, SPUtils.AES_PWD, "");
    }

    @Override
    public void onBackPressed()
    {
        BackUtils.isBack(SDLoginActivity.this);
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.erp_activity_login;
    }

    @Override
    protected boolean beforeOnCreate()
    {
        return super.beforeOnCreate();
    }

    @Override
    public void onClick(View view)
    {
        HttpURLUtil.setIsTrueRegister(true);//设置为体验
        switch (view.getId())
        {
            case R.id.ll_login:

                String account = et_account.getText().toString();
                String pwd = et_pwd.getText().toString();

                if (StringUtils.isEmpty(account) && StringUtils.isEmpty(pwd))
                {
                    MyToast.showToast(SDLoginActivity.this, "请输入账号密码！");
                    return;
                }

                if (pwd.length()<8)
                {
                   //弹窗
                    initPop(account,pwd);
                }else {

                    LoginUtils.login(SDLoginActivity.this, mHttpHelper, userDao, account, pwd, "", false);
                }
                break;
            case R.id.tv_register:
                Intent intent1 = new Intent(this, RegActivity1.class);
                intent1.putExtra("is_register", true);
                startActivity(intent1);
                break;
            case R.id.iv_remember_pwd:
                if (iv_remember_pwd.isSelected())
                {
                    isNeedRememberPwd = false;
                    iv_remember_pwd.setSelected(false);
                } else
                {
                    isNeedRememberPwd = true;
                    iv_remember_pwd.setSelected(true);
                }
                break;
            case R.id.tv_forget_pwd:
                Intent forgetIntent = new Intent(this, ForgetActivity.class);
                startActivity(forgetIntent);
                break;
        }
    }

    private void initPop(final String account,final String pwd)
    {
        DialogTextFilter dialogTextFilter = new DialogTextFilter();
        dialogTextFilter.setTitleString("提 示");
        dialogTextFilter.setYesString("确定");
        dialogTextFilter.setHaveBtn(1);
        dialogTextFilter.setContentString("您的密码小于8位，建议修改！");

        DialogImUtils.getInstance().showCommonDialog(SDLoginActivity.this, dialogTextFilter, new
                DialogImUtils.OnYesOrNoListener()
                {
                    @Override
                    public void onYes()
                    {//这里就是登陆的方法。
                        LoginUtils.login(SDLoginActivity.this, mHttpHelper, userDao, account, pwd, "", false);
                    }

                    @Override
                    public void onNo()
                    {

                    }
                });


    }

}
