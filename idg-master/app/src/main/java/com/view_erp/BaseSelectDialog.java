package com.view_erp;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.AbsListView;

import com.http.SDHttpHelper;
import com.injoy.idg.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import tablayout.view.xlistview.XListView;

/**
 * Created by cx on 2016/10/19.
 */
public abstract class BaseSelectDialog extends BaseDialog implements XListView.IXListViewListener{

    protected XListView mListView;
    protected Context context;
    protected SDHttpHelper mHttpHelper;


    @Override
    protected void init(AlertDialog dialog, Context context) {
        this.context=context;
        mHttpHelper = new SDHttpHelper(context);
        mListView = (XListView) dialog.findViewById(R.id.lv_find_result);
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(true);
//		mListView.setAutoLoadEnable(true);
        mListView.setXListViewListener(this);
        mListView.setRefreshTime(getTime());
        mListView.setOnScrollListener(new XListView.OnXScrollListener() {
            @Override
            public void onXScrolling(View view) {

            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    protected String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)
                .format(new Date());
    }

    protected void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime(getTime());
    }

}
