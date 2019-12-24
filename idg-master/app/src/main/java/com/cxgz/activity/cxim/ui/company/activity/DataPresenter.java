package com.cxgz.activity.cxim.ui.company.activity;

import android.content.Context;

import com.chaoxiang.base.utils.SDGson;
import com.cxgz.activity.cxim.base.DataCallBack;
import com.cxgz.activity.cxim.base.ViewInterface;

/**
 * Created by selson on 2017/8/18.
 * 控制器
 */
public class DataPresenter
{
    private ViewInterface viewInterface;
    private DateModel dateModel;

    public DataPresenter(ViewInterface viewInterface)
    {
        dateModel = new DateModel();
        this.viewInterface = viewInterface;
    }

    /**
     * @param context
     * @param beanJson 请求参数
     */
    public void getDeptData(Context context, String beanJson)
    {
        dateModel.getDept(context, beanJson, new DataCallBack()
        {
            @Override
            public void requestSuccess(Object responseStr)
            {
                //转换数据
                if (viewInterface != null)
                {
                    viewInterface.setData(SDGson.toJson(responseStr));
                }
            }

            @Override
            public void requestFailed(int code, String errMsg)
            {
                viewInterface.setEmpty(errMsg);
            }
        });
    }

    /**
     * @param context
     * @param beanJson 请求参数
     */
    public void getDeptNumData(Context context, String beanJson)
    {
        dateModel.getDeptNum(context, beanJson, new DataCallBack()
        {
            @Override
            public void requestSuccess(Object responseStr)
            {
                //转换数据
                if (viewInterface != null)
                {
                    viewInterface.setData2(SDGson.toJson(responseStr));
                }
            }

            @Override
            public void requestFailed(int code, String errMsg)
            {
                viewInterface.setEmpty(errMsg);
            }
        });
    }


}
