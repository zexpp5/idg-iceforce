package newProject.ui.system_msg;

import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cxgz.activity.cx.base.BaseActivity;
import com.http.HttpURLUtil;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.utils.SDToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.ApplyListActivity;
import newProject.api.ListHttpHelper;
import newProject.company.business_trip.approval.BusinessTripApprovalListActivity;
import newProject.company.business_trip.approval.BusinessTripDetailActivity;
import newProject.company.capital_express.CapitalExpressActivity;
import newProject.company.capital_express.ExpressListBean;
import newProject.company.leave_approval.LeaveAppListActivity;
import newProject.company.myapproval.MyApprovalNewActivity;
import newProject.company.vacation.VacationApprovalDetailActivity;
import newProject.company.vacation.VacationApprovalListActivity;
import newProject.company.vacation.VacationDetailActivity;
import newProject.company.vacation.WebVacationActivity;
import newProject.ui.work.GeneralMeetingActivity;
import newProject.ui.work.SuperGMeetingActivity;
import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.StatusTipsView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static java.util.Collections.addAll;

/**
 * @author selson
 *         系统消息
 */
public class SystemMsgListActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener
{
    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.loading_view)
    StatusTipsView mTips;
    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    private int mPage = 1;
    private int mBackListSize = 0;
    SystemMsgAdapter projectEstateAdapter;


    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_system_msg_list;
    }

    @Override
    protected void init()
    {
        ButterKnife.bind(this);
        View.OnClickListener onClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        };
        //标题
        mNavigatorBar = (CustomNavigatorBar) findViewById(R.id.title_bar);
        mNavigatorBar.setLeftImageOnClickListener(onClickListener);
        mNavigatorBar.setLeftTextOnClickListener(onClickListener);
        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(false);
        mNavigatorBar.setRightSecondImageVisible(false);
        mNavigatorBar.setMidText("系统消息");
        recycler_view.setLayoutManager(new LinearLayoutManager(this));


        projectEstateAdapter = new SystemMsgAdapter(new ArrayList<SystemMsgListBean.DataBean>());
        recycler_view.setAdapter(projectEstateAdapter);
        initRefresh();


        mTips = (StatusTipsView) findViewById(R.id.loading_view);
        mTips.showLoading();
        mTips.setOnVisibleChangeListener(new StatusTipsView.OnVisibleChangeListener()
        {

            @Override
            public void onVisibleChanged(boolean visible)
            {
                if (recycler_view != null)
                {
                    recycler_view.setVisibility(visible ? GONE : VISIBLE);
                }
            }
        });

        mTips.setOnRetryListener(new StatusTipsView.OnRetryListener()
        {
            @Override
            public void onRetry()
            {
                mTips.showLoading();
                getData();
            }
        });
        getData();
        projectEstateAdapter.setOnItemClickListener(this);
    }

    private void initRefresh()
    {
        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(SystemMsgListActivity.this))
                {
                    if (projectEstateAdapter.getData().size() >= mBackListSize)
                    {
                        mRefreshLayout.finishRefresh(1000);
                    } else
                    {
                        mPage = mPage + 1;
                        getData();
                        mRefreshLayout.finishRefresh(1000);
                    }
                } else
                {
                    SDToast.showShort("请检查网络连接");
                    mRefreshLayout.finishRefresh(1000);
                }

            }
        });
    }


    /**
     * 获取网络数据
     */
    public void getData()
    {
        ListHttpHelper.getSystemMsgList(this, mPage + "", true, new SDRequestCallBack(SystemMsgListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                // ToastUtils.show(getActivity(), msg);
                mTips.showAccessFail();
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                SystemMsgListBean result = (SystemMsgListBean) responseInfo.getResult();
                List<SystemMsgListBean.DataBean> data = result.getData();
                mTips.hide();
                if (mPage == 1)
                {
                    projectEstateAdapter.getData().clear();
                    mBackListSize = result.getTotal();
                    if (data.size() > 0)
                    {
                    } else
                    {
                        mTips.showNoContent("暂无数据");
                    }
                }
                for (SystemMsgListBean.DataBean dataBean : data)
                {
                    projectEstateAdapter.getData().add(0, dataBean);
                }
                projectEstateAdapter.notifyDataSetChanged();

                if (mPage == 1)
                {
                    recycler_view.scrollToPosition(projectEstateAdapter.getItemCount() - 1);
                }
            }
        });
    }

    /**
     * 点击详情
     * <p>
     * "1"   借支申请
     * "2"   请假申请
     * "3"   事务申请 - 事务报告
     * "4"   工作外出
     * "5"   我的日志
     * "6"   项目协同
     * "7"   语音会议
     * "8"   公司通知
     * "9"   月会安排
     * "10   日常会议组
     * "11"  通用分组
     * "12"  资本快报
     * "13"  年会弹幕   年会：预留，暂时没用到
     * "14"  销假申请
     * "15"  出差申请
     * "16"  内刊
     *
     * @param adapter
     * @param view
     * @param position
     */
    @Override


    public void onItemClick(BaseQuickAdapter adapter, View view, int position)
    {
        if (projectEstateAdapter.getItem(position).getTitle().contains("完成"))
        {
            return;
        }
        int bid = projectEstateAdapter.getItem(position).getBid();
        Intent intent;
        switch (projectEstateAdapter.getItem(position).getBtype())
        {
            case 2:
                //请假申请
//                intent = new Intent(this, VacationApprovalDetailActivity.class);
//                intent.putExtra("eid", bid + "");
//                startActivity(intent);

                Bundle bundle3 = new Bundle();
                bundle3.putInt("EID", bid);
                bundle3.putInt("SIGN", 1);
                intent = new Intent(this, VacationDetailActivity.class);
                intent.putExtras(bundle3);
                startActivity(intent);
                break;
            case 8:
                //公司通知
                String tokenString = DisplayUtil.getUserInfo(this, 10);
                String urlString = HttpURLUtil.newInstance().append("comNotice/detail/h.htm?comNoticeId=")
                        .toString() + bid + "&token=" + tokenString;
                Bundle bundle = new Bundle();
                bundle.putString("TITLE", "通知公告");
                bundle.putString("URL", urlString);
                intent = new Intent(this, WebVacationActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case 9:
                //月会安排
                intent = new Intent(this, SuperGMeetingActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putLong("EID", bid);
                bundle2.putBoolean("ADD", false);
                intent.putExtras(bundle2);
                startActivity(intent);
                break;
            case 12:
                //资本快报
                startActivity(new Intent(this, CapitalExpressActivity.class));
                break;
            case 14:
                //销假申请
                startActivity(new Intent(this, LeaveAppListActivity.class));
                break;
            case 15:
                //出差审批
                Bundle bundle4 = new Bundle();
                bundle4.putString("EID", bid + "");
                intent = new Intent(this, BusinessTripDetailActivity.class);
                intent.putExtras(bundle4);
                startActivity(intent);

//                int approveId = mApplyAdapter.getData().get(position).getBusinessId();
//                Intent intent = new Intent(this, ApprovalBusinessTripDetailActivity.class);
//                intent.putExtra("eid", approveId + "");
//                intent.putExtra("applyId", mApplyAdapter.getData().get(position).getApplyId() + "");
//                startActivityForResult(intent, mRequestCode);

                break;
            default:
                break;
        }

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
