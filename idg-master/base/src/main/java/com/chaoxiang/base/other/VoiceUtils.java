package com.chaoxiang.base.other;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;

import com.chaoxiang.base.utils.SPUtils;

import static android.content.Context.VIBRATOR_SERVICE;


/**
 * User: Selson
 * Date: 2017-06-02
 * FIXME
 */
public class VoiceUtils
{
    private static VoiceUtils instance;

    private VoiceUtils()
    {

    }

    public static synchronized VoiceUtils getInstance()
    {
        if (instance == null)
        {
            instance = new VoiceUtils();
        }
        return instance;
    }

    public static void startVoice(Context context)
    {
        boolean voiceIsCheck = (boolean) SPUtils.get(context, SPUtils.VOICE_ISCHECK, true);

        if (voiceIsCheck)
        {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (notification == null) return;
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        }
    }

    public static void startVibrator(Context context, int repeat)
    {
        boolean vibratorIsCheck = (boolean) SPUtils.get(context, SPUtils.VIBRATOR_ISCHECK, true);

        if (vibratorIsCheck)
        {
            Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
//            vibrator.vibrate(10000);//设置震动的时长，ms
            long [] pattern = {100,400,100,400};
            vibrator.vibrate(pattern, repeat);
        }
    }

} 