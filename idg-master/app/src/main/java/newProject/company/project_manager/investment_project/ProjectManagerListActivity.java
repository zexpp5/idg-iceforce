package newProject.company.project_manager.investment_project;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.config.Constants;
import com.cxgz.activity.cx.base.BaseActivity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.util.LogUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.utils.SDToast;

import java.util.ArrayList;
import java.util.List;

import newProject.api.ListHttpHelper;
import newProject.company.project_manager.growth_project.bean.AllOpporListBean;
import newProject.company.project_manager.growth_project.bean.InfluenceListBean;
import newProject.company.project_manager.growth_project.bean.OpporListBean;
import newProject.company.project_manager.growth_project.PEDetailActivity;
import newProject.company.project_manager.growth_project.bean.UnOpporListBean;
import newProject.company.project_manager.investment_project.bean.InvestListBean;
import newProject.company.project_manager.investment_project.bean.ResearchReportBean;
import newProject.company.project_manager.investment_project.bean.TMTBean;
import newProject.company.project_manager.tmt_project.TmtDetailActivity;
import newProject.company.vacation.WebVacationActivity;
import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.DrawText;
import yunjing.view.StatusTipsView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2017/10/20.
 * positon CHOOSE  0 1 2 3 4
 * 项目管理 研究报告 潜在项目  已约见项目  未约见项目
 */
public class ProjectManagerListActivity extends BaseActivity
{
    private static final int REQUEST_SEARCH = 1001;
    private Class mClass[] = {InvestListBean.class, ResearchReportBean.class, TMTBean.class,
            OpporListBean.class, UnOpporListBean.class, AllOpporListBean.class, InfluenceListBean.class};
    private List<InvestListBean.DataBean> mProjectLists = new ArrayList<>();
    private List<ResearchReportBean.DataBean> mReportLists = new ArrayList<>();
    private List<TMTBean.DataBean> mTmtLists = new ArrayList<>();
    private List<OpporListBean.DataBean> mOpporLists = new ArrayList<>();
    private List<UnOpporListBean.DataBean> mUnOpporLists = new ArrayList<>();
    private List<AllOpporListBean.DataBean> mAllOpporLists = new ArrayList<>();
    private List<InfluenceListBean.DataBean> mInfluenceLists = new ArrayList<>();

    private ProjectAdapter mProjectAdapter;
    private ReportAdapter mReportAdapter;
    private TmTAdapter mTmtAdapter;
    private OpportAdapter mOpportAdapter;
    private AllOpportAdapter allOpportAdapter;
    private UnOpportAdapter mUnOpportAdapter;
    private InfluenceAdapter influenceAdapter;
    private RecyclerView mRecyclerView;
    private CustomNavigatorBar mNavigatorBar;
    private StatusTipsView mTips;
    private int mChoose = 0;
    private int mPage = 1;
    private int mBackListSize = 0;
    private RefreshLayout mRefreshLayout;
    private String mSearchOne = "";
    private String mSearchTwo = "";
    private String s_projName, s_induIds, s_stageIds;
    private List<String> stateIdsList = new ArrayList<>();
    private List<String> indusIdsList = new ArrayList<>();
    private String mTitle = "项目管理";
    private String mUrl = "/project/report/pdf/view/";

