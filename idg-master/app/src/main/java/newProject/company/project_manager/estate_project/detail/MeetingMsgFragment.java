package newProject.company.project_manager.estate_project.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.config.Constants;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.utils.SDToast;
import com.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.project_manager.investment_project.InvestmentProjectSixFragment;
import newProject.company.project_manager.investment_project.bean.MeetingListBean;
import newProject.company.vacation.WebVacationActivity;
import newProject.utils.NewCommonDialog;
import yunjing.utils.DisplayUtil;
import yunjing.utils.DividerItemDecoration2;
import yunjing.utils.ToastUtils;
import yunjing.view.StatusTipsView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * author: Created by aniu on 2018/6/15.
 */

public class MeetingMsgFragment extends Fragment {

    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.loading_view)
    StatusTipsView mLoadingView;
    private InverstmentProjectSixAdapter mAdapter;
    private List<MeetingListBean.DataBean> mDataLists = new ArrayList<>();
    private String mEid;

    public static Fragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt("EID", id);
        MeetingMsgFragment fragment = new MeetingMsgFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getIn();
        initRefresh();
        initAdapter();
        getNetData();
    }

    private void getIn() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mEid = bundle.getInt("EID", 0) + "";
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    public void initRefresh() {
        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (DisplayUtil.hasNetwork(getActivity())) {
                    getNetData();
                    mRefreshLayout.finishRefresh(1000);
                    mRefreshLayout.setLoadmoreFinished(false);
                } else {
                    SDToast.showShort("请检查网络连接");
                    mRefreshLayout.finishRefresh(1000);
                }
            }
        });

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
                getNetData();
            }
        });

        if (!DisplayUtil.hasNetwork(getContext())) {
            mLoadingView.showAccessFail();
        }

    }


    private void getNetData() {
        ListHttpHelper.getEstateMettingList(getActivity(), mEid, new SDRequestCallBack(MeetingListBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                if (error.getExceptionCode() == 400) {
                    mLoadingView.showNoContent(msg);
                }
                //   ToastUtils.show(getActivity(), msg);

            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                MeetingListBean listBean = (MeetingListBean) responseInfo.getResult();
                if (null != listBean && null != listBean.getData() && !listBean.getData().isEmpty()) {
                    mLoadingView.hide();
                    setData(listBean);
                } else {
                    mLoadingView.showNoContent("暂未有会议信息数据");
                }
            }
        });
    }

    private void setData(MeetingListBean listBean) {
        mAdapter.setNewData(listBean.getData());
    }


    private String mMeettingUrl = "/project/house/opinion/detail/h.htm?opinionId=";

    private void initAdapter() {
        mAdapter = new InverstmentProjectSixAdapter(R.layout.item_estate_project_six_layout, mDataLists);
        DividerItemDecoration2 dividerItemDecoration2 = new DividerItemDecoration2(getContext(),
                DividerItemDecoration2.VERTICAL_LIST, R.drawable.gray_shape, 0);
        mRecyclerview.addItemDecoration(dividerItemDecoration2);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MeetingListBean.DataBean dataBean = mAdapter.getData().get(position);
                String token = (String) SPUtils.get(getContext(), SPUtils.ACCESS_TOKEN, "");
                Bundle bundle = new Bundle();
                bundle.putString("URL", Constants.API_SERVER_URL + mMeettingUrl + dataBean.getOpinionId() + "&token=" + token);
                bundle.putString("TITLE", "会议详情");
                bundle.putBoolean("SHARE", false);
                Intent intent = new Intent(getActivity(), WebVacationActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.item_four) {
                    final MeetingListBean.DataBean bean = mAdapter.getData().get(position);
                    final NewCommonDialog dialog = new NewCommonDialog(getActivity());
                    dialog.setPositiveListener(new NewCommonDialog.DialogPositiveListener() {
                        @Override
                        public void onClick(String inputText, String select) {

                        }

                        @Override
                        public void onSearchClick(String content) {
                            commitApproval(bean.getOpinionId() + "");
                        }
                    });
                    dialog.initDialogTips(getActivity(), "", "请确定本条信息的准确性！").show();
                }
            }
        });
    }

    public void commitApproval(String eid) {
        ListHttpHelper.approvalEstateMeet(getActivity(), eid, new SDRequestCallBack() {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                if (error.getExceptionCode() == 400) {
                    mLoadingView.showNoContent(msg);
                }
                //   ToastUtils.show(getActivity(), msg);

            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                if (responseInfo.getMsg() != null) {
                    ToastUtils.show(getActivity(), responseInfo.getMsg());
                } else {
                    ToastUtils.show(getActivity(), "审批成功");
                }
                getNetData();

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    private class InverstmentProjectSixAdapter extends BaseQuickAdapter<MeetingListBean.DataBean, BaseViewHolder> {


        public InverstmentProjectSixAdapter(@LayoutRes int layoutResId, @Nullable List<MeetingListBean.DataBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, MeetingListBean.DataBean item) {
            holder.setText(R.id.tv_title, null == item.getOpinionTypeName() ? "" : item.getOpinionTypeName())
                    .setText(R.id.item_one, null == item.getOpinionDate() ? "" : item.getOpinionDate())
                    .setText(R.id.item_two, null == item.getConclusion() ? "" : item.getConclusion())
                    .setText(R.id.item_three, null == item.getEditByName() ? "" : item.getEditByName())
                    .setText(R.id.item_four, null == item.getApprovedByName() ? "" : item.getApprovedByName());

            if (item.getApprovedByName() != null && item.getApprovedByName().length() > 0) {
                holder.setText(R.id.item_four, null == item.getApprovedByName() ? "" : item.getApprovedByName());
                holder.setTextColor(R.id.item_four, 0xff333333);
            } else {
                if (item.getTeam() != null && item.getTeam().size() > 0) {
                    String account = DisplayUtil.getUserInfo(getActivity(), 8);
                    for (int i = 0; i < item.getTeam().size(); i++) {
                        if (item.getTeam().get(i).equals(account)) {
                            holder.setTextColor(R.id.item_four, 0xfff70909);
                            holder.setText(R.id.item_four, "我要审批");
                            holder.addOnClickListener(R.id.item_four);
                        }
                    }
                }
            }
        }
    }


}
