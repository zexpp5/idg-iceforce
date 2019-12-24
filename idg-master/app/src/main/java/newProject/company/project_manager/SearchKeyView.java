package newProject.company.project_manager;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

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

import java.util.ArrayList;

import newProject.api.ListHttpHelper;
import newProject.company.project_manager.investment_project.ProjectManagerListActivity;
import yunjing.utils.DisplayUtil;

/**
 * author: Created by aniu on 2018/6/21.
 */

public class SearchKeyView extends LinearLayout {

    SmartRefreshLayout smart_refresh_layout;
    RecyclerView recyclerview;
    private String key;
    private CallKeyWork callKeyWork;
    private int page = 1;
    int total;
    SearchKeyAdapter searchKeyAdapter;

    public SearchKeyView(Context context) {
        super(context);
        initView();
    }

    public SearchKeyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SearchKeyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.layout_search_key, this);
        smart_refresh_layout = (SmartRefreshLayout) view.findViewById(R.id.smart_refresh_layout);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        smart_refresh_layout.setEnableRefresh(true);
        smart_refresh_layout.setEnableLoadmore(true);


        searchKeyAdapter = new SearchKeyAdapter(new ArrayList<String>());
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setAdapter(searchKeyAdapter);
        recyclerview.setNestedScrollingEnabled(false);
        searchKeyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                setVisibility(GONE);
                if (callKeyWork == null)
                    return;
                callKeyWork.callKeyWord(searchKeyAdapter.getItem(position));

                callKeyWork.callIsShow(false);
            }
        });

        smart_refresh_layout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (DisplayUtil.hasNetwork(getContext())) {
                    page = 1;
                    getData();
                    smart_refresh_layout.finishRefresh(1000);
                    smart_refresh_layout.setLoadmoreFinished(false);
                    smart_refresh_layout.setEnableLoadmore(true);
                } else {
                    smart_refresh_layout.finishRefresh(1000);
                }
            }
        });
        smart_refresh_layout.setEnableLoadmoreWhenContentNotFull(true);
        smart_refresh_layout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (DisplayUtil.hasNetwork(getContext())) {
                    if (searchKeyAdapter.getData().size() >= total) {
                        smart_refresh_layout.finishLoadmore(1000);
                        SDToast.showShort("没有更多数据了");
                    } else {
                        page = page + 1;
                        getData();
                        smart_refresh_layout.finishLoadmore(1000);
                    }
                } else {
                    smart_refresh_layout.finishLoadmore(1000);
                }

            }
        });
    }

    public void setKey(String key) {
        this.key = key;
        if (TextUtils.isEmpty(key) && getVisibility() == View.VISIBLE) {
            searchKeyAdapter.getData().clear();
            setVisibility(GONE);
            if (callKeyWork != null)
                callKeyWork.callIsShow(false);
        }
        page = 1;
        getData();
    }


    public void getData() {
        if (!DisplayUtil.hasNetwork(getContext())) {
            return;
        }
        ListHttpHelper.getQueryHintList(getContext(), page, key, new SDRequestCallBack(KeyWorkBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {

            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                KeyWorkBean accountListBean = (KeyWorkBean) responseInfo.getResult();
                boolean isHasData = (accountListBean.getData() == null || accountListBean.getData().size() == 0);
                if (page == 1 && isHasData) {
                    searchKeyAdapter.getData().clear();
                    setVisibility(GONE);
                    if (callKeyWork != null)
                        callKeyWork.callIsShow(false);
                    return;
                }

                if (isHasData) return;
                if (page == 1) {
                    searchKeyAdapter.getData().clear();
                    total = accountListBean.getTotal();
                }
                searchKeyAdapter.addData(accountListBean.getData());
                if (searchKeyAdapter.getData().contains(key) && searchKeyAdapter.getData().size() == 1) {
                    return;
                }
                searchKeyAdapter.notifyDataSetChanged();
                searchKeyAdapter.setKeyword(key);
                if (callKeyWork != null) {
                    setVisibility(VISIBLE);
                    callKeyWork.callIsShow(true);
                }

            }
        });
    }

    public void setCallKeyWork(CallKeyWork callKeyWork) {
        this.callKeyWork = callKeyWork;
    }

    public interface CallKeyWork {
        void callKeyWord(String keyWork);

        void callIsShow(boolean isShow);
    }
}
