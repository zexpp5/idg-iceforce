package newProject.company.collect;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cxgz.activity.cx.base.BaseActivity;
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

import newProject.mine.CollectHttpHelper;
import newProject.company.collect.allbean.AddressListBean;
import newProject.company.collect.allbean.CardListBean;
import newProject.company.collect.allbean.CompanyListBean;
import newProject.company.collect.allbean.IdNumberListBean;
import newProject.company.collect.allbean.LogisticListBean;
import newProject.company.collect.allbean.OtherListBean;
import newProject.company.collect.allbean.TicketListBean;
import newProject.company.collect.allfragment.CardFragment;
import newProject.company.collect.allfragment.CompanyAccountFragment;
import newProject.company.collect.allfragment.CompanyAddressFragment;
import newProject.company.collect.allfragment.IdNumberFragment;
import newProject.company.collect.allfragment.LogisticFragment;
import newProject.company.collect.allfragment.OtherFragment;
import newProject.company.collect.allfragment.TicketFragment;
import yunjing.utils.DisplayUtil;
import yunjing.utils.FragmentCallBackInterface;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.StatusTipsView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2017/10/26.
 * position 0-6 分别为
 *公司账号，开票信息，公司地址
 *个人卡号，证件号码，物流地址，其他
 */

public class AllListActivity extends BaseActivity implements FragmentCallBackInterface {

    private Class mClass[] = {CompanyListBean.class, TicketListBean.class, AddressListBean.class,
            CardListBean.class, IdNumberListBean.class,LogisticListBean.class, OtherListBean.class};
    private List<CompanyListBean.DataBean> mCompanyLists = new ArrayList<>();
    private List<TicketListBean.DataBean> mTicketLists = new ArrayList<>();
    private List<AddressListBean.DataBean> mAddressLists = new ArrayList<>();
    private List<CardListBean.DataBean> mCardLists = new ArrayList<>();
    private List<IdNumberListBean.DataBean> mIdNumberLists = new ArrayList<>();
    private List<LogisticListBean.DataBean> mLogisticLists = new ArrayList<>();
    private List<OtherListBean.DataBean> mOtherLists = new ArrayList<>();
    private CompanyAdapter mCompanyAdapter;
    private TicketAdapter mTicketAdapter;
    private AddressAdapter mAddressAdapter;
    private CardAdapter mCardAdapter;
    private NumberAdapter mNumberAdapter;
    private LogisticAdapter mLogisticAdapter;
    private OtherAdapter mOtherAdapter;
    private RecyclerView mRecyclerView;
    private FragmentManager mFragmentManager;
    private CustomNavigatorBar mNavigatorBar;
    private Fragment mSelectFragment;
    private StatusTipsView mTips;
    private int mChoose = 0;
    private int mPage = 1;
    private int mBackListSize = 0;
    private RefreshLayout mRefreshLayout;
    private String mTitle="";


    @Override
    protected void init()
    {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null)
        {
            mChoose = bundle.getInt("POSITION", 0);
            mTitle=bundle.getString("TITLE");
        }

