package newProject.company.vacation;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.base_erp.ERPSumbitBaseFragment;
import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.DialogUtils;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.NumberUtil;
import com.chaoxiang.base.utils.PickerDialog;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.entity.VoiceEntity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.DialogUtilsIm;
import com.utils.SDToast;
import com.utils.StringUtil;
import com.utils.slidedatetimepicker.SlideDateTimeListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.vacation.bean.DateStartEndBean;
import newProject.company.vacation.bean.VacationInfoBean;
import newProject.ui.annual_meeting.AnnualMeetingMainActivity;
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
 * 休假
 */
public class VacationFragment extends ERPSumbitBaseFragment
{
    public SimpleDateFormat mFormatterSubmit = new SimpleDateFormat("yyyy-MM-dd");
    @Bind(R.id.tv_current_day)
    FontTextView tvCurrentDay;
    @Bind(R.id.tv_current_day2)
    FontTextView tvCurrentDay2;
    @Bind(R.id.tv_current_day3)
    FontTextView tvCurrentDay3;
    @Bind(R.id.tv_total_day)
    FontTextView tvTotalDay;
    @Bind(R.id.tv_total_day2)
    FontTextView tvTotalDay2;
    @Bind(R.id.tv_total_day3)
    FontTextView tvTotalDay3;
    private FragmentCallBackInterface mCallBack;
    private boolean mHandledPress = false;
    private CustomNavigatorBar mNavigatorBar;
    private FontTextView mVacationType;
    private FontTextView mAmText, mPmText;
    private FontEditext mReason;
    private FontEditext mExplain;
    private FontTextView mVStartTime, mVEndTime;
    private Button mAddBtn;
    private String mStartTime, mEndTime;
    private List<SelectItemBean> mCustomTypeList = new ArrayList<>();
    private SelectItemBean mCustomTypeBean;
    private String mType = "0";

    private String mAvailableDay = "0";
    private String mMinDay = "0";
    private String mAmInt = "1", mPmInt = "2";
    private List<SelectItemBean> mAmList = new ArrayList<>();
    private SelectItemBean mAmBean, mPmBean;

    @Override
    protected int getContentLayout()
    {
        return R.layout.vacation_layout;
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

        mNavigatorBar = (CustomNavigatorBar) view.findViewById(R.id.title_bar);
        mNavigatorBar.setLeftImageOnClickListener(mOnClickListener);
        mNavigatorBar.setLeftTextOnClickListener(mOnClickListener);
        mNavigatorBar.setMidText("请假申请");
        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(false);
        mNavigatorBar.setRightSecondImageVisible(false);

        mVacationType = (FontTextView) view.findViewById(R.id.vacation_type);
        mVacationType.setOnClickListener(mOnClickListener);
        mVStartTime = (FontTextView) view.findViewById(R.id.tv_start_time);
        mVStartTime.setOnClickListener(mOnClickListener);
        mVEndTime = (FontTextView) view.findViewById(R.id.tv_end_time);
        mVEndTime.setOnClickListener(mOnClickListener);
        mAmText = (FontTextView) view.findViewById(R.id.am_text);
        mAmText.setVisibility(View.GONE);
        mAmText.setOnClickListener(mOnClickListener);
        mPmText = (FontTextView) view.findViewById(R.id.pm_text);
        mPmText.setOnClickListener(mOnClickListener);
        mPmText.setVisibility(View.GONE);
        mReason = (FontEditext) view.findViewById(R.id.et_vacation_reson);
        mExplain = (FontEditext) view.findViewById(R.id.et_vacation_explain);

        mAddBtn = (Button) view.findViewById(R.id.add_btn);
        mAddBtn.setText("提  交");
        mAddBtn.setOnClickListener(mOnClickListener);


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
                final Calendar calendar = Calendar.getInstance();
                new PickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        mStartTime = DateUtils.clanderTodatetime(calendar, "yyyy-MM-dd");
                        mVStartTime.setText(mStartTime);
                        judgeDate();

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show();

            }
            if (v == mVEndTime)
            {
                final Calendar calendar = Calendar.getInstance();
                new PickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        mEndTime = DateUtils.clanderTodatetime(calendar, "yyyy-MM-dd");
                        mVEndTime.setText(mEndTime);
                        judgeDate();
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show();

            }
            if (v == mAmText)
            {
                showAmType(0);
            }
            if (v == mPmText)
            {
                showPmType(1);
            }

