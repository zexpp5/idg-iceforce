package newProject.company.resumption;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.base_erp.ERPSumbitBaseFragment;
import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.NumberUtil;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.entity.VoiceEntity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.SDToast;
import com.utils.slidedatetimepicker.SlideDateTimeListener;
import com.utils.slidedatetimepicker.SlideDateTimePicker;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import newProject.api.ListHttpHelper;
import newProject.company.vacation.bean.VacationInfoBean;
import tablayout.view.textview.FontEditext;
import tablayout.view.textview.FontTextView;
import yunjing.bean.SelectItemBean;
import yunjing.utils.CommonDialog;
import yunjing.utils.DisplayUtil;
import yunjing.utils.FragmentCallBackInterface;
import yunjing.utils.PopupDialog;
import yunjing.view.CustomNavigatorBar;

import static yunjing.utils.PopupDialog.showmLogisticDialogUtil;

/**
 * Created by Administrator on 2017/11/20.
 * 销假
 */

public class ReLeaveFragment extends ERPSumbitBaseFragment
{
    public SimpleDateFormat mFormatterSubmit = new SimpleDateFormat("yyyy-MM-dd");
    private FragmentCallBackInterface mCallBack;
    private boolean mHandledPress = false;
    private CustomNavigatorBar mNavigatorBar;
    private FontTextView mVacationType;
    private FontTextView mAmText, mPmText;
    private FontEditext mExplain;
    private FontTextView mVStartTime, mVEndTime;
    private Button mAddBtn;
    private String mStartTime, mEndTime;
    private int mType;
    private double mMinDay;
    private String mAmInt = "1", mPmInt = "2";
    private List<SelectItemBean> mAmList = new ArrayList<>();
    private SelectItemBean mAmBean, mPmBean;
    private double mLeaveDay;
    private String mTypeString;
    private int mLeaveId;

    @Override
    protected int getContentLayout()
    {
        return R.layout.releave_layout;
    }

    @Override
    public void onSelectedImg(List<String> imgPaths)
    {
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

    }

    @Override
    public void onDelAttachItem(View v)
    {

    }

