package com.cxgz.activity.cxim.ui.company.activity;

import android.content.Context;

import com.cxgz.activity.cxim.base.DataCallBack;
import com.cxgz.activity.cxim.http.ImHttpHelper;
import com.cxgz.activity.cxim.ui.company.bean.DeptBeanList;
import com.cxgz.activity.cxim.ui.company.bean.DeptNumBeanList;
import com.cxgz.activity.cxim.workCircle.bean.WorkCilcleDetailListBean;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.lidroid.xutils.exception.HttpException;

/**
 * Created by selson on 2017/8/18.
 */
public class DateModel
{
    public DateModel()
    {

    }

    /**
     * 获取部门。
     */
    public void getDept(final Context context, String beanJson, final DataCallBack requestListener)
    {
        ImHttpHelper.getDept(context, beanJson, new SDRequestCallBack(DeptBeanList.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                requestListener.requestFailed(error.getExceptionCode(), msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                if (responseInfo.getStatus() == 200)
                    requestListener.requestSuccess(responseInfo.getResult());
            }
        });
    }

    /**
     * 获取部门。
     */
    public void getDeptNum(final Context context, String beanJson, final DataCallBack requestListener)
    {
        ImHttpHelper.getDeptNum(context, beanJson, new SDRequestCallBack(DeptNumBeanList.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                requestListener.requestFailed(error.getExceptionCode(), msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                if (responseInfo.getStatus() == 200)
                    requestListener.requestSuccess(responseInfo.getResult());
            }
        });
    }


}
