package newProject.company.business_trip;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.PickerDialog;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.ui.UpdatePasswordActivity;
import com.utils.DialogImUtils;
import com.utils.DialogUtilsIm;
import com.utils.SPUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import newProject.api.ListHttpHelper;
import newProject.bean.CityListBean;
import newProject.company.business_trip.bean.TripFilter;
import tablayout.view.textview.FontEditext;
import tablayout.view.textview.FontTextView;
import yunjing.bean.SelectItemBean;
import yunjing.utils.CommonDialog;
import yunjing.utils.DisplayUtil;
import yunjing.utils.PopupDialog;
import yunjing.view.CustomNavigatorBar;



/**
 * Created by selson on 2018/6/4.
 */
public class BusinessTripAddActivity extends BaseActivity
{
    @Bind(R.id.title_bar)
    CustomNavigatorBar titleBar;
    @Bind(R.id.tv_trip_type)
    FontTextView tvTripType;
    @Bind(R.id.tv_start_from_city)
    FontEditext tvStartFromCity;
    @Bind(R.id.tv_objective_city)
    FontEditext tvObjectiveCity;
    @Bind(R.id.ll_add_city_all)
    LinearLayout llAddCityAll;
    @Bind(R.id.rl_add_city)
    RelativeLayout rlAddCity;
    @Bind(R.id.tv_start_time)
    FontTextView tvStartTime;
    @Bind(R.id.tv_end_time)
    FontTextView tvEndTime;
    @Bind(R.id.et_trip_money)
    FontEditext etTripMoney;
    @Bind(R.id.et_trip_remark)
    FontEditext etTripRemark;
    @Bind(R.id.add_btn)
    Button addBtn;

    private String[] mtypeString = {"国内", "海外"};
    private String startCity = "";
    private String objectiveCity = "";

    @Override
    protected void init()
    {
        ButterKnife.bind(this);
        addBtn.setText("提 交");
        initTitle();
        initView();
    }

    private void initView()
    {
//        DisplayUtil.getMoneyFilter
//                (BusinessTripAddActivity.this, etTripMoney)
//        etTripMoney.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
    }

    private void setEdittextEnabled(boolean isEnabled)
    {
        if (isEnabled)
        {
            tvStartFromCity.setEnabled(true);
            tvObjectiveCity.setEnabled(true);
        } else
        {
            tvStartFromCity.setEnabled(false);
            tvObjectiveCity.setEnabled(false);
        }
    }

