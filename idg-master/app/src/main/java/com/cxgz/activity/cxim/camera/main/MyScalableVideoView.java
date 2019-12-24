package com.cxgz.activity.cxim.camera.main;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;

import com.yqritc.scalablevideoview.ScalableVideoView;

/**
 * User: Selson
 * Date: 2016-09-22
 * FIXME
 */
public class MyScalableVideoView extends ScalableVideoView
{

    public MyScalableVideoView(Context context)
    {
        super(context);
    }

    public MyScalableVideoView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public MyScalableVideoView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public void setCompletion(final VideoCallBack callBack)
    {
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                callBack.success(true);
            }
        });
    }

    interface VideoCallBack
    {
        void success(boolean s);
    }

}