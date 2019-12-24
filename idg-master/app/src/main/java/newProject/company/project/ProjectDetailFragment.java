package newProject.company.project;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base_erp.ERPSumbitBaseFragment;
import com.bumptech.glide.Glide;
import com.cc.emojilibrary.EmojiconEditText;
import com.chaoxiang.base.utils.ImageUtils;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.SDSelectContactActivity;
import com.cxgz.activity.cxim.dialog.VoiceRecordDialog;
import com.cxgz.activity.cxim.http.ImHttpHelper;
import com.cxgz.activity.cxim.manager.AudioPlayManager;
import com.cxgz.activity.cxim.utils.FileUtill;
import com.cxgz.activity.cxim.utils.InfoUtils;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.entity.VoiceEntity;
import com.google.gson.Gson;
import com.http.FileUpload;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.StringUtil;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import newProject.api.DetailHttpHelper;
import newProject.api.SubmitHttpHelper;
import newProject.company.project.bean.ProjectDetailBean;
import newProject.company.project.bean.ProjectSubmitBean;
import tablayout.view.dialog.SelectImgDialog;
import yunjing.utils.DisplayUtil;
import yunjing.utils.FragmentCallBackInterface;
import yunjing.utils.ToastUtils;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.RoundedImageView;

import static android.app.Activity.RESULT_OK;
import static com.utils_erp.FileDealUtil.imgFolder;


/**
 * Created by tujingwu on 2017/10/21.
 * 随便写写吧，心累
 */

public class ProjectDetailFragment extends ERPSumbitBaseFragment
{
    @Bind(R.id.rl_speak_title)
    RelativeLayout rlSpeakTitle;  //聊天顶部名字
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;  //布局
    @Bind(R.id.btn_set_mode_voice)
    Button btnSetModeVoice;     //切换到录音
    @Bind(R.id.btn_set_mode_keyboard)
    Button btnSetModeKeyboard;   //切换到文字
    @Bind(R.id.btn_press_to_speak)
    LinearLayout btnPressToSpeak;  //录音按钮
    @Bind(R.id.et_sendmessage)
    EmojiconEditText etSendmessage; //文字编辑框
    @Bind(R.id.btn_picture)
    Button btnPicture;            //发送图片
    @Bind(R.id.btn_send)
    Button btnSend;                //发送文字
    @Bind(R.id.edittext_layout)
    RelativeLayout edittextLayout;

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

    /**
     * 内容区域的操作。关于轮询，聊天列表，的请求。
     */
    ProjectDetailAdapter projectDetailAdapter = null;
    List<ProjectDetailDataBean> projectDataBeanList = new ArrayList<>();
    //轮询用的最后一条信息
    private String mLastEid = "";

    private Timer mTimerPost = new Timer(true);
    private TimerTask mTimerTaskPost;
    private Handler mHandler;//全局变量

    protected SelectImgDialog selectImgDialog;

    private final int REQUEST_CODE_CAMERA = 10011;
    private final int REQUEST_CODE_LOCAL = 10033;

    private PowerManager.WakeLock wakeLock;

    private FragmentCallBackInterface mCallBack;
    private boolean mHandledPress = false;
    @Bind(R.id.title_bar)
    CustomNavigatorBar mTopBar;


    //个人信息栏
    @Bind(R.id.head_bar_icon)
    RoundedImageView mHead_bar_icon;
    @Bind(R.id.head_bar_name)
    TextView mHead_bar_name;
    @Bind(R.id.head_bar_time)
    TextView mHead_bar_time;
    @Bind(R.id.head_bar_department)
    TextView mHead_bar_department;//部门
    @Bind(R.id.head_bar_job)
    TextView mHead_bar_job;//职位
    @Bind(R.id.head_bar_number)
    TextView mHead_bar_number;//序号


    @Bind(R.id.project_title)
    EditText mET_ProjectTitle;
    @Bind(R.id.project_cooperation)
    TextView mTV_Cooperation;
    @Bind(R.id.affaris_explain_content_ed)
    EditText mET_Explain;//说明

    private long mEid;
    private static final int mREQUEST_CODE = 1;
    private List<String> mEidLists = new ArrayList<>();
    private String mCC;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (!(getActivity() instanceof FragmentCallBackInterface))
        {
            throw new ClassCastException("Hosting activity must implement FragmentCallBackInterface");
        } else
        {
            mCallBack = (FragmentCallBackInterface) getActivity();
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        mCallBack.setSelectedFragment(this);
    }

