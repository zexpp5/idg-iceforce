package com.cxgz.activity.cxim.workCircle.mvp.modle;

import android.content.Context;
import android.os.AsyncTask;

import com.chaoxiang.base.utils.MyToast;
import com.cxgz.activity.cxim.bean.CircleModelListBean;
import com.cxgz.activity.cxim.http.ImHttpHelper;
import com.cxgz.activity.cxim.http.WorkRecordFilter;
import com.cxgz.activity.cxim.workCircle.bean.WorkCilcleDetailListBean;
import com.cxgz.activity.cxim.workCircle.listener.IDataRequestListener;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.lidroid.xutils.exception.HttpException;


/**
 * @author yiw
 * @ClassName: CircleModel
 * @Description: 因为逻辑简单，这里我就不写model的接口了
 * @date 2015-12-28 下午3:54:55
 */
public class CircleModel
{
    public CircleModel()
    {

    }

    public void loadDetailData(Context context, String s_btype, String l_eid, final IDataRequestListener listener)
    {
        requestWorkDetail(context, s_btype, l_eid, listener);
    }

    public void loadData(Context context, int tagInt, final int page, final IDataRequestListener listener)
    {
        if (tagInt == 0)
            requestfindPageMyWordAndReceive(context, page, listener);
        if (tagInt == 1)
            requestfindPageMyWordAndReceive2(context, page, listener);
    }

    public void deleteCircle(final IDataRequestListener listener)
    {
        requestServer(listener);
    }

    public void addFavort(final IDataRequestListener listener)
    {
        requestServer(listener);
    }

    public void deleteFavort(final IDataRequestListener listener)
    {
        requestServer(listener);
    }

    public void addComment(Context context, final WorkRecordFilter workRecordFilter, final IDataRequestListener listener)
    {
//        requestServer(listener);
        requestWorkRecord(context, workRecordFilter, listener);
    }

    public void deleteComment(final IDataRequestListener listener)
    {
        requestServer(listener);
    }

    /**
     * @param listener 设定文件
     * @return void    返回类型
     * @throws
     * @Title: requestServer
     */
    private void requestServer(final IDataRequestListener listener)
    {
        new AsyncTask<Object, Integer, Object>()
        {
            @Override
            protected Object doInBackground(Object... params)
            {
                //和后台交互
                return null;
            }

            protected void onPostExecute(Object result)
            {
                listener.loadSuccess(result);
            }
        }.execute();
    }

    /**
     * 获取我和我提交给别人的工作圈状态。
     * @param page
     * @param listener
     */
    private void requestfindPageMyWordAndReceive(final Context context, int page, final IDataRequestListener listener)
    {
        ImHttpHelper.findPageMyWordAndReceive(context, page, new SDRequestCallBack(CircleModelListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
            }
            
            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                if (responseInfo.getStatus() == 200)
                    listener.loadSuccess(responseInfo.getResult());
            }
        });
    }

    /**
     * 自己的
     * @param context
     * @param page
     * @param listener
     */
    private void requestfindPageMyWordAndReceive2(final Context context, int page, final IDataRequestListener listener)
    {
        ImHttpHelper.findPageMyWord(context, page, new SDRequestCallBack(CircleModelListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
//                MyToast.showToast(context, msg);
                if (responseInfo.getStatus() == 200)
                    listener.loadSuccess(responseInfo.getResult());
            }
        });
    }

    /**
     * 评论工作圈
     *
     * @param context
     * @param workRecordFilter
     */
    private void requestWorkRecord(final Context context, WorkRecordFilter workRecordFilter, final IDataRequestListener listener)
    {
        ImHttpHelper.postRecord(context, workRecordFilter, new SDRequestCallBack()
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(context, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                if (responseInfo.getStatus() == 200)
                    listener.loadSuccess(responseInfo.getResult());
            }
        });
    }

    private void requestWorkDetail(final Context context, String s_btype, String l_eid, final IDataRequestListener listener)
    {
        ImHttpHelper.findRecordDetail(context, s_btype, l_eid, new SDRequestCallBack(WorkCilcleDetailListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(context, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                if (responseInfo.getStatus() == 200)
                    listener.loadSuccess(responseInfo.getResult());
            }
        });
    }


}
