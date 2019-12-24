package newProject.company.project_manager.investment_project.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cxgz.activity.cxim.base.BaseFragment;
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
import newProject.company.project_manager.investment_project.FollowProjectDetailActivity;
import newProject.company.project_manager.investment_project.InvestedProjectsDetailActivity;
import newProject.company.project_manager.investment_project.PotentialProjectsDetailActivity;
import newProject.company.project_manager.investment_project.adapter.PopuVoiceListAdapter;
import newProject.company.project_manager.investment_project.adapter.WorkCircleAdapter;
import newProject.company.project_manager.investment_project.adapter.WorkCircleInduAdapter;
import newProject.company.project_manager.investment_project.adapter.WorkCircleListPopupAdapter;
import newProject.company.project_manager.investment_project.bean.FollowProjectBaseBean;
import newProject.company.project_manager.investment_project.bean.FollowProjectListBean;
import newProject.company.project_manager.investment_project.bean.FollowProjectMultiBean;
import newProject.company.project_manager.investment_project.bean.IconListBean;
import newProject.company.project_manager.investment_project.bean.PPIndustryListBean;
import newProject.company.project_manager.investment_project.bean.WorkCircleIndusBean;
import newProject.company.project_manager.investment_project.voice.VoiceListBean;
import newProject.company.project_manager.investment_project.voice.VoiceUtils;
import yunjing.utils.DisplayUtil;
import yunjing.view.StatusTipsView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.utils.SPUtils.USER_ACCOUNT;



/**
 * Created by zsz on 2019/4/24.
 */

public class WorkCircleFragment extends BaseFragment {
    @Bind(R.id.loading_view)
    StatusTipsView mTips;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;

    private int mPage = 0;
    private int mBackListSize = 0;

    WorkCircleAdapter adapter;

    List<FollowProjectMultiBean> followProjectMultiBeen = new ArrayList<>();

    List<PPIndustryListBean.DataBeanX.DataBean> industryLists = new ArrayList<>();
    List<WorkCircleIndusBean> induLists = new ArrayList<>();
    List<WorkCircleIndusBean> otherLists = new ArrayList<>();
    PopupWindow popupWindow;

    List<IconListBean> popuList = new ArrayList<>();

