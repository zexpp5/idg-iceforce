package newProject.company.project_manager.investment_project;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cxgz.activity.cxim.base.BaseActivity;
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
import newProject.company.project_manager.investment_project.adapter.FollowOnDetailMultiAdapter;
import newProject.company.project_manager.investment_project.adapter.PopuVoiceListAdapter;
import newProject.company.project_manager.investment_project.adapter.TrackProgressIceForceMultiAdapter;
import newProject.company.project_manager.investment_project.bean.FollowProjectBaseBean;
import newProject.company.project_manager.investment_project.bean.FollowProjectListBean;
import newProject.company.project_manager.investment_project.bean.FollowProjectMultiBean;
import newProject.company.project_manager.investment_project.voice.VoiceListBean;
import newProject.company.project_manager.investment_project.voice.VoiceUtils;
import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.StatusTipsView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.utils.SPUtils.USER_ACCOUNT;

/**
 * Created by zsz on 2019/5/6.
 */

public class FollowOnDetailActivity extends BaseActivity {
    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    @Bind(R.id.loading_view)
    StatusTipsView mTips;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;

    private int mPage = 0;
    private int mBackListSize = 0;

    FollowOnDetailMultiAdapter adapter;

    List<FollowProjectMultiBean> followProjectMultiBeen = new ArrayList<>();
    List<FollowProjectMultiBean> CMultiBeen = new ArrayList<>();
    List<FollowProjectMultiBean> HMultiBeen = new ArrayList<>();

