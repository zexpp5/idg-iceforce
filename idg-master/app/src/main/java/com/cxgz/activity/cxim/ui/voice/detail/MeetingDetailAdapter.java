package com.cxgz.activity.cxim.ui.voice.detail;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.config.Config;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.utils.FileDownLoadUtils;
import com.injoy.idg.R;

import java.io.File;
import java.util.List;


/**
 * Created by selson on 2017/10/21.
 * 语音会议详情item
 */
public class MeetingDetailAdapter extends BaseQuickAdapter<MeetingVoiceDetail, BaseViewHolder>
{
    private Context mContext;
    protected int screenWidth;//屏幕宽度
    protected int maxVoiceWidth;//语音最大宽度
    protected int minVoiceWidth;//语音最小宽度
    protected float voicerate;//宽度比例

    public MeetingDetailAdapter(Context context, @Nullable List<MeetingVoiceDetail> data)
    {
        super(R.layout.item_im_voice_detail, data);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, MeetingVoiceDetail item)
    {
        if (StringUtils.notEmpty(item.getIcon()))
            Glide.with(mContext)
                    .load(item.getIcon())
                    .fitCenter()
                    .placeholder(R.mipmap.temp_user_head)
                    .error(R.mipmap.temp_user_head)
                    .crossFade()
                    .into((ImageView) helper.getView(R.id.img_user_icon));

        helper.setText(R.id.tv_user_name, item.getName());

        if (StringUtils.notEmpty(item.getLength()))
            helper.setText(R.id.tv_length, item.getLength() + " '");

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
}
