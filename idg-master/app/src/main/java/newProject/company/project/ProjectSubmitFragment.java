package newProject.company.project;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base_erp.ERPSumbitBaseFragment;
import com.bumptech.glide.Glide;
import com.cxgz.activity.cxim.base.SDSelectContactActivity;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.entity.VoiceEntity;
import com.google.gson.Gson;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import newProject.api.SubmitHttpHelper;
import newProject.bean.CCBean;
import newProject.bean.CompanyNoBean;
import newProject.company.project.bean.ProjectSubmitBean;
import newProject.utils.TypeConstant;
import yunjing.utils.DisplayUtil;
import yunjing.utils.FragmentCallBackInterface;
import yunjing.utils.ToastUtils;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.RoundedImageView;

import static android.app.Activity.RESULT_OK;


/**
 * Created by tujingwu on 2017/10/21.
 * 随便写写吧，心累
 */

public class ProjectSubmitFragment extends ERPSumbitBaseFragment
{
    @Bind(R.id.rl_speak_for_project)
    RelativeLayout rlSpeakForProject;
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
    TextView mET_ProjectTitle;
    @Bind(R.id.project_cooperation)
    TextView mTV_Cooperation;
    @Bind(R.id.affaris_explain_content_ed)
    EditText mET_Explain;//说明


    private static final int mREQUEST_CODE = 1;
    private ArrayList<CCBean> mCCList;
    private List<SDUserEntity> mSendList;//选择的协作人

    @Override
    protected void init(View view)
    {
        //初始化附件
        view.setOnClickListener(this);
        selectPic();
        recordVoice();
        setFile();

        initTopBar();
        initListener();
        setInfo();
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
                    getActivity().onBackPressed();
                else if (v == mTopBar.getRightText())
                    excuteSubmit();
            }
        };
        mTopBar.setLeftImageOnClickListener(topBarListener);
        mTopBar.setRightTextOnClickListener(topBarListener);

        //隐藏项目协同的下方聊天的布局
        rlSpeakForProject.setVisibility(View.GONE);
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

        SubmitHttpHelper.getCompanyNo(getActivity(), TypeConstant.XM_CODE[0], new SDRequestCallBack(CompanyNoBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                mHead_bar_number.setText("");
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                CompanyNoBean companyNoBean = (CompanyNoBean) responseInfo.getResult();
                if (null != companyNoBean && null != companyNoBean.getData() && !companyNoBean.getData().isEmpty())
                    mHead_bar_number.setText("编号：" + companyNoBean.getData());
                else
                    mHead_bar_number.setText("");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mREQUEST_CODE && resultCode == RESULT_OK && data != null)
            getCC(data);

    }

    /**
     * 得到协作人
     *
     * @return
     */
    private void getCC(Intent data)
    {
        //返回来的字符串
        mSendList = (List<SDUserEntity>) data.getSerializableExtra(SDSelectContactActivity.SELECTED_DATA);// str即为回传的值

        if (null == mSendList || mSendList.isEmpty())
            return;

        if (mCCList != null)
        {
            mCCList.clear();
            mCCList = null;
        }
        mCCList = new ArrayList<>();
        String ccStr = "";
        for (int i = 0; i < mSendList.size(); i++)
        {
            if (i != mSendList.size() - 1)
                ccStr += mSendList.get(i).getName() + "、";
            else
                ccStr += mSendList.get(i).getName();

            //审批人
            CCBean ccListBean = new CCBean();
            ccListBean.setName(mSendList.get(i).getName());
            ccListBean.setEid(mSendList.get(i).getEid());
            ccListBean.setImAccount(mSendList.get(i).getImAccount());
            mCCList.add(ccListBean);
        }
        mTV_Cooperation.setText(ccStr);
    }


    private void initListener()
    {
        mTV_Cooperation.setOnClickListener(onclickLisetener);
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

    //提交
    private void excuteSubmit()
    {
        if (TextUtils.isEmpty(mET_ProjectTitle.getText()))
        {
            ToastUtils.show(getActivity(), "邀约项目不能为空！");
            return;
        }

        if (TextUtils.isEmpty(mET_Explain.getText()))
        {
            ToastUtils.show(getActivity(), "项目说明不能为空！");
            return;
        }


        ProjectSubmitBean psb = new ProjectSubmitBean();
        psb.setYgId(DisplayUtil.getUserInfo(getActivity(), 3));
        psb.setYgName(DisplayUtil.getUserInfo(getActivity(), 1));
        psb.setYgDeptId(DisplayUtil.getUserInfo(getActivity(), 6));
        psb.setYgDeptName(DisplayUtil.getUserInfo(getActivity(), 2));
        psb.setYgJob(DisplayUtil.getUserInfo(getActivity(), 7));

        psb.setTitle(mET_ProjectTitle.getText().toString());
        psb.setRemark(mET_Explain.getText().toString());


        try
        {
            psb.setCc(DisplayUtil.copyListToStringArray(mCCList));
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
                if (progresDialog != null)
                    progresDialog.dismiss();
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                if (progresDialog != null)
                    progresDialog.dismiss();
                ToastUtils.show(getContext(), "提交成功");
                if (mCallBack != null)
                {
                    mCallBack.refreshList();
                }
                getActivity().onBackPressed();
            }
        });
    }


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

    /**
     * 去到全公司联系人列表，协作人
     */
    private void toContactsList()
    {
        Intent intent = new Intent(getActivity(), SDSelectContactActivity.class);
        intent.putExtra(SDSelectContactActivity.IS_NEED_REMOVEOWER, true);
        intent.putExtra(SDSelectContactActivity.HIDE_TAB, true);
        intent.putExtra(SDSelectContactActivity.NEED_ONCLICK_TO_CONFIRM, true);
        //能选
        intent.putExtra(SDSelectContactActivity.IS_CAN_CHANGE, true);
        //是否是单选
        intent.putExtra(SDSelectContactActivity.SELECTED_ONE, false);
        if (null != mSendList && !mSendList.isEmpty())
            intent.putExtra(SDSelectContactActivity.INIT_SELECTED_CONTACT, (Serializable) mSendList);

        startActivityForResult(intent, mREQUEST_CODE);
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

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (null != progresDialog)
            progresDialog = null;
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
}
