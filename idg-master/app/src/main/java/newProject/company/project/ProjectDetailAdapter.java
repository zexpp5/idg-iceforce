package newProject.company.project;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.config.Config;
import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.BigImgActivity;
import com.cxgz.activity.cxim.manager.AudioPlayManager;
import com.cxgz.activity.cxim.utils.FileDownLoadUtils;
import com.injoy.idg.R;

import java.io.File;
import java.util.List;

import static com.injoy.idg.R.id.iv_sendPicture;


/**
 * Created by selson on 2017/10/21.
 * 语音会议详情item
 */
public class ProjectDetailAdapter extends BaseMultiItemQuickAdapter<ProjectDetailDataBean, BaseViewHolder>
{
    //我的
    public final static int MY_TEXT = 0;
    public final static int MY_PIC = 1;
    public final static int MY_VOICE = 2;
    //他的
    public final static int HIS_TEXT = 3;
    public final static int HIS_PIC = 4;
    public final static int HIS_VOICE = 5;

    private Context mContext;
    protected int screenWidth;//屏幕宽度
    protected int maxVoiceWidth;//语音最大宽度
    protected int minVoiceWidth;//语音最小宽度
    protected float voicerate;//宽度比例
    List<ProjectDetailDataBean> mData = null;


    public ProjectDetailAdapter(Context context, @Nullable List<ProjectDetailDataBean> data)
    {
        super(data);
        this.mData = data;
        this.mContext = context;
        addItemType(MY_TEXT, R.layout.cxim_row_sent_message);
        addItemType(MY_PIC, R.layout.cxim_row_sent_picture);
        addItemType(MY_VOICE, R.layout.cxim_row_sent_voice);

        addItemType(HIS_TEXT, R.layout.cxim_project_received_message);
        addItemType(HIS_PIC, R.layout.cxim_project_received_picture);
        addItemType(HIS_VOICE, R.layout.cxim_project_received_voice);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final ProjectDetailDataBean item)
    {
        DisplayMetrics dm = new DisplayMetrics();
        dm = mContext.getApplicationContext().getResources().getDisplayMetrics();
        final int scalX = dm.widthPixels;

        //公用部分
        if (StringUtils.notEmpty(item.getIcon()))
        {
            Glide.with(mContext)
                    .load(item.getIcon())
                    .fitCenter()
                    .placeholder(R.mipmap.temp_user_head)
                    .error(R.mipmap.temp_user_head)
                    .crossFade()
                    .into((ImageView) helper.getView(R.id.img_user_icon));
        }
        //时间
        if (helper.getLayoutPosition() == 0)
        {
            if (StringUtils.notEmpty(item.getCreateTime()))
            {
                helper.getView(R.id.timestamp).setVisibility(View.VISIBLE);
                if (StringUtils.notEmpty(item.getCreateTime()))
                    helper.setText(R.id.timestamp, DateUtils.getTimestampString(DateUtils.reTurnDate("yyyy-MM-dd HH:mm:ss", item
                            .getCreateTime())));
                else
                    helper.getView(R.id.timestamp).setVisibility(View.GONE);
            }
        } else
        {
            long currentTime = DateUtils.dateToMillis("yyyy-MM-dd HH:mm:ss", item.getCreateTime());
            long lastTime = DateUtils.dateToMillis("yyyy-MM-dd HH:mm:ss", mData.get(helper.getLayoutPosition() - 1)
                    .getCreateTime());
            if (StringUtils.notEmpty(item.getCreateTime()) && DateUtils.isCloseEnough(currentTime, lastTime))
            {
                helper.getView(R.id.timestamp).setVisibility(View.GONE);
            } else
            {
                helper.setText(R.id.timestamp, DateUtils.getTimestampString(DateUtils.reTurnDate("yyyy-MM-dd HH:mm:ss", item
                        .getCreateTime())));
                helper.getView(R.id.timestamp).setVisibility(View.VISIBLE);
            }
        }

        switch (helper.getItemViewType())
        {
            case MY_TEXT:
                helper.setText(R.id.tv_chatcontent, item.getContent());
                break;
            case MY_PIC:
//                final File localFile = new File(item.getContent());
                if (StringUtils.notEmpty(item.getContent()))
                {
                    Glide.with(mContext)
                            .load(item.getContent())
                            .fitCenter()
                            .override(scalX / 2, scalX / 2)
                            .placeholder(R.mipmap.load_img_init)
                            .crossFade()
                            .into((ImageView) helper.getView(iv_sendPicture));
                }

                helper.getView(R.id.iv_sendPicture).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(mContext, BigImgActivity.class);
                        intent.putExtra(BigImgActivity.EXTR_PATH, item.getContent());
                        mContext.startActivity(intent);
                    }
                });

                break;
            case MY_VOICE:

                if (StringUtils.notEmpty(item.getLength()))
                    helper.setText(R.id.tv_length, item.getLength() + " '");

                voice(helper, item);
                helper.getView(R.id.iv_voice_ly).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        String voiceFileName2 = item.getContent().substring(item.getContent().lastIndexOf("/") + 1);
                        if (StringUtils.notEmpty(FileDownLoadUtils.getInstance().getFilePath(Config.CACHE_VOICE_DIR,
                                voiceFileName2)))
                        {
                            playVoice(FileDownLoadUtils.getInstance().getFilePath(Config.CACHE_VOICE_DIR,
                                    voiceFileName2), (ImageView) helper.getView(R.id.iv_voice));
                        } else
                        {
                            MyToast.showToast(mContext, "正在下载语音");
                            voice(helper, item);
                        }
                    }
                });

                break;

            case HIS_TEXT:
                helper.setText(R.id.tv_chatcontent, item.getContent());
                if (StringUtils.notEmpty(item.getName()))
                {
                    helper.setText(R.id.tv_user_name, item.getName());
                    helper.getView(R.id.tv_user_name).setVisibility(View.VISIBLE);
                }
                if (StringUtils.notEmpty(item.getDeptName()))
                {
                    helper.setText(R.id.tv_user_dept, item.getDeptName());
                    helper.getView(R.id.tv_user_dept).setVisibility(View.VISIBLE);
                }

                if (StringUtils.notEmpty(item.getJob()))
                {
                    helper.setText(R.id.tv_user_job, item.getJob());
                    helper.getView(R.id.tv_user_job).setVisibility(View.VISIBLE);
                }

                break;
            case HIS_PIC:

                if (StringUtils.notEmpty(item.getName()))
                {
                    helper.setText(R.id.tv_user_name, item.getName());
                    helper.getView(R.id.tv_user_name).setVisibility(View.VISIBLE);
                }
                if (StringUtils.notEmpty(item.getDeptName()))
                {
                    helper.setText(R.id.tv_user_dept, item.getDeptName());
                    helper.getView(R.id.tv_user_dept).setVisibility(View.VISIBLE);
                }

                if (StringUtils.notEmpty(item.getJob()))
                {
                    helper.setText(R.id.tv_user_job, item.getJob());
                    helper.getView(R.id.tv_user_job).setVisibility(View.VISIBLE);
                }

                if (StringUtils.notEmpty(item.getContent()))
                {
                    Glide.with(mContext)
                            .load(item.getContent())
                            .fitCenter()
                            .override(scalX / 2, scalX / 2)
                            .placeholder(R.mipmap.load_img_init)
                            .crossFade()
                            .into((ImageView) helper.getView(iv_sendPicture));
                }

                helper.getView(R.id.iv_sendPicture).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(mContext, BigImgActivity.class);
                        intent.putExtra(BigImgActivity.EXTR_PATH, item.getContent());
                        mContext.startActivity(intent);
                    }
                });

                break;
            case HIS_VOICE:
                if (StringUtils.notEmpty(item.getName()))
                {
                    helper.setText(R.id.tv_user_name, item.getName());
                    helper.getView(R.id.tv_user_name).setVisibility(View.VISIBLE);
                }
                if (StringUtils.notEmpty(item.getDeptName()))
                {
                    helper.setText(R.id.tv_user_dept, item.getDeptName());
                    helper.getView(R.id.tv_user_dept).setVisibility(View.VISIBLE);
                }

                if (StringUtils.notEmpty(item.getJob()))
                {
                    helper.setText(R.id.tv_user_job, item.getJob());
                    helper.getView(R.id.tv_user_job).setVisibility(View.VISIBLE);
                }

                if (StringUtils.notEmpty(item.getLength()))
                    helper.setText(R.id.tv_length, item.getLength() + " '");

                voice(helper, item);
                helper.getView(R.id.iv_voice_ly).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        String voiceFileName2 = item.getContent().substring(item.getContent().lastIndexOf("/") + 1);
                        if (StringUtils.notEmpty(FileDownLoadUtils.getInstance().getFilePath(Config.CACHE_VOICE_DIR,
                                voiceFileName2)))
                        {
                            playVoice(FileDownLoadUtils.getInstance().getFilePath(Config.CACHE_VOICE_DIR,
                                    voiceFileName2), (ImageView) helper.getView(R.id.iv_voice));
                        } else
                        {
                            MyToast.showToast(mContext, "正在下载语音");
                            voice(helper, item);
                        }
                    }
                });

                break;
        }
    }

    private void voice(BaseViewHolder helper, ProjectDetailDataBean item)
    {
        if (StringUtils.notEmpty(item.getContent()))
        {
            String url = item.getContent();
            if (url.startsWith("http"))
            {
                File voiceFile = new File(Config.CACHE_VOICE_DIR
                        + File.separator + url.substring(url.lastIndexOf("/") + 1));
                if (!voiceFile.exists())
                {
                    FileDownLoadUtils.getInstance().downLoadAudio(mContext, url, Config.CACHE_VOICE_DIR, url
                            .substring(url.lastIndexOf("/") + 1));
                }
            }
        }

        if (StringUtils.notEmpty(item.getLength()))
        {
            WindowManager wm = (WindowManager) mContext
                    .getSystemService(Context.WINDOW_SERVICE);
            screenWidth = wm.getDefaultDisplay().getWidth();
            maxVoiceWidth = (int) (screenWidth * 0.55);
            minVoiceWidth = (int) (screenWidth * 0.20);
            voicerate = (float) minVoiceWidth / (float) maxVoiceWidth;

            RelativeLayout relativeLayout = (RelativeLayout) helper.getView(R.id.iv_voice_ly);

            /** 根据语音长度来动态设置iv_voice_ly的长度**/
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) relativeLayout.getLayoutParams();
            float lengthRate = (float) item.getLength() / 15.0f;

            //小于3秒
            if (item.getLength() < 4)
            {
                layoutParams.width = minVoiceWidth;
            }
            //大于3秒的。
            else if (item.getLength() > 3)
            {
                layoutParams.width = minVoiceWidth + (int) item.getLength() * ((maxVoiceWidth - minVoiceWidth) / 57);
            }
            relativeLayout.setLayoutParams(layoutParams);
        }
    }

    /**
     * 停止播放录音
     */
    public void stopPlayVoice()
    {
        if (AudioPlayManager.getInstance().getAudioPlayerHandler() != null && AudioPlayManager.getInstance().isPlaying())
        {
            AudioPlayManager.getInstance().stop();
            AudioPlayManager.getInstance().close();
        }
    }

    /**
     * 播放录音
     */
    public void playVoice(String filePath, ImageView imageView)
    {
        if (!(new File(filePath).exists()))
        {
            return;
        }
        try
        {
            AudioPlayManager.getInstance().setOnVoiceListener(new AudioPlayManager.OnVoiceListener()
            {
                @Override
                public void playPositionChange(int currentPosition)
                {

                }

                @Override
                public void playCompletion()
                {
                    stopPlayVoice();
                }

                @Override
                public void playDuration(int duration)
                {

                }

                @Override
                public void playException(Exception e)
                {

                }

                @Override
                public void playStopVioce()
                {

                }
            });
            stopPlayVoice();
            AudioPlayManager.getInstance().play(mContext, filePath, imageView);
        } catch (Exception e)
        {
            //错误提示！
//            MyToast.showToast(MeetingActivity.this, "测试中！" + e.toString());
        }
    }
}
