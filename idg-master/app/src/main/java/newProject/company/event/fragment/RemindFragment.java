package newProject.company.event.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
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
import newProject.company.event.fragment.adapter.RemindAdapter;
import newProject.company.project_manager.investment_project.FollowProjectDetailActivity;
import newProject.company.project_manager.investment_project.InvestedProjectsDetailActivity;
import newProject.company.project_manager.investment_project.PotentialProjectsDetailActivity;
import newProject.company.project_manager.investment_project.bean.IDGBaseBean;
import newProject.company.project_manager.investment_project.bean.ToDoListBean;
import yunjing.utils.DisplayUtil;
import yunjing.view.StatusTipsView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.utils.SPUtils.USER_ACCOUNT;

/**
 * Created by zsz on 2019/4/11.
 */

public class RemindFragment extends Fragment {
    @Bind(R.id.loading_view)
    StatusTipsView mTips;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    private int mPage = 1;
    private int mBackListSize = 0;
    List<ToDoListBean.DataBeanX.DataBean> datas = new ArrayList<>();
    RemindAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_need_to_do, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initViews();
    }

    private void initViews() {

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RemindAdapter(datas);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                initPopupWindow(view,position,((ToDoListBean.DataBeanX.DataBean)adapter.getData().get(position)).getShowDesc());
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent in = new Intent();
                if ("POTENTIAL".equals(datas.get(position).getProjType())){
                    in.setClass(getActivity(), PotentialProjectsDetailActivity.class);
                }else if ("INVESTED".equals(datas.get(position).getProjType())){
                    in.setClass(getActivity(), InvestedProjectsDetailActivity.class);
                }else if("FOLLOW_ON".equals(datas.get(position).getProjType())){
                    in.setClass(getActivity(), FollowProjectDetailActivity.class);
                }else {
                    SDToast.showShort("未知项目类型");
                    return;
                }
                in.putExtra("projName",datas.get(position).getProjName());
                in.putExtra("projId",datas.get(position).getBusId());
                startActivity(in);
            }
        });
        initRefresh();
        mTips.showLoading();
        mTips.setOnVisibleChangeListener(new StatusTipsView.OnVisibleChangeListener() {

            @Override
            public void onVisibleChanged(boolean visible) {
                if (recyclerView != null) {
                    recyclerView.setVisibility(visible ? GONE : VISIBLE);
                }
            }
        });

        mTips.setOnRetryListener(new StatusTipsView.OnRetryListener() {
            @Override
            public void onRetry() {
                mTips.showLoading();
                getData();
            }
        });

    }

    private void initRefresh() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (DisplayUtil.hasNetwork(getActivity())) {
                    mPage = 1;
                    mRefreshLayout.finishRefresh(1000);
                    mRefreshLayout.setLoadmoreFinished(false);
                    mRefreshLayout.setEnableLoadmore(true);
                    getData();
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
                if (DisplayUtil.hasNetwork(getActivity())) {
                    if (adapter.getData().size() >= mBackListSize) {
                        mRefreshLayout.finishLoadmore(1000);
                        SDToast.showShort("没有更多数据了");
                    } else {
                        mPage = mPage + 1;
                        getData();
                        mRefreshLayout.finishLoadmore(1000);
                    }
                } else {
                    SDToast.showShort("请检查网络连接");
                    mRefreshLayout.finishLoadmore(1000);
                }

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getData();
    }

    private void getData() {
        ListHttpHelper.getToDoEventList(getActivity(), SPUtils.get(getActivity(), USER_ACCOUNT, "").toString(), "TIPS", "0", "10", new SDRequestCallBack(ToDoListBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showLong(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                ToDoListBean bean = (ToDoListBean) responseInfo.getResult();
                if ("success".equals(bean.getData().getCode())) {
                    mBackListSize = bean.getData().getTotal();
                    if (mPage == 1) {
                        adapter.getData().clear();
                        if (bean.getData().getData().size() > 0) {
                            mTips.hide();
                        } else {
                            mTips.showNoContent("暂无数据");
                        }
                    }
                    adapter.getData().addAll(bean.getData().getData());
                    adapter.notifyDataSetChanged();
                } else {
                    mTips.showNoContent(bean.getData().getReturnMessage());
                }
            }
        });
    }

    public void initPopupWindow(View view, final int position,String str) {

        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.popupwindow_remind, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        setBackgroundAlpha(0.5f);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1f);
            }
        });
        TextView tvContent = (TextView) contentView.findViewById(R.id.tv_content);
        TextView tvDesc = (TextView) contentView.findViewById(R.id.tv_desc);
        tvContent.setText("确认标记为已读？");
        tvDesc.setText(str);
        //tvContent.setGravity(Gravity.CENTER);
        Button btnAgree = (Button) contentView.findViewById(R.id.btn_agree);
        btnAgree.setText("确认");
        Button btnDisagree = (Button) contentView.findViewById(R.id.btn_disagree);
        btnDisagree.setText("取消");
        btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListHttpHelper.updateTipsFlagData(getActivity(), datas.get(position).getMsgId(),new SDRequestCallBack(IDGBaseBean.class) {
                    @Override
                    public void onRequestFailure(HttpException error, String msg) {
                        SDToast.showShort(msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo) {
                        IDGBaseBean baseBean = (IDGBaseBean) responseInfo.getResult();
                        if ("success".equals(baseBean.getData().getCode())) {
                            mPage = 1;
                            getData();
                        } else {
                            SDToast.showShort(baseBean.getData().getReturnMessage());
                        }
                    }
                });
                popupWindow.dismiss();
            }
        });

        btnDisagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
    }

    public void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = alpha;
        getActivity().getWindow().setAttributes(lp);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
