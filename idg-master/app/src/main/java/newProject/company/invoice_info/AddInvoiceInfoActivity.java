package newProject.company.invoice_info;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chaoxiang.base.utils.StringUtils;
import com.injoy.idg.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.company.superpower.bean.SupertiorBean;
import yunjing.utils.DisplayUtil;
import yunjing.utils.ToastUtils;
import yunjing.view.CustomNavigatorBar;


public class AddInvoiceInfoActivity extends AppCompatActivity {
    private static final int mREQUEST_CODE = 1;
    private static final int mRESULT_CODE = 2;

    @Bind(R.id.title_bar)
    CustomNavigatorBar mTopBar;

    @Bind(R.id.et_name)
    EditText mET_Name;
    @Bind(R.id.et_tax_number)
    EditText mET_TaxNumber;
    @Bind(R.id.et_billing_address)
    EditText mET_BillingAddress;
    @Bind(R.id.et_number)
    EditText mET_Number;
    @Bind(R.id.et_bank_accounts)
    EditText mET_BankAccounts;//开户行
    @Bind(R.id.et_phone)
    EditText mET_Phone;
    @Bind(R.id.et_fax)
    EditText mET_Fax;//传真


    @Bind(R.id.add_btn)
    Button mAddBtn;

    private String mDepartId;
    private SupertiorBean mSupertiorData;//上级

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_invoice_info_layout);
        ButterKnife.bind(this);
        initTopBar();
    }

    private void initTopBar() {
        mTopBar.setLeftTextVisible(false);
        mTopBar.setRightTextVisible(false);
        mTopBar.setMidText("发票信息");
        mTopBar.setTitleBarBackground(DisplayUtil.mTitleColor);
        View.OnClickListener topBarListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == mTopBar.getLeftImageView())
                    finish();
                else if (v == mAddBtn)
                    excuteSubmit();
            }
        };
        mTopBar.setLeftImageOnClickListener(topBarListener);
        mAddBtn.setOnClickListener(topBarListener);
        mAddBtn.setText("添加");
    }


    private void excuteSubmit() {
        if (TextUtils.isEmpty(mET_Name.getText())) {
            ToastUtils.show(this, "公司名称不能为空！");
            return;
        }

        if (TextUtils.isEmpty(mET_TaxNumber.getText())) {
            ToastUtils.show(this, "纳税号不能为空！");
            return;
        }

        if (TextUtils.isEmpty(mET_BillingAddress.getText())) {
            ToastUtils.show(this, "发票地址不能为空！");
            return;
        }


        if (TextUtils.isEmpty(mET_Number.getText())) {
            ToastUtils.show(this, "账号不能为空！");
            return;
        }

        if (TextUtils.isEmpty(mET_BankAccounts.getText())) {
            ToastUtils.show(this, "开户行不能为空！");
            return;
        }

        if (TextUtils.isEmpty(mET_Phone.getText())) {
            ToastUtils.show(this, "电话不能为空！");
            return;
        }

        if (TextUtils.isEmpty(mET_Fax.getText())) {
            ToastUtils.show(this, "传真不能为空！");
            return;
        }

        if (!StringUtils.isMobileNO(mET_Number.getText().toString())) {
            ToastUtils.show(this, "账号只能输入手机号！");
            return;
        }


       /* if (mET_Password.getText().length() < 6) {
            ToastUtils.show(this, "密码最短为6位数！");
            return;
        }
        if (mET_Password.getText().length() > 20) {
            ToastUtils.show(this, "密码最长为20位数！");
            return;
        }
*//*
        String newPwd = MD5Util.MD5(MD5Util.MD5(mET_Password.getText().toString()));//加密


        SubmitNewCBean snb = new SubmitNewCBean();
        snb.setDeptName(mTV_Depart.getText().toString());
        snb.setDeptId(mDepartId);
        snb.setJob(mET_Job.getText().toString());
        snb.setName(mET_Name.getText().toString());
        snb.setPid(mSupertiorData.getUserId() + "");
        snb.setAccount(mET_Number.getText().toString());
        snb.setPassword(newPwd);*/

      /*  SubmitHttpHelper.submitSysuser(this, new Gson().toJson(snb), new SDRequestCallBack() {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                ToastUtils.show(AddInvoiceInfoActivity.this, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                ToastUtils.show(AddInvoiceInfoActivity.this, "添加成功");
                finish();
            }
        });*/

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
