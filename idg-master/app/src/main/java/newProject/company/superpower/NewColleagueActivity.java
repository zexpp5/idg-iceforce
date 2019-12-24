package newProject.company.superpower;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chaoxiang.base.utils.MD5Util;
import com.chaoxiang.base.utils.StringUtils;
import com.google.gson.Gson;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.api.SubmitHttpHelper;
import newProject.company.superpower.bean.DepartListBean;
import newProject.company.superpower.bean.SubmitNewCBean;
import newProject.company.superpower.bean.SupertiorBean;
import newProject.utils.DepartDialog;
import newProject.utils.DialogNomalView;
import yunjing.bean.ListDialogBean;
import yunjing.utils.DisplayUtil;
import yunjing.utils.ToastUtils;
import yunjing.view.CustomNavigatorBar;


public class NewColleagueActivity extends AppCompatActivity {
    private static final int mREQUEST_CODE = 1;
    private static final int mRESULT_CODE = 2;

    @Bind(R.id.title_bar)
    CustomNavigatorBar mTopBar;

    @Bind(R.id.tv_depart)
    TextView mTV_Depart;
    @Bind(R.id.tv_superior)
    TextView mTV_Superior;
    @Bind(R.id.et_name)
    EditText mET_Name;
    @Bind(R.id.et_job)
    EditText mET_Job;
    @Bind(R.id.et_number)
    EditText mET_Number;
    @Bind(R.id.et_password)
    EditText mET_Password;


    @Bind(R.id.add_btn)
    Button mAddBtn;

