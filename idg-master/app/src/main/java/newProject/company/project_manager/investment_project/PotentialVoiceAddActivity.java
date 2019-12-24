package newProject.company.project_manager.investment_project;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.cxgz.activity.cxim.dialog.VoiceRecordDialog;
import com.cxgz.activity.cxim.manager.AudioPlayManager;
import com.cxgz.activity.cxim.utils.FileUtill;
import com.entity.VoiceEntity;
import com.google.gson.reflect.TypeToken;
import com.http.FileUpload;
import com.http.SDResponseInfo;
import com.injoy.idg.R;
import com.utils.SDToast;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.project_manager.investment_project.adapter.VoiceListAdapter;
import newProject.company.project_manager.investment_project.bean.VoiceBaseBean;
import newProject.company.project_manager.investment_project.voice.VoiceListBean;
import tablayout.view.textview.FontEditext;
import yunjing.view.CustomNavigatorBar;

/**
 * Created by zsz on 2019/4/11.
 */

public class PotentialVoiceAddActivity extends BaseActivity implements AudioPlayManager.OnVoiceListener{
    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    @Bind(R.id.fet_content)
    FontEditext fetContent;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.btn_press_to_speak)
    LinearLayout btnPressToSpeak;  //录音按钮

    VoiceListAdapter adapter;
    List<VoiceListBean> datas = new ArrayList<>();
    List<VoiceListBean> updateFiles = new ArrayList<>();
    //录音状态
    private boolean isRecording = false;
    //是否取消录音
    private boolean isCancelVoiceRecord;
    /**
     * 录音dialog
     */
    private VoiceRecordDialog voiceRecordDialog;
    //录音计时器
    private Timer timer = new Timer();

    protected VoiceEntity mVoiceEntity;
    private AudioPlayManager audioPlayManager;


    @Override
    protected void init() {
        ButterKnife.bind(this);
        mNavigatorBar.setRightTextVisible(true);
        mNavigatorBar.setRightImageVisible(false);
        mNavigatorBar.setRightSecondImageVisible(false);
        mNavigatorBar.setMidText("填写项目介绍");
        mNavigatorBar.setLeftImageVisible(false);
        mNavigatorBar.setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mNavigatorBar.setLeftTextVisible(true);
        mNavigatorBar.setLeftText("取消");
        mNavigatorBar.setLeftTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mNavigatorBar.setRightTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringUtils.isEmpty(fetContent.getText()) && datas.size() == 0){
                    SDToast.showShort("请填写项目介绍或录入语音");
                    return;
                }

                if (datas.size() > 0){
                    boolean uploadFlag = false;
                    for (int i = 0; i < datas.size(); i++) {
                        if (StringUtils.notEmpty(datas.get(i).getUrl())){
                            updateFiles.add(datas.get(i));
                        }else {
                            uploadFlag = true;
                            uploadVoiceFile(datas.get(i));
                        }
                    }
                    if (!uploadFlag){
                        Intent intent = new Intent();
                        intent.putExtra("flag","voice");
                        intent.putExtra("desc",  fetContent.getText().toString());
                        intent.putExtra("voices", (Serializable) datas);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }else {
                    Intent intent = new Intent();
                    intent.putExtra("flag","text");
                    intent.putExtra("desc",  fetContent.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        datas.addAll((List<VoiceListBean>)getIntent().getSerializableExtra("voices"));
        fetContent.setText(getIntent().getStringExtra("desc"));
        fetContent.setSelection(fetContent.getText().length());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VoiceListAdapter(datas);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.iv_voice:
                        playVoice((ImageView) view,datas.get(position).getFile());
                        break;
                    case R.id.iv_remove:
                        datas.remove(position);
                        adapter.notifyDataSetChanged();
                        break;
                }

            }
        });

        btnPressToSpeak.setOnTouchListener(new SpeakListen());

    }


    @Override
    protected int getContentLayout() {
        return R.layout.activity_potential_voice_add;
    }

    @Override
    public void playPositionChange(int currentPosition)
    {
    }

    @Override
    public void playCompletion()
    {

    }

    @Override
    public void playDuration(int duration)
    {
        SDLogUtil.debug("duration===" + duration);

    }

    @Override
    public void playException(Exception e)
    {
        e.printStackTrace();
        //SDToast.showLong("语音文件已损坏");

    }

    @Override
    public void playStopVioce()
    {

    }

    /**
     * 按住说话listener
     */
    class SpeakListen implements View.OnTouchListener
    {
        @Override
        public boolean onTouch(final View v, MotionEvent event)
        {
            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    //标识是否录音中
                    isRecording = true;
                    isCancelVoiceRecord = false;
                    if (!FileUtill.isExitsSdcard())
                    {
                        MyToast.showToast(PotentialVoiceAddActivity.this, getString(R.string.Send_voice_need_sdcard_support));
                        return false;
                    }
                    if (voiceRecordDialog == null)
                    {
                        voiceRecordDialog = new VoiceRecordDialog(PotentialVoiceAddActivity.this, new VoiceRecordDialog
                                .VoiceRecordDialogListener()
                        {
                            @Override
                            public void onRecordFinish(final String path, final int length)
                            {
                                PotentialVoiceAddActivity.this.runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        btnPressToSpeak.setFocusable(false);
                                        //处理录音
                                        dealVoiceData(path, length);
                                    }
                                });

                            }
                        });
                    }
                    voiceRecordDialog.getSoundVolumeLayout().setBackgroundResource(R.mipmap.tt_sound_volume_default_bk);
                    voiceRecordDialog.getMicImage().setVisibility(View.VISIBLE);
                    voiceRecordDialog.show();
                    return true;
                case MotionEvent.ACTION_MOVE:
                {
                    if (event.getY() < 0)
                    {
                        isCancelVoiceRecord = true;
                        voiceRecordDialog.getSoundVolumeLayout().setBackgroundResource(R.mipmap.tt_sound_volume_cancel_bk);
                    } else
                    {
                        isCancelVoiceRecord = false;
                        voiceRecordDialog.getSoundVolumeLayout().setBackgroundResource(R.mipmap.tt_sound_volume_default_bk);
                    }
                    return true;
                }

                case MotionEvent.ACTION_UP:
                    //未录音
                    isRecording = false;
                    if (event.getY() < 0)
                    {
                        voiceRecordDialog.getMicImage().setVisibility(View.GONE);

                        timer.schedule(new TimerTask()
                        {
                            public void run()
                            {
                                if (voiceRecordDialog.isShowing())
                                    voiceRecordDialog.dismiss();
                                this.cancel();
                            }
                        }, 300);
                        isCancelVoiceRecord = true;
                    } else
                    {
                        isCancelVoiceRecord = false;
                    }
                    voiceRecordDialog.dismiss();
            }
            return true;
        }
    }

    /**
     * 处理语音数据
     *
     * @param path
     * @param length
     */
    public void dealVoiceData(String path, int length)
    {
        if (length >= 1)
        {
            //语音长度大于1
            SDLogUtil.syso("voice length=" + length);
            SDLogUtil.syso("voice filePath=" + path);
            if (path != null)
            {
                if (!isCancelVoiceRecord)
                {
                    File file = new File(path);
                    if (file.exists())
                    {

                        datas.add(new VoiceListBean(file,length));
                        adapter.notifyDataSetChanged();
                       //setProjectChatFilter(3, "", "", "", length + "");
                    }
//                    sendVoice(file, length + "");
                }
            }
        } else
        {
            voiceRecordDialog.getSoundVolumeLayout().setBackgroundResource(R.mipmap.tt_sound_volume_short_tip_bk);
            timer.schedule(new TimerTask()
            {
                public void run()
                {
                    if (voiceRecordDialog.isShowing())
                        voiceRecordDialog.dismiss();
                    this.cancel();
                }
            }, 3000);
            MyToast.showToast(this, "录音时间太短");
        }
        if (isCancelVoiceRecord)
        {
            File file = new File(path);
            if (file != null)
            {
                if (file.exists())
                {
                    file.delete();
                    SDLogUtil.syso("删除录音-文件delete==>" + path);
                }
            }
        }
    }

    public void playVoice(ImageView ivVoice,File voiceFile){
        audioPlayManager = AudioPlayManager.getInstance();
        audioPlayManager.setOnVoiceListener(this);
        ivVoice.setBackgroundColor(0xffffffff);
        ivVoice.setImageResource(R.drawable.play_annex_voice);
        AudioPlayManager.getInstance().play(PotentialVoiceAddActivity.this,voiceFile.getPath() , ivVoice);
    }

    private void uploadVoiceFile(final VoiceListBean voiceFile){
        ListHttpHelper.submitVoiceApi(PotentialVoiceAddActivity.this.getApplication(), voiceFile.getFile(), new FileUpload.UploadListener() {
            @Override
            public void onSuccess(SDResponseInfo responseInfo, HashMap<String, Object> result) {
                VoiceBaseBean bean = new SDGson().fromJson(responseInfo.getResult().toString(), new TypeToken<VoiceBaseBean>() {}.getType());
                updateFiles.add(new VoiceListBean(voiceFile.getFile(),voiceFile.getSecond(),bean.getData().get(0).getPath()));
                if (updateFiles.size() == datas.size()){
                    Intent intent = new Intent();
                    intent.putExtra("flag","voice");
                    intent.putExtra("desc",  fetContent.getText().toString());
                    intent.putExtra("voices", (Serializable) updateFiles);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }

            @Override
            public void onProgress(int byteCount, int totalSize) {

            }

            @Override
            public void onFailure(Exception ossException) {

            }
        });
    }


}
