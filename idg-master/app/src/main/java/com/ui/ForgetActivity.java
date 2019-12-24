package com.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bean.CheckCodeResp;
import com.bean.CheckMoblieCode;
import com.bean.GetCodeResp;
import com.bean.IshavaPhone;
import com.bean.SendMessageResp;
import com.chaoxiang.base.config.Config;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cx.base.BaseActivity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.ui.http.BasicDataHttpHelper;
import com.utils.SDToast;
import com.view.TimeButton;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

import yunjing.view.CustomNavigatorBar;

/**
 * linjp 20160606
 * 忘记密码
 */
public class ForgetActivity extends BaseActivity
{
    protected EditText etMobilePhone;
    protected EditText etCode;
    protected ImageView ivShowCode;
    protected EditText etMesgCode;
    protected TimeButton btnGetValidateCode;
    protected Button btnNext;
    protected CustomNavigatorBar mTopBar;
    View mDecorView;


    protected LinearLayout llRight;

    @Override
    protected void init()
    {
        setTitle("忘记密码");
        setLeftBack(R.mipmap.icon_public_back);//返回的

        mDecorView = getWindow().getDecorView();
        initView();
        initTop();
        llRight = (LinearLayout) findViewById(R.id.ll_bottom_page_left);
        llRight.setVisibility(View.INVISIBLE);
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_forget_password;
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
        etMobilePhone = (EditText) findViewById(R.id.etMobilePhone);
        etCode = (EditText) findViewById(R.id.etCode);
        ivShowCode = (ImageView) findViewById(R.id.ivShowCode);
        etMesgCode = (EditText) findViewById(R.id.etMesgCode);
        mTopBar = (CustomNavigatorBar) findViewById(R.id.title_bar);

        btnNext = (Button) findViewById(R.id.btnNext);

        btnGetValidateCode = (TimeButton) findViewById(R.id.btnGetValidateCode);
        btnGetValidateCode.onCreate(savedInstanceState);
        btnGetValidateCode.setTextAfter("秒").setTextBefore("获取验证码").setLenght(100 * 1000);

        ivShowCode.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getCode();

            }
        });

        etCode.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (hasFocus)
                {
                    getCode();
                }
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (checkInput())
                {
                    checkMoblieCode();
                }
            }
        });
        btnGetValidateCode.setEnabled(false);
        etMobilePhone.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (etMobilePhone.getText().toString().trim().length() == 11)
                {
                    if (StringUtils.isMobileNO(etMobilePhone.getText().toString().trim()))
                    {
                        ishavaPhone();
                        if (etCode.getText().toString().trim().length() >= 4)
                        {
                            btnGetValidateCode.setEnabled(true);
                            btnGetValidateCode.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    if (checkInput2())
                                    {
                                        checkCode();

                                    } else
                                    {
                                        btnGetValidateCode.setEnabled(false);
                                    }

                                }
                            });
                        } else
                        {

                            btnGetValidateCode.setEnabled(false);
                        }
                    } else
                    {

                        btnGetValidateCode.setEnabled(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
        etCode.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (etMobilePhone.getText().toString().trim().length() == 11)
                {
                    if (StringUtils.isMobileNO(etMobilePhone.getText().toString().trim()))
                    {

                        if (etCode.getText().toString().trim().length() >= 4)
                        {
                            btnGetValidateCode.setEnabled(true);
                            btnGetValidateCode.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    if (checkInput2())
                                    {
                                        checkCode();
                                    } else
                                    {
                                        btnGetValidateCode.setEnabled(false);
                                    }

                                }
                            });
                        } else
                        {

                            btnGetValidateCode.setEnabled(false);
                        }
                    } else
                    {

                        btnGetValidateCode.setEnabled(false);
                    }
                } else
                {

                    btnGetValidateCode.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
    }


    private void initTop()
    {
        mTopBar.setMidText("忘记密码");
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


    private boolean checkInput()
    {

        if (TextUtils.isEmpty(etMobilePhone.getText().toString().trim()))
        {
            SDToast.showLong("请输入手机号！");
            return false;
        }

        if (!StringUtils.isMobileNO(etMobilePhone.getText().toString().trim()))
        {
            SDToast.showLong("请入合法的手机号！");
            return false;
        }
        if (TextUtils.isEmpty(etMesgCode.getText().toString().trim()))
        {
            SDToast.showLong("请输入短信验证码！");
            return false;
        }

        return true;
    }

    private boolean checkInput2()
    {

        if (TextUtils.isEmpty(etMobilePhone.getText().toString().trim()))
        {
            SDToast.showLong("请输入手机号！");
            return false;
        }

        if (!StringUtils.isMobileNO(etMobilePhone.getText().toString().trim()))
        {
            SDToast.showLong("请入合法的手机号！");
            return false;
        }
        if (TextUtils.isEmpty(etCode.getText().toString().trim()))
        {
            SDToast.showLong("请输入图形验证码！");
            return false;
        }

        return true;
    }

    protected List<NameValuePair> pairs = new ArrayList<>();

    /**
     * status
     * 响应状态码	int	200=成功，500=服务器错误
     * code	验证码	String	有效时间1分钟
     */
    GetCodeResp getCodeResp;

    /**
     * 获取图形验证码
     */
    private void getCode()
    {
        if (TextUtils.isEmpty(etMobilePhone.getText().toString().trim()))
            SDToast.showShort("请输入手机号");

        if (!StringUtils.isMobileNO(etMobilePhone.getText().toString().trim()))
        {
            SDToast.showShort("请入合法的手机号");
        }

        BasicDataHttpHelper.getGetCode(ForgetActivity.this, etMobilePhone.getText().toString().trim(),
                new SDRequestCallBack(GetCodeResp.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        MyToast.showToast(ForgetActivity.this, msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        getCodeResp = (GetCodeResp) responseInfo.result;
                        if (getCodeResp.getStatus() == 200)
                        {
                            if (getCodeResp.getData().getCode() == null || getCodeResp.getData().getCode().equals("null"))
                            {
                                SDToast.showShort("获取图形验证码失败~！");
                                ivShowCode.setImageBitmap(GenerateCodeUtils.getInstance().createBitmap2("点击重试"));
                            } else
                            {
                                ivShowCode.setImageBitmap(GenerateCodeUtils.getInstance().createBitmap2(getCodeResp.getData()
                                        .getCode()));
                            }
                        } else
                        {
                            SDToast.showShort("请稍候再试");
                        }
                    }
                });
    }

    /**
     * status
     * 响应状态码	int	200=验证成功，500=服务器错误，501= 验证码过期
     */
    CheckCodeResp checkCodeResp;

    /**
     * 检测验证码
     */
    private void checkCode()
    {
        if (TextUtils.isEmpty(etCode.getText().toString().trim()))
            SDToast.showShort("请输入图形验证码");

        BasicDataHttpHelper.postCheckCode(ForgetActivity.this,
                getCodeResp.getData().getCodeKey()
                , etCode.getText().toString().trim(), new SDRequestCallBack(CheckCodeResp.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        MyToast.showToast(ForgetActivity.this, "图形码错误");
                        cancelTime();
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        checkCodeResp = (CheckCodeResp) responseInfo.result;

                        if (checkCodeResp.getStatus().equals("200"))
                        {
                            sendMessage();
                        } else
                        {
                            SDToast.showShort(checkCodeResp.getMsg() + "");
                        }
                    }
                });
    }

    SendMessageResp sendMessageResp;

    /**
     * 发送短信验证码
     */
    protected void sendMessage()
    {
        if (TextUtils.isEmpty(etMobilePhone.getText().toString().trim()))
            SDToast.showShort("请输入手机号");

        if (!StringUtils.isMobileNO(etMobilePhone.getText().toString().trim()))
        {
            SDToast.showLong("请入合法的手机号");
        }

        BasicDataHttpHelper.postSendMessage(ForgetActivity.this, etMobilePhone.getText().toString().trim()
                , new SDRequestCallBack(SendMessageResp.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        MyToast.showToast(ForgetActivity.this, msg);
                        cancelTime();
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        sendMessageResp = (SendMessageResp) responseInfo.result;

                        if (sendMessageResp.getStatus().equals("200"))
                        {
                            SDToast.showShort("验证码获取成功");
                            if (Config.IS_SHOW_CODE)
                                MyToast.showToast(ForgetActivity.this, "测试：" + sendMessageResp.getData().getMessageCode());

                        } else
                        {
                            SDToast.showShort(sendMessageResp.getMsg() + "");
                        }

                    }
                });
    }

    private void cancelTime()
    {
        btnGetValidateCode.clearTimer();
        btnGetValidateCode.setEnabled(true);
        btnGetValidateCode.setTextAfter("秒").setTextBefore("获取验证码").setLenght(100 * 1000);
    }


    CheckMoblieCode checkMoblieCode;

    /**
     * 检测短信验证码
     */
    private void checkMoblieCode()
    {
        BasicDataHttpHelper.postCheckMessage(ForgetActivity.this, etMobilePhone.getText().toString().trim()
                , etMesgCode.getText().toString().trim(), new SDRequestCallBack(CheckMoblieCode.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        MyToast.showToast(ForgetActivity.this, msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        checkMoblieCode = (CheckMoblieCode) responseInfo.result;

                        if (checkMoblieCode.getStatus().equals("200"))
                        {
                            Bundle bundle = new Bundle();
                            bundle.putString("phone", etMobilePhone.getText().toString());
                            bundle.putBoolean("isForget", true);
                            openActivity(UpdatePasswordActivity.class, bundle);
                            finish();
                        } else
                        {
                            SDToast.showShort(checkMoblieCode.getMsg() + "");
                        }

                    }
                });
    }

    /**
     * 参数名	名称	类型	是否必需	备注
     phone	手机号码	String 	是
     */
    /**
     * status
     * 响应状态码	int	201=手机号码已经注册，202=手机号码没有注册
     */
    IshavaPhone ishavaPhone;

    /**
     * 是否注册过的手机号
     */
    private void ishavaPhone()
    {
        if (TextUtils.isEmpty(etMobilePhone.getText().toString().trim()))
            SDToast.showShort("请输入手机号");

        if (!StringUtils.isMobileNO(etMobilePhone.getText().toString().trim()))
        {
            SDToast.showShort("请入合法的手机号");
        }

        BasicDataHttpHelper.postIsHavaPhone(ForgetActivity.this, etMobilePhone.getText().toString().trim(), new
                SDRequestCallBack(IshavaPhone.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
//                MyToast.showToast(ForgetActivity.this, msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        ishavaPhone = (IshavaPhone) responseInfo.result;
                        if (ishavaPhone.getStatus().equals(isExsit))
                        {
                            getCode();
                        } else if (ishavaPhone.getStatus().equals(isUnExsit))
                        {
                            SDToast.showShort("手机号码没有注册过");
                        } else
                        {
                            SDToast.showShort(ishavaPhone.getMsg());
                        }
                    }
                });
    }

    public static final String isExsit = "201";
    public static final String isUnExsit = "202";

}