    @Override
    protected void init()
    {
        DisplayUtil.init(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null)
        {
            mChoose = bundle.getInt("CHOOSE", 0);
            mTitle = bundle.getString("TITLE");
        }
        //标题
        mNavigatorBar = (CustomNavigatorBar) findViewById(R.id.title_bar);
        mNavigatorBar.setLeftImageOnClickListener(mOnClickListener);
        mNavigatorBar.setLeftTextOnClickListener(mOnClickListener);
        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(false);
        mNavigatorBar.setRightSecondImageOnClickListener(mOnClickListener);
        mNavigatorBar.setRightSecondImageVisible(true);
        mNavigatorBar.setMidText(mTitle);

        //下拉刷新
        mRefreshLayout = (RefreshLayout) findViewById(R.id.smart_refresh_layout);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(ProjectManagerListActivity.this))
                {
                    mSearchOne = "";
                    /*s_projName="";
                    s_induIds="";
                    s_stageIds ="";*/
                    mPage = 1;
                    clearPage(mChoose);
                    getPageData(mChoose, mPage + "", mClass[mChoose], true, mSearchOne, mSearchTwo, s_projName, s_induIds,
                            s_stageIds);
                    mRefreshLayout.finishRefresh(1000);
                    mRefreshLayout.setLoadmoreFinished(false);
                    mRefreshLayout.setEnableLoadmore(true);
                } else
                {
                    SDToast.showShort("请检查网络连接");
                    mRefreshLayout.finishRefresh(1000);
                }
            }
        });
        mRefreshLayout.setEnableLoadmoreWhenContentNotFull(true);
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener()
        {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(ProjectManagerListActivity.this))
                {
                    if (getSize(mChoose) >= mBackListSize)
                    {
                        mRefreshLayout.finishLoadmore(1000);
                        SDToast.showShort("没有更多数据了");
                    } else
                    {
                        mPage = mPage + 1;
                        getPageData(mChoose, mPage + "", mClass[mChoose], false, mSearchOne, mSearchTwo, s_projName, s_induIds,
                                s_stageIds);
                        mRefreshLayout.finishLoadmore(1000);
                    }
                } else
                {
                    SDToast.showShort("请检查网络连接");
                    mRefreshLayout.finishLoadmore(1000);
                }

            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
        return R.layout.project_manager_list_layout;
    }

    /**
     * 初始化选择是哪个模块的列表
     */
    public void switchOne(int which)
    {
        switch (which)
        {
            case 0:
                mProjectAdapter = new ProjectAdapter(R.layout.four_item_layout, mProjectLists);
                mRecyclerView.setAdapter(mProjectAdapter);
                mProjectAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position)
                    {
                        InvestListBean.DataBean affairBean = (InvestListBean.DataBean) adapter
                                .getData().get(position);
                        if (adapter.getData().size() > 0 && affairBean != null)
                        {
                            Bundle bundle = new Bundle();
                            bundle.putInt("EID", affairBean.getProjId());
                            bundle.putString("TITLE", affairBean.getProjName());
                            Intent intent = new Intent(ProjectManagerListActivity.this, ProjectListActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }
                });
                getPageData(mChoose, mPage + "", mClass[which], false, mSearchOne, mSearchTwo, s_projName, s_induIds, s_stageIds);
                break;
            case 1:
                mReportAdapter = new ReportAdapter(R.layout.four_item_layout, mReportLists);
                mRecyclerView.setAdapter(mReportAdapter);
                mReportAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position)
                    {
                        ResearchReportBean.DataBean affairBean = (ResearchReportBean.DataBean) adapter
                                .getData().get(position);
                        if (adapter.getData().size() > 0 && affairBean != null)
                        {
                            Bundle bundle = new Bundle();
                            bundle.putString("TITLE", affairBean.getDocName());
                            String token = getToken();
                            bundle.putString("URL", Constants.API_SERVER_URL + mUrl + affairBean.getDocId() + ".htm?token=" +
                                    token);
                            Intent intent = new Intent(ProjectManagerListActivity.this, WebVacationActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }
                });
                getPageData(mChoose, mPage + "", mClass[which], false, mSearchOne, mSearchTwo, s_projName, s_induIds, s_stageIds);
                break;
            case 2:
                mTmtAdapter = new TmTAdapter(R.layout.four_item_layout, mTmtLists);
                mRecyclerView.setAdapter(mTmtAdapter);
                mTmtAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position)
                    {
                        TMTBean.DataBean affairBean = (TMTBean.DataBean) adapter
                                .getData().get(position);
                        if (adapter.getData().size() > 0 && affairBean != null)
                        {
                            Bundle bundle = new Bundle();
                            bundle.putString("TITLE", affairBean.getProjName());
                            bundle.putInt("EID", affairBean.getProjId());
                            Intent intent = new Intent(ProjectManagerListActivity.this, TmtDetailActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }
                });
                getPageData(mChoose, mPage + "", mClass[which], false, mSearchOne, mSearchTwo, s_projName, s_induIds, s_stageIds);
                break;
            case 3:
                mOpportAdapter = new OpportAdapter(R.layout.four_item_layout, mOpporLists);
                mRecyclerView.setAdapter(mOpportAdapter);
                mOpportAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position)
                    {
                        OpporListBean.DataBean affairBean = (OpporListBean.DataBean) adapter
                                .getData().get(position);
                        if (adapter.getData().size() > 0 && affairBean != null)
                        {
                            Bundle bundle = new Bundle();
                            bundle.putString("TITLE", affairBean.getProjName());
                            bundle.putInt("EID", affairBean.getProjId());
                            Intent intent = new Intent(ProjectManagerListActivity.this, PEDetailActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }
                });
                getPageData(mChoose, mPage + "", mClass[which], false, mSearchOne, mSearchTwo, s_projName, s_induIds, s_stageIds);
                break;
            case 4:
                mUnOpportAdapter = new UnOpportAdapter(R.layout.four_item_layout, mUnOpporLists);
                mRecyclerView.setAdapter(mUnOpportAdapter);
                mUnOpportAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position)
                    {
                        UnOpporListBean.DataBean affairBean = (UnOpporListBean.DataBean) adapter
                                .getData().get(position);
                        if (adapter.getData().size() > 0 && affairBean != null)
                        {
                            Bundle bundle = new Bundle();
                            bundle.putString("TITLE", affairBean.getProjName());
                            bundle.putInt("EID", affairBean.getProjId());
                            Intent intent = new Intent(ProjectManagerListActivity.this, PEDetailActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }
                });
                getPageData(mChoose, mPage + "", mClass[which], false, mSearchOne, mSearchTwo, s_projName, s_induIds, s_stageIds);
                break;
            case 5://所有
                allOpportAdapter = new AllOpportAdapter(R.layout.four_item_layout, mAllOpporLists);
                mRecyclerView.setAdapter(allOpportAdapter);
                allOpportAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position)
                    {
                        AllOpporListBean.DataBean affairBean = (AllOpporListBean.DataBean) adapter

                                .getData().get(position);
                        if (adapter.getData().size() > 0 && affairBean != null)
                        {
                            Bundle bundle = new Bundle();
                            bundle.putString("TITLE", affairBean.getProjName());
                            bundle.putInt("EID", affairBean.getProjId());
                            Intent intent = new Intent(ProjectManagerListActivity.this, PEDetailActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }
                });
                getPageData(mChoose, mPage + "", mClass[which], false, mSearchOne, mSearchTwo, s_projName, s_induIds, s_stageIds);
                break;

            case 6://最具影响力的公司
                influenceAdapter = new InfluenceAdapter(R.layout.four_item_layout, mInfluenceLists);
                mRecyclerView.setAdapter(influenceAdapter);
                influenceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position)
                    {
//                        AllOpporListBean.DataBean affairBean = (AllOpporListBean.DataBean) adapter
//
//                                .getData().get(position);
//                        if (adapter.getData().size() > 0 && affairBean != null)
//                        {
//                            Bundle bundle = new Bundle();
//                            bundle.putString("TITLE", affairBean.getProjName());
//                            bundle.putInt("EID", affairBean.getProjId());
//                            Intent intent = new Intent(ProjectManagerListActivity.this, PEDetailActivity.class);
//                            intent.putExtras(bundle);
//                            startActivity(intent);
//                        }
                    }
                });
                getPageData(mChoose, mPage + "", mClass[which], false, mSearchOne, mSearchTwo, s_projName, s_induIds, s_stageIds);
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
            mProjectLists.clear();
            mProjectAdapter.getData().clear();
        } else if (which == 1)
        {
            mReportLists.clear();
            mReportAdapter.getData().clear();
        } else if (which == 2)
        {
            mTmtLists.clear();
            mTmtAdapter.getData().clear();
        } else if (which == 3)
        {
            mOpporLists.clear();
            mOpportAdapter.getData().clear();
        } else if (which == 4)
        {
            mUnOpporLists.clear();
            mUnOpportAdapter.getData().clear();
        } else if (which == 5)
        {
            mAllOpporLists.clear();
            allOpportAdapter.getData().clear();
        } else if (which == 6)
        {
            mInfluenceLists.clear();
            influenceAdapter.getData().clear();
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
            } else if (v == mNavigatorBar.getRihtSecondImage())
            {
                Intent intent = new Intent(ProjectManagerListActivity.this, SearchListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("TYPE", mChoose);
                bundle.putString("TITLE", mTitle);
                intent.putExtras(bundle);
                intent.putStringArrayListExtra(SearchListActivity.PUT_ONE, (ArrayList<String>) stateIdsList);
                intent.putStringArrayListExtra(SearchListActivity.PUT_TWO, (ArrayList<String>) indusIdsList);
                startActivityForResult(intent, REQUEST_SEARCH);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case REQUEST_SEARCH:
                if (resultCode == RESULT_OK && data != null)
                {
                    //返回来的字符串
                    stateIdsList = data.getStringArrayListExtra(SearchListActivity.SELECTED_ONE);
                    indusIdsList = data.getStringArrayListExtra(SearchListActivity.SELECTED_TWO);
                    s_projName = data.getStringExtra(SearchListActivity.SEARCH_CONTENT);
                    mSearchOne = data.getStringExtra(SearchListActivity.SEARCH_ONE);
                    mSearchTwo = data.getStringExtra(SearchListActivity.SEARCH_TWO);
                    if (stateIdsList != null && stateIdsList.size() > 0)
                    {
                        StringBuilder builder = new StringBuilder();
                        for (int i = 0; i < stateIdsList.size(); i++)
                        {
                            builder.append(stateIdsList.get(i) + ",");
                        }
                        s_stageIds = builder.substring(0, builder.length() - 1);
                    } else
                    {
                        s_stageIds = "";
                    }
                    if (indusIdsList != null && indusIdsList.size() > 0)
                    {
                        StringBuilder builder = new StringBuilder();
                        for (int i = 0; i < indusIdsList.size(); i++)
                        {
                            builder.append(indusIdsList.get(i) + ",");
                        }
                        s_induIds = builder.substring(0, builder.length() - 1);
                    } else
                    {
                        s_induIds = "";
                    }
                    refreshList();
                }
                break;
        }
    }

    public int getSize(int which)
    {
        if (which == 0)
        {
            return mProjectAdapter.getData().size();
        } else if (which == 1)
        {
            return mReportAdapter.getData().size();
        } else if (which == 2)
        {
            return mTmtAdapter.getData().size();
        } else if (which == 3)
        {
            return mOpportAdapter.getData().size();
        } else if (which == 4)
        {
            return mUnOpportAdapter.getData().size();
        } else if (which == 5) //所有
        {
            return allOpportAdapter.getData().size();
        } else
        {
            return 0;
        }
    }


    public void getTitle(DrawText drawText, int projGroup)
    {
        String title;
        int color;
        switch (projGroup)
        {
            case -1:
            case 8:
                title = "其他";
                color = Color.argb(255, 99, 141, 208);

                break;
            case 1:
                if (mChoose == 2 || mChoose == 3 || mChoose == 4 || mChoose == 5 || mChoose == 6)
                    title = "融";
                else
                    title = "VC";
                color = Color.argb(255, 174, 17, 41);

                break;
            case 2:
                title = "工业";
                color = Color.argb(255, 210, 135, 62);

                break;
            case 3:
                if (mChoose == 2 || mChoose == 3 || mChoose == 4 || mChoose == 5 || mChoose == 6)
                    title = "潜";
                else
                    title = "PE";
                color = Color.argb(255, 174, 17, 41);

                break;
            case 4:
                title = "地产";
                color = Color.argb(255, 99, 141, 208);

                break;
            case 5:
                title = "保险";
                color = Color.argb(255, 70, 158, 133);

                break;
            case 6:
                title = "娱乐";
                color = Color.argb(255, 99, 141, 208);

                break;
            case 7:
                title = "体育";
                color = Color.argb(255, 70, 158, 133);

                break;
            case 9:
                title = "并购";
                color = Color.argb(255, 174, 17, 41);

                break;
            case 10:
                title = "医疗";
                color = Color.argb(255, 70, 158, 133);

                break;
            case 87619:
                title = "能源";
                color = Color.argb(255, 70, 158, 133);

                break;
            case 144144:
                title = "金融";
                color = Color.argb(255, 210, 135, 62);

                break;
            case 5354:
                title = "PE";
                color = Color.argb(255, 210, 135, 62);

                break;
            default:
                title = "其他";
                color = Color.argb(255, 99, 141, 208);

                break;
        }
        drawText.setColor(color);
        drawText.setText(title);
    }

    private class ProjectAdapter extends BaseQuickAdapter<InvestListBean.DataBean, BaseViewHolder>
    {
        public ProjectAdapter(@LayoutRes int layoutResId, @Nullable List<InvestListBean.DataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, InvestListBean.DataBean item)
        {
            if (item.getProjName() != null)
            {
                holder.getView(R.id.one_title).setVisibility(VISIBLE);
                holder.setText(R.id.one_title, item.getProjName());
            } else
            {
                holder.getView(R.id.one_title).setVisibility(View.GONE);
            }

            if (item.getProjManagerName() != null)
            {
                holder.getView(R.id.one_content).setVisibility(VISIBLE);
                holder.setText(R.id.one_content, item.getProjManagerName());
            } else
            {
                holder.getView(R.id.one_content).setVisibility(View.GONE);
            }

            if (item.getInduName() != null)
            {
                holder.getView(R.id.two_content).setVisibility(VISIBLE);
                holder.setText(R.id.two_content, item.getInduName());
            } else
            {
                holder.getView(R.id.two_content).setVisibility(View.GONE);
            }
            if (item.getBusiness() != null)
            {
                holder.getView(R.id.three_content).setVisibility(VISIBLE);
                holder.setText(R.id.three_content, item.getBusiness());
            } else
            {
                holder.getView(R.id.three_content).setVisibility(View.GONE);
            }

            int projGroup = item.getProjGroup();
            DrawText item_image = holder.getView(R.id.item_image);
            getTitle(item_image, projGroup);
        }
    }

    private class ReportAdapter extends BaseQuickAdapter<ResearchReportBean.DataBean, BaseViewHolder>
    {
        public ReportAdapter(@LayoutRes int layoutResId, @Nullable List<ResearchReportBean.DataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, ResearchReportBean.DataBean item)
        {
            if (item.getDocName() != null)
            {
                holder.getView(R.id.one_title).setVisibility(VISIBLE);
                holder.setText(R.id.one_title, item.getDocName());
            } else
            {
                holder.getView(R.id.one_title).setVisibility(View.GONE);
            }

            if (item.getAuthorName() != null)
            {
                holder.getView(R.id.one_content).setVisibility(VISIBLE);
                holder.setText(R.id.one_content, item.getAuthorName());
            } else
            {
                holder.getView(R.id.one_content).setVisibility(View.GONE);
            }

            if (item.getInduName() != null)
            {
                holder.getView(R.id.two_content).setVisibility(VISIBLE);
                holder.setText(R.id.two_content, item.getInduName());
            } else
            {
                holder.getView(R.id.two_content).setVisibility(View.GONE);
            }
            if (item.getSummary() != null)
            {
                holder.getView(R.id.three_content).setVisibility(VISIBLE);
                holder.setText(R.id.three_content, item.getSummary());
            } else
            {
                holder.getView(R.id.three_content).setVisibility(View.GONE);
            }

            int projGroup = item.getIndusGroup();
            DrawText item_image = holder.getView(R.id.item_image);
            getTitle(item_image, projGroup);


        }
    }

    private class TmTAdapter extends BaseQuickAdapter<TMTBean.DataBean, BaseViewHolder>
    {
        public TmTAdapter(@LayoutRes int layoutResId, @Nullable List<TMTBean.DataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, TMTBean.DataBean item)
        {
            if (item.getProjName() != null)
            {
                holder.getView(R.id.one_title).setVisibility(VISIBLE);
                holder.setText(R.id.one_title, item.getProjName());
            } else
            {
                holder.getView(R.id.one_title).setVisibility(View.GONE);
            }

            if (item.getFollowUpPersonName() != null)
            {
                holder.getView(R.id.one_content).setVisibility(VISIBLE);
                holder.setText(R.id.one_content, item.getFollowUpPersonName());
            } else
            {
                holder.getView(R.id.one_content).setVisibility(View.GONE);
            }

            if (item.getIndu() != null)
            {
                holder.getView(R.id.two_content).setVisibility(VISIBLE);
                holder.setText(R.id.two_content, item.getIndu());
            } else
            {
                holder.getView(R.id.two_content).setVisibility(View.GONE);
            }
            if (item.getZhDesc() != null)
            {
                holder.getView(R.id.three_content).setVisibility(VISIBLE);
                holder.setText(R.id.three_content, item.getZhDesc());
            } else
            {
                holder.getView(R.id.three_content).setVisibility(View.GONE);
            }


            DrawText item_image = holder.getView(R.id.item_image);
            getTitle(item_image, 1);


        }
    }

    private class OpportAdapter extends BaseQuickAdapter<OpporListBean.DataBean, BaseViewHolder>
    {
        public OpportAdapter(@LayoutRes int layoutResId, @Nullable List<OpporListBean.DataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, OpporListBean.DataBean item)
        {
            if (item.getProjName() != null)
            {
                holder.getView(R.id.one_title).setVisibility(VISIBLE);
                holder.setText(R.id.one_title, item.getProjName());
            } else
            {
                holder.getView(R.id.one_title).setVisibility(View.GONE);
            }

            if (item.getUserName() != null)
            {
                holder.getView(R.id.one_content).setVisibility(VISIBLE);
                holder.setText(R.id.one_content, item.getUserName());
            } else
            {
                holder.getView(R.id.one_content).setVisibility(View.GONE);
            }

            if (item.getComIndus() != null)
            {
                holder.getView(R.id.two_content).setVisibility(VISIBLE);
                holder.setText(R.id.two_content, item.getComIndus());
            } else
            {
                holder.getView(R.id.two_content).setVisibility(View.GONE);
            }
            if (item.getBizDesc() != null)
            {
                holder.getView(R.id.three_content).setVisibility(VISIBLE);
                holder.setText(R.id.three_content, item.getBizDesc());
            } else
            {
                holder.getView(R.id.three_content).setVisibility(View.GONE);
            }

            DrawText item_image = holder.getView(R.id.item_image);
            getTitle(item_image, 3);

        }
    }

    private class AllOpportAdapter extends BaseQuickAdapter<AllOpporListBean.DataBean, BaseViewHolder>
    {
        public AllOpportAdapter(@LayoutRes int layoutResId, @Nullable List<AllOpporListBean.DataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, AllOpporListBean.DataBean item)
        {
            if (item.getProjName() != null)
            {
                holder.getView(R.id.one_title).setVisibility(VISIBLE);
                holder.setText(R.id.one_title, item.getProjName());
            } else
            {
                holder.getView(R.id.one_title).setVisibility(View.GONE);
            }

            if (item.getUserName() != null)
            {
                holder.getView(R.id.one_content).setVisibility(VISIBLE);
                holder.setText(R.id.one_content, item.getUserName());
            } else
            {
                holder.getView(R.id.one_content).setVisibility(View.GONE);
            }

            if (item.getComIndus() != null)
            {
                holder.getView(R.id.two_content).setVisibility(VISIBLE);
                holder.setText(R.id.two_content, item.getComIndus());
            } else
            {
                holder.getView(R.id.two_content).setVisibility(View.GONE);
            }
            if (item.getBizDesc() != null)
            {
                holder.getView(R.id.three_content).setVisibility(VISIBLE);
                holder.setText(R.id.three_content, item.getBizDesc());
            } else
            {
                holder.getView(R.id.three_content).setVisibility(View.GONE);
            }
            DrawText item_image = holder.getView(R.id.item_image);
            getTitle(item_image, 3);

        }
    }


    private class InfluenceAdapter extends BaseQuickAdapter<InfluenceListBean.DataBean, BaseViewHolder>
    {
        public InfluenceAdapter(@LayoutRes int layoutResId, @Nullable List<InfluenceListBean.DataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, InfluenceListBean.DataBean item)
        {
            if (item.getProjName() != null)
            {
                holder.getView(R.id.one_title).setVisibility(VISIBLE);
                holder.setText(R.id.one_title, item.getProjName());
            } else
            {
                holder.getView(R.id.one_title).setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(item.getProjManagerName()))
            {
                holder.getView(R.id.one_content).setVisibility(VISIBLE);
                holder.setText(R.id.one_content, item.getProjManagerName());
            } else
            {
                holder.getView(R.id.one_content).setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(item.getIndusType()))
            {
                holder.getView(R.id.two_content).setVisibility(VISIBLE);
                holder.setText(R.id.two_content, item.getIndusType());
            } else
            {
                holder.getView(R.id.two_content).setVisibility(View.GONE);
            }
            if (item.getBizDesc() != null)
            {
                holder.getView(R.id.three_content).setVisibility(VISIBLE);
                holder.setText(R.id.three_content, item.getBizDesc());
            } else
            {
                holder.getView(R.id.three_content).setVisibility(View.GONE);
            }

            DrawText item_image = holder.getView(R.id.item_image);
            getTitle(item_image, 3);
        }
    }

    private class UnOpportAdapter extends BaseQuickAdapter<UnOpporListBean.DataBean, BaseViewHolder>
    {
        public UnOpportAdapter(@LayoutRes int layoutResId, @Nullable List<UnOpporListBean.DataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, UnOpporListBean.DataBean item)
        {
            if (item.getProjName() != null)
            {
                holder.getView(R.id.one_title).setVisibility(VISIBLE);
                holder.setText(R.id.one_title, item.getProjName());
            } else
            {
                holder.getView(R.id.one_title).setVisibility(View.GONE);
            }

            if (item.getUserName() != null)
            {
                holder.getView(R.id.one_content).setVisibility(VISIBLE);
                holder.setText(R.id.one_content, item.getUserName());
            } else
            {
                holder.getView(R.id.one_content).setVisibility(View.GONE);
            }

            if (item.getComIndus() != null)
            {
                holder.getView(R.id.two_content).setVisibility(VISIBLE);
                holder.setText(R.id.two_content, item.getComIndus());
            } else
            {
                holder.getView(R.id.two_content).setVisibility(View.GONE);
            }
            if (item.getBizDesc() != null)
            {
                holder.getView(R.id.three_content).setVisibility(VISIBLE);
                holder.setText(R.id.three_content, item.getBizDesc());
            } else
            {
                holder.getView(R.id.three_content).setVisibility(View.GONE);
            }
            DrawText item_image = holder.getView(R.id.item_image);
            getTitle(item_image, 3);
        }
    }

    public int selectImage(int choose)
    {
        switch (choose)
        {
            case -1:
                return R.mipmap.others_icon;
            case 1:
                return R.mipmap.tmt_icon;
            case 2:
                return R.mipmap.techo_icon;
            case 3:
                return R.mipmap.pe_growth_icon;
            case 4:
                return R.mipmap.state_group_icon;
            case 5:
                return R.mipmap.insume_icon;
            case 6:
                return R.mipmap.clutrue_icon;
            case 7:
                return R.mipmap.sport_icon;
            case 8:
                return R.mipmap.others_icon;
            case 9:
                return R.mipmap.purchase_icon;
            case 10:
                return R.mipmap.medy_icon;
            case 5354:
                return R.mipmap.pe_growth_icon;
            case 87619:
                return R.mipmap.power_icon;
            case 144144:
                return R.mipmap.fintech_icon;
            case 11:  //所有项目
                return R.mipmap.all_project;
            default:
                return R.mipmap.others_icon;
        }

    }

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
                    getPageData(mChoose, mPage + "", mClass[mChoose], false, mSearchOne, mSearchTwo, s_projName, s_induIds,
                            s_stageIds);
                }
            }, 500);
        }
    }

    public void getPageData(int which, final String pageNumber, Class mclass, final boolean isRefresh,
                            String search_one, String search_two, String s_projName, String s_induIds,
                            String s_stageIds)
    {
        ListHttpHelper.getInvestList(which, ProjectManagerListActivity.this, pageNumber, s_projName,
                s_induIds, s_stageIds, search_one, search_two, new SDRequestCallBack(mclass)
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
                            if (mChoose == 0)
                            {//项目
                                InvestListBean bean = (InvestListBean) responseInfo.getResult();
                                if (bean.getData() != null && bean.getData().size() > 0)
                                {
                                    mProjectLists = bean.getData();
                                    hasData = true;
                                    if (isRefresh)
                                    {
                                        mProjectAdapter.setNewData(mProjectLists);
                                    } else
                                    {
                                        mProjectAdapter.addData(mProjectLists);
                                        mProjectAdapter.notifyDataSetChanged();
                                    }
                                    mBackListSize = bean.getTotal();
                                } else
                                {
                                    if (mProjectAdapter.getData().size() > 0)
                                    {
                                        hasData = true;
                                    }
                                }
                            } else if (mChoose == 1)
                            {
                                ResearchReportBean bean = (ResearchReportBean) responseInfo.getResult();
                                if (bean.getData() != null && bean.getData().size() > 0)
                                {
                                    mReportLists = bean.getData();
                                    hasData = true;
                                    if (isRefresh)
                                    {
                                        mReportAdapter.setNewData(mReportLists);
                                    } else
                                    {
                                        mReportAdapter.addData(mReportLists);
                                        mReportAdapter.notifyDataSetChanged();
                                    }
                                    mBackListSize = bean.getTotal();
                                } else
                                {
                                    if (mReportAdapter.getData().size() > 0)
                                    {
                                        hasData = true;
                                    }
                                }
                            } else if (mChoose == 2)
                            {
                                TMTBean bean = (TMTBean) responseInfo.getResult();
                                if (bean.getData() != null && bean.getData().size() > 0)
                                {
                                    mTmtLists = bean.getData();
                                    hasData = true;
                                    if (isRefresh)
                                    {
                                        mTmtAdapter.setNewData(mTmtLists);
                                    } else
                                    {
                                        mTmtAdapter.addData(mTmtLists);
                                        mTmtAdapter.notifyDataSetChanged();
                                    }
                                    mBackListSize = bean.getTotal();
                                } else
                                {
                                    if (mTmtAdapter.getData().size() > 0)
                                    {
                                        hasData = true;
                                    }
                                }
                            } else if (mChoose == 3)
                            {
                                OpporListBean bean = (OpporListBean) responseInfo.getResult();
                                if (bean.getData() != null && bean.getData().size() > 0)
                                {
                                    mOpporLists = bean.getData();
                                    hasData = true;
                                    if (isRefresh)
                                    {
                                        mOpportAdapter.setNewData(mOpporLists);
                                    } else
                                    {
                                        mOpportAdapter.addData(mOpporLists);
                                        mOpportAdapter.notifyDataSetChanged();
                                    }
                                    mBackListSize = bean.getTotal();
                                } else
                                {
                                    if (mOpportAdapter.getData().size() > 0)
                                    {
                                        hasData = true;
                                    }
                                }
                            } else if (mChoose == 4)
                            {
                                UnOpporListBean bean = (UnOpporListBean) responseInfo.getResult();
                                if (bean.getData() != null && bean.getData().size() > 0)
                                {
                                    mUnOpporLists = bean.getData();
                                    hasData = true;
                                    if (isRefresh)
                                    {
                                        mUnOpportAdapter.setNewData(mUnOpporLists);
                                    } else
                                    {
                                        mUnOpportAdapter.addData(mUnOpporLists);
                                        mUnOpportAdapter.notifyDataSetChanged();
                                    }
                                    mBackListSize = bean.getTotal();
                                } else
                                {
                                    if (mUnOpportAdapter.getData().size() > 0)
                                    {
                                        hasData = true;
                                    }
                                }
                            } else if (mChoose == 5)
                            {
                                AllOpporListBean allOpporListBean = (AllOpporListBean) responseInfo.getResult();
                                if (allOpporListBean.getData() != null && allOpporListBean.getData().size() > 0)
                                {
                                    mAllOpporLists = allOpporListBean.getData();
                                    hasData = true;
                                    if (isRefresh)
                                    {
                                        allOpportAdapter.setNewData(mAllOpporLists);
                                    } else
                                    {
                                        allOpportAdapter.addData(mAllOpporLists);
                                        allOpportAdapter.notifyDataSetChanged();
                                    }
                                    mBackListSize = allOpporListBean.getTotal();
                                } else
                                {
                                    if (allOpportAdapter.getData().size() > 0)
                                    {
                                        hasData = true;
                                    }
                                }
                            } else if (mChoose == 6)
                            {
                                InfluenceListBean influenceListBean = (InfluenceListBean) responseInfo.getResult();
                                if (influenceListBean.getData() != null && influenceListBean.getData().size() > 0)
                                {
                                    mInfluenceLists = influenceListBean.getData();
                                    hasData = true;
                                    if (isRefresh)
                                    {
                                        influenceAdapter.setNewData(mInfluenceLists);
                                    } else
                                    {
                                        influenceAdapter.addData(mInfluenceLists);
                                        influenceAdapter.notifyDataSetChanged();
                                    }
                                    mBackListSize = influenceListBean.getTotal();
                                } else
                                {
                                    if (influenceAdapter.getData().size() > 0)
                                    {
                                        hasData = true;
                                    }
                                }
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
