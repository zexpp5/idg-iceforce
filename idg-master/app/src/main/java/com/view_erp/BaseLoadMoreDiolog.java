package com.view_erp;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.view.View;

import com.chaoxiang.base.utils.SDLogUtil;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.CommonUtils;
import com.utils.EmptyLayoutForList;
import com.utils.SDToast;

import java.lang.reflect.Type;

/**
 * Created by cx on 2016/10/19.
 */
public abstract class BaseLoadMoreDiolog extends BaseSelectDialog {
    protected static final int CURR_MAX_PAGE_SIZE = 15;
    protected int currPage = 1;
    protected EmptyLayoutForList mEmptyLayout;
    private boolean loadMore = false;
    private boolean loadMoreToast = false;
    protected boolean isFirstLoadFinish = false;

    protected static final int FIRST_LOAD = 0;
    protected static final int LOAD_MORE = 1;
    protected static final int REFRESH = 2;
    private boolean refresh = false;

    private int currStatus;

    protected BroadcastReceiver register;


    public int getCurrStatus()
    {
        return currStatus;
    }

    public void setCurrStatus(int currStatus)
    {
        this.currStatus = currStatus;
        if (currStatus == FIRST_LOAD)
        {
            refresh = false;
            loadMore = false;
        }
    }

    protected void registerBroastReceiver()
    {

    }



    /**
     * 重置
     */
    protected void reset()
    {
        //mListView.setAdapter(null);
        setCurrPage(1);
        mEmptyLayout.showLoading();
        setCurrStatus(FIRST_LOAD);
    }

    @Override
    protected void init(AlertDialog dialog, Context context) {
        super.init(dialog, context);
        mEmptyLayout = new EmptyLayoutForList(context, mListView);
        mEmptyLayout.showLoading();
        mEmptyLayout.setOnErrorOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                mEmptyLayout.showLoading();
                getData(FIRST_LOAD);
            }
        });
