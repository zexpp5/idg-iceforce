package com.cxgz.activity.cxim.person;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.chaoxiang.base.utils.SDGson;
import com.injoy.idg.R;
import com.base.BaseApplication;
import com.chaoxiang.base.utils.MD5Util;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.imsdk.pushuser.IMUserManager;
import com.cxgz.activity.cx.base.BaseActivity;
import com.entity.SDFileListEntity;
import com.google.gson.reflect.TypeToken;
import com.http.FileUpload;
import com.http.HttpURLUtil;
import com.http.SDResponseInfo;
import com.lidroid.xutils.http.RequestParams;
import com.superdata.im.utils.CxNotificationUtils;
import com.ui.SDLoginActivity;
import com.utils.FileUploadUtil;
import com.utils.SDToast;
import com.utils.SPUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 修改密码。
 */
public class SDPersonAlterPswActivity extends BaseActivity
{
    private static final String TAG = "SDPersonAlterPswActivity";

    private EditText edOldPsw, edNewPsw, edConfirmPsw;
    private String strOldPsw, strNewPsw, strConfirmPsw;
    private Button btnOkay;

    @Override
    protected void init()
    {
        // TODO Auto-generated method stub
        setTitle(getString(R.string.alterpsw));
        setLeftBack(R.drawable.folder_back);
        edOldPsw = (EditText) findViewById(R.id.edOldPsw);
        edNewPsw = (EditText) findViewById(R.id.edNewPsw);
        edConfirmPsw = (EditText) findViewById(R.id.edConfirmPsw);
        btnOkay = (Button) findViewById(R.id.btn_ok);

        btnOkay.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                getValue();
                if (strOldPsw.equals(""))
                {
                    showToast(getString(R.string.pleaseinputoldpws));
                    return;
                } else if (strNewPsw.equals(""))
                {
                    showToast(getString(R.string.pleaseinputnewpws));
                    return;
                } else if (strConfirmPsw.equals(""))
                {
                    showToast(getString(R.string.pleaseinputnewpwsagain));
                    return;
                } else if (!strNewPsw.equals(strConfirmPsw))
                {
                    showToast(getString(R.string.pawinputwrongtwice));
                    return;
                }
                if (strNewPsw.length() < 6 && strNewPsw.length() > 18)
                {
                    showToast(getString(R.string.pleaseinput6to18));
                    return;
                }
                if (!strOldPsw.equals(SPUtils.get(SDPersonAlterPswActivity.this, SPUtils.PWD, "")))
                {
                    SDLogUtil.debug(TAG, "strOldPsw-------->>" + SPUtils.get(SDPersonAlterPswActivity.this, SPUtils.PWD, ""));
                    SDLogUtil.debug(TAG, "strNewPsw-------->>" + strNewPsw);
                    showToast(getString(R.string.oldpswwrang));
                    return;
                }

                String url = HttpURLUtil.newInstance().append("sysUser").append("update").toString();

                RequestParams params = new RequestParams();

                if (!TextUtils.isEmpty(strOldPsw) && !TextUtils.isEmpty(strNewPsw))
                {
                    params.addBodyParameter("password", MD5Util.MD5(MD5Util.MD5(strOldPsw)));
                    params.addBodyParameter("newPassWord", MD5Util.MD5(MD5Util.MD5(strNewPsw)));
                }

                postRequest(url, null, params);

            }
        });
    }

    private void getValue()
    {
        strOldPsw = edOldPsw.getText().toString();
        strNewPsw = edNewPsw.getText().toString();
        strConfirmPsw = edConfirmPsw.getText().toString();
    }

    private boolean progressShow;

    /**
     * 提交请求
     *
     * @param url
     * @param files
     */
    private void postRequest(String url, List<SDFileListEntity> files,
                             RequestParams params)
    {
        progressShow = true;
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);

        pd.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                progressShow = false;
            }
        });

        pd.setMessage(getString(R.string.Is_post));
        pd.show();

        FileUploadUtil.resumableUpload(getApplication(), files, "", null,
                url, params, new FileUpload.UploadListener()
                {
                    @Override
                    public void onSuccess(SDResponseInfo responseInfo,
                                          HashMap<String, Object> returns)
                    {
                        if (!progressShow)
                        {
                            pd.dismiss();
                            return;
                        }
                        SDGson gson = new SDGson();
                        Map<String, String> s = gson.fromJson(
                                responseInfo.result.toString(),
                                new TypeToken<Map<String, String>>()
                                {
                                }.getType());
                        int status = Integer.parseInt(s.get("status"));
                        if (status == 200)
                        {

                            showHintDialog("密码修改成功，请重新登录！");

                        } else
                        {
                            SDToast.showLong(getString(R.string.pswchancefail));
                        }

                        pd.dismiss();

                        MyToast.showToast(SDPersonAlterPswActivity.this, R.string.password_change_success);


                    }

                    @Override
                    public void onProgress(int byteCount, int totalSize)
                    {

                    }

                    @Override
                    public void onFailure(Exception ossException)
                    {
                        pd.dismiss();
                        SDToast.showLong(getString(R.string.person_info_alterfail));
                    }
                });
    }

    private AlertDialog.Builder mLogoutTipsDialog;

    private void showHintDialog(String promptString)
    {
        if (mLogoutTipsDialog == null)
        {
            mLogoutTipsDialog = new AlertDialog.Builder(this);
        }
        mLogoutTipsDialog.setMessage(promptString);
        mLogoutTipsDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                mLogoutTipsDialog.create().dismiss();

                logout();
            }
        });

