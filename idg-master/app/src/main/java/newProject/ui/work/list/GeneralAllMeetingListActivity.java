package newProject.ui.work.list;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.MonPickerDialog;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.ScreenUtils;
import com.chaoxiang.base.utils.YearPickerDialog;
import com.cxgz.activity.cx.base.BaseActivity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.ui.http.BasicDataHttpHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import newProject.ui.work.GMDataList;
import newProject.ui.work.GMeetingDataBean;
import newProject.ui.work.GeneralMeetingActivity;
import newProject.ui.work.SuperGMeetingActivity;
import tablayout.view.textview.FontTextView;
import yunjing.utils.DividerItemDecoration2;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.StatusTipsView;

import static com.injoy.idg.R.id.recycler_view;


/**
 * Created by selson on 2017/11/27.
 * 日常会议-我的会议
 */
public class GeneralAllMeetingListActivity extends BaseActivity
{
    @Bind(R.id.title_bar)
    CustomNavigatorBar titleBar;
    @Bind(R.id.tv_year)
    FontTextView tvYear;
    @Bind(R.id.rl_year)
    RelativeLayout rlYear;
    @Bind(R.id.rl_month)
    RelativeLayout rlMonth;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.tv_month)
    FontTextView tvMonth;

    @Bind(R.id.swipe_container)
    SwipeRefreshLayout swipe_container;

    @Bind(R.id.loading_view)
    StatusTipsView mTips;

    private int tmpYear = 0;
    private int tmpMonth = 0;

    private boolean isCanScroll = true;

    private GMeetingListAdapter gMeetingAdapter;
    private List<GMeetingDataBean> mDatas = new ArrayList<>();

    private int mType = 0; //默认全部 0位全部，1位我的。---会议
    public static String TYPE = "mType";

    LinearLayoutManager linearLayoutManager;

    //填充详情
    private void setAdapter()
    {
        gMeetingAdapter = new GMeetingListAdapter(GeneralAllMeetingListActivity.this, mDatas);
        linearLayoutManager = new LinearLayoutManager(GeneralAllMeetingListActivity.this);
        recyclerView.addItemDecoration(new DividerItemDecoration2(GeneralAllMeetingListActivity.this, LinearLayoutManager
                .VERTICAL, R.drawable.recyclerview_divider,
                ScreenUtils.dp2px(this, 1)));
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(gMeetingAdapter);
        gMeetingAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {
//                SuperGMeetingActivity
                Intent intent = new Intent(GeneralAllMeetingListActivity.this, SuperGMeetingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("EID", mDatas.get(position).getEid());
                bundle.putBoolean("ADD", false);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
                setIsBoolean(isCanScroll);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);

                if (dy < 0) //向下滚动
                {
                    int visibleItemCount = linearLayoutManager.getChildCount();
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();
                    if (isCanScroll && pastVisiblesItems == 0)
                    {
                        setIsBoolean(isCanScroll);
                    }
                }
            }
        });
    }

    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener()
    {
        @Override
        public void onRefresh()
        {
            //加入已完成的-区分结束的
            setIsBoolean(false);
            getData(0, tmpYear, tmpMonth);
        }
    };

    private void setIsBoolean(boolean isCan)
    {
        if (isCan)
        {
            swipe_container.setEnabled(true);
            isCanScroll = isCan;
        } else
        {
            swipe_container.setEnabled(false);
            isCanScroll = isCan;
        }

    }

    private void reFresh()
    {
        gMeetingAdapter.notifyDataSetChanged();
    }

    @Override
    protected void init()
    {
        ButterKnife.bind(this);

        if (this.getIntent().getExtras() != null)
        {
            Bundle bundle = this.getIntent().getExtras();
            mType = bundle.getInt(TYPE);
        }
        if (mType == 0)
        {
            titleBar.setMidText(getResources().getString(R.string.im_business_meeting_all));
        } else if (mType == 1)
        {
            titleBar.setMidText(getResources().getString(R.string.im_business_meeting_mine));
        }

        titleBar.setLeftImageVisible(true);
        titleBar.setLeftImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        mTips.showLoading();
        mTips.setOnVisibleChangeListener(new StatusTipsView.OnVisibleChangeListener()
        {

            @Override
            public void onVisibleChanged(boolean visible)
            {
                if (recyclerView != null)
                {
                    recyclerView.setVisibility(visible ? View.GONE : View.VISIBLE);
                }
            }
        });

        mTips.setOnRetryListener(new StatusTipsView.OnRetryListener()
        {
            @Override
            public void onRetry()
            {
                mTips.showLoading();
                getData(0, tmpYear, tmpMonth);
            }
        });

        swipe_container.setOnRefreshListener(onRefreshListener);

        setAdapter();

        tmpYear = DateUtils.getNowDate(Calendar.YEAR);
        tmpMonth = DateUtils.getNowDate(Calendar.MONTH);

        tvYear.setText(tmpYear + "年");
        tvMonth.setText(tmpMonth + "月");

        getData(0, tmpYear, tmpMonth);
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_general_all_meeting_list;
    }

    private void getData(final int type, int year, int month)
    {
        String monthString = "";
        if (month < 10)
        {
            monthString = "0" + month;
        } else
        {
            monthString = month + "";
        }
        String dateString = "";
        String typeString = "";
        if (type == 0)
        {
            typeString = "month";
            dateString = year + "-" + monthString;
        }
        boolean isALL = false;

        if (mType == 0)
        {
            isALL = true;
        }

        BasicDataHttpHelper.postGMeetingMonthList(GeneralAllMeetingListActivity.this, typeString, dateString, isALL, new
                SDRequestCallBack()
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        swipe_container.setRefreshing(false);
                        setIsBoolean(true);
                        MyToast.showToast(GeneralAllMeetingListActivity.this, msg);
                        setList(null);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        swipe_container.setRefreshing(false);
//                        List<GMeetingDataBean> dataList = new ArrayList<GMeetingDataBean>();
                        GMDataList gmDataList = GMDataList.parse(responseInfo.getResult().toString());
                        if (gmDataList != null)
                        {
                            mDatas.clear();
                            if (gmDataList.getData() != null && gmDataList.getData().size() > 0)
                            {
                                mDatas.addAll(gmDataList.getData());
                            }
                            setIsBoolean(isCanScroll);
                            removeFinishData(isCanScroll);

                            reFresh();
                        } else
                        {
                            setIsBoolean(true);
                            mDatas.clear();
                            reFresh();
                        }

                        if (mDatas.size() > 0)
                        {
                            mTips.hide();
                        } else
                        {
                            mTips.showNoMeeting("暂无会议安排");
                        }
                    }
                });
    }

    private void removeFinishData(boolean isCanScroll)
    {
        if (isCanScroll)
        {
            long nowTime = System.currentTimeMillis();
            Iterator<GMeetingDataBean> iterator = mDatas.iterator();
            while (iterator.hasNext())
            {
                GMeetingDataBean gMeetingDataBean = iterator.next();
                long endTime = DateUtils.dateToMillis("yyyy-MM-dd HH:mm:ss", gMeetingDataBean.getEndTime());
                if (nowTime > endTime)
                {
                    iterator.remove();
                }
            }
        } else
        {

        }
    }

    private void setList(List<GMeetingDataBean> gMeetingDataBeanList)
    {

    }

    @OnClick({R.id.rl_year, R.id.rl_month})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.rl_year:
                final Calendar calendar = Calendar.getInstance();
                new YearPickerDialog(GeneralAllMeetingListActivity.this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog
                        .OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        tmpYear = year;
                        tvYear.setText(tmpYear + "年");
                        getData(0, tmpYear, tmpMonth);

//                        if (tmpYear == DateUtils.getNowDate(Calendar.YEAR) && tmpMonth == DateUtils.getNowDate(Calendar.MONTH))
//                        {
//                            setIsBoolean(true);
//                        } else
//                        {
//                            setIsBoolean(false);
//                        }
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show();
                break;
            case R.id.rl_month:
                final Calendar calendar2 = Calendar.getInstance();
                new MonPickerDialog(GeneralAllMeetingListActivity.this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog
                        .OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        tmpMonth = monthOfYear + 1;
                        tvMonth.setText(tmpMonth + "月");
                        getData(0, tmpYear, tmpMonth);
//                        if (tmpYear == DateUtils.getNowDate(Calendar.YEAR) && tmpMonth == DateUtils.getNowDate(Calendar.MONTH))
//                        {
//                            setIsBoolean(true);
//                        } else
//                        {
//                            setIsBoolean(false);
//                        }
                    }
                }, calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DATE)).show();
                break;
        }
    }

}
