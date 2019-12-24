package com.cxgz.activity.cxim.person;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cx.base.BaseActivity;
import com.cxgz.activity.cxim.bean.PersonalListBean;
import com.cxgz.activity.cxim.http.AddFriendFilter;
import com.cxgz.activity.cxim.http.ImHttpHelper;
import com.facebook.drawee.view.SimpleDraweeView;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.chaoxiang.base.config.Constants;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.FileDownloadUtil;
import com.utils.SDImgHelper;
import com.utils.SPUtils;

import tablayout.view.textview.FontEditext;

/**
 * 进入个人通讯信息
 */
public class NotFriendPersonalInfoActivity extends BaseActivity
{
    private SimpleDraweeView ivContactsHead;//头像
    private TextView tv_info_userName;//用户名
    private Button btn_add_friend; //发信息

    private ImageView webview_carousel;

    private String userName;
    //    private String userId;
    private String cxid;//标识

    private String hisImAccount;//imID

    private String infoString;

    private String selfId;//自己的id
    private String selfName;//自己的名字

    private String imAccount;//imID

    private ImageView info_sex;

    /**
     * 头像路径
     */
    private String imgPath = "", Path = "", oldicon = "";

    @Override
    protected void init()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null)
        {
            finish();
            return;
        }

        infoString = bundle.getString(SPUtils.USER_ACCOUNT, "");

        String[] position = infoString.split("&");
        //对方的信息-二维码
        userId = position[0];
        hisImAccount = position[1];
        userName = position[2];
        cxid = position[3];
        //获取个人信息
        getPersonInfo(hisImAccount);
        //自己的信息
        selfId = (String) SPUtils.get(this, SPUtils.USER_ID, "");
        selfName = (String) SPUtils.get(this, SPUtils.USER_NAME, "");
        imAccount = (String) SPUtils.get(this, SPUtils.IM_NAME, "");

        setTitle("详细资料");

        setLeftBack(R.drawable.folder_back);

        webview_carousel = (ImageView) findViewById(R.id.webview_carousel);

        Glide.with(NotFriendPersonalInfoActivity.this)
                .load(Constants.IM_IMG_PERSONAL_INFO_BG)
                .fitCenter()
                .crossFade()
                .into(webview_carousel);

        tv_info_userName = (TextView) findViewById(R.id.tv_info_userName);
        tv_info_userName.setText(userName);

        info_sex = (ImageView) findViewById(R.id.info_sex);

        btn_add_friend = (Button) findViewById(R.id.btn_add_friend);
        btn_add_friend.setOnClickListener(this);

        ivContactsHead = (SimpleDraweeView) findViewById(R.id.info_headImg);
        ivContactsHead.setBackgroundResource(R.mipmap.temp_user_head);


    }

    @Override
    public void onClick(View view)
    {
        super.onClick(view);

        switch (view.getId())
        {
            case R.id.btn_add_friend:

                //先验证是否是好友。及判断是否是自己
                if (hisImAccount.equals(imAccount))
                {
                    showFriendDialog("不能加自己为好友！");
                    return;
                }
                // 您和（那个人的真名）已经是好友了
                getJudgeFriend();

                break;
        }
    }

    /**
     * 清空所有消息的dialog
     */
    private AlertDialog.Builder mLogoutTipsDialog;

    private void showFriendDialog(String promptString)
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

    /**
     * @param promptString
     */
    private void showAddDialog(final String promptString)
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View contentView = LayoutInflater.from(this).inflate(R.layout.prompt_add_friend, null);
        dialog.setContentView(contentView);
        dialog.setCanceledOnTouchOutside(true);

        TextView tv_out_url = (TextView) contentView.findViewById(R.id.tv_out_url);
        tv_out_url.setText(promptString);

        tv_out_url.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //完成
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showAddFriendDialog(final String outUrlString)
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View contentView = LayoutInflater.from(this).inflate(R.layout.prompt_send_add_friend_info, null);
        dialog.setContentView(contentView);
        dialog.setCanceledOnTouchOutside(true);

        final FontEditext et_verify_info = (FontEditext) contentView.findViewById(R.id.et_verify_info);

        if (StringUtils.notEmpty(selfName))
        {
            et_verify_info.setText("我是" + selfName);
        } else
        {
            et_verify_info.setText("我是");
        }

        TextView tv_open = (TextView) contentView.findViewById(R.id.tv_open);
        TextView tv_cancel = (TextView) contentView.findViewById(R.id.tv_cancel);

        tv_open.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //完成
                if (StringUtils.notEmpty(et_verify_info.getText().toString()))
                {
                    AddFriendFilter addFriendFilter = new AddFriendFilter();
                    addFriendFilter.setCreateTime(String.valueOf(System.currentTimeMillis()));
                    addFriendFilter.setFriendId(String.valueOf(personalInfoList.getData().getEid()));
                    addFriendFilter.setFriendImaccount(personalInfoList.getData().getImAccount());
                    addFriendFilter.setFriendIcon(personalInfoList.getData().getIcon());
                    addFriendFilter.setFriendName(personalInfoList.getData().getName());
                    addFriendFilter.setRemark(et_verify_info.getText().toString().trim());
                    sendVerifyInfo(addFriendFilter);
                    dialog.dismiss();
                } else
                {
                    MyToast.showToast(NotFriendPersonalInfoActivity.this, "请填写验证信息！");
                }
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //清除
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_not_friend_personal_info;
    }

    private ProgressDialog progresDialog;

    /**
     * 判断是否是好友
     */
    private void getJudgeFriend()
    {
        if (StringUtils.isEmpty(cxid) || StringUtils.isEmpty(userId))
        {
            showFriendDialog("信息不完整，请重新扫二维码！");
            NotFriendPersonalInfoActivity.this.finish();
            return;
        }

        ImHttpHelper.findJudgeFriend(NotFriendPersonalInfoActivity.this, cxid, userId, mHttpHelper, new SDRequestCallBack(SysUserCusBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(NotFriendPersonalInfoActivity.this, R.string.request_fail);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                SysUserCusBean sysUserCusBean = (SysUserCusBean) responseInfo.getResult();
                if (sysUserCusBean.getStatus() == 200)
                {
                    //是好友了。
                    if (sysUserCusBean.getData())
                    {
                        showFriendDialog("您和" + userName + "已经是好友了");
                    }
                    //不是好友。
                    else
                    {
                        showAddFriendDialog("");
                        //显示一个添加好友的框框
                    }
                }
//                MyToast.showToast(NotFriendPersonalInfoActivity.this, R.string.request_succeed);
            }
        });
    }

