package com.ui.activity;

import android.text.TextUtils;
import android.view.View;

import com.injoy.idg.R;
import com.base.SendMsgBaseActivity;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.entity.VoiceEntity;
import com.entity.administrative.employee.Annexdata;

import java.io.File;
import java.util.List;

/**
 * 显示附件
 * Created by cx on 2016/5/12.
 */
public class ShowAnnexDataActivity extends SendMsgBaseActivity {
    private List<Annexdata> annexdata;
    private String id;
    @Override
    protected int getContentLayout() {
        return R.layout.img_main_activity;
    }

    @Override
    protected void init() {
        super.init();
        setTitle("显示图片");
        setLeftBack(R.mipmap.back_back);//返回的
        annexdata= (List<Annexdata>)getIntent().getSerializableExtra("annexdata");
        id=getIntent().getStringExtra("id");
        if(!TextUtils.isEmpty(id)){
            showFile(annexdata,Long.parseLong(id));
        }

    }

    @Override
    public void onSelectedImg(List<String> imgPaths) {

    }

    @Override
    public void onClickLocationFunction(double latitude, double longitude, String address) {

    }

    @Override
    public void onClickSendRange(List<SDUserEntity> userEntities) {

    }

    @Override
    public void onClickSendPerson(List<SDUserEntity> userEntities) {

    }

    @Override
    public void onClickAttach(List<File> pickerFile) {

    }

    @Override
    public void onDelAttachItem(View v) {

    }

    @Override
    public void onClickVoice(VoiceEntity voiceEntity) {

    }

    @Override
    public int getDraftDataType() {
        return 0;
    }

    @Override
    public void onClickMoneyFile(String moneyString) {

    }
}
