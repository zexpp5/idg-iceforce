package com.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bean.CheckCodeResp;
import com.bean.CheckMoblieCode;
import com.bean.GetCodeResp;
import com.bean.IshavaPhone;
import com.bean.SendMessageResp;
import com.chaoxiang.base.config.Config;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SPUtils;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cx.base.BaseActivity;
import com.entity.gztutils.ZTUtils;
import com.http.HttpURLUtil;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.superdata.im.constants.CxSPIMKey;
import com.ui.http.BasicDataHttpHelper;
import com.ui.utils.CheckMoblieTyBean;
import com.ui.utils.LoginUtils;
import com.utils.SDToast;
import com.view.TimeButton;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import tablayout.view.textview.FontTextView;
import yunjing.view.CustomNavigatorBar;

/**
 * linjp 20160605
 * 注册 页面1
 */
public class RegActivity1 extends BaseActivity
{
    protected EditText etMobilePhone;
    protected EditText etCode;
    protected EditText etMesgCode;
    protected TimeButton btnGetValidateCode;
    protected ImageView ivShowCode;
    protected Button btnNext;
    View mDecorView;

    public static final String isExsit = "502";
    public static final String isUnExsit = "501";
    protected LinearLayout llRight, login_and_ty;
    private boolean isRegister = false;
    private FontTextView tv_ty_login;
    private CustomNavigatorBar mTopBar;

