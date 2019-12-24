package com.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.cxgz.activity.cxim.person.NotFriendPersonalInfoActivity;
import com.cxgz.activity.cxim.PersonalInfoActivity;
import com.cxgz.activity.db.dao.SDAllConstactsDao;
import com.cxgz.activity.db.dao.SDAllConstactsEntity;
import com.injoy.idg.R;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.cxgz.activity.db.dao.SDUserDao;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.chaoxiang.base.config.Constants;
import com.ui.http.BasicDataHttpHelper;
import com.utils.SPUtils;

import java.io.IOException;
import java.util.Vector;

import tablayout.utils.camera.CameraManager;
import tablayout.utils.decoding.CaptureActivityHandler;
import tablayout.utils.decoding.InactivityTimer;
import tablayout.view.zxing.ViewfinderView;

/**
 * 二维码扫描
 */
public class CaptureActivity extends BaseActivity implements Callback
{
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;

    private TextView tvtTopTitle;

    @Override
    protected void init()
    {
        setLeftBack(R.drawable.folder_back);

        setTitle("扫一扫");

        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.twodimcode;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface)
        {
            initCamera(surfaceHolder);
        } else
        {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = false;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL)
        {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (handler != null)
        {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy()
    {
        inactivityTimer.shutdown();
        super.onDestroy();
    }


    private void initCamera(SurfaceHolder surfaceHolder)
    {
        try
        {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe)
        {
            return;
        } catch (RuntimeException e)
        {
            return;
        }
        if (handler == null)
        {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height)
    {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        if (!hasSurface)
        {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView()
    {
        return viewfinderView;
    }

    public Handler getHandler()
    {
        return handler;
    }

    public void drawViewfinder()
    {
        viewfinderView.drawViewfinder();

    }

    public void handleDecode(Result obj, Bitmap barcode)
    {
        inactivityTimer.onActivity();
        viewfinderView.drawResultBitmap(barcode);

        playBeepSoundAndVibrate();

        String str = obj.getText();
//        System.out.println("lalalla:" + str);
        SDLogUtil.debug("二维码的扫描结果:" + str);
//        Intent it = new Intent(CaptureActivity.this, WorkCircleMainActivity.class);
//        it.putExtra("result", str);
//        setResult(1, it);
//        finish();


        //控制判断
        String addFriend = "cx:injoy365.cn";
        String openOurCompanyDownLoad = Constants.API_SERVER_URL + "/register/bsRegister.htm?versionCode=5";
        String outUrlStringFlag = "http";

        //年会签到
        String annualMeeting = "cx:idgMeetId.cn";

        if (StringUtils.containsString(str, addFriend))
        {
            String[] position = str.split("&");
            //对方的信息-二维码
            String tmpUserId = position[0];
            String tmpUserImAccount = position[1];
            String tmpUserName = position[2];
            String tmpUserTag = position[3];

            if (judgeIsFriend(String.valueOf(tmpUserImAccount)))
            {
                Intent intent = new Intent(this, PersonalInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(SPUtils.USER_ID, tmpUserImAccount);
                intent.putExtras(bundle);
                startActivity(intent);
            } else
            {
                //加好友！
                SDLogUtil.debug("二维码-加好友:" + str);
                Intent intent = new Intent(this, NotFriendPersonalInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(SPUtils.USER_ACCOUNT, str);
                intent.putExtras(bundle);
                startActivity(intent);
            }

        } else if (StringUtils.containsString(str, openOurCompanyDownLoad))
        {
            //跳转网址
            SDLogUtil.debug("二维码-应用下载链接:" + str);
            Uri uri = Uri.parse(openOurCompanyDownLoad);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else if (StringUtils.containsString(str, outUrlStringFlag))
        {

            showAddDialog(str);
            SDLogUtil.debug("二维码-外部链接:" + str);
        } else if (StringUtils.containsString(str, annualMeeting))
        {
            //年会签到
            String[] position = str.split("&");
            //对方的信息-二维码
            String tmpMeetId = position[0];
            String tmpTah = position[1];
            if (StringUtils.notEmpty(tmpMeetId))
            {
                BasicDataHttpHelper.postRegister(CaptureActivity.this, tmpMeetId);
            }
        } else
        {
//            最后一种直接返回的
            Intent intent = new Intent();
            intent.putExtra("data", str);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }

    }

    private boolean judgeIsFriend(String imAccount)
    {
        boolean isExist = false;

        SDUserDao mUserDao = new SDUserDao(this);
//        SDUserEntity userInfo = mUserDao.findUserByImAccount(imAccount);
        //替换全局的通讯录
        SDAllConstactsEntity userInfo = SDAllConstactsDao.getInstance()
                .findAllConstactsByImAccount(imAccount);
        if (StringUtils.notEmpty(userInfo))
            isExist = true;

        return isExist;
    }

    private void showAddDialog(final String outUrlString)
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View contentView = LayoutInflater.from(this).inflate(R.layout.prompt_info_url, null);
        dialog.setContentView(contentView);
        dialog.setCanceledOnTouchOutside(true);

        //打开链接
        TextView tv_open = (TextView) contentView.findViewById(R.id.tv_open);
        TextView tv_cancel = (TextView) contentView.findViewById(R.id.tv_cancel);

        TextView tv_out_url = (TextView) contentView.findViewById(R.id.tv_out_url);
        tv_out_url.setText(outUrlString);
        //打开链接！
        tv_open.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //完成
                Uri uri = Uri.parse(outUrlString);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

                dialog.dismiss();

                CaptureActivity.this.finish();

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


//    private void showAddDialog2(final String outUrlString)
//    {
//        final Dialog dialog = new Dialog(this);
//        View contentView = LayoutInflater.from(this).inflate(R.layout_city.prompt_info_url, null);
//        dialog.setContentView(contentView);
//        dialog.setCanceledOnTouchOutside(true);
//
//        //打开链接
//        TextView tv_open = (TextView) contentView.findViewById(R.id.tv_open);
//        TextView tv_cancel = (TextView) contentView.findViewById(R.id.tv_cancel);
//
//        TextView tv_out_url = (TextView) contentView.findViewById(R.id.tv_out_url);
//        tv_out_url.setText(outUrlString);
//        //打开链接！
//        tv_open.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                //完成
//                Uri uri = Uri.parse(outUrlString);
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
//
//                dialog.dismiss();
//
//                CaptureActivity.this.finish();
//
//            }
//        });
//
//        tv_cancel.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                //清除
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
//    }


    private void initBeepSound()
    {
        if (playBeep && mediaPlayer == null)
        {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try
            {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e)
            {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate()
    {
        if (playBeep && mediaPlayer != null)
        {
            mediaPlayer.start();
        }
        if (vibrate)
        {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener()
    {
        public void onCompletion(MediaPlayer mediaPlayer)
        {
            mediaPlayer.seekTo(0);
        }
    };

}