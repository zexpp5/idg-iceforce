package com.cxgz.activity.superqq.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.cxgz.activity.db.help.SDImgHelper;
import com.cxgz.activity.superqq.activity.InviteActivity;
import com.cxgz.activity.superqq.activity.SDPersonLoadingAddressActivity;
import com.entity.SDDraftEntity;
import com.entity.SDFileListEntity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.superdata.im.constants.CxSPIMKey;
import com.superdata.marketing.view.percent.PercentRelativeLayout;
import com.ui.SDLoginActivity;
import com.utils.CachePath;
import com.utils.FileUtil;
import com.utils.SPUtils;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tablayout.view.textview.FontTextView;



/**
 * 超信-我-界面
 */
public class SuperMineFragment extends BaseFragment
{
    private static final String TAG = "SuperMeFragment";

    String imAccount = "";
    @Bind(R.id.iv_head)
    SimpleDraweeView ivHead;
    @Bind(R.id.ll_erweima)
    LinearLayout llErweima;
    @Bind(R.id.ll_mine_info_detail)
    LinearLayout llMineInfoDetail;
    @Bind(R.id.rl_msg_notification)
    RelativeLayout rlMsgNotification;
    @Bind(R.id.rl_msg_voice)
    RelativeLayout rlMsgVoice;
    @Bind(R.id.rl_msg_vibrate)
    RelativeLayout rlMsgVibrate;
    @Bind(R.id.rl_speaker_play_voice)
    RelativeLayout rlSpeakerPlayVoice;
    @Bind(R.id.rl_location)
    RelativeLayout rlLocation;
    @Bind(R.id.rl_setting_version_update)
    RelativeLayout rlSettingVersionUpdate;
    @Bind(R.id.rl_send_client_download)
    RelativeLayout rlSendClientDownload;
    @Bind(R.id.rl_clear_cache)
    RelativeLayout rlClearCache;

