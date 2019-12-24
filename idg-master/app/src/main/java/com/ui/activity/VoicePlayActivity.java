package com.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.injoy.idg.R;
import com.base.BasePlayAudioActivity;
import com.chaoxiang.base.utils.SDLogUtil;
import com.cxgz.activity.cxim.manager.AudioPlayManager;
import com.entity.administrative.employee.Annexdata;
import com.chaoxiang.base.config.Constants;
import com.utils.CachePath;
import com.utils.FileDownload;
import com.utils.FileDownloadUtil;
import com.utils.FileUtil;
import com.utils.SDToast;
import com.utils.StringUtil;

import java.io.File;

/**
 * @author zjh
 */
@SuppressLint("HandlerLeak")
public class VoicePlayActivity extends BasePlayAudioActivity implements AudioPlayManager.OnVoiceListener
{
    private TextView tv_current_position;
    private TextView tv_duration;
    private TextView tv_loading;
    private LinearLayout ll_play_content;
    private LinearLayout ll_loading;
    private String playPath;
    private String fileName;
    private Annexdata voiceFileEntity;
    public static final String DATA = "data";
    private AudioPlayManager audioPlayManager;
    private final int UPDATE_WHAT = 100;


    private Handler handler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            int what = msg.what;
            if (what == UPDATE_WHAT)
            {
                int precent = msg.arg1;
                if (precent != 100)
                {
                    tv_loading.setText(StringUtil.getResourceString(VoicePlayActivity.this, R.string.loading) + precent + "%");
                } else
                {
                    tv_loading.setText(StringUtil.getResourceString(VoicePlayActivity.this, R.string.loading_voice));
                }
            }

        }

        ;
    };


    /**
     * 启动播放语音页面
     *
     * @param context
     * @param fileListEntity
     */
    public static void startVoiceActivity(Context context, Annexdata fileListEntity)
    {
        Intent intent = new Intent(context, VoicePlayActivity.class);
        intent.putExtra(DATA, fileListEntity);
        context.startActivity(intent);
    }

    @Override
    protected void init()
    {
        tv_current_position = (TextView) findViewById(R.id.tv_current_position);
        tv_duration = (TextView) findViewById(R.id.tv_duration);
        tv_loading = (TextView) findViewById(R.id.tv_loading);
        ll_play_content = (LinearLayout) findViewById(R.id.ll_play_content);
        ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
        setData();
    }

    private void setData()
    {
        audioPlayManager = AudioPlayManager.getInstance();
        audioPlayManager.setOnVoiceListener(this);
        voiceFileEntity = (Annexdata) getIntent().getSerializableExtra(DATA);
        playPath = voiceFileEntity.getPath();
        fileName = voiceFileEntity.getSrcName();
        if (fileName.endsWith(Constants.RADIO_PREFIX_NEW))
        {
            fileName = fileName.substring(0, fileName.indexOf(Constants.RADIO_PREFIX_NEW));
        }
        File cacheFile = new File(FileUtil.getSDFolder() + "/" + CachePath.VOICE_CACHE, fileName);
        SDLogUtil.syso("cacheFile == " + cacheFile.getAbsolutePath());
        SDLogUtil.syso("SrcName == " + fileName);
        SDLogUtil.syso("playPath==" + playPath);
        if (cacheFile.exists())
        {
            //从本地取
            SDLogUtil.syso("从本地获取");
            audioPlayManager.play(this, cacheFile.getAbsolutePath(), null);
        } else
        {
            //从网络获取
            SDLogUtil.syso("从网络获取");
            File saveDir = new File(FileUtil.getSDFolder(), CachePath.VOICE_CACHE);
            if (saveDir != null)
            {
                if (!saveDir.exists())
                {
                    saveDir.mkdirs();
                }
            }
            final File saveFile = new File(saveDir, fileName);
            SDLogUtil.syso("网络保存文件路径===" + saveFile.getAbsolutePath());
            FileDownloadUtil.resumableDownload(getApplication(), saveFile.getAbsolutePath(), playPath, voiceFileEntity.getAnnexWay(), "", new FileDownload.DownloadListener()
            {

                @Override
                public void onSuccess(String filePath)
                {
                    SDLogUtil.syso("==filePath===" + filePath);
                    audioPlayManager.play(VoicePlayActivity.this, filePath, null);
                }

                @Override
                public void onProgress(int byteCount, int totalSize)
                {
                    if (totalSize != 0)
                    {
                        int precent = byteCount / totalSize;
                        Message message = handler.obtainMessage();
                        message.arg1 = precent;
                        message.what = UPDATE_WHAT;
                        handler.sendMessage(message);
                    }
                }

                @Override
                public void onFailure(Exception ossException)
                {
                    if (saveFile.exists())
                    {
                        saveFile.delete();
                    }
                    SDToast.showLong("语音文件下载失败");
                    finish();
                }
            });
        }
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.sd_play_voice_activity;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        audioPlayManager.stop();
    }


    private void showPlayProgress()
    {
        ll_loading.setVisibility(View.GONE);
        ll_play_content.setVisibility(View.VISIBLE);
    }


    @Override
    public void playPositionChange(int currentPosition)
    {
        SDLogUtil.syso("currentPosition == " + currentPosition);
        int second = currentPosition;
        if (second < 10)
        {
            tv_current_position.setText("0" + second);
        } else
        {
            tv_current_position.setText("" + second);
        }
    }

    @Override
    public void playCompletion()
    {
        finish();
    }

    @Override
    public void playDuration(int duration)
    {
        SDLogUtil.debug("duration===" + duration);
        showPlayProgress();
        tv_duration.setText(StringUtil.getResourceString(this, R.string.voice_total) + duration + StringUtil.getResourceString(this, R.string.second));
    }

    @Override
    public void playException(Exception e)
    {
        e.printStackTrace();
        //SDToast.showLong("语音文件已损坏");
        finish();
    }

    @Override
    public void playStopVioce()
    {

    }
}