            if (v == mVacationType)
            {
                getVacationInfo();
            }
        }
    };

    //是否冲突
    boolean isConflict = false;

    //休假冲突
    private void judgeDate()
    {
        if (StringUtils.notEmpty(mVStartTime.getText().toString().trim()) && StringUtils.notEmpty(mVEndTime.getText().toString
                ().trim()))
        {
            ListHttpHelper.judgeDate(getActivity(), mVStartTime.getText().toString(), mVEndTime.getText().toString(), new
                    SDRequestCallBack(DateStartEndBean.class)

                    {
                        @Override
                        public void onRequestFailure(HttpException error, String msg)
                        {
                            SDLogUtil.debug("http-" + msg);
                        }

                        @Override
                        public void onRequestSuccess(SDResponseInfo responseInfo)
                        {
                            if (responseInfo != null && responseInfo.getResult() != null)
                            {
                                DateStartEndBean dateStartEndBean = (DateStartEndBean) responseInfo.getResult();
                                if (dateStartEndBean != null)
                                {
                                    if (dateStartEndBean.getData().isIsConflict())
                                    {
                                        isConflict = false;
                                        DialogUtilsIm.dialogPayFinish(getActivity()
                                                , "提 示", dateStartEndBean.getData().getMsg(), "确定", "", new DialogUtilsIm
                                                        .OnYesOrNoListener2()

                                                {
                                                    @Override
                                                    public void onYes()
                                                    {

                                                    }
                                                });
                                    } else
                                    {
                                        isConflict = false;
                                    }

                                }
                            }
                        }
                    });
        }
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
     *
     */
    public void commitData()
    {
        String approval = DisplayUtil.getUserInfo(getActivity(), 8);
        String type = mVacationType.getText().toString();
        String reason = mReason.getText().toString();
        String explain = mExplain.getText().toString();

        //进行自己判断是否为空
        if (TextUtils.isEmpty(type))
        {
            SDToast.showShort("请选择请假类型");
            return;
        } else if (TextUtils.isEmpty(mStartTime))
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

        if (mStartTime.equals(mEndTime) && Integer.parseInt(mAmInt) > Integer.parseInt(mPmInt))
        {
            SDToast.showShort("开始时间不能大于结束时间");
            return;
        }

        if (isConflict)
        {
            SDToast.showShort("请假时间有月会安排");
            return;
        }

        if (mType.equals("22"))
        {
            if (TextUtils.isEmpty(reason))
            {
                SDToast.showShort("请输入请假原因");
                return;
            }
        }

        showProgress();

        postData(approval, mStartTime, mEndTime, mAmInt, mPmInt, mType + "", explain, reason, mAvailableDay, mMinDay);
    }

    /**
     * 提交
     */
    public void postData(String userName, String leaveStart, String leaveEnd, String startType, String endType,
                         String leaveType, String leaveMemo, String leaveReason, String availableDay, String minDay)
    {
        ListHttpHelper.commitVacation(userName, leaveStart, leaveEnd, startType, endType, leaveType,
                leaveMemo, leaveReason, availableDay, minDay, mHttpHelper, new SDRequestCallBack()
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

    List<VacationInfoBean.DataBean>datas=new ArrayList<>();
    //获取休假信息
    public void getVacationInfo()
    {
        ListHttpHelper.getVacationInfo(mHttpHelper, new SDRequestCallBack(VacationInfoBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                if (msg != null)
                {
                    MyToast.showToast(getActivity(), msg);
                }
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                VacationInfoBean bean = (VacationInfoBean) responseInfo.getResult();

                datas = bean.getData();
                mCustomTypeList.clear();
                for (int i = 0; i < datas.size(); i++)
                {
                    mCustomTypeList.add(new SelectItemBean(datas.get(i).getName()+"("+datas.get(i).getAvailableDay()+"天"+")", i,
                            String.valueOf(datas.get(i).getCode()), datas.get(i).getAvailableDay() + "",
                            NumberUtil.reTurnNumber(datas.get(i).getMinDay()), datas.get(i).getTotalDays() + ""));
                }
                if (datas.size() > 0)
                {
                    PopupDialog.showmMonthDialogUtil(mCustomTypeBean, getActivity(), "请假类型", mCustomTypeList, "0", new
                            CommonDialog.CustomerTypeInterface()
                            {
                                @Override
                                public void menuItemClick(SelectItemBean bean, int index)
                                {
                                    mCustomTypeBean = bean;
                                    mCustomTypeBean.setSelectIndex(index);
                                    //类型名字
                                    mVacationType.setText(datas.get(index).getName());
                                    mType = bean.getId();
                                    //可以请的天数
                                    mAvailableDay = bean.getSelectName();
                                    //最小天数
                                    mMinDay = bean.getSelectWay();
                                    if (!TextUtils.isEmpty(bean.getSelectWay()))
                                    {
                                        if (Double.valueOf(bean.getSelectWay()) < 1)
                                        {
                                            mAmText.setVisibility(View.VISIBLE);
                                            mPmText.setVisibility(View.VISIBLE);
                                        } else
                                        {
                                            mAmText.setVisibility(View.GONE);
                                            mPmText.setVisibility(View.GONE);
                                        }
                                    }
                                    String mVacationTypeString = "";
                                    if (bean.getSelectString().indexOf("(") != -1)
                                    {
                                        mVacationTypeString = bean.getSelectString().substring(0, bean.getSelectString()
                                                .indexOf("("));
                                    } else
                                    {
                                        mVacationTypeString = bean.getSelectString();
                                    }

                                    tvCurrentDay.setText("当前可休" + mVacationTypeString + "天数");
                                    tvTotalDay.setText("本年度" + mVacationTypeString + "天数");

                                    tvCurrentDay2.setText(NumberUtil.subZeroAndDot(bean.getSelectName()));
                                    tvTotalDay2.setText(NumberUtil.subZeroAndDot(bean.getSelectString2()));
                                }
                            });
                } else
                {
                    SDToast.showShort("暂无请假信息");
                }
            }
        });
    }


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

    private void showAmType(int amOrPm)
    {
        toAmPmDatas(amOrPm);
        String timeTitleString = "开始时间";
        if (StringUtils.empty(mAmBean))
        {
            mAmBean = new SelectItemBean();
            mAmBean.setId("1");
            mAmBean.setIndex(1);
            mAmBean.setSelectIndex(0);
            mAmBean.setCheck(true);
            mAmBean.setSelectString("上午(09:00)");
        }

        showmLogisticDialogUtil(mAmBean, getActivity(), timeTitleString, mAmList, "0", new CommonDialog.CustomerTypeInterface()
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

    private void showPmType(int amOrPm)
    {
        toAmPmDatas(amOrPm);
        String timeTitleString = "结束时间";
        if (StringUtils.empty(mPmBean))
        {
            mPmBean = new SelectItemBean();
            mPmBean.setId("2");
            mPmBean.setIndex(2);
            mPmBean.setSelectIndex(1);
            mPmBean.setCheck(true);
            mPmBean.setSelectString("下午(18:00)");
        }
        showmLogisticDialogUtil(mPmBean, getActivity(), timeTitleString, mAmList, "0", new CommonDialog.CustomerTypeInterface()
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
    private void toAmPmDatas(int amOrPm)
    {
        mAmList.clear();
        if (amOrPm == 0)
        {
            mAmList.add(new SelectItemBean("上午(09:00)", 1, "1"));
            mAmList.add(new SelectItemBean("下午(14:00)", 2, "2"));
        } else if (amOrPm == 1)
        {
            mAmList.add(new SelectItemBean("下午(13:00)", 1, "1"));
            mAmList.add(new SelectItemBean("下午(18:00)", 2, "2"));
        } else
        {
            mAmList.add(new SelectItemBean("上午", 1, "1"));
            mAmList.add(new SelectItemBean("下午", 2, "2"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
