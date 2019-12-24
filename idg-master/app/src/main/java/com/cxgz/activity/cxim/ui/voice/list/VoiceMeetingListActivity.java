package com.cxgz.activity.cxim.ui.voice.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chaoxiang.base.utils.DialogUtils;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.ScreenUtils;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.ui.voice.detail.MeetingActivity;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.cxgz.activity.cxim.base.SDSelectContactActivity;
import com.cxgz.activity.cxim.http.ImHttpHelper;
import com.cxgz.activity.cxim.ui.voice.MeetingAddFilter;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.chaoxiang.base.config.Constants;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.utils.ToolUtils;
import com.utils.DialogMeetingUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import newProject.utils.NewCommonDialog;
import yunjing.utils.DisplayUtil;
import yunjing.utils.DividerItemDecoration2;
import yunjing.utils.IntentUtils;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.StatusTipsView;

import static yunjing.utils.IntentUtils.REQUEST_CODE_FOR_IM;

/**
 * Created by selson on 2017/10/21.
 */
public class VoiceMeetingListActivity extends BaseActivity
{
    @Bind(R.id.apply_add_tv)
    TextView applyAddTv;
    @Bind(R.id.first_ll)
    LinearLayout firstLl;
    @Bind(R.id.apply_search_tv)
    TextView applySearchTv;
    @Bind(R.id.second_ll)
    LinearLayout secondLl;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.title_bar)
    CustomNavigatorBar titleBar;
    @Bind(R.id.loading_view)
    StatusTipsView loadingView;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    @Bind(R.id.top_btn_layout)
    RelativeLayout mTopBarLayout;
    @Bind(R.id.space_view)
    View mSpaceView;

    private VoiceMeetingAdapter voiceMeetingAdapter;
    private List<VoiceListBean.DataBean> mDatas = new ArrayList<>();
    private boolean isHide = false;
    private int mPage = 1;
    private int mPageCount = 0;
    private String searchString = "";
    private String isSearch = "";

    @Override
    protected void init()
    {
        ButterKnife.bind(this);
        initView();
        initData();
        getData();
    }

    private void initView()
    {
        //标题 大小默认
        titleBar.setMidText(getResources().getString(R.string.voice_title));
        titleBar.setLeftImageVisible(true);
        titleBar.setRightSecondImageVisible(true);
        titleBar.setRightImageVisible(true);
        titleBar.setRightTextVisible(false);

        //右边布局
        titleBar.setLeftImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });


        titleBar.setRightImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                IntentUtils.startSelectContactActivity(VoiceMeetingListActivity.this);
            }
        });

        titleBar.setRightSecondImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final NewCommonDialog dialog = new NewCommonDialog(VoiceMeetingListActivity.this);
                dialog.setPositiveListener(new NewCommonDialog.DialogPositiveListener()
                {
                    @Override
                    public void onClick(String inputText, String select)
                    {

                    }

                    @Override
                    public void onSearchClick(String content)
                    {
                        searchString = content;
                        refreshData();
                    }
                });
                dialog.initDialogSearch(VoiceMeetingListActivity.this).show();
            }
        });

        applyAddTv.setText(getResources().getString(R.string.voice_list_add));
        applySearchTv.setText(getResources().getString(R.string.voice_list_search));

        loadingView.showLoading();
        loadingView.setOnVisibleChangeListener(new StatusTipsView.OnVisibleChangeListener()
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
        loadingView.setOnRetryListener(new StatusTipsView.OnRetryListener()
        {

            @Override
            public void onRetry()
            {
                loadingView.showLoading();
                refreshData();
            }
        });

        smartRefreshLayout.setReboundInterpolator(new DecelerateInterpolator());
        smartRefreshLayout.setDragRate(0.25f);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                refreshData();
                smartRefreshLayout.finishRefresh(1000);
                smartRefreshLayout.setLoadmoreFinished(false);
                smartRefreshLayout.setEnableLoadmore(true);
            }
        });

        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener()
        {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout)
            {
                if (mPageCount > mPage)
                {
                    ++mPage;
                    getData();
                    smartRefreshLayout.finishLoadmore(1000);
                    smartRefreshLayout.setEnableLoadmore(false);
                } else
                {
                    smartRefreshLayout.finishLoadmore(1000);
                    smartRefreshLayout.setLoadmoreFinished(true);
                }
            }
        });
    }

    private void initData()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            isSearch = bundle.getString(Constants.IS_SEARCH);
            searchString = bundle.getString(Constants.SEARCH, "");
            isHide = bundle.getBoolean("HIDE");
        }
        //初始化搜索有内容
        if (!TextUtils.isEmpty(searchString) || isHide){
            titleBar.setRightImageVisible(false);
            titleBar.setRightSecondImageVisible(false);
        }else{
            titleBar.setRightImageVisible(true);
            titleBar.setRightSecondImageVisible(true);
        }
        setAdapter();
    }

    private void getData()
    {
        ImHttpHelper.postMeetingList(VoiceMeetingListActivity.this, mPage, searchString, isSearch, new SDRequestCallBack
                (VoiceListBean
                        .class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                loadingView.showAccessFail();
                MyToast.showToast(VoiceMeetingListActivity.this, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                VoiceListBean voiceListBean = (VoiceListBean) responseInfo.getResult();
                if (StringUtils.notEmpty(voiceListBean))
                {
                    List<VoiceListBean.DataBean> dataList = voiceListBean.getData();
                    if (dataList != null && dataList.size() > 0)
                    {
                        mPageCount = voiceListBean.getPageCount();
                        mDatas.addAll(dataList);
                        refresh();
                    } else
                    {

                    }
                }
                hideTips();
            }
        });
    }

    private void hideTips()
    {
        String tips = getResources().getString(R.string.no_data);
        if (mDatas.size() > 0)
        {
            loadingView.hide();
        } else
        {
            loadingView.showNoContent(tips);
        }
    }

    private void setAdapter()
    {
        voiceMeetingAdapter = new VoiceMeetingAdapter(VoiceMeetingListActivity.this, mDatas);
        recyclerView.setLayoutManager(new LinearLayoutManager(VoiceMeetingListActivity.this));
        recyclerView.addItemDecoration(new DividerItemDecoration2(VoiceMeetingListActivity.this, LinearLayoutManager
                .VERTICAL, R.drawable.recyclerview_divider,
                ScreenUtils.dp2px(this, 10)));
        recyclerView.setAdapter(voiceMeetingAdapter);
        voiceMeetingAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                Intent intent = new Intent(VoiceMeetingListActivity.this, MeetingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.EID, mDatas.get(position).getEid() + "");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void refreshData()
    {
        clearData();
        mPage = 1;
        getData();
    }

    private void refresh()
    {
        voiceMeetingAdapter.notifyDataSetChanged();
    }

    private void clearData()
    {
        mDatas.clear();
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.apply_list_activity_layout_one;
    }

    @OnClick({R.id.right_image, R.id.right_second_image, R.id.loading_view})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.right_image:
                IntentUtils.startSelectContactActivity(VoiceMeetingListActivity.this);
                break;
            case R.id.right_second_image:

                final NewCommonDialog dialog = new NewCommonDialog(VoiceMeetingListActivity.this);
                dialog.setPositiveListener(new NewCommonDialog.DialogPositiveListener()
                {
                    @Override
                    public void onClick(String inputText, String select)
                    {

                    }

                    @Override
                    public void onSearchClick(String content)
                    {
                        searchString = content;
                        refreshData();
                    }
                });
                dialog.initDialogSearch(VoiceMeetingListActivity.this).show();
                break;
            case R.id.loading_view:
                break;

        }
    }

    List<SDUserEntity> userList = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case REQUEST_CODE_FOR_IM:
                if (resultCode == RESULT_OK && data != null)
                {
                    //返回来的字符串
                    userList = (List<SDUserEntity>) data.getSerializableExtra(
                            SDSelectContactActivity.SELECTED_DATA);// str即为回传的值
                    if (userList != null && userList.size() > 0)
                    {
                        if (userList.size() > Constants.IM_MEETING_NUM)
                        {
                            DialogUtils.getInstance().showDialog(VoiceMeetingListActivity.this, "会议人数最多" + Constants
                                    .IM_GROUP_NUM + "人");
                        } else
                        {
                            showMeetingName();
                        }

                    }
                }
                break;
        }
    }

    private void showMeetingName()
    {
        DialogMeetingUtils.getInstance().showEditSomeThingDialog(VoiceMeetingListActivity.this, new DialogMeetingUtils
                .onTitleClickListener()
        {
            @Override
            public void setTitle(String s)
            {
                addMeeting(s);
            }
        });
    }

    /**
     * 创建语音会议
     */
    private void addMeeting(String name)
    {
        MeetingAddFilter meetingAddFilter = new MeetingAddFilter();
        if (StringUtils.notEmpty(DisplayUtil.getUserInfo(VoiceMeetingListActivity.this, 3)))
            meetingAddFilter.setYgId(DisplayUtil.getUserInfo(VoiceMeetingListActivity.this, 3));

        if (StringUtils.notEmpty(DisplayUtil.getUserInfo(VoiceMeetingListActivity.this, 1)))
            meetingAddFilter.setYgName(DisplayUtil.getUserInfo(VoiceMeetingListActivity.this, 1));

        if (StringUtils.notEmpty(name))
            meetingAddFilter.setTitle(name);

        if (StringUtils.notEmpty(userList))
        {
            meetingAddFilter.setCc(ToolUtils.userListToStringArray(userList));
        }

        ImHttpHelper.postMeetingAdd(VoiceMeetingListActivity.this, SDGson.toJson(meetingAddFilter), new SDRequestCallBack()
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(VoiceMeetingListActivity.this, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                MyToast.showToast(VoiceMeetingListActivity.this, "创建成功！");
                refreshData();
            }
        });
    }

}