//            mLogoutTipsDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener()
//            {
//                @Override
//                public void onClick(DialogInterface dialog, int which)
//                {
//
//                }
//            });
        mLogoutTipsDialog.create().show();
    }

    private void logout()
    {
        CxNotificationUtils.cleanAllNotification(this);
        IMUserManager.loginOut(this);
        BaseApplication.getInstance().removeAllActivity();

        SPUtils.put(SDPersonAlterPswActivity.this, SPUtils.PWD, "");
        SPUtils.put(SDPersonAlterPswActivity.this, SPUtils.AES_PWD, "");
        SPUtils.put(SDPersonAlterPswActivity.this, SPUtils.IS_AUTO_LOGIN, false);

        openActivity(SDLoginActivity.class);
        finish();

//        final ProgressDialog pd = new ProgressDialog(this);
//        String st = getResources().getString(R.string.Are_logged_out);
//        pd.setMessage(st);
//        pd.setCanceledOnTouchOutside(false);
//        pd.show();
//        SDApplication.getInstance().logout(new EMCallBack() {
//
//            @Override
//            public void onSuccess() {
//                SDApplication.getInstance().logout();
//                runOnUiThread(new Runnable() {
//                    public void run() {
//                        pd.dismiss();
//
//                        // 重新显示登陆页面
//                        LinkedList<Activity> activities = SDApplication.getInstance().getActivityList();
//                        for (int i = 0; i < activities.size(); i++) {
//                            if (null != activities.get(i)) {
//                                activities.get(i).finish();
//                            }
//                        }
//                        startActivity(new Intent(SDPersonAlterPswActivity.this,
//                                SDLoginActivity.class));
//                        SPUtils.put(SDPersonAlterPswActivity.this, SPUtils.PWD, "");
//                        SPUtils.put(SDPersonAlterPswActivity.this, SPUtils.AES_PWD, "");
//                        SPUtils.put(SDPersonAlterPswActivity.this, SPUtils.IS_AUTO_LOGIN, false);
//                        finish();
//                    }
//
//                });
//            }
//
//            @Override
//            public void onProgress(int progress, String status) {
//
//            }
//
//            @Override
//            public void onError(int code, String message) {
//
//            }
//        });
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.sd_person_alterpsw;
    }

}
