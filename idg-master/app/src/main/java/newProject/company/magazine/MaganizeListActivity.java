package newProject.company.magazine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.config.Constants;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.utils.SDToast;
import com.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.vacation.WebVacationActivity;
import newProject.utils.NewCommonDialog;
import yunjing.utils.DisplayUtil;
import yunjing.utils.ToastUtils;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.StatusTipsView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2017/11/27.
 */

public class MaganizeListActivity extends Activity  {

    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.loading_view)
    StatusTipsView mLoadingView;
    @Bind(R.id.title_bar)
    CustomNavigatorBar titleBar;
    @Bind(R.id.all_recycler_layout)
    RecyclerView mRecyclerview;
    @Bind(R.id.all_fragment_container)
    FrameLayout allFragmentContainer;
    private MagazineAdapter mMagazineAdapter;
    private int mPage = 1;
    private List<MagazineListBean.DataBean> mList = new ArrayList<>();
    private int mBackListSize = 0;
    private String mSearch="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_list_activity_layout);
        ButterKnife.bind(this);
        initRefresh();
        getNetData(mPage + "", mSearch, true);
    }

    public void initRefresh() {
        titleBar.setMidText("内刊");
        titleBar.setLeftImageOnClickListener(mOnClickListener);
        titleBar.setRightImageVisible(false);
        titleBar.setRightSecondImageVisible(true);
        titleBar.setRightTextVisible(false);
        titleBar.setRightSecondImageOnClickListener(mOnClickListener);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (DisplayUtil.hasNetwork(MaganizeListActivity.this)) {
                    mPage = 1;
                    mList.clear();
                    mMagazineAdapter.getData().clear();
                    getNetData(mPage + "", mSearch, true);
                    mRefreshLayout.finishRefresh(1000);
                    mRefreshLayout.setLoadmoreFinished(false);
                    mRefreshLayout.setEnableLoadmore(true);
                } else {
                    SDToast.showShort("请检查网络连接");
                    mRefreshLayout.finishRefresh(1000);
                }

            }
        });
        mRefreshLayout.setEnableLoadmoreWhenContentNotFull(true);
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (DisplayUtil.hasNetwork(MaganizeListActivity.this)) {
                    if (mMagazineAdapter.getData().size() >= mBackListSize) {
                        mRefreshLayout.finishLoadmore(1000);
                        SDToast.showShort("没有更多数据了");
                    } else {
                        mPage = mPage + 1;
                        getNetData(mPage + "", mSearch, false);
                        mRefreshLayout.finishLoadmore(1000);
                    }
                } else {
                    SDToast.showShort("请检查网络连接");
                    mRefreshLayout.finishLoadmore(1000);
                }

            }
        });
        setData();

        mLoadingView.showLoading();
        mLoadingView.setOnVisibleChangeListener(new StatusTipsView.OnVisibleChangeListener() {

            @Override
            public void onVisibleChanged(boolean visible) {
                if (mRecyclerview != null) {
                    mRecyclerview.setVisibility(visible ? GONE : VISIBLE);
                }
            }
        });
        mLoadingView.setOnRetryListener(new StatusTipsView.OnRetryListener() {

            @Override
            public void onRetry() {
                mLoadingView.showLoading();
                getNetData(mPage + "", mSearch, true);
            }
        });

        if (!DisplayUtil.hasNetwork(MaganizeListActivity.this)) {
            mLoadingView.showAccessFail();
        }

    }

    private View.OnClickListener mOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v==titleBar.getLeftImageView()){
                finish();
            }else if (v==titleBar.getRihtSecondImage()){
                final NewCommonDialog dialog=new NewCommonDialog(MaganizeListActivity.this);
                dialog.setPositiveListener(new NewCommonDialog.DialogPositiveListener() {
                    @Override
                    public void onClick(String inputText, String select) {

                    }

                    @Override
                    public void onSearchClick(String content) {
                        mSearch= content;
                        refresh();
                    }
                });
                dialog.initDialogSearch(MaganizeListActivity.this).show();
            }
        }
    };

    public void refresh(){
        if (mRecyclerview != null)
        {
            mRecyclerview.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    mPage = 1;
                    mList.clear();
                    mMagazineAdapter.getData().clear();
                    getNetData( mPage + "",mSearch,true);
                }
            }, 500);
        }
    }

    private void getNetData(String page, String s_title, final boolean isRefresh) {
        ListHttpHelper.getMagazineList(MaganizeListActivity.this, page, s_title, new SDRequestCallBack(MagazineListBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                mLoadingView.showAccessFail();
                ToastUtils.show(MaganizeListActivity.this, msg);

            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                boolean hasData = false;
                String tips = "暂无内容";
                MagazineListBean listBean = (MagazineListBean) responseInfo.getResult();
                if (null != listBean && null != listBean.getData() && !listBean.getData().isEmpty()) {
                    mList = listBean.getData();
                    hasData = true;
                    if (isRefresh) {
                        mMagazineAdapter.setNewData(mList);
                    } else {
                        mMagazineAdapter.addData(mList);
                        mMagazineAdapter.notifyDataSetChanged();
                    }
                    mBackListSize = listBean.getTotal();
                } else {
                    if (mMagazineAdapter.getData().size() > 0) {
                        hasData = true;
                    }
                }
                if (hasData) {
                    mLoadingView.hide();
                } else {
                    mLoadingView.showNoContent(tips);
                }
            }
        });
    }


    private void setData() {
        mMagazineAdapter = new MagazineAdapter(R.layout.magazine_item_layout, mList);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(MaganizeListActivity.this));
        mRecyclerview.setAdapter(mMagazineAdapter);
        mMagazineAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MagazineListBean.DataBean bean = (MagazineListBean.DataBean) adapter.getData().get(position);
                if (adapter.getData().size() > 0 && bean != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("URL", getURL(bean.getEid()));
                    bundle.putString("TITLE",bean.getTitle());
                    Intent intent = new Intent(MaganizeListActivity.this, WebVacationActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    public String getURL(int eid){
        String token= SPUtils.get(this, SPUtils.ACCESS_TOKEN, "").toString();
        String url=  Constants.getIp()+"/magazine/pdf/view/"+eid+".htm?token="+token;
        return url;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private class MagazineAdapter extends BaseQuickAdapter<MagazineListBean.DataBean, BaseViewHolder> {


        public MagazineAdapter(@LayoutRes int layoutResId, @Nullable List<MagazineListBean.DataBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, MagazineListBean.DataBean item) {
            if (item.getTitle() != null) {
                holder.setText(R.id.pdf_item_title, item.getTitle());
            } else {
                holder.setText(R.id.pdf_item_title, "");
            }
            if (item.getPublishTime() != null) {
                holder.setText(R.id.pdf_item_time, item.getPublishTime());
            } else {
                holder.setText(R.id.pdf_item_time, "");
            }
        }
    }


}
