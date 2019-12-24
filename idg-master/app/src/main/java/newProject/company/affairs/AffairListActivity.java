package newProject.company.affairs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cxgz.activity.cx.base.BaseActivity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.chaoxiang.base.config.Constants;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.utils.SDToast;

import java.util.ArrayList;
import java.util.List;

import newProject.api.ListHttpHelper;
import newProject.company.affairs.bean.AffairListBean;
import newProject.company.project.ProjectDetailFragment;
import newProject.company.project.ProjectSubmitFragment;
import newProject.utils.NewCommonDialog;
import yunjing.utils.DisplayUtil;
import yunjing.utils.FragmentCallBackInterface;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.StatusTipsView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2017/10/20.
 * positon 0,1
 * 事务申请  1位项目协同
 *
 */

public class AffairListActivity extends BaseActivity implements FragmentCallBackInterface {
    private Class mClass[] = {AffairListBean.class,ProjectCoopListBean.class};
    private List<AffairListBean.AffairListDataBean> mAffairLists = new ArrayList<>();
    private List<ProjectCoopListBean.ProjectDataBean> mProjectLists = new ArrayList<>();
    private TextView mApplyAddText,mApplySearchText;
    private LinearLayout mFirstLL,mSecondLL;
    private AffairAdapter mAffairAdapter;
    private ProjectAdapter mProjectAdapter;
    private RecyclerView mRecyclerView;
    private FragmentManager mFragmentManager;
    private CustomNavigatorBar mNavigatorBar;
    private Fragment mSelectFragment;
    private StatusTipsView mTips;
    private int mChoose = 0;
    private int mPage = 1;
    private int mBackListSize = 0;
    private RefreshLayout mRefreshLayout;
    private String mSearch="";
    private RelativeLayout mTopBarLayout;
    private View mSpaceView;
    private String isSearch="";
    private boolean isHide=false;
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
            isHide=bundle.getBoolean("HIDE");
        }
        //控制搜索进来隐藏
        mTopBarLayout= (RelativeLayout) findViewById(R.id.top_btn_layout);
        mSpaceView=findViewById(R.id.space_view);
        //初始化搜索有内容
        if (!TextUtils.isEmpty(mSearch) ||isHide){
            mTopBarLayout.setVisibility(GONE);
            mSpaceView.setVisibility(GONE);
        }else{
            mTopBarLayout.setVisibility(VISIBLE);
            mSpaceView.setVisibility(VISIBLE);
        }
        //按钮文字
        mApplyAddText= (TextView) findViewById(R.id.apply_add_tv);
        mApplySearchText= (TextView) findViewById(R.id.apply_search_tv);
        mFirstLL= (LinearLayout) findViewById(R.id.first_ll);
        mFirstLL.setOnClickListener(mOnClickListener);
        mSecondLL= (LinearLayout) findViewById(R.id.second_ll);
        mSecondLL.setOnClickListener(mOnClickListener);
        //标题
        mNavigatorBar = (CustomNavigatorBar) findViewById(R.id.title_bar);
        mNavigatorBar.setLeftImageOnClickListener(mOnClickListener);
        mNavigatorBar.setLeftTextOnClickListener(mOnClickListener);
        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(false);
        mNavigatorBar.setRightSecondImageVisible(false);
        if (mChoose==0){
            mNavigatorBar.setMidText("事务报告");
            mApplyAddText.setText("提交报告");
            mApplySearchText.setText("查找报告");
        }else if (mChoose==1){
            mNavigatorBar.setMidText("项目协同");
            mApplyAddText.setText("创建项目");
            mApplySearchText.setText("查找项目");
        }

        //下拉刷新
        mRefreshLayout = (RefreshLayout) findViewById(R.id.smart_refresh_layout);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(AffairListActivity.this))
                {
                    mSearch="";
                    mPage = 1;
                    clearPage(mChoose);
                    getPageData(mChoose,mPage + "", mClass[mChoose], true,mSearch, isSearch);
                    mRefreshLayout.finishRefresh(1000);
                    mRefreshLayout.setLoadmoreFinished(false);
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
                if (DisplayUtil.hasNetwork(AffairListActivity.this))
                {
                    if (getSize(mChoose) >= mBackListSize)
                    {
                        mRefreshLayout.finishLoadmore(1000);
                        mRefreshLayout.setLoadmoreFinished(true);
                    } else
                    {
                        mPage = mPage + 1;
                        getPageData(mChoose,mPage + "", mClass[mChoose], false,mSearch, isSearch);
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
        return R.layout.apply_list_activity_layout_one;
    }


    /**
     * 初始化选择是哪个模块的列表
     */
    public void switchOne(int which)
    {
        switch (which)
        {
            case 0:
                mAffairAdapter = new AffairAdapter(R.layout.apply_item_layout, mAffairLists);
                mRecyclerView.setAdapter(mAffairAdapter);
                mAffairAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position)
                    {
                        AffairListBean.AffairListDataBean affairBean = (AffairListBean.AffairListDataBean) adapter.getData().get
                                (position);
                        if (adapter.getData().size() > 0 && affairBean != null)
                        {
                            mRecyclerView.setVisibility(GONE);
                            Bundle bundle = new Bundle();
                            bundle.putLong("EID", affairBean.getEid());
                            AffairsDetailFragment fragment = new AffairsDetailFragment();
                            fragment.setArguments(bundle);
                            replaceFragment(fragment);
                        }
                    }
                });
                getPageData(mChoose,mPage + "", mClass[which], false,mSearch,isSearch);
                break;
            case 1:
                mProjectAdapter = new ProjectAdapter(R.layout.apply_item_layout, mProjectLists);
                mRecyclerView.setAdapter(mProjectAdapter);
                mProjectAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position)
                    {
                        ProjectCoopListBean.ProjectDataBean affairBean = (ProjectCoopListBean.ProjectDataBean) adapter
                                .getData().get(position);
                        if (adapter.getData().size() > 0 && affairBean != null)
                        {
                            mRecyclerView.setVisibility(GONE);
                            Bundle bundle = new Bundle();
                            bundle.putLong("EID", affairBean.getEid());
                            ProjectDetailFragment fragment = new ProjectDetailFragment();
                            fragment.setArguments(bundle);
                            replaceFragment(fragment);
                        }
                    }
                });
                getPageData(mChoose,mPage + "", mClass[which], false,mSearch, isSearch);
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
            mAffairLists.clear();
            mAffairAdapter.getData().clear();
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
            if (v == mFirstLL)
            {
                if (mChoose==0) {
                    AffairsReportFragment fragment = new AffairsReportFragment();
                    replaceFragment(fragment);
                }else if (mChoose==1){
                    ProjectSubmitFragment fragment = new ProjectSubmitFragment();
                    replaceFragment(fragment);
                }
            }
            if (v == mSecondLL)
            {
                final NewCommonDialog dialog=new NewCommonDialog(AffairListActivity.this);
                dialog.setPositiveListener(new NewCommonDialog.DialogPositiveListener() {
                    @Override
                    public void onClick(String inputText, String select) {

                    }

                    @Override
                    public void onSearchClick(String content) {
                        mSearch= content;
                        refreshList();
                    }
                });
                dialog.initDialogSearch(AffairListActivity.this).show();
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
    public void callBackObject(Object object)
    {

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
                    getPageData(mChoose,mPage + "", mClass[mChoose], true,mSearch, isSearch);
                }
            }, 500);
        }
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
        if (which == 0) {
            return mAffairAdapter.getData().size();
        }else {
            return 0;
        }
    }


    private class AffairAdapter extends BaseQuickAdapter<AffairListBean.AffairListDataBean, BaseViewHolder>
    {


        public AffairAdapter(@LayoutRes int layoutResId, @Nullable List<AffairListBean.AffairListDataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, AffairListBean.AffairListDataBean item)
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
                /*if ((item.getEid()+"").equals(DisplayUtil.getUserInfo(AffairListActivity.this,3))){
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

    private class ProjectAdapter extends BaseQuickAdapter<ProjectCoopListBean.ProjectDataBean, BaseViewHolder>
    {
        public ProjectAdapter(@LayoutRes int layoutResId, @Nullable List<ProjectCoopListBean.ProjectDataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, ProjectCoopListBean.ProjectDataBean item)
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


    public void getPageData(int which, final String pageNumber, Class mclass, final boolean isRefresh, String search, String isSearch)
    {
        ListHttpHelper.getTwoList(which,pageNumber,search,isSearch,mHttpHelper, new SDRequestCallBack(mclass)
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
                    {
                        AffairListBean bean = (AffairListBean) responseInfo.getResult();
                        if (bean.getData() != null && bean.getData().size() > 0)
                        {
                            mAffairLists = bean.getData();
                            hasData = true;
                            if (isRefresh)
                            {
                                mAffairAdapter.setNewData(mAffairLists);
                            } else
                            {
                                mAffairAdapter.addData(mAffairLists);
                                mAffairAdapter.notifyDataSetChanged();
                            }
                            mBackListSize = bean.getTotal();
                        } else
                        {
                            if (mAffairAdapter.getData().size() > 0)
                            {
                                hasData = true;
                            }
                        }
                        tips = "暂无事务";
                    }else if (mChoose == 1){//项目
                        ProjectCoopListBean bean = (ProjectCoopListBean) responseInfo.getResult();
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
                        tips = "暂无事务";
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
