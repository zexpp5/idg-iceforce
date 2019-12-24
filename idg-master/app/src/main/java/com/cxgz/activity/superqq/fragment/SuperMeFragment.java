package com.cxgz.activity.superqq.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.BaseApplication;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseFragment;
import com.cxgz.activity.cxim.bean.PersonalListBean;
import com.cxgz.activity.cxim.http.ImHttpHelper;
import com.cxgz.activity.cxim.person.SDPersonalAlterActivity;
import com.cxgz.activity.cxim.utils.MainTopMenuUtils;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.cxgz.activity.superqq.activity.ErweimaActivity;
import com.cxgz.activity.superqq.activity.SettingsActivitiy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.superdata.im.constants.CxSPIMKey;
import com.superdata.marketing.view.percent.PercentLinearLayout;
import com.ui.SDLoginActivity;
import com.utils.SDImgHelper;
import com.utils.SPUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.mine.CollectActivity;
import newProject.mine.LogisticSearchActivity;
import newProject.mine.SuperConfigActivity;
import newProject.mine.SuperFindActivity;
import newProject.company.superpower.SuperPowerActivity;
import newProject.company.superuser.SuperUserListActivity;
import tablayout.view.textview.FontTextView;
import yunjing.utils.DisplayUtil;


/**
 * 超信-我-界面
 */
public class SuperMeFragment extends BaseFragment
{
    private static final String TAG = "SuperMeFragment";

    String imAccount = "";

    @Bind(R.id.iv_neterror)
    ImageView ivNeterror;
    @Bind(R.id.tv_connect_errormsg)
    FontTextView tvConnectErrormsg;
    @Bind(R.id.iv_head)
    SimpleDraweeView ivHead;
    @Bind(R.id.tv_nickName)
    FontTextView tvNickName;
    @Bind(R.id.text_name_type)
    FontTextView textNameType;
    @Bind(R.id.tv_department_type)
    FontTextView tvDepartmentType;
    @Bind(R.id.text_company_name)
    FontTextView textCompanyName;
    @Bind(R.id.tv_company_name)
    FontTextView tvCompanyName;
    @Bind(R.id.ll_erweima)
    PercentLinearLayout llErweima;
    @Bind(R.id.imageView4)
    ImageView imageView4;
    @Bind(R.id.ll_erweima_left)
    PercentLinearLayout llErweimaLeft;
    @Bind(R.id.ll_mine_info_detail)
    PercentLinearLayout llMineInfoDetail;
    @Bind(R.id.ll_kaoqin)
    PercentLinearLayout llKaoqin;
    @Bind(R.id.ll_collect)
    PercentLinearLayout llCollect;
    @Bind(R.id.ll_logistics)
    PercentLinearLayout llLogistics;
    @Bind(R.id.ll_super_user)
    PercentLinearLayout llSuperUser;
    @Bind(R.id.ll_super_power)
    PercentLinearLayout llSuperPower;
    @Bind(R.id.ll_super_search)
    PercentLinearLayout llSuperSearch;
    @Bind(R.id.ll_my_setting_pl)
    PercentLinearLayout llMySettingPl;
    @Bind(R.id.ll_my_quit)
    PercentLinearLayout llMyQuit;
    @Bind(R.id.ll_super_layout)
    LinearLayout llSuperLayout;
    @Bind(R.id.ll_super_config)
    PercentLinearLayout llSuperConfig;

    @Override
    protected int getContentLayout()
    {
        return R.layout.super_me_tab;
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setMineInfo();
    }

