package newProject.api;

import android.content.Context;
import android.text.TextUtils;

import com.erp_https.BaseAPI;
import com.http.HttpURLUtil;
import com.http.SDHttpHelper;
import com.http.callback.SDRequestCallBack;
import com.lidroid.xutils.http.RequestParams;

import org.apache.http.message.BasicNameValuePair;

import yunjing.utils.JsonUtils;

/**
 * Created by Administrator on 2017/10/21.
 */

public class SubmitHttpHelper extends BaseAPI {


    /**
     * 提交审批
     *
     * @param callBack
     */
    public static void postApproval(Context context, String eid, String type, String remark, String approvalFinal, SDRequestCallBack callBack) {
        String url = HttpURLUtil.newInstance().append("approvalset").append("task").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(eid)) {
            pairs.add(new BasicNameValuePair("bid", eid));
        }
        if (!TextUtils.isEmpty(type)) {
            pairs.add(new BasicNameValuePair("btype", type));
        }
        if (!TextUtils.isEmpty(remark)) {
            pairs.add(new BasicNameValuePair("remark", remark));
        }
        if (!TextUtils.isEmpty(approvalFinal)) {
            pairs.add(new BasicNameValuePair("approvalFinal", approvalFinal));//1=同意；2=不同意，3=考虑
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }


    /**
     * 获取单号(只用于个人信息栏显示)
     *
     * @param callBack
     */
    public static void getCompanyNo(Context context, String type, SDRequestCallBack callBack) {
        String url = HttpURLUtil.newInstance().append("companyNo").append("current").toString();
        pairs.clear();
        pairs.add(new BasicNameValuePair("type", type));
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, false, callBack);
    }


    /**
     * 提交借支申请
     *
     * @param callBack
     */
    public static void submitLoan(Context context, String jsonStr, String annex, SDRequestCallBack callBack) {
        String url = HttpURLUtil.newInstance().append("loan").append("save").toString();
        pairs.clear();
        pairs = JsonUtils.getInstance().reTurnPair(jsonStr);
        if (!TextUtils.isEmpty(annex)) {
            pairs.add(new BasicNameValuePair("annex", annex));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, false, callBack);
    }

    /**
     * 提交事务报告
     *
     * @param callBack
     */
    public static void submitAffair(Context context, String jsonStr, String annex, SDRequestCallBack callBack) {
        String url = HttpURLUtil.newInstance().append("affair").append("save").toString();
        pairs.clear();
        pairs = JsonUtils.getInstance().reTurnPair(jsonStr);
        if (!TextUtils.isEmpty(annex)) {
            pairs.add(new BasicNameValuePair("annex", annex));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }


    /**
     * 提交外出工作
     *
     * @param callBack
     */
    public static void submitOutWork(Context context, String jsonStr, String annex, SDRequestCallBack callBack) {
        String url = HttpURLUtil.newInstance().append("outWork").append("save").toString();
        pairs.clear();
        pairs = JsonUtils.getInstance().reTurnPair(jsonStr);
        if (!TextUtils.isEmpty(annex)) {
            pairs.add(new BasicNameValuePair("annex", annex));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, false, callBack);
    }


    /**
     * 提交请假申请
     *
     * @param callBack
     */
    public static void submitHoliday(Context context, String jsonStr, String annex, SDRequestCallBack callBack) {
        String url = HttpURLUtil.newInstance().append("holiday").append("save").toString();
        pairs.clear();
        pairs = JsonUtils.getInstance().reTurnPair(jsonStr);
        if (!TextUtils.isEmpty(annex)) {
            pairs.add(new BasicNameValuePair("annex", annex));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, false, callBack);
    }

    /**
     * 提交项目协同
     *
     * @param callBack
     */
    public static void submitProject(Context context, String jsonStr, SDRequestCallBack callBack) {
        String url = HttpURLUtil.newInstance().append("project").append("save").toString();
        pairs.clear();
        pairs = JsonUtils.getInstance().reTurnPair(jsonStr);
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, false, callBack);
    }

    /**
     * 提交新同事
     *
     * @param callBack
     */
    public static void submitSysuser(Context context, String jsonStr, SDRequestCallBack callBack) {
        String url = HttpURLUtil.newInstance().append("sysuser").append("save").toString();
        pairs.clear();
        pairs = JsonUtils.getInstance().reTurnPair(jsonStr);
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    /**
     * 提交新同事
     *
     * @param callBack
     */
    public static void updateNewUser(Context context, String jsonStr, SDRequestCallBack callBack) {
        String url = HttpURLUtil.newInstance().append("sysuser").append("updateNewUser").toString();
        pairs.clear();
        pairs = JsonUtils.getInstance().reTurnPair(jsonStr);
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    /**
     * 发送批阅提醒
     *
     * @param callBack
     */
    public static void postRemind(Context context, String type, String eid, SDRequestCallBack callBack) {
        String url = HttpURLUtil.newInstance().append(type).append("approval").append("remind").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(eid)) {
            pairs.add(new BasicNameValuePair("eid", eid));
        }
        //  pairs = JsonUtils.getInstance().reTurnPair(jsonStr);
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }


    /**
     * @param context
     * @param applyId   主键id
     * @param approveId 审批id
     * @param isApprove 审批意见
     * @param reason    驳回原因
     * @param callBack
     */
    public static void postHolidayApprove(Context context, String applyId, String approveId, String isApprove, String reason, SDRequestCallBack callBack) {
        String url = HttpURLUtil.newInstance().append("holiday").append("approve").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(applyId)) {
            pairs.add(new BasicNameValuePair("applyId", applyId));
        }
        if (!TextUtils.isEmpty(approveId)) {
            pairs.add(new BasicNameValuePair("approveId", approveId));
        }
        if (!TextUtils.isEmpty(isApprove)) {
            pairs.add(new BasicNameValuePair("isApprove", isApprove));
        }
        if (!TextUtils.isEmpty(reason)) {
            pairs.add(new BasicNameValuePair("reason", reason));
        }
        //  pairs = JsonUtils.getInstance().reTurnPair(jsonStr);
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void postBusinessTripApprove(Context context, String eid,  String isApprove, String reason, SDRequestCallBack callBack) {
        String url = HttpURLUtil.newInstance().append("iceforce/post/approveTrip").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(eid)) {
            pairs.add(new BasicNameValuePair("applyId", eid));
        }
        if (!TextUtils.isEmpty(isApprove)) {
            pairs.add(new BasicNameValuePair("isApprove", isApprove));
        }
        if (!TextUtils.isEmpty(reason)) {
            pairs.add(new BasicNameValuePair("reason", reason));
        }
        //  pairs = JsonUtils.getInstance().reTurnPair(jsonStr);
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }
}
