package newProject.company.invested_project;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.config.Constants;
import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.cxgz.activity.cxim.workCircle.utils.GlideCircleTransform;
import com.cxgz.activity.cxim.workCircle.widgets.ExpandTextView;
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
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.invested_project.bean.BeanFollowUp2;
import newProject.company.invested_project.bean.BeanFollowUpProgress;
import newProject.company.project_manager.investment_project.adapter.PopuVoiceListAdapter;
import newProject.company.project_manager.investment_project.voice.VoiceListBean;
import newProject.company.project_manager.investment_project.voice.VoiceUtils;
import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.StatusTipsView;


/**
 * 跟踪进展
 */
public class ActivityFollowUp extends BaseActivity
{
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    @Bind(R.id.loading_view)
    StatusTipsView loadingView;
    @Bind(R.id.title_bar)
    CustomNavigatorBar titleBar;

    private ApplyAdapter mApplyAdapter;
    private String mEid = "";
    private List<BeanFollowUpProgress> mDataLists = new ArrayList<>();
    private int pageNo = 1;
    private int pageSize = 10;

    public void reFresh()
    {
        getNetData();
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_follow_up_main;
    }

    @Override
    protected void init()
    {
        ButterKnife.bind(this);
        if (getIntent().getExtras() != null)
        {
            Bundle bundle = getIntent().getExtras();
            mEid = bundle.getString(Constants.PROJECT_EID, "");
        }

        titleBar.setMidText("跟踪进展");
        titleBar.setLeftImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        initRefresh();
        initRV();

        if (!DisplayUtil.hasNetwork(this))
        {
            loadingView.showAccessFail();
        } else
        {
            getNetData();
        }
    }