    @Override
    protected void init(View view)
    {
        setTitle(R.string.super_tab_05);
        setBarBackGround(DisplayUtil.mTitleColor);
        addRightBtn2(R.mipmap.menu_add, new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                MainTopMenuUtils.getInstance(getActivity()).showMenu(view, "1");
            }
        });

       /* addLeftBtn(R.drawable.folder_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });*/
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private void initView()
    {
        int isSuper = (int) SPUtils.get(getActivity(), SPUtils.IS_SUPER, 0);
        int SuperStatus = (int) SPUtils.get(getActivity(), SPUtils.IS_SUPER_STATUS, 0);
        if (isSuper == 1 && SuperStatus == 1)
        {
            llSuperLayout.setVisibility(View.VISIBLE);
        } else
        {
            llSuperLayout.setVisibility(View.GONE);
        }
        userId = (String) SPUtils.get(getActivity(), SPUtils.USER_ID, "");
        imAccount = (String) SPUtils.get(getActivity(), SPUtils.IM_NAME, "").toString();

        //个人信息
        llMineInfoDetail.setOnClickListener(this);
        //考勤
        llKaoqin.setOnClickListener(this);
        //收藏
        llCollect.setOnClickListener(this);
        //物流
        llLogistics.setOnClickListener(this);
        //超级用户
        llSuperUser.setOnClickListener(this);
        //超级权限
        llSuperPower.setOnClickListener(this);
        //超级搜索
        llSuperSearch.setOnClickListener(this);
        //超级配置
        llSuperConfig.setOnClickListener(this);
        //设置
        llMySettingPl.setOnClickListener(this);

        //二维码点击
        llErweima.setOnClickListener(this);

        llMyQuit.setOnClickListener(this);


    }

    private void setMineInfo()
    {
        ImHttpHelper.findPersonInfoByImAccount(getActivity(), false, mHttpHelper, imAccount, new SDRequestCallBack
                (PersonalListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                //MyToast.showToast(getActivity(), "刷新个人信息失败！");
                bindView();
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                PersonalListBean personalListBean = (PersonalListBean) responseInfo.getResult();
                SDImgHelper.getInstance(getActivity()).loadSmallImg(personalListBean.getData().getIcon(), R.mipmap
                        .temp_user_head, ivHead);
                tvNickName.setText(personalListBean.getData().getName());
                textCompanyName.setText(personalListBean.getData().getJob());
                SPUtils.put(getActivity(), SPUtils.USER_ICON, personalListBean.getData().getIcon());
                SPUtils.put(getActivity(), SPUtils.USER_NAME, personalListBean.getData().getName());

            }
        });
    }

    private void bindView()
    {
        userId = (String) SPUtils.get(getActivity(), SPUtils.USER_ID, "-1");
        SDUserEntity user = mUserDao.findUserByUserID(userId);
        String nick_name, jobName;
        if (user != null)
        {
            SDImgHelper.getInstance(getActivity()).loadSmallImg(user.getIcon(), R.mipmap.temp_user_head, ivHead);
            nick_name = user.getName();
            jobName = user.getJob();
        } else
        {
            nick_name = (String) SPUtils.get(getActivity(), SPUtils.USER_NAME, "");
            jobName = (String) SPUtils.get(getActivity(), SPUtils.COMPANY_JOB, "");
        }
        tvNickName.setText(nick_name);
        textCompanyName.setText(jobName);
    }

    @Override
    public void onClick(View v)
    {
        Bundle bundle = null;
        switch (v.getId())
        {
            case R.id.ll_kaoqin:// 考勤
//                startActivity(new Intent(getActivity(), AttendanceSubmitActivity.class));
                break;
            case R.id.ll_collect://收藏
                startActivity(new Intent(getActivity(), CollectActivity.class));
                break;
            case R.id.ll_logistics://物流
                startActivity(new Intent(getActivity(), LogisticSearchActivity.class));
                break;
            case R.id.ll_super_user: // 超级用户
                startActivity(new Intent(getActivity(), SuperUserListActivity.class));
                break;
            case R.id.ll_super_power://超级权限
                startActivity(new Intent(getActivity(), SuperPowerActivity.class));
                break;
            case R.id.ll_super_search://超级搜索
                startActivity(new Intent(getActivity(), SuperFindActivity.class));
                break;
            case R.id.ll_super_config://超级配置
                startActivity(new Intent(getActivity(), SuperConfigActivity.class));
                break;

            case R.id.ll_mine_info_detail://编辑资料

                if (TextUtils.isEmpty(imAccount))
                {
                    return;
                }
                bundle = new Bundle();
                bundle.putString(SPUtils.USER_ID, imAccount);
                openActivity(SDPersonalAlterActivity.class, bundle);
                break;

            case R.id.ll_my_setting_pl://设置

                openActivity(SettingsActivitiy.class);

                break;
            case R.id.ll_erweima://点击了二维码图标
                String goUrl = DisplayUtil.getInviteUrl(getActivity());
                if (StringUtils.notEmpty(goUrl))
                {
                    Intent intent = new Intent(getActivity(), ErweimaActivity.class);
                    intent.putExtra("text", goUrl);
                    startActivity(intent);
                }
//                if (StringUtils.notEmpty(getParams()))
//                {
//                    Intent intent = new Intent(getActivity(), ErweimaActivity.class);
//                    intent.putExtra("text", getParams());
//                    startActivity(intent);
//                }
                break;

            case R.id.ll_my_quit://退出
                //logoutDialog();

                break;
        }
    }

    public void logoutDialog()
    {
        int screenWidth, screenHeight;
        DisplayMetrics dm = new DisplayMetrics();
        dm = getActivity().getApplicationContext().getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;


        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_exit, null);
        dialog.setContentView(contentView);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) ((screenWidth / 2.5) * 2); // 宽度
