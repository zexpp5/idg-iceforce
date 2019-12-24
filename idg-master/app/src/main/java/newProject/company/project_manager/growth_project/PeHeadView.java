package newProject.company.project_manager.growth_project;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.StringUtils;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.SDToast;

import java.util.ArrayList;
import java.util.List;

import newProject.api.ListHttpHelper;
import newProject.company.project_manager.growth_project.bean.PeDetailBean;
import tablayout.view.textview.FontEditext;
import tablayout.view.textview.FontTextView;
import yunjing.bean.SelectItemBean;
import yunjing.utils.DisplayUtil;

/**
 * Created by Administrator on 2018/3/1.
 */

public class PeHeadView extends LinearLayout
{
    ImageView openEdit;
    FontEditext projectName;
    FontEditext investPresent;
    FontTextView investInvite;
    FontTextView investTrade;
    FontEditext investGov;
    FontEditext investCity;
    FontEditext investState;
    FontTextView connectTime;
    FontEditext responseMan;
    FontTextView groupSituation;
    FontEditext tradeIntroduceText;
    FontTextView btn_emphases_project;
    ImageView arrowImage;
    private LayoutInflater mInflater;
    private View mView;
    private boolean mShow = true;

    private List<SelectItemBean> mInvestList = new ArrayList<>();
    private String[] mInvest = {"未约见", "已约见"};
    private SelectItemBean mInvestBean;
    private String invContactStatus = "unDate";

    private List<SelectItemBean> mCapitalList = new ArrayList<>();
    private String[] mCapital = {"未投资", "已投资"};

    private SelectItemBean mCapitalBean;
    private String invStatus = "unInv";

    private String[] mImportantString = {"是", "是(新建)", "否"};
    private int importantInt = 3;

    private String invDate;
    private List<SelectItemBean> mInduList = new ArrayList<>();
    private SelectItemBean mInduBean;
    private String comIndus;
    private String projName;
    private String mProjectId;
    private Context mContext;

    private String[] mFollowStrings = {"继续跟进", "放弃", "观望"};

    public PeHeadView(Context context)
    {
        super(context);
        mContext = context;
        initViews(context);

    }


    public PeHeadView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        mContext = context;
        initViews(context);
    }

    public PeHeadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initViews(context);
    }

    private void initViews(Context context)
    {
        mInflater = LayoutInflater.from(context);
        mView = mInflater.inflate(R.layout.pe_head_layout, null, false);
        addView(mView);

        openEdit = (ImageView) mView.findViewById(R.id.open_edit);
        projectName = (FontEditext) mView.findViewById(R.id.project_name);
        investPresent = (FontEditext) mView.findViewById(R.id.invest_present);
        investInvite = (FontTextView) mView.findViewById(R.id.invest_invite);
        investTrade = (FontTextView) mView.findViewById(R.id.invest_trade);
        investGov = (FontEditext) mView.findViewById(R.id.invest_gov);
        investCity = (FontEditext) mView.findViewById(R.id.invest_city);
        investState = (FontEditext) mView.findViewById(R.id.invest_state);
        connectTime = (FontTextView) mView.findViewById(R.id.connect_time);
        responseMan = (FontEditext) mView.findViewById(R.id.response_man);
        groupSituation = (FontTextView) mView.findViewById(R.id.group_situation);
        tradeIntroduceText = (FontEditext) mView.findViewById(R.id.trade_introduce_text);
//        arrowImage = (ImageView) mView.findViewById(R.id.arrow_image);

        btn_emphases_project = (FontTextView) mView.findViewById(R.id.btn_emphases_project);
        //arrowImage.setVisibility(GONE);
        DisplayUtil.editTextable(responseMan, false);
        DisplayUtil.editTextable(investState, false);
        controllerLayout(false);
    }

    public ImageView getOpenEdit()
    {
        return openEdit;
    }


    public String getProjectId()
    {
        return mProjectId;
    }

    public FontTextView getInvestInvite()
    {
        return investInvite;
    }

    public String getInvContactStatus()
    {
        return invContactStatus;
    }

    public void getPageInfo(int mEid)
    {

        ListHttpHelper.getPEDetail(mContext, mEid + "", new SDRequestCallBack(PeDetailBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                PeDetailBean bean = (PeDetailBean) responseInfo.getResult();
                if (bean != null && bean.getData() != null)
                {
                    setInfo(bean.getData());
                }
            }
        });
    }

    public void setInfo(PeDetailBean.DataBean data)
    {
        mProjectId = data.getProjId() + "";
        if (data.getProjName() != null)
        {
            projectName.setText(data.getProjName());
        }
        if (data.getInvRound() != null)
        {
            investPresent.setText(data.getInvRound());
        }
        if (data.getInvContactStatus() != null)
        {
            invContactStatus = data.getInvContactStatus();
            if (data.getInvContactStatus().equals("unDate"))
            {
                investInvite.setText(mInvest[0]);
            } else
            {
                investInvite.setText(mInvest[1]);
            }
            /*if (mInvestBean==null){
                if (!TextUtils.isEmpty(data.getInvContactStatus())) {
                    mInvestBean = new SelectItemBean(mClasss[data.getFileCat()], data.getFileCat(), data.getFileCat()+"");
                    mInvestBean.setSelectIndex(data.getFileCat());
                }
            }*/

        }
        if (data.getComIndus() != null)
        {
            investTrade.setText(data.getComIndus());
            comIndus = data.getComIndus();
        }
        if (data.getInvGroup() != null)
        {
            investGov.setText(data.getInvGroup());
        }
        if (data.getInvFlowUp() != null)
        {
            if (data.getInvFlowUp().equals("flowUp"))
            {
                investState.setText(mFollowStrings[0]);
            } else if (data.getInvFlowUp().equals("abandon"))
            {
                investState.setText(mFollowStrings[1]);
            } else
            {
                investState.setText(mFollowStrings[2]);
            }
        }
        if (data.getRegion() != null)
        {
            investCity.setText(data.getRegion());
        }

        if (data.getInvDate() != null)
        {
            connectTime.setText(DateUtils.getFormatDate("yyyy-MM-dd", Long.parseLong(data.getInvDate())));
        }

        if (data.getInvStatus() != null)
        {
            invStatus = data.getInvStatus();
            if (data.getInvStatus().equals("unInv"))
            {
                groupSituation.setText(mCapital[0]);
            } else
            {
                groupSituation.setText(mCapital[1]);
            }
        }

        if (StringUtils.notEmpty(data.getImportantStatus()))
        {
            importantInt = data.getImportantStatus();
            if (data.getImportantStatus() == 1)
            {
                btn_emphases_project.setText(mImportantString[0]);
            } else if (data.getImportantStatus() == 2)
            {
                btn_emphases_project.setText(mImportantString[1]);
            } else if (data.getImportantStatus() == 3)
            {
                btn_emphases_project.setText(mImportantString[2]);
            }
        }

        if (data.getUserName() != null)
        {
            responseMan.setText(data.getUserName());
        }

        if (data.getBizDesc() != null)
        {
            tradeIntroduceText.setText(data.getBizDesc());
        }


    }


    /**
     * 设置是否可以编辑
     *
     * @param isOpen
     */
    public void controllerLayout(boolean isOpen)
    {
        DisplayUtil.editTextable(projectName, isOpen);
        DisplayUtil.editTextable(investPresent, isOpen);
        DisplayUtil.editTextable(investCity, isOpen);
        DisplayUtil.editTextable(investGov, isOpen);
        DisplayUtil.editTextable(tradeIntroduceText, isOpen);

    }


}
