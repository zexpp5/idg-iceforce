package com.cxgz.activity.cx.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cx.base.BaseActivity;
import com.injoy.idg.R;

/**
 * 单文本框输入
 *
 * @author Amy
 */
public class SDCrmEditTextActivity extends BaseActivity implements TextWatcher {
    private static final String TAG = "SDCrmEditTextActivity";

    private EditText edValue;
    private String strValue = "", strTitle;
    public static String VALUES = "values";
    public static final String MAX_SIZE = "mx_z";
    public static final String IS_NAME = "isname";
    public static final String IS_PHONE = "isphone";
    public static final String IS_EMAIL = "isemail";
    public static final String IS_MONEY = "ismoney";
    private boolean isname = false;// 是否是名字
    private boolean isphone = false;// 是否是手机号码
    private boolean isemail = false;// 是否是邮箱
    private boolean ismoney = false;// 是否是邮箱
    private int maxSize = 0;

    @Override
    protected void init() {
        // TODO Auto-generated method stub
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            finish();
            return;
        }

        strTitle = bundle.getString("title", getString(R.string.pleaseinput)).trim();
        isname = bundle.getBoolean(IS_NAME, false);
        isphone = bundle.getBoolean(IS_PHONE, false);
        isemail = bundle.getBoolean(IS_EMAIL, false);
        ismoney = bundle.getBoolean(IS_MONEY, false);
        maxSize = bundle.getInt(MAX_SIZE, 0);
        strValue = bundle.getString(VALUES, "");
        if ("0".equals(strValue))
            strValue = "";

        setTitle(strTitle);
        setLeftBack(R.drawable.folder_back);
        edValue = (EditText) findViewById(R.id.edValue);

        // edValue.setFocusable(true);

        if (isname) {
            edValue.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        } else if (isphone) {
            edValue.setInputType(InputType.TYPE_CLASS_PHONE);
        } else if (isemail) {
            edValue.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        } else if (ismoney) {
            int inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER;
            edValue.setInputType(inputType);

            edValue.addTextChangedListener(doubleEdittext);
        }

        if (maxSize > 0) {
            edValue.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                    maxSize)});
        }

        if (!strValue.equals("")) {
            edValue.setText(strValue);
            int length = strValue.length();
            edValue.setSelection(length);
        }

        addRightBtn(getString(R.string.save), new OnClickListener() {

            @Override
            public void onClick(View v) {
                postRight();

            }
        });
    }

    private void postRight() {
        SDLogUtil.debug(TAG, "postRight");

        strValue = edValue.getText().toString().trim();
        if (TextUtils.isEmpty(strValue)) {
            showToast(getString(R.string.pleaseinput));
            return;
        }
        if (isname) {
            buildResultOK(strValue);
            finish();
        } else if (isphone) {
            if (!StringUtils.isMobileNO(strValue)) {
                showToast(getString(R.string.p_phone));
            } else {
                buildResultOK(strValue);
                finish();
            }
        } else if (isemail) {
            if (StringUtils.isEmail(strValue)) {
                buildResultOK(strValue);
                finish();
            } else {
                showToast(getString(R.string.not_email));
            }
        } else if (ismoney) {
           if (strValue.contains(".")){
               if (strValue.endsWith(".")){
                   showToast("请正确输入金额");
                   return;
               }
           }else{
               if (strValue.length()>6) {
                   showToast("金额整数最大6位数");
                   return;
               }
           }
            buildResultOK(strValue);
            finish();
        }
    }

    @Override
    protected int getContentLayout() {
        // TODO Auto-generated method stub
        return R.layout.sd_crm_oneedittext;
    }


    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub
        strValue = s.toString();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }

    /**
     * 限制edittext输入金额条件
     */
    public TextWatcher doubleEdittext = new TextWatcher() {
        public void afterTextChanged(Editable s) {

            try {
                String temp = s.toString();
                int posDot = temp.indexOf(".");

                //输入9位整数后只能输入.,否则删除第9位
                if (temp.length() > 9) {
                    if (posDot > 0) {
                        if (temp.length() - posDot - 1 > 2) {
                            s.delete(posDot + 3, posDot + 4);
                        }
                    }
                    if (posDot < 0) {
                        s.delete(9, 10);
                    }
                }

                //如果第一个数为0，第二个数只能为.,否则删除第二个数
                int iszeo = temp.indexOf("0");
                if (iszeo == 0 && temp.length() > 1) {
                    if (posDot != 1) {
                        s.delete(1, 2);
                    }
                }

                //如果第一个数为.就删掉
                if (posDot == 0) {
                    s.delete(posDot, posDot + 1);
                }

                //小数点后保留两位
                if (posDot <= 0) return;
                if (temp.length() <= 9)
                    if (temp.length() - posDot - 1 > 2) {
                        s.delete(posDot + 3, posDot + 4);
                    }

            } catch (Exception e) {

            }

        }

        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }
    };

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // TODO Auto-generated method stub
        if (isphone) {
            if (s.length() > 11) {
                showToast(getString(R.string.rightphonestyle));
                edValue.setText(strValue);
                edValue.setSelection(11);
            }
        } else if (isemail) {
            if (s.length() > 50) {
                showToast(getString(R.string.emailmaxleng50));
                edValue.setText(strValue);
                edValue.setSelection(50);
            }
        } else if (ismoney) {
//            if(!isNum(strValue)){
//                MyToast.showToast(SDCrmEditTextActivity.this,"请输入正确的金额");
//                return;
//            }

            if (s.length() > 10) {
                showToast(getString(R.string.moneymaxleng10));
                edValue.setText(strValue);
                edValue.setSelection(10);
            }
        }

    }

    private void buildResultOK(String value) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("value", value);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
    }

    public static boolean isNum(String str) {
//        return str.matches("^(([1-9]{1,9})|0)(\\.[0-9]{1,2})?$");
        return str.matches("^[+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");

    }


}
