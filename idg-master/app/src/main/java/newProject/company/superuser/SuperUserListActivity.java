package newProject.company.superuser;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cxgz.activity.cxim.base.SDSelectContactActivity;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.utils.SDToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.superuser.bean.NoSuperUserListBean;
import newProject.company.superuser.bean.SuperUserListBean;
import yunjing.utils.DisplayUtil;
import yunjing.utils.DividerItemDecoration;
import yunjing.utils.ToastUtils;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.StatusTipsView;


/**
 * Created by tujingwu on 2017/10/26.
 */

public class SuperUserListActivity extends AppCompatActivity {

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

    private List<String> mEidLists = new ArrayList<>();
    private static final int mREQUEST_CODE = 1;

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
        mTopBar.setMidText(getResources().getString(R.string.super_user));
        mTopBar.setTitleBarBackground(DisplayUtil.mTitleColor);
        View.OnClickListener topBarListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == mTopBar.getLeftImageView()) {
                    finish();
                } else if (v == mTopBar.getRightImage()) {
                    toContactsList();
                }
            }
        };
        mTopBar.setLeftImageOnClickListener(topBarListener);
        mTopBar.setRightImageOnClickListener(topBarListener);
    }

    /**
     * 去到非超级用户联系人列表
     */
    private void setContactsListData() {
        Intent intent = new Intent(this, SDSelectContactActivity.class);
        intent.putExtra(SDSelectContactActivity.SELECTED_ONE, true);
        intent.putExtra(SDSelectContactActivity.NEED_ONCLICK_TO_CONFIRM, false);
        intent.putExtra(SDSelectContactActivity.isSuperUser, true);
        intent.putExtra(SDSelectContactActivity.isSetBg, true);

        intent.putExtra(SDSelectContactActivity.TITLE_TEXT, "超级用户");
        intent.putExtra(SDSelectContactActivity.IM_ACCOUNT_LIST, (Serializable) mEidLists);
        startActivityForResult(intent, mREQUEST_CODE);
    }

    //获取不是超级用户列表数据
    private void toContactsList() {
        ListHttpHelper.getNoSuperList(this, new SDRequestCallBack(NoSuperUserListBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                ToastUtils.show(SuperUserListActivity.this, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                NoSuperUserListBean listBean = (NoSuperUserListBean) responseInfo.getResult();
                if (null != listBean && null != listBean.getData()) {
                    for (NoSuperUserListBean.DataBean data : listBean.getData()) {
                        mEidLists.add(data.getEid() + "");
                    }

                    if (listBean.getData().size() == 0)//好坑，只好加一个-1了，没数据的时候
                        mEidLists.add("-1");
                }
                setContactsListData();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mREQUEST_CODE && resultCode == RESULT_OK && data != null)
            postSuper(data);
    }

    private void postSuper(Intent data) {
        if (!mEidLists.isEmpty())
            mEidLists.clear();

        //返回来的字符串
        List<SDUserEntity> mSendList = (List<SDUserEntity>) data.getSerializableExtra(SDSelectContactActivity.SELECTED_DATA);// str即为回传的值
        if (null == mSendList || mSendList.size() == 0)
            return;

        ListHttpHelper.postSuper(this, mSendList.get(0).getEid() + "", new SDRequestCallBack() {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                ToastUtils.show(SuperUserListActivity.this, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                ToastUtils.show(SuperUserListActivity.this, "添加成功");
                getNetData();
            }
        });

    }

    public void initRefresh() {
        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (DisplayUtil.hasNetwork(SuperUserListActivity.this)) {
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
        ListHttpHelper.getSuperList(this, new SDRequestCallBack(SuperUserListBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                ToastUtils.show(SuperUserListActivity.this, msg);
                mLoadingView.setVisibility(View.VISIBLE);
                mLoadingView.showLoading();
                mLoadingView.showAccessFail();
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                SuperUserListBean listBean = (SuperUserListBean) responseInfo.getResult();
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


    private void setData(List<SuperUserListBean.DataBean> data) {
        mAdapter = new NewColleagueListAdapter(R.layout.sp_address_list_item, data);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, R.drawable.recyclerview_divider);
        dividerItemDecoration.setDrawBorderTopAndBottom(true);
        mRecyclerview.addItemDecoration(dividerItemDecoration);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerview.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {//1=启用，0=停用
                    case R.id.start_tv:
                        mPleaseStatu = "1";
                        break;
                    case R.id.stop_tv:
                        mPleaseStatu = "0";
                        break;
                    default:
                        break;
                }

                SuperUserListBean.DataBean dataBean = (SuperUserListBean.DataBean) adapter.getData().get(position);
                posPlease(dataBean, position);
            }
        });
    }

    private void posPlease(final SuperUserListBean.DataBean dataBean, final int position) {
        ListHttpHelper.postSuperUserStatus(this, dataBean.getEid() + "", mPleaseStatu, new SDRequestCallBack() {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                ToastUtils.show(SuperUserListActivity.this, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                Object result = responseInfo.getResult();
                try {
                    int status = new JSONObject(result.toString()).getInt("status");
                    String msg = new JSONObject(result.toString()).getString("msg");
                    if (status == 200) {
                        // mAdapter.remove(position);

                        if (mAdapter.getData().isEmpty() || mAdapter.getData().size() == 0) {
                            mLoadingView.setVisibility(View.VISIBLE);
                            mLoadingView.showLoading();
                            mLoadingView.showNoContent("暂无数据");
                        }

                        dataBean.setSuperStatus(Integer.parseInt(mPleaseStatu));
                        mAdapter.notifyItemChanged(position);
                    }
                    ToastUtils.show(SuperUserListActivity.this, msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private class NewColleagueListAdapter extends BaseQuickAdapter<SuperUserListBean.DataBean, BaseViewHolder> {


        public NewColleagueListAdapter(@LayoutRes int layoutResId, @Nullable List<SuperUserListBean.DataBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, SuperUserListBean.DataBean item) {
            holder.setText(R.id.tv_name, item.getName())
                    .setText(R.id.tv_dept, item.getDeptName())
                    .setText(R.id.tv_job, item.getJob())
                    .addOnClickListener(R.id.start_tv)
                    .addOnClickListener(R.id.stop_tv);

            Glide.with(SuperUserListActivity.this)
                    .load(item.getIcon())
                    .error(R.mipmap.contact_icon)
                    .into((ImageView) holder.getView(R.id.iv_header_img));

            if (item.getSuperStatus() == 1) {
                holder.setTextColor(R.id.start_tv, 0xffffffff);
                holder.setTextColor(R.id.stop_tv, 0xff9e9e9e);
                holder.setBackgroundRes(R.id.start_tv, R.drawable.shape_blue_press_btn);
                holder.setBackgroundRes(R.id.stop_tv, R.drawable.shape_blue_normal_btn);
            } else {
                holder.setTextColor(R.id.start_tv, 0xff9e9e9e);
                holder.setTextColor(R.id.stop_tv, 0xffffffff);
                holder.setBackgroundRes(R.id.start_tv, R.drawable.shape_blue_normal_btn);
                holder.setBackgroundRes(R.id.stop_tv, R.drawable.shape_blue_press_btn);
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
