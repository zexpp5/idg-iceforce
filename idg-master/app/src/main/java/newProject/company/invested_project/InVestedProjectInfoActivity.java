package newProject.company.invested_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.chaoxiang.base.config.Constants;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.injoy.idg.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import newProject.bean.TabLayoutBtnMessage;
import newProject.company.invested_project.editinfo.ActivityAddFollowUp;
import newProject.company.invested_project.editinfo.ActivityDirectorInfo;
import newProject.company.invested_project.editinfo.ActivityProgram;
import newProject.company.project_manager.investment_project.bean.PotentialProjectsDetailBean;
import newProject.utils.TabLayoutBtnUtils;
import yunjing.view.CustomNavigatorBar;


/**
 * Created by selson on 2019/4/26.
 * 已投资项目-
 */
public class InVestedProjectInfoActivity extends BaseActivity
{
    @Bind(R.id.bottom_line)
    View bottomLine;
    @Bind(R.id.vp_view)
    ViewPager vpView;
    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;

    //    private String[] mTitleList = {"基本资料", "项目信息", "投资方案", "基金投资", "会议讨论", "相关附件", "投后跟踪"};//页卡标题集合
    private List<Fragment> mViewList = new ArrayList<>();//页卡视图集合
    private List<String> tmpFragmentList = new ArrayList<>(); //页卡标题集合

    private List<String> tmpTitleList = new ArrayList<>(); //页卡标题集合

    private String mEid = "0";
    private String currentFgCode = "baseInfo";  //默认

    private String[] mTitleList = null;
    private BasePagerAdapter mAdapter = null;

    public static int mRequestCode = 1000;
    private int mRequestCode2 = 1002;
    public static int mRequestCode6 = 1006;
    public static int mRequestCode7 = 1007;

    List<PotentialProjectsDetailBean.DataBeanX.DataBean.SegmentListBean> segmentLists = new ArrayList<>();

    static final String baseInfo = "baseInfo";
    static final String detailInfo = "detailInfo";
    static final String investPlan = "investPlan";
    static final String fundInvest = "fundInvest";
    static final String meeting = "meeting";
    static final String fileInfo = "fileInfo";
    static final String projTrack = "projTrack";
    public static final String director = "boardMeeting";

