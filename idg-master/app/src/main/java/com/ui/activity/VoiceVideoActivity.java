package com.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chaoxiang.base.utils.SDLogUtil;
import com.cxgz.activity.cx.base.BaseActivity;
import com.cxgz.activity.cxim.support.handle.AudioVoiceRecordHandler;
import com.entity.VoiceEntity;
import com.chaoxiang.base.config.Constants;
import com.injoy.idg.R;
import com.utils.CachePath;
import com.utils.CommonUtils;
import com.utils.FileUtil;
import com.utils.SDToast;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author zjh
 */
public class VoiceVideoActivity extends BaseActivity {
    private ImageView micImage;
    private View buttonPressToSpeak;
    public static final String RESULT = "result";

    public static Handler micImageHandler = null;
    private AudioVoiceRecordHandler audioRecorderInstance = null;
    private Dialog soundVolumeDialog = null;
    private LinearLayout soundVolumeLayout = null;
    private File audioSaveFile;
    private Thread audioRecorderThread = null;
    private Timer timer = new Timer();

    private RelativeLayout voice_r_ll;

    /**
     * @param voiceValue
     * @Description 根据分贝值设置录音时的音量动画
     */
    private void onReceiveMaxVolume(int voiceValue) {
        if (voiceValue < 200.0) {
            micImage.setImageResource(R.mipmap.tt_sound_volume_01);
        } else if (voiceValue > 200.0 && voiceValue < 600) {
            micImage.setImageResource(R.mipmap.tt_sound_volume_02);
        } else if (voiceValue > 600.0 && voiceValue < 1200) {
            micImage.setImageResource(R.mipmap.tt_sound_volume_03);
        } else if (voiceValue > 1200.0 && voiceValue < 2400) {
            micImage.setImageResource(R.mipmap.tt_sound_volume_04);
        } else if (voiceValue > 2400.0 && voiceValue < 10000) {
            micImage.setImageResource(R.mipmap.tt_sound_volume_05);
        } else if (voiceValue > 10000.0 && voiceValue < 28000.0) {
            micImage.setImageResource(R.mipmap.tt_sound_volume_06);
        } else if (voiceValue > 28000.0) {
            micImage.setImageResource(R.mipmap.tt_sound_volume_07);
        }
    }

    public static Handler getUiHandler() {
        return micImageHandler;
    }

    @Override
    protected void init() {
//        tvTitle.setText("录音");
//        setLeftBack(R.drawable.folder_back);
        micImage = (ImageView) findViewById(R.id.mic_image);
        buttonPressToSpeak = findViewById(R.id.btn_press_to_speak);
        voice_r_ll = (RelativeLayout) findViewById(R.id.voice_r_ll);
        voice_r_ll.getBackground().setAlpha(150);
        voice_r_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        buttonPressToSpeak.setOnTouchListener(new PressToSpeakListen());
        initHandle();
        initSoundVolumeDlg();
    }

    /**
     * @Description 初始化音量对话框
     */
    private void initSoundVolumeDlg() {
        soundVolumeDialog = new Dialog(this, R.style.SoundVolumeStyle);
        soundVolumeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        soundVolumeDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        soundVolumeDialog.setContentView(R.layout.sd_sound_volume_dialog);
        soundVolumeDialog.setCanceledOnTouchOutside(true);
        micImage = (ImageView) soundVolumeDialog.findViewById(R.id.sound_volume_img);
        soundVolumeLayout = (LinearLayout) soundVolumeDialog.findViewById(R.id.sound_volume_bk);
    }

    private void initHandle() {
        micImageHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                // 切换msg切换图片
//			micImage.setImageDrawable(micImages[msg.what]);
                switch (msg.what) {
                    case Constants.RECEIVE_MAX_VOLUME:
                        onReceiveMaxVolume((Integer) msg.obj);
                        break;
                    case Constants.RECORD_AUDIO_TOO_LONG:
                        doFinishRecordAudio();
                        break;
                }
            }
        };
    }

    /**
     * @Description 录音超时，发消息调用该方法
     */
    public void doFinishRecordAudio() {
        try {
            if (audioRecorderInstance.isRecording()) {
                audioRecorderInstance.setRecording(false);
            }
            if (soundVolumeDialog.isShowing()) {
                soundVolumeDialog.dismiss();
            }

            buttonPressToSpeak.setPressed(false);
            audioRecorderInstance.setRecordTime(Constants.MAX_SOUND_RECORD_TIME);
            onRecordVoiceEnd(Constants.MAX_SOUND_RECORD_TIME);
        } catch (Exception e) {
        }
    }

    /**
     * @param audioLen
     * @Description 录音结束后处理录音数据
     */
    private void onRecordVoiceEnd(float audioLen) {}

    @Override
    protected int getContentLayout() {
        return R.layout.sd_workcircle_voice_activity;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(null!=audioRecorderInstance){//设置不在录音
            audioRecorderInstance.setRecording(false);
        }
    }

    /**
     * 按住说话listener
     */
    class PressToSpeakListen implements View.OnTouchListener {
        @SuppressLint("Wakelock")
        @SuppressWarnings("unused")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!CommonUtils.isExitsSdcard()) {
                        String st4 = getResources().getString(R.string.Send_voice_need_sdcard_support);
                        SDToast.showShort(st4);
                        return false;
                    }
                    try {
                        v.setPressed(true);
                        soundVolumeLayout.setBackgroundResource(R.mipmap.tt_sound_volume_default_bk);
//						if (VoicePlayClickListener.isPlaying) {
//							VoicePlayClickListener.currentPlayListener.stopPlayVoice();
//						}
                        startRecoder();
                    } catch (Exception e) {
                        e.printStackTrace();
                        v.setPressed(false);
                        SDToast.showShort(R.string.recoding_fail);
                        return false;
                    }

                    return true;
                case MotionEvent.ACTION_MOVE: {
                    if (event.getY() < 0) {
                        soundVolumeLayout.setBackgroundResource(R.mipmap.tt_sound_volume_cancel_bk);
                    } else {
                        soundVolumeLayout.setBackgroundResource(R.mipmap.tt_sound_volume_default_bk);
                    }
                    return true;
                }
                case MotionEvent.ACTION_UP:
                    v.setPressed(false);
                    if (audioRecorderInstance.isRecording()) {
                        audioRecorderInstance.setRecording(false);
                    }
                    if (event.getY() < 0) {
                        micImage.setVisibility(View.GONE);
                        timer.schedule(new TimerTask() {
                            public void run() {
                                if (soundVolumeDialog.isShowing())
                                    soundVolumeDialog.dismiss();
                                this.cancel();
                            }
                        }, 300);
                    } else {
                        // stop recording and send voice file
                        try {
                            int length = (int) audioRecorderInstance.getRecordTime();
                            if (length > 0) {
                                //语音长度大于0
                                SDLogUtil.syso("length=" + length);
                                SDLogUtil.syso("filePath=" + audioSaveFile.getAbsolutePath());
                                if (audioSaveFile == null) {
                                    setResult(RESULT_CANCELED);
                                } else {
                                    Intent data = new Intent();
                                    VoiceEntity voiceEntity = new VoiceEntity(audioSaveFile.getAbsolutePath(),
                                            audioSaveFile.getName(),
                                            length);
                                    data.putExtra(RESULT, voiceEntity);
                                    setResult(RESULT_OK, data);
                                }
                                finish();
                            } else {
                                soundVolumeLayout.setBackgroundResource(R.mipmap.tt_sound_volume_short_tip_bk);
                                timer.schedule(new TimerTask() {
                                    public void run() {
                                        if (soundVolumeDialog.isShowing())
                                            soundVolumeDialog.dismiss();
                                        this.cancel();
                                    }
                                }, 300);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            SDToast.showShort(getResources().getString(R.string.recording_error));
                        }

                    }
            }
            return true;
        }
    }

    private void startRecoder() {
        micImage.setImageResource(R.mipmap.tt_sound_volume_01);
        micImage.setVisibility(View.VISIBLE);
        soundVolumeLayout.setBackgroundResource(R.mipmap.tt_sound_volume_default_bk);
        soundVolumeDialog.show();
        audioSaveFile = new File(getSaveDir(), userName + "_" + System.currentTimeMillis() + ".spx");

        audioRecorderInstance = new AudioVoiceRecordHandler(audioSaveFile.getAbsolutePath());
        audioRecorderThread = new Thread(audioRecorderInstance);
        audioRecorderInstance.setRecording(true);
        audioRecorderThread.start();
    }

    public File getSaveDir() {
        File saveDir = new File(FileUtil.getSDFolder(), CachePath.VOICE_CACHE);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        return saveDir;
    }


}
