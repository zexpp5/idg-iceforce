package newProject.company.project_manager.investment_project;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.cxgz.activity.cxim.dialog.VoiceRecordDialog;
import com.cxgz.activity.cxim.manager.AudioPlayManager;
import com.entity.VoiceEntity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.SDToast;
import com.utils.SPUtils;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import newProject.api.ListHttpHelper;
import newProject.company.invested_project.ActivityQxb;
import newProject.company.invested_project.ActivityQxbSearch;
import newProject.company.project_manager.investment_project.adapter.ListPopupAdapter;
import newProject.company.project_manager.investment_project.adapter.StatusAdapter;
import newProject.company.project_manager.investment_project.adapter.TrackProgressAddAdapter;
import newProject.company.project_manager.investment_project.bean.GroupListBean;
import newProject.company.project_manager.investment_project.bean.IDGBaseBean;
import newProject.company.project_manager.investment_project.bean.IDGTpyeBaseBean;
import newProject.company.project_manager.investment_project.bean.NewGroupListBean;
import newProject.company.project_manager.investment_project.bean.ProjectNameListBean;
import newProject.company.project_manager.investment_project.bean.PublicUserListBean;
import newProject.company.project_manager.investment_project.voice.VoiceListBean;
import yunjing.view.CustomNavigatorBar;

import static com.utils.SPUtils.USER_ACCOUNT;

/**
 * Created by zsz on 2019/4/10.
 */

