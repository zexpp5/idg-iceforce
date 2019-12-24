package newProject.company.project_manager.growth_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.utils.DateUtils;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.utils.SDToast;
import com.view.LoginOutDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.project_manager.growth_project.bean.FollowListBean;
import yunjing.bean.SelectItemBean;
import yunjing.utils.CommonDialog;
import yunjing.utils.DisplayUtil;
import yunjing.utils.PopupDialog;
import yunjing.view.CustomNavigatorBar;


/**
 * Created by Administrator on 2017/12/12.
 */
public class PEDetailActivity extends Activity
{

    public static final int RESULT_TYPE = 5684;
    public static final int EDIT_TYPE = 5698;
    @Bind(R.id.title_bar)
    CustomNavigatorBar mTitleBar;
    @Bind(R.id.follow_recycler_layout)
    RecyclerView mRecyclerView;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;

    private int mEid;
    private String mTitle;
    private String mProjectId;
    private FollowAdapter mFollowAdapater;
    private int mBackListSize = 0;
    private int mPage = 1;
    private Class mClass[] = {FollowListBean.class};
    private List<FollowListBean.DataBean> mFollowList = new ArrayList<>();
    private List<FollowListBean.DataBean> mClearList = new ArrayList<>();
    private String[] mFollowStrings = {"继续跟进", "放弃", "观望"};
    private PeHeadView mPeHeadView;
    private boolean mShow = true;