    private String projectName = "";

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_invested_project_info_main;
    }

    @Override
    protected void init()
    {
        ButterKnife.bind(this);
        if (getIntent().getExtras() != null)
        {
            Bundle bundle = getIntent().getExtras();

            segmentLists = (List<PotentialProjectsDetailBean.DataBeanX.DataBean.SegmentListBean>) bundle.getSerializable
                    (Constants.FG_ALL);
            if (segmentLists == null || segmentLists.size() < 1)
            {

            }

            tmpFragmentList = (List<String>) bundle.getSerializable(Constants.FG_LIST);
            if (tmpFragmentList == null || tmpFragmentList.size() < 1)
            {
                this.finish();
            }
            tmpTitleList = (List<String>) bundle.getSerializable(Constants.FG_LIST_TITLE);
            if (tmpTitleList == null || tmpTitleList.size() < 1)
            {
                this.finish();
            }


            mEid = bundle.getString(Constants.PROJECT_EID, "-1");
            currentFgCode = bundle.getString(Constants.FG_DODE, "baseInfo");//显示的FG
            projectName = bundle.getString(Constants.PROJECT_NAME, "");
        }

        if (tmpFragmentList.size() > 4)
        {
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        } else
        {
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        }

        initView();

        setTitle();

        setFragmentList();

        initListener();

        if (StringUtils.notEmpty(currentFgCode))
        {
            setTabNum(currentFgCode);
        }

        setFragmentButton(currentFgCode);
    }

    private void setFragmentList()
    {
        mViewList.clear();
        Iterator<String> iterator = tmpFragmentList.iterator();
        while (iterator.hasNext())
        {
            String next = iterator.next();
            switch (next)
            {
                case baseInfo:
                    mViewList.add(FragmentBaseInfo.newInstance(mEid));
                    break;
                case detailInfo:
                    mViewList.add(FragmentInvestmentProjectSituation.newInstance(mEid));
                    break;
                case investPlan:
                    mViewList.add(FragmentInvestmentProgram.newInstance(mEid));
                    break;
                case fundInvest:
                    mViewList.add(FragmentInvestmentFund.newInstance(mEid));
                    break;
                case meeting:
                    mViewList.add(FragmentInvestmentMeeting.newInstance(mEid, ""));
                    break;
                case fileInfo:
                    mViewList.add(FragmentInvestmentFile.newInstance(mEid));
                    break;
                case projTrack:
                    mViewList.add(FragmentInvestmentFollowUp.newInstance(mEid));
                    break;
                case director:
                    mViewList.add(FragmentDirectorInfo.newInstance(mEid));
                    break;
            }
        }
        for (int i = 0; i < mTitleList.length; i++)
        {
            tabLayout.addTab(tabLayout.newTab().setText(mTitleList[i]).setContentDescription(tmpFragmentList.get(i)));//添加tab选项卡
        }
        SDLogUtil.debug("tmpFragmentList");
    }

    public void initView()
    {
        mNavigatorBar.setMidText("项目档案");
        mNavigatorBar.setLeftImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    private void setTitle()
    {
        mTitleList = new String[tmpTitleList.size()];
        for (int i = 0; i < mTitleList.length; i++)
        {
            mTitleList[i] = tmpTitleList.get(i);
        }
    }

    public void initListener()
    {
        mAdapter = new BasePagerAdapter(getSupportFragmentManager(), mViewList,
                mTitleList);
        vpView.setAdapter(mAdapter);//给ViewPager设置适配器
        tabLayout.setupWithViewPager(vpView);//将TabLayout和ViewPager关联起来。
        tabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器

        vpView.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                SDLogUtil.debug("");
            }

            @Override
            public void onPageSelected(int position)
            {
                if (StringUtils.notEmpty(position))
                {
                    if (mAdapter.getItem(position) instanceof FragmentBaseInfo)
                    {
                        setFragmentButton(baseInfo);
                    } else if (mAdapter.getItem(position) instanceof FragmentInvestmentProjectSituation)
                    {
                        setFragmentButton(detailInfo);
                    } else if (mAdapter.getItem(position) instanceof FragmentInvestmentProgram)
                    {
                        setFragmentButton(investPlan);
                    } else if (mAdapter.getItem(position) instanceof FragmentInvestmentFund)
                    {
                        setFragmentButton(fundInvest);
                    } else if (mAdapter.getItem(position) instanceof FragmentInvestmentMeeting)
                    {
                        setFragmentButton(meeting);
                    } else if (mAdapter.getItem(position) instanceof FragmentInvestmentFile)
                    {
                        setFragmentButton(fileInfo);
                    } else if (mAdapter.getItem(position) instanceof FragmentInvestmentFollowUp)
                    {
                        setFragmentButton(projTrack);
                    } else if (mAdapter.getItem(position) instanceof FragmentDirectorInfo)
                    {
                        setFragmentButton(director);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });
    }

    private boolean isSituationEdite = false;

    private void setFragmentButton(String fgCode)
    {
        mNavigatorBar.setRightTextVisible(false);
        switch (fgCode)
        {
            case baseInfo:
                mNavigatorBar.setRightImageResouce(R.mipmap.icon_public_edit);
                mNavigatorBar.setRightImageVisible(reTurnPermision(fgCode));
//                mNavigatorBar.setRightImageVisible(true);
                mNavigatorBar.setRightImageOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        FragmentBaseInfo fragmentInvestmentProgram = (FragmentBaseInfo) mAdapter.getItem(vpView
                                .getCurrentItem());
                        fragmentInvestmentProgram.StartActivityInfoAc();
                    }
                });

                break;
            case detailInfo:
                setSituation(isSituationEdite);
                break;
            case investPlan:
                mNavigatorBar.setRightImageResouce(R.mipmap.icon_public_add);
                mNavigatorBar.setRightImageVisible(reTurnPermision(fgCode));
                mNavigatorBar.setRightImageOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent2 = new Intent(InVestedProjectInfoActivity.this, ActivityProgram.class);
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("mProjId", mEid);
                        intent2.putExtras(bundle2);
                        startActivityForResult(intent2, mRequestCode2);
                    }
                });
                break;
            case fundInvest:
//                mNavigatorBar.setRightImageResouce(R.mipmap.icon_public_edit);
//                mNavigatorBar.setRightImageVisible(true);
//                mNavigatorBar.setRightImageOnClickListener(new View.OnClickListener()
//                {
//                    @Override
//                    public void onClick(View v)
//                    {
//
//                    }
//                });
                break;
            case meeting:
                mNavigatorBar.setRightImageResouce(R.mipmap.icon_public_edit);
                mNavigatorBar.setRightImageVisible(false);
                mNavigatorBar.setRightImageOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                    }
                });
                //董事会信息
            case director:
                mNavigatorBar.setRightImageResouce(R.mipmap.icon_public_add);
                mNavigatorBar.setRightImageVisible(true);
                mNavigatorBar.setRightImageOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(InVestedProjectInfoActivity.this, ActivityDirectorInfo.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.PROJECT_EID, mEid);
                        bundle.putString(Constants.PROJECT_NAME, projectName);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, mRequestCode7);

                    }
                });

                break;
            case fileInfo:
                mNavigatorBar.setRightImageResouce(R.mipmap.icon_public_edit);
                mNavigatorBar.setRightImageVisible(false);
                mNavigatorBar.setRightImageOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                    }
                });
                break;
            case projTrack:
                mNavigatorBar.setRightImageResouce(R.mipmap.icon_public_add);
                mNavigatorBar.setRightImageVisible(reTurnPermision(fgCode));
                mNavigatorBar.setRightImageOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent6 = new Intent(InVestedProjectInfoActivity.this, ActivityAddFollowUp.class);
                        Bundle bundle6 = new Bundle();
                        bundle6.putString("projId", mEid);
                        bundle6.putString("mTitle", "新增投后跟踪");
                        intent6.putExtras(bundle6);
                        startActivityForResult(intent6, mRequestCode6);
                    }
                });
                break;
        }
    }

    private boolean reTurnPermision(String fgCode)
    {
        boolean a = false;
        Iterator<PotentialProjectsDetailBean.DataBeanX.DataBean.SegmentListBean> iterator = segmentLists.iterator();
        while (iterator.hasNext())
        {
            PotentialProjectsDetailBean.DataBeanX.DataBean.SegmentListBean segmentListBean = iterator.next();
            if (StringUtils.notEmpty(segmentListBean.getCode()))
                if (fgCode.equalsIgnoreCase(segmentListBean.getCode()))
                    if (StringUtils.notEmpty(segmentListBean.getPermission()))
                    {
                        if (segmentListBean.getPermission() == 2)
                        {
                            a = true;
                        } else
                        {
                            a = false;
                        }
                    }
        }
        return a;
    }

    private void setSituation(boolean isEdite)
    {
        if (reTurnPermision(detailInfo))
        {
            if (isEdite)
            {
                mNavigatorBar.setRightImageVisible(true);
                mNavigatorBar.setRightTextVisible(true);
                mNavigatorBar.setRightText("完 成");
                mNavigatorBar.setRightTextOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        TabLayoutBtnMessage tabLayoutBtnMessage = TabLayoutBtnUtils.getInstance().getTabBean(detailInfo,
                                false, "编辑");
                        if (tabLayoutBtnMessage != null)
                            EventBus.getDefault().post(tabLayoutBtnMessage);

                        isSituationEdite = false;
                        setSituation(false);
                    }
                });
            } else
            {
                mNavigatorBar.setRightImageResouce(R.mipmap.icon_public_edit);
                mNavigatorBar.setRightImageVisible(true);
                mNavigatorBar.setRightTextVisible(false);
                mNavigatorBar.setRightImageOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        TabLayoutBtnMessage tabLayoutBtnMessage = TabLayoutBtnUtils.getInstance().getTabBean(detailInfo,
                                true, "编辑");
                        if (tabLayoutBtnMessage != null)
                            EventBus.getDefault().post(tabLayoutBtnMessage);

                        isSituationEdite = true;
                        setSituation(true);
                    }
                });
            }
        } else
        {
            mNavigatorBar.setRightImageVisible(false);
            mNavigatorBar.setRightTextVisible(false);
        }
    }

    private void setTabNum(String fgCode)
    {
        for (int j = 0; j < tmpFragmentList.size(); j++)
        {
            if (tmpFragmentList.get(j).equals(fgCode))
            {
                tabLayout.getTabAt(j).select();
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mRequestCode2 && resultCode == RESULT_OK)
        {
            //投资方案回调
            if (mAdapter.getCurrentFg() instanceof FragmentInvestmentProgram)
            {
                if (mAdapter.getItem(vpView.getCurrentItem()) instanceof FragmentInvestmentProgram)
                {
                    FragmentInvestmentProgram fragmentInvestmentProgram = (FragmentInvestmentProgram) mAdapter.getItem(vpView
                            .getCurrentItem());
                    fragmentInvestmentProgram.reFresh();
                }

            }
        } else if (requestCode == mRequestCode && resultCode == RESULT_OK)
        {
            if (mAdapter.getCurrentFg() instanceof FragmentBaseInfo)
            {
                if (mAdapter.getItem(vpView.getCurrentItem()) instanceof FragmentBaseInfo)
                {
                    FragmentBaseInfo fragmentBaseInfo = (FragmentBaseInfo) mAdapter.getItem(vpView
                            .getCurrentItem());
                    fragmentBaseInfo.reFresh();
                }
            }
        } else if (requestCode == mRequestCode6 && resultCode == RESULT_OK)
        {
            if (mAdapter.getCurrentFg() instanceof FragmentInvestmentFollowUp)
            {
                if (mAdapter.getItem(vpView.getCurrentItem()) instanceof FragmentInvestmentFollowUp)
                {
                    FragmentInvestmentFollowUp fragmentInvestmentFollowUp = (FragmentInvestmentFollowUp) mAdapter.getItem(vpView
                            .getCurrentItem());
                    fragmentInvestmentFollowUp.reFresh();
                }
            }
        } else if (requestCode == mRequestCode7 && resultCode == RESULT_OK)
        {
            if (mAdapter.getCurrentFg() instanceof FragmentDirectorInfo)
            {
                if (mAdapter.getItem(vpView.getCurrentItem()) instanceof FragmentDirectorInfo)
                {
                    FragmentDirectorInfo fragmentDirectorInfo = (FragmentDirectorInfo) mAdapter.getItem(vpView
                            .getCurrentItem());
                    fragmentDirectorInfo.reFresh();
                }
            }
        }

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ButterKnife.unbind(this);
        mViewList.clear();
        tmpTitleList.clear();
    }


//    @Subscribe(threadMode = ThreadMode.MainThread) //默认方式, 在发送线程执行
//    public void onTabLayoutBtnEvent(TabLayoutBtnMessage tabLayoutBtnMessage)
//    {
//        if (null != tabLayoutBtnMessage)
//        {
//            if (tabLayoutBtnMessage.fgCode.equals(detailInfo))
//            {
//                if (!tabLayoutBtnMessage.getInvestmentBean().isKey())
//                {
//
//                }
//            }
//        }
//    }
}