public class PotentialProjectsAddActivity extends BaseActivity {
    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.et_team)
    EditText etTeam;
    /*@Bind(R.id.tv_state)
    TextView tvState;*/
    @Bind(R.id.tv_desc)
    TextView tvDesc;
    @Bind(R.id.tv_group)
    TextView tvGroup;
    @Bind(R.id.et_money)
    EditText etMoney;
    @Bind(R.id.tv_principal)
    TextView tvPrincipal;
    @Bind(R.id.tv_team_member)
    TextView tvTeamMember;
    @Bind(R.id.tv_remind_name)
    TextView tvRemindName;
    @Bind(R.id.iv_voice)
    ImageView ivVoice;
    @Bind(R.id.iv_choose)
    ImageView ivChoose;
    @Bind(R.id.iv_important)
    ImageView ivImportant;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.ll_radio)
    LinearLayout llRadio;

    boolean isChoose = false;
    boolean isImportant = false;
    StatusAdapter adapter;
    List<IDGTpyeBaseBean.DataBeanX.DataBean> datas = new ArrayList<>();
    String stateIndexStr;
    List<GroupListBean.DataBeanX.DataBean> groupLists = new ArrayList<>();
    String groupIndexStr;
    List<String> popuList = new ArrayList<>();

    private List<PublicUserListBean.DataBeanX.DataBean> principalLists = new ArrayList<>();
    private List<PublicUserListBean.DataBeanX.DataBean> teamMemberrecommendLists = new ArrayList<>();
    private List<PublicUserListBean.DataBeanX.DataBean> remindLists = new ArrayList<>();

    List<VoiceListBean> voiceFiles = new ArrayList<>();
    String descStr = "";

    private int indexLoopView1 = 0;
    private int indexLoopView2 = 0;


    private List<NewGroupListBean.DataBeanX.DataBean> newGroupListBeen = new ArrayList<>();
    //String gruopKey;

    ListPopupWindow listPopupWindow;
    ListPopupAdapter listPopupAdapter;
    List<String> popupNameList = new ArrayList<>();
    private List<ProjectNameListBean.DataBeanX.DataBean> nameList = new ArrayList<>();
    private String projId;
    TextWatcher textSwitcher=  new TextWatcher() {
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

                getNameData(editable.toString());
            }
        }
    };

    @Override
    protected void init() {
        ButterKnife.bind(this);
        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(false);
        mNavigatorBar.setRightSecondImageVisible(false);
        mNavigatorBar.setMidText("新建潜在项目");
        mNavigatorBar.setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvGroup.setText(SPUtils.get(PotentialProjectsAddActivity.this,SPUtils.DEPT_NAME,"").toString());
        groupIndexStr = SPUtils.get(PotentialProjectsAddActivity.this,SPUtils.DEPT_ID,"").toString();
        //判断是否VC组ID，是就隐藏
        if (groupIndexStr.equals("1")){
            llRadio.setVisibility(View.GONE);
            isChoose = true;
            ivChoose.setImageResource(R.mipmap.icon_p_p_a_choose2);
        }else {
            llRadio.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(lm);
        adapter = new StatusAdapter(datas);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                for (int i = 0; i < datas.size(); i++){
                    datas.get(i).setFlag(false);
                }
                datas.get(position).setFlag(true);
                stateIndexStr = datas.get(position).getCodeKey();
                adapter.notifyDataSetChanged();
            }
        });

        etName.addTextChangedListener(textSwitcher);

        getProjectStateData();
        //getGroupData();
        getGroupDataNew();
    }

    @OnClick({R.id.ll_group,R.id.ll_principal,R.id.ll_team_member,R.id.rl_remind,R.id.btn_ok,R.id.tv_desc,R.id.ll_choose,R.id.ll_important,R.id.btn_qxb})
    public void onItemClick(View view){
        Intent intent = new Intent(PotentialProjectsAddActivity.this, PublicUserListActivity.class);
        switch (view.getId()){
            /*case R.id.ll_state:
                //initPopupWindow(view,"STATE");
                break;*/
            case R.id.ll_group:
                //initPopupWindow(view,"GROUP");
                initIndustryPopupWindow();
                break;
            case R.id.ll_principal:
                intent.putExtra("userLists", (Serializable) principalLists);
                intent.putExtra("at", true);
                intent.putExtra("mTitle","项目负责人");
                startActivityForResult(intent, 100);
                break;
            case R.id.ll_team_member:
                intent.putExtra("userLists", (Serializable) teamMemberrecommendLists);
                intent.putExtra("at", true);
                intent.putExtra("mTitle","小组成员");
                startActivityForResult(intent, 200);
                break;
            case R.id.rl_remind:
                intent.putExtra("userLists", (Serializable) remindLists);
                intent.putExtra("at", true);
                intent.putExtra("mTitle","推荐给谁看");
                startActivityForResult(intent, 400);
                break;
            case R.id.btn_ok:
                if (StringUtils.isEmpty(etName.getText())){
                    SDToast.showShort("请填写项目名称");
                    return;
                }
                /*if (StringUtils.isEmpty(etTeam.getText())){
                    SDToast.showShort("请填写项目团队");
                    return;
                }*/
                if (StringUtils.isEmpty(stateIndexStr)){
                    SDToast.showShort("请选择项目状态");
                    return;
                }
                if (StringUtils.isEmpty(tvDesc.getText()) && voiceFiles.size() == 0){
                    SDToast.showShort("请填写项目介绍或录入语音");
                    return;
                }
                /*if (StringUtils.isEmpty(tvGroup.getText())){
                    SDToast.showShort("请选择行业小组");
                    return;
                }
                if (StringUtils.isEmpty(etMoney.getText())){
                    SDToast.showShort("请填写拟投金额");
                    return;
                }*/
                uploadData();
                break;
            case R.id.tv_desc:
                Intent voiceIntent = new Intent(PotentialProjectsAddActivity.this,PotentialVoiceAddActivity.class);
                voiceIntent.putExtra("voices",(Serializable) voiceFiles);
                voiceIntent.putExtra("desc",StringUtils.isEmpty(tvDesc.getText()) ? "" : tvDesc.getText().toString());
                startActivityForResult(voiceIntent, 300);
                break;
            case R.id.ll_choose:
                if (!isChoose){
                    isChoose = true;
                    ivChoose.setImageResource(R.mipmap.icon_p_p_a_choose2);
                }else {
                    isChoose  = false;
                    ivChoose.setImageResource(R.mipmap.icon_p_p_a_choose);
                }
                break;
            case R.id.ll_important:
                if (!isImportant){
                    isImportant = true;
                    ivImportant.setImageResource(R.mipmap.icon_p_p_a_choose2);
                }else {
                    isImportant  = false;
                    ivImportant.setImageResource(R.mipmap.icon_p_p_a_choose);
                }
                break;
            case R.id.btn_qxb:
                if (StringUtils.isEmpty(etName.getText())){
                    SDToast.showShort("请填写项目名称");
                    return;
                }

                Intent intent9 = new Intent(PotentialProjectsAddActivity.this, ActivityQxbSearch.class);
                intent9.putExtra("companyName", etName.getText().toString());
                startActivity(intent9);

                break;
        }

    }

    private void getNameData(String name){
        etName.removeTextChangedListener(textSwitcher);
        ListHttpHelper.getProjectNameListData(this,name ,new SDRequestCallBack(ProjectNameListBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                ProjectNameListBean bean = (ProjectNameListBean) responseInfo.getResult();
                if (bean.getData().getData() != null && bean.getData().getData().size() > 0) {
                    nameList.clear();
                    nameList.addAll(bean.getData().getData());
                    initPopupWindow(etName);
                }else {
                    etName.addTextChangedListener(textSwitcher);
                }
            }
        });
    }

    private void getProjectStateData(){
        ListHttpHelper.getIDGBaseData(this, "newPotentialStatus", new SDRequestCallBack(IDGTpyeBaseBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                IDGTpyeBaseBean bean = (IDGTpyeBaseBean) responseInfo.getResult();
                datas.clear();
                datas.addAll(bean.getData().getData());
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void getGroupData(){
        ListHttpHelper.getGroupListData(this, new SDRequestCallBack(GroupListBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                GroupListBean bean = (GroupListBean) responseInfo.getResult();
                groupLists.clear();
                groupLists.addAll(bean.getData().getData());
            }
        });
    }

    private void getGroupDataNew() {
        ListHttpHelper.getGroupListDataNew(this, new SDRequestCallBack(NewGroupListBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                NewGroupListBean bean = (NewGroupListBean) responseInfo.getResult();
                newGroupListBeen = bean.getData().getData();
            }
        });
    }

    private void initPopupWindow(View view,final String flag) {
        popuList.clear();
        if ("STATE".equals(flag)){
            for (int i = 0; i < datas.size(); i++)
                popuList.add(datas.get(i).getCodeNameZhCn());
        }else {
            for (int i = 0; i < groupLists.size(); i++)
                popuList.add(groupLists.get(i).getDeptName());
        }
        final ListPopupWindow listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        listPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        ListPopupAdapter listPopupAdapter = new ListPopupAdapter(popuList, PotentialProjectsAddActivity.this);
        listPopupWindow.setAdapter(listPopupAdapter);
        listPopupWindow.setModal(true);
        listPopupWindow.setAnchorView(view);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if ("STATE".equals(flag)){
                    //tvState.setText(datas.get(i).getCodeNameZhCn());
                    stateIndexStr = datas.get(i).getCodeKey();
                }else {
                    tvGroup.setText(groupLists.get(i).getDeptName());
                    groupIndexStr = groupLists.get(i).getDeptId();
                }

                listPopupWindow.dismiss();
            }
        });
        listPopupWindow.show();
    }

    private void initIndustryPopupWindow() {
        final List<String> listStr = new ArrayList<>();
        final List<String> listStrChildren = new ArrayList<>();
        View contentView = LayoutInflater.from(this).inflate(R.layout.popupwindow_loopview, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        ColorDrawable cd = new ColorDrawable(0xd0000000);
        popupWindow.setBackgroundDrawable(cd);
        popupWindow.showAtLocation(contentView, Gravity.CENTER, 0, 0);
        LoopView loopView = (LoopView) contentView.findViewById(R.id.loopView1);
        final LoopView loopView2 = (LoopView) contentView.findViewById(R.id.loopView2);
        Button btnOk = (Button) contentView.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tvGroup.setText(newGroupListBeen.get(indexLoopView1).getChildren().get(indexLoopView2).getDeptName());
                groupIndexStr = newGroupListBeen.get(indexLoopView1).getChildren().get(indexLoopView2).getDeptId();

                popupWindow.dismiss();
            }
        });
        Button btnCancel = (Button) contentView.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

            for (int i = 0; i < newGroupListBeen.size(); i++) {
                listStr.add(newGroupListBeen.get(i).getDeptName());
            }

        loopView.setNotLoop();
        loopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                indexLoopView1 = index;
                indexLoopView2 = 0;
                listStrChildren.clear();

                    for (int i = 0; i < newGroupListBeen.get(index).getChildren().size(); i++) {
                        listStrChildren.add(newGroupListBeen.get(index).getChildren().get(i).getDeptName());
                    }

                loopView2.setItems(listStrChildren);
                loopView2.setCurrentPosition(0);
                loopView2.setTextSize(18);

            }
        });
        loopView.setItems(listStr);
        loopView.setInitPosition(0);
        loopView.setTextSize(18);


            for (int i = 0; i < newGroupListBeen.get(0).getChildren().size(); i++) {
                listStrChildren.add(newGroupListBeen.get(0).getChildren().get(i).getDeptName());
            }

        loopView2.setNotLoop();
        loopView2.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                indexLoopView2 = index;

                groupIndexStr = newGroupListBeen.get(indexLoopView1).getChildren().get(indexLoopView2).getDeptId();

            }
        });
        loopView2.setItems(listStrChildren);
        loopView2.setInitPosition(0);
        loopView2.setTextSize(18);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 300){
                if (data.getStringExtra("flag").equals("voice")){
                    voiceFiles.clear();
                    voiceFiles.addAll((List<VoiceListBean>) data.getSerializableExtra("voices"));
                    if (voiceFiles.size() > 0){
                        ivVoice.setVisibility(View.VISIBLE);
                    }else {
                        ivVoice.setVisibility(View.GONE);
                    }
                }

                descStr = data.getStringExtra("desc");
                tvDesc.setText(descStr);

            }else {
                //返回来的字符串
                final List<PublicUserListBean.DataBeanX.DataBean> userList = (List<PublicUserListBean.DataBeanX.DataBean>) data.getSerializableExtra("userLists");// str即为回传的值
                if (userList != null && userList.size() > 0) {

                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < userList.size(); i++) {

                        builder.append(userList.get(i).getUserName() + "、");
                    }

                    if (requestCode == 100) {
                        principalLists = userList;
                        tvPrincipal.setText(builder.substring(0, builder.length() - 1));
                    } else if (requestCode == 200) {
                        teamMemberrecommendLists = userList;
                        tvTeamMember.setText(builder.substring(0, builder.length() - 1));
                    }else if (requestCode == 400){
                        remindLists = userList;
                        tvRemindName.setText(builder.substring(0, builder.length() - 1));
                    }

                } else {
                    if (requestCode == 100) {
                        tvPrincipal.setText("");
                        principalLists.clear();
                    } else if (requestCode == 200) {
                        tvTeamMember.setText("");
                        teamMemberrecommendLists.clear();
                    }else if (requestCode == 400){
                        tvRemindName.setText("");
                        remindLists.clear();
                    }
                }
            }
        }
    }

    public void uploadData(){
        String strPr = "";
        if (principalLists.size() > 0) {
            StringBuffer sbPr = new StringBuffer();
            for (int i = 0; i < principalLists.size(); i++)
                sbPr.append(principalLists.get(i).getUserId() + ",");
            strPr = sbPr.substring(0, sbPr.length() - 1);
        }

        String strTeam = "";
        if (teamMemberrecommendLists.size() > 0) {
            StringBuffer sbTeam = new StringBuffer();
            for (int i = 0; i < teamMemberrecommendLists.size(); i++)
                sbTeam.append(teamMemberrecommendLists.get(i).getUserId() + ",");
           strTeam = sbTeam.substring(0, sbTeam.length() - 1);
        }

        String remindsStr = "";
        if (remindLists.size() > 0) {
            StringBuffer reminds = new StringBuffer();
            reminds.append("[");
            for (int i = 0; i < remindLists.size(); i++) {
                reminds.append("\"" + remindLists.get(i).getUserAccount() + "\",");
            }
            remindsStr = reminds.substring(0, reminds.length() - 1) + "]";
        }

        String pathsStr = "";
        String audioTimesStr = "";
        if (voiceFiles.size() > 0){
            StringBuffer paths  = new StringBuffer();
            StringBuffer audioTimes = new StringBuffer();
            for (int i = 0; i < voiceFiles.size(); i++){
                paths.append(voiceFiles.get(i).getUrl() + ",");
                audioTimes.append(voiceFiles.get(i).getSecond()+",");
            }
            pathsStr = paths.substring(0,paths.length()-1);
            audioTimesStr = audioTimes.substring(0,audioTimes.length()-1);
        }

        ListHttpHelper.sendPotentialProjectsAddData(this, SPUtils.get(this, USER_ACCOUNT, "").toString(), etName.getText().toString(), stateIndexStr, tvDesc.getText().toString(),groupIndexStr,
                etMoney.getText().toString(), etTeam.getText().toString(),pathsStr,strPr, strTeam,audioTimesStr,isChoose ? "Y" : "N",remindsStr,isImportant ? "2":"",new SDRequestCallBack(IDGBaseBean.class) {
                    @Override
                    public void onRequestFailure(HttpException error, String msg) {
                        SDToast.showShort(msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo) {
                        IDGBaseBean bean = (IDGBaseBean) responseInfo.getResult();
                        if ("success".equals(bean.getData().getCode())){
                            SDToast.showShort("提交成功");
                            finish();
                        }else{
                            SDToast.showShort(bean.getData().getReturnMessage());
                        }
                    }
                });
    }

    private void initPopupWindow(View view) {
        popupNameList.clear();
        for (int i = 0; i < nameList.size(); i++)
            popupNameList.add(nameList.get(i).getProjName());

        if (listPopupWindow != null){
            listPopupAdapter.notifyDataSetChanged();
        }
        listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        listPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        listPopupAdapter = new ListPopupAdapter(popupNameList, this);
        listPopupWindow.setAdapter(listPopupAdapter);
        listPopupWindow.setModal(true);
        listPopupWindow.setAnchorView(view);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /*etName.removeTextChangedListener(textSwitcher);
                etName.setText(nameList.get(i).getProjName());
                etName.setSelection(etName.length());
                projId = nameList.get(i ).getProjId();
                listPopupWindow.dismiss();
                etName.addTextChangedListener(textSwitcher);*/
                listPopupWindow.dismiss();
                Intent intent = new Intent();
                if ("POTENTIAL".equals(nameList.get(i).getProjType()))
                {
                    intent.setClass(PotentialProjectsAddActivity.this, PotentialProjectsDetailActivity.class);
                } else if ("INVESTED".equals(nameList.get(i).getProjType()))
                {
                    intent.setClass(PotentialProjectsAddActivity.this, InvestedProjectsDetailActivity.class);
                } else if ("FOLLOW_ON".equals(nameList.get(i).getProjType()))
                {
                    intent.setClass(PotentialProjectsAddActivity.this, FollowProjectDetailActivity.class);
                } else
                {
                    SDToast.showShort("未知项目类型");
                    return;
                }
                intent.putExtra("projName", nameList.get(i).getProjName());
                intent.putExtra("projId", nameList.get(i).getProjId());
                startActivity(intent);
            }
        });
        listPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                etName.addTextChangedListener(textSwitcher);
            }
        });
        listPopupWindow.show();
    }

    /*class SpeakListen implements View.OnTouchListener
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
                        MyToast.showToast(PotentialProjectsAddActivity.this, getString(R.string.Send_voice_need_sdcard_support));
                        return false;
                    }
                    if (voiceRecordDialog == null)
                    {
                        voiceRecordDialog = new VoiceRecordDialog(PotentialProjectsAddActivity.this, new VoiceRecordDialog
                                .VoiceRecordDialogListener()
                        {
                            @Override
                            public void onRecordFinish(final String path, final int length)
                            {
                                PotentialProjectsAddActivity.this.runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        ivVoice.setFocusable(false);
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
     *//*
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
                        voiceFile = file;
                        ivVoice.setVisibility(View.VISIBLE);
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

    public void playVoice(){
        audioPlayManager = AudioPlayManager.getInstance();
        audioPlayManager.setOnVoiceListener(this);
        ivVoice.setBackgroundColor(0xffffffff);
        ivVoice.setImageResource(R.drawable.play_annex_voice);
        AudioPlayManager.getInstance().play(PotentialProjectsAddActivity.this,voiceFile.getPath() , ivVoice);
        //onClickVoice(mVoiceEntity);
    }

    private void uploadVoiceFile(){
        ListHttpHelper.submitVoiceApi(PotentialProjectsAddActivity.this.getApplication(), voiceFile, new FileUpload.UploadListener() {
            @Override
            public void onSuccess(SDResponseInfo responseInfo, HashMap<String, Object> result) {
                VoiceBaseBean bean = new SDGson().fromJson(responseInfo.getResult().toString(), new TypeToken<VoiceBaseBean>() {}.getType());
                uploadData(bean.getData().get(0).getPath());
            }

            @Override
            public void onProgress(int byteCount, int totalSize) {

            }

            @Override
            public void onFailure(Exception ossException) {

            }
        });
    }*/

    @Override
    protected int getContentLayout() {
        return R.layout.activity_add_potential_projects;
    }


}
