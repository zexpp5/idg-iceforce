package com.cxgz.activity.basic;

import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;

import com.cxgz.activity.cxim.base.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import tablayout.view.xlistview.XListView;

/**
 * 父XListView
 */
public abstract class BaseXListViewActivity extends BaseActivity implements XListView.IXListViewListener {
	protected XListView mListView;
	protected Handler handler = new Handler();
	
	@Override
	protected void init() {
		mListView = (XListView) findViewById(getXListViewId());
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

	protected String getTime() {
		return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)
				.format(new Date());
	}
	
	protected void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(getTime());
	}
	
	public abstract int getXListViewId();



}