    String state;
    String comIndu;
    String isMyFollow;
    String dateRange;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_work_circle;
    }

    @Override
    protected void init(View view) {
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new WorkCircleAdapter(followProjectMultiBeen);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.ll_voice){
                    /*ImageView ivVoice = (ImageView) view.findViewById(R.id.iv_voice);
                    ivVoice.setBackgroundColor(0xffffffff);
                    ivVoice.setImageResource(R.drawable.play_annex_voice);
                    VoiceUtils.getInstance().getVoiceFromNet(getActivity(),followProjectMultiBeen.get(position).getData().getUrl(),ivVoice);*/
                    if (followProjectMultiBeen.get(position).getData().getUrl().contains(","))
                    {
                        initVoiceListPopupWindow(view, followProjectMultiBeen.get(position).getData());
                    } else
                    {
                        ImageView ivVoice = (ImageView) view.findViewById(R.id.iv_voice);
                        ivVoice.setBackgroundColor(0xffffffff);
                        ivVoice.setImageResource(R.drawable.play_annex_voice);
                        VoiceUtils.getInstance().getVoiceFromNet(getActivity(), followProjectMultiBeen.get(position).getData().getUrl(), ivVoice);
                    }
                }else if (view.getId() == R.id.tv_content){
                    Intent intent = new Intent();
                    if ("POTENTIAL".equals(followProjectMultiBeen.get(position).getData().getProjType())){
                        intent.setClass(getActivity(), PotentialProjectsDetailActivity.class);
                    }else if ("INVESTED".equals(followProjectMultiBeen.get(position).getData().getProjType())){
                        intent.setClass(getActivity(), InvestedProjectsDetailActivity.class);
                    }else if("FOLLOW_ON".equals(followProjectMultiBeen.get(position).getData().getProjType())){
                        intent.setClass(getActivity(), FollowProjectDetailActivity.class);
                    }else {
                        SDToast.showShort("未知项目类型");
                        return;
                    }
                    intent.putExtra("projName",followProjectMultiBeen.get(position).getData().getProjName());
                    intent.putExtra("projId",followProjectMultiBeen.get(position).getData().getProjId());
                    startActivity(intent);
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
                state = "";
                comIndu = "";
                isMyFollow = "";
                dateRange = "";
                getData();
            }
        });

        getIndustryData();
    }

    @Override
    public void onStart() {
        super.onStart();
        mPage = 0;
        mBackListSize = 0;
        getData();
    }

    @OnClick({R.id.ll_title,R.id.iv_add,R.id.iv_date})
    public void onItemClick(View view){
        switch (view.getId()){
            case R.id.ll_title:
                if (popupWindow != null && popupWindow.isShowing() ){
                    popupWindow.dismiss();
                }else {
                    setPopupWindowData();
                    initTopPopupWindow(view);
                }
                break;
            case R.id.iv_add:
                popuList.clear();
                popuList.add(new IconListBean(R.mipmap.icon_public_add,"工作进展",""));
                popuList.add(new IconListBean(R.mipmap.icon_public_add,"我的工作圈","1"));
                popuList.add(new IconListBean(R.mipmap.icon_public_add,"重点跟踪","2"));
                initlistPopupWindow((View) view.getParent(),"S");
                break;
            case R.id.iv_date:
                popuList.clear();
                popuList.add(new IconListBean(R.mipmap.icon_public_add,"全部","All"));
                popuList.add(new IconListBean(R.mipmap.icon_public_add,"最近一周","1Week"));
                popuList.add(new IconListBean(R.mipmap.icon_public_add,"最近一月","1Month"));
                popuList.add(new IconListBean(R.mipmap.icon_public_add,"最近三月","3Month"));
                initlistPopupWindow((View) view.getParent(),"D");
                break;
        }
    }

    private void initRefresh()
    {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(getActivity()))
                {
                    mPage = 0;
                    mRefreshLayout.finishRefresh(1000);
                    mRefreshLayout.setLoadmoreFinished(false);
                    mRefreshLayout.setEnableLoadmore(true);
                    state = "";
                    comIndu = "";
                    isMyFollow = "";
                    dateRange = "";
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
                if (DisplayUtil.hasNetwork(getActivity()))
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
        ListHttpHelper.getFollowProjectList(getActivity(), SPUtils.get(getActivity(), USER_ACCOUNT, "").toString(),mPage+"", "10",state,comIndu,isMyFollow,dateRange, new SDRequestCallBack(FollowProjectListBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showLong(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                FollowProjectListBean bean = (FollowProjectListBean) responseInfo.getResult();
                mBackListSize = bean.getData().getTotal();
                if (mPage == 0)
                {
                    adapter.getData().clear();
                    if (bean.getData().getData().size() > 0)
                    {
                        mTips.hide();
                    } else
                    {
                        mTips.showNoContent("暂无数据");
                    }
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
                    followProjectMultiBeen.add(new FollowProjectMultiBean(itemType,bean.getData().getData().get(i)));
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void getIndustryData() {
        ListHttpHelper.getIndustryListData(getActivity(), new SDRequestCallBack(PPIndustryListBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                PPIndustryListBean bean = (PPIndustryListBean) responseInfo.getResult();
                industryLists.clear();
                industryLists.addAll(bean.getData().getData());
            }
        });
    }

    public void setPopupWindowData(){
        induLists.clear();
        otherLists.clear();
        WorkCircleIndusBean indusBean = new WorkCircleIndusBean();
        indusBean.setDesc("全部");
        induLists.add(indusBean);

        for (int i = 0; i < industryLists.size(); i++){
            WorkCircleIndusBean indusBean1 = new WorkCircleIndusBean();
            indusBean1.setDesc(industryLists.get(i).getCodeNameZhCn());
            indusBean1.setKey(industryLists.get(i).getCodeKey());
            induLists.add(indusBean1);
        }
        for (int j = 0; j < industryLists.get(0).getChildren().size();j++){
            WorkCircleIndusBean indusBean2 = new WorkCircleIndusBean();
            indusBean2.setDesc(industryLists.get(0).getChildren().get(j).getCodeNameZhCn());
            indusBean2.setKey(industryLists.get(0).getChildren().get(j).getCodeKey());
            otherLists.add(indusBean2);
        }
    }

    public void initTopPopupWindow(View view){
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.popupwindow_work_circle_top, null);
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);

        RecyclerView recyclerViewIndus = (RecyclerView) contentView.findViewById(R.id.recycler_view_indus);
        WorkCircleInduAdapter induAdapter = new WorkCircleInduAdapter(induLists);
        recyclerViewIndus.setLayoutManager(new GridLayoutManager(getActivity(),4));
        recyclerViewIndus.setAdapter(induAdapter);
        induAdapter.notifyDataSetChanged();

        RecyclerView recyclerViewOther = (RecyclerView) contentView.findViewById(R.id.recycler_view_other);
        final WorkCircleInduAdapter induAdapter2 = new WorkCircleInduAdapter(otherLists);
        recyclerViewOther.setLayoutManager(new GridLayoutManager(getActivity(),4));
        recyclerViewOther.setAdapter(induAdapter2);
        induAdapter2.notifyDataSetChanged();

        induAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                if (position == 0){
                    popupWindow.dismiss();
                    mPage = 0;
                    mBackListSize = 0;
                    getData();
                    return;
                }

                for (int i = 0; i < induLists.size(); i++){
                    induLists.get(i).setFlag(false);
                }
                induLists.get(position).setFlag(true);

                adapter.notifyDataSetChanged();
                //第一个是全部，所以要减一
                position--;

                otherLists.clear();
                for (int i = 0; i < industryLists.get(position).getChildren().size(); i++){
                    WorkCircleIndusBean bean = new WorkCircleIndusBean();
                    bean.setDesc(industryLists.get(position).getChildren().get(i).getCodeNameZhCn());
                    bean.setKey(industryLists.get(position).getChildren().get(i).getCodeKey());
                    otherLists.add(bean);
                }
                induAdapter2.notifyDataSetChanged();
            }
        });

        induAdapter2.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                popupWindow.dismiss();
                mPage = 0;
                mBackListSize = 0;
                state = "";
                comIndu = otherLists.get(position).getKey();
                isMyFollow = "";
                dateRange = "";
                getData();
            }
        });

        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);
        setBackgroundAlpha(0.5f);
        //contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
        int xPos = view.getWidth() / 2 - popupWindow.getContentView().getMeasuredWidth() / 2;
        popupWindow.showAsDropDown(view, xPos,3);
        //(location[0] + view.getWidth() / 2) + popupWidth / 2, location[1] + popupHeight
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1f);
            }
        });
    }


    public void initlistPopupWindow(View view,final String flag){

        final ListPopupWindow listPopupWindow = new ListPopupWindow(getActivity());
        listPopupWindow.setWidth(340);
        listPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        listPopupWindow.setDropDownGravity(Gravity.RIGHT);
        final WorkCircleListPopupAdapter listPopupAdapter = new WorkCircleListPopupAdapter(popuList, getActivity());
        listPopupWindow.setAdapter(listPopupAdapter);
        listPopupWindow.setModal(true);
        listPopupWindow.setAnchorView(view);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listPopupWindow.dismiss();
                if (flag.equals("S")){
                    if (i == 0){

                    }else if (i == 1){
                        state = "";
                        comIndu = "";
                        isMyFollow = popuList.get(i).getKey();
                        dateRange = "";
                        getData();
                    }else {
                        state = popuList.get(i).getKey();
                        comIndu = "";
                        isMyFollow = "";
                        dateRange = "";
                        getData();
                    }

                }else {
                    state = "";
                    comIndu = "";
                    isMyFollow = "";
                    dateRange = popuList.get(i).getKey();
                    getData();
                }

            }
        });
        listPopupWindow.show();
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

        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.popupwindow_voice_list, null);
        RecyclerView voiceRecyclerView = (RecyclerView) contentView.findViewById(R.id.recycler_view);
        voiceRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        PopuVoiceListAdapter adapter = new PopuVoiceListAdapter(voiceListBeen);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener()
        {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position)
            {
                switch (view.getId())
                {
                    case R.id.iv_voice:
                        VoiceUtils.getInstance().getVoiceFromNet(getActivity(), ((VoiceListBean) adapter.getData().get
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
