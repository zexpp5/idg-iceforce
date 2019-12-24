package newProject.company.project_manager.investment_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.SDToast;
import com.utils.SPUtils;
import com.utils.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.project_manager.investment_project.adapter.ListPopupAdapter;
import newProject.company.project_manager.investment_project.adapter.TrackProgressAddAdapter;
import newProject.company.project_manager.investment_project.adapter.VoiceListAdapter;
import newProject.company.project_manager.investment_project.bean.IDGBaseBean;
import newProject.company.project_manager.investment_project.bean.IDGTpyeBaseBean;
import newProject.company.project_manager.investment_project.bean.ProjectBaseBean;
import newProject.company.project_manager.investment_project.bean.VoiceBaseBean;
import newProject.company.project_manager.investment_project.voice.VoiceListBean;
import tablayout.view.textview.FontEditext;
import yunjing.view.CustomNavigatorBar;

import static com.utils.SPUtils.USER_ACCOUNT;

/**
 * Created by zsz on 2019/4/11.
 */

public class TrackProgressAddActivity extends BaseActivity implements AudioPlayManager.OnVoiceListener {
    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    @Bind(R.id.fet_name)
    FontEditext fetName;
    @Bind(R.id.fet_content)
    FontEditext fetContent;
    /*@Bind(R.id.ll_voice)
    LinearLayout llVoice;
    @Bind(R.id.iv_voice)
    ImageView ivVoice;*/
    @Bind(R.id.btn_press_to_speak)
    LinearLayout btnPressToSpeak;  //录音按钮
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.voice_recycler_view)
    RecyclerView voiceRecyclerView;

    private String codeKey;
    TrackProgressAddAdapter adapter;
    List<IDGTpyeBaseBean.DataBeanX.DataBean> datas = new ArrayList<>();

    ListPopupWindow listPopupWindow;
    ListPopupAdapter listPopupAdapter;
    List<String> popuList = new ArrayList<>();
    int pIndex;
    List<ProjectBaseBean.DataBeanX.DataBean> projectLists = new ArrayList<>();

    String projId;
    String validDate = "";

    VoiceListAdapter voiceListAdapter;
    List<VoiceListBean> voiceListBeen = new ArrayList<>();
    List<VoiceListBean> updateFiles = new ArrayList<>();
    TextWatcher textSwitcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() != 0) {
                if (listPopupWindow != null) {
                    listPopupWindow.dismiss();
                }
                getData(editable.toString());
            }
        }
    };

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

    private String isFlag;

    @Override
    protected void init() {
        ButterKnife.bind(this);
        mNavigatorBar.setRightTextVisible(true);
        mNavigatorBar.setRightImageVisible(false);
        mNavigatorBar.setRightSecondImageVisible(false);
        mNavigatorBar.setMidText("新增跟踪进展");
        mNavigatorBar.setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mNavigatorBar.setRightTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringUtils.isEmpty(projId)) {
                    SDToast.showShort("请选择选择项目");
                    updateFiles.clear();
                    return;
                }

                if (StringUtils.isEmpty(fetContent.getText()) && voiceListBeen.size() == 0){
                    SDToast.showShort("请选择添加语音或文字");
                    return;
                }

                if (StringUtils.isEmpty(codeKey)) {
                    SDToast.showShort("请选择跟踪进展状态");
                    updateFiles.clear();
                    return;
                }

                if (voiceListBeen.size() > 0) {
                    for (int i = 0; i < voiceListBeen.size(); i++) {
                        uploadVoiceFile(voiceListBeen.get(i));
                    }
                } else {
                    sendData("", "");
                }
            }
        });

        isFlag = getIntent().getStringExtra("flag");
        if ("DETAIL".equals(isFlag)) {
            projId = getIntent().getStringExtra("projId");
            if (!StringUtils.isEmpty(getIntent().getStringExtra("validDate"))) {
                validDate = getIntent().getStringExtra("validDate");
            }
            //VC组 需要修改标题
            if (!StringUtils.isEmpty(getIntent().getStringExtra("title"))) {
                mNavigatorBar.setMidText(getIntent().getStringExtra("title"));
            }
            fetName.setText(getIntent().getStringExtra("projName"));
            fetName.setEnabled(false);
            fetContent.requestFocus();
        } else {
            //fetName.addTextChangedListener(textSwitcher);
            fetName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(TrackProgressAddActivity.this,PublicChooseProjectActivity.class);
                    startActivityForResult(intent,100);
                }
            });
        }

        /*if (!StringUtils.isEmpty(getIntent().getStringExtra("projName"))){
            projId = getIntent().getStringExtra("projId");
            fetName.setText(getIntent().getStringExtra("projName"));
            fetName.setSelection(fetName.length());
        }else {

        }*/

        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(lm);
        adapter = new TrackProgressAddAdapter(datas);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                for (int i = 0; i < datas.size(); i++) {
                    datas.get(i).setFlag(false);
                }
                datas.get(position).setFlag(true);
                codeKey = datas.get(position).getCodeKey();
                adapter.notifyDataSetChanged();
            }
        });

        btnPressToSpeak.setOnTouchListener(new SpeakListen());
        /*ivVoice.setImageResource(R.drawable.play_annex_voice);
        llVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVoice();
            }
        });*/

        voiceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        voiceListAdapter = new VoiceListAdapter(voiceListBeen);
        voiceRecyclerView.setAdapter(voiceListAdapter);
        voiceListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_voice:
                        playVoice((ImageView) view, voiceListBeen.get(position).getFile());
                        break;
                    case R.id.iv_remove:
                        voiceListBeen.remove(position);
                        voiceListAdapter.notifyDataSetChanged();
                        if (voiceListBeen.size() == 0) {
                            fetContent.setVisibility(View.VISIBLE);
                        }
                        break;
                }

            }
        });

        getBaseData();

    }

    public void getData(String name) {
        ListHttpHelper.getTrackProgessProjectNameListData(this, SPUtils.get(this, USER_ACCOUNT, "").toString(), name, new SDRequestCallBack(ProjectBaseBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                ProjectBaseBean bean = (ProjectBaseBean) responseInfo.getResult();
                if (bean.getData().getData() != null && bean.getData().getData().size() != 0) {
                    projectLists.clear();
                    projectLists.addAll(bean.getData().getData());
                    initPopupWindow(fetName);
                }
            }
        });
    }

    private void initPopupWindow(View view) {
        popuList.clear();
        for (int i = 0; i < projectLists.size(); i++)
            popuList.add(projectLists.get(i).getProjName());

        if (listPopupWindow != null) {
            listPopupAdapter.notifyDataSetChanged();
        }
        listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        listPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        listPopupAdapter = new ListPopupAdapter(popuList, this);
        listPopupWindow.setAdapter(listPopupAdapter);
        listPopupWindow.setModal(true);
        listPopupWindow.setAnchorView(view);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                fetName.removeTextChangedListener(textSwitcher);
                fetName.setText(projectLists.get(i).getProjName());
                fetName.setSelection(fetName.length());
                projId = projectLists.get(i).getProjId();
                listPopupWindow.dismiss();
                fetName.addTextChangedListener(textSwitcher);
            }
        });
        listPopupWindow.show();
    }

    public void getBaseData() {
        ListHttpHelper.getIDGBaseData(this, "followOnStatus", new SDRequestCallBack(IDGTpyeBaseBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                IDGTpyeBaseBean baseBean = (IDGTpyeBaseBean) responseInfo.getResult();
                datas.clear();
                datas.addAll(baseBean.getData().getData());
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void sendData(String path, String audioTime) {
        //VC组 需要修改标题
        if (!StringUtils.isEmpty(getIntent().getStringExtra("title"))) {

            ListHttpHelper.sendVCTrackProgressAddData(this, SPUtils.get(this, USER_ACCOUNT, "").toString(), getIntent().getStringExtra("projId"), updateFiles.size() > 0 ? path : fetContent.getText().toString(),updateFiles.size() > 0 ? "AUDIO" : "TEXT", audioTime, codeKey, getIntent().getStringExtra("codeKey"), new SDRequestCallBack(IDGBaseBean.class) {
                @Override
                public void onRequestFailure(HttpException error, String msg) {
                    SDToast.showShort(msg);
                }

                @Override
                public void onRequestSuccess(SDResponseInfo responseInfo) {
                    IDGBaseBean bean = (IDGBaseBean) responseInfo.getResult();
                    if ("success".equals(bean.getData().getCode())) {
                        SDToast.showShort("提交成功");
                        finish();
                    } else {
                        SDToast.showShort(bean.getData().getReturnMessage());
                    }
                }
            });
        }else {
            ListHttpHelper.sendTrackProgressAddData(this, SPUtils.get(this, USER_ACCOUNT, "").toString(), projId, updateFiles.size() > 0 ? path : fetContent.getText().toString(), updateFiles.size() > 0 ? "AUDIO" : "TEXT", audioTime, validDate, codeKey,
                    getIntent().getStringExtra("origBusId"), getIntent().getStringExtra("busType"), getIntent().getStringExtra("applyUser"), new SDRequestCallBack(IDGBaseBean.class) {
                        @Override
                        public void onRequestFailure(HttpException error, String msg) {
                            SDToast.showShort(msg);
                        }

                        @Override
                        public void onRequestSuccess(SDResponseInfo responseInfo) {
                            IDGBaseBean bean = (IDGBaseBean) responseInfo.getResult();
                            if ("success".equals(bean.getData().getCode())) {
                                SDToast.showShort("提交成功");
                                finish();
                            } else {
                                SDToast.showShort(bean.getData().getReturnMessage());
                            }
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            projId = data.getStringExtra("projId");
            fetName.setText(data.getStringExtra("projName"));
            fetName.setEnabled(false);
            fetContent.requestFocus();
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_track_progress_add;
    }

    @Override
    public void playPositionChange(int currentPosition) {
    }

    @Override
    public void playCompletion() {

    }

    @Override
    public void playDuration(int duration) {
        SDLogUtil.debug("duration===" + duration);

    }

    @Override
    public void playException(Exception e) {
        e.printStackTrace();
        //SDToast.showLong("语音文件已损坏");

    }

    @Override
    public void playStopVioce() {

    }

    /**
     * 按住说话listener
     */
    class SpeakListen implements View.OnTouchListener {
        @Override
        public boolean onTouch(final View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //标识是否录音中
                    isRecording = true;
                    isCancelVoiceRecord = false;
                    if (!FileUtill.isExitsSdcard()) {
                        MyToast.showToast(TrackProgressAddActivity.this, getString(R.string.Send_voice_need_sdcard_support));
                        return false;
                    }
                    if (voiceRecordDialog == null) {
                        voiceRecordDialog = new VoiceRecordDialog(TrackProgressAddActivity.this, new VoiceRecordDialog
                                .VoiceRecordDialogListener() {
                            @Override
                            public void onRecordFinish(final String path, final int length) {
                                TrackProgressAddActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
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
                case MotionEvent.ACTION_MOVE: {
                    if (event.getY() < 0) {
                        isCancelVoiceRecord = true;
                        voiceRecordDialog.getSoundVolumeLayout().setBackgroundResource(R.mipmap.tt_sound_volume_cancel_bk);
                    } else {
                        isCancelVoiceRecord = false;
                        voiceRecordDialog.getSoundVolumeLayout().setBackgroundResource(R.mipmap.tt_sound_volume_default_bk);
                    }
                    return true;
                }

                case MotionEvent.ACTION_UP:
                    //未录音
                    isRecording = false;
                    if (event.getY() < 0) {
                        voiceRecordDialog.getMicImage().setVisibility(View.GONE);

                        timer.schedule(new TimerTask() {
                            public void run() {
                                if (voiceRecordDialog.isShowing())
                                    voiceRecordDialog.dismiss();
                                this.cancel();
                            }
                        }, 300);
                        isCancelVoiceRecord = true;
                    } else {
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
    public void dealVoiceData(String path, int length) {
        if (length >= 1) {
            //语音长度大于1
            SDLogUtil.syso("voice length=" + length);
            SDLogUtil.syso("voice filePath=" + path);
            if (path != null) {
                if (!isCancelVoiceRecord) {
                    File file = new File(path);
                    if (file.exists()) {
                        //voiceFile = file;
                        fetContent.setVisibility(View.GONE);
                        voiceListBeen.add(new VoiceListBean(file, length));
                        voiceListAdapter.notifyDataSetChanged();
                        //llVoice.setVisibility(View.VISIBLE);
                        //setProjectChatFilter(3, "", "", "", length + "");
                    }
//                    sendVoice(file, length + "");
                }
            }
        } else {
            voiceRecordDialog.getSoundVolumeLayout().setBackgroundResource(R.mipmap.tt_sound_volume_short_tip_bk);
            timer.schedule(new TimerTask() {
                public void run() {
                    if (voiceRecordDialog.isShowing())
                        voiceRecordDialog.dismiss();
                    this.cancel();
                }
            }, 3000);
            MyToast.showToast(this, "录音时间太短");
        }
        if (isCancelVoiceRecord) {
            File file = new File(path);
            if (file != null) {
                if (file.exists()) {
                    file.delete();
                    SDLogUtil.syso("删除录音-文件delete==>" + path);
                }
            }
        }
    }

    public void playVoice(ImageView ivVoice, File voiceFile) {
        audioPlayManager = AudioPlayManager.getInstance();
        audioPlayManager.setOnVoiceListener(this);
        ivVoice.setBackgroundColor(0xffffffff);
        ivVoice.setImageResource(R.drawable.play_annex_voice);
        AudioPlayManager.getInstance().play(TrackProgressAddActivity.this, voiceFile.getPath(), ivVoice);
        //onClickVoice(mVoiceEntity);
    }

    private void uploadVoiceFile(final VoiceListBean voiceFile) {
        ListHttpHelper.submitVoiceApi(TrackProgressAddActivity.this.getApplication(), voiceFile.getFile(), new FileUpload.UploadListener() {
            @Override
            public void onSuccess(SDResponseInfo responseInfo, HashMap<String, Object> result) {
                VoiceBaseBean bean = new SDGson().fromJson(responseInfo.getResult().toString(), new TypeToken<VoiceBaseBean>() {
                }.getType());
                updateFiles.add(new VoiceListBean(voiceFile.getFile(), voiceFile.getSecond(), bean.getData().get(0).getPath()));
                if (updateFiles.size() == voiceListBeen.size()) {
                    String pathsStr = "";
                    String audioTimesStr = "";
                    StringBuffer paths = new StringBuffer();
                    StringBuffer audioTimes = new StringBuffer();
                    for (int i = 0; i < updateFiles.size(); i++) {
                        paths.append(updateFiles.get(i).getUrl() + ",");
                        audioTimes.append(updateFiles.get(i).getSecond() + ",");
                    }
                    pathsStr = paths.substring(0, paths.length() - 1);
                    audioTimesStr = audioTimes.substring(0, audioTimes.length() - 1);
                    sendData(pathsStr, audioTimesStr);
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