//        mEmptyLayout.setOnEmptyOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                mEmptyLayout.showLoading();
//                getData(REFRESH);
//            }
//        });
        mListView.setPullLoadEnable(false);
        getData(FIRST_LOAD);
        registerBroastReceiver();
    }



    protected int getCurrPage()
    {
        return currPage;
    }

    /**
     * 设置当前页数
     *
     * @param currPage
     */
    public void setCurrPage(int currPage)
    {
        this.currPage = currPage;
        if (currPage == 1)
        {
            mListView.setPullLoadEnable(false);
        }
    }

    /**
     * 设置是不是加载更多
     *
     * @return
     */
    public boolean isLoadMore()
    {
        return loadMore;
    }

    /**
     * 设置是不是加载更多
     *
     * @param loadMore
     */
    public void setLoadMore(boolean loadMore)
    {
        this.loadMore = loadMore;
    }

    @Override
    public void onRefresh()
    {
        refresh = true;
        if (CommonUtils.isNetWorkConnected(context))
        {
            SDLogUtil.debug("onRefresh");
            getData(REFRESH);
        } else
        {
            SDToast.showShort(context.getString(R.string.connect_failuer_toast));
            onLoad();
        }
    }

    @Override
    public void onLoadMore()
    {
        if (CommonUtils.isNetWorkConnected(context))
        {
            SDLogUtil.debug("onLoadMore");
            if (!refresh)
            {
                getData(LOAD_MORE);
                loadMore = true;
            }
        } else
        {
            SDToast.showShort(context.getString(R.string.connect_failuer_toast));
            onLoad();
        }
    }

    /**
     * 获取数据（内部处理）
     */
    protected void getData(int status)
    {
        if (status == FIRST_LOAD)
        {
            currPage = 1;
        } else if (status == LOAD_MORE)
        {
            if (loadMore)
            {
                currPage++;
            } else if (!loadMore && loadMoreToast)
            {
//                SDToast.showShort("没有数据了");
                loadMoreToast = false;
                onLoad();
                return;
            } else if (!loadMore)
            {
                onLoad();
                return;
            }
        } else if (REFRESH == status)
        {
            currPage = 1;
        }
        setCurrStatus(status);
        getData();
    }

    /**
     * 获取数据
     */
    protected abstract void getData();


    /**
     * 该类请求的回调
     *
     * @author Administrator
     */
    protected abstract class BaseRequestCallBack extends SDRequestCallBack
    {

        public BaseRequestCallBack()
        {
            super();
        }

        public BaseRequestCallBack(Class clazz)
        {
            super(clazz);
        }

        public BaseRequestCallBack(Type type)
        {
            super(type);
        }

        @Override
        public void onRequestFailure(HttpException error, String msg)
        {
            int statusCode = error.getExceptionCode();
            if (statusCode == 501)
            {
                mListView.setVisibility(View.GONE);
                mListView.setPullLoadEnable(false);
                mEmptyLayout.showEmpty();
            } else
            {
                if (loadMore)
                {
                    currPage--;
                }
                if (refresh || loadMore || isFirstLoadFinish)
                {
                    refresh = false;
                    loadMore = false;
                    onLoad();
                    SDToast.showShort(context.getString(R.string.net_error));
                } else
                {
                    mListView.setVisibility(View.GONE);
                    mEmptyLayout.showError();
                }
                onFailure(error, msg, getCurrStatus());
            }
        }

        @Override
        public final void onRequestSuccess(SDResponseInfo responseInfo)
        {
//            int statusCodeString = 200;
//            String jsonString = (String) responseInfo.getResult();
//            try
//            {
//                JSONObject obj = new JSONObject(jsonString);
//                statusCodeString = obj.getInt("status");
//            } catch (JSONException e)
//            {
//                e.printStackTrace();
//            }
//            if (statusCodeString == 501)
//            {
//                mListView.setPullLoadEnable(false);
//                mEmptyLayout.showEmpty();
//            } else
//            {

            mListView.setVisibility(View.VISIBLE);
            refresh = false;
            if (getCurrStatus() == FIRST_LOAD)
            {
                int total = firstLoad(responseInfo); // 总条数
                int pageNum = total / CURR_MAX_PAGE_SIZE; // 第几页
                int pageSize = total % CURR_MAX_PAGE_SIZE; // 多少条
                if (pageSize != 0) pageNum++;
                if (pageNum == 0)
                {
                    mListView.setPullLoadEnable(false);
                    mEmptyLayout.showEmpty();
                } else if (pageNum <= currPage)
                { // 不能加载更多
                    setLoadMore(false);
                    mListView.setPullLoadEnable(false);
                    mEmptyLayout.showData();
                    isFirstLoadFinish = true;
                } else
                { //  加载更多
                    isFirstLoadFinish = true;
                    setLoadMore(true);
                    loadMoreToast = true;
                    mListView.setPullLoadEnable(true);
                    mEmptyLayout.showData();
                }
            } else if (getCurrStatus() == LOAD_MORE)
            {
                onLoad();
                int total = loadMore(responseInfo); // 总条数
                int pageNum = total / CURR_MAX_PAGE_SIZE; // 第几页
                int pageSize = total % CURR_MAX_PAGE_SIZE; // 多少条
                if (pageSize != 0) pageNum++;
                if (pageNum <= currPage)
                { // 不能加载更多
                    setLoadMore(false);
//                    SDToast.showShort("没有数据了");
                    mListView.setPullLoadEnable(false);
                } else
                {
                    setLoadMore(true);
                }
            } else if (REFRESH == getCurrStatus())
            {
                onLoad();
                int total = refresh(responseInfo); // 总条数
                int pageNum = total / CURR_MAX_PAGE_SIZE; // 第几页
                int pageSize = total % CURR_MAX_PAGE_SIZE; // 多少条
                if (pageSize != 0) pageNum++;
                if (pageNum == 0)
                {
                    mListView.setPullLoadEnable(false);
                    mEmptyLayout.showEmpty();
                } else if (pageNum <= currPage)
                {
                    setLoadMore(false);
                    mListView.setPullLoadEnable(false);
                    mEmptyLayout.showData();
                } else
                {
                    mEmptyLayout.showData();
                    setLoadMore(true);
                    loadMoreToast = true;
                    mListView.setPullLoadEnable(true);
                }
            }
            onSuccess(responseInfo);
        }

        protected void onFailure(HttpException error, String msg, int status)
        {

        }

        protected void onSuccess(SDResponseInfo responseInfo)
        {

        }

        /**
         * 第一次加载 判断是不是加载更多设置
         *
         * @return 返回当前条数
         */
        protected abstract int firstLoad(SDResponseInfo responseInfo);

        /**
         * 加载更多 需要判断是不是加载更多设置
         *
         * @return 返回值 当前总记录数
         */
        protected abstract int loadMore(SDResponseInfo responseInfo);

        /**
         * 刷新
         *
         * @return 返回当前条数
         */
        protected abstract int refresh(SDResponseInfo responseInfo);
    }


    /**
     * 设置总数
     *
     * @param total
     */
    protected void setTotal(int total)
    {
        int pageNum = total / CURR_MAX_PAGE_SIZE; // 第几页
        int pageSize = total % CURR_MAX_PAGE_SIZE; // 多少条
        if (pageSize != 0) pageNum++;
        if (pageNum <= currPage)
        { // 不能加载更多
            setLoadMore(false);
//                    SDToast.showShort("没有数据了");
            mListView.setPullLoadEnable(false);
        } else
        {
            setLoadMore(true);
            mListView.setPullLoadEnable(true);
        }
    }
}
