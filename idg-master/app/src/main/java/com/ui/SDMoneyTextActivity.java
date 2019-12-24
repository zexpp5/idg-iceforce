package com.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.chaoxiang.base.utils.StringUtils;
import com.injoy.idg.R;
import com.chaoxiang.base.utils.SDLogUtil;
import com.cxgz.activity.cx.base.BaseActivity;

/**
 * 单文本框输入
 *
 * @author Amy
 */
public class SDMoneyTextActivity extends BaseActivity implements TextWatcher {
    private static final String TAG = "SDCrmEditTextActivity";

    private EditText edValue;
    private String strValue = "", strTitle;
    public static String VALUES = "values";
    public static final String MAX_SIZE = "mx_z";
    public static final String IS_NAME = "isname";
    public static final String IS_PHONE = "isphone";
    public static final String IS_EMAIL = "isemail";
    private boolean isname = false;// 是否是名字
    private boolean isphone = false;// 是否是手机号码
    private boolean isemail = false;// 是否是邮箱
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
        maxSize = bundle.getInt(MAX_SIZE, 0);
        strValue = bundle.getString(VALUES, "");

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
        SDLogUtil.debug(TAG,"postRight");

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
        // if(issum&&strValue.length()==10){
        // SDToast.showShort(strTitle
        // + "只能输入7位整数和两位小数");
        // }
    }

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
        }

    }

    private void buildResultOK(String value) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("value", value);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
    }

}
