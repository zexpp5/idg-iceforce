package com.cxgz.activity.cxim.workCircle.mvp.contract;


import com.cxgz.activity.cxim.bean.CircleModelListBean;
import com.cxgz.activity.cxim.http.WorkRecordFilter;
import com.cxgz.activity.cxim.workCircle.bean.CircleItem;
import com.cxgz.activity.cxim.workCircle.bean.CommentConfig;
import com.cxgz.activity.cxim.workCircle.bean.CommentItem;
import com.cxgz.activity.cxim.workCircle.bean.FavortItem;

import java.util.List;

/**
 * Created by suneee on 2016/7/15.
 */
public interface CircleContract
{

    interface View extends BaseView
    {
        void update2DeleteCircle(String circleId);

        void update2AddFavorite(int circlePosition, FavortItem addItem);

        void update2DeleteFavort(int circlePosition, String favortId);

        void update2AddComment(int circlePosition, CommentItem addItem);

        void update2DeleteComment(int circlePosition, String commentId);

        void updateEditTextBodyVisible(int visibility, CommentConfig commentConfig);

        void update2loadData(int loadType, List<CircleItem> datas);

        void update2loadData2(int loadType, List<CircleItem> datas, CircleModelListBean circleModelListBean);

        void update2loadDetailData(int loadType, List<CircleItem> datas);

        void update2AddComment(int position, WorkRecordFilter workRecordFilter);
    }

    interface Presenter extends BasePresenter
    {
        void loadData(int loadType);

        void deleteCircle(final String circleId);

        void addFavort(final int circlePosition);

        void deleteFavort(final int circlePosition, final String favortId);

        void deleteComment(final int circlePosition, final String commentId);

    }
}
