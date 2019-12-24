package newProject.company.invested_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.chaoxiang.base.config.Constants;
import com.chaoxiang.base.utils.ImageUtils;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseFragment;
import com.cxgz.activity.cxim.person.SDPersonalAlterActivity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.SPUtils;

import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import newProject.api.ListHttpHelper;
import newProject.company.invested_project.bean.BeanBaseInfo;
import newProject.company.invested_project.editinfo.ActivityBaseInfo;
import tablayout.view.textview.FontEditext;
import tablayout.view.textview.FontTextView;
import yunjing.utils.DisplayUtil;
import yunjing.view.StatusTipsView;

import static com.injoy.idg.R.id.ll_water_01;

/**
 * Created by selson on 2019/4/28.
 * 已投资项目-基础资料
 */
public class FragmentBaseInfo extends BaseFragment
{
    @Bind(R.id.tv_project_name)
    FontEditext tvProjectName;
    @Bind(R.id.tv_city)
    FontEditext tvCity;
    @Bind(R.id.tv_industry_classification)
    FontEditext tvIndustryClassification;
    @Bind(R.id.tv_company_people_number)
    FontEditext tvCompanyPeopleNumber;
    @Bind(R.id.tv_industry_group)
    FontEditext tvIndustryGroup;
    @Bind(R.id.tv_partner)
    FontTextView tvPartner;
    @Bind(R.id.tv_project_preside)
    FontTextView tvProjectPreside;
    @Bind(R.id.tv_group_number)
    FontTextView tvGroupNumber;
    @Bind(R.id.tv_contributer)
    FontTextView tvContributer;
    @Bind(R.id.tv_investigate_preside)
    FontTextView tvInvestigatePreside;
    @Bind(R.id.tv_financing_round)
    FontEditext tvFinancingRound;
    @Bind(R.id.tv_project_lawyer)
    FontTextView tvProjectLawyer;
    @Bind(R.id.tv_source_person)
    FontEditext tvSourcePerson;
    @Bind(R.id.tv_source_way)
    FontEditext tvSourceWay;
    @Bind(R.id.view_loading)
    StatusTipsView viewLoading;
    @Bind(R.id.scrollView)
    ScrollView scrollView;
    @Bind(R.id.tv_director_has)
    FontTextView tvDirectorHas;
    @Bind(R.id.tv_director_info)
    FontTextView tvDirectorInfo;
    @Bind(R.id.tv_observer)
    FontTextView tvObserver;
    @Bind(R.id.tv_esg)
    FontTextView tvEsg;

    @Bind(R.id.ll_water)
    LinearLayout ll_water;

    private String mEid = "";
    private String userName = "";

    public static Fragment newInstance(String eid)
    {
        Bundle args = new Bundle();
        args.putString(Constants.PROJECT_EID, eid);
        FragmentBaseInfo fragment = new FragmentBaseInfo();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getContentLayout() {
        return R.layout.fragment_info_base_main;
    }

    @Override
    protected void init(View view)
    {
        ButterKnife.bind(this, view);
        getEid();
        setEdtEnable(false);

        if (!DisplayUtil.hasNetwork(getContext()))
        {
            viewLoading.showAccessFail();
        } else
        {
            reFresh();
        }

        viewLoading.showLoading();
        viewLoading.setOnVisibleChangeListener(new StatusTipsView.OnVisibleChangeListener()
        {
            @Override
            public void onVisibleChanged(boolean visible)
            {
                if (scrollView != null)
                {
                    scrollView.setVisibility(visible ? View.GONE : View.VISIBLE);
                }
            }
        });
        viewLoading.setOnRetryListener(new StatusTipsView.OnRetryListener()
        {
            @Override
            public void onRetry()
            {
                viewLoading.showLoading();
                reFresh();
            }
        });
    }

    private void getEid()
    {
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            mEid = bundle.getString(Constants.PROJECT_EID, "");
        }
        userName = loginUserName;
    }

    BeanBaseInfo.DataBeanX.DataBean baseInfoBean = null;

