package newProject.company.invoice_info;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.utils.SDToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.superpower.bean.NewColleagueListBean;
import yunjing.bean.ListDialogBean;
import yunjing.utils.DisplayUtil;
import yunjing.utils.ToastUtils;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.DialogListView;
import yunjing.view.StatusTipsView;


/**
 * Created by tujingwu on 2017/10/26.
 */

public class InvoiceInfoListActivity extends AppCompatActivity {

    @Bind(R.id.title_bar)
    CustomNavigatorBar mTopBar;

    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.loading_view)
    StatusTipsView mLoadingView;

    private String mPleaseStatu = "";
    private InvoiceInfoListAdapter mAdapter;
    private DialogListView mDialogListView;
    private List<ListDialogBean> mSuperiorList = new ArrayList<>();//上下级列表数据

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_colleague_list_layout);
        ButterKnife.bind(this);
        initTopBar();
        initRefresh();
        getNetData();
    }

    private void initTopBar() {
        mTopBar.setLeftTextVisible(false);
        mTopBar.setRightTextVisible(false);
        mTopBar.setRightImageVisible(true);
        mTopBar.setRightSecondImageVisible(true);
        mTopBar.setMidText("发票信息");
        mTopBar.setTitleBarBackground(DisplayUtil.mTitleColor);
        View.OnClickListener topBarListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == mTopBar.getLeftImageView()) {
                    finish();
                }
            }
        };
        mTopBar.setLeftImageOnClickListener(topBarListener);
    }

    public void initRefresh() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (DisplayUtil.hasNetwork(InvoiceInfoListActivity.this)) {
                    getNetData();
                    mRefreshLayout.finishRefresh(1000);
                    mRefreshLayout.setLoadmoreFinished(false);
                } else {
                    SDToast.showShort("请检查网络连接");
                    mRefreshLayout.finishRefresh(1000);
                }

            }
        });


        mLoadingView.setOnRetryListener(new StatusTipsView.OnRetryListener() {

            @Override
            public void onRetry() {
                getNetData();
            }
        });

    }

    private void getNetData() {
        ListHttpHelper.getNewList(this, new SDRequestCallBack(NewColleagueListBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                ToastUtils.show(InvoiceInfoListActivity.this, msg);
                mLoadingView.setVisibility(View.VISIBLE);
                mLoadingView.showLoading();
                mLoadingView.showAccessFail();
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                NewColleagueListBean listBean = (NewColleagueListBean) responseInfo.getResult();
                if (null != listBean && null != listBean.getData() && !listBean.getData().isEmpty()) {
                    mLoadingView.hide();
                    setData(listBean.getData());
                } else {
                    mLoadingView.setVisibility(View.VISIBLE);
                    mLoadingView.showLoading();
                    mLoadingView.showNoContent("暂无数据");
                }
            }
        });
    }


    private void setData(final List<NewColleagueListBean.DataBean> data) {
        mAdapter = new InvoiceInfoListAdapter(R.layout.item_invoice_info_list_layout, data);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerview.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
    }




    private class InvoiceInfoListAdapter extends BaseQuickAdapter<NewColleagueListBean.DataBean, BaseViewHolder> {


        public InvoiceInfoListAdapter(@LayoutRes int layoutResId, @Nullable List<NewColleagueListBean.DataBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, NewColleagueListBean.DataBean item) {
            holder.setText(R.id.tv_name, item.getName())
                    .setText(R.id.tv_tax_number, "")
                    .setText(R.id.tv_bank_name, "");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
