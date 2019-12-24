package newProject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.config.Constants;
import com.chaoxiang.base.utils.BitmapUtils;
import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.ScreenUtils;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cx.base.BaseActivity;
import com.http.HttpURLUtil;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.utils.SDToast;

import java.util.ArrayList;
import java.util.List;

import newProject.api.ListHttpHelper;
import newProject.company.Schedule.ScheDuleMeetFragment;
import newProject.company.Schedule.ScheduleListBean;
import newProject.company.announce.AnnounceListBean;
import newProject.company.announce.CompanyAnnounceFragment;
import newProject.company.borrowapplay.BorrowApplyDetailFragment;
import newProject.company.borrowapplay.BorrowApplySubmitFragment;
import newProject.company.borrowapplay.bean.LoanListBean;
import newProject.company.capital_express.CapitalExpressActivity;
import newProject.company.capital_express.ExpressListBean;
import newProject.company.daily.MyDailyDetailFragment;
import newProject.company.daily.MyDailyFragment;
import newProject.company.daily.MyDailyListBean;
import newProject.company.goout.GoOutDetailFragment;
import newProject.company.goout.GoOutFragment;
import newProject.company.goout.bean.GoOutListBean;
import newProject.company.leaveapplay.bean.NewLeaveListBean;
import newProject.company.vacation.VacationDetailFragment;
import newProject.company.vacation.VacationFragment;
import newProject.company.vacation.WebVacationActivity;
import newProject.ui.annual_meeting.AnnualMeetingMainActivity;
import newProject.utils.ImgAsyncTaskUtils;
import newProject.utils.NewCommonDialog;
import newProject.view.CornerTransform;
import newProject.view.RoundedImageView;
import yunjing.utils.DisplayUtil;
import yunjing.utils.FragmentCallBackInterface;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.StatusTipsView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.injoy.idg.R.id.apply_recycler_layout;
import static newProject.company.capital_express.CapitalExpressActivity.COMPANY_HAVEIMG;
import static newProject.company.capital_express.CapitalExpressActivity.COMPANY_NOIMG;

/**
 * Created by Administrator on 2017/10/20.
 * 借支申请，请假申请，外出工作，日志列表,公司公告列表
 * position 分为为 0,1,2,3,4
 */

public class ApplyListActivity extends BaseActivity implements FragmentCallBackInterface
{
    private Class mClass[] = {LoanListBean.class, NewLeaveListBean.class, GoOutListBean.class,
            MyDailyListBean.class, AnnounceListBean.class, ScheduleListBean.class};
    private List<LoanListBean.LoanDataBean> mLoanLists = new ArrayList<>();
    private List<NewLeaveListBean.NewLeaveDataBean> mApplyLists = new ArrayList<>();
    private List<GoOutListBean.GoOutDataBean> mOutLists = new ArrayList<>();
    private List<MyDailyListBean.MyDailyDataBean> mMyDailyLists = new ArrayList<>();
    private List<AnnounceListBean.DataBean> mAnnounceLists = new ArrayList<>();
    private List<ScheduleListBean.DataBean> mScheduleLists = new ArrayList<>();
    private TextView mApplyAddText, mApplySearchText;
    private LinearLayout mFirstLL, mSecondLL;
    private LoanAdapter mLoanAdapter;
    private ApplyAdapter mApplyAdapter;
    private OutAdapter mOutAdapter;
    private MyDailyAdapter mMyDailyAdapter;
    private AnnounAdapter mAnnounAdapter;
    private ScheduleAdapter mScheduleAdapter;
    private RecyclerView mRecyclerView;
    private FragmentManager mFragmentManager;
    private CustomNavigatorBar mNavigatorBar;
    private Fragment mSelectFragment;
    private StatusTipsView mTips;
    private int mChoose = 0;
    private int mPage = 1;
    private int mBackListSize = 0;
    private RefreshLayout mRefreshLayout;
    private String mSearch = "";
    private RelativeLayout mTopBarLayout;
    private View mSpaceView;
    private ImageView mAddIcon, mSearchIcon;
    private String isSearch = "";
    private boolean isHide = false;
    private boolean mIsSuper = false;


    @Override
    protected void init()
    {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null)
        {
            mChoose = bundle.getInt("CHOOSE", 0);
            mSearch = bundle.getString(Constants.SEARCH);
            isSearch = bundle.getString(Constants.IS_SEARCH);
            isHide = bundle.getBoolean("HIDE");
        }
       /* //控制搜索进来隐藏
        mTopBarLayout= (RelativeLayout) findViewById(R.id.top_btn_layout);
        mSpaceView=findViewById(R.id.space_view);*/