    public void initRefresh()
    {
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                pageNo = 1;
                if (DisplayUtil.hasNetwork(ActivityFollowUp.this))
                {
                    getNetData();
                    smartRefreshLayout.finishRefresh(1000);
                    smartRefreshLayout.setLoadmoreFinished(false);
                    smartRefreshLayout.setEnableLoadmore(true);
                } else
                {
                    SDToast.showShort("请检查网络连接");
                    smartRefreshLayout.finishRefresh(1000);
                }

            }
        });

        smartRefreshLayout.setEnableLoadmoreWhenContentNotFull(true);
        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener()
        {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(ActivityFollowUp.this))
                {
//                    if (projectLibAdapter.getData().size() >= mBackListSize)
//                    {
//                        smartRefreshLayout.finishLoadmore(1000);
//                        SDToast.showShort("没有更多数据了");
//                    } else
//                    {
                    pageNo = pageNo + 1;
                    getNetData();
                    smartRefreshLayout.finishLoadmore(1000);
//                    }
                } else
                {
                    SDToast.showShort("请检查网络连接");
                    smartRefreshLayout.finishLoadmore(1000);
                }
            }
        });

        loadingView.showLoading();
        loadingView.setOnVisibleChangeListener(new StatusTipsView.OnVisibleChangeListener()
        {

            @Override
            public void onVisibleChanged(boolean visible)
            {
                if (recyclerview != null)
                {
                    recyclerview.setVisibility(visible ? View.GONE : View.VISIBLE);
                }
            }
        });

        loadingView.setOnRetryListener(new StatusTipsView.OnRetryListener()
        {
            @Override
            public void onRetry()
            {
                pageNo = 1;
                loadingView.showLoading();
                getNetData();
            }
        });

        if (!DisplayUtil.hasNetwork(this))
        {
            loadingView.showAccessFail();
        }
    }

    private void getNetData()
    {
        ListHttpHelper.postFollowUp(ActivityFollowUp.this, mEid, pageNo + "", pageSize + "", new SDRequestCallBack
                (BeanFollowUp2.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                loadingView.showNoContent("暂无数据");
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                BeanFollowUp2 listBean = (BeanFollowUp2) responseInfo.getResult();
                mDataLists.clear();
                if (pageNo == 1)
                {
                    if (null != listBean.getData() && StringUtils.notEmpty(listBean.getData().getData()) && listBean.getData()
                            .getData().size() > 0)
                    {
                        mDataLists.addAll(listBean.getData().getData());
                        loadingView.hide();
                        setData();
                    } else
                    {
                        loadingView.showNoContent("暂无数据");
                        setData();
                    }
                } else
                {
                    if (StringUtils.notEmpty(listBean.getData().getData()) || listBean.getData().getData().size() > 0)
                        mDataLists.addAll(listBean.getData().getData());
                    setData();
                }
            }
        });
    }

    private void setData()
    {
        Iterator<BeanFollowUpProgress> iterator = mDataLists.iterator();
        while (iterator.hasNext())
        {
            BeanFollowUpProgress next = iterator.next();
            if (next != null)
            {
                if (StringUtils.notEmpty(next.getNoteType()) && next.getNoteType().equalsIgnoreCase("text"))
                {
                    next.setItemType(1);
                } else if (StringUtils.notEmpty(next.getNoteType()) && next.getNoteType().equalsIgnoreCase("audio"))
                {
                    next.setItemType(2);
                } else
                {
                    next.setItemType(1);
                }
            }
        }
        mApplyAdapter.notifyDataSetChanged();
    }

    private void initRV()
    {
        mApplyAdapter = new ApplyAdapter(mDataLists);
        recyclerview.setAdapter(mApplyAdapter);

        recyclerview.setLayoutManager(new LinearLayoutManager(ActivityFollowUp.this));
        mApplyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {

            }
        });
        mApplyAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener()
        {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position)
            {
                switch (view.getId())
                {
                    case R.id.ll_voice:
                        if (mDataLists.get(position).getNote().contains(","))
                        {
                            initVoiceListPopupWindow(view, mDataLists.get(position));
                        } else
                        {
                            ImageView ivVoice = (ImageView) view.findViewById(R.id.iv_voice);
                            ivVoice.setBackgroundColor(0xffffffff);
                            ivVoice.setImageResource(R.drawable.play_annex_voice);
                            VoiceUtils.getInstance().getVoiceFromNet(ActivityFollowUp.this, mDataLists.get(position)
                                    .getNote(), ivVoice);
                        }
                        break;
                }
            }
        });
    }

    private void initVoiceListPopupWindow(View view, BeanFollowUpProgress bean)
    {
        String[] urls = bean.getNote().split(",");
        String[] times = bean.getAudioTime().split(",");
        List<VoiceListBean> voiceListBeen = new ArrayList<>();
        for (int i = 0; i < urls.length; i++)
        {
            voiceListBeen.add(new VoiceListBean(Integer.parseInt(times[i]), urls[i]));
        }

        View contentView = LayoutInflater.from(ActivityFollowUp.this).inflate(R.layout.popupwindow_voice_list, null);
        RecyclerView voiceRecyclerView = (RecyclerView) contentView.findViewById(R.id.recycler_view);
        voiceRecyclerView.setLayoutManager(new LinearLayoutManager(ActivityFollowUp.this));
        PopuVoiceListAdapter adapter = new PopuVoiceListAdapter(voiceListBeen);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener()
        {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position)
            {
                switch (view.getId())
                {
                    case R.id.iv_voice:
                        VoiceUtils.getInstance().getVoiceFromNet(ActivityFollowUp.this, ((VoiceListBean) adapter.getData().get
                                (position)).getUrl(), (ImageView) view);
                        break;
                }
            }
        });
        voiceRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams
                .WRAP_CONTENT, true);
        setBackgroundAlpha(0.5f);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener()
        {
            @Override
            public void onDismiss()
            {
                setBackgroundAlpha(1f);
            }
        });
    }

    public void setBackgroundAlpha(float alpha)
    {
        WindowManager.LayoutParams lp = ActivityFollowUp.this.getWindow().getAttributes();
        lp.alpha = alpha;
        ActivityFollowUp.this.getWindow().setAttributes(lp);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private class ApplyAdapter extends BaseMultiItemQuickAdapter<BeanFollowUpProgress, BaseViewHolder>
    {
        public static final int TEXT = 1;
        public static final int AUDIO = 2;

        public ApplyAdapter(List<BeanFollowUpProgress> data)
        {
            super(data);
            addItemType(TEXT, R.layout.item_activity_follow_up);
            addItemType(AUDIO, R.layout.item_activity_follow_up2);
        }

        @Override
        protected void convert(BaseViewHolder helper, BeanFollowUpProgress item)
        {
            if (helper.getLayoutPosition() == 0)
            {
                helper.getView(R.id.ll_line).setVisibility(View.GONE);
            } else
            {
                helper.getView(R.id.ll_line).setVisibility(View.VISIBLE);
            }
            Glide.with(ActivityFollowUp.this)
                    .load(item.getCreateByPhoto())
                    .placeholder(R.mipmap.temp_user_head)
                    .error(R.mipmap.temp_user_head)
                    .crossFade()
                    .transform(new GlideCircleTransform(ActivityFollowUp.this))
                    .into((ImageView) helper.getView(R.id.tv_img));

            if (StringUtils.notEmpty(item.getCreateByName()))
            {
                helper.setText(R.id.tv_name, item.getCreateByName() + "");
            } else
            {
                helper.setText(R.id.tv_name, "");
            }
            if (StringUtils.notEmpty(item.getCreateDate()))
            {
                helper.setText(R.id.tv_time, DateUtils.getDate("yyyy-MM-dd", item.getCreateDate()));
            } else
            {
                helper.setText(R.id.tv_time, "");
            }
            switch (helper.getItemViewType())
            {
                case TEXT:
                    if (StringUtils.notEmpty(item.getNote()))
                    {
                        ExpandTextView expandTextView = (ExpandTextView) helper.getView(R.id.tv_content);
                        expandTextView.setText(item.getNote() + "");
                    } else
                    {
                        ExpandTextView expandTextView = (ExpandTextView) helper.getView(R.id.tv_content);
                        expandTextView.setText("");
                    }
                    break;

                case AUDIO:

                    if (StringUtils.notEmpty(item.getAudioTime()))
                    {
                        helper.setText(R.id.tv_name, item.getAudioTime() + "″");
                    } else
                    {
                        helper.setText(R.id.tv_name, "");
                    }

                    helper.addOnClickListener(R.id.ll_voice);
                    break;
            }
        }
    }
}