//        lp.height = screenHeight / 2; // 高度
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);


//        dialog.setCanceledOnTouchOutside(false);
        TextView content = (TextView) contentView.findViewById(R.id.content);
        content.setText("是否确认退出");
        TextView tv_open = (TextView) contentView.findViewById(R.id.tv_open);//确定
        tv_open.setText(getActivity().getResources().getString(R.string.choose_sex_sure));
        TextView tv_cancel = (TextView) contentView.findViewById(R.id.tv_cancel);//取消
        tv_cancel.setText(getActivity().getResources().getString(R.string.choose_sex_cancel));
        TextView et_verify_info = (TextView) contentView.findViewById(R.id.et_verify_info);
        tv_open.setOnClickListener(new View.OnClickListener()
        {//确定
            @Override
            public void onClick(View v)
            {
                logout();
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener()
        {//取消
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
        dialog.show();

//        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
//        builder.setTitle("退出登录");
//        builder.setMessage("是否确定退出？");
//        builder.setCancelable(true);
//        builder.setPositiveButton("是",
//                new DialogInterface.OnClickListener()
//                {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which)
//                    {
//                        logout();
//                    }
//                });
//        builder.setNegativeButton("否", new DialogInterface.OnClickListener()
//        {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i)
//            {
//                dialogInterface.dismiss();
//            }
//        });
//        builder.show();
    }

    private void logout()
    {
        com.chaoxiang.base.utils.SPUtils.put(getActivity(), CxSPIMKey.IS_LOGIN, false);
        BaseApplication.getInstance().logout();
        startActivity(new Intent(getActivity(), SDLoginActivity.class));
        getActivity().finish();
    }


    /**
     * 设置参数
     */
    private String getParams()
    {
        String urlString;
//        long companyIdLong = (Long) SPUtils.get(getActivity(), SPUtils.COMPANY_ID, 1L);
//        String companyId = String.valueOf(companyIdLong);
        String userId = (String) SPUtils.get(getActivity(), SPUtils.USER_ID, "").toString();
        String userName = (String) SPUtils.get(getActivity(), SPUtils.USER_NAME, "").toString();
        String imAccount = (String) SPUtils.get(getActivity(), SPUtils.IM_NAME, "").toString();

        String cxid = "cx:injoy365.cn";

        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(imAccount) || StringUtils.isEmpty(userName) || StringUtils
                .isEmpty(cxid))
        {
            urlString = "";
            MyToast.showToast(getActivity(), "信息不全，生成失败！");
        } else
        {
            urlString = appendString(userId) + appendString(imAccount) + appendString(userName) + cxid;
            SDLogUtil.debug("SuperMineFragment的二维码-urlString：" + urlString);
        }

        SDLogUtil.debug("SuperMineFragment的二维码2-urlString：" + appendString(userId) + appendString(imAccount) + appendString
                (userName) + cxid);

        return urlString;
    }

    private String appendString(String appString)
    {
        String goUrl;
        StringBuilder stringInfo = new StringBuilder();

        goUrl = stringInfo.append(appString) + "&";
        return goUrl;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
