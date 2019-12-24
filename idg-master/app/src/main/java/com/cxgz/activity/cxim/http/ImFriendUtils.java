package com.cxgz.activity.cxim.http;

import android.content.Context;

import com.chaoxiang.base.utils.MyToast;
import com.cxgz.activity.cxim.bean.PersonInfoBean;
import com.cxgz.activity.cxim.bean.PersonalListBean;
import com.http.SDHttpHelper;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;

/**
 * User: Selson
 * Date: 2017-01-13
 * FIXME
 */
public class ImFriendUtils
{
    public static PersonInfoBean searchPersonalInfo(final Context context, String userId)
    {
        final PersonInfoBean[] personInfoBean = new PersonInfoBean[1];
        SDHttpHelper mHttpHelper = new SDHttpHelper(context);
        ImHttpHelper.findPersonInfo(context, mHttpHelper, userId, new SDRequestCallBack(PersonalListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(context, R.string.request_fail_data);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                PersonalListBean personalInfoList = (PersonalListBean) responseInfo.getResult();
                personInfoBean[0] = personalInfoList.getData();
            }
        });
        return personInfoBean[0];
    }

    public static PersonInfoBean searchPersonalInfoByImAccount(final Context context, String imAccount)
    {
        final PersonInfoBean[] personInfoBean = new PersonInfoBean[1];
        SDHttpHelper mHttpHelper = new SDHttpHelper(context);
        ImHttpHelper.findPersonInfoByImAccount(context, true, mHttpHelper, imAccount, new SDRequestCallBack(PersonalListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(context, R.string.request_fail_data);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                PersonalListBean personalInfoList = (PersonalListBean) responseInfo.getResult();
                personInfoBean[0] = personalInfoList.getData();
            }
        });
        return personInfoBean[0];
    }

} 