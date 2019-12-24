package com.cxgz.activity.cxim.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.injoy.idg.R;
import com.cxgz.activity.cxim.manager.AudioRecordManager;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-01-25
 * @desc
 */
public class VoiceRecordDialog extends Dialog
{
    private ImageView micImage;
    private LinearLayout soundVolumeLayout;
    private AudioRecordManager audioRecordManager;


    public VoiceRecordDialog(Context context, VoiceRecordDialogListener voiceRecordListener)
    {
        super(context, R.style.SoundVolumeStyle);
        init(voiceRecordListener);
    }

    private void init(final VoiceRecordDialogListener voiceRecordListener)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.sd_sound_volume_dialog);
        setCanceledOnTouchOutside(true);
        micImage = (ImageView) findViewById(R.id.sound_volume_img);
        soundVolumeLayout = (LinearLayout) findViewById(R.id.sound_volume_bk);

        audioRecordManager = AudioRecordManager.getInstance();
        audioRecordManager.setRecordListener(new AudioRecordManager.VoiceRecordListener()
        {
            @Override
            public void onStartRecord()
            {

            }

            @Override
            public void onReceiveMaxVolume(int voiceValue)
            {
                receiveMaxVolume(voiceValue);
            }

            @Override
            public void onOverTimerRecord(String path, int length)
            {
                if (voiceRecordListener != null)
                {
                    voiceRecordListener.onRecordFinish(path, length);
                }
            }

            @Override
            public void onFinishRecord(String path, int length)
            {
                if (voiceRecordListener != null)
                {
                    voiceRecordListener.onRecordFinish(path, length);
                }
            }
        });
    }

    @Override
    public void show()
    {
        super.show();
        audioRecordManager.startRecoder();
    }

    @Override
    public void dismiss()
    {
        super.dismiss();
        audioRecordManager.stopRecord();
    }

    /**
     * @param voiceValue
     * @Description 根据分贝值设置录音时的音量动画
     */
    private void receiveMaxVolume(int voiceValue)
    {
        if (voiceValue < 200.0)
        {
            micImage.setImageResource(R.mipmap.tt_sound_volume_01);
        } else if (voiceValue > 200.0 && voiceValue < 600)
        {
            micImage.setImageResource(R.mipmap.tt_sound_volume_02);
        } else if (voiceValue > 600.0 && voiceValue < 1200)
        {
            micImage.setImageResource(R.mipmap.tt_sound_volume_03);
        } else if (voiceValue > 1200.0 && voiceValue < 2400)
        {
            micImage.setImageResource(R.mipmap.tt_sound_volume_04);
        } else if (voiceValue > 2400.0 && voiceValue < 10000)
        {
            micImage.setImageResource(R.mipmap.tt_sound_volume_05);
        } else if (voiceValue > 10000.0 && voiceValue < 28000.0)
        {
            micImage.setImageResource(R.mipmap.tt_sound_volume_06);
        } else if (voiceValue > 28000.0)
        {
            micImage.setImageResource(R.mipmap.tt_sound_volume_07);
        }
    }

    public interface VoiceRecordDialogListener
    {
        void onRecordFinish(String path, int length);
    }

    public LinearLayout getSoundVolumeLayout()
    {
        return soundVolumeLayout;
    }

    public ImageView getMicImage()
    {
        return micImage;
    }
}
