package newProject.company.expense;

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
import com.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.expense.bean.ExpenseListBean;
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
 * 报销审批
 */
public class ExpenseListActivity extends Activity
{

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
    private ExpenseAdapter mExpenseAdapter;
    private int mPage = 1;
    private List<ExpenseListBean.DataBean> mList = new ArrayList<>();
    private int mBackListSize = 0;
    private String mSearch = "";
    public static int REFRESHCODE = 1055;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_list_activity_layout);
        ButterKnife.bind(this);
        initRefresh();
        getNetData(mPage + "", mSearch, true);
    }

    public void initRefresh()
    {
        titleBar.setMidText("报销审批");
        titleBar.setLeftImageOnClickListener(mOnClickListener);
        titleBar.setRightImageVisible(false);
        titleBar.setRightSecondImageVisible(false);
        titleBar.setRightTextVisible(false);
        titleBar.setRightSecondImageOnClickListener(mOnClickListener);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(ExpenseListActivity.this))
                {
                    mPage = 1;
                    mList.clear();
                    mExpenseAdapter.getData().clear();
                    getNetData(mPage + "", mSearch, true);
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
        mRefreshLayout.setEnableLoadmoreWhenContentNotFull(true);
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener()
        {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(ExpenseListActivity.this))
                {
                    if (mExpenseAdapter.getData().size() >= mBackListSize)
                    {
                        mRefreshLayout.finishLoadmore(1000);
                        SDToast.showShort("没有更多数据了");
                    } else
                    {
                        mPage = mPage + 1;
                        getNetData(mPage + "", mSearch, false);
                        mRefreshLayout.finishLoadmore(1000);
                    }
                } else
                {
                    SDToast.showShort("请检查网络连接");
                    mRefreshLayout.finishLoadmore(1000);
                }

            }
        });
        setData();

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
                getNetData(mPage + "", mSearch, true);
            }
        });

        if (!DisplayUtil.hasNetwork(ExpenseListActivity.this))
        {
            mLoadingView.showAccessFail();
        }
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (v == titleBar.getLeftImageView())
            {
                finish();
            } else if (v == titleBar.getRightImage())
            {
                final NewCommonDialog dialog = new NewCommonDialog(ExpenseListActivity.this);
                dialog.setPositiveListener(new NewCommonDialog.DialogPositiveListener()
                {
                    @Override
                    public void onClick(String inputText, String select)
                    {

                    }

                    @Override
                    public void onSearchClick(String content)
                    {
                        mSearch = content;
                        refresh();
                    }
                });
                dialog.initDialogSearch(ExpenseListActivity.this).show();
            }
        }
    };

    public void refresh()
    {
        if (mRecyclerview != null)
        {
            mRecyclerview.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    mPage = 1;
                    mList.clear();
                    mExpenseAdapter.getData().clear();
                    getNetData(mPage + "", mSearch, true);
                }
            }, 500);
        }
    }

    private void getNetData(String page, String s_title, final boolean isRefresh)
    {
        ListHttpHelper.getExpenseList(ExpenseListActivity.this, page, s_title, new SDRequestCallBack(ExpenseListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                mLoadingView.showAccessFail();
                ToastUtils.show(ExpenseListActivity.this, msg);

            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                boolean hasData = false;
                ExpenseListBean listBean = (ExpenseListBean) responseInfo.getResult();
                if (null != listBean && null != listBean.getData() && !listBean.getData().isEmpty())
                {
                    mList = listBean.getData();
                    hasData = true;
                    if (isRefresh)
                    {
                        mExpenseAdapter.setNewData(mList);
                    } else
                    {
                        mExpenseAdapter.addData(mList);
                        mExpenseAdapter.notifyDataSetChanged();
                    }
                    mBackListSize = listBean.getTotal();
                } else
                {
                    hasData = false;
                    mList.clear();
                    mExpenseAdapter.notifyDataSetChanged();
                }
                if (hasData)
                {
                    mLoadingView.hide();
                } else
                {
                    mLoadingView.showNoContent(getResources().getString(R.string.had_no_data));
                }
            }
        });
    }

    private void setData()
    {
        mRecyclerview.setLayoutManager(new LinearLayoutManager(ExpenseListActivity.this));
        mExpenseAdapter = new ExpenseAdapter(R.layout.expense_item_layout, mList);
        mRecyclerview.setAdapter(mExpenseAdapter);
        mExpenseAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                ExpenseListBean.DataBean bean = (ExpenseListBean.DataBean) adapter.getData().get(position);

                if (adapter.getData().size() > 0 && bean != null)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("EID", bean.getObjectId());
                    bundle.putString("BID", bean.getSubObjectId());
                    bundle.putBoolean("YES", true);
                    Intent intent = new Intent();
                    if (bean.getItemType().equals("Reimbursement") || bean.getItemType().equals("EmployeeVoucher") || bean
                            .getItemType().equals("GeneralVoucher") || bean.getItemType().equals("SpecialVoucher") || bean
                            .getItemType().equals("AssetsVoucher"))
                    {
                        //报销单
                        bundle.putString("expenseType", bean.getItemType());
                        intent.setClass(ExpenseListActivity.this, ExpenseBillActivity.class);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, REFRESHCODE);
                    } else if (bean.getItemType().equals("Payment") || bean.getItemType().equals("PaymentVoucher"))
                    {
                        //付款单
                        bundle.putString("expenseType", bean.getItemType());
                        intent.setClass(ExpenseListActivity.this, PayBillActivity.class);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, REFRESHCODE);
                    } else if (bean.getItemType().equals("Reim2Pay"))
                    {
                        //报销付款单
                        bundle.putString("expenseType", bean.getItemType());
                        intent.setClass(ExpenseListActivity.this, ExpenseAndPayActivity.class);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, REFRESHCODE);
                    } else if (bean.getItemType().equals("Loan") || bean.getItemType().equals("EmployeeLoan"))
                    {
                        //借款单
                        bundle.putString("expenseType", bean.getItemType());
                        intent.setClass(ExpenseListActivity.this, LoanBillActivity.class);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, REFRESHCODE);
                    } else if ("DDFee".equals(bean.getItemType()))
                    {
                        bundle.putString("expenseType", bean.getItemType());
                        intent.setClass(ExpenseListActivity.this, DDFeeActivity.class);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, REFRESHCODE);
                    }

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REFRESHCODE)
        {
            if (resultCode == RESULT_OK)
            {
                refresh();
            }
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private class ExpenseAdapter extends BaseQuickAdapter<ExpenseListBean.DataBean, BaseViewHolder>
    {


        public ExpenseAdapter(@LayoutRes int layoutResId, @Nullable List<ExpenseListBean.DataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, ExpenseListBean.DataBean item)
        {
            holder.getView(R.id.one_content).setVisibility(VISIBLE);
            holder.setText(R.id.one_content, "未审批");
            holder.setTextColor(R.id.one_content, 0xffcd7555);
            if (item.getItemTypeName() != null)
            {
                holder.setText(R.id.one_title, item.getItemTypeName());
            } else
            {
                holder.setText(R.id.one_title, "");
            }
            if (item.getCreate() != null)
            {
                holder.setText(R.id.two_content, item.getCreate());
            } else
            {
                holder.setText(R.id.two_content, "");
            }
            if (item.getAmount() != null)
            {
                holder.setText(R.id.three_content, item.getAmount());
            } else
            {
                holder.setText(R.id.three_content, "");
            }
            if (item.getCreateDate() != null)
            {
                holder.setText(R.id.four_content, item.getCreateDate());
            } else
            {
                holder.setText(R.id.four_content, "");
            }
        }
    }


}