    public void StartActivityInfoAc()
    {
        Intent intent = new Intent(getActivity(), ActivityBaseInfo.class);
        if (StringUtils.notEmpty(baseInfoBean))
        {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.PROJECT_EID, mEid);
            bundle.putSerializable("baseInfoBean", (Serializable) baseInfoBean);
            intent.putExtras(bundle);
        }
        getActivity().startActivityForResult(intent, InVestedProjectInfoActivity.mRequestCode);
    }

    private void getPageInfo()
    {
        if (StringUtils.notEmpty(mEid) && StringUtils.notEmpty(userName))
            ListHttpHelper.getInvestementBaseInfo(getActivity(), mEid, userName, new SDRequestCallBack(BeanBaseInfo.class)
            {
                @Override
                public void onRequestFailure(HttpException error, String msg)
                {
                    viewLoading.showNoContent("暂无数据");
                }

                @Override
                public void onRequestSuccess(SDResponseInfo responseInfo)
                {
                    BeanBaseInfo bean = (BeanBaseInfo) responseInfo.getResult();
                    if (bean != null && bean.getData() != null)
                    {
                        setIsShow(true, "");
                        if (bean.getData().getData() != null)
                            setInfo(bean.getData().getData());
                    } else
                    {
                        setIsShow(false, "");
                    }

                }
            });
    }

    private void setIsShow(boolean isShow, String msg)
    {
        if (isShow)
        {
            viewLoading.hide();
        } else
        {
            viewLoading.showNoContent(msg);
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void setInfo(final BeanBaseInfo.DataBeanX.DataBean dataBean)
    {
        String myNickName = DisplayUtil.getUserInfo(getActivity(), 1);
        if (StringUtils.notEmpty(myNickName))
        {
            ImageUtils.waterMarking(getActivity(), true, false, ll_water, myNickName);
        }

        baseInfoBean = dataBean;

        if (StringUtils.notEmpty(dataBean.getProjName()))
            tvProjectName.setText(dataBean.getProjName());

        if (StringUtils.notEmpty(dataBean.getHeadCityStr()))
            tvCity.setText(dataBean.getHeadCityStr());  //城市。

        if (StringUtils.notEmpty(dataBean.getComInduStr()))
            tvIndustryClassification.setText(dataBean.getComInduStr());

        if (StringUtils.notEmpty(dataBean.getHeadCount()))
            tvCompanyPeopleNumber.setText(dataBean.getHeadCount());

        if (StringUtils.notEmpty(dataBean.getIndusGroupStr()))
            tvIndustryGroup.setText(dataBean.getIndusGroupStr());

        if (StringUtils.notEmpty(dataBean.getProjPartnerNames()))
            tvPartner.post(new Runnable()
            {
                @Override
                public void run()
                {
                    if (tvPartner.getLineCount() > 1)
                    {
                        int num = reTurnNum(dataBean.getProjPartnerNames());
                        tvPartner.setText(num + "人");
                        isPartner = true;
                    } else
                    {
                        tvPartner.setText(dataBean.getProjPartnerNames());
                    }
                }
            });

        if (StringUtils.notEmpty(dataBean.getProjManagerNames()))
            tvProjectPreside.post(new Runnable()
            {
                @Override
                public void run()
                {
                    if (tvProjectPreside.getLineCount() > 1)
                    {
                        int num = reTurnNum(dataBean.getProjManagerNames());
                        tvProjectPreside.setText(num + "人");
                        isProjManager = true;
                    } else
                    {
                        tvProjectPreside.setText(dataBean.getProjManagerNames());
                    }
                }
            });

        if (StringUtils.notEmpty(dataBean.getProjTeamNames()))
            tvGroupNumber.post(new Runnable()
            {
                @Override
                public void run()
                {
                    if (tvGroupNumber.getLineCount() > 1)
                    {
                        int num = reTurnNum(dataBean.getProjTeamNames());
                        tvGroupNumber.setText(num + "人");
                        isProjTeam = true;
                    } else
                    {
                        tvGroupNumber.setText(dataBean.getProjTeamNames());
                    }
                }
            });

        if (StringUtils.notEmpty(dataBean.getContributerNames()))
            tvContributer.post(new Runnable()
            {
                @Override
                public void run()
                {
                    if (tvContributer.getLineCount() > 1)
                    {
                        int num = reTurnNum(dataBean.getContributerNames());
                        tvContributer.setText(num + "人");
                        isContributer = true;
                    } else
                    {
                        tvContributer.setText(dataBean.getContributerNames());
                    }
                }
            });

        if (StringUtils.notEmpty(dataBean.getProjDDManagerNames()))
            tvInvestigatePreside.post(new Runnable()
            {
                @Override
                public void run()
                {
                    if (tvInvestigatePreside.getLineCount() > 1)
                    {
                        int num = reTurnNum(dataBean.getProjDDManagerNames());
                        tvInvestigatePreside.setText(num + "人");
                        isProjDDManager = true;
                    } else
                    {
                        tvInvestigatePreside.setText(dataBean.getProjDDManagerNames());
                    }
                }
            });

        if (StringUtils.notEmpty(dataBean.getCurrentRound()))
            tvFinancingRound.setText(dataBean.getCurrentRound());    //融资轮次

        if (StringUtils.notEmpty(dataBean.getProjLawyerNames()))
            tvProjectLawyer.post(new Runnable()
            {
                @Override
                public void run()
                {
                    if (tvProjectLawyer.getLineCount() > 1)
                    {
                        int num = reTurnNum(dataBean.getProjLawyerNames());
                        tvProjectLawyer.setText(num + "人");
                        isProjLawyer = true;
                    } else
                    {
                        tvProjectLawyer.setText(dataBean.getProjLawyerNames());
                    }
                }
            });

        if (StringUtils.notEmpty(dataBean.getSeatFlag()))
        {
            if (dataBean.getSeatFlag().equalsIgnoreCase("Y"))
                tvDirectorHas.setText("是");
            else
                tvDirectorHas.setText("否");
        } else
        {

        }

        if (StringUtils.notEmpty(dataBean.getEsgFlag()))
        {
            if (dataBean.getEsgFlag().equalsIgnoreCase("Y"))
                tvEsg.setText("是");
            else
                tvEsg.setText("否");
        } else
        {

        }

        if (StringUtils.notEmpty(dataBean.getProjDirectorNames()))
            tvDirectorInfo.post(new Runnable()
            {
                @Override
                public void run()
                {
                    if (tvDirectorInfo.getLineCount() > 1)
                    {
                        int num = reTurnNum(dataBean.getProjDirectorNames());
                        tvDirectorInfo.setText(num + "人");
                        isDirector = true;
                    } else
                    {
                        tvDirectorInfo.setText(dataBean.getProjDirectorNames());
                    }
                }
            });

        if (StringUtils.notEmpty(dataBean.getProjObserverNames()))
            tvObserver.post(new Runnable()
            {
                @Override
                public void run()
                {
                    if (tvObserver.getLineCount() > 1)
                    {
                        int num = reTurnNum(dataBean.getProjObserverNames());
                        tvObserver.setText(num + "人");
                        isObserver = true;
                    } else
                    {
                        tvObserver.setText(dataBean.getProjObserverNames());
                    }
                }
            });

        if (StringUtils.notEmpty(dataBean.getProjSourPer()))
            tvSourcePerson.setText(dataBean.getProjSourPer());

        if (StringUtils.notEmpty(dataBean.getProjSourStr()))
            tvSourceWay.setText(dataBean.getProjSourStr());    //来源渠道
    }

    private int reTurnNum(String perStr)
    {
        int intNum = 0;
        if (perStr.contains(","))
        {
            String[] strList = perStr.split(",");
            intNum = strList.length;
        }
        return intNum;
    }

    public void reFresh()
    {
        getPageInfo();
    }

    private void setEdtEnable(boolean isEnable)
    {
        tvProjectName.setEnabled(isEnable);
        tvCity.setEnabled(isEnable);
        tvIndustryClassification.setEnabled(isEnable);
        tvCompanyPeopleNumber.setEnabled(isEnable);
        tvIndustryGroup.setEnabled(isEnable);
        tvFinancingRound.setEnabled(isEnable);
        tvSourcePerson.setEnabled(isEnable);
        tvSourceWay.setEnabled(isEnable);
    }

    private boolean isPartner = false;
    private boolean isProjManager = false;
    private boolean isProjTeam = false;
    private boolean isContributer = false;
    private boolean isProjDDManager = false;
    private boolean isProjLawyer = false;
    private boolean isDirector = false;
    private boolean isObserver = false;

    @OnClick({R.id.tv_partner, R.id.tv_project_preside, R.id.tv_group_number, R.id.tv_contributer,
            R.id.tv_investigate_preside,
            R.id.tv_project_lawyer, R.id.view_loading, R.id.tv_director_info, R.id.tv_observer})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.tv_partner:
                if (isPartner)
                {
                    tvPartner.setText(baseInfoBean.getProjPartnerNames());
                    isPartner = false;
                } else
                {
                    if (tvPartner.getLineCount() > 1)
                    {
                        int num = reTurnNum(baseInfoBean.getProjPartnerNames());
                        tvPartner.setText(num + "人");
                        isPartner = true;
                    }
                }
                break;
            case R.id.tv_project_preside:
                if (isProjManager)
                {
                    tvProjectPreside.setText(baseInfoBean.getProjManagerNames());
                    isProjManager = false;
                } else
                {
                    if (tvProjectPreside.getLineCount() > 1)
                    {
                        int num = reTurnNum(baseInfoBean.getProjManagerNames());
                        tvProjectPreside.setText(num + "人");
                        isProjManager = true;
                    }
                }
                break;
            case R.id.tv_group_number:
                if (isProjTeam)
                {
                    tvGroupNumber.setText(baseInfoBean.getProjTeamNames());
                    isProjTeam = false;
                } else
                {
                    if (tvGroupNumber.getLineCount() > 1)
                    {
                        int num = reTurnNum(baseInfoBean.getProjTeamNames());
                        tvGroupNumber.setText(num + "人");
                        isProjTeam = true;
                    }
                }
                break;
            case R.id.tv_contributer:
                if (isContributer)
                {
                    tvContributer.setText(baseInfoBean.getContributerNames());
                    isContributer = false;
                } else
                {
                    if (tvContributer.getLineCount() > 1)
                    {
                        int num = reTurnNum(baseInfoBean.getContributerNames());
                        tvContributer.setText(num + "人");
                        isContributer = true;
                    }
                }
                break;
            case R.id.tv_investigate_preside:
                if (isProjDDManager)
                {
                    tvInvestigatePreside.setText(baseInfoBean.getProjDDManagerNames());
                    isProjDDManager = false;
                } else
                {
                    if (tvInvestigatePreside.getLineCount() > 1)
                    {
                        int num = reTurnNum(baseInfoBean.getProjDDManagerNames());
                        tvInvestigatePreside.setText(num + "人");
                        isProjDDManager = true;
                    }
                }
                break;
            case R.id.tv_project_lawyer:
                if (isProjLawyer)
                {
                    tvProjectLawyer.setText(baseInfoBean.getProjLawyerNames());
                    isProjLawyer = false;
                } else
                {
                    if (tvProjectLawyer.getLineCount() > 1)
                    {
                        int num = reTurnNum(baseInfoBean.getProjLawyerNames());
                        tvProjectLawyer.setText(num + "人");
                        isProjLawyer = true;
                    }
                }

            case R.id.tv_director_info:
                if (isDirector)
                {
                    tvDirectorInfo.setText(baseInfoBean.getProjDirectorNames());
                    isDirector = false;
                } else
                {
                    if (tvDirectorInfo.getLineCount() > 1)
                    {
                        int num = reTurnNum(baseInfoBean.getProjDirectorNames());
                        tvDirectorInfo.setText(num + "人");
                        isDirector = true;
                    }
                }

            case R.id.tv_observer:
                if (isObserver)
                {
                    tvObserver.setText(baseInfoBean.getProjObserverNames());
                    isObserver = false;
                } else
                {
                    if (tvObserver.getLineCount() > 1)
                    {
                        int num = reTurnNum(baseInfoBean.getProjObserverNames());
                        tvObserver.setText(num + "人");
                        isObserver = true;
                    }
                }
                break;
            case R.id.view_loading:
                //刷新
                reFresh();
                break;
        }
    }
}
