package newProject.company.leave_approval;

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
import com.chaoxiang.base.utils.MyToast;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.utils.DialogMeetingUtils;
import com.utils.SDToast;
import com.utils.ToolMainUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.resumption.ReleaveListBean;
import newProject.utils.NewCommonDialog;
import yunjing.utils.DisplayUtil;
import yunjing.utils.ToastUtils;
import yunjing.view.StatusTipsView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.injoy.idg.R.id.three_title;

/**
 * Created by Administrator on 2017/11/27.
 */

public class LeaveAppOneFragment extends Fragment
{

    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.loading_view)
    StatusTipsView mLoadingView;
    private ApplyAdapter mApplyAdapter;
    private String signed = "1";//1=批审中，2=同意，3=拒绝


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        initRefresh();
        getNetData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.vacation_approval_list_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    public void initRefresh()
    {
        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(getActivity()))
                {
                    getNetData();
                    mRefreshLayout.finishRefresh(1000);
                    mRefreshLayout.setLoadmoreFinished(false);
                } else
                {
                    SDToast.showShort("请检查网络连接");
                    mRefreshLayout.finishRefresh(1000);
                }

            }
        });

        mLoadingView.showLoading();
        mLoadingView.setOnVisibleChangeListener(new StatusTipsView.OnVisibleChangeListener()
        {

            @Override
            public void onVisibleChanged(boolean visible)
            {
                if (mRecyclerview != null)
                {
                    mRecyclerview.setVisibility(visible ? GONE : VISIBLE);
                }
            }
        });
        mLoadingView.setOnRetryListener(new StatusTipsView.OnRetryListener()
        {

            @Override
            public void onRetry()
            {
                mLoadingView.showLoading();
                getNetData();
            }
        });

        if (!DisplayUtil.hasNetwork(getContext()))
        {
            mLoadingView.showAccessFail();
        }

    }


    private void getNetData()
    {
        ListHttpHelper.getApprovalList(getActivity(), signed, new SDRequestCallBack(ReleaveListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                mLoadingView.showAccessFail();
                ToastUtils.show(getActivity(), msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                ReleaveListBean listBean = (ReleaveListBean) responseInfo.getResult();
                if (null != listBean && null != listBean.getData() && !listBean.getData().isEmpty())
                {
                    mLoadingView.hide();
                    setData(listBean);
                } else
                {
                    mLoadingView.showNoContent("暂无数据");
                }
            }
        });
    }

    private void setData(ReleaveListBean hab)
    {
        mApplyAdapter = new ApplyAdapter(R.layout.releave_item_layout, hab.getData());
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setAdapter(mApplyAdapter);
        mApplyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                final ReleaveListBean.DataBean bean = (ReleaveListBean.DataBean) adapter.getData().get(position);
                NewCommonDialog dialog = new NewCommonDialog(getActivity());
                dialog.setPositiveListener(new NewCommonDialog.DialogPositiveListener()
                {
                    @Override
                    public void onClick(String inputText, String select)
                    {
                        if (select.equals("1"))
                        {
                            commitApproval(bean.getKid() + "", 2, bean.getUserName() + "", "");
                        } else if (select.equals("2"))
                        {
                            DialogMeetingUtils.getInstance().showEditSomeThingDialog(getActivity(), "不同意",
                                    "请输入理由", new DialogMeetingUtils
                                            .onTitleClickListener()
                                    {
                                        @Override
                                        public void setTitle(String s)
                                        {
                                            commitApproval(bean.getKid() + "", 3, bean.getUserName() + "", s);
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onSearchClick(String content)
                    {

                    }
                });
                dialog.initDialog(true).show();
            }
        });
    }

    public void commitApproval(String applyId, int select, String appUser, String reason)
    {
        ListHttpHelper.commitLeaveApp(getActivity(), applyId, select + "", appUser, reason, new SDRequestCallBack()
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(getActivity(), msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                if (responseInfo.getMsg() != null)
                {
                    MyToast.showToast(getActivity(), responseInfo.getMsg());
                }
                mApplyAdapter.getData().clear();
                getNetData();
                ToolMainUtils.getInstance().getUnreadNum(getActivity(), ToolMainUtils.TYPE_RESUMPTION);
            }
        });
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    private class ApplyAdapter extends BaseQuickAdapter<ReleaveListBean.DataBean, BaseViewHolder>
    {


        public ApplyAdapter(@LayoutRes int layoutResId, @Nullable List<ReleaveListBean.DataBean> data)
        {
            super(layoutResId, data);
        }

        //(null == item.getUserName() ? "" : DisplayUtil.getName(item.getUserName(),getActivity())) + "的销假")

        @Override
        protected void convert(BaseViewHolder holder, ReleaveListBean.DataBean item)
        {
            holder.setText(R.id.one_title, (null == item.getUserName() ? "" : DisplayUtil.getName(item.getUserName(),
                    getActivity())) + "的销假")
                    .setText(R.id.two_title, "销假类型：" + (null == item.getLeaveType() ? "" : item.getLeaveType()))
                    .setText(three_title, "开始时间：" + (null == item.getResumptionBegin() ? "" : item.getResumptionBegin()))
                    .setText(R.id.four_title, "结束时间：" + (null == item.getResumptionEnd() ? "" : item.getResumptionEnd()))
                    .setText(R.id.five_title, "销假时长：" + item.getResumptionDays() + "天")
                    .setText(R.id.six_title, "申请时间：" + item.getOperateDate());

            if (item.getSigned() == 1)
            {//0-未审批  1-同意  2-不同意
                holder.setText(R.id.one_content, "批审中")
                        .setTextColor(R.id.one_content, 0xffff5f13);
            } else if (item.getSigned() == 2)
            {
                holder.setText(R.id.one_content, "同意")
                        .setTextColor(R.id.one_content, 0xff2a8301);
            } else if (item.getSigned() == 3)
            {
                holder.setText(R.id.one_content, "驳回")
                        .setTextColor(R.id.one_content, 0xffeb4849);
            }
        }
    }


}
