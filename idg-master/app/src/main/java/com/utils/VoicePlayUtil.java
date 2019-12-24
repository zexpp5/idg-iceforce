package com.utils;

import android.content.Context;

import com.base.BaseApplication;
import com.chaoxiang.base.utils.SDLogUtil;
import com.cxgz.activity.cxim.manager.AudioPlayManager;
import com.entity.administrative.employee.Annexdata;
import com.chaoxiang.base.config.Constants;

import java.io.File;

/**
 * Created by cx on 2016/8/5.
 */
public class VoicePlayUtil {
    private String playPath;
    private String fileName;
    private Annexdata voiceFileEntity;
    public static final String DATA = "data";
    private AudioPlayManager audioPlayManager;
    private final int UPDATE_WHAT = 100;

    private static VoicePlayUtil instance;
    public static VoicePlayUtil getInstance()
    {
        if (null == instance)
        {
            instance = new VoicePlayUtil();
        }
        return instance;
    }
    public  void setData(final Context context,Annexdata fileListEntity)
    {
        audioPlayManager = AudioPlayManager.getInstance();
        audioPlayManager.setOnVoiceListener(new MyOnVoiceListener());
        voiceFileEntity = fileListEntity;
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
            audioPlayManager.play(context, cacheFile.getAbsolutePath(), null);
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
            FileDownloadUtil.resumableDownload(BaseApplication.getInstance(), saveFile.getAbsolutePath(),
                    playPath, voiceFileEntity.getAnnexWay(), "",
                    new FileDownload.DownloadListener()
            {

                @Override
                public void onSuccess(String filePath)
                {
                    SDLogUtil.syso("==filePath===" + filePath);
                    audioPlayManager.play(context, filePath, null);
                }

                @Override
                public void onProgress(int byteCount, int totalSize)
                {
//                    if (totalSize != 0)
//                    {
//                        int precent = byteCount / totalSize;
//                        Message message = handler.obtainMessage();
//                        message.arg1 = precent;
//                        message.what = UPDATE_WHAT;
//                        handler.sendMessage(message);
//                    }
                }

                @Override
                public void onFailure(Exception ossException)
                {
                    if (saveFile.exists())
                    {
                        saveFile.delete();
                    }
                    SDToast.showLong("语音文件下载失败");
                    //finish();
                }
            });
        }
    }

    private class MyOnVoiceListener implements AudioPlayManager.OnVoiceListener {

        @Override
        public void playPositionChange(int currentPosition) {

        }

        @Override
        public void playCompletion() {
            audioPlayManager.stop();
        }

        @Override
        public void playDuration(int duration) {

        }

        @Override
        public void playException(Exception e) {

        }

        @Override
        public void playStopVioce() {

        }
    }
}
