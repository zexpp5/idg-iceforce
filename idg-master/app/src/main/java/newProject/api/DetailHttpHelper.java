package newProject.api;

import android.content.Context;
import android.text.TextUtils;

import com.erp_https.BaseAPI;
import com.http.HttpURLUtil;
import com.http.SDHttpHelper;
import com.http.callback.SDRequestCallBack;
import com.lidroid.xutils.http.RequestParams;

import org.apache.http.message.BasicNameValuePair;

/**
 * Created by Administrator on 2017/10/21.
 */

public class DetailHttpHelper extends BaseAPI {


    /**
     * 获取审批内容list
     *
     * @param callBack
     */
    public static void getApprovalList(Context context, String l_bid, String i_type, SDRequestCallBack callBack) {
        String url = HttpURLUtil.newInstance().append("approvalset").append("getApprovalList").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(l_bid)) {
            pairs.add(new BasicNameValuePair("l_bid", l_bid));
        }
        if (!TextUtils.isEmpty(i_type)) {
            pairs.add(new BasicNameValuePair("i_type", i_type));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    /**
     * 获取借支申请详情
     *
     * @param callBack
     */
    public static void getLoanDetail(Context context, String eid, SDRequestCallBack callBack) {
        String url = HttpURLUtil.newInstance().append("loan").append("detail").append(eid).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    /**
     * 获取外出工作详情
     *
     * @param callBack
     */
    public static void getOutWorkDetail(Context context, String eid, SDRequestCallBack callBack) {
        String url = HttpURLUtil.newInstance().append("outWork").append("detail").append(eid).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    /**
     * 获取事务详情
     *
     * @param callBack
     */
    public static void getAffairDetail(Context context, String eid, SDRequestCallBack callBack) {
        String url = HttpURLUtil.newInstance().append("affair").append("detail").append(eid).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    /**
     * 获取请假详情
     *
     * @param callBack
     */
    public static void getHolidayDetail(Context context, String eid, SDRequestCallBack callBack) {
        String url = HttpURLUtil.newInstance().append("holiday").append("detail").append(eid).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }


    /**
     * 获取项目协同详情
     *
     * @param callBack
     */
    public static void getProjectDetail(Context context, String eid, SDRequestCallBack callBack) {
        String url = HttpURLUtil.newInstance().append("project").append("detail").append(eid).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    /**
     * 获取用户详情
     *
     * @param callBack
     */
    public static void getSysuserDetail(Context context, String eid, SDRequestCallBack callBack) {
        String url = HttpURLUtil.newInstance().append("sysuser").append("detail").append(eid).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    /**
     * 获取我的审批详情页数据
     * @param context
     * @param eid
     * @param callBack
     */
    public static void getApproveDetail(Context context, String eid, SDRequestCallBack callBack) {
        String url = HttpURLUtil.newInstance().append("holiday").append("getApproveDetail").append(eid).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    /**
     * 获取我的审批详情页数据
     * @param context
     * @param eid
     * @param callBack
     */
    public static void getBusinessTripApproveDetail(Context context, String eid, SDRequestCallBack callBack) {
        String url = HttpURLUtil.newInstance().append("travel").append("detail").append(eid).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    /**
     * 获取不良资产情况详情
     *
     * @param context
     * @param callBack
     */
    public static void getBadAssetsList(Context context, String projId, SDRequestCallBack callBack) {
        String url = HttpURLUtil.newInstance().append("badAssets").append("detail").append(projId).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }
}
