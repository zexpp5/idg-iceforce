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
import android.widget.Toast;

import com.bean.CheckCodeResp;
import com.bean.CheckMoblieCode;
import com.bean.GetCodeResp;
import com.bean.IshavaPhone;
import com.bean.SendMessageResp;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cx.base.BaseActivity;
import com.http.HttpURLUtil;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.utils.SDToast;
import com.view.TimeButton;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * linjp 20160605
 * 注册 页面1
 */
public class RegActivity2 extends BaseActivity
{
    protected EditText etMobilePhone;
    protected EditText etCode;
    protected ImageView ivShowCode;
    protected EditText etMesgCode;
    protected TimeButton btnGetValidateCode;
    protected Button btnNext;
    View mDecorView;

    public static final String isExsit = "201";
    public static final String isUnExsit = "202";
    protected LinearLayout llRight;

    @Override
    protected void init()
    {
        setTitle("注册");
        setLeftBack(R.mipmap.back_back);//返回的

        mDecorView = getWindow().getDecorView();
        initView();
        llRight = (LinearLayout) findViewById(R.id.llRight);
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

        llRight = (LinearLayout) findViewById(R.id.llRight);
        etMobilePhone = (EditText) findViewById(R.id.etMobilePhone);
        etCode = (EditText) findViewById(R.id.etCode);
        ivShowCode = (ImageView) findViewById(R.id.ivShowCode);
        etMesgCode = (EditText) findViewById(R.id.etMesgCode);

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
                        // ishavaPhone();
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

        String url = HttpURLUtil.newInstance().append("register/getCode.json")
                .toString();
        pairs.clear();
        RequestParams params = new RequestParams();

        mHttpHelper.get(url, params, true, new SDRequestCallBack(GetCodeResp.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                Toast.makeText(RegActivity2.this, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                getCodeResp = (GetCodeResp) responseInfo.result;

                if (getCodeResp.getStatus() == 200)
                {
                    if (getCodeResp.getData().getCode() == null || getCodeResp.getData().getCode().equals("null"))
                    {
                        SDToast.showShort("获取t图形验证码失败~！");
                        ivShowCode.setImageBitmap(GenerateCodeUtils.getInstance().createBitmap2("点击重试"));
                    } else
                    {
                        ivShowCode.setImageBitmap(GenerateCodeUtils.getInstance().createBitmap2(getCodeResp.getData().getCode()));
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

        String url = HttpURLUtil.newInstance().append("register/checkCode.json")
                .toString();
        pairs.clear();
        if (!TextUtils.isEmpty(getCodeResp.getData().getCodeKey()))
        {
            pairs.add(new BasicNameValuePair("codeKey", getCodeResp.getData().getCodeKey()));
        } else
        {
            //SDToast.showShort("请稍候再试！");
        }

        if (!TextUtils.isEmpty(etCode.getText().toString().trim()))
        {
            pairs.add(new BasicNameValuePair("code", etCode.getText().toString().trim()));
        } else
        {
            SDToast.showShort("请输入图形验证码！");
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);

        mHttpHelper.post(url, params, true, new SDRequestCallBack(CheckCodeResp.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                Toast.makeText(RegActivity2.this, msg, Toast.LENGTH_SHORT).show();
                cancelTime();
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                checkCodeResp = (CheckCodeResp) responseInfo.result;

//                if (checkCodeResp.getStatus().equals("200")) {
//                    sendMessage();
//                } else {
//                    SDToast.showShort(checkCodeResp.getMsg() + "");
//                }
                sendMessage();
            }
        });
    }


    /**
     * phone	手机号	String	是	手机短信有效期为1分钟，1分钟内重复请求该接口会返回上次的验证码
     *
     */

    /**
     * {
     * "messageCode ": "2201",
     * "phone ": "13640635965",
     * "msg": "短信发送成功",
     * "status": 200
     * }
     */
    SendMessageResp sendMessageResp;

    protected void sendMessage()
    {

        String url = HttpURLUtil.newInstance().append("register/sendMessage.json")
                .toString();
        pairs.clear();


        if (!TextUtils.isEmpty(etMobilePhone.getText().toString().trim()))
        {
            pairs.add(new BasicNameValuePair("phone", etMobilePhone.getText().toString().trim()));
        } else
        {
            SDToast.showShort("请输入手机号");

        }

        if (!StringUtils.isMobileNO(etMobilePhone.getText().toString().trim()))
        {
            SDToast.showLong("请入合法的手机号！");
        }
        if (!TextUtils.isEmpty(etCode.getText().toString().trim()))
        {
            pairs.add(new BasicNameValuePair("code", etCode.getText().toString() + ""));
        } else
        {
            SDToast.showShort("请输入图片中的验证码");
        }

        pairs.add(new BasicNameValuePair("versionCode", "5"));

        RequestParams params = new RequestParams();
        params.addQueryStringParameter(pairs);
        mHttpHelper.post(url, params, true, new SDRequestCallBack(SendMessageResp.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                Toast.makeText(RegActivity2.this, msg, Toast.LENGTH_SHORT).show();
                cancelTime();
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                sendMessageResp = (SendMessageResp) responseInfo.result;
                if (sendMessageResp.getStatus().equals("200"))
                {
                    SDToast.showShort("验证码获取成功！");

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

    /**
     * 参数名	名称	类型	是否必需	备注
     phone	手机号	String	是
     messageCode	4位数字验证码	String	是
     */
    /**
     *
     * 参数名	名称	类型	备注
     status
     响应状态码	int	200=校验成功，500=服务器错误，501=验证码过期
     */
    /**
     * {
     * "msg": "match ok",
     * "status": 200
     * }
     */
    CheckMoblieCode checkMoblieCode;

    /**
     * 检测短信验证码
     */
    private void checkMoblieCode()
    {
        String url = HttpURLUtil.newInstance().append("register/checkMoblieCode.json")
                .toString();
        pairs.clear();

        if (!TextUtils.isEmpty(etMobilePhone.getText().toString().trim()))
        {
            pairs.add(new BasicNameValuePair("phone", etMobilePhone.getText().toString().trim()));
        }

        if (!TextUtils.isEmpty(etCode.getText().toString().trim()))
        {
            pairs.add(new BasicNameValuePair("messageCode", etMesgCode.getText().toString().trim()));
        }


        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, new SDRequestCallBack(CheckMoblieCode.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                Toast.makeText(RegActivity2.this, " 请稍候再试~", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                checkMoblieCode = (CheckMoblieCode) responseInfo.result;

                if (checkMoblieCode.getStatus().equals("200"))
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("phone", etMobilePhone.getText().toString());
                    openActivity(RegActivity2.class, bundle);
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
        String url = HttpURLUtil.newInstance().append("register/isHavaPhone/" + etMobilePhone.getText().toString().trim()+".json")
                .toString();
        pairs.clear();
//        pairs.add(new BasicNameValuePair("versionCode", "5"));
        RequestParams params = new RequestParams();
        params.addQueryStringParameter(pairs);
        mHttpHelper.getNotoken(url, params, true, new SDRequestCallBack(IshavaPhone.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                Toast.makeText(RegActivity2.this, msg, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                ishavaPhone = (IshavaPhone) responseInfo.result;

                if (ishavaPhone.getStatus().equals(isExsit))
                {
                    SDToast.showShort("手机号码已经被注册！");
                } else if (ishavaPhone.getStatus().equals(isUnExsit))
                {
                    SDToast.showShort("手机号码可用！");
                    getCode();
                } else
                {
                    SDToast.showShort(ishavaPhone.getMsg());
                }


            }
        });

    }


}
