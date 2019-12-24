package newProject.company.project_manager.investment_project.voice;

import android.content.Context;
import android.widget.ImageView;

import com.base.BaseApplication;
import com.chaoxiang.base.utils.SDLogUtil;
import com.cxgz.activity.cxim.manager.AudioPlayManager;
import com.utils.CachePath;
import com.utils.FileDownload;
import com.utils.FileDownloadUtil;
import com.utils.FileUtil;
import com.utils.SDToast;

import java.io.File;

/**
 * Created by zsz on 2019/4/20.
 */

public class VoiceUtils {
    private AudioPlayManager audioPlayManager;
    private final int UPDATE_WHAT = 100;
    private static VoiceUtils instance;
    public static VoiceUtils getInstance()
    {
        if (null == instance)
        {
            instance = new VoiceUtils();
        }
        return instance;
    }
    public void getVoiceFromNet(final Context context, String playPath,final ImageView imageView){
        audioPlayManager = AudioPlayManager.getInstance();
        audioPlayManager.setOnVoiceListener(new MyOnVoiceListener());
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
        final File saveFile = new File(saveDir, "iceforce");
        SDLogUtil.syso("网络保存文件路径===" + saveFile.getAbsolutePath());
        FileDownloadUtil.resumableDownload(BaseApplication.getInstance(), saveFile.getAbsolutePath(), playPath, 2, "", new FileDownload.DownloadListener()
        {

            @Override
            public void onSuccess(String filePath)
            {
                SDLogUtil.syso("==filePath===" + filePath);
                audioPlayManager.play(context, filePath, imageView);
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