    @Bind(R.id.ll_my_quit)
    LinearLayout llMyQuit;
    @Bind(R.id.tv_department_type)
    FontTextView tvDepartmentType;
    @Bind(R.id.tv_nickName)
    FontTextView tvNickName;
    @Bind(R.id.tvClearCache)
    FontTextView tvClearCache;
    @Bind(R.id.cb_location)
    CheckBox cbLocation;
    @Bind(R.id.cb_msg_notification)
    CheckBox cbMsgNotification;
    @Bind(R.id.cb_msg_voice)
    CheckBox cbMsgVoice;
    @Bind(R.id.cb_msg_vibrate)
    CheckBox cbMsgVibrate;
    @Bind(R.id.toHideLine)
    View toHideLine;
    @Bind(R.id.cb_speaker_play_voice)
    CheckBox cbSpeakerPlayVoice;
    @Bind(R.id.ll_msg_notifycation)
    LinearLayout llMsgNotifycation;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.top_title_ll)
    PercentRelativeLayout topTitleLl;

    @Override
    protected int getContentLayout()
    {
        return R.layout.super_qq_tab;
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

        addRightBtn2(R.mipmap.menu_add, new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                MainTopMenuUtils.getInstance(getActivity()).showMenu(view, "1");
            }
        });

        addLeftBtn(R.mipmap.icon_public_back, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().finish();
            }
        });

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
        toolbar.setBackgroundColor(getResources().getColor(R.color.top_bg2));
        topTitleLl.setBackgroundColor(getResources().getColor(R.color.top_bg2));

        userId = (String) SPUtils.get(getActivity(), SPUtils.USER_ID, "");
        imAccount = (String) SPUtils.get(getActivity(), SPUtils.IM_NAME, "").toString();

        //各种配置
        setCache();

        initSettingStatus();
    }

    private void setMineInfo()
    {
        ImHttpHelper.findPersonInfoByImAccount(getActivity(), false, mHttpHelper, imAccount, new SDRequestCallBack
                (PersonalListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
//                MyToast.showToast(getActivity(), "刷新个人信息失败！");
                bindView();
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                PersonalListBean personalListBean = (PersonalListBean) responseInfo.getResult();
                SDImgHelper.getInstance(getActivity()).loadSmallImg(personalListBean.getData().getIcon(), R.mipmap
                        .temp_user_head, ivHead);
                tvNickName.setText(personalListBean.getData().getName());
                SPUtils.put(getActivity(), SPUtils.USER_ICON, personalListBean.getData().getIcon());
                SPUtils.put(getActivity(), SPUtils.USER_NAME, personalListBean.getData().getName());

            }
        });
    }

    private void bindView()
    {
        userId = (String) SPUtils.get(getActivity(), SPUtils.USER_ID, "-1");
        SDUserEntity user = mUserDao.findUserByUserID(userId);
        String nick_name;
        if (user != null)
        {
            SDImgHelper.getInstance(getActivity()).loadSmallImg(user.getIcon(), R.mipmap.temp_user_head, ivHead);
            nick_name = user.getName();
        } else
        {
            nick_name = (String) SPUtils.get(getActivity(), SPUtils.USER_NAME, "");
        }
        tvNickName.setText(nick_name);
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

    @OnClick({R.id.ll_erweima, R.id.ll_mine_info_detail, R.id.rl_msg_notification, R.id.rl_msg_voice, R.id.rl_msg_vibrate, R.id
            .rl_speaker_play_voice, R.id.rl_location, R.id.rl_setting_version_update, R.id.rl_send_client_download, R.id
            .rl_clear_cache, R.id.ll_my_quit})
    public void onViewClicked(View view)
    {
        Bundle bundle = null;

        switch (view.getId())
        {
            case R.id.ll_erweima:
//                if (StringUtils.notEmpty(getParams()))
//                {
//                    Intent intent = new Intent(getActivity(), ErweimaActivity.class);
//                    intent.putExtra("text", getParams());
//                    startActivity(intent);
//                }
                getActivity().startActivity(new Intent(getActivity(), InviteActivity.class));

                break;

            case R.id.ll_mine_info_detail:
                if (TextUtils.isEmpty(imAccount))
                {
                    return;
                }
                bundle = new Bundle();
                bundle.putString(SPUtils.USER_ID, imAccount);
                openActivity(SDPersonalAlterActivity.class, bundle);
                break;

            //通知
            case R.id.rl_msg_notification:
                if (cbMsgNotification.isChecked())
                {
                    SPUtils.put(getActivity(), "notificationIsCheck", false);
                    cbMsgNotification.setChecked(false);
                    /**
                     * 隐藏声音与震动
                     */
                    llMsgNotifycation.setVisibility(View.GONE);
                    rlMsgVoice.setVisibility(View.GONE);
                    rlMsgVibrate.setVisibility(View.GONE);
                    toHideLine.setVisibility(View.GONE);

                } else
                {
                    SPUtils.put(getActivity(), "notificationIsCheck", true);
                    cbMsgNotification.setChecked(true);
                    /**
                     * 显示声音与震动
                     */
                    llMsgNotifycation.setVisibility(View.VISIBLE);
                    rlMsgVoice.setVisibility(View.VISIBLE);
                    rlMsgVibrate.setVisibility(View.VISIBLE);
                    toHideLine.setVisibility(View.VISIBLE);
                }

                break;

            //声音
            case R.id.rl_msg_voice:

                if (cbMsgVoice.isChecked())
                {
                    com.chaoxiang.base.utils.SPUtils.put(getActivity(), com.chaoxiang.base.utils.SPUtils.VOICE_ISCHECK, false);
                    cbMsgVoice.setChecked(false);
                } else
                {
                    com.chaoxiang.base.utils.SPUtils.put(getActivity(), com.chaoxiang.base.utils.SPUtils.VOICE_ISCHECK, true);
                    cbMsgVoice.setChecked(true);
                }

                break;

            //震动
            case R.id.rl_msg_vibrate:

                if (cbMsgVibrate.isChecked())
                {
                    com.chaoxiang.base.utils.SPUtils.put(getActivity(), com.chaoxiang.base.utils.SPUtils.VIBRATOR_ISCHECK, false);
                    cbMsgVibrate.setChecked(false);
                } else
                {
                    com.chaoxiang.base.utils.SPUtils.put(getActivity(), com.chaoxiang.base.utils.SPUtils.VIBRATOR_ISCHECK, true);
                    cbMsgVibrate.setChecked(true);
                }


                break;

            //使用扬声器播放音乐
            case R.id.rl_speaker_play_voice:

                if (cbSpeakerPlayVoice.isChecked())
                {
                    SPUtils.put(getActivity(), "speakerIsCheck", false);
                    cbSpeakerPlayVoice.setChecked(false);
                } else
                {
                    SPUtils.put(getActivity(), "speakerIsCheck", true);
                    cbSpeakerPlayVoice.setChecked(true);
                }

                break;
            case R.id.rl_location:

                if (cbLocation.isChecked())
                {
                    SPUtils.put(getActivity(), SPUtils.SETTINGS_POS, 0);
                    cbLocation.setChecked(false);
                } else
                {
                    SPUtils.put(getActivity(), SPUtils.SETTINGS_POS, 1);
                    cbLocation.setChecked(true);
                }

                break;
            case R.id.rl_setting_version_update:

                break;
            case R.id.rl_send_client_download:

                Intent intent = new Intent();
                intent.setClass(getActivity(), SDPersonLoadingAddressActivity.class);
                startActivity(intent);

                break;
            case R.id.rl_clear_cache:

                AlertDialog.Builder builder = new AlertDialog.Builder(
                        getActivity());
                builder.setMessage(getString(R.string.if_delete_cache));
                builder.setCancelable(true);
                builder.setPositiveButton("是",
                        new DialogInterface.OnClickListener()
                        {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                delefile();
                                setCache();
                                clearnDraftData();
                            }
                        });
                builder.setNegativeButton("否",
                        new DialogInterface.OnClickListener()
                        {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                            }
                        });
                builder.create().show();
                break;
            case R.id.ll_my_quit:
                logoutDialog();
                break;
        }
    }

    String sdCachePath = Environment.getExternalStorageDirectory()
            + File.separator + CachePath.CACHE + File.separator;

    private void setCache()
    {
        File file = new File(sdCachePath);
        String size = "0";
        try
        {
            size = FileUtil.formetFileSize(FileUtil.getFolderSize(file));
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        tvClearCache.setText(getString(R.string.delete_cache) + "(" + size + ")");
    }

    /**
     * 删除本地缓存
     */
    private void delefile()
    {
        File f1 = new File(sdCachePath);
        FileUtil.deleteFile(f1);
        Fresco.getImagePipeline().clearCaches();
        try
        {
            mDbUtils.deleteAll(SDFileListEntity.class);
        } catch (DbException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * 清除数据库草稿箱数据(被清除的草稿箱数据)
     */
    private void clearnDraftData()
    {
        try
        {
            mDbUtils.delete(SDDraftEntity.class, WhereBuilder.b("flag", "!=", 1));
        } catch (DbException e)
        {
            e.printStackTrace();
        }
    }

    private boolean notificationIsCheck;
    private boolean voiceIsCheck;
    private boolean vibrateIsCheck;
    private boolean speakerIsCheck;

    private void initSettingStatus()
    {
        notificationIsCheck = (boolean) SPUtils.get(getActivity(), "notificationIsCheck", true);
        cbMsgNotification.setChecked(notificationIsCheck);
        if (notificationIsCheck)
        {
            llMsgNotifycation.setVisibility(View.VISIBLE);
            rlMsgVoice.setVisibility(View.VISIBLE);
            rlMsgVibrate.setVisibility(View.VISIBLE);
        }
        voiceIsCheck = (boolean) com.chaoxiang.base.utils.SPUtils.get(getActivity(), com.chaoxiang.base.utils.SPUtils
                .VOICE_ISCHECK, true);
        cbMsgVoice.setChecked(voiceIsCheck);
        vibrateIsCheck = (boolean) com.chaoxiang.base.utils.SPUtils.get(getActivity(), com.chaoxiang.base.utils.SPUtils
                .VIBRATOR_ISCHECK, true);
        cbMsgVibrate.setChecked(vibrateIsCheck);
        speakerIsCheck = (boolean) SPUtils.get(getActivity(), "speakerIsCheck", true);
        cbSpeakerPlayVoice.setChecked(speakerIsCheck);

        cbLocation.setChecked(Integer.parseInt(SPUtils.get(getActivity(), SPUtils.SETTINGS_POS, 0).toString()) == 0 ? false :
                true);
    }
}
