package com.cxgz.activity.superqq.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.entity.SDDraftEntity;
import com.entity.SDFileListEntity;
import com.entity.update.UpdateEntity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.superdata.marketing.view.percent.PercentRelativeLayout;
import com.utils.AppUtils;
import com.utils.CachePath;
import com.utils.FileUtil;
import com.utils.SPUtils;

import java.io.File;

import yunjing.http.BaseHttpHelper;
import com.view.LoginOutDialog;


public class SettingsActivitiy extends BaseActivity implements OnClickListener
{
    public final static String VERSION_UPDATE_ACTION = "version_update";
    private TextView tvClearCache, tvLoading, tvAbout, tvWorkReport, tvWorkTask, tvBusiness;
    private RelativeLayout rlMsgNotification, rlMsgVoice, rlMsgVibrate, rlSpeakerPaly, rlLocation, rl_setting_version_update;
    private CheckBox cbMsgNotification, cbMsgVoice, cbMsgVibrate, cbSpeakerPaly, cbLocation;
    private LinearLayout msgNotifycation;
    private PercentRelativeLayout login_out_ll;
    //    private EMChatOptions chatOptions;
    private boolean notificationIsCheck;
    private boolean voiceIsCheck;
    private boolean vibrateIsCheck;
    private boolean speakerIsCheck;
    private View toHideLine;//点击消息通知开关隐藏该View

    @Override
    protected void init()
    {
        setLeftBack(R.drawable.folder_back);
        setTitle("设置");
        initView();
        setCache();
        initSettingStatus();
        setBarBackGround(0xff343440);
        mCurrentVersionCode = AppUtils.getVersionCode(this);
    }

    private void initSettingStatus()
    {
        notificationIsCheck = (boolean) SPUtils.get(this, "notificationIsCheck", true);
        cbMsgNotification.setChecked(notificationIsCheck);
        if (notificationIsCheck)
        {
            msgNotifycation.setVisibility(View.VISIBLE);
            rlMsgVoice.setVisibility(View.VISIBLE);
            rlMsgVibrate.setVisibility(View.VISIBLE);
        }
        voiceIsCheck = (boolean) com.chaoxiang.base.utils.SPUtils.get(this, com.chaoxiang.base.utils.SPUtils.VOICE_ISCHECK, true);
        cbMsgVoice.setChecked(voiceIsCheck);
        vibrateIsCheck =  (boolean) com.chaoxiang.base.utils.SPUtils.get(this, com.chaoxiang.base.utils.SPUtils.VIBRATOR_ISCHECK, true);
        cbMsgVibrate.setChecked(vibrateIsCheck);
        speakerIsCheck = (boolean) SPUtils.get(this, "speakerIsCheck", true);
        cbSpeakerPaly.setChecked(speakerIsCheck);

        cbLocation.setChecked(Integer.parseInt(SPUtils.get(this, SPUtils.SETTINGS_POS, 0).toString()) == 0 ? false : true);
    }