    private List<SelectItemBean> mInvestList = new ArrayList<>();
    private String[] mInvest = {"未约见", "已约见"};
    private SelectItemBean mInvestBean;
    private String invContactStatus = "unDate";
    View foot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedetail_layout);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            mEid = bundle.getInt("EID", 0);
            mTitle = bundle.getString("TITLE");
        }
        mTitleBar.setMidText(mTitle);
        mTitleBar.setLeftImageVisible(true);
        mTitleBar.setRightTextVisible(false);
        mTitleBar.setRightImageVisible(false);
        mTitleBar.setRightSecondImageVisible(false);
        mTitleBar.setLeftImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        mPeHeadView = new PeHeadView(PEDetailActivity.this);
        foot = LayoutInflater.from(this).inflate(R.layout.foot_ped_add, null);
        initRefresh();
        if (mPeHeadView != null)
        {
            mPeHeadView.getPageInfo(mEid);
        }
        if (mPeHeadView != null && mPeHeadView.getOpenEdit() != null)
        {
            mPeHeadView.getOpenEdit().setOnClickListener(mOnClicklistner);
        }
        foot.setOnClickListener(mOnClicklistner);
        if (mPeHeadView != null && mPeHeadView.getInvestInvite() != null)
        {
            mPeHeadView.getInvestInvite().setOnClickListener(mOnClicklistner);
        }
        getFollowInfo(mPage + "", mClass[0], mEid + "", true);

    }

    public View.OnClickListener mOnClicklistner = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (mPeHeadView != null)
            {
                if (v == mPeHeadView.getOpenEdit())
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("TITLE", mTitle);
                    bundle.putInt("EID", mEid);
                    Intent intent = new Intent(PEDetailActivity.this, PeAddActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, EDIT_TYPE);
                } else if (v == foot)
                {
                    if (mPeHeadView.getProjectId() != null)
                    {
                        mProjectId = mPeHeadView.getProjectId();
                    }
                    Intent intent = new Intent(PEDetailActivity.this, AddFollowActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(AddFollowActivity.PROJECTID, mProjectId);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, RESULT_TYPE);
                } else if (v == mPeHeadView.getInvestInvite())
                {
//                    showLogisticsClass();
                }
            }
        }
    };

    public void setHeight(boolean mShow)
    {
        if (mShow)
        {
            mClearList.clear();
            mFollowAdapater.setNewData(mClearList);
            mFollowAdapater.notifyDataSetChanged();
        } else
        {
            mFollowAdapater.setNewData(mFollowList);
            mFollowAdapater.notifyDataSetChanged();
        }
    }

    public void initRefresh()
    {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mFollowAdapater = new FollowAdapter(R.layout.follow_item_layout, mFollowList);
        mFollowAdapater.addHeaderView(mPeHeadView);
        mFollowAdapater.setFooterView(foot);
        mRecyclerView.setAdapter(mFollowAdapater);
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener()
        {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(PEDetailActivity.this))
                {
                    if (mFollowAdapater.getData().size() >= mBackListSize)
                    {
                        mRefreshLayout.finishLoadmore(1000);
                        mRefreshLayout.setLoadmoreFinished(true);
                        mRefreshLayout.setEnableLoadmore(false);
                    } else
                    {
                        mPage = mPage + 1;
                        getFollowInfo(mPage + "", mClass[0], mEid + "", false);
                        mRefreshLayout.finishLoadmore(1000);
                    }
                } else
                {
                    SDToast.showShort("请检查网络连接");
                    mRefreshLayout.finishLoadmore(1000);
                }

            }
        });
    }

    private void getFollowInfo(String pageNumber, Class mClas, String s_projId, final boolean isRefresh)
    {
        ListHttpHelper.getFollowList(PEDetailActivity.this, pageNumber, s_projId, new SDRequestCallBack(mClas)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                boolean hasData = false;
                try
                {
                    FollowListBean bean = (FollowListBean) responseInfo.getResult();
                    if (bean.getData() != null && bean.getData().size() > 0)
                    {
                        mFollowList = bean.getData();
                        hasData = true;
                        if (isRefresh)
                        {
                            mFollowAdapater.setNewData(mFollowList);
                        } else
                        {
                            mFollowAdapater.addData(mFollowList);
                            mFollowAdapater.notifyDataSetChanged();
                        }
                        mBackListSize = bean.getTotal();
                    } else
                    {
                        if (mFollowAdapater.getData().size() > 0)
                        {
                            hasData = true;
                        }
                    }
                } catch (ClassCastException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    private class FollowAdapter extends BaseQuickAdapter<FollowListBean.DataBean, BaseViewHolder>
    {


        public FollowAdapter(@LayoutRes int layoutResId, @Nullable List<FollowListBean.DataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(final BaseViewHolder holder, final FollowListBean.DataBean item)
        {
            if (holder.getLayoutPosition() == 1)
            {
                holder.setVisible(R.id.tv_title, true);
                holder.setVisible(R.id.space_item_view, false);
            } else
            {
                holder.setVisible(R.id.space_item_view, true);
                holder.setVisible(R.id.tv_title, false);
            }
            if (holder.getLayoutPosition() == mData.size())
            {
                holder.setVisible(R.id.line_big, true);
            } else
                holder.setVisible(R.id.line_big, false);
            if (item.getDevDate() != null)
            {
                holder.setText(R.id.one_content, DisplayUtil.timesTwo(DateUtils.dateToMillis("yyyy/MM/dd", item.getDevDate()) +
                        ""));
            } else
            {
                holder.setText(R.id.one_content, "");
            }
            if (item.getKeyNote() != null)
            {
                holder.setText(R.id.two_content, item.getKeyNote());
            } else
            {
                holder.setText(R.id.two_content, "");
            }
            if (item.getFollowPerson() != null)
            {
                holder.setText(R.id.four_content, item.getFollowPerson());
            } else
            {
                holder.setText(R.id.four_content, "");
            }

            if (item.getInvFlowUp() != null)
            {
                if (item.getInvFlowUp().equals("flowUp"))
                {
                    holder.setText(R.id.three_content, mFollowStrings[0]);
                } else if (item.getInvFlowUp().equals("abandon"))
                {
                    holder.setText(R.id.three_content, mFollowStrings[1]);
                } else if (item.getInvFlowUp().equals("WS"))
                {
                    holder.setText(R.id.three_content, mFollowStrings[2]);
                }
            } else
            {
                holder.setText(R.id.three_content, "");
            }

            holder.getView(R.id.delete_image).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    LoginOutDialog.deleteDialog(PEDetailActivity.this, new LoginOutDialog.loginOutListener()
                    {
                        @Override
                        public void setTrue()
                        {
                            deleteFollow(item.getDevId());
                        }

                        @Override
                        public void setCancle()
                        {

                        }
                    }, "提示", "是否确认删除");
                }
            });
            holder.getView(R.id.edit_image).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(PEDetailActivity.this, AddFollowActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(AddFollowActivity.PROJECTID, item.getProjId() + "");
                    bundle.putString(AddFollowActivity.TIME, item.getDevDate() != null ? DisplayUtil.timesTwo(DateUtils
                            .dateToMillis("yyyy/MM/dd", item.getDevDate()) +
                            "") : "");
                    bundle.putString(AddFollowActivity.KEYNOTE, item.getKeyNote());
                    bundle.putString(AddFollowActivity.FOLLOW, item.getInvFlowUp());
                    bundle.putString(AddFollowActivity.EID, item.getDevId() + "");
                    bundle.putString(AddFollowActivity.FOLLOWMMAN, item.getFollowPerson());
                    intent.putExtras(bundle);
                    startActivityForResult(intent, RESULT_TYPE);
                }
            });
        }
    }

    public void deleteFollow(int eid)
    {
        ListHttpHelper.deleteFollow(PEDetailActivity.this, eid + "", new SDRequestCallBack()
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                refreshList();
                if (responseInfo.getMsg() != null)
                {
                    SDToast.showShort(responseInfo.getMsg());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case RESULT_TYPE:
                if (resultCode == RESULT_OK && data != null)
                {
                    refreshList();
                }
                break;
            case EDIT_TYPE:
                if (mPeHeadView != null)
                {
                    mPeHeadView.getPageInfo(mEid);
                }
                break;
        }
    }

    private void refreshList()
    {
        mPage = 1;
        mFollowList.clear();
        mFollowAdapater.getData().clear();
        mFollowAdapater.notifyDataSetChanged();
        getFollowInfo(mPage + "", mClass[0], mEid + "", true);
    }

    private void showLogisticsClass()
    {
        toClassDatas();
        if (mInvestBean == null)
        {
            if (mPeHeadView.getInvContactStatus().equals("unDate"))
            {
                mInvestBean = new SelectItemBean(mInvest[0], 0, "unDate");
                mInvestBean.setSelectIndex(0);
            } else
            {
                mInvestBean = new SelectItemBean(mInvest[1], 1, "date");
                mInvestBean.setSelectIndex(1);
            }
        }
        PopupDialog.showmLogisticDialogUtil(mInvestBean, this, "约见状态", mInvestList, "0", new CommonDialog.CustomerTypeInterface()
        {
            @Override
            public void menuItemClick(SelectItemBean bean, int index)
            {
                mInvestBean = bean;
                mInvestBean.setSelectIndex(index);
                if (mPeHeadView != null && mPeHeadView.getInvestInvite() != null)
                {
                    mPeHeadView.getInvestInvite().setText(bean.getSelectString());
                }
                invContactStatus = bean.getId();
                setState();
            }
        });
    }

    //
    private void toClassDatas()
    {
        mInvestList.clear();
        mInvestList.add(new SelectItemBean(mInvest[0], 0, "unDate"));
        mInvestList.add(new SelectItemBean(mInvest[1], 1, "date"));
    }

    public void setState()
    {
        ListHttpHelper.commitState(PEDetailActivity.this, invContactStatus, mEid + "", new SDRequestCallBack()
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                if (mPeHeadView != null)
                {
                    mPeHeadView.getPageInfo(mEid);
                }
            }
        });
    }


}
