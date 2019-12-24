package newProject.company.superpower;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.superpower.bean.NewColleagueListBean;
import yunjing.bean.ListDialogBean;
import yunjing.utils.DisplayUtil;
import yunjing.utils.DividerItemDecoration;
import yunjing.utils.ToastUtils;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.DialogListView;
import yunjing.view.StatusTipsView;


/**
 * Created by tujingwu on 2017/10/26.
 */

public class NewColleagueListActivity extends AppCompatActivity {

    @Bind(R.id.title_bar)
    CustomNavigatorBar mTopBar;

    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.loading_view)
    StatusTipsView mLoadingView;

    private String mPleaseStatu = "";
    private NewColleagueListAdapter mAdapter;
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
        mTopBar.setMidText(getResources().getString(R.string.im_new_friend_title));
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
                if (DisplayUtil.hasNetwork(NewColleagueListActivity.this)) {
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
                ToastUtils.show(NewColleagueListActivity.this, msg);
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
        mAdapter = new NewColleagueListAdapter(R.layout.item_new_colleague_list_layout, data);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, R.drawable.recyclerview_divider);
        dividerItemDecoration.setDrawBorderTopAndBottom(true);
        mRecyclerview.addItemDecoration(dividerItemDecoration);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerview.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                NewColleagueListBean.DataBean dataBean = (NewColleagueListBean.DataBean) adapter.getData().get(position);
                switch (view.getId()) {
                    case R.id.agree_tv://同意
                        mPleaseStatu = "1";
                        if (mSuperiorList.isEmpty() || mSuperiorList.size() == 0)
                            getSuperiorData(dataBean, position);
                        else
                            initListDialog(mSuperiorList, dataBean, position);
                        break;
                    case R.id.refuse_tv://拒绝
                        mPleaseStatu = "2";
                        posPlease(data.get(position).getEid() + "", "", position);
                        break;
                    default:
                        break;
                }

            }
        });
    }

    /**
     * 获取上下级列表数据
     *
     * @param dataBean
     * @param position
     */
    private void getSuperiorData(final NewColleagueListBean.DataBean dataBean, final int position) {
        ListHttpHelper.getUsersByCode(this, "", new SDRequestCallBack() {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.getResult().toString());
                    JSONArray dataArray = jsonObject.getJSONArray("data");
                    if (null != dataArray) {
                        for (int i = 0; i < dataArray.length(); i++) {
                            ListDialogBean dialogBean = new ListDialogBean();
                            int userId = dataArray.getJSONObject(i).getInt("userId");
                            String userName = dataArray.getJSONObject(i).getString("userName");

                            dialogBean.setCheck(false);
                            dialogBean.setContent(userName);
                            dialogBean.setId(userId);

                            mSuperiorList.add(dialogBean);
                        }
                        initListDialog(mSuperiorList, dataBean, position);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initListDialog(final List<ListDialogBean> data, final NewColleagueListBean.DataBean dataBean, final int position) {
        if (mDialogListView == null)
            mDialogListView = DialogListView.newBuilder(this)
                    .cancelTouchout(false)
                    .isMultiselect(false)
                    .style(R.style.dialogStyle)
                    .setItemListener(new DialogListView.SigleSelectListener() {
                        @Override
                        public void itemSelectListener(ListDialogBean selectItemBean) {
                            //执行同意
                            posPlease(dataBean.getEid() + "", selectItemBean.getId() + "", position);
                        }
                    })
                    .build();
        mDialogListView.show();
        mDialogListView.setTitle("上级");
        mDialogListView.setData(data);
    }


    private void posPlease(String eid, String pid, final int position) {
        ListHttpHelper.postNewC(this, pid, eid, mPleaseStatu, new SDRequestCallBack() {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                ToastUtils.show(NewColleagueListActivity.this, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                Object result = responseInfo.getResult();
                try {
                    int status = new JSONObject(result.toString()).getInt("status");
                    String msg = new JSONObject(result.toString()).getString("msg");
                    if (status == 200) {
                        mAdapter.remove(position);

                        if (mAdapter.getData().isEmpty() || mAdapter.getData().size() == 0) {
                            mLoadingView.setVisibility(View.VISIBLE);
                            mLoadingView.showLoading();
                            mLoadingView.showNoContent("暂无数据");
                        }
                    }
                    ToastUtils.show(NewColleagueListActivity.this, msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private class NewColleagueListAdapter extends BaseQuickAdapter<NewColleagueListBean.DataBean, BaseViewHolder> {


        public NewColleagueListAdapter(@LayoutRes int layoutResId, @Nullable List<NewColleagueListBean.DataBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, NewColleagueListBean.DataBean item) {
            holder.getView(R.id.agree_tv).setPressed(true);
            holder.setText(R.id.tv_name, item.getName())
                    .addOnClickListener(R.id.agree_tv)
                    .addOnClickListener(R.id.refuse_tv);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
