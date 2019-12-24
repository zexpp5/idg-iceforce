package newProject.company.investment;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.SDToast;
import com.utils.ToolMainUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import newProject.api.DetailHttpHelper;
import newProject.api.ListHttpHelper;
import newProject.api.SubmitHttpHelper;
import newProject.bean.CityListBean;
import newProject.company.business_trip.approval.BusinessTripDetailListBean;
import newProject.company.expense.bean.BeanSubmit;
import tablayout.view.textview.FontEditext;
import tablayout.view.textview.FontTextView;
import yunjing.utils.ToastUtils;
import yunjing.view.CustomNavigatorBar;

/**
 * Created by selson on 2018/6/4.
 */
public class ApprovalBusinessTripDetailActivity extends BaseActivity
{
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
    @Bind(R.id.tv_person)
    FontTextView tvPerson;
    @Bind(R.id.fet_comment)
    FontEditext fetComment;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.img_agree)
    ImageView imgAgree;
    @Bind(R.id.tv_tongyi)
    TextView tvTongyi;
    @Bind(R.id.ll_agree)
    RelativeLayout llAgree;
    @Bind(R.id.img_dis_agree)
    ImageView imgDisAgree;
    @Bind(R.id.tv_disagree)
    TextView tvDisagree;
    @Bind(R.id.ll_dis_agree)
    RelativeLayout llDisAgree;
    @Bind(R.id.btn_ok)
    Button btnOk;


    private String mEid;
    private String applyId;

    BusinessTripDetailListBean businessTripDetailListBean = null;

    @Override
    protected void init()
    {
        ButterKnife.bind(this);
        getCityData();
        mEid = getIntent().getStringExtra("id");
        applyId = getIntent().getStringExtra("applyId");

        setAgreeStatus();
    }

    private void setAgreeStatus()
    {
        llAgree.setBackgroundResource(R.drawable.shape_ll_pink_bg);
        llDisAgree.setBackgroundResource(R.drawable.shape_ll_bg);
        flag = "2";
        fetComment.setVisibility(View.GONE);
        imgAgree.setVisibility(View.VISIBLE);
        imgDisAgree.setVisibility(View.GONE);
    }

    private void setDisAgreeStatus()
    {
        llAgree.setBackgroundResource(R.drawable.shape_ll_bg);
        llDisAgree.setBackgroundResource(R.drawable.shape_ll_pink_bg);
        flag = "3";
        fetComment.setVisibility(View.VISIBLE);
        imgDisAgree.setVisibility(View.VISIBLE);
        imgAgree.setVisibility(View.GONE);
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_trip_detail;
    }

    private void getNetData()
    {
        DetailHttpHelper.getBusinessTripApproveDetail(this, mEid, new SDRequestCallBack(BusinessTripDetailListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                ToastUtils.show(ApprovalBusinessTripDetailActivity.this, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                businessTripDetailListBean = (BusinessTripDetailListBean) responseInfo.getResult();
                setData();
            }
        });
    }

    List<CityListBean.DataBean> cityBeanList = new ArrayList<>();

    private void getCityData()
    {
        ListHttpHelper.getCityTrip(ApprovalBusinessTripDetailActivity.this, "", true, new SDRequestCallBack(CityListBean.class)
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

    private void setData()
    {
        if (businessTripDetailListBean == null)
        {
            return;
        }

        if (businessTripDetailListBean.getData() != null)
        {
            BusinessTripDetailListBean.DataBean dataBean = businessTripDetailListBean.getData();
            if (StringUtils.notEmpty(dataBean.getTripType()))
            {
                if (dataBean.getTripType().equals("MAINLAND"))
                    tvTripType.setText("国内");
                else if (dataBean.getTripType().equals("OVERSEA"))
                    tvTripType.setText("海外");
                else
                    tvTripType.setText("地球");
            }

            if (StringUtils.notEmpty(dataBean.getApplyStr()))
                tvPerson.setText(getCityString(dataBean.getApplyStr()));
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
            if (StringUtils.notEmpty(dataBean.getApproveReason()))
                etTripRemark.setText(dataBean.getApproveReason());

            if (StringUtils.notEmpty(dataBean.getBusinessId()))
                applyId = dataBean.getBusinessId() + "";

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
        }

        return cityName;
    }

    //提交审批
    private void posApprrvoal(String inputText, String select)
    {
        if (flag.equals("2"))
        {
            inputText = "同意";
        }

        //2同意、3不同意
        SubmitHttpHelper.postBusinessTripApprove(this, applyId, select, inputText, new SDRequestCallBack(BeanSubmit.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(ApprovalBusinessTripDetailActivity.this, msg);
            }

            @Override
            public void onRequestSuccess(final SDResponseInfo responseInfo)
            {

                final BeanSubmit beanSubmit = (BeanSubmit) responseInfo.getResult();
                if (beanSubmit.getStatus() == 200)
                {
                    ToolMainUtils.getInstance().getUnreadNum(ApprovalBusinessTripDetailActivity.this, ToolMainUtils.TYPE_TRAVE);
                    new Handler().postDelayed(new Runnable()
                    {
                        public void run()
                        {
                            SDToast.showShort("审批成功");
                            setResult(RESULT_OK);
                            finish();
                        }
                    }, 1000);
                }
            }
        });
    }

    String flag = "2";

    @OnClick({R.id.iv_back, R.id.ll_agree, R.id.ll_dis_agree, R.id.btn_ok})
    public void onViewClick(View view)
    {
        switch (view.getId())
        {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_agree:
                setAgreeStatus();
                break;
            case R.id.ll_dis_agree:
                setDisAgreeStatus();
                break;
            case R.id.btn_ok:
                if (flag.equals("3") && StringUtils.isEmpty(fetComment.getText()))
                {
                    SDToast.showShort("请填写审批意见");
                    return;
                }
                posApprrvoal(fetComment.getText().toString(), flag);
                break;
        }
    }
}