    @Override
    protected void init() {
        ButterKnife.bind(this);
        mNavigatorBar.setMidText(getIntent().getStringExtra("projName"));
        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(false);
        mNavigatorBar.setRightSecondImageVisible(false);
        mNavigatorBar.setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FollowOnDetailMultiAdapter(followProjectMultiBeen);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.ll_voice){
                   /* ImageView ivVoice = (ImageView) view.findViewById(R.id.iv_voice);
                    ivVoice.setBackgroundColor(0xffffffff);
                    ivVoice.setImageResource(R.drawable.play_annex_voice);
                    VoiceUtils.getInstance().getVoiceFromNet(FollowOnDetailActivity.this,followProjectMultiBeen.get(position).getData().getUrl(),ivVoice);*/
                    if (followProjectMultiBeen.get(position).getData().getUrl().contains(","))
                    {
                        initVoiceListPopupWindow(view, followProjectMultiBeen.get(position).getData());
                    } else
                    {
                        ImageView ivVoice = (ImageView) view.findViewById(R.id.iv_voice);
                        ivVoice.setBackgroundColor(0xffffffff);
                        ivVoice.setImageResource(R.drawable.play_annex_voice);
                        VoiceUtils.getInstance().getVoiceFromNet(FollowOnDetailActivity.this, followProjectMultiBeen.get(position)
                                .getData().getUrl(), ivVoice);
                    }
                }
            }
        });
        recyclerView.setAdapter(adapter);
        initRefresh();
        mTips.showLoading();
        mTips.setOnVisibleChangeListener(new StatusTipsView.OnVisibleChangeListener()
        {

            @Override
            public void onVisibleChanged(boolean visible)
            {
                if (recyclerView != null)
                {
                    recyclerView.setVisibility(visible ? GONE : VISIBLE);
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


    }

    @Override
    protected void onStart() {
        super.onStart();
        mPage = 0;
        mBackListSize = 0;
        getData();
    }

    private void initRefresh()
    {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(FollowOnDetailActivity.this))
                {
                    mPage = 0;
                    mRefreshLayout.finishRefresh(1000);
                    mRefreshLayout.setLoadmoreFinished(false);
                    mRefreshLayout.setEnableLoadmore(true);
                    getData();
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
                if (DisplayUtil.hasNetwork(FollowOnDetailActivity.this))
                {
                    if (adapter.getData().size() >= mBackListSize)
                    {
                        mRefreshLayout.finishLoadmore(1000);
                        SDToast.showShort("没有更多数据了");
                    } else
                    {
                        mPage = mPage + 1;
                        getData();
                        mRefreshLayout.finishLoadmore(1000);
                    }
                } else
                {
                    SDToast.showShort("请检查网络连接");
                    mRefreshLayout.finishLoadmore(1000);
                }

            }
        });
    }

    public void getData(){
        ListHttpHelper.getFollowOnDetailList(this, SPUtils.get(this, USER_ACCOUNT, "").toString(), mPage + "", "10", "0",getIntent().getStringExtra("projId"), new SDRequestCallBack(FollowProjectListBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                FollowProjectListBean bean = (FollowProjectListBean) responseInfo.getResult();
                mBackListSize = bean.getData().getTotal();
                if (mPage == 0)
                {
                    CMultiBeen.clear();
                    /*if (bean.getData().getData().size() > 0)
                    {
                        mTips.hide();
                    } else
                    {
                        mTips.showNoContent("暂无数据");
                    }*/
                }
                for (int i = 0; i < bean.getData().getData().size(); i++){
                    int itemType = 0;
                    if(bean.getData().getData().get(i).getContentType().equals("TEXT")){
                        if (bean.getData().getData().get(i).getFollowType().equals("UPDATE_STATE")){
                            itemType = 2;
                        }else{
                            itemType = 1;
                        }
                    }else {
                        //音频
                        itemType = 3;
                    }
                    CMultiBeen.add(new FollowProjectMultiBean(itemType,bean.getData().getData().get(i)));
                }
                getHistoryData();
            }
        });
    }

    public void getHistoryData(){
        ListHttpHelper.getFollowOnDetailList(this, SPUtils.get(this, USER_ACCOUNT, "").toString(), mPage + "", "10", "1", getIntent().getStringExtra("projId"),new SDRequestCallBack(FollowProjectListBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                FollowProjectListBean bean = (FollowProjectListBean) responseInfo.getResult();
                mBackListSize = bean.getData().getTotal();
                if (mPage == 0)
                {
                    HMultiBeen.clear();
                    /*if (bean.getData().getData().size() > 0)
                    {
                        mTips.hide();
                    } else
                    {
                        mTips.showNoContent("暂无数据");
                    }*/
                }
                for (int i = 0; i < bean.getData().getData().size(); i++){
                    int itemType = 0;
                    if(bean.getData().getData().get(i).getContentType().equals("TEXT")){
                        if (bean.getData().getData().get(i).getFollowType().equals("UPDATE_STATE")){
                            itemType = 2;
                        }else{
                            itemType = 1;
                        }
                    }else {
                        //音频
                        itemType = 3;
                    }
                    HMultiBeen.add(new FollowProjectMultiBean(itemType,bean.getData().getData().get(i)));
                }
                addAllData();
            }
        });
    }

    //合并最新和历史数据
    public void addAllData(){
        mTips.hide();
        followProjectMultiBeen.clear();
        FollowProjectMultiBean bean = new FollowProjectMultiBean(0,new FollowProjectBaseBean());
        bean.setTitle("-------- 最新更新 ("+getIntent().getStringExtra("date")+") --------");
        followProjectMultiBeen.add(bean);
        followProjectMultiBeen.addAll(CMultiBeen);
        FollowProjectMultiBean bean2 = new FollowProjectMultiBean(0,new FollowProjectBaseBean());
        bean2.setTitle("-------- 历史更新 --------");
        followProjectMultiBeen.add(bean2);
        followProjectMultiBeen.addAll(HMultiBeen);
        adapter.notifyDataSetChanged();
    }

    private void initVoiceListPopupWindow(View view, FollowProjectBaseBean bean)
    {
        String[] urls = bean.getUrl().split(",");
        String[] times = bean.getAudioTime().split(",");
        List<VoiceListBean> voiceListBeen = new ArrayList<>();
        for (int i = 0; i < urls.length; i++)
        {
            voiceListBeen.add(new VoiceListBean(Integer.parseInt(times[i]), urls[i]));
        }

        View contentView = LayoutInflater.from(this).inflate(R.layout.popupwindow_voice_list, null);
        RecyclerView voiceRecyclerView = (RecyclerView) contentView.findViewById(R.id.recycler_view);
        voiceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        PopuVoiceListAdapter adapter = new PopuVoiceListAdapter(voiceListBeen);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener()
        {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position)
            {
                switch (view.getId())
                {
                    case R.id.iv_voice:
                        VoiceUtils.getInstance().getVoiceFromNet(FollowOnDetailActivity.this, ((VoiceListBean) adapter.getData().get
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
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().setAttributes(lp);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_follow_on_detail;
    }
}
