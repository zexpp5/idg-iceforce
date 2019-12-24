package com.http.approhttp;

import com.http.HttpURLUtil;
import com.http.SDHttpHelper;
import com.http.callback.SDRequestCallBack;

/**
 * 审批人列表
 * Created by cx on 2016/5/31.
 */
public class ContactApprovalHttp {

    public static void findContactApprovalList(String type, String dpId, SDHttpHelper mHttpHelper,
                                               SDRequestCallBack callBack){
        String url = HttpURLUtil.newInstance().append("contact").append("approval").append(type).append(dpId).toString();
        mHttpHelper.get(url, null, true, callBack);
    }
}