        //标题
        mNavigatorBar = (CustomNavigatorBar) findViewById(R.id.title_bar);
        mNavigatorBar.setLeftImageOnClickListener(mOnClickListener);
        mNavigatorBar.setLeftTextOnClickListener(mOnClickListener);
        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(true);
        mNavigatorBar.setRightImageResouce(R.mipmap.icon_public_add);
        mNavigatorBar.setRightImageOnClickListener(mOnClickListener);
        mNavigatorBar.setRightSecondImageVisible(false);
        mNavigatorBar.setMidText(mTitle);
        mNavigatorBar.setTitleBarBackground(DisplayUtil.mTitleColor);
        //下拉刷新
        mRefreshLayout = (RefreshLayout) findViewById(R.id.smart_refresh_layout);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(AllListActivity.this))
                {

                    mPage = 1;
                    clearPage(mChoose);
                    getPageData(mChoose, mPage + "", mClass[mChoose], true);
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
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener()
        {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(AllListActivity.this))
                {
                    if (getSize(mChoose) >= mBackListSize)
                    {
                        mRefreshLayout.finishLoadmore(1000);
                        mRefreshLayout.setLoadmoreFinished(true);
                        mRefreshLayout.setEnableLoadmore(false);
                    } else
                    {
                        mPage = mPage + 1;
                        getPageData(mChoose, mPage + "", mClass[mChoose], false);
                        mRefreshLayout.finishLoadmore(1000);
                    }
                } else
                {
                    SDToast.showShort("请检查网络连接");
                    mRefreshLayout.finishLoadmore(1000);
                }

            }
        });


        mRecyclerView = (RecyclerView) findViewById(R.id.all_recycler_layout);
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


    /**
     * 初始化选择是哪个模块的列表
     *
     * @param which   0-6 分别为
     * 公司账号，开票信息，公司地址
     * 个人卡号，证件号码，物流地址，其他
     */
    public void switchOne(int which)
    {
        switch (which)
        {
            case 0:
                mCompanyAdapter = new CompanyAdapter(R.layout.all_item_layout, mCompanyLists);
                mRecyclerView.setAdapter(mCompanyAdapter);
                mCompanyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position)
                    {
                        CompanyListBean.DataBean bean = (CompanyListBean.DataBean) adapter.getData().get
                                (position);
                        if (adapter.getData().size() > 0 && bean != null)
                        {
                            mRecyclerView.setVisibility(GONE);
                            Bundle bundle = new Bundle();
                            bundle.putLong("EID", bean.getEid());
                            bundle.putBoolean("ADD",false);
                            CompanyAccountFragment fragment=new CompanyAccountFragment();
                            fragment.setArguments(bundle);
                            replaceFragment(fragment);
                        }
                    }
                });
                getPageData(which, mPage + "", mClass[which], false);
                break;
            case 1:
                mTicketAdapter = new TicketAdapter(R.layout.all_item_layout, mTicketLists);
                mRecyclerView.setAdapter(mTicketAdapter);
                mTicketAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position)
                    {
                        TicketListBean.DataBean bean = (TicketListBean.DataBean) adapter.getData
                                ().get(position);
                        if (adapter.getData().size() > 0 && bean != null)
                        {
                            mRecyclerView.setVisibility(GONE);
                            Bundle bundle = new Bundle();
                            bundle.putLong("EID", bean.getEid());
                            bundle.putBoolean("ADD",false);
                            TicketFragment fragment=new TicketFragment();
                            fragment.setArguments(bundle);
                            replaceFragment(fragment);
                        }
                    }
                });
                getPageData(which, mPage + "", mClass[which], false);
                break;
            case 2:
                mAddressAdapter = new AddressAdapter(R.layout.all_item_layout, mAddressLists);
                mRecyclerView.setAdapter(mAddressAdapter);
                mAddressAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position)
                    {
                        AddressListBean.DataBean bean = (AddressListBean.DataBean) adapter.getData
                                ().get(position);
                        if (adapter.getData().size() > 0 && bean != null)
                        {
                            mRecyclerView.setVisibility(GONE);
                            Bundle bundle = new Bundle();
                            bundle.putLong("EID", bean.getEid());
                            bundle.putBoolean("ADD",false);
                            CompanyAddressFragment fragment=new CompanyAddressFragment();
                            fragment.setArguments(bundle);
                            replaceFragment(fragment);
                        }

                    }
                });
                getPageData(which, mPage + "", mClass[which], false);
                break;
            case 3:
                mCardAdapter = new CardAdapter(R.layout.all_item_layout, mCardLists);
                mRecyclerView.setAdapter(mCardAdapter);
                mCardAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position)
                    {
                        if (adapter.getData().size() > 0)
                        {
                            CardListBean.DataBean approvalBean = (CardListBean.DataBean) adapter.getData().get
                                    (position);
                            if (approvalBean != null)
                            {
                                mRecyclerView.setVisibility(GONE);
                                Bundle bundle = new Bundle();
                                bundle.putLong("EID", approvalBean.getEid());
                                bundle.putBoolean("ADD",false);
                                CardFragment fragment=new CardFragment();
                                fragment.setArguments(bundle);
                                replaceFragment(fragment);
                            }
                        }
                    }
                });
                getPageData(which, mPage + "", mClass[which], false);
                break;
            case 4:
                mNumberAdapter = new NumberAdapter(R.layout.all_item_layout, mIdNumberLists);
                mRecyclerView.setAdapter(mNumberAdapter);
                mNumberAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position)
                    {
                        if (adapter.getData().size() > 0)
                        {
                            IdNumberListBean.DataBean approvalBean = (IdNumberListBean.DataBean) adapter.getData().get
                                    (position);
                            if (approvalBean != null)
                            {
                                mRecyclerView.setVisibility(GONE);
                                Bundle bundle = new Bundle();
                                bundle.putLong("EID", approvalBean.getEid());
                                bundle.putBoolean("ADD",false);
                                IdNumberFragment fragment=new IdNumberFragment();
                                fragment.setArguments(bundle);
                                replaceFragment(fragment);

                            }
                        }
                    }
                });
                getPageData(which, mPage + "", mClass[which], false);
                break;
            case 5:
                mLogisticAdapter = new LogisticAdapter(R.layout.all_item_layout, mLogisticLists);
                mRecyclerView.setAdapter(mLogisticAdapter);
                mLogisticAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position)
                    {
                        if (adapter.getData().size() > 0)
                        {
                            LogisticListBean.DataBean approvalBean = (LogisticListBean.DataBean) adapter.getData().get
                                    (position);
                            if (approvalBean != null)
                            {
                                mRecyclerView.setVisibility(GONE);
                                Bundle bundle = new Bundle();
                                bundle.putLong("EID", approvalBean.getEid());
                                bundle.putBoolean("ADD",false);
                                LogisticFragment fragment=new LogisticFragment();
                                fragment.setArguments(bundle);
                                replaceFragment(fragment);
                            }
                        }
                    }
                });
                getPageData(which, mPage + "", mClass[which], false);
                break;
            case 6:
                mOtherAdapter = new OtherAdapter(R.layout.all_item_layout, mOtherLists);
                mRecyclerView.setAdapter(mOtherAdapter);
                mOtherAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position)
                    {
                        if (adapter.getData().size() > 0)
                        {
                            OtherListBean.DataBean approvalBean = (OtherListBean.DataBean) adapter.getData().get
                                    (position);
                            if (approvalBean != null)
                            {
                                mRecyclerView.setVisibility(GONE);
                                Bundle bundle = new Bundle();
                                bundle.putLong("EID", approvalBean.getEid());
                                bundle.putBoolean("ADD",false);
                                OtherFragment fragment=new OtherFragment();
                                fragment.setArguments(bundle);
                                replaceFragment(fragment);
                            }
                        }
                    }
                });
                getPageData(which, mPage + "", mClass[which], false);
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
            mCompanyLists.clear();
            mCompanyAdapter.getData().clear();
        } else if (which == 1)
        {
            mTicketLists.clear();
            mTicketAdapter.getData().clear();
        } else if (which == 2)
        {
            mAddressLists.clear();
            mAddressAdapter.getData().clear();
        } else if (which == 3)
        {
            mCardLists.clear();
            mCardAdapter.getData().clear();
        } else if (which == 4)
        {
            mIdNumberLists.clear();
            mNumberAdapter.getData().clear();
        } else if (which == 5)
        {
            mLogisticLists.clear();
            mLogisticAdapter.getData().clear();
        } else if (which == 6)
        {
            mOtherLists.clear();
            mOtherAdapter.getData().clear();
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
                Bundle bundle=new Bundle();
                bundle.putBoolean("ADD",true);
                if (mChoose==0){
                    CompanyAccountFragment fragment=new CompanyAccountFragment();
                    fragment.setArguments(bundle);
                    replaceFragment(fragment);
                }else if (mChoose==1){
                    TicketFragment fragment=new TicketFragment();
                    fragment.setArguments(bundle);
                    replaceFragment(fragment);
                }else if (mChoose==2){
                    CompanyAddressFragment fragment=new CompanyAddressFragment();
                    fragment.setArguments(bundle);
                    replaceFragment(fragment);
                }else if (mChoose==3){
                    CardFragment fragment=new CardFragment();
                    fragment.setArguments(bundle);
                    replaceFragment(fragment);
                }else if (mChoose==4){
                    IdNumberFragment fragment=new IdNumberFragment();
                    fragment.setArguments(bundle);
                    replaceFragment(fragment);
                }else if (mChoose==5){
                    LogisticFragment fragment=new LogisticFragment();
                    fragment.setArguments(bundle);
                    replaceFragment(fragment);
                }else if (mChoose==6){
                    OtherFragment fragment=new OtherFragment();
                    fragment.setArguments(bundle);
                    replaceFragment(fragment);
                }
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
        ft.replace(R.id.all_fragment_container, fragment);
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
                    getPageData(mChoose, mPage + "", mClass[mChoose], true);
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
            return mCompanyAdapter.getData().size();
        } else if (which == 1)
        {
            return mTicketAdapter.getData().size();
        } else if (which == 2)
        {
            return mAddressAdapter.getData().size();
        } else if (which == 3)
        {
            return mCardAdapter.getData().size();
        } else
        {
            return 0;
        }
    }


    private class CompanyAdapter extends BaseQuickAdapter<CompanyListBean.DataBean, BaseViewHolder>
    {


        public CompanyAdapter(@LayoutRes int layoutResId, @Nullable List<CompanyListBean.DataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, CompanyListBean.DataBean item)
        {
            holder.setText(R.id.one_title, "公司名称：");
            holder.setText(R.id.two_title, "开户行：");
            holder.setText(R.id.three_title, "账号：");
            if (item.getCompanyName() != null)
            {
                holder.setText(R.id.one_content, item.getCompanyName());
            }else{
                holder.setText(R.id.one_content, "");
            }
            if (item.getOpenBank() != null)
            {
                holder.setText(R.id.two_content, item.getOpenBank());
            }else{
                holder.setText(R.id.two_content, "");
            }
            if (item.getAccountNum() != null)
            {
                holder.setText(R.id.three_content, item.getAccountNum());
            }else{
                holder.setText(R.id.three_content, "");
            }

        }
    }

    private class TicketAdapter extends BaseQuickAdapter<TicketListBean.DataBean, BaseViewHolder>
    {


        public TicketAdapter(@LayoutRes int layoutResId, @Nullable List<TicketListBean.DataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, TicketListBean.DataBean item)
        {
            holder.setText(R.id.one_title, "公司名称：");
            holder.setText(R.id.two_title, "纳税号：");
            holder.setText(R.id.three_title, "开户行：");
            if (item.getCompanyName() != null)
            {
                holder.setText(R.id.one_content, item.getCompanyName());
            }else{
                holder.setText(R.id.one_content, "");
            }
            if (item.getTaxNumber() != null)
            {
                holder.setText(R.id.two_content, item.getTaxNumber());
            }else{
                holder.setText(R.id.two_content, "");
            }
            if (item.getOpenBank() != null)
            {
                holder.setText(R.id.three_content, item.getOpenBank());
            }else{
                holder.setText(R.id.three_content, "");
            }
        }
    }

    private class AddressAdapter extends BaseQuickAdapter<AddressListBean.DataBean, BaseViewHolder>
    {


        public AddressAdapter(@LayoutRes int layoutResId, @Nullable List<AddressListBean.DataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, AddressListBean.DataBean item)
        {
            holder.setText(R.id.one_title, "公司名称：");
            holder.setText(R.id.two_title, "公司地址：");
            holder.setTextColor(R.id.two_title,0xffaaaaaa);
            holder.setText(R.id.three_title, "公司电话：");
            holder.setTextColor(R.id.three_title,0xffaaaaaa);
            if (item.getCompanyName() != null)
            {
                holder.setText(R.id.one_content, item.getCompanyName());
            }else{
                holder.setText(R.id.one_content, "");
            }
            if (item.getAddress() != null)
            {
                holder.setText(R.id.two_content, item.getAddress());
                holder.setTextColor(R.id.two_content,0xffaaaaaa);
            }else{
                holder.setText(R.id.two_content, "");
            }
            if (item.getTelephone() != null)
            {
                holder.setText(R.id.three_content, item.getTelephone());
                holder.setTextColor(R.id.three_content,0xffaaaaaa);
            }else{
                holder.setText(R.id.three_content, "");
            }
        }
    }

    private class CardAdapter extends BaseQuickAdapter<CardListBean.DataBean, BaseViewHolder>
    {


        public CardAdapter(@LayoutRes int layoutResId, @Nullable List<CardListBean.DataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, CardListBean.DataBean item)
        {
            holder.setText(R.id.one_title, "卡号：");
            holder.setText(R.id.two_title, "开户行：");
            holder.setTextColor(R.id.two_title,0xffaaaaaa);
            holder.setText(R.id.three_title, "客服电话：");
            holder.setTextColor(R.id.three_title,0xffaaaaaa);
            if (item.getCard() != null)
            {
                holder.setText(R.id.one_content, item.getCard());
            }else{
                holder.setText(R.id.one_content, "");
            }
            if (item.getBank() != null)
            {
                holder.setText(R.id.two_content, item.getBank());
                holder.setTextColor(R.id.two_content,0xffaaaaaa);
            }else{
                holder.setText(R.id.two_content, "");
            }
            if (item.getTelephone() != null)
            {
                holder.setText(R.id.three_content, item.getTelephone());
                holder.setTextColor(R.id.three_content,0xffaaaaaa);
            }else{
                holder.setText(R.id.three_content, "");
            }

        }
    }

    private class NumberAdapter extends BaseQuickAdapter<IdNumberListBean.DataBean, BaseViewHolder>
    {


        public NumberAdapter(@LayoutRes int layoutResId, @Nullable List<IdNumberListBean.DataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, IdNumberListBean.DataBean item)
        {
            holder.setText(R.id.one_title, "证件类型：");
            holder.setText(R.id.two_title, "证件号码：");
            holder.getView(R.id.ll_three).setVisibility(GONE);
            if (item.getType() != null)
            {
                holder.setText(R.id.one_content, item.getType());
            }else{
                holder.setText(R.id.one_content, "");
            }
            if (item.getIdNumber() != null)
            {
                holder.setText(R.id.two_content, item.getIdNumber());
            }else{
                holder.setText(R.id.two_content, "");
            }

        }
    }

    private class LogisticAdapter extends BaseQuickAdapter<LogisticListBean.DataBean, BaseViewHolder>
    {


        public LogisticAdapter(@LayoutRes int layoutResId, @Nullable List<LogisticListBean.DataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, LogisticListBean.DataBean item)
        {
            holder.setText(R.id.one_title, "收件人：");
            holder.setText(R.id.two_title, "联系电话：");
            holder.setText(R.id.three_title, "收件地址：");
            if (item.getAddressee() != null)
            {
                holder.setText(R.id.one_content, item.getAddressee());
            }else{
                holder.setText(R.id.one_content, "");
            }
            if (item.getTelephone() != null)
            {
                holder.setText(R.id.two_content, item.getTelephone());
            }else{
                holder.setText(R.id.two_content, "");
            }
            if (item.getReceiveAddress() != null)
            {
                holder.setText(R.id.three_content, item.getReceiveAddress());
            }else{
                holder.setText(R.id.three_content, "");
            }
        }
    }

    private class OtherAdapter extends BaseQuickAdapter<OtherListBean.DataBean, BaseViewHolder>
    {


        public OtherAdapter(@LayoutRes int layoutResId, @Nullable List<OtherListBean.DataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, OtherListBean.DataBean item)
        {
            holder.setText(R.id.one_title, "时间：");
            holder.setText(R.id.two_title, "标题：");
            holder.getView(R.id.ll_three).setVisibility(GONE);
            if (item.getCreateTime() != null)
            {
                holder.setText(R.id.one_content, item.getCreateTime());
            }else{
                holder.setText(R.id.one_content, "");
            }
            if (item.getTitle() != null)
            {
                holder.setText(R.id.two_content, item.getTitle());
            }else{
                holder.setText(R.id.two_content, "");
            }
        }
    }

    /**
     * 0-6
     *
     * @param which
     */
    public void getPageData(final int which, final String pageNumber, Class mclass, final boolean isRefresh)
    {
        CollectHttpHelper.getSevenList(which, pageNumber, mHttpHelper, new SDRequestCallBack(mclass)
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
                        CompanyListBean bean = (CompanyListBean) responseInfo.getResult();
                        if (bean.getData() != null && bean.getData().size() > 0)
                        {
                            mCompanyLists = bean.getData();
                            hasData = true;
                            if (isRefresh)
                            {
                                mCompanyAdapter.setNewData(mCompanyLists);
                            } else
                            {
                                mCompanyAdapter.addData(mCompanyLists);
                                mCompanyAdapter.notifyDataSetChanged();
                            }
                            mBackListSize = bean.getTotal();
                        } else
                        {
                            if (mCompanyAdapter.getData().size() > 0)
                            {
                                hasData = true;
                            }
                        }
                        tips = "暂无公司账号";
                    } else if (which == 1)
                    {
                        TicketListBean bean = (TicketListBean) responseInfo.getResult();
                        if (bean.getData() != null && bean.getData().size() > 0)
                        {
                            mTicketLists = bean.getData();
                            hasData = true;
                            if (isRefresh)
                            {
                                mTicketAdapter.setNewData(mTicketLists);
                            } else
                            {
                                mTicketAdapter.addData(mTicketLists);
                                mTicketAdapter.notifyDataSetChanged();
                            }
                            mBackListSize = bean.getTotal();
                        } else
                        {
                            if (mTicketAdapter.getData().size() > 0)
                            {
                                hasData = true;
                            }
                        }
                        tips = "暂无内容";
                    } else if (which == 2)
                    {
                        AddressListBean bean = (AddressListBean) responseInfo.getResult();
                        if (bean.getData() != null && bean.getData().size() > 0)
                        {
                            mAddressLists = bean.getData();
                            hasData = true;
                            if (isRefresh)
                            {
                                mAddressAdapter.setNewData(mAddressLists);
                            } else
                            {
                                mAddressAdapter.addData(mAddressLists);
                                mAddressAdapter.notifyDataSetChanged();
                            }
                            mBackListSize = bean.getTotal();
                        } else
                        {
                            if (mAddressAdapter.getData().size() > 0)
                            {
                                hasData = true;
                            }
                        }
                        tips = "暂无公司地址";
                    } else if (which == 3)
                    {
                        CardListBean bean = (CardListBean) responseInfo.getResult();
                        if (bean.getData() != null && bean.getData().size() > 0)
                        {
                            mCardLists = bean.getData();
                            hasData = true;
                            if (isRefresh)
                            {
                                mCardAdapter.setNewData(mCardLists);
                            } else
                            {
                                mCardAdapter.addData(mCardLists);
                                mCardAdapter.notifyDataSetChanged();
                            }
                            mBackListSize = bean.getTotal();
                        } else
                        {
                            if (mCardAdapter.getData().size() > 0)
                            {
                                hasData = true;
                            }
                        }
                        tips = "暂无个人卡号";
                    }else if (which == 4)
                    {
                        IdNumberListBean bean = (IdNumberListBean) responseInfo.getResult();
                        if (bean.getData() != null && bean.getData().size() > 0)
                        {
                            mIdNumberLists = bean.getData();
                            hasData = true;
                            if (isRefresh)
                            {
                                mNumberAdapter.setNewData(mIdNumberLists);
                            } else
                            {
                                mNumberAdapter.addData(mIdNumberLists);
                                mNumberAdapter.notifyDataSetChanged();
                            }
                            mBackListSize = bean.getTotal();
                        } else
                        {
                            if (mNumberAdapter.getData().size() > 0)
                            {
                                hasData = true;
                            }
                        }
                        tips = "暂无证件号码";
                    }else if (which == 5)
                    {
                        LogisticListBean bean = (LogisticListBean) responseInfo.getResult();
                        if (bean.getData() != null && bean.getData().size() > 0)
                        {
                            mLogisticLists = bean.getData();
                            hasData = true;
                            if (isRefresh)
                            {
                                mLogisticAdapter.setNewData(mLogisticLists);
                            } else
                            {
                                mLogisticAdapter.addData(mLogisticLists);
                                mLogisticAdapter.notifyDataSetChanged();
                            }
                            mBackListSize = bean.getTotal();
                        } else
                        {
                            if (mLogisticAdapter.getData().size() > 0)
                            {
                                hasData = true;
                            }
                        }
                        tips = "暂无物流地址";
                    }else if (which == 6)
                    {
                        OtherListBean bean = (OtherListBean) responseInfo.getResult();
                        if (bean.getData() != null && bean.getData().size() > 0)
                        {
                            mOtherLists = bean.getData();
                            hasData = true;
                            if (isRefresh)
                            {
                                mOtherAdapter.setNewData(mOtherLists);
                            } else
                            {
                                mOtherAdapter.addData(mOtherLists);
                                mOtherAdapter.notifyDataSetChanged();
                            }
                            mBackListSize = bean.getTotal();
                        } else
                        {
                            if (mOtherAdapter.getData().size() > 0)
                            {
                                hasData = true;
                            }
                        }
                        tips = "暂无其他";
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



    @Override
    protected int getContentLayout() {
        return R.layout.all_list_activity_layout;
    }
}