    private String mDepartId;
    private SupertiorBean mSupertiorData;//上级

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_colleague);
        ButterKnife.bind(this);
        initTopBar();
        initListener();
    }

    private void initListener() {
        mTV_Depart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListHttpHelper.getDepartList(NewColleagueActivity.this, new SDRequestCallBack(DepartListBean.class) {
                    @Override
                    public void onRequestFailure(HttpException error, String msg) {
                        ToastUtils.show(NewColleagueActivity.this, msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo) {
                        DepartListBean dataBean = (DepartListBean) responseInfo.getResult();
                        if (null != dataBean && null != dataBean.getData()) {
                            showDepartDialog(dataBean.getData());
                        } else
                            ToastUtils.show(NewColleagueActivity.this, "暂无部门数据");
                    }
                });

            }
        });

        mTV_Superior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewColleagueActivity.this, SuperiorListActivity.class);
                intent.putExtra("current_superior", mSupertiorData);
                startActivityForResult(intent, mREQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mREQUEST_CODE && resultCode == mRESULT_CODE) {
            mSupertiorData = (SupertiorBean) data.getSerializableExtra("supertior");
            if (null != mSupertiorData)
                mTV_Superior.setText(mSupertiorData.getUserName());
        }
    }

    private void showDepartDialog(List<DepartListBean.DataBean> data) {
        List<ListDialogBean> listData = new ArrayList<>();
        for (DepartListBean.DataBean db : data) {
            ListDialogBean ldb = new ListDialogBean();
            ldb.setCheck(false);
            ldb.setContent(db.getName());
            ldb.setDeptId(db.getEid());

            listData.add(ldb);
        }
        DepartDialog allDepartDialog = DepartDialog.newBuilder(this)
                .cancelTouchout(false)
                .isMultiselect(false)
                .style(R.style.dialogStyle)
                .setItemListener(new DepartDialog.SigleSelectListener() {
                    @Override
                    public void itemSelectListener(ListDialogBean selectItemBean) {
                        mTV_Depart.setText(selectItemBean.getContent());
                        mDepartId = selectItemBean.getDeptId() + "";
                    }

                    @Override
                    public void okListener() {
                        showAddDepartDialog();
                    }
                }).build();

        allDepartDialog.show();
        allDepartDialog.setTitle("所有部门");
        allDepartDialog.setData(listData);
    }

    DialogNomalView addDepartDialog;

    private void showAddDepartDialog() {
        addDepartDialog = DialogNomalView.newBuilder(this)
                .setContentLayout(R.layout.dialog_add_depart_layout)
                .cancelTouchout(false)
                .style(R.style.dialogStyle)
                .addViewOnclick(R.id.btnOk, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (addDepartDialog.getDepartName().isEmpty()) {
                            ToastUtils.show(NewColleagueActivity.this, "名字不能为空");
                            return;
                        }
                        findDepart();
                    }
                })
                .addViewOnclick(R.id.btnCancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addDepartDialog.dismiss();
                    }
                })
                .build();
        addDepartDialog.show();
    }

    private void findDepart() {
        ListHttpHelper.postExistDeptName(this, new SDRequestCallBack() {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                ToastUtils.show(NewColleagueActivity.this, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                Object result = responseInfo.getResult();
                try {
                    JSONObject jsonObject = new JSONObject(result.toString());
                    JSONObject data = jsonObject.getJSONObject("data");
                    int status = data.getInt("status");
                    if (status == 501) {
                        addDepart();
                    } else {
                        String msg = data.getString("msg");
                        ToastUtils.show(NewColleagueActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //添加部门
    private void addDepart() {
        String departName = addDepartDialog.getDepartName();
        ListHttpHelper.postSysdept(this, departName, new SDRequestCallBack() {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                ToastUtils.show(NewColleagueActivity.this, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                ToastUtils.show(NewColleagueActivity.this, "添加成功");
                addDepartDialog.dismiss();
            }
        });
    }

    private void initTopBar() {
        mTopBar.setLeftTextVisible(false);
        mTopBar.setRightTextVisible(false);
        mTopBar.setMidText(getResources().getString(R.string.im_new_friend_title));
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
            ToastUtils.show(this, "姓名不能为空！");
            return;
        }

        if (TextUtils.isEmpty(mTV_Depart.getText())) {
            ToastUtils.show(this, "部门不能为空！");
            return;
        }

        if (TextUtils.isEmpty(mET_Job.getText())) {
            ToastUtils.show(this, "职务不能为空！");
            return;
        }


        if (TextUtils.isEmpty(mTV_Superior.getText())) {
            ToastUtils.show(this, "上级不能为空！");
            return;
        }

        if (TextUtils.isEmpty(mET_Number.getText())) {
            ToastUtils.show(this, "账号不能为空！");
            return;
        }

        if (TextUtils.isEmpty(mET_Password.getText())) {
            ToastUtils.show(this, "密码不能为空！");
            return;
        }

        if (!StringUtils.isMobileNO(mET_Number.getText().toString())) {
            ToastUtils.show(this, "账号只能输入手机号！");
            return;
        }


        if (mET_Password.getText().length() < 6) {
            ToastUtils.show(this, "密码最短为6位数！");
            return;
        }
        if (mET_Password.getText().length() > 20) {
            ToastUtils.show(this, "密码最长为20位数！");
            return;
        }

        String newPwd = MD5Util.MD5(MD5Util.MD5(mET_Password.getText().toString()));//加密


        SubmitNewCBean snb = new SubmitNewCBean();
        snb.setDeptName(mTV_Depart.getText().toString());
        snb.setDeptId(mDepartId);
        snb.setJob(mET_Job.getText().toString());
        snb.setName(mET_Name.getText().toString());
        snb.setPid(mSupertiorData.getUserId() + "");
        snb.setAccount(mET_Number.getText().toString());
        snb.setPassword(newPwd);

        SubmitHttpHelper.submitSysuser(this, new Gson().toJson(snb), new SDRequestCallBack() {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                ToastUtils.show(NewColleagueActivity.this, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                ToastUtils.show(NewColleagueActivity.this, "添加成功");
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if (null != addDepartDialog)
            addDepartDialog.dismiss();
    }

}
