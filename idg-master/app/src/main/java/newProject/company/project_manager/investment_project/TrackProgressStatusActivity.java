package newProject.company.project_manager.investment_project;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

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
import butterknife.OnClick;
import newProject.api.ListHttpHelper;
import newProject.company.project_manager.investment_project.adapter.PopuVoiceListAdapter;
import newProject.company.project_manager.investment_project.adapter.TrackProgressMultiAdapter;
import newProject.company.project_manager.investment_project.bean.FollowProjectByIdBaseBean;
import newProject.company.project_manager.investment_project.bean.FollowProjectByIdListBean;
import newProject.company.project_manager.investment_project.bean.FollowProjectByIdMultiBean;
import newProject.company.project_manager.investment_project.bean.IDGBaseBean;
import newProject.company.project_manager.investment_project.bean.PotentialProjectsDetailBean;
import newProject.company.project_manager.investment_project.voice.VoiceListBean;
import newProject.company.project_manager.investment_project.voice.VoiceUtils;
import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;

import static com.utils.SPUtils.USER_ACCOUNT;

/**
 * Created by zsz on 2019/5/28.
 */

public class TrackProgressStatusActivity extends BaseActivity {
    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    @Bind(R.id.tv_desc)
    TextView tvDesc;
    @Bind(R.id.tv_text)
    TextView tvText;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.btn_ok)
    Button btnOk;

    private int mPage = 0;
    private int mBackListSize = 0;

    TrackProgressMultiAdapter adapter;

    List<FollowProjectByIdMultiBean> followProjectByIdMultiBeen = new ArrayList<>();


    @Override
    protected int getContentLayout() {
        return R.layout.activity_track_progress_status;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(false);
        mNavigatorBar.setRightSecondImageVisible(false);
        mNavigatorBar.setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mNavigatorBar.setMidText(getIntent().getStringExtra("projName"));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TrackProgressMultiAdapter(followProjectByIdMultiBeen);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.ll_voice){
                    /*ImageView ivVoice = (ImageView) view.findViewById(R.id.iv_voice);
                    ivVoice.setBackgroundColor(0xffffffff);
                    ivVoice.setImageResource(R.drawable.play_annex_voice);
                    VoiceUtils.getInstance().getVoiceFromNet(TrackProgressStatusActivity.this,followProjectByIdMultiBeen.get(position).getData().getNote(),ivVoice);*/
                    if (followProjectByIdMultiBeen.get(position).getData().getNote().contains(","))
                    {
                        initVoiceListPopupWindow(view, followProjectByIdMultiBeen.get(position).getData());
                    } else
                    {
                        ImageView ivVoice = (ImageView) view.findViewById(R.id.iv_voice);
                        ivVoice.setBackgroundColor(0xffffffff);
                        ivVoice.setImageResource(R.drawable.play_annex_voice);
                        VoiceUtils.getInstance().getVoiceFromNet(TrackProgressStatusActivity.this, followProjectByIdMultiBeen.get(position)
                                .getData().getNote(), ivVoice);
                    }
                }
            }
        });
        recyclerView.setAdapter(adapter);
        initRefresh();
        getDescData();
        getData();

    }

    private void initRefresh()
    {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(TrackProgressStatusActivity.this))
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
                if (DisplayUtil.hasNetwork(TrackProgressStatusActivity.this))
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

    public void getDescData() {
        ListHttpHelper.getPotentialProjectsDetailList(this, SPUtils.get(this, USER_ACCOUNT, "").toString(), getIntent().getStringExtra("projId"), new SDRequestCallBack(PotentialProjectsDetailBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showLong(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                PotentialProjectsDetailBean bean = (PotentialProjectsDetailBean) responseInfo.getResult();
                if (bean.getData().getData().getBaseInfoMap() != null) {
                    tvDesc.setText(bean.getData().getData().getBaseInfoMap().getZhDesc());
                }else {
                    SDToast.showShort(bean.getData().getReturnMessage());
                }
            }
        });
    }

    public void getData(){
        ListHttpHelper.getFollowProjectListByProjId(this, SPUtils.get(this, USER_ACCOUNT, "").toString(),getIntent().getStringExtra("projId") ,mPage+"", "10", new SDRequestCallBack(FollowProjectByIdListBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showLong(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                FollowProjectByIdListBean bean = (FollowProjectByIdListBean) responseInfo.getResult();
                mBackListSize = bean.getData().getTotal();
                if (mPage == 0)
                {
                    followProjectByIdMultiBeen.clear();
                }
                for (int i = 0; i < bean.getData().getData().size(); i++){
                    int itemType = 0;
                    if(bean.getData().getData().get(i).getNoteType().equals("TEXT")){

                        itemType = 1;
                    }else {
                        //音频
                        itemType = 3;
                    }
                    followProjectByIdMultiBeen.add(new FollowProjectByIdMultiBean(itemType,bean.getData().getData().get(i)));
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick(R.id.btn_ok)
    public void onItemClick(final View view){
        view.setClickable(false);
        ListHttpHelper.updateNeedToDoDoneMsgData(this,SPUtils.get(this, USER_ACCOUNT, "").toString(),getIntent().getStringExtra("origBusId"), new SDRequestCallBack(IDGBaseBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
                view.setClickable(true);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                IDGBaseBean baseBean = (IDGBaseBean) responseInfo.getResult();
                if ("success".equals(baseBean.getData().getCode())) {
                    SDToast.showShort("提交成功!");
                    finish();
                } else {
                    SDToast.showShort(baseBean.getData().getReturnMessage());
                    view.setClickable(true);
                }
            }
        });
    }

    private void initVoiceListPopupWindow(View view, FollowProjectByIdBaseBean bean)
    {
        String[] urls = bean.getNote().split(",");
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
                        VoiceUtils.getInstance().getVoiceFromNet(TrackProgressStatusActivity.this, ((VoiceListBean) adapter.getData().get
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

}