        //按钮文字
        mApplyAddText = (TextView) findViewById(R.id.apply_add_text);
        mApplySearchText = (TextView) findViewById(R.id.apply_search_text);
        //图标
        mAddIcon = (ImageView) findViewById(R.id.top_add_icon);
        mSearchIcon = (ImageView) findViewById(R.id.top_add_icon_two);
        //布局
        mFirstLL = (LinearLayout) findViewById(R.id.first_layout);
        mFirstLL.setOnClickListener(mOnClickListener);
        mSecondLL = (LinearLayout) findViewById(R.id.second_layout);
        mSecondLL.setOnClickListener(mOnClickListener);
        //标题
        mNavigatorBar = (CustomNavigatorBar) findViewById(R.id.title_bar);
        mNavigatorBar.setLeftImageOnClickListener(mOnClickListener);
        mNavigatorBar.setLeftTextOnClickListener(mOnClickListener);
        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(true);
        mNavigatorBar.setRightSecondImageVisible(true);
        mNavigatorBar.setRightImageOnClickListener(mOnClickListener);
        mNavigatorBar.setRightSecondImageOnClickListener(mOnClickListener);
        if (mChoose == 0)
        {
            mNavigatorBar.setMidText("借支申请");
            mApplyAddText.setText("借支申请");
            mApplySearchText.setText("查找借支");
        } else if (mChoose == 1)
        {
            mNavigatorBar.setMidText("休假申请");
            mNavigatorBar.setRightImageVisible(false);
            mNavigatorBar.setRightSecondImageVisible(false);
            mApplyAddText.setText("休假申请");
            mApplySearchText.setText("查找请假");
        } else if (mChoose == 2)
        {
            mNavigatorBar.setMidText("工作外出");
            mApplyAddText.setText("外出申请");
            mApplySearchText.setText("查找外出");
        } else if (mChoose == 3)
        {
            mNavigatorBar.setMidText("我的日志");
            mApplyAddText.setText("写日志");
            mApplySearchText.setText("查日志");
            //需要换样式
            mFirstLL.setBackgroundResource(R.drawable.top_button_radius);
            mApplyAddText.setTextColor(Color.WHITE);
            mAddIcon.setBackgroundResource(R.drawable.top_button_radius_left_white);
            mAddIcon.setImageDrawable(getResources().getDrawable(R.mipmap.public_add_blue));
            mSecondLL.setBackgroundResource(R.drawable.top_button_radius);
            mApplySearchText.setTextColor(Color.WHITE);
            mSearchIcon.setBackgroundResource(R.drawable.top_button_radius_left_white);
            mSearchIcon.setImageDrawable(getResources().getDrawable(R.mipmap.public_search_blue));
        } else if (mChoose == 4)
        {
            mNavigatorBar.setMidText("通知公告");
            mApplyAddText.setText("通知公告");
            mApplySearchText.setText("查找通知");
            mNavigatorBar.setRightImageVisible(false);
            /*int isSuper = (int) SPUtils.get(this, SPUtils.IS_SUPER, 0);
            int SuperStatus = (int) SPUtils.get(this, SPUtils.IS_SUPER_STATUS, 0);
            if (isSuper == 1 && SuperStatus == 1)
            {
                mNavigatorBar.setRightImageVisible(true);
                mIsSuper=true;
            } else
            {
                mNavigatorBar.setRightImageVisible(false);
                mIsSuper=false;
            }*/
        } else if (mChoose == 5)
        {
            mNavigatorBar.setMidText("日常会议");
            mNavigatorBar.setRightImageVisible(false);
        }

        //初始化搜索有内容
        if (!TextUtils.isEmpty(mSearch) || isHide)
        {
            mNavigatorBar.setRightImageVisible(false);
            mNavigatorBar.setRightSecondImageVisible(false);
        }