//    private String changBean(String verifyString)
//    {
//        AddFriendVerifyBean info = new AddFriendVerifyBean();
//        info.setCompanyId(Integer.valueOf(""));
//        info.setUserId(Integer.valueOf(userId));
//        info.setUserName(userName);
//        info.setSelfId(Integer.valueOf(selfId));
//        info.setSelfName(selfName);
//        info.setAttached(verifyString);
//        info.setHxAccount(imAccount);
//        String nowTimeString = String.valueOf((int) (System.currentTimeMillis() / 1000));
//        info.setCreatTime(nowTimeString);
//
//        SDUserEntity userInfo = new SDUserDao(NotFriendPersonalInfoActivity.this).findUserByImAccount(imAccount);
//        if (StringUtils.notEmpty(userInfo))
//        {
//            userInfo.getIcon();
//        }
//        if (StringUtils.notEmpty(userInfo.getIcon()))
//        {
//            info.setIcon(userInfo.getIcon());
//        } else
//        {
//            info.setIcon("");
//        }
//
//        String jsonString = SDGson.toJson(info);
//
//        SDLogUtil.debug("发送验证的json字符串！" + jsonString);
//        return jsonString == null ? "" : jsonString;
//    }

    /**
     * 发送验证信息
     */
    private void sendVerifyInfo(AddFriendFilter addFriendFilter)
    {
        if (addFriendFilter == null)
        {
            showFriendDialog("信息不完整，请重新扫二维码！");
            return;
        }

        showProgress();

        ImHttpHelper.findSendAddFriend(NotFriendPersonalInfoActivity.this, addFriendFilter, mHttpHelper, new SDRequestCallBack()
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                if (progresDialog != null)
                    progresDialog.dismiss();
                MyToast.showToast(NotFriendPersonalInfoActivity.this, R.string.request_fail);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                if (progresDialog != null)
                    progresDialog.dismiss();
                MyToast.showToast(NotFriendPersonalInfoActivity.this, "发送验证成功！");
            }
        });
    }

    /**
     *
     */
    private void showProgress()
    {
        progresDialog = new ProgressDialog(NotFriendPersonalInfoActivity.this);
        progresDialog.setCanceledOnTouchOutside(false);
        progresDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                progresDialog.dismiss();
            }
        });

        progresDialog.setMessage(getString(R.string.Is_post));
        progresDialog.show();
    }

    PersonalListBean personalInfoList;

    private void getPersonInfo(String imAccount)
    {
        ImHttpHelper.findPersonInfoByImAccount(NotFriendPersonalInfoActivity.this, true, mHttpHelper, imAccount, new SDRequestCallBack(PersonalListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(NotFriendPersonalInfoActivity.this, R.string.request_fail_data);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                personalInfoList = (PersonalListBean) responseInfo.getResult();
                showHeadImg(personalInfoList.getData().getIcon());
                userId = String.valueOf(personalInfoList.getData().getEid());

                if (personalInfoList.getData().getName() != null)
                    tv_info_userName.setText(personalInfoList.getData().getName());
                else
                    tv_info_userName.setText(personalInfoList.getData().getAccount());

                if (personalInfoList.getData().getSex()==1)
                {
                    info_sex.setBackgroundResource(R.mipmap.male);
                } else
                {
                    info_sex.setBackgroundResource(R.mipmap.female);
                }

            }
        });

    }

    /**
     * 显示头像
     *
     * @param icoUrl
     */
    protected void showHeadImg(final String icoUrl)
    {
        oldicon = FileDownloadUtil.getDownloadIP(0, icoUrl);
        SDImgHelper.getInstance(this).loadSmallImg(oldicon,
                R.mipmap.temp_user_head, ivContactsHead);
        ivContactsHead.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                if (StringUtils.notEmpty(userInfo.getUserId()))
//                {
//                    Intent intent = new Intent(PersonalInfoActivity.this, SDPersonalAlterActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString(com.utils.SPUtils.USER_ID, userInfo.getUserId() + "");
//                    intent.putExtras(bundle);
//                    PersonalInfoActivity.this.startActivity(intent);
//                }
            }
        });
    }

}