    public boolean onBackPressed()
    {
        if (!mHandledPress)
        {
            mHandledPress = true;
            return true;
        }
        return false;
    }

    @Override
    protected void init(View view)
    {
        ButterKnife.bind(this, view);

        mHandler = new Handler();
        wakeLock = ((PowerManager) getActivity().getSystemService(Context.POWER_SERVICE))
                .newWakeLock(PowerManager.FULL_WAKE_LOCK, "lock");

        //初始化附件
        view.setOnClickListener(this);

        selectPic();
        recordVoice();
        setFile();

        getBund();
        initTopBar();
        initListener();
        setInfo();
        getNetData();

        setAdapter();

        //聊天的数据
        getProjectChatList();

    }

    private void getBund()
    {
        mEid = getArguments().getLong("EID");
    }

    private void initTopBar()
    {
        mTopBar.setLeftTextVisible(false);
        mTopBar.setMidText(getResources().getString(R.string.im_business_work_submit));
        View.OnClickListener topBarListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (v == mTopBar.getLeftImageView())
                {
                    getActivity().onBackPressed();
                } else if (v == mTopBar.getRightText())
                    checkSubmit();
            }
        };
        mTopBar.setLeftImageOnClickListener(topBarListener);
        mTopBar.setRightTextOnClickListener(topBarListener);
    }

    /**
     * 设置添加页面个人信息
     */
    public void setInfo()
    {
        Glide.with(getActivity())
                .load(DisplayUtil.getUserInfo(getActivity(), 0))
                .error(R.mipmap.contact_icon)
                .into(mHead_bar_icon);
        mHead_bar_name.setText(DisplayUtil.getUserInfo(getActivity(), 1));
        mHead_bar_department.setText(DisplayUtil.getUserInfo(getActivity(), 2));
        mHead_bar_job.setText(DisplayUtil.getUserInfo(getActivity(), 7));

        mHead_bar_time.setText("日期：" + DisplayUtil.getTime());
    }


    private void getNetData()
    {
        DetailHttpHelper.getProjectDetail(getActivity(), mEid + "", new SDRequestCallBack(ProjectDetailBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                ToastUtils.show(getActivity(), msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                ProjectDetailBean pdb = (ProjectDetailBean) responseInfo.getResult();
                if (null != pdb && null != pdb.getData())
                    setData(pdb);
            }

        });

    }


    //设置请求回来的数据
    private void setData(ProjectDetailBean pdb)
    {

        ProjectDetailBean.DataBean.ProjectBean project = pdb.getData().getProject();
        if (null == project)
            return;

        setEditext(project);

        //编号
        if (null != project.getSerNo())
            mHead_bar_number.setText(project.getSerNo());
        //头像
        if (project.getIcon() != null)
        {
            Glide.with(getActivity())
                    .load(project.getIcon())
                    .error(R.mipmap.contact_icon)
                    .into(mHead_bar_icon);
        } else
        {
            Glide.with(getActivity())
                    .load("")
                    .placeholder(R.mipmap.contact_icon)
                    .error(R.mipmap.contact_icon)
                    .into(mHead_bar_icon);
        }
        //名字
        if (project.getYgName() != null)
        {
            mHead_bar_name.setText(project.getYgName());
        }
        //部门
        if (project.getYgDeptName() != null)
        {
            mHead_bar_department.setText(project.getYgDeptName());
        }
        //职位
        if (project.getYgJob() != null)
        {
            mHead_bar_job.setText(project.getYgJob());
        }
        //时间
        if (project.getCreateTime() != null)
        {
            mHead_bar_time.setText("日期：" + project.getCreateTime());
        }

        if (project.getSerNo() != null)
        {
            mHead_bar_number.setText("编号：" + project.getSerNo());
        }

        //协作人
        List<ProjectDetailBean.DataBean.CcListBean> ccList = pdb.getData().getCcList();
        if (null != ccList && ccList.size() > 0)
        {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < ccList.size(); i++)
            {
                if (ccList.get(i) != null && ccList.get(i).getUserName() != null)
                {
                    builder.append(ccList.get(i).getUserName() + "、");
                    mEidLists.add(ccList.get(i).getUserId() + "");
                }
            }
            mTV_Cooperation.setText(builder.substring(0, builder.length() - 1));
        }

        //标题
        if (project.getTitle() != null)
        {
            mET_ProjectTitle.setText(project.getTitle());
        }
        //内容
        if (project.getRemark() != null)
        {
            mET_Explain.setText(project.getRemark());
        }

        mCC = new Gson().toJson(pdb.getData().getCcList());
    }

    private void setEditext(ProjectDetailBean.DataBean.ProjectBean projectBean)
    {
        String userId = DisplayUtil.getUserInfo(getActivity(), 3);
        String applayUserId = projectBean.getYgId() + "";


        if (!userId.equals(applayUserId))
        {//不是自己发布，不可修改
            setNoEnable();
            mTopBar.setRightTextVisible(false);
        }
    }

    private void setNoEnable()
    {
        DisplayUtil.editTextable(mET_ProjectTitle, false);
        DisplayUtil.editTextable(mET_Explain, false);
    }

    private void initListener()
    {
        mTV_Cooperation.setOnClickListener(onclickLisetener);

        btnPressToSpeak.setOnTouchListener(new SpeakListen());
    }

    View.OnClickListener onclickLisetener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.project_cooperation://协作人
                    toContactsList();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 去到全公司联系人列表
     */
    private void toContactsList()
    {
        List<SDUserEntity> userLists = new ArrayList<>();
        if (mEidLists != null && mEidLists.size() > 0)
        {
            userLists = mUserDao.findUserByUserId(mEidLists);
        }
        Intent intent = new Intent(getActivity(), SDSelectContactActivity.class);
        intent.putExtra(SDSelectContactActivity.IS_NEED_REMOVEOWER, false);
        intent.putExtra(SDSelectContactActivity.HIDE_TAB, false);
        intent.putExtra(SDSelectContactActivity.SELECTED_ONE, false);
        //是否能修改
        intent.putExtra(SDSelectContactActivity.IS_CAN_CHANGE, false);
        intent.putExtra(SDSelectContactActivity.INIT_SELECTED_CONTACT, (Serializable) userLists);
        startActivityForResult(intent, mREQUEST_CODE);
    }

    //检查提交
    private void checkSubmit()
    {
        if (TextUtils.isEmpty(mET_ProjectTitle.getText()))
        {
            ToastUtils.show(getActivity(), "邀约项目不能为空！");
            return;
        }

        if (TextUtils.isEmpty(mTV_Cooperation.getText()))
        {
            ToastUtils.show(getActivity(), "协作人不能为空！");
            return;
        }

        if (TextUtils.isEmpty(mET_Explain.getText()))
        {
            ToastUtils.show(getActivity(), "项目说明不能为空！");
            return;
        }

        posDetail();
    }

    //提交修改的详情
    private void posDetail()
    {

        ProjectSubmitBean psb = new ProjectSubmitBean();
        psb.setYgId(DisplayUtil.getUserInfo(getActivity(), 3));
        psb.setYgName(DisplayUtil.getUserInfo(getActivity(), 1));
        psb.setYgDeptId(DisplayUtil.getUserInfo(getActivity(), 6));
        psb.setYgDeptName(DisplayUtil.getUserInfo(getActivity(), 2));
        psb.setYgJob(DisplayUtil.getUserInfo(getActivity(), 7));


        psb.setTitle(mET_ProjectTitle.getText().toString());
        psb.setRemark(mET_Explain.getText().toString());
        psb.setEid(mEid + "");

        try
        {
            psb.setCc(mCC);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        Progress();
        posData(psb);
    }

    private void posData(ProjectSubmitBean psb)
    {
        SubmitHttpHelper.submitProject(getActivity(), new Gson().toJson(psb), new SDRequestCallBack()
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                ToastUtils.show(getContext(), msg);
                progresDialog.dismiss();
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                ToastUtils.show(getContext(), "修改成功");
                progresDialog.dismiss();
                if (mCallBack != null)
                {
                    mCallBack.refreshList();
                }
                getActivity().onBackPressed();
            }
        });
    }

    protected void Progress()
    {
        progresDialog = new ProgressDialog(getActivity());
        progresDialog.setCanceledOnTouchOutside(false);
        progresDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
//                upload.cancel();
            }
        });
        progresDialog.setMessage(getString(R.string.Is_post));
        progresDialog.show();
    }


    @Override
    protected int getContentLayout()
    {
        return R.layout.fragment_project_cooperation;
    }


    //---------------------------------------附件---------------------------------//

    @Override
    public void onSelectedImg(List<String> imgPaths)
    {
        if (imgPaths != null)
        {
            this.imgPaths = imgPaths;
        }
    }

    @Override
    public void onClickLocationFunction(double latitude, double longitude, String address)
    {
    }

    @Override
    public void onClickSendRange(List<SDUserEntity> userEntities)
    {

    }

    @Override
    public void onClickSendPerson(List<SDUserEntity> userEntities)
    {
    }

    @Override
    public void onClickAttach(List<File> pickerFile)
    {
        if (pickerFile != null)
        {
            this.files = pickerFile;
        }
    }

    @Override
    public void onDelAttachItem(View v)
    {

    }

    @Override
    public void onClickVoice(VoiceEntity voiceEntity)
    {
        if (voiceEntity != null)
        {
            this.voice = new File(voiceEntity.getFilePath());
        }
    }

    @Override
    public int getDraftDataType()
    {
        return 0;
    }

    @Override
    public void onClickMoneyFile(String moneyString)
    {

    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btn_set_mode_voice, R.id.btn_set_mode_keyboard, R.id.btn_press_to_speak, R.id
            .btn_picture, R.id.btn_send})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_set_mode_voice:
                changVoice();
                break;
            case R.id.btn_set_mode_keyboard:
                changeText();
                break;
            case R.id.btn_press_to_speak:

                break;
            case R.id.btn_picture:
                changePicture();
                break;
            case R.id.btn_send:
                if (StringUtils.notEmpty(etSendmessage.getText().toString().trim()))
                    setProjectChatFilter(1, "", "", etSendmessage.getText().toString().trim(), "");
                break;
        }
    }

    //录音界面
    private void changVoice()
    {
        btnPressToSpeak.setVisibility(View.VISIBLE);
        edittextLayout.setVisibility(View.GONE);
        btnSend.setVisibility(View.GONE);
    }

    //发送文字界面
    private void changeText()
    {
        btnPressToSpeak.setVisibility(View.GONE);
        edittextLayout.setVisibility(View.VISIBLE);
        btnSend.setVisibility(View.VISIBLE);
    }

    private void changePicture()
    {
        if (selectImgDialog == null)
        {
            selectImgDialog = new SelectImgDialog(getActivity(),
                    new String[]{StringUtil.getResourceString(getActivity(), R.string.camera), StringUtil.getResourceString
                            (getActivity(), R.string.album)});
            selectImgDialog.setOnSelectImgListener(new SelectImgDialog.OnSelectImgListener()
            {
                @Override
                public void onClickCanera(View v)
                {
                    if (!imgFolder.exists())
                    {
                        imgFolder.mkdirs();
                    }
                    cameraImgPath = new File(imgFolder, "sd_img_" + System.currentTimeMillis() + ".jpg");
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraImgPath));
                    startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
                }

                @Override
                public void onClickCancel(View v)
                {
                    //TODO
                }

                @Override
                public void onClickAlum(View v)
                {
                    Intent intent;
                    if (Build.VERSION.SDK_INT < 19)
                    {
                        intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                    } else
                    {
                        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    }
                    startActivityForResult(intent, REQUEST_CODE_LOCAL);
                }
            });
        }
        selectImgDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            switch (requestCode)
            {
                case REQUEST_CODE_CAMERA:
                    if (cameraImgPath != null)
                    {

                        ImageUtils.ImageSize imageInfo = ImageUtils.getImageSize(cameraImgPath.getAbsolutePath());
                        String height = "";
                        String width = "";
                        if (StringUtils.notEmpty(imageInfo))
                        {
                            height = imageInfo.height + "";
                            width = imageInfo.width + "";
                        }
                        imgPaths.add(cameraImgPath.getAbsolutePath());
                        setProjectChatFilter(2, height, width, "", "");
                    }
                    break;

                case REQUEST_CODE_LOCAL:
                    if (data != null)
                    {
                        Uri selectedUri = data.getData();
                        if (selectedUri != null)
                        {
                            String imgPath = "";
                            if (selectedUri.getScheme().equals("content"))
                            {
                                imgPath = InfoUtils.getInstance().queryPathByUri(getActivity(), selectedUri);
                            } else if (selectedUri.getScheme().equals("file"))
                            {
                                imgPath = InfoUtils.getInstance().parseUri(selectedUri);
                            }

                            ImageUtils.ImageSize imageInfo = ImageUtils.getImageSize(imgPath);
                            String height = "";
                            String width = "";
                            if (StringUtils.notEmpty(imageInfo))
                            {
                                height = imageInfo.height + "";
                                width = imageInfo.width + "";
                            }
                            imgPaths.add(imgPath);
                            setProjectChatFilter(2, height, width, "", "");
                        }
                    }
                    break;
            }
        }
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
                        MyToast.showToast(getActivity(), getString(R.string.Send_voice_need_sdcard_support));
                        return false;
                    }
                    if (voiceRecordDialog == null)
                    {
                        voiceRecordDialog = new VoiceRecordDialog(getActivity(), new VoiceRecordDialog
                                .VoiceRecordDialogListener()
                        {
                            @Override
                            public void onRecordFinish(final String path, final int length)
                            {
                                getActivity().runOnUiThread(new Runnable()
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
            SDLogUtil.syso("length=" + length);
            SDLogUtil.syso("filePath=" + path);
            if (path != null)
            {
                if (!isCancelVoiceRecord)
                {
                    File file = new File(path);
                    if (file.exists())
                    {
                        voiceFile = file;
                        setProjectChatFilter(3, "", "", "", length + "");
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
            MyToast.showToast(getActivity(), "录音时间太短");
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

    /**
     * 获取聊天的总的列表
     */
    private void getProjectChatList()
    {
        if (StringUtils.notEmpty(mEid))
            ImHttpHelper.PostProjectChatList(getActivity(), mEid + "", new SDRequestCallBack(ProjectChatBean.class)
            {
                @Override
                public void onRequestFailure(HttpException error, String msg)
                {
                    MyToast.showToast(getActivity(), msg);

                }

                @Override
                public void onRequestSuccess(SDResponseInfo responseInfo)
                {
                    ProjectChatBean projectChatBean = (ProjectChatBean) responseInfo.getResult();
                    if (projectChatBean != null)
                    {
                        if (projectChatBean.getData() != null && projectChatBean.getData().size() > 0)
                        {
                            //转换数据
                            changItemType(projectChatBean.getData());
                            refresh();
                        }
                    }
                }
            });
    }

    //获取最新的那一条
    private void getProjectChatNew()
    {
        if (StringUtils.notEmpty(mEid))
        {
            ImHttpHelper.PostProjectChatNew(getActivity(), mEid + "", mLastEid, new SDRequestCallBack(ProjectNewChatBean.class)
            {
                @Override
                public void onRequestFailure(HttpException error, String msg)
                {
                    SDLogUtil.debug("im_项目协同获取最新的数据失败！");
                }

                @Override
                public void onRequestSuccess(SDResponseInfo responseInfo)
                {
                    ProjectNewChatBean projectChatBean = (ProjectNewChatBean) responseInfo.getResult();
                    if (projectChatBean != null)
                    {
                        if (projectChatBean.getData() != null && projectChatBean.getData().getData().size() > 0)
                        {
                            //转换数据
                            changItemType(projectChatBean.getData().getData());
                            refresh();
                            scrollBottom();
                        }
                    }
                }
            });
        }
    }

    ProjectAddChatFilter projectAddChatFilter;

    private void setProjectChatFilter(int type, String height, String width, String Content, String length)
    {
        if (type == 1)
        {
            imgPaths.clear();
            voiceFile = null;
        } else if (type == 2)
        {
            Content = "";
            voiceFile = null;
        } else if (type == 3)
        {
            Content = "";
            imgPaths.clear();
        }

        projectAddChatFilter = new ProjectAddChatFilter();
        projectAddChatFilter.setBid(mEid + "");
        projectAddChatFilter.setYgId(DisplayUtil.getUserInfo(getActivity(), 3));
        projectAddChatFilter.setType(type + "");
        projectAddChatFilter.setContent(Content);
        projectAddChatFilter.setLength(height);
        projectAddChatFilter.setHeight(length + "");
        projectAddChatFilter.setWidth(width);

        sendProjectChat();

        imgPaths.clear();
        voiceFile = null;
    }

    List<String> imgPaths = new ArrayList<>();
    File voiceFile = null;

    /**
     * 发送协同聊天。
     */
    private void sendProjectChat()
    {
        if (projectAddChatFilter != null)
        {
            ImHttpHelper.postProjectChatSend(getActivity(), new Gson().toJson(projectAddChatFilter), imgPaths, voiceFile, new
                    FileUpload.UploadListener()
                    {
                        @Override
                        public void onSuccess(SDResponseInfo responseInfo, HashMap<String, Object> result)
                        {
                            MyToast.showToast(getActivity(), "发送成功！");
                            etSendmessage.setText("");
                        }

                        @Override
                        public void onProgress(int byteCount, int totalSize)
                        {

                        }

                        @Override
                        public void onFailure(Exception ossException)
                        {
                            MyToast.showToast(getActivity(), "发送失败！");
                        }
                    });
        }

    }

    /**
     * 轮询请求
     **/
    private void startPostTimer()
    {
        mTimerTaskPost = new TimerTask()
        {
            public void run()
            {
                mHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //轮询用的最后一条ID
                        if (projectDataBeanList != null && projectDataBeanList.size() > 0)
                            mLastEid = projectDataBeanList.get(projectDataBeanList.size() - 1).getEid() + "";
                        getProjectChatNew();
                    }
                });
            }
        };
        if (mTimerPost == null)
        {
            mTimerPost = new Timer(true);
        }
        mTimerPost.schedule(mTimerTaskPost, 1000, 5000);
    }

    private void stopTimer()
    {
        if (mTimerPost != null)
        {
            mTimerPost = null;
        }
        if (mTimerTaskPost != null)
        {
            mTimerTaskPost.cancel();
            mTimerTaskPost = null;
        }
    }

    //刷新
    private void refresh()
    {
        projectDetailAdapter.notifyDataSetChanged();
    }

    private void changItemType(List<ProjectDetailDataBean> tmpProjectDataBeanList)
    {
        String myUserId = DisplayUtil.getUserInfo(getActivity(), 3);
        if (StringUtils.notEmpty(myUserId))
        {
            for (int i = 0; i < tmpProjectDataBeanList.size(); i++)
            {
                if (tmpProjectDataBeanList.get(i).getYgId() == Integer.parseInt(myUserId))
                {
//                    我的
                    if (tmpProjectDataBeanList.get(i).getType() == 1)
                    {
                        tmpProjectDataBeanList.get(i).setItemType(ProjectDetailAdapter.MY_TEXT);
                    } else if (tmpProjectDataBeanList.get(i).getType() == 2)
                    {
                        tmpProjectDataBeanList.get(i).setItemType(ProjectDetailAdapter.MY_PIC);
                    } else if (tmpProjectDataBeanList.get(i).getType() == 3)
                    {
                        tmpProjectDataBeanList.get(i).setItemType(ProjectDetailAdapter.MY_VOICE);
                    } else
                    {
                        tmpProjectDataBeanList.get(i).setItemType(ProjectDetailAdapter.MY_TEXT);
                    }
                } else
                {
                    //对方的
                    if (tmpProjectDataBeanList.get(i).getType() == 1)
                    {
                        tmpProjectDataBeanList.get(i).setItemType(ProjectDetailAdapter.HIS_TEXT);
                    } else if (tmpProjectDataBeanList.get(i).getType() == 2)
                    {
                        tmpProjectDataBeanList.get(i).setItemType(ProjectDetailAdapter.HIS_PIC);
                    } else if (tmpProjectDataBeanList.get(i).getType() == 3)
                    {
                        tmpProjectDataBeanList.get(i).setItemType(ProjectDetailAdapter.HIS_VOICE);
                    } else
                    {
                        tmpProjectDataBeanList.get(i).setItemType(ProjectDetailAdapter.HIS_TEXT);
                    }
                }
            }
        }

        projectDataBeanList.addAll(tmpProjectDataBeanList);
    }

    //填充详情
    private void setAdapter()
    {
        projectDetailAdapter = new ProjectDetailAdapter(getActivity(), projectDataBeanList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(projectDetailAdapter);
//        projectDetailAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
//        {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
//            {
//
//            }
//        });
        //滚动到底部
        scrollBottom();
    }

    //自动滚动到底部
    private void scrollBottom()
    {
        if (projectDataBeanList != null && projectDataBeanList.size() > 0)
        {
            try
            {
                recyclerView.scrollToPosition(projectDataBeanList.size() - 1);
            } catch (Exception e)
            {
                SDLogUtil.debug(e + "");
            }
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

    @Override
    public void onPause()
    {
        super.onPause();
        //暂停
        if (wakeLock.isHeld())
            wakeLock.release();
        //停止轮询
        stopTimer();

        try
        {
            stopPlayVoice();
            // 停止录音
            if (voiceRecordDialog.isShowing())
            {
                isCancelVoiceRecord = false;
            }
            voiceRecordDialog.dismiss();
        } catch (Exception e)
        {

        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        wakeLock.acquire();
        startPostTimer();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        //停止轮询定时器
        stopTimer();
        if (null != progresDialog)
            progresDialog = null;
    }


}
