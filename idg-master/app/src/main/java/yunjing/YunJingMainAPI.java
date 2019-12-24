package yunjing;

import android.content.Context;

import com.erp_https.BaseAPI;
import com.http.HttpURLUtil;
import com.http.SDHttpHelper;
import com.http.callback.SDRequestCallBack;
import com.lidroid.xutils.http.RequestParams;

/**
 * Created by tujingwu on 2017/8/12.
 * 首页（一级、二级界面）api
 */

public class YunJingMainAPI extends BaseAPI {
    /**
     * 获取公司的所有部门
     * @param callBack
     */
    public static void getDeptsData(Context context, SDRequestCallBack callBack) {
        String url = HttpURLUtil.newInstance().append("sysDepartment").append("getDept").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, false, callBack);
    }

}