    @Override
    public void onClickVoice(VoiceEntity voiceEntity)
    {

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
    protected void init(View view)
    {
        view.setOnClickListener(this);
        Bundle bundle=getArguments();
        if(bundle!=null){
            mLeaveDay=bundle.getDouble("DAY");
            mTypeString=bundle.getString("TYPES");
            mType=bundle.getInt("TYPE");
            mLeaveId=bundle.getInt("EID");
        }
        mNavigatorBar = (CustomNavigatorBar) view.findViewById(R.id.title_bar);
        mNavigatorBar.setLeftImageOnClickListener(mOnClickListener);
        mNavigatorBar.setLeftTextOnClickListener(mOnClickListener);
        mNavigatorBar.setMidText("销假申请");
        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(false);
        mNavigatorBar.setRightSecondImageVisible(false);

        mVacationType = (FontTextView) view.findViewById(R.id.vacation_type);
        mVacationType.setOnClickListener(null);
        mVacationType.setText(mTypeString);
        mVStartTime = (FontTextView) view.findViewById(R.id.tv_start_time);
        mVStartTime.setOnClickListener(mOnClickListener);
        mVEndTime = (FontTextView) view.findViewById(R.id.tv_end_time);
        mVEndTime.setOnClickListener(mOnClickListener);
        mAmText = (FontTextView) view.findViewById(R.id.am_text);
        mAmText.setOnClickListener(mOnClickListener);
        mPmText = (FontTextView) view.findViewById(R.id.pm_text);
        mPmText.setOnClickListener(mOnClickListener);
        mExplain = (FontEditext) view.findViewById(R.id.et_vacation_explain);
        mAmText.setVisibility(View.GONE);
        mPmText.setVisibility(View.GONE);
        mAddBtn = (Button) view.findViewById(R.id.add_btn);
        mAddBtn.setText("提  交");
        mAddBtn.setOnClickListener(mOnClickListener);

        getVacationInfo();

    }

    //获取休假信息
    public void getVacationInfo(){
        ListHttpHelper.getVacationInfo(mHttpHelper, new SDRequestCallBack(VacationInfoBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                if (msg!=null) {
                    MyToast.showToast(getActivity(), msg);
                }
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                VacationInfoBean bean= (VacationInfoBean) responseInfo.getResult();
                List<VacationInfoBean.DataBean> datas = bean.getData();
                if (datas!=null && datas.size()>0){
                    for (int i=0;i<datas.size();i++){
                        if (datas.get(i).getCode()==mType){
                            mMinDay=datas.get(i).getMinDay();
                            break;
                        }
                    }
                }
                if (mMinDay==1) {
                    mAmText.setVisibility(View.GONE);
                    mPmText.setVisibility(View.GONE);
                }else{
                    mAmText.setVisibility(View.VISIBLE);
                    mPmText.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    private View.OnClickListener mOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (v == mNavigatorBar.getLeftImageView() || v == mNavigatorBar.getLeftText())
            {
                getActivity().onBackPressed();
            }
            if (v == mAddBtn)
            {
                //提交或者发送
                commitData();
            }
            if (v == mVStartTime)
            {
                new SlideDateTimePicker.Builder(getActivity().getSupportFragmentManager())
                        .setListener(startTimeListener)
                        .setInitialDate(new Date())
                        //.setMinDate(minDate)
                        //.setMaxDate(maxDate)
                        .setIs24HourTime(true)
                        //.setTheme(SlideDateTimePicker.HOLO_DARK)
                        //.setIndicatorColor(Color.parseColor("#990000"))
                        .build()
                        .show();

            }
            if (v == mVEndTime)
            {
                new SlideDateTimePicker.Builder(getActivity().getSupportFragmentManager())
                        .setListener(endTimeListener)
                        .setInitialDate(new Date())
                        //.setMinDate(minDate)
                        //.setMaxDate(maxDate)
                        .setIs24HourTime(true)
                        //.setTheme(SlideDateTimePicker.HOLO_DARK)
                        //.setIndicatorColor(Color.parseColor("#990000"))
                        .build()
                        .show();
            }
            if (v == mAmText)
            {
                showAmType();
            }
            if (v == mPmText)
            {
                showPmType();
            }
        }
    };


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
     *
     */
    public void commitData()
    {
        String explain = mExplain.getText().toString();

        //进行自己判断是否为空
       if (TextUtils.isEmpty(mStartTime))
        {
            SDToast.showShort("请选择开始时间");
            return;
        } else if (TextUtils.isEmpty(mEndTime))
        {
            SDToast.showShort("请选择结束时间");
            return;
        }/*  else if (TextUtils.isEmpty(explain)) {
            SDToast.showShort("请输入备注");
            return;
        }*/


        if (!DateUtils.compareTwoDateM(mStartTime, mEndTime))
        {
            SDToast.showShort("开始时间不能大于结束时间");
            return;
        }

        showProgress();

        postData(mLeaveDay+"", mStartTime, mEndTime, mAmInt, mPmInt, mType + "", explain, mMinDay+"",mLeaveId+"");
    }

    /**
     * 提交
     */
    public void postData(String leaveDay, String leaveStart, String leaveEnd, String startType, String endType,
                         String leaveType, String remark, String minDay, String eid)
    {

        ListHttpHelper.commitReleave(leaveDay, leaveStart, leaveEnd, startType, endType, leaveType,
                remark, minDay,eid, mHttpHelper, new SDRequestCallBack()
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        if (progresDialog != null)
                        {
                            progresDialog.dismiss();
                        }
                        if (msg != null)
                        {
                            SDToast.showShort(msg);
                        } else
                        {
                            SDToast.showShort(R.string.request_fail);
                        }
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        if (progresDialog != null)
                        {
                            progresDialog.dismiss();
                        }
                        if (mCallBack != null)
                        {
                            mCallBack.refreshList();
                        }
                        getActivity().onBackPressed();
                        SDToast.showShort(R.string.request_succeed);
                    }
                });
    }


    protected void showProgress()
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


    private SlideDateTimeListener startTimeListener = new SlideDateTimeListener()
    {

        @Override
        public void onDateTimeSet(Date date)
        {
            mVStartTime.setText(mFormatterSubmit.format(date));
            //用于提交的
            mStartTime = mFormatterSubmit.format(date);
        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel()
        {

        }
    };




    private SlideDateTimeListener endTimeListener = new SlideDateTimeListener()
    {
        @Override
        public void onDateTimeSet(Date date)
        {
            mVEndTime.setText(mFormatterSubmit.format(date));
            //用于提交的
            mEndTime = mFormatterSubmit.format(date);
        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel()
        {

        }
    };

    private void showAmType()
    {
        toAmPmDatas();
        showmLogisticDialogUtil(mAmBean, getActivity(), "时间", mAmList, "0", new CommonDialog.CustomerTypeInterface()
        {
            @Override
            public void menuItemClick(SelectItemBean bean, int index)
            {
                mAmBean = bean;
                mAmBean.setSelectIndex(index);
                mAmText.setText(bean.getSelectString());
                mAmInt = bean.getId();

            }
        });
    }

    private void showPmType()
    {
        toAmPmDatas();
        showmLogisticDialogUtil(mPmBean, getActivity(), "时间", mAmList, "0", new CommonDialog.CustomerTypeInterface()
        {
            @Override
            public void menuItemClick(SelectItemBean bean, int index)
            {
                mPmBean = bean;
                mPmBean.setSelectIndex(index);
                mPmText.setText(bean.getSelectString());
                mPmInt = bean.getId();

            }
        });
    }

    //
    private void toAmPmDatas()
    {
        mAmList.clear();
        mAmList.add(new SelectItemBean("上午", 1, "1"));
        mAmList.add(new SelectItemBean("下午", 2, "2"));
    }

}