    private void initTitle()
    {
        titleBar.setMidText(getResources().getString(R.string.im_work_travel_add_title));
        titleBar.setLeftImageVisible(true);
        titleBar.setRightSecondImageVisible(false);
        titleBar.setRightImageVisible(false);
        titleBar.setLeftImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        tvStartFromCity.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                boolean isReturn = false;
                if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    if (typeString.equals("MAINLAND"))
                    {
                        DisplayUtil.hideInputSoft(BusinessTripAddActivity.this);
                        getCityData(tvStartFromCity);
                        isReturn = true;
                    } else
                    {
                        isReturn = false;
                    }
                }
                return isReturn;
            }
        });

        tvObjectiveCity.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                boolean isReturn = false;
                if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    if (typeString.equals("MAINLAND"))
                    {
                        DisplayUtil.hideInputSoft(BusinessTripAddActivity.this);
                        getCityData(tvObjectiveCity);
                        isReturn = true;
                    } else
                    {
                        isReturn = false;
                    }
                }
                return isReturn;
            }
        });

        tvStartFromCity.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                if (StringUtils.empty(tvTripType.getText().toString().trim()))
                {
                    tvStartFromCity.clearFocus();
                    MyToast.showToast(BusinessTripAddActivity.this, "请选择出差类型");
                    hideSoftKeyboard();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (StringUtils.empty(tvTripType.getText().toString().trim()))
                {
                    tvStartFromCity.removeTextChangedListener(this);
                    tvStartFromCity.setText("");
                    tvStartFromCity.addTextChangedListener(this);
                }
            }
        });

        tvObjectiveCity.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                if (StringUtils.empty(tvTripType.getText().toString().trim()))
                {
                    tvObjectiveCity.clearFocus();
                    hideSoftKeyboard();
                    MyToast.showToast(BusinessTripAddActivity.this, "请选择出差类型");
                }
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (StringUtils.empty(tvTripType.getText().toString().trim()))
                {
                    tvObjectiveCity.removeTextChangedListener(this);
                    tvObjectiveCity.setText("");
                    tvObjectiveCity.addTextChangedListener(this);
                }
            }
        });
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_business_trip;
    }

    @OnClick({R.id.tv_trip_type, R.id.rl_add_city, R.id.tv_start_time, R.id.tv_end_time, R.id.add_btn})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.tv_trip_type:
                showDialog();
                break;
            case R.id.rl_add_city:
                addCityView();
                break;
            case R.id.tv_start_time:

                final Calendar calendar = Calendar.getInstance();
                new PickerDialog(BusinessTripAddActivity.this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog
                        .OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        tvStartTime.setText(DateUtils.clanderTodatetime(calendar, "yyyy-MM-dd"));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show();
                break;
            case R.id.tv_end_time:
                final Calendar calendar2 = Calendar.getInstance();
                new PickerDialog(BusinessTripAddActivity.this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog
                        .OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        calendar2.set(year, monthOfYear, dayOfMonth);
                        tvEndTime.setText(DateUtils.clanderTodatetime(calendar2, "yyyy-MM-dd"));
                    }
                }, calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DATE)).show();
                break;
            case R.id.add_btn:
                postData();
                break;
        }
    }

    private List<CityView> mViewList = new ArrayList<>();

    private void addCityView()
    {
        final CityView cityView = new CityView(BusinessTripAddActivity.this);
        cityView.setOnDelListener(new CityView.onDelListener()
        {
            @Override
            public void onDel(int Position)
            {
                Iterator<CityView> listCityViewIterator = mViewList.iterator();
                while (listCityViewIterator.hasNext())
                {
                    CityView cityView1 = listCityViewIterator.next();
                    if (cityView1.getmPosition() == Position)
                    {
                        listCityViewIterator.remove();
//                        mViewList.remove(cityView1);
                        llAddCityAll.removeView(cityView1);
                        SDLogUtil.debug("数量：" + mViewList.size());
                    }
                }
            }
        });

        cityView.setmPosition(mViewList.size() + 1);
        mViewList.add(cityView);
        llAddCityAll.addView(cityView);

        cityView.getTv_objective_city().setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                boolean isReturn = false;
                if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    if (typeString.equals("MAINLAND"))
                    {
                        DisplayUtil.hideInputSoft(BusinessTripAddActivity.this);
                        getCityData(cityView.getTv_objective_city());
                        isReturn = true;
                    } else
                    {
                        isReturn = false;
                    }
                }
                return isReturn;
            }
        });
    }

    private List<SelectItemBean> mCapitalList = new ArrayList<>();
    private SelectItemBean mImportantBean;
    String typeString = "";

    private void toImportantDatas()
    {
        mCapitalList.clear();
        mCapitalList.add(new SelectItemBean(mtypeString[0], 0, "MAINLAND"));
        mCapitalList.add(new SelectItemBean(mtypeString[1], 1, "OVERSEA"));
    }

    private void showDialog()
    {
        toImportantDatas();
        PopupDialog.showmLogisticDialogUtil(mImportantBean, this, "出差类型", mCapitalList, "0", new CommonDialog
                .CustomerTypeInterface()
        {
            @Override
            public void menuItemClick(SelectItemBean bean, int index)
            {
                mImportantBean = bean;
                mImportantBean.setSelectIndex(index);
                tvTripType.setText(mtypeString[index]);
                typeString = bean.getId();
            }
        });
    }

    private void showCityDialog(final EditText view)
    {
        String title = "城市";
        if (view == tvStartFromCity)
        {
            title = "出发城市";
        }
        if (view == tvObjectiveCity)
        {
            title = "目的城市";
        }
//        mCitySelectList = mCitySelectList.subList(0, 50);
        PopupDialog.showCommonDialogUtil(mCityBean, this, title, mCitySelectList, true, new CommonDialog
                .CustomerTypeInterface()
        {
            @Override
            public void menuItemClick(SelectItemBean bean, int index)
            {
                mCityBean = bean;
                mCityBean.setSelectIndex(index);
                view.setText(bean.getSelectString());
                if (view == tvStartFromCity)
                {
                    startCity = bean.getId();
                }

                if (view == tvObjectiveCity)
                {
                    objectiveCity = bean.getId();
                }

                for (int i = 0; i < mViewList.size(); i++)
                {
                    if (mViewList.get(i).getTv_objective_city() == view)
                    {
                        mViewList.get(i).setCityId(bean.getId());
                    }
                }
            }
        });
    }

    private String getCityId()
    {
        String cityString = "";
        List<String> listString = new ArrayList<>();

        if (StringUtils.notEmpty(listString))
        {
            listString.add(objectiveCity);
        }

        for (int i = 0; i < mViewList.size(); i++)
        {
            if (StringUtils.notEmpty(mViewList.get(i).getCityId()))
                listString.add(mViewList.get(i).getCityId());
        }
        cityString = SDGson.toJson(listString);
        return cityString;
    }

    private String getCityId2()
    {
        String cityString = "";
        List<String> listString = new ArrayList<>();
        listString.add(tvObjectiveCity.getText().toString().trim());
        for (int i = 0; i < mViewList.size(); i++)
        {
            if (StringUtils.notEmpty(mViewList.get(i).getTvObjective()))
                listString.add(mViewList.get(i).getTvObjective());
        }
        cityString = SDGson.toJson(listString);
        return cityString;
    }

    private void getCityData(final EditText view)
    {
        ListHttpHelper.getCityTrip(BusinessTripAddActivity.this, "", true, new SDRequestCallBack(CityListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDLogUtil.debug("error_", msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                CityListBean cityListBean = (CityListBean) responseInfo.getResult();
                if (StringUtils.notEmpty(cityListBean) && StringUtils.notEmpty(cityListBean.getData()))
                {
                    toAddCityDatas(cityListBean.getData());
                    showCityDialog(view);
                }
            }
        });
    }


    private List<SelectItemBean> mCitySelectList = new ArrayList<>();
    private SelectItemBean mCityBean;

    private void toAddCityDatas(List<CityListBean.DataBean> mCityList)
    {
        mCitySelectList.clear();
        for (int i = 0; i < mCityList.size(); i++)
        {
            mCitySelectList.add(new SelectItemBean(mCityList.get(i).getName(), i, mCityList.get(i).getId() + ""));
        }
    }

    private void postData()
    {
        if (StringUtils.empty(typeString))
        {
            MyToast.showToast(BusinessTripAddActivity.this, "请选择出差类型");
            return;
        }
        if (StringUtils.empty(tvStartFromCity.getText().toString()))
        {
            MyToast.showToast(BusinessTripAddActivity.this, "请选择出发城市");
            return;
        }
        if (StringUtils.empty(tvObjectiveCity.getText().toString()))
        {
            MyToast.showToast(BusinessTripAddActivity.this, "请选择目的城市");
            return;
        }
        if (StringUtils.empty(tvStartTime.getText().toString()))
        {
            MyToast.showToast(BusinessTripAddActivity.this, "请选择出发日期");
            return;
        }
        if (StringUtils.empty(tvEndTime.getText().toString()))
        {
            MyToast.showToast(BusinessTripAddActivity.this, "请选择结束日期");
            return;
        }

        if (StringUtils.empty(etTripRemark.getText().toString()))
        {
            MyToast.showToast(BusinessTripAddActivity.this, "请输入出差事由");
            return;
        }


        TripFilter tripFilter = new TripFilter();
        tripFilter.setTripType(typeString);
        if (typeString.equals("MAINLAND"))
        {
            String objectiveCity = getCityId();

            if (StringUtils.notEmpty(startCity))
            {
                tripFilter.setStartCity(startCity);
            } else
            {
                tripFilter.setStartCity(tvStartFromCity.getText().toString().trim());
            }
            if (StringUtils.notEmpty(objectiveCity))
            {
                tripFilter.setTargetCitys(objectiveCity);
            } else
            {
                tripFilter.setTargetCitys(tvObjectiveCity.getText().toString().trim());
            }
        } else
        {
            tripFilter.setStartCity(tvStartFromCity.getText().toString().trim());

            String objectiveCity = getCityId2();

            if (StringUtils.notEmpty(objectiveCity))
            {
                tripFilter.setTargetCitys(objectiveCity);
            } else
            {
                tripFilter.setTargetCitys(tvObjectiveCity.getText().toString().trim());
            }
        }

        tripFilter.setStartDate(tvStartTime.getText().toString().trim());
        tripFilter.setEndDate(tvEndTime.getText().toString().trim());
        tripFilter.setBudget(etTripMoney.getText().toString().trim());
        tripFilter.setRemark(etTripRemark.getText().toString().trim());
        tripFilter.setApply(DisplayUtil.getUserInfo(BusinessTripAddActivity.this, 5));

        ListHttpHelper.postAddTrip(BusinessTripAddActivity.this, SDGson.toJson(tripFilter), new SDRequestCallBack()
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(BusinessTripAddActivity.this, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                if (responseInfo.getStatus() == 200)
                {
                    DialogImUtils.getInstance().dialogCommom(BusinessTripAddActivity.this, "提示", "提交成功"
                            , "关闭", "", new DialogImUtils.OnYesOrNoListener()
                            {
                                @Override
                                public void onYes()
                                {
                                    finish();
                                }

                                @Override
                                public void onNo()
                                {

                                }
                            });
                } else
                {
                    MyToast.showToast(BusinessTripAddActivity.this, responseInfo.getMsg());
                }
            }
        });
    }
}