    private void initView()
    {
        msgNotifycation = (LinearLayout) findViewById(R.id.ll_msg_notifycation);
        tvWorkReport = (TextView) findViewById(R.id.tv_setting_workreport);
        tvWorkReport.setOnClickListener(this);
        tvWorkTask = (TextView) findViewById(R.id.tv_setting_task);
        tvWorkTask.setOnClickListener(this);
        tvBusiness = (TextView) findViewById(R.id.tv_setting_business);
        tvBusiness.setOnClickListener(this);
        tvClearCache = (TextView) findViewById(R.id.tvClearCache);
        tvClearCache.setOnClickListener(this);
        tvLoading = (TextView) findViewById(R.id.tvLoading);
        tvLoading.setOnClickListener(this);
        rl_setting_version_update = (RelativeLayout) findViewById(R.id.rl_setting_version_update);
        rl_setting_version_update.setOnClickListener(this);
        rlMsgNotification = (RelativeLayout) findViewById(R.id.rl_msg_notification);
        rlMsgNotification.setOnClickListener(this);
        rlMsgVoice = (RelativeLayout) findViewById(R.id.rl_msg_voice);
        rlMsgVoice.setOnClickListener(this);
        rlMsgVibrate = (RelativeLayout) findViewById(R.id.rl_msg_vibrate);
        rlMsgVibrate.setOnClickListener(this);
        rlSpeakerPaly = (RelativeLayout) findViewById(R.id.rl_speaker_play_voice);
        rlSpeakerPaly.setOnClickListener(this);
        rlLocation = (RelativeLayout) findViewById(R.id.rl_location);
        rlLocation.setOnClickListener(this);
        cbMsgNotification = (CheckBox) findViewById(R.id.cb_msg_notification);
        cbMsgVoice = (CheckBox) findViewById(R.id.cb_msg_voice);
        cbMsgVibrate = (CheckBox) findViewById(R.id.cb_msg_vibrate);
        cbSpeakerPaly = (CheckBox) findViewById(R.id.cb_speaker_play_voice);
        cbLocation = (CheckBox) findViewById(R.id.cb_location);
        toHideLine = findViewById(R.id.toHideLine);//点击消息通知开关隐藏View

        login_out_ll = (PercentRelativeLayout) findViewById(R.id.login_out_ll);
        login_out_ll.setOnClickListener(this);

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

    @Override
    public void onClick(View view)
    {
        Intent intent = new Intent();
        boolean isCheck = false;
        switch (view.getId())
        {
//            case R.id.tvAbout:
//                intent.setClass(SettingsActivitiy.this, SDAboutActivity.class);
//                startActivity(intent);
//                break;
            case R.id.tvClearCache:
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        SettingsActivitiy.this);
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
            //客户端下载
            case R.id.tvLoading:
                intent.setClass(SettingsActivitiy.this, SDPersonLoadingAddressActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_setting_version_update:
                getUpdate();
                break;
//            case R.id.tv_setting_workreport://工作报告
//                intent.setClass(SettingsActivitiy.this, SDSetApproveActivity.class);
//                intent.putExtra("approveType", SPUtils.REPORTAPPROVALS);
//                startActivity(intent);
//                break;
//            case R.id.tv_setting_business://事务审批
//                intent.setClass(SettingsActivitiy.this, SDSetApproveActivity.class);
//                intent.putExtra("approveType", SPUtils.APPROVALAPPROVALS);
//                startActivity(intent);
//                break;
//            case R.id.tv_setting_task://工作任务
//                intent.setClass(SettingsActivitiy.this, SetTaskApproveActivity.class);
//                startActivity(intent);
//                break;
            case R.id.rl_location://定位
                if (cbLocation.isChecked())
                {
                    SPUtils.put(SettingsActivitiy.this, SPUtils.SETTINGS_POS, 0);
                    cbLocation.setChecked(false);
                } else
                {
                    SPUtils.put(SettingsActivitiy.this, SPUtils.SETTINGS_POS, 1);
                    cbLocation.setChecked(true);
                }
                break;
            case R.id.rl_msg_notification://消息通知
                if (cbMsgNotification.isChecked())
                {
                    SPUtils.put(SettingsActivitiy.this, "notificationIsCheck", false);
                    cbMsgNotification.setChecked(false);
                    /**
                     * 隐藏声音与震动
                     */
                    rlMsgVoice.setVisibility(View.GONE);
                    rlMsgVibrate.setVisibility(View.GONE);
                    toHideLine.setVisibility(View.GONE);

                } else
                {
                    SPUtils.put(SettingsActivitiy.this, "notificationIsCheck", true);
                    cbMsgNotification.setChecked(true);
                    /**
                     * 显示声音与震动
                     */
                    rlMsgVoice.setVisibility(View.VISIBLE);
                    rlMsgVibrate.setVisibility(View.VISIBLE);
                    toHideLine.setVisibility(View.VISIBLE);
                }

                break;

            case R.id.rl_msg_voice://声音
                if (cbMsgVoice.isChecked())
                {
                    com.chaoxiang.base.utils.SPUtils.put(SettingsActivitiy.this, com.chaoxiang.base.utils.SPUtils.VOICE_ISCHECK, false);
                    cbMsgVoice.setChecked(false);
                } else
                {
                    com.chaoxiang.base.utils.SPUtils.put(SettingsActivitiy.this, com.chaoxiang.base.utils.SPUtils.VOICE_ISCHECK, true);
                    cbMsgVoice.setChecked(true);
                }
                break;
            case R.id.rl_msg_vibrate://震动
                if (cbMsgVibrate.isChecked())
                {
                    com.chaoxiang.base.utils.SPUtils.put(SettingsActivitiy.this, com.chaoxiang.base.utils.SPUtils.VIBRATOR_ISCHECK, false);
                    cbMsgVibrate.setChecked(false);
                } else
                {
                    com.chaoxiang.base.utils.SPUtils.put(SettingsActivitiy.this, com.chaoxiang.base.utils.SPUtils.VIBRATOR_ISCHECK, true);
                    cbMsgVibrate.setChecked(true);
                }
                break;
            case R.id.rl_speaker_play_voice:
                if (cbSpeakerPaly.isChecked())
                {
                    SPUtils.put(SettingsActivitiy.this, "speakerIsCheck", false);
                    cbSpeakerPaly.setChecked(false);
                } else
                {
                    SPUtils.put(SettingsActivitiy.this, "speakerIsCheck", true);
                    cbSpeakerPaly.setChecked(true);
                }
                break;
            case R.id.login_out_ll:

                logoutDialog();

                break;
        }
    }

    public void logoutDialog()
    {
        LoginOutDialog.logoutDialog(SettingsActivitiy.this, new LoginOutDialog.loginOutListener()
        {
            @Override
            public void setTrue()
            {
                LoginOutDialog.logout(SettingsActivitiy.this);
            }

            @Override
            public void setCancle()
            {

            }
        });
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

    @Override
    protected int getContentLayout()
    {
        return R.layout.sd_person_settings;
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

    private int mCurrentVersionCode;//当前版本


    private void getUpdate()
    {
        BaseHttpHelper.updateInfo(mHttpHelper, new SDRequestCallBack(UpdateEntity.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(SettingsActivitiy.this, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                UpdateEntity entity = (UpdateEntity) responseInfo.result;
                if (entity.getData().getIsOpen().equals("true"))
                {
                    if (entity != null)
                    {
                        if (mCurrentVersionCode < Integer.parseInt(entity.getData().getVersionCode()))
                        {
                            showAddFriendDialog(entity);
                        } else
                        {
                            MyToast.showToast(SettingsActivitiy.this, "已经是最新版本！");
                        }
                    }
                } else
                {
                    MyToast.showToast(SettingsActivitiy.this, "已经是最新版本！");
                }
            }
        });
    }

    private void showAddFriendDialog(final UpdateEntity entity)
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View contentView = LayoutInflater.from(this).inflate(R.layout.update_info_show, null);
        dialog.setContentView(contentView);
        dialog.setCanceledOnTouchOutside(false);
        TextView tv_open = (TextView) contentView.findViewById(R.id.tv_open);
        TextView tv_cancel = (TextView) contentView.findViewById(R.id.tv_cancel);
        TextView et_verify_info = (TextView) contentView.findViewById(R.id.et_verify_info);
        et_verify_info.setText(entity.getData().getDescription());
        tv_open.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                dialog.dismiss();
                if (StringUtils.notEmpty(entity.getData().getUrlLink()))
                {
                    download(entity);
                } else
                {
                    MyToast.showToast(SettingsActivitiy.this, "打开出错，请稍候再试!");
                }
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //强制更新
                if (entity.getData().getStatus().equals("1"))
                {
                    SDLogUtil.debug("版本-强制更新");
                    //清除
                    dialog.dismiss();
                    System.exit(0);
                }
                //普通更新
                else if (entity.getData().getStatus().equals("2"))
                {
                    SDLogUtil.debug("版本- 普通更新");
                    dialog.dismiss();
                }

            }
        });
        dialog.show();
    }

    public void download(UpdateEntity entity)
    {
        SDLogUtil.debug("版本-下载链接:" + entity.getData().getUrlLink());
        Uri uri = Uri.parse(entity.getData().getUrlLink().toString().trim());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        SettingsActivitiy.this.startActivity(intent);
    }
}