    @Override
    protected void init()
    {
        isRegister = getIntent().getBooleanExtra("is_register", true);
        setLeftBack(R.mipmap.icon_public_back);//返回的

        mDecorView = getWindow().getDecorView();
        initView();
        if (isRegister)
        {
            setTitle(getResources().getString(R.string.register_title));
            initTop(getResources().getString(R.string.register_title));
        } else
        {//体验
            setTitle(getResources().getString(R.string.phone_register_title));
            initTop(getResources().getString(R.string.phone_register_title));
        }
        llRight = (LinearLayout) findViewById(R.id.ll_bottom_page_left);
        llRight.setVisibility(View.INVISIBLE);
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_reg1;
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
        TextView tv_login = (TextView) findViewById(R.id.tv_login);
        login_and_ty = (LinearLayout) findViewById(R.id.login_and_ty);//
        tv_ty_login = (FontTextView) findViewById(R.id.tv_ty_login);
        mTopBar = (CustomNavigatorBar) findViewById(R.id.title_bar);
        tv_login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SPUtils.put(getApplication(), CxSPIMKey.IS_REGISTER, false);
                Intent intent = new Intent(RegActivity1.this, SDLoginActivity.class);
                startActivity(intent);
            }
        });

        tv_ty_login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                HttpURLUtil.setIsTrueRegister(false);//设置为体验
                Intent intentTy = new Intent(RegActivity1.this, RegActivity1.class);
                intentTy.putExtra("is_register", false);
                startActivity(intentTy);
            }
        });

        if (isRegister)
        {
//            login_and_ty.setVisibility(View.VISIBLE);//不要进入体验跟已有帐号
        } else
        {//体验
            login_and_ty.setVisibility(View.GONE);
        }

        llRight = (LinearLayout) findViewById(R.id.ll_bottom_page_left);
        etMobilePhone = (EditText) findViewById(R.id.etMobilePhone);
        etMesgCode = (EditText) findViewById(R.id.etMesgCode);
        etCode = (EditText) findViewById(R.id.etCode);

        ivShowCode = (ImageView) findViewById(R.id.ivShowCode);
        btnNext = (Button) findViewById(R.id.btnNext);

        if (isRegister)
        {
            btnNext.setText(R.string.register);
        } else
        {//体验
            btnNext.setText(R.string.phone_register_ty);
        }

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

        btnNext.setEnabled(false);
        btnNext.setTextColor(Color.WHITE);
        btnNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (checkInput())
                {
                    if (isRegister)
                    {
                        checkMoblieCode();
                    } else
                    {
                        //体验
//                        showShareDialog();
                    }

                }
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

        btnGetValidateCode.setEnabled(false);
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
                    cancelTime();
                }
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
                btnGetValidateCode.setEnabled(false);
                if (etMobilePhone.getText().toString().trim().length() == 11)
                {
                    if (StringUtils.isMobileNO(etMobilePhone.getText().toString().trim()))
                    {
                        if (etCode.getText().toString().trim().length() >= 4)
                        {
                            btnGetValidateCode.setEnabled(true);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

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
                        if (isRegister)
                            ishavaPhone();
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
    }

    private void initTop(String titleStr)
    {
        mTopBar.setMidText(titleStr);
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

    private boolean checkInput2()
    {
        /*验证所有需要的信息都不为空*/
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

        if (etCode.getText().toString().length() < 4)
        {
            SDToast.showLong("请输入图形验证码！");
            return false;
        }

        return true;
    }

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
        BasicDataHttpHelper.getGetCode(RegActivity1.this, etMobilePhone.getText().toString().trim(),
                new SDRequestCallBack(GetCodeResp.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        MyToast.showToast(RegActivity1.this, msg);
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
                                // sendMessage();
                            }
                        } else
                        {
                            SDToast.showShort("请稍候再试~！");
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
            SDToast.showShort("请输入图形验证码！");

        BasicDataHttpHelper.postCheckCode(RegActivity1.this, getCodeResp.getData().getCodeKey(),
                etCode.getText().toString().trim(), new SDRequestCallBack(CheckCodeResp.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        if (error.getExceptionCode() == 508)
                        {
                            SDToast.showShort("图形码错误");
                        } else
                        {
                            SDToast.showShort(msg);
                        }
                        cancelTime();
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        checkCodeResp = (CheckCodeResp) responseInfo.result;
                        sendMessage();
                    }
                });
    }

    private boolean checkInput()
    {
        if (etMobilePhone.getText().toString().length() != 11)
        {
            SDToast.showLong("请输入正确的手机号！");
            return false;
        }

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

        if (TextUtils.isEmpty(etMesgCode.getText().toString().trim()))
        {
            SDToast.showLong("请输入短信验证码！");
            return false;
        }

        return true;
    }

    protected List<NameValuePair> pairs = new ArrayList<>();

    SendMessageResp sendMessageResp;

    //获取短信验证码
    protected void sendMessage()
    {
        btnGetValidateCode.setEnabled(false);

        if (TextUtils.isEmpty(etMobilePhone.getText().toString().trim()))
        {
            SDToast.showShort("请输入手机号");
        }

        if (!StringUtils.isMobileNO(etMobilePhone.getText().toString().trim()))
        {
            SDToast.showLong("请入合法的手机号！");
        }

        BasicDataHttpHelper.postSendMessage(RegActivity1.this, etMobilePhone.getText().toString().trim(),
                new SDRequestCallBack(SendMessageResp.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        MyToast.showToast(RegActivity1.this, msg);
                        cancelTime();
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        btnNext.setEnabled(true);
                        sendMessageResp = (SendMessageResp) responseInfo.result;
                        if (sendMessageResp.getStatus().equals("200"))
                        {
                            /**
                             * 暂时弹出验证码
                             */
                            MyToast.showToast(RegActivity1.this, getResources().getString(R.string.register_get_message_success));
                            if (Config.IS_SHOW_CODE)
                                MyToast.showToast(RegActivity1.this, "测试：" + sendMessageResp.getData().getMessageCode());
                        } else
                        {
                            MyToast.showToast(RegActivity1.this, sendMessageResp.getMsg());
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


    /**
     * 参数名	名称	类型	备注
     * status
     * 响应状态码	int	200=校验成功，500=服务器错误，501=验证码过期
     */
    CheckMoblieCode checkMoblieCode;

    /**
     * 检测短信验证码
     */
    private void checkMoblieCode()
    {
        BasicDataHttpHelper.postCheckMessage(RegActivity1.this, etMobilePhone.getText().toString().trim(),
                etMesgCode.getText().toString().trim(), new SDRequestCallBack(CheckMoblieCode.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        MyToast.showToast(RegActivity1.this, msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        checkMoblieCode = (CheckMoblieCode) responseInfo.result;
                        if (checkMoblieCode.getStatus().equals("200"))
                        {
                            if (isRegister)
                            {
                                Bundle bundle = new Bundle();
                                bundle.putString("phone", etMobilePhone.getText().toString().trim());
                                openActivity(RegActivityPassWord.class, bundle);
                            } else
                            {//体验

                            }

                        } else
                        {
                            MyToast.showToast(RegActivity1.this, checkMoblieCode.getMsg());
                        }
                    }
                });
    }

    /**
     * status
     * 响应状态码	int	201=手机号码已经注册，202=手机号码没有注册
     */
    IshavaPhone ishavaPhone;
    boolean isRegisterPhone = false;

    /**
     * 是否注册过的手机号
     */
    private void ishavaPhone()
    {
        BasicDataHttpHelper.postIsHavaPhone(RegActivity1.this, etMobilePhone.getText().toString().trim(), new SDRequestCallBack
                (IshavaPhone.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                if (error.getExceptionCode() == 502)
                {
                    isRegisterPhone = true;
                    btnGetValidateCode.setEnabled(false);
                }
                MyToast.showToast(RegActivity1.this, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                ishavaPhone = (IshavaPhone) responseInfo.result;
                isRegisterPhone = true;

                if (ishavaPhone.getStatus().equals(isExsit))
                {
                    btnGetValidateCode.setEnabled(false);
                    SDToast.showShort("手机号码已经被注册！");
                } else if (ishavaPhone.getStatus().equals(isUnExsit))
                {
                    btnGetValidateCode.setEnabled(true);
                    SDToast.showShort("手机号码可用！");
                } else
                {
                    SDToast.showShort(ishavaPhone.getMsg());
                }
            }
        });

    }


    //体验
    private void checkMoblieCodeTy(String versionNum)
    {
        String url = HttpURLUtil.newInstance().append("system/checkMessageExperience")
                .toString();
        pairs.clear();

        if (!TextUtils.isEmpty(etMobilePhone.getText().toString().trim()))
        {
            pairs.add(new BasicNameValuePair("phone", etMobilePhone.getText().toString().trim()));
        }

        if (!TextUtils.isEmpty(etMesgCode.getText().toString().trim()))
        {
            pairs.add(new BasicNameValuePair("messageCode", etMesgCode.getText().toString().trim()));
        }

        if (!TextUtils.isEmpty(versionNum))
        {
            pairs.add(new BasicNameValuePair("versionNum", versionNum));
        }
        if (StringUtils.notEmpty(versionNum))
        {
            com.utils.SPUtils.put(RegActivity1.this, com.utils.SPUtils.TYPE_FOR_TRADE_NUM, versionNum);
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, new SDRequestCallBack(CheckMoblieTyBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(RegActivity1.this, R.string.register_get_message_again);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                CheckMoblieTyBean checkMoblieCode = (CheckMoblieTyBean) responseInfo.result;
                CheckMoblieTyBean.DataBean.LoginUserBean entity = checkMoblieCode.getData().getLoginUser();
                if (checkMoblieCode.getStatus() == 200)
                {
                    String userId = entity.getEid() + "";
                    String accessToken = entity.getToken();
                    String name = entity.getName();
                    String imAccount = entity.getImAccount();
                    String account = entity.getAccount();
                    String pwd = "123456";
                    com.utils.SPUtils.put(RegActivity1.this, com.utils.SPUtils.ACCESS_TOKEN, accessToken);
                    com.utils.SPUtils.put(RegActivity1.this, com.utils.SPUtils.USER_ID, userId);
                    //直接登陆im

                    LoginUtils.loginTy(RegActivity1.this, mHttpHelper, userDao, account, pwd, "", true, accessToken);
                    if (isRegister)
                    {

                    } else
                    {//体验

                    }

                } else
                {
                    //CheckMoblieTyBean
                }
            }
        });
    }

}
