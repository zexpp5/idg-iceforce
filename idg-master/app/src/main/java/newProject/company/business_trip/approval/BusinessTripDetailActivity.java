package newProject.company.business_trip.approval;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.DetailHttpHelper;
import newProject.api.ListHttpHelper;
import newProject.api.SubmitHttpHelper;
import newProject.bean.CityListBean;
import tablayout.view.textview.FontTextView;
import yunjing.utils.ToastUtils;
import yunjing.view.CustomNavigatorBar;

/**
 * Created by selson on 2018/6/4.
 */
public class BusinessTripDetailActivity extends BaseActivity
{
    @Bind(R.id.title_bar)
    CustomNavigatorBar titleBar;
    @Bind(R.id.tv_trip_type)
    FontTextView tvTripType;
    @Bind(R.id.tv_start_from_city)
    FontTextView tvStartFromCity;
    @Bind(R.id.tv_objective_city)
    FontTextView tvObjectiveCity;
    @Bind(R.id.tv_start_time)
    FontTextView tvStartTime;
    @Bind(R.id.tv_end_time)
    FontTextView tvEndTime;
    @Bind(R.id.et_trip_money)
    FontTextView etTripMoney;
    @Bind(R.id.et_trip_remark)
    FontTextView etTripRemark;

    @Bind(R.id.tv_reason_content)
    FontTextView tvReasonContent;
    @Bind(R.id.ll_psyj_main)
    LinearLayout llPsyjMain;

    private String mEid;
    private String signed;

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_business_trip_detail_main;
    }

    @Override
    protected void init()
    {
        ButterKnife.bind(this);
        mEid = getIntent().getStringExtra("EID");
        signed = getIntent().getStringExtra("signed");
        initTopBar();
        getCityData();
    }

    private void initTopBar()
    {
        titleBar.setLeftTextVisible(false);
        titleBar.setRightTextVisible(false);
        titleBar.setMidText("差旅申请");
        View.OnClickListener topBarListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (v == titleBar.getLeftImageView())
                {
                    finish();
                }
            }
        };
        titleBar.setLeftImageOnClickListener(topBarListener);
    }

    private void getNetData()
    {
        DetailHttpHelper.getBusinessTripApproveDetail(this, mEid, new SDRequestCallBack(BusinessTripDetailListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                ToastUtils.show(BusinessTripDetailActivity.this, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                BusinessTripDetailListBean businessTripDetailListBean = (BusinessTripDetailListBean) responseInfo.getResult();
                setData(businessTripDetailListBean);
            }
        });
    }

    List<CityListBean.DataBean> cityBeanList = new ArrayList<>();

    private void getCityData()
    {
        ListHttpHelper.getCityTrip(BusinessTripDetailActivity.this, "", true, new SDRequestCallBack(CityListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDLogUtil.debug("error_", msg);
                getNetData();
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                CityListBean cityListBean = (CityListBean) responseInfo.getResult();
                if (StringUtils.notEmpty(cityListBean) && StringUtils.notEmpty(cityListBean.getData()))
                {
                    cityBeanList.clear();
                    cityBeanList.addAll(cityListBean.getData());
                }
                getNetData();
            }
        });
    }

    private void setData(BusinessTripDetailListBean responseInfo)
    {
        if (responseInfo == null)
        {
            return;
        }

        if (responseInfo.getData() != null)
        {
            BusinessTripDetailListBean.DataBean dataBean = responseInfo.getData();
            if (StringUtils.notEmpty(dataBean.getTripType()))
            {
                if (dataBean.getTripType().equals("MAINLAND"))
                    tvTripType.setText("国内");
                else if (dataBean.getTripType().equals("OVERSEA"))
                    tvTripType.setText("海外");
                else
                    tvTripType.setText("地球");
            }
            if (StringUtils.notEmpty(dataBean.getStartCity()))
                tvStartFromCity.setText(getCityString(dataBean.getStartCity()));
            if (StringUtils.notEmpty(dataBean.getTargetCitys()))
                tvObjectiveCity.setText(getCityListName(dataBean.getTargetCitys()));
            if (StringUtils.notEmpty(dataBean.getStartDate()))
                tvStartTime.setText(dataBean.getStartDate());
            if (StringUtils.notEmpty(dataBean.getEndDate()))
                tvEndTime.setText(dataBean.getEndDate());
            if (StringUtils.notEmpty(dataBean.getBudget()))
                etTripMoney.setText(dataBean.getBudget() + "");
            if (StringUtils.notEmpty(dataBean.getRemark()))
                etTripRemark.setText(dataBean.getRemark());

            llPsyjMain.setVisibility(View.GONE);
            if (signed.equals("3"))
            {
                if (StringUtils.notEmpty(dataBean.getApproveReason()))
                {
                    llPsyjMain.setVisibility(View.VISIBLE);
                    tvReasonContent.setText(dataBean.getApproveReason());
                }
//            else
//            {
//                llPsyjMain.setVisibility(View.GONE);
//                tvReasonContent.setText("无");
//            }
            }
        }
    }

    private String getCityListName(List<String> strings)
    {
        String cityName = "";
        for (int i = 0; i < strings.size(); i++)
        {
            cityName += getCityString(strings.get(i));
            if (i != strings.size() - 1)
            {
                cityName += ",";
            }
        }
        return cityName;
    }

    private String getCityString(String cityType)
    {
        String cityName = "";
        if (StringUtils.notEmpty(cityType))
        {
            cityName = cityType;
        } else
        {
            cityName = "";
        }
        if (cityBeanList.size() > 0)
        {
            Iterator<CityListBean.DataBean> iterator = cityBeanList.iterator();
            while (iterator.hasNext())
            {
                CityListBean.DataBean dataBean = iterator.next();
                if (String.valueOf(dataBean.getId()).equals(cityType))
                {
                    cityName = dataBean.getName();
                }
            }
        } else
        {
            cityName = cityType;
        }
        return cityName;
    }

    //提交审批
    private void posApprrvoal(String inputText, String select)
    {   //2同意、3不同意
        if (select.equals("1"))
        {
            select = "2";
        } else
        {
            select = "3";
        }
        SubmitHttpHelper.postBusinessTripApprove(this, mEid, select, inputText, new SDRequestCallBack()
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                ToastUtils.show(BusinessTripDetailActivity.this, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                ToastUtils.show(BusinessTripDetailActivity.this, "审批成功");
                setResult(2000);
                finish();
            }
        });
    }

}