        //下拉刷新
        mRefreshLayout = (RefreshLayout) findViewById(R.id.smart_refresh_layout);
        //mRefreshLayout.setReboundDuration(2000);
        mRefreshLayout.setReboundInterpolator(new DecelerateInterpolator());
        mRefreshLayout.setDragRate(0.25f);
        if (mChoose == 4)
        {
            mRefreshLayout.setEnableLoadmore(false);
        }
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(ApplyListActivity.this))
                {
                    if (mChoose == 4)
                    {
                        if (getSize(mChoose) >= mBackListSize)
                        {
                            mRefreshLayout.finishRefresh(1000);
                            SDToast.showShort("没有更多数据了");
                        } else
                        {
                            mPage = mPage + 1;
                            getPageData(mChoose, mPage + "", mClass[mChoose], false, mSearch, isSearch);
                            mRefreshLayout.finishRefresh(1000);
                        }
                    } else
                    {
                        mSearch = "";
                        mPage = 1;
                        clearPage(mChoose);
                        getPageData(mChoose, mPage + "", mClass[mChoose], true, mSearch, isSearch);
                        mRefreshLayout.finishRefresh(1000);
                        mRefreshLayout.setLoadmoreFinished(false);
                        mRefreshLayout.setEnableLoadmore(true);
                    }
                } else
                {
                    SDToast.showShort("请检查网络连接");
                    mRefreshLayout.finishRefresh(1000);
                }
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener()
        {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(ApplyListActivity.this))
                {
                    if (getSize(mChoose) >= mBackListSize)
                    {
                        mRefreshLayout.finishLoadmore(1000);
                        SDToast.showShort("没有更多数据");
                    } else
                    {
                        mPage = mPage + 1;
                        getPageData(mChoose, mPage + "", mClass[mChoose], false, mSearch, isSearch);
                        mRefreshLayout.finishLoadmore(1000);
                    }
                } else
                {
                    SDToast.showShort("请检查网络连接");
                    mRefreshLayout.finishLoadmore(1000);
                }

            }
        });


        mRecyclerView = (RecyclerView) findViewById(apply_recycler_layout);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (mChoose == 4)
        {
            mRecyclerView.setBackgroundColor(Color.parseColor("#EAEAEB"));
        }

        //出错
        mTips = (StatusTipsView) findViewById(R.id.loading_view);
        mTips.showLoading();
        mTips.setOnVisibleChangeListener(new StatusTipsView.OnVisibleChangeListener()
        {

            @Override
            public void onVisibleChanged(boolean visible)
            {
                if (mRecyclerView != null)
                {
                    mRecyclerView.setVisibility(visible ? GONE : VISIBLE);
                }
            }
        });
        mTips.setOnRetryListener(new StatusTipsView.OnRetryListener()
        {

            @Override
            public void onRetry()
            {
                mTips.showLoading();
                switchOne(mChoose);
            }
        });
        //初始化列表
        switchOne(mChoose);
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.apply_list_activity_layout_two;
    }


    /**
     * 初始化选择是哪个模块的列表
     *
     * @param which 0, 借支申请
     *              1，请假申请
     *              2，外出工作
     *              3，日志列表
     */
    public void switchOne(int which)
    {
        switch (which)
        {
            case 0:
                mLoanAdapter = new LoanAdapter(R.layout.apply_item_layout, mLoanLists);
                mRecyclerView.setAdapter(mLoanAdapter);
                mLoanAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position)
                    {
                        LoanListBean.LoanDataBean announceBean = (LoanListBean.LoanDataBean) adapter.getData().get
                                (position);
                        if (adapter.getData().size() > 0 && announceBean != null)
                        {
                            mRecyclerView.setVisibility(GONE);
                            Bundle bundle = new Bundle();
                            bundle.putLong("EID", announceBean.getEid());
                            bundle.putBoolean("IsDetail", true);
                            BorrowApplyDetailFragment fragment = new BorrowApplyDetailFragment();
                            fragment.setArguments(bundle);
                            replaceFragment(fragment);
                        }
                    }
                });
                getPageData(which, mPage + "", mClass[which], false, mSearch, isSearch);
                break;
            case 1:
                mApplyAdapter = new ApplyAdapter(R.layout.vacation_item_layout, mApplyLists);
                mRecyclerView.setAdapter(mApplyAdapter);
                mApplyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position)
                    {
                        NewLeaveListBean.NewLeaveDataBean departDataBean = (NewLeaveListBean.NewLeaveDataBean) adapter.getData
                                ().get(position);
                        if (adapter.getData().size() > 0 && departDataBean != null)
                        {
                            mRecyclerView.setVisibility(GONE);
                            Bundle bundle = new Bundle();
                            bundle.putInt("EID", departDataBean.getLeaveId());
                            VacationDetailFragment fragment = new VacationDetailFragment();
                            fragment.setArguments(bundle);
                            replaceFragment(fragment);
                        }
                    }
                });
                getPageData(which, mPage + "", mClass[which], false, mSearch, isSearch);
                mRefreshLayout.setEnableLoadmore(false);
                mRefreshLayout.setEnableRefresh(false);
                mRefreshLayout.setEnableAutoLoadmore(false);
                break;
            case 2:
                mOutAdapter = new OutAdapter(R.layout.apply_item_layout, mOutLists);
                mRecyclerView.setAdapter(mOutAdapter);
                mOutAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position)
                    {
                        GoOutListBean.GoOutDataBean fileCenterBean = (GoOutListBean.GoOutDataBean) adapter.getData
                                ().get(position);
                        if (adapter.getData().size() > 0 && fileCenterBean != null)
                        {
                            mRecyclerView.setVisibility(GONE);
                            Bundle bundle = new Bundle();
                            bundle.putLong("EID", fileCenterBean.getEid());
                            bundle.putBoolean("IsDetail", true);
                            GoOutDetailFragment fragment = new GoOutDetailFragment();
                            fragment.setArguments(bundle);
                            replaceFragment(fragment);
                        }

                    }
                });
                getPageData(which, mPage + "", mClass[which], false, mSearch, isSearch);
                break;
            case 3:
                mMyDailyAdapter = new MyDailyAdapter(R.layout.apply_item_layout, mMyDailyLists);
                mRecyclerView.setAdapter(mMyDailyAdapter);
                mMyDailyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position)
                    {
                        if (adapter.getData().size() > 0)
                        {
                            MyDailyListBean.MyDailyDataBean approvalBean = (MyDailyListBean.MyDailyDataBean) adapter.getData().get
                                    (position);
                            if (approvalBean != null)
                            {
                                mRecyclerView.setVisibility(GONE);
                                Bundle bundle = new Bundle();
                                bundle.putLong("EID", approvalBean.getEid());
                                MyDailyDetailFragment fragment = new MyDailyDetailFragment();
                                fragment.setArguments(bundle);
                                replaceFragment(fragment);
                            }
                        }
                    }
                });
                getPageData(which, mPage + "", mClass[which], false, mSearch, isSearch);
                break;
            case 4: //通知公告
                mAnnounAdapter = new AnnounAdapter(R.layout.announ_item_layout, mAnnounceLists);
                mRecyclerView.setAdapter(mAnnounAdapter);
                mAnnounAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position)
                    {
                        if (adapter.getData().size() > 0)
                        {
                            AnnounceListBean.DataBean approvalBean = (AnnounceListBean.DataBean) adapter.getData().get
                                    (position);
                            if (approvalBean != null)
                            {
//                                Bundle bundle = new Bundle();
//                                bundle.putLong("EID", approvalBean.getEid());
//                                bundle.putBoolean("ISSUPER", mIsSuper);
//                                CompanyAnnounceFragment fragment = new CompanyAnnounceFragment();
//                                fragment.setArguments(bundle);
//                                replaceFragment(fragment);
                                String eidString = approvalBean.getEid() + "";
                                String tokenString = DisplayUtil.getUserInfo(ApplyListActivity.this, 10);
                                String urlString = HttpURLUtil.newInstance().append("comNotice/detail/h.htm?comNoticeId=")
                                        .toString() + eidString + "&token=" + tokenString;

                                Bundle bundle = new Bundle();
                                bundle.putString("TITLE", "通知公告");
                                bundle.putString("URL", urlString);
                                Intent intent = new Intent(ApplyListActivity.this, WebVacationActivity.class);
                                intent.putExtras(bundle);
                                startActivity(intent);

                            }
                        }
                    }
                });
                getPageData(which, mPage + "", mClass[which], false, mSearch, isSearch);
                break;
            case 5:
                mScheduleAdapter = new ScheduleAdapter(R.layout.announ_depart_item_layout, mScheduleLists);
                mRecyclerView.setAdapter(mScheduleAdapter);
                mScheduleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position)
                    {
                        if (adapter.getData().size() > 0)
                        {
                            ScheduleListBean.DataBean approvalBean = (ScheduleListBean.DataBean) adapter.getData().get
                                    (position);
                            if (approvalBean != null)
                            {
                                mRecyclerView.setVisibility(GONE);
                                Bundle bundle = new Bundle();
                                bundle.putLong("EID", approvalBean.getEid());
                                bundle.putBoolean("ADD", false);
                                ScheDuleMeetFragment fragment = new ScheDuleMeetFragment();
                                fragment.setArguments(bundle);
                                replaceFragment(fragment);
                            }
                        }
                    }
                });
                getPageData(which, mPage + "", mClass[which], false, mSearch, isSearch);
                break;
            default:
                mTips.showNoContent("获取出错");
                break;
        }
    }

    public void clearPage(int which)
    {
        if (which == 0)
        {
            mLoanLists.clear();
            mLoanAdapter.getData().clear();
        } else if (which == 1)
        {
            mApplyLists.clear();
            mApplyAdapter.getData().clear();
        } else if (which == 2)
        {
            mOutLists.clear();
            mOutAdapter.getData().clear();
        } else if (which == 3)
        {
            mMyDailyLists.clear();
            mMyDailyAdapter.getData().clear();
        } else if (which == 4)
        {
            mAnnounceLists.clear();
            mAnnounAdapter.getData().clear();
        } else if (which == 5)
        {
            mScheduleLists.clear();
            mScheduleAdapter.getData().clear();
        }
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (v == mNavigatorBar.getLeftImageView() || v == mNavigatorBar.getLeftText())
            {
                finish();
            }
            if (v == mNavigatorBar.getRightImage())
            {
                if (mChoose == 0)
                {
                    BorrowApplySubmitFragment fragment = new BorrowApplySubmitFragment();
                    replaceFragment(fragment);
                } else if (mChoose == 1)
                {
                    VacationFragment fragment = new VacationFragment();
                    replaceFragment(fragment);
                } else if (mChoose == 2)
                {
                    GoOutFragment fragment = new GoOutFragment();
                    replaceFragment(fragment);
                } else if (mChoose == 3)
                {
                    MyDailyFragment fragment = new MyDailyFragment();
                    replaceFragment(fragment);
                } else if (mChoose == 4)
                {
                    /*CompanyNotifyFragment fragment=new CompanyNotifyFragment();
                    replaceFragment(fragment);*/
                } else if (mChoose == 5)
                {
                    ScheDuleMeetFragment fragment = new ScheDuleMeetFragment();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("ADD", true);
                    fragment.setArguments(bundle);
                    replaceFragment(fragment);
                }
            }
            if (v == mNavigatorBar.getRihtSecondImage())
            {
                final NewCommonDialog dialog = new NewCommonDialog(ApplyListActivity.this);
                dialog.setPositiveListener(new NewCommonDialog.DialogPositiveListener()
                {
                    @Override
                    public void onClick(String inputText, String select)
                    {

                    }

                    @Override
                    public void onSearchClick(String content)
                    {
                        mSearch = content;
                        refreshList();
                    }
                });
                dialog.initDialogSearch(ApplyListActivity.this).show();
            }

        }
    };


    //替换fragment，使用replace节省内存
    public void replaceFragment(Fragment fragment)
    {
        if (fragment == null)
        {
            return;
        }
        if (mFragmentManager == null)
        {
            mFragmentManager = getSupportFragmentManager();
        }
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.replace(R.id.apply_fragment_container, fragment);
        ft.commit();
    }

    public void setSelectedFragment(Fragment fragment)
    {
        mSelectFragment = fragment;
    }

    @Override
    public void refreshList()
    {
        if (mRecyclerView != null)
        {
            mRecyclerView.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    mPage = 1;
                    clearPage(mChoose);
                    getPageData(mChoose, mPage + "", mClass[mChoose], true, mSearch, isSearch);
                }
            }, 500);
        }
    }

    @Override
    public void callBackObject(Object object)
    {
    }

    @Override
    public void onBackPressed()
    {
        //直接返回
        if (mSelectFragment == null)
        {
            super.onBackPressed();
        } else
        {
            //进入fragment 返回
            if (mRecyclerView != null)
            {
                mRecyclerView.setVisibility(VISIBLE);
            }
            mFragmentManager.beginTransaction().remove(mSelectFragment).commit();
            mSelectFragment.onDestroyView();
            mSelectFragment = null;
            DisplayUtil.hideInputSoft(this);

        }
    }


    public int getSize(int which)
    {
        if (which == 0)
        {
            return mLoanAdapter.getData().size();
        } else if (which == 1)
        {
            return mApplyAdapter.getData().size();
        } else if (which == 2)
        {
            return mOutAdapter.getData().size();
        } else if (which == 3)
        {
            return mMyDailyAdapter.getData().size();
        } else if (which == 4)
        {
            return mAnnounAdapter.getData().size();
        } else if (which == 5)
        {
            return mScheduleAdapter.getData().size();
        } else
        {
            return 0;
        }
    }


    private class LoanAdapter extends BaseQuickAdapter<LoanListBean.LoanDataBean, BaseViewHolder>
    {


        public LoanAdapter(@LayoutRes int layoutResId, @Nullable List<LoanListBean.LoanDataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, LoanListBean.LoanDataBean item)
        {
            if (item.getTitle() != null)
            {
                holder.getView(R.id.apply_item_title).setVisibility(VISIBLE);
                holder.setText(R.id.apply_item_title, item.getTitle());
            } else
            {
                holder.getView(R.id.apply_item_title).setVisibility(View.INVISIBLE);
            }

            if (item.getYgName() != null)
            {
                holder.getView(R.id.apply_item_name).setVisibility(VISIBLE);
                holder.setText(R.id.apply_item_name, item.getYgName());
            } else
            {
                holder.getView(R.id.apply_item_name).setVisibility(View.INVISIBLE);
            }
            if (item.getYgDeptName() != null)
            {
                holder.getView(R.id.apply_item_department).setVisibility(VISIBLE);
                holder.setText(R.id.apply_item_department, item.getYgDeptName());
            } else
            {
                holder.getView(R.id.apply_item_department).setVisibility(View.INVISIBLE);
            }
            if (item.getCreateTime() != null)
            {
                holder.getView(R.id.apply_item_time).setVisibility(VISIBLE);
                holder.setText(R.id.apply_item_time, item.getCreateTime());
            } else
            {
                holder.getView(R.id.apply_item_time).setVisibility(View.INVISIBLE);
            }

            if (item.getStatusName() != null)
            {
                holder.getView(R.id.apply_item_state).setVisibility(VISIBLE);
               /* if ((item.getEid()+"").equals(DisplayUtil.getUserInfo(ApplyListActivity.this,3))){
                    holder.setText(R.id.apply_item_state, "待我审批");
                }else {*/
                holder.setText(R.id.apply_item_state, item.getStatusName());
                //}
            } else
            {
                holder.getView(R.id.apply_item_state).setVisibility(View.INVISIBLE);
            }

        }
    }

    private class ApplyAdapter extends BaseQuickAdapter<NewLeaveListBean.NewLeaveDataBean, BaseViewHolder>
    {


        public ApplyAdapter(@LayoutRes int layoutResId, @Nullable List<NewLeaveListBean.NewLeaveDataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, NewLeaveListBean.NewLeaveDataBean item)
        {
            if (item.getUserName() != null)
            {
                holder.getView(R.id.one_title).setVisibility(VISIBLE);
                holder.setText(R.id.one_title, item.getUserName() + "的申请");
            } else
            {
                holder.getView(R.id.one_title).setVisibility(View.INVISIBLE);
            }
            if (item.getSigned() == 1)
            {
                holder.setTextColor(R.id.one_content, 0xffff5f13);
                holder.setText(R.id.one_content, "批审中");
            } else if (item.getSigned() == 2)
            {
                holder.setTextColor(R.id.one_content, 0xff2a8301);
                holder.setText(R.id.one_content, "批审通过");
            } else if (item.getSigned() == 3)
            {
                holder.setTextColor(R.id.one_content, 0xffeb4849);
                holder.setText(R.id.one_content, "批审驳回");
            }
            if (item.getLeaveType() != null)
            {
                holder.getView(R.id.two_content).setVisibility(VISIBLE);
                holder.setText(R.id.two_content, item.getLeaveType());
            } else
            {
                holder.getView(R.id.two_content).setVisibility(View.INVISIBLE);
            }
            if (item.getLeaveStart() != null)
            {
                holder.getView(R.id.three_content).setVisibility(VISIBLE);
                holder.setText(R.id.three_content, item.getLeaveStart());
            } else
            {
                holder.getView(R.id.three_content).setVisibility(View.INVISIBLE);
            }

            if (item.getLeaveEnd() != null)
            {
                holder.getView(R.id.four_content).setVisibility(VISIBLE);
                holder.setText(R.id.four_content, item.getLeaveEnd());

            } else
            {
                holder.getView(R.id.four_content).setVisibility(View.INVISIBLE);
            }
            if (item.getLeaveDay() >= 0)
            {
                holder.getView(R.id.five_content).setVisibility(VISIBLE);
                holder.setText(R.id.five_content, item.getLeaveDay() + "天");

            } else
            {
                holder.getView(R.id.five_content).setVisibility(View.INVISIBLE);
            }
        }
    }

    private class OutAdapter extends BaseQuickAdapter<GoOutListBean.GoOutDataBean, BaseViewHolder>
    {


        public OutAdapter(@LayoutRes int layoutResId, @Nullable List<GoOutListBean.GoOutDataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, GoOutListBean.GoOutDataBean item)
        {
            if (item.getReason() != null)
            {
                holder.getView(R.id.apply_item_title).setVisibility(VISIBLE);
                holder.setText(R.id.apply_item_title, item.getReason());
            } else
            {
                holder.getView(R.id.apply_item_title).setVisibility(View.INVISIBLE);
            }

            if (item.getYgName() != null)
            {
                holder.getView(R.id.apply_item_name).setVisibility(VISIBLE);
                holder.setText(R.id.apply_item_name, item.getYgName());
            } else
            {
                holder.getView(R.id.apply_item_name).setVisibility(View.INVISIBLE);
            }
            if (item.getYgDeptName() != null)
            {
                holder.getView(R.id.apply_item_department).setVisibility(VISIBLE);
                holder.setText(R.id.apply_item_department, item.getYgDeptName());
            } else
            {
                holder.getView(R.id.apply_item_department).setVisibility(View.INVISIBLE);
            }
            if (item.getCreateTime() != null)
            {
                holder.getView(R.id.apply_item_time).setVisibility(VISIBLE);
                holder.setText(R.id.apply_item_time, item.getCreateTime());
            } else
            {
                holder.getView(R.id.apply_item_time).setVisibility(View.INVISIBLE);
            }

            if (item.getStatusName() != null)
            {
                holder.getView(R.id.apply_item_state).setVisibility(VISIBLE);
                /*if ((item.getEid()+"").equals(DisplayUtil.getUserInfo(ApplyListActivity.this,3))){
                    holder.setText(R.id.apply_item_state, "待我审批");
                }else {*/
                holder.setText(R.id.apply_item_state, item.getStatusName());
                //}
            } else
            {
                holder.getView(R.id.apply_item_state).setVisibility(View.INVISIBLE);
            }
        }
    }

    private class MyDailyAdapter extends BaseQuickAdapter<MyDailyListBean.MyDailyDataBean, BaseViewHolder>
    {


        public MyDailyAdapter(@LayoutRes int layoutResId, @Nullable List<MyDailyListBean.MyDailyDataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, MyDailyListBean.MyDailyDataBean item)
        {
            holder.getView(R.id.apply_item_state).setVisibility(View.GONE);
            if (item.getTitle() != null)
            {
                holder.getView(R.id.apply_item_title).setVisibility(VISIBLE);
                holder.setText(R.id.apply_item_title, item.getTitle());
            } else
            {
                holder.getView(R.id.apply_item_title).setVisibility(View.INVISIBLE);
            }

            if (item.getYgName() != null)
            {
                holder.getView(R.id.apply_item_name).setVisibility(VISIBLE);
                holder.setText(R.id.apply_item_name, item.getYgName());
            } else
            {
                holder.getView(R.id.apply_item_name).setVisibility(View.INVISIBLE);
            }
            if (item.getYgDeptName() != null)
            {
                holder.getView(R.id.apply_item_department).setVisibility(VISIBLE);
                holder.setText(R.id.apply_item_department, item.getYgDeptName());
            } else
            {
                holder.getView(R.id.apply_item_department).setVisibility(View.INVISIBLE);
            }
            if (item.getCreateTime() != null)
            {
                holder.getView(R.id.apply_item_time).setVisibility(VISIBLE);
                holder.setText(R.id.apply_item_time, item.getCreateTime());
            } else
            {
                holder.getView(R.id.apply_item_time).setVisibility(View.INVISIBLE);
            }
        }
    }

    private void setHaveOrNot(List<AnnounceListBean.DataBean> mExpressLists)
    {
        for (AnnounceListBean.DataBean dataBean : mExpressLists)
        {
            if (StringUtils.notEmpty(dataBean.getCover()))
            {
                dataBean.setItemType(COMPANY_HAVEIMG);
            } else
            {
                dataBean.setItemType(COMPANY_NOIMG);
            }
        }
        SDLogUtil.debug("123456");
    }

    /**
     * 公告adapter
     */
    private class AnnounAdapter extends BaseMultiItemQuickAdapter<AnnounceListBean.DataBean, BaseViewHolder>
    {
        CornerTransform transformation = null;

        private void initView()
        {
            transformation = new CornerTransform(ApplyListActivity.this, ScreenUtils.dp2px
                    (ApplyListActivity.this, 5));
            transformation.setExceptCorner(false, false, true, true);
        }

        public AnnounAdapter(@LayoutRes int layoutResId, @Nullable List<AnnounceListBean.DataBean> data)
        {
            super(data);
            addItemType(CapitalExpressActivity.COMPANY_HAVEIMG, R.layout.announ_item_layout);
            addItemType(CapitalExpressActivity.COMPANY_NOIMG, R.layout.announ_item_layout_no_url);
            initView();
        }

        @Override
        protected void convert(final BaseViewHolder holder, AnnounceListBean.DataBean item)
        {
            switch (holder.getItemViewType())
            {
                case CapitalExpressActivity.COMPANY_HAVEIMG:
//                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                            LinearLayout.LayoutParams.WRAP_CONTENT);
//                    lp.setMargins(0, 0, 0, ScreenUtils.dp2px(ApplyListActivity.this, 15));
//                    holder.getView(R.id.ll_main).setLayoutParams(lp);

                    if (StringUtils.notEmpty(item.getCreateTime()))
                    {
                        holder.getView(R.id.tv_time).setVisibility(View.VISIBLE);
                        holder.setText(R.id.tv_time, DateUtils.getTimestampString(DateUtils.reTurnDate("yyyy-MM-dd HH:mm:ss",
                                item.getCreateTime()
                        )));
                    } else
                    {
                        holder.getView(R.id.tv_time).setVisibility(View.GONE);
                    }

                    holder.setText(R.id.tv_title, item.getTitle());
                    holder.setText(R.id.tv_content, item.getRemark());

                    if (StringUtils.notEmpty(item.getCover()))
                    {
                        DisplayMetrics dm = new DisplayMetrics();
                        dm = ApplyListActivity.this.getApplicationContext().getResources().getDisplayMetrics();
                        int viewWidth = dm.widthPixels - ScreenUtils.dp2px(ApplyListActivity.this, 30);
                        int realityHeiht = (int) (viewWidth * 598 / 1068);

                        final RoundedImageView roundedImageView = (RoundedImageView) holder.getView(R.id.img_url);
                        Glide.with(ApplyListActivity.this)
                                .load(item.getCover())
                                .centerCrop()
                                .error(R.mipmap.img_default_pic)
                                .placeholder(R.mipmap.img_default_pic)
                                .override(viewWidth, realityHeiht)
                                .into(new SimpleTarget<GlideDrawable>()
                                {
                                    @Override
                                    public void onResourceReady(GlideDrawable resource,
                                                                GlideAnimation<? super GlideDrawable> glideAnimation)
                                    {
                                        roundedImageView.setImageDrawable(resource);
                                    }
                                });
                    }

                    break;


                case CapitalExpressActivity.COMPANY_NOIMG:
                    if (StringUtils.notEmpty(item.getCreateTime()))
                    {
                        holder.getView(R.id.tv_time).setVisibility(View.VISIBLE);
                        holder.setText(R.id.tv_time, DateUtils.getTimestampString(DateUtils.reTurnDate("yyyy-MM-dd HH:mm:ss",
                                item.getCreateTime()
                        )));
                    } else
                    {
                        holder.getView(R.id.tv_time).setVisibility(View.GONE);
                    }

                    holder.setText(R.id.tv_title, item.getTitle());
                    holder.setText(R.id.tv_content, item.getRemark());

                    break;
            }
        }
    }

    /**
     * adapter
     */
    private class ScheduleAdapter extends BaseQuickAdapter<ScheduleListBean.DataBean, BaseViewHolder>
    {


        public ScheduleAdapter(@LayoutRes int layoutResId, @Nullable List<ScheduleListBean.DataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, ScheduleListBean.DataBean item)
        {
            if (item.getTitle() != null)
            {
                holder.getView(R.id.item_name).setVisibility(VISIBLE);
                holder.setText(R.id.item_name, item.getTitle());
            } else
            {
                holder.getView(R.id.item_name).setVisibility(View.INVISIBLE);
            }
            if (item.getCreateTime() != null)
            {
                holder.getView(R.id.item_time).setVisibility(VISIBLE);
                holder.setText(R.id.item_time, item.getCreateTime());
            } else
            {
                holder.getView(R.id.item_time).setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 0-5
     *
     * @param which
     * @param isSearch
     */
    public void getPageData(final int which, final String pageNumber, Class mclass, final boolean isRefresh, String search,
                            String isSearch)
    {
        ListHttpHelper.getFiveList(which, pageNumber, search, isSearch, mHttpHelper, true, new SDRequestCallBack(mclass)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                mTips.showAccessFail();
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                boolean hasData = false;
                String tips = "暂无内容";
                try
                {
                    if (which == 0)
                    {
                        LoanListBean bean = (LoanListBean) responseInfo.getResult();
                        if (bean.getData() != null && bean.getData().size() > 0)
                        {
                            mLoanLists = bean.getData();
                            hasData = true;
                            if (isRefresh)
                            {
                                mLoanAdapter.setNewData(mLoanLists);
                            } else
                            {
                                mLoanAdapter.addData(mLoanLists);
                                mLoanAdapter.notifyDataSetChanged();
                            }
                            mBackListSize = bean.getTotal();
                        } else
                        {
                            if (mLoanAdapter.getData().size() > 0)
                            {
                                hasData = true;
                            }
                        }
                        tips = "暂无借支";
                    } else if (which == 1)
                    {
                        NewLeaveListBean bean = (NewLeaveListBean) responseInfo.getResult();
                        if (bean.getData() != null && bean.getData().size() > 0)
                        {
                            mApplyLists = bean.getData();
                            hasData = true;
                            if (isRefresh)
                            {
                                mApplyAdapter.setNewData(mApplyLists);
                            } else
                            {
                                mApplyAdapter.addData(mApplyLists);
                                mApplyAdapter.notifyDataSetChanged();
                            }
                            mBackListSize = bean.getTotal();
                        } else
                        {
                            if (mApplyAdapter.getData().size() > 0)
                            {
                                hasData = true;
                            }
                        }
                        tips = "暂无休假";
                    } else if (which == 2)
                    {
                        GoOutListBean bean = (GoOutListBean) responseInfo.getResult();
                        if (bean.getData() != null && bean.getData().size() > 0)
                        {
                            mOutLists = bean.getData();
                            hasData = true;
                            if (isRefresh)
                            {
                                mOutAdapter.setNewData(mOutLists);
                            } else
                            {
                                mOutAdapter.addData(mOutLists);
                                mOutAdapter.notifyDataSetChanged();
                            }
                            mBackListSize = bean.getTotal();
                        } else
                        {
                            if (mOutAdapter.getData().size() > 0)
                            {
                                hasData = true;
                            }
                        }
                        tips = "暂无外出";
                    } else if (which == 3)
                    {
                        MyDailyListBean bean = (MyDailyListBean) responseInfo.getResult();
                        if (bean.getData() != null && bean.getData().size() > 0)
                        {
                            mMyDailyLists = bean.getData();
                            hasData = true;
                            if (isRefresh)
                            {
                                mMyDailyAdapter.setNewData(mMyDailyLists);
                            } else
                            {
                                mMyDailyAdapter.addData(mMyDailyLists);
                                mMyDailyAdapter.notifyDataSetChanged();
                            }
                            mBackListSize = bean.getTotal();
                        } else
                        {
                            if (mMyDailyAdapter.getData().size() > 0)
                            {
                                hasData = true;
                            }
                        }
                        tips = "暂无日志";
                    } else if (which == 4)
                    {
                        AnnounceListBean bean = (AnnounceListBean) responseInfo.getResult();
                        if (bean.getData() != null && bean.getData().size() > 0)
                        {
                            mAnnounceLists = bean.getData();
                            hasData = true;
                            if (isRefresh)
                            {
                                mAnnounAdapter.setNewData(mAnnounceLists);
                            } else
                            {
                                setHaveOrNot(mAnnounceLists);
                                for (AnnounceListBean.DataBean dataBean : mAnnounceLists)
                                {
                                    mAnnounAdapter.addData(0, dataBean);
                                }
                                mAnnounAdapter.notifyDataSetChanged();
                            }
                            mRecyclerView.smoothScrollToPosition(mAnnounAdapter.getItemCount() - 1);
                            mBackListSize = bean.getTotal();
                        } else
                        {
                            if (mAnnounAdapter.getData().size() > 0)
                            {
                                hasData = true;
                            }
                        }
                        tips = "暂无通知";
                        if (hasData)
                        {
                            if (mPage == 1)
                            {
                                mRecyclerView.scrollToPosition(mAnnounAdapter.getItemCount() - 1);
                            }
                        }
                    } else if (which == 5)
                    {
                        ScheduleListBean bean = (ScheduleListBean) responseInfo.getResult();
                        if (bean.getData() != null && bean.getData().size() > 0)
                        {
                            mScheduleLists = bean.getData();
                            hasData = true;
                            if (isRefresh)
                            {
                                mScheduleAdapter.setNewData(mScheduleLists);
                            } else
                            {
                                mScheduleAdapter.addData(mScheduleLists);
                                mScheduleAdapter.notifyDataSetChanged();
                            }
                            mBackListSize = bean.getTotal();
                        } else
                        {
                            if (mScheduleAdapter.getData().size() > 0)
                            {
                                hasData = true;
                            }
                        }
                        tips = "暂无会议";
                    }
                } catch (ClassCastException e)
                {
                    e.printStackTrace();
                }
                if (hasData)
                {
                    mTips.hide();
                } else
                {
                    mTips.showNoContent(tips);
                }
            }
        });
    }
}
