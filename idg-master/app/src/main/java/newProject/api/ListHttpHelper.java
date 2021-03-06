package newProject.api;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.chaoxiang.base.config.Constants;
import com.chaoxiang.base.utils.StringUtils;
import com.entity.SDFileListEntity;
import com.erp_https.BaseAPI;
import com.http.FileUpload;
import com.http.HttpURLUtil;
import com.http.SDHttpHelper;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.http.config.BusinessType;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.utils.FileUploadPath;
import com.utils.FileUploadUtil;
import com.utils.SDToast;
import com.utils.SPUtils;

import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import newProject.company.project_manager.investment_project.PotentialProjectsAddActivity;
import newProject.company.project_manager.investment_project.bean.NewsLetterItemListBean;
import yunjing.http.BaseHttpHelper;
import yunjing.utils.DisplayUtil;
import yunjing.utils.JsonUtils;

import static android.R.attr.id;
import static com.chaoxiang.base.utils.StringUtils.append;
import static com.injoy.idg.R.id.year;
import static com.umeng.socialize.utils.DeviceConfig.context;
import static com.utils.SPUtils.USER_ACCOUNT;

/**
 * Created by Administrator on 2017/10/21.
 */

public class ListHttpHelper extends BaseAPI
{
    /**
     * 列表
     *
     * @param which       0-3 四列表
     *                    0,借支列表
     *                    1，请假列表
     *                    2，外出列表
     *                    3，日志列表
     *                    4,公司通知
     *                    5,日常会议
     * @param pageNumber
     * @param isSearch
     * @param mHttpHelper
     */
    public static void getFiveList(int which, String pageNumber, String search, String isSearch, SDHttpHelper mHttpHelper,
                                   boolean isShowPro,
                                   SDRequestCallBack callBack)
    {
        String url = "";
        if (which == 0)
        {
            url = HttpURLUtil.newInstance().append("loan/list").append(pageNumber).toString();
        } else if (which == 1)
        {
            url = HttpURLUtil.newInstance().append("holiday/queryList").toString();
        } else if (which == 2)
        {
            url = HttpURLUtil.newInstance().append("outWork/list").append(pageNumber).toString();
        } else if (which == 3)
        {
            url = HttpURLUtil.newInstance().append("workLog/list").append(pageNumber).toString();
        } else if (which == 4)
        {
            url = HttpURLUtil.newInstance().append("comNotice/list").append(pageNumber).toString();
        } else if (which == 5)
        {
            url = HttpURLUtil.newInstance().append("meet/list").append(pageNumber).toString();
        } else if (which == 6)//公司新闻
        {
            url = HttpURLUtil.newInstance().append("comNews/list").append(pageNumber).toString();
        }
        pairs.clear();
        if (which == 2)
        {
            if (!TextUtils.isEmpty(search))
            {
                pairs.add(new BasicNameValuePair("s_reason", search));
            }
        } else
        {
            if (!TextUtils.isEmpty(search))
            {
                pairs.add(new BasicNameValuePair("s_title", search));
            }
        }
        if (StringUtils.notEmpty(isSearch))
        {
            pairs.add(new BasicNameValuePair(Constants.IS_SEARCH_PARAM, Constants.IS_SEARCH_CONTENT));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        if (which == 1)
        {
            mHttpHelper.get(url, params, isShowPro, callBack);
        } else
        {
            mHttpHelper.post(url, params, isShowPro, callBack);
        }
    }


    /**
     * 0事务申请列表
     * 1项目协同列表
     *
     * @param pageNumber
     * @param isSearch
     * @param mHttpHelper
     * @param callBack
     */
    public static void getTwoList(int which, String pageNumber, String search, String isSearch, SDHttpHelper mHttpHelper,
                                  SDRequestCallBack callBack)
    {
        String url = "";
        if (which == 0)
        {
            url = HttpURLUtil.newInstance().append("affair/list").append(pageNumber).toString();
        } else if (which == 1)
        {
            url = HttpURLUtil.newInstance().append("project/list").append(pageNumber).toString();
        }
        pairs.clear();
        if (!TextUtils.isEmpty(search))
        {
            pairs.add(new BasicNameValuePair("s_title", search));
        }
        if (StringUtils.notEmpty(isSearch))
        {
            pairs.add(new BasicNameValuePair(Constants.IS_SEARCH_PARAM, Constants.IS_SEARCH_CONTENT));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    //日志添加
    public static void commitMyDaily(String title, String remark, String cc, String ygId, String annex,
                                     String eid, String ygDeptId, String ygDeptName, String ygJob, String ygName,
                                     String createTime, SDHttpHelper mHttpHelper, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("workLog").append("save").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(ygId))
        {
            pairs.add(new BasicNameValuePair("ygId", ygId));
        }
        if (!TextUtils.isEmpty(ygName))
        {
            pairs.add(new BasicNameValuePair("ygName", ygName));
        }
        if (!TextUtils.isEmpty(ygDeptId))
        {
            pairs.add(new BasicNameValuePair("ygDeptId", ygDeptId));
        }
        if (!TextUtils.isEmpty(createTime))
        {
            pairs.add(new BasicNameValuePair("createTime", createTime));
        }
        if (!TextUtils.isEmpty(ygDeptName))
        {
            pairs.add(new BasicNameValuePair("ygDeptName", ygDeptName));
        }
        if (!TextUtils.isEmpty(ygJob))
        {
            pairs.add(new BasicNameValuePair("ygJob", ygJob));
        }
        if (!TextUtils.isEmpty(title))
        {
            pairs.add(new BasicNameValuePair("title", title));
        }
        if (!TextUtils.isEmpty(remark))
        {
            pairs.add(new BasicNameValuePair("remark", remark));
        }
        if (!TextUtils.isEmpty(cc))
        {
            pairs.add(new BasicNameValuePair("cc", cc));
        }
        if (!TextUtils.isEmpty(annex))
        {
            pairs.add(new BasicNameValuePair("annex", annex));
        }
        if (!TextUtils.isEmpty(eid))
        {
            pairs.add(new BasicNameValuePair("eid", eid));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    //日志详情
    public static void getMyDaily(String eid, SDHttpHelper mHttpHelper, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("workLog").append("detail").append(eid).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.get(url, params, true, callBack);
    }


    //事务添加
    public static void commitAffair(String title, String remark, String cc, String ygId, String annex,
                                    String eid, String ygDeptId, String ygDeptName, String ygJob, String ygName,
                                    String approvalPerson, SDHttpHelper mHttpHelper, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("affair").append("save").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(ygId))
        {
            pairs.add(new BasicNameValuePair("ygId", ygId));
        }
        if (!TextUtils.isEmpty(ygName))
        {
            pairs.add(new BasicNameValuePair("ygName", ygName));
        }
        if (!TextUtils.isEmpty(ygDeptId))
        {
            pairs.add(new BasicNameValuePair("ygDeptId", ygDeptId));
        }
        if (!TextUtils.isEmpty(ygDeptName))
        {
            pairs.add(new BasicNameValuePair("ygDeptName", ygDeptName));
        }
        if (!TextUtils.isEmpty(ygJob))
        {
            pairs.add(new BasicNameValuePair("ygJob", ygJob));
        }
        if (!TextUtils.isEmpty(title))
        {
            pairs.add(new BasicNameValuePair("title", title));
        }
        if (!TextUtils.isEmpty(remark))
        {
            pairs.add(new BasicNameValuePair("remark", remark));
        }
        if (!TextUtils.isEmpty(approvalPerson))
        {
            pairs.add(new BasicNameValuePair("approvalPerson", approvalPerson));
        }
        if (!TextUtils.isEmpty(cc))
        {
            pairs.add(new BasicNameValuePair("cc", cc));
        }
        if (!TextUtils.isEmpty(annex))
        {
            pairs.add(new BasicNameValuePair("annex", annex));
        }
        if (!TextUtils.isEmpty(eid))
        {
            pairs.add(new BasicNameValuePair("eid", eid));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }


    //请假添加
    public static void commitLeave(String title, String remark, String cc, String ygId, String annex,
                                   String eid, String ygDeptId, String ygDeptName, String ygJob, String ygName,
                                   String approvalPerson, String startTime, String endTime,
                                   SDHttpHelper mHttpHelper, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("holiday").append("save").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(ygId))
        {
            pairs.add(new BasicNameValuePair("ygId", ygId));
        }
        if (!TextUtils.isEmpty(ygName))
        {
            pairs.add(new BasicNameValuePair("ygName", ygName));
        }
        if (!TextUtils.isEmpty(ygDeptId))
        {
            pairs.add(new BasicNameValuePair("ygDeptId", ygDeptId));
        }
        if (!TextUtils.isEmpty(ygDeptName))
        {
            pairs.add(new BasicNameValuePair("ygDeptName", ygDeptName));
        }
        if (!TextUtils.isEmpty(ygJob))
        {
            pairs.add(new BasicNameValuePair("ygJob", ygJob));
        }
        if (!TextUtils.isEmpty(title))
        {
            pairs.add(new BasicNameValuePair("title", title));
        }
        if (!TextUtils.isEmpty(remark))
        {
            pairs.add(new BasicNameValuePair("remark", remark));
        }
        if (!TextUtils.isEmpty(approvalPerson))
        {
            pairs.add(new BasicNameValuePair("approvalPerson", approvalPerson));
        }
        if (!TextUtils.isEmpty(startTime))
        {
            pairs.add(new BasicNameValuePair("startTime", startTime));
        }
        if (!TextUtils.isEmpty(endTime))
        {
            pairs.add(new BasicNameValuePair("endTime", endTime));
        }
        if (!TextUtils.isEmpty(cc))
        {
            pairs.add(new BasicNameValuePair("cc", cc));
        }
        if (!TextUtils.isEmpty(annex))
        {
            pairs.add(new BasicNameValuePair("annex", annex));
        }
        if (!TextUtils.isEmpty(eid))
        {
            pairs.add(new BasicNameValuePair("eid", eid));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }


    //外出添加
    public static void commitGoOut(String reason, String remark, String ygId, String annex,
                                   String eid, String ygDeptId, String ygDeptName, String ygJob, String ygName,
                                   String approvalPerson, String startTime, String endTime, String targetAddress,
                                   String vehicles, SDHttpHelper mHttpHelper, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("outWork").append("save").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(ygId))
        {
            pairs.add(new BasicNameValuePair("ygId", ygId));
        }
        if (!TextUtils.isEmpty(ygName))
        {
            pairs.add(new BasicNameValuePair("ygName", ygName));
        }
        if (!TextUtils.isEmpty(ygDeptId))
        {
            pairs.add(new BasicNameValuePair("ygDeptId", ygDeptId));
        }
        if (!TextUtils.isEmpty(ygDeptName))
        {
            pairs.add(new BasicNameValuePair("ygDeptName", ygDeptName));
        }
        if (!TextUtils.isEmpty(ygJob))
        {
            pairs.add(new BasicNameValuePair("ygJob", ygJob));
        }
        if (!TextUtils.isEmpty(reason))
        {
            pairs.add(new BasicNameValuePair("reason", reason));
        }
        if (!TextUtils.isEmpty(remark))
        {
            pairs.add(new BasicNameValuePair("remark", remark));
        }
        if (!TextUtils.isEmpty(targetAddress))
        {
            pairs.add(new BasicNameValuePair("targetAddress", targetAddress));
        }
        if (!TextUtils.isEmpty(vehicles))
        {
            pairs.add(new BasicNameValuePair("vehicles", vehicles));
        }
        if (!TextUtils.isEmpty(approvalPerson))
        {
            pairs.add(new BasicNameValuePair("approvalPerson", approvalPerson));
        }
        if (!TextUtils.isEmpty(startTime))
        {
            pairs.add(new BasicNameValuePair("startTime", startTime));
        }
        if (!TextUtils.isEmpty(endTime))
        {
            pairs.add(new BasicNameValuePair("endTime", endTime));
        }
        if (!TextUtils.isEmpty(annex))
        {
            pairs.add(new BasicNameValuePair("annex", annex));
        }
        if (!TextUtils.isEmpty(eid))
        {
            pairs.add(new BasicNameValuePair("eid", eid));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }


    //公司通知
    public static void commitNotify(String title, String remark, String ygId, String annex, String eid,
                                    String ygDeptId, String ygDeptName, String ygJob, String ygName,
                                    SDHttpHelper mHttpHelper, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("comNotice").append("save").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(ygId))
        {
            pairs.add(new BasicNameValuePair("ygId", ygId));
        }
        if (!TextUtils.isEmpty(ygName))
        {
            pairs.add(new BasicNameValuePair("ygName", ygName));
        }
        if (!TextUtils.isEmpty(ygDeptId))
        {
            pairs.add(new BasicNameValuePair("ygDeptId", ygDeptId));
        }
        if (!TextUtils.isEmpty(ygDeptName))
        {
            pairs.add(new BasicNameValuePair("ygDeptName", ygDeptName));
        }
        if (!TextUtils.isEmpty(ygJob))
        {
            pairs.add(new BasicNameValuePair("ygJob", ygJob));
        }
        if (!TextUtils.isEmpty(title))
        {
            pairs.add(new BasicNameValuePair("title", title));
        }
        if (!TextUtils.isEmpty(remark))
        {
            pairs.add(new BasicNameValuePair("remark", remark));
        }
        if (!TextUtils.isEmpty(annex))
        {
            pairs.add(new BasicNameValuePair("annex", annex));
        }
        if (!TextUtils.isEmpty(eid))
        {
            pairs.add(new BasicNameValuePair("eid", eid));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }


    //公司通知详情
    public static void getNotify(String eid, SDHttpHelper mHttpHelper, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("comNotice").append("detail").append(eid).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.get(url, params, true, callBack);
    }

    //公司通知详情
    public static void getNotifyDetail(Context context, String eid, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("comNotice").append("detail").append(eid).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    /**
     * 获取新同事消息数量
     *
     * @param callBack
     */
    public static void getNewNum(Context context, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("sysuser").append("new").append("num").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }


    /**
     * 获取新同事列表
     *
     * @param callBack
     */
    public static void getNewList(Context context, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("sysuser").append("new").append("list").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    /**
     * 新同事请求添加好友
     *
     * @param callBack status:1=同意，2=拒绝
     * @param pid      上级id
     */
    public static void postNewC(Context context, String pid, String eid, String status, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("sysuser").append("new").append("approval").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(pid))
        {
            pairs.add(new BasicNameValuePair("pid", pid));
        }
        if (!TextUtils.isEmpty(eid))
        {
            pairs.add(new BasicNameValuePair("eid", eid));
        }
        if (!TextUtils.isEmpty(eid))
        {
            pairs.add(new BasicNameValuePair("status", status));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }


    /**
     * 超级用户启用停用
     *
     * @param callBack status:1=启用，0=停用
     */
    public static void postSuperUserStatus(Context context, String userId, String status, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("sysuser").append("set").append("super").append("status").append(userId)
                .toString();
        pairs.clear();
        if (!TextUtils.isEmpty(status))
        {
            pairs.add(new BasicNameValuePair("status", status));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }


    /**
     * 系统用户启用停用
     *
     * @param callBack status:1=启用，0=停用
     */
    public static void postUserStatus(Context context, String userId, String status, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("sysuser").append("set").append("status").append(userId).toString();
        pairs.clear();
        if (!TextUtils.isEmpty(status))
        {
            pairs.add(new BasicNameValuePair("status", status));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    /**
     * 获取超级用户列表
     *
     * @param callBack
     */
    public static void getSuperList(Context context, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("sysuser").append("super").append("list").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }


    /**
     * 获取非超级用户列表
     *
     * @param callBack
     */
    public static void getNoSuperList(Context context, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("sysuser").append("notsuper").append("list").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    /**
     * 普通账号变成超级用户账号
     *
     * @param callBack
     */
    public static void postSuper(Context context, String userId, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("sysuser").append("set").append("super").append(userId).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    /**
     * 获取部门列表数据
     *
     * @param callBack
     */
    public static void getDepartList(Context context, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("sysdept").append("findDeptList").toString();

        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    /**
     * 查询部门名字是否存在
     *
     * @param callBack
     */
    public static void postExistDeptName(Context context, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("sysdept").append("existDeptName").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    /**
     * 添加部门
     *
     * @param callBack
     */
    public static void postSysdept(Context context, String departName, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("sysdept").append("save").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(departName))
        {
            pairs.add(new BasicNameValuePair("name", departName));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }


    /**
     * 获取超级配置数据
     *
     * @param callBack
     */
    public static void getConfigDetail(Context context, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("sysSetting").append("detail").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    /**
     * 提交超级配置数据
     *
     * @param callBack
     */
    public static void commitConfigDetail(Context context, String location, String isRead, String fingerprintLogin,
                                          SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("sysSetting").append("update").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(location))
        {
            pairs.add(new BasicNameValuePair("location", location));
        }
        if (!TextUtils.isEmpty(isRead))
        {
            pairs.add(new BasicNameValuePair("isRead", isRead));
        }
        if (!TextUtils.isEmpty(fingerprintLogin))
        {
            pairs.add(new BasicNameValuePair("fingerprintLogin", fingerprintLogin));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }


    /**
     * 获取上级人员列表
     *
     * @param callBack
     */
    public static void getUsersByCode(Context context, String userId, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("sysuser").append("getUsersByCode").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(userId))
        {
            pairs.add(new BasicNameValuePair("userId", userId));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    //日常会议
    public static void commitScheduleMeet(String title, String cc, String remark, String type,
                                          String meetPlace, String startTime, String endTime, String annex, String eid,
                                          SDHttpHelper mHttpHelper, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("meet").append("save").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(type))
        {
            pairs.add(new BasicNameValuePair("type", type));
        }
        if (!TextUtils.isEmpty(meetPlace))
        {
            pairs.add(new BasicNameValuePair("meetPlace", meetPlace));
        }
        if (!TextUtils.isEmpty(title))
        {
            pairs.add(new BasicNameValuePair("title", title));
        }
        if (!TextUtils.isEmpty(cc))
        {
            pairs.add(new BasicNameValuePair("cc", cc));
        }
        if (!TextUtils.isEmpty(remark))
        {
            pairs.add(new BasicNameValuePair("remark", remark));
        }
        if (!TextUtils.isEmpty(startTime))
        {
            pairs.add(new BasicNameValuePair("startTime", startTime));
        }
        if (!TextUtils.isEmpty(endTime))
        {
            pairs.add(new BasicNameValuePair("endTime", endTime));
        }
        if (!TextUtils.isEmpty(annex))
        {
            pairs.add(new BasicNameValuePair("annex", annex));
        }
        if (!TextUtils.isEmpty(eid))
        {
            pairs.add(new BasicNameValuePair("eid", eid));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    //日志详情
    public static void getSchedule(String eid, SDHttpHelper mHttpHelper, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("meet").append("detail").append(eid).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.get(url, params, true, callBack);
    }


    //休假
    public static void commitVacation(String userName, String leaveStart, String leaveEnd, String startType, String endType,
                                      String leaveType, String leaveMemo, String leaveReason, String availableDay, String minDay,
                                      SDHttpHelper mHttpHelper, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("holiday").append("save").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(userName))
        {
            pairs.add(new BasicNameValuePair("userName", userName));
        }
        if (!TextUtils.isEmpty(leaveStart))
        {
            pairs.add(new BasicNameValuePair("leaveStart", leaveStart));
        }
        if (!TextUtils.isEmpty(leaveEnd))
        {
            pairs.add(new BasicNameValuePair("leaveEnd", leaveEnd));
        }
        if (!TextUtils.isEmpty(startType))
        {
            pairs.add(new BasicNameValuePair("startType", startType));
        }
        if (!TextUtils.isEmpty(endType))
        {
            pairs.add(new BasicNameValuePair("endType", endType));
        }
        if (!TextUtils.isEmpty(leaveType))
        {
            pairs.add(new BasicNameValuePair("leaveType", leaveType));
        }
        if (!TextUtils.isEmpty(leaveMemo))
        {
            pairs.add(new BasicNameValuePair("leaveMemo", leaveMemo));
        }
        if (!TextUtils.isEmpty(leaveReason))
        {
            pairs.add(new BasicNameValuePair("leaveReason", leaveReason));
        }
        if (!TextUtils.isEmpty(availableDay))
        {
            pairs.add(new BasicNameValuePair("availableDay", availableDay));
        }
        if (!TextUtils.isEmpty(minDay))
        {
            pairs.add(new BasicNameValuePair("minDay", minDay));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    /**
     * 判断休假
     */
    public static void judgeDate(Context context, String leaveStart, String leaveEnd, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("holiday/check/conflict").toString();
        pairs.clear();
        if (StringUtils.notEmpty(leaveStart))
        {
            pairs.add(new BasicNameValuePair("leaveStart", leaveStart));
        }
        if (StringUtils.notEmpty(leaveEnd))
        {
            pairs.add(new BasicNameValuePair("leaveEnd", leaveEnd));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    //销假
    public static void commitReleave(String leaveDay, String leaveStart, String leaveEnd, String startType, String endType,
                                     String leaveType, String remark, String minDay, String eid,
                                     SDHttpHelper mHttpHelper, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("holiday/resumption/apply").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(leaveDay))
        {
            pairs.add(new BasicNameValuePair("leaveDay", leaveDay));
        }
        if (!TextUtils.isEmpty(leaveStart))
        {
            pairs.add(new BasicNameValuePair("leaveStart", leaveStart));
        }
        if (!TextUtils.isEmpty(leaveEnd))
        {
            pairs.add(new BasicNameValuePair("leaveEnd", leaveEnd));
        }
        if (!TextUtils.isEmpty(startType))
        {
            pairs.add(new BasicNameValuePair("startType", startType));
        }
        if (!TextUtils.isEmpty(endType))
        {
            pairs.add(new BasicNameValuePair("endType", endType));
        }
        if (!TextUtils.isEmpty(leaveType))
        {
            pairs.add(new BasicNameValuePair("leaveType", leaveType));
        }
        if (!TextUtils.isEmpty(minDay))
        {
            pairs.add(new BasicNameValuePair("minDay", minDay));
        }
        if (!TextUtils.isEmpty(remark))
        {
            pairs.add(new BasicNameValuePair("remark", remark));
        }
        if (!TextUtils.isEmpty(eid))
        {
            pairs.add(new BasicNameValuePair("eid", eid));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    //休假信息
    public static void getVacationInfo(SDHttpHelper mHttpHelper, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("holiday").append("myHoliday").toString();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.get(url, params, true, callBack);
    }

    //休假详情
    public static void getVacationDetail(String eid, SDHttpHelper mHttpHelper, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("holiday").append("detail").append(eid).toString();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.get(url, params, true, callBack);
    }

    /**
     * 获取自己的审批列表数据
     *
     * @param context
     * @param callBack
     */
    public static void getAllApproveDetail(Context context, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("holiday").append("getAllApproveDetail").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    /**
     * 获取自己的审批列表数据
     *
     * @param context
     * @param callBack
     */
    public static void getVacationApplyList(Context context, String signed, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("holiday/queryList").append(signed).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    public static void getResumptionList(Context context, String signed, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("holiday").append("resumption/self/list").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(signed))
        {
            pairs.add(new BasicNameValuePair("signed", signed));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getHasApproval(Context context, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("holiday/resumption/has/approve").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getHasSomeApproval(Context context, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("system/approve/right").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getApprovalList(Context context, String signed, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("holiday").append("resumption/approve/list").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(signed))
        {
            pairs.add(new BasicNameValuePair("signed", signed));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void commitLeaveApp(Context context, String applyId, String signed, String applyUser, String reason,
                                      SDRequestCallBack
                                              callBack)
    {
        String url = HttpURLUtil.newInstance().append("holiday/resumption/approve").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(applyId))
        {
            pairs.add(new BasicNameValuePair("applyId", applyId));
        }
        if (!TextUtils.isEmpty(signed))
        {
            pairs.add(new BasicNameValuePair("signed", signed));
        }
        if (!TextUtils.isEmpty(applyUser))
        {
            pairs.add(new BasicNameValuePair("applyUser", applyUser));
        }
        if (!TextUtils.isEmpty(reason))
        {
            pairs.add(new BasicNameValuePair("reason", reason));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }


    /**
     * 获取超级用户列表
     *
     * @param context
     * @param callBack
     */
    public static void getSysUserList(Context context, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("sysuser").append("list").append("manager").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    /**
     * 获取H5
     *
     * @param context
     * @param callBack
     */
    public static void getH5(Context context, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("out").append("fenbeitong").append("travel").append("url").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    public static void getInvestList(int type, Context context, String pageNumber, String s_projName, String s_induIds,
                                     String s_stageIds, String s_one, String s_two, SDRequestCallBack callBack)
    {
        String url = "";
        if (type == 0)
        {
            url = HttpURLUtil.newInstance().append("project").append("list").append(pageNumber).toString();
        } else if (type == 1)
        {
            url = HttpURLUtil.newInstance().append("project").append("report").append("list").append(pageNumber).toString();
        } else if (type == 2)
        {
            url = HttpURLUtil.newInstance().append("project").append("tmt").append("list").append(pageNumber).toString();
        } else if (type == 3)
        {
            url = HttpURLUtil.newInstance().append("project").append("potential").append("list").append("date").append
                    (pageNumber).toString();
        } else if (type == 4)
        {
            url = HttpURLUtil.newInstance().append("project").append("potential").append("list").append("unDate").append
                    (pageNumber).toString();
        } else if (type == 5)
        {
            url = HttpURLUtil.newInstance().append("project").append("potential").append("list").append("all").append
                    (pageNumber).toString();
        } else if (type == 6)
        {
            url = HttpURLUtil.newInstance().append("project/potential/influ/list").append
                    (pageNumber).toString();
        }
        pairs.clear();

        if (type == 0)
        {
            if (!TextUtils.isEmpty(s_projName))
            {
                pairs.add(new BasicNameValuePair("s_projName", s_projName));
            }
            if (!TextUtils.isEmpty(s_stageIds))
            {
                pairs.add(new BasicNameValuePair("s_stageIds", s_stageIds));
            }
            if (!TextUtils.isEmpty(s_induIds))
            {
                pairs.add(new BasicNameValuePair("s_induIds", s_induIds));
            }
        } else if (type == 1)
        {
            if (!TextUtils.isEmpty(s_projName))
            {
                pairs.add(new BasicNameValuePair("s_keyWord", s_projName));
            }
            if (!TextUtils.isEmpty(s_one))
            {
                pairs.add(new BasicNameValuePair("s_docName", s_one));
            }
            if (!TextUtils.isEmpty(s_two))
            {
                pairs.add(new BasicNameValuePair("s_summary", s_two));
            }
        } else if (type == 2)
        {
            if (!TextUtils.isEmpty(s_projName))
            {
                pairs.add(new BasicNameValuePair("s_projName", s_projName));
            }
        } else if (type == 3 || type == 4 || type == 5)
        {
            if (!TextUtils.isEmpty(s_projName))
            {
                pairs.add(new BasicNameValuePair("s_projName", s_projName));
            }
            if (!TextUtils.isEmpty(s_one))
            {
                pairs.add(new BasicNameValuePair("s_userName", s_one));
            }
            if (!TextUtils.isEmpty(s_induIds))
            {
                pairs.add(new BasicNameValuePair("s_indus", s_induIds));
            }
        } else if (type == 6)
        {
            if (!TextUtils.isEmpty(s_projName))
            {
                pairs.add(new BasicNameValuePair("s_projName", s_projName));
            }
            if (!TextUtils.isEmpty(s_induIds))
            {
                pairs.add(new BasicNameValuePair("s_indusType", s_induIds));
            }
            if (!TextUtils.isEmpty(s_one))
            {
                pairs.add(new BasicNameValuePair("s_projManager", s_one));
            }
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getStateList(Context context, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("project").append("filter").append("items").append("stage").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getIndustryList(Context context, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("project").append("filter").append("items").append("indu").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getInvestWayList(Context context, String eid, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("project").append("detail").append("invest").append(eid).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getAnnexList(Context context, String eid, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("project").append("detail")
                .append("annex").append(eid).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }


    public static void getBaseInfor(Context context, String eid, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("project").append("detail").append("base").append(eid).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getBaseHouseInfor(Context context, String eid, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("project").append("house")
                .append("detail").append(eid).append("base").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getTmtDetail(Context context, String eid, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("project").append("tmt").append("detail").append(eid).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getPEDetail(Context context, String eid, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("project").append("potential").append("detail").append(eid).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }


    /**
     * 获取会议列表
     *
     * @param context
     * @param eid
     * @param callBack
     */
    public static void getMettingList(Context context, String eid, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("project").append("opinion").append("list").append(eid).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    /**
     * 获取地产会议列表
     *
     * @param context
     * @param eid
     * @param callBack
     */
    public static void getEstateMettingList(Context context, String eid, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("project").append("house")
                .append("opinion").append("list").append(eid).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }


    public static void approvalMeet(Context context, String eid, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("project").append("detail").append("approve").append("opinion").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(eid))
        {
            pairs.add(new BasicNameValuePair("opinionId", eid));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    /**
     * 房地产项目审批
     *
     * @param context
     * @param eid
     * @param callBack
     */
    public static void approvalEstateMeet(Context context, String eid, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("project").append("detail")
                .append("house").append("approve").append("opinion").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(eid))
        {
            pairs.add(new BasicNameValuePair("opinionId", eid));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void approvalInvest(Context context, String eid, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("project").append("detail").append("approve").append("invest").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(eid))
        {
            pairs.add(new BasicNameValuePair("planId", eid));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    /**
     * 获取项目情况列表
     *
     * @param context
     * @param eid
     * @param callBack
     */
    public static void getProjectDetailList(Context context, String eid, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("project").append("detail").append("more").append(eid).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    /**
     * 获取地产项目情况列表
     *
     * @param context
     * @param callBack
     */
    public static void getProjectEstateList(Context context, String pageNumber, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("project").append("house").append("list").append(pageNumber).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    /**
     * 获取地产项目情况列表
     *
     * @param context
     * @param eid
     * @param callBack
     */
    public static void getEstateProjectDetailList(Context context, String eid, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("project")
                .append("house")
                .append("detail").append(eid).append("more").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    /**
     * 获取项目情况-- 资金投资列表
     *
     * @param context
     * @param eid
     * @param callBack
     */
    public static void getContractList(Context context, String eid, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("project").append("detail").append("contract").append(eid).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getExpressList(Context context, String pageNumber, boolean ishow, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("wx").append("bulletin").append("list").append(pageNumber).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, ishow, callBack);
    }

    //节目列表
    public static void getVotingList(Context context, String bid, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("annual").append("item").append("list").append(bid).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    //iStar
    public static void getIstarList(Context context, String bid, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("annual/item/iStar/list").append(bid).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    public static void commitVotingList(Context context, String meetId, String itemIds, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("annual").append("item").append("vote").append("items").append(meetId)
                .toString();
        pairs.clear();
        if (!TextUtils.isEmpty(itemIds))
        {
            pairs.add(new BasicNameValuePair("itemIds", itemIds));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    //iStar投票
    public static void commitIstarList(Context context, String meetId, String itemIds, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("annual/item/iStar/vote/items")
                .append(meetId)
                .toString();
        pairs.clear();
        if (!TextUtils.isEmpty(itemIds))
        {
            pairs.add(new BasicNameValuePair("itemIds", itemIds));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getPrize(Context context, String bid, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("annual").append("prize").append("list").append(bid).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    public static void getPrizeInfo(Context context, String eid, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("annual").append("prize").append("name").append("list").append(eid)
                .toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }


    public static void getMagazineList(Context context, String pageNumber, String s_title, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("magazine").append("list").append(pageNumber).toString();
        pairs.clear();
        if (!TextUtils.isEmpty(s_title))
        {
            pairs.add(new BasicNameValuePair("s_title", s_title));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getToolsList(Context context, String pageNumber, String s_title, int i_kind, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("tool").append("list").append(pageNumber).toString();
        pairs.clear();
        if (!TextUtils.isEmpty(s_title))
        {
            pairs.add(new BasicNameValuePair("s_title", s_title));
        }
        if (StringUtils.notEmpty(i_kind))
        {
            pairs.add(new BasicNameValuePair("i_kind", i_kind + ""));
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getMenuList(Context context, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("project").append("menu").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }


    //pe
    public static void commitPe(String projName, String invRound, String invContactStatus, String comIndus,
                                String invGroup, String region, String invFlowUp, String invDate, String userName,
                                String invStatus, String bizDesc, String eid, Context context, int importantInt,
                                SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("project").append("potential").append("save").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(projName))
        {
            pairs.add(new BasicNameValuePair("projName", projName));
        }
        if (!TextUtils.isEmpty(invRound))
        {
            pairs.add(new BasicNameValuePair("invRound", invRound));
        }
        if (!TextUtils.isEmpty(invContactStatus))
        {
            pairs.add(new BasicNameValuePair("invContactStatus", invContactStatus));
        }
        if (!TextUtils.isEmpty(comIndus))
        {
            pairs.add(new BasicNameValuePair("comIndus", comIndus));
        }
        if (!TextUtils.isEmpty(invGroup))
        {
            pairs.add(new BasicNameValuePair("invGroup", invGroup));
        }
        if (!TextUtils.isEmpty(region))
        {
            pairs.add(new BasicNameValuePair("region", region));
        }
        if (!TextUtils.isEmpty(invFlowUp))
        {
            pairs.add(new BasicNameValuePair("invFlowUp", invFlowUp));
        }
        if (!TextUtils.isEmpty(invDate))
        {
            pairs.add(new BasicNameValuePair("invDate", invDate));
        }
        if (!TextUtils.isEmpty(userName))
        {
            pairs.add(new BasicNameValuePair("userName", userName));
        }
        if (!TextUtils.isEmpty(invStatus))
        {
            pairs.add(new BasicNameValuePair("invStatus", invStatus));
        }
        if (!TextUtils.isEmpty(bizDesc))
        {
            pairs.add(new BasicNameValuePair("bizDesc", bizDesc));
        }
        if (!TextUtils.isEmpty(eid))
        {
            pairs.add(new BasicNameValuePair("eid", eid));
        }

        if (StringUtils.notEmpty((importantInt)))
        {
            pairs.add(new BasicNameValuePair("importantStatus", importantInt + ""));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getInduList(Context context, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("project").append("potential").append("indus").append("list").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    public static void getNewsLetterKey(Context context, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/get/newsLetter/industry/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    public static void getIndu2List(Context context, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("project/influ/filter/items/indus").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getFollowList(Context context, String pageNumber, String s_projId, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("project").append("potential").append("follow").append("list").append
                (pageNumber).toString();
        pairs.clear();
        if (!TextUtils.isEmpty(s_projId))
        {
            pairs.add(new BasicNameValuePair("s_projId", s_projId));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void commitState(Context context, String invContactStatus, String eid, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("project").append("potential").append("contactstatus").append("update")
                .toString();
        pairs.clear();
        if (!TextUtils.isEmpty(invContactStatus))
        {
            pairs.add(new BasicNameValuePair("invContactStatus", invContactStatus));
        }
        if (!TextUtils.isEmpty(eid))
        {
            pairs.add(new BasicNameValuePair("eid", eid));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }


    public static void commitFollow(Context context, String projId, String devDate, String invFlowUp,
                                    String keyNote, String eid, String followPerson, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("project").append("potential").append("follow").append("save").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(projId))
        {
            pairs.add(new BasicNameValuePair("projId", projId));
        }
        if (!TextUtils.isEmpty(devDate))
        {
            pairs.add(new BasicNameValuePair("devDate", devDate));
        }
        if (!TextUtils.isEmpty(invFlowUp))
        {
            pairs.add(new BasicNameValuePair("invFlowUp", invFlowUp));
        }
        if (!TextUtils.isEmpty(keyNote))
        {
            pairs.add(new BasicNameValuePair("keyNote", keyNote));
        }
        if (!TextUtils.isEmpty(followPerson))
        {
            pairs.add(new BasicNameValuePair("followPerson", followPerson));
        }
        if (!TextUtils.isEmpty(eid))
        {
            pairs.add(new BasicNameValuePair("eid", eid));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void deleteFollow(Context context, String eid, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("project").append("potential").append("follow").append("delete").append
                (eid).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }


    public static void getExpenseList(Context context, String pageNumber, String s_title, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("cost/approve").append("list").append(pageNumber).toString();
        pairs.clear();
        if (!TextUtils.isEmpty(s_title))
        {
            pairs.add(new BasicNameValuePair("s_title", s_title));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getExpenseApprovalList(Context context, String pageNumber, String s_title, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("cost").append("list").append(pageNumber).toString();
        pairs.clear();
        if (!TextUtils.isEmpty(s_title))
        {
            pairs.add(new BasicNameValuePair("s_title", s_title));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }


    public static void getExpenseDetail(Context context, String eid, String urlEndString, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("cost/detail").append(urlEndString).toString();
        pairs.clear();
        if (!TextUtils.isEmpty(eid))
        {
            pairs.add(new BasicNameValuePair("eid", eid));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getDDFeeDetail(Context context, String eid, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("/cost/detail/df").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(eid))
        {
            pairs.add(new BasicNameValuePair("eid", eid));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getExpensePayDetail(Context context, String eid, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("cost/detail/rp").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(eid))
        {
            pairs.add(new BasicNameValuePair("eid", eid));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getPayDetail(Context context, String eid, String urlEndString, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("cost/detail").append(urlEndString).toString();
        pairs.clear();
        if (!TextUtils.isEmpty(eid))
        {
            pairs.add(new BasicNameValuePair("eid", eid));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getLoanDetail(Context context, String eid, String urlEndString, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("cost/detail").append(urlEndString).toString();
        pairs.clear();
        if (!TextUtils.isEmpty(eid))
        {
            pairs.add(new BasicNameValuePair("eid", eid));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void approvalBill(Context context, String oaItemType, String affairId, String userName,
                                    String comments, SDRequestCallBack callBack)
    {
        userName = DisplayUtil.getUserInfo(context, 11);
        String url = HttpURLUtil.newInstance().append("iceforce/post/approveform").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(oaItemType))
        {
            pairs.add(new BasicNameValuePair("oaItemType", oaItemType));
        }
        if (!TextUtils.isEmpty(affairId))
        {
            pairs.add(new BasicNameValuePair("affairId", affairId));
        }
        if (!TextUtils.isEmpty(userName))
        {
            pairs.add(new BasicNameValuePair("userName", userName));
        }
        if (!TextUtils.isEmpty(comments))
        {
            pairs.add(new BasicNameValuePair("comments", comments));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void approvalDisAgreeBill(Context context, String oaItemType, String subObjId, String sendUser,
                                            String comments, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/cancelform").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(oaItemType))
        {
            pairs.add(new BasicNameValuePair("oaItemType", oaItemType));
        }
        if (!TextUtils.isEmpty(subObjId))
        {
            pairs.add(new BasicNameValuePair("subObjId", subObjId));
        }
        if (!TextUtils.isEmpty(sendUser))
        {
            pairs.add(new BasicNameValuePair("sendUser", sendUser));
        }
        if (!TextUtils.isEmpty(comments))
        {
            pairs.add(new BasicNameValuePair("comments", comments));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    //判断下载权限
    public static void judgeAnnexPower(Context context, String projId, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("/project/detail/annex/download/right").toString();
        pairs.clear();
        if (StringUtils.notEmpty(projId))
        {
            pairs.add(new BasicNameValuePair("projId", projId));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    /**
     * @param context
     * @param callBack
     */
    public static void getAccountList(Context context, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("/project/influ/filter/items/managers").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void postAddTrip(Context context, String jsonString, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("travel/save").toString();
        pairs.clear();
        pairs = JsonUtils.getInstance().reTurnPair(jsonString);
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getCityTrip(Context context, String jsonString, boolean isShowProgress, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("travel/cityList").toString();
        pairs.clear();
//        pairs = JsonUtils.getInstance().reTurnPair(jsonString);
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, isShowProgress, callBack);
    }

    /**
     * 我的出差-审批中
     *
     * @param context
     * @param callBack
     */
    public static void postTravelApplyList(Context context, int page, String isApprove, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("travel").append("list").append(page + "").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (StringUtils.notEmpty(isApprove))
            params.addBodyParameter("isApprove", isApprove);
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    /**
     * 我的出差-审批中
     *
     * @param context
     * @param callBack
     */
    public static void postTravelApprovalList(Context context, int page, String search, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("travel/approveList").append(page + "").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (StringUtils.notEmpty(search))
            params.addBodyParameter("search", search);
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    /**
     * 查询联想词
     *
     * @param context
     * @param callBack
     */
    public static void getQueryHintList(Context context, int page, String keyWork, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("/project/queryHint/list").append(page + "").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (StringUtils.notEmpty(keyWork))
            params.addBodyParameter("s_projName", keyWork);
        else
            return;
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }


    /**
     * 获取不良资产情况列表
     *
     * @param context
     * @param callBack
     */
    public static void getBadAssetsList(Context context, String s_projName, String pageNumber, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("badAssets").append("list").append(pageNumber).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(s_projName))
        {
            params.addBodyParameter("s_projName", s_projName);
        }
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }


    /**
     * 获取不良资产情况跟进情况
     *
     * @param context
     * @param callBack
     */
    public static void getFollowUpList(Context context, String projId, String pageNumber, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("badAssets").append("progress")
                .append("list")
                .append(projId)
                .append(pageNumber).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    /**
     * 获取系统消息列表列表
     *
     * @param context
     */
    public static void getSystemMsgList(Context context, String pageNumber, boolean isShow, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("systemMessage")
                .append("list").append(pageNumber).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, isShow, callBack);
    }


    /**
     * 获取NewsLetter列表
     *
     * @param context
     * @param callBack
     */
    public static void getNewsLetterList(Context context, String pageNumber, String groupId, String docName, boolean isShow,
                                         SDRequestCallBack
                                                 callBack)
    {
        String url = HttpURLUtil.newInstance().append("news/letter/list").append(pageNumber).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(groupId))
        {
            params.addBodyParameter("groupId", groupId);
        }

        if (!TextUtils.isEmpty(docName))
        {
            params.addBodyParameter("docName", docName);
        }
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, isShow, callBack);
    }


    /**
     * iceforce首页跟进项目进展列表
     *
     * @param context
     * @param username
     * @param pageNo
     * @param pageSize
     * @param callBack
     */
    public static void getFollowProjectList(Context context, String username, String pageNo, String pageSize,
                                            String followUpStatus, String comIndu, String isMyFollow, String dateRange,
                                            SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/sns/follow/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        if (!TextUtils.isEmpty(pageNo))
        {
            params.addBodyParameter("pageNo", pageNo);
        }

        if (!TextUtils.isEmpty(pageSize))
        {
            params.addBodyParameter("pageSize", pageSize);
        }

        if (!TextUtils.isEmpty(followUpStatus))
        {
            params.addBodyParameter("followUpStatus", followUpStatus);
        }

        if (!TextUtils.isEmpty(comIndu))
        {
            params.addBodyParameter("comIndu", comIndu);
        }

        if (!TextUtils.isEmpty(isMyFollow))
        {
            params.addBodyParameter("isMyFollow", isMyFollow);
        }

        if (!TextUtils.isEmpty(dateRange))
        {
            params.addBodyParameter("dateRange", dateRange);
        }

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getFollowOnDetailList(Context context, String username, String pageNo, String pageSize,
                                             String currentOrHistory, String projId,
                                             SDRequestCallBack callBack)

    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/sns/follow/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        if (!TextUtils.isEmpty(pageNo))
        {
            params.addBodyParameter("pageNo", pageNo);
        }

        if (!TextUtils.isEmpty(pageSize))
        {
            params.addBodyParameter("pageSize", pageSize);
        }

        if (!TextUtils.isEmpty(currentOrHistory))
        {
            params.addBodyParameter("currentOrHistory", currentOrHistory);
        }

        if (!TextUtils.isEmpty(projId))
        {
            params.addBodyParameter("projId", projId);
        }

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    /**
     * 根据项目id查跟踪进展
     *
     * @param context
     * @param username
     * @param projId
     * @param pageNo
     * @param pageSize
     * @param callBack
     */
    public static void getFollowProjectListByProjId(Context context, String username, String projId, String pageNo, String
            pageSize, SDRequestCallBack
                                                            callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/proj/notes/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        if (!TextUtils.isEmpty(projId))
        {
            params.addBodyParameter("projId", projId);
        }

        if (!TextUtils.isEmpty(pageNo))
        {
            params.addBodyParameter("pageNo", pageNo);
        }

        if (!TextUtils.isEmpty(pageSize))
        {
            params.addBodyParameter("pageSize", pageSize);
        }

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    /**
     * iceforce首页待办事项
     *
     * @param context
     * @param username
     * @param pageNo
     * @param pageSize
     * @param callBack
     */
    public static void getToDoEventList(Context context, String username, String matterType, String pageNo, String pageSize,
                                        SDRequestCallBack
                                                callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/msg/list/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        if (!TextUtils.isEmpty(matterType))
        {
            params.addBodyParameter("matterType", matterType);
        }

        if (!TextUtils.isEmpty(pageNo))
        {
            params.addBodyParameter("pageNo", pageNo);
        } else
        {
            params.addBodyParameter("pageNo", "1");
        }

        if (!TextUtils.isEmpty(pageSize))
        {
            params.addBodyParameter("pageSize", pageSize);
        } else
        {
            params.addBodyParameter("pageSize", "5");
        }

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, false, callBack);
    }

    /**
     * 潜在项目->个人列表
     *
     * @param context
     * @param username
     * @param projType
     * @param pageNo
     * @param pageSize
     * @param projName
     * @param indusGroup
     * @param projManagers
     * @param comIndu
     * @param projInvestedStatus
     * @param stsId
     * @param callBack
     */
    public static void getPotentialPersonalList(Context context, String username, String projType, String pageNo, String
            pageSize, String projName, String indusGroup,
                                                String projManagers, String comIndu, String projInvestedStatus, String stsId,
                                                SDRequestCallBack
                                                        callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/proj/query/myproj").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        if (!TextUtils.isEmpty(projType))
        {
            params.addBodyParameter("projType", projType);
        }

        if (!TextUtils.isEmpty(pageNo))
        {
            params.addBodyParameter("pageNo", pageNo);
        }

        if (!TextUtils.isEmpty(pageSize))
        {
            params.addBodyParameter("pageSize", pageSize);
        }

        if (!TextUtils.isEmpty(projName))
        {
            params.addBodyParameter("projName", projName);
        }

        if (!TextUtils.isEmpty(indusGroup))
        {
            params.addBodyParameter("indusGroup", indusGroup);
        }

        if (!TextUtils.isEmpty(projManagers))
        {
            params.addBodyParameter("projManagers", projManagers);
        }

        if (!TextUtils.isEmpty(comIndu))
        {
            params.addBodyParameter("comIndu", comIndu);
        }

        if (!TextUtils.isEmpty(projInvestedStatus))
        {
            params.addBodyParameter("projInvestedStatus", projInvestedStatus);
        }

        if (!TextUtils.isEmpty(stsId))
        {
            params.addBodyParameter("stsId", stsId);
        }

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    //VC组潜在,跟进列表接口
    public static void getPotentialPersonalListForVCGroup(Context context, String username, String pageNo,
                                                          String pageSize, String projName, String startDate, String endDate,
                                                          String stsId, String comIndu, String vcSts, String dateRange,
                                                          SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/proj/query/vcProj").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }


        if (!TextUtils.isEmpty(pageNo))
        {
            params.addBodyParameter("pageNo", pageNo);
        }

        if (!TextUtils.isEmpty(pageSize))
        {
            params.addBodyParameter("pageSize", pageSize);
        }

        if (!TextUtils.isEmpty(projName))
        {
            params.addBodyParameter("projName", projName);
        }

        if (!TextUtils.isEmpty(startDate))
        {
            params.addBodyParameter("startDate", startDate);
        }

        if (!TextUtils.isEmpty(endDate))
        {
            params.addBodyParameter("endDate", endDate);
        }

        if (!TextUtils.isEmpty(stsId))
        {
            params.addBodyParameter("stsId", stsId);
        }

        if (!TextUtils.isEmpty(comIndu))
        {
            params.addBodyParameter("comIndu", comIndu);
        }

        if (!TextUtils.isEmpty(vcSts))
        {
            params.addBodyParameter("vcSts", vcSts);
        }


        if (!TextUtils.isEmpty(dateRange))
        {
            params.addBodyParameter("dateRange", dateRange);
        }


        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getProjectLibraryList(Context context, String username, String projType, String pageNo, String pageSize,
                                             String projName, String indusGroup,
                                             String projManagers, String comIndu, String projInvestedStatus, String stsId,
                                             String projQueryStr, String teamQueryStr, String queryStr,
                                             SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/proj/query/allproj").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        if (!TextUtils.isEmpty(projType))
        {
            params.addBodyParameter("projType", projType);
        }

        if (!TextUtils.isEmpty(pageNo))
        {
            params.addBodyParameter("pageNo", pageNo);
        }

        if (!TextUtils.isEmpty(pageSize))
        {
            params.addBodyParameter("pageSize", pageSize);
        }

        if (!TextUtils.isEmpty(projName))
        {
            params.addBodyParameter("projName", projName);
        }

        if (!TextUtils.isEmpty(indusGroup))
        {
            params.addBodyParameter("indusGroup", indusGroup);
        }

        if (!TextUtils.isEmpty(projManagers))
        {
            params.addBodyParameter("projManagers", projManagers);
        }

        if (!TextUtils.isEmpty(comIndu))
        {
            params.addBodyParameter("comIndu", comIndu);
        }

        if (!TextUtils.isEmpty(projInvestedStatus))
        {
            params.addBodyParameter("projInvestedStatus", projInvestedStatus);
        }

        if (!TextUtils.isEmpty(stsId))
        {
            params.addBodyParameter("stsId", stsId);
        }

        if (!TextUtils.isEmpty(projQueryStr))
        {
            params.addBodyParameter("projQueryStr", projQueryStr);
        }

        if (!TextUtils.isEmpty(teamQueryStr))
        {
            params.addBodyParameter("teamQueryStr", teamQueryStr);
        }

        if (!TextUtils.isEmpty(queryStr))
        {
            params.addBodyParameter("queryStr", queryStr);
        }

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getWaitingToSeeProjectList(Context context, String username, String pageNo,
                                                  String pageSize, String tag, String projName, String startDate, String endDate,
                                                  String stsId, String vcSts, String dateRange, String comIndu, String
                                                          isAllocation,
                                                  SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/proj/query/peProj").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }


        if (!TextUtils.isEmpty(pageNo))
        {
            params.addBodyParameter("pageNo", pageNo);
        }

        if (!TextUtils.isEmpty(pageSize))
        {
            params.addBodyParameter("pageSize", pageSize);
        }

        if (!TextUtils.isEmpty(tag))
        {
            params.addBodyParameter("tag", tag);
        }

        if (!TextUtils.isEmpty(projName))
        {
            params.addBodyParameter("projName", projName);
        }

        if (!TextUtils.isEmpty(startDate))
        {
            params.addBodyParameter("startDate", startDate);
        }

        if (!TextUtils.isEmpty(endDate))
        {
            params.addBodyParameter("endDate", endDate);
        }

        if (!TextUtils.isEmpty(stsId))
        {
            params.addBodyParameter("stsId", stsId);
        }

        if (!TextUtils.isEmpty(vcSts))
        {
            params.addBodyParameter("vcSts", vcSts);
        }

        if (!TextUtils.isEmpty(dateRange))
        {
            params.addBodyParameter("dateRange", dateRange);
        }

        if (!TextUtils.isEmpty(comIndu))
        {
            params.addBodyParameter("comIndu", comIndu);
        }


        if (!TextUtils.isEmpty(isAllocation))
        {
            params.addBodyParameter("isAllocation", isAllocation);
        }


        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }


    public static void saveWaitingToSeeUserListData(Context context, String username, String projId, String userIds,
                                                    SDRequestCallBack
                                                            callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/get/proj/update/manager.json?username=" + username + "&projId="
                + projId + "&userIds=" + userIds).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        /*if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        if (!TextUtils.isEmpty(userIds))
        {
            params.addBodyParameter("userIds", userIds);
        }

        if (!TextUtils.isEmpty(projId))
        {
            params.addBodyParameter("projId", projId);
        }*/

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    /**
     * 潜在项目->小组列表
     *
     * @param context
     * @param username
     * @param projType
     * @param pageNo
     * @param pageSize
     * @param projName
     * @param indusGroup
     * @param projManagers
     * @param comIndu
     * @param projInvestedStatus
     * @param stsId
     * @param callBack
     */
    public static void getPotentialGroupList(Context context, String username, String projType, String pageNo, String pageSize,
                                             String projName, String indusGroup,
                                             String projManagers, String comIndu, String projInvestedStatus, String stsId,
                                             SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/proj/query/teamproj").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        if (!TextUtils.isEmpty(projType))
        {
            params.addBodyParameter("projType", projType);
        }

        if (!TextUtils.isEmpty(pageNo))
        {
            params.addBodyParameter("pageNo", pageNo);
        }

        if (!TextUtils.isEmpty(pageSize))
        {
            params.addBodyParameter("pageSize", pageSize);
        }

        if (!TextUtils.isEmpty(projName))
        {
            params.addBodyParameter("projName", projName);
        }

        if (!TextUtils.isEmpty(indusGroup))
        {
            params.addBodyParameter("indusGroup", indusGroup);
        }

        if (!TextUtils.isEmpty(projManagers))
        {
            params.addBodyParameter("projManagers", projManagers);
        }

        if (!TextUtils.isEmpty(comIndu))
        {
            params.addBodyParameter("comIndu", comIndu);
        }

        if (!TextUtils.isEmpty(projInvestedStatus))
        {
            params.addBodyParameter("projInvestedStatus", projInvestedStatus);

        }

        if (!TextUtils.isEmpty(stsId))
        {
            params.addBodyParameter("stsId", stsId);

        }

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getPotentialProjectsDetailList(Context context, String username, String projId, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/get/proj/query/detail.json?projId=" + projId + "&username=" +
                username).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    /**
     * APP基础数据
     *
     * @param context
     * @param type
     * @param callBack
     */
    public static void getIDGBaseData(Context context, String type, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/get/syscode/queryByType/holder.json?type=" + type).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    public static void getFollowonStatusBaseData(Context context, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/get/status/allFollowonStatus/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    public static void updateFollowStateData(Context context, String username, String projId, SDRequestCallBack
            callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/get/proj/changeVcFollowStatus/holder.json?username=" + username
                + "&projId=" + projId + "&status=" + "1").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        /*if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }


            params.addBodyParameter("status", "1");


        if (!TextUtils.isEmpty(projId))
        {
            params.addBodyParameter("projId", projId);
        }*/

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    public static void updateProjectStateData(Context context, String username, String projId, String stsId, SDRequestCallBack
            callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/proj/changeStatus/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        if (!TextUtils.isEmpty(stsId))
        {
            params.addBodyParameter("stsId", stsId);
        }

        if (!TextUtils.isEmpty(projId))
        {
            params.addBodyParameter("projId", projId);
        }

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void updateProjectStateForPEData(Context context, String username, String projId, String stsId,
                                                   SDRequestCallBack
                                                           callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/proj/changePEStatus/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        if (!TextUtils.isEmpty(stsId))
        {
            params.addBodyParameter("stsId", stsId);
        }

        if (!TextUtils.isEmpty(projId))
        {
            params.addBodyParameter("projId", projId);
        }

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getGroupListData(Context context, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/get/dept/queryIndusDept/holder.json").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    public static void getGroupListDataNew(Context context, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/get/dept/queryIndusDeptNew/holder.json").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    public static void getItemRatingData(Context context, String username, String projId, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/sns/score/proj").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        if (!TextUtils.isEmpty(projId))
        {
            params.addBodyParameter("projId", projId);
        }

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void sendItemRatingScoreData(Context context, String rateId, String teamScore, String businessScore, String
            comment, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/sns/score/projadd").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(rateId))
        {
            params.addBodyParameter("rateId", rateId);
        }

        if (!TextUtils.isEmpty(teamScore))
        {
            params.addBodyParameter("teamScore", teamScore);
        }

        if (!TextUtils.isEmpty(businessScore))
        {
            params.addBodyParameter("businessScore", businessScore);
        }

        if (!TextUtils.isEmpty(comment))
        {
            params.addBodyParameter("comment", comment);
        }

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void sendItemScoringData(Context context, String username, String projId, String discussion, String date,
                                           String projScoreList, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/sns/score/addList").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        if (!TextUtils.isEmpty(projId))
        {
            params.addBodyParameter("projId", projId);
        }

        if (!TextUtils.isEmpty(discussion))
        {
            params.addBodyParameter("discussion", discussion);
        }

        if (!TextUtils.isEmpty(date))
        {
            params.addBodyParameter("date", date);
        }

        if (!TextUtils.isEmpty(projScoreList))
        {
            params.addBodyParameter("projScoreList", projScoreList);
        }


        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void sendAtData(Context context, String username, String projId, String projName, String comments,
                                  String recommendUsers, String followOnUsers, String scoreUsers, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/sns/at/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        if (!TextUtils.isEmpty(projId))
        {
            params.addBodyParameter("projId", projId);
        }

        if (!TextUtils.isEmpty(projName))
        {
            params.addBodyParameter("projName", projName);
        }

        if (!TextUtils.isEmpty(comments))
        {
            params.addBodyParameter("comments", comments);
        }

        if (!TextUtils.isEmpty(recommendUsers))
        {
            params.addBodyParameter("recommendUsers", recommendUsers);
        }

        if (!TextUtils.isEmpty(followOnUsers))
        {
            params.addBodyParameter("followOnUsers", followOnUsers);
        }

        if (!TextUtils.isEmpty(scoreUsers))
        {
            params.addBodyParameter("scoreUsers", scoreUsers);
        }

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getPublicUserListData(Context context, String userName, String type, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/user/queryByUserName/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();

        params.addBodyParameter("userName", userName);
        if (StringUtils.notEmpty(type))
            params.addBodyParameter("type", type);

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, false, callBack);
    }

    public static void getPublicAtUserListData(Context context, String userName, String queryUser, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/user/queryUserForAt/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();

        params.addBodyParameter("userName", userName);

        params.addBodyParameter("queryUser", queryUser);

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, false, callBack);
    }

    public static void sendPotentialProjectsAddData(Context context, String username, String projName, String stsId, String
            zhDesc,
                                                    String indusGroup, String planInvAmount, String teamDesc, String voiceUrl,
                                                    String projManagers, String projTeams, String audioTime, String isPrivacy,
                                                    String followOnUsers, String followUpStatus,
                                                    SDRequestCallBack callBack)

    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/proj/add/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        if (!TextUtils.isEmpty(projName))
        {
            params.addBodyParameter("projName", projName);
        }

        if (!TextUtils.isEmpty(stsId))
        {
            params.addBodyParameter("stsId", stsId);
        }

        if (!TextUtils.isEmpty(zhDesc))
        {
            params.addBodyParameter("zhDesc", zhDesc);
        }

        if (!TextUtils.isEmpty(indusGroup))
        {
            params.addBodyParameter("indusGroup", indusGroup);
        }

        if (!TextUtils.isEmpty(planInvAmount))
        {
            params.addBodyParameter("planInvAmount", planInvAmount);
        }

        if (!TextUtils.isEmpty(teamDesc))
        {
            params.addBodyParameter("teamDesc", teamDesc);
        }

        if (!TextUtils.isEmpty(voiceUrl))
        {
            params.addBodyParameter("voiceUrl", voiceUrl);
        }

        if (!TextUtils.isEmpty(projManagers))
        {
            params.addBodyParameter("projManagers", projManagers);
        }

        if (!TextUtils.isEmpty(projTeams))
        {
            params.addBodyParameter("projTeams", projTeams);
        }

        if (!TextUtils.isEmpty(audioTime))
        {
            params.addBodyParameter("audioTime", audioTime);
        }

        if (!TextUtils.isEmpty(isPrivacy))
        {
            params.addBodyParameter("isPrivacy", isPrivacy);
        }

        if (!TextUtils.isEmpty(followOnUsers))
        {
            params.addBodyParameter("followOnUsers", followOnUsers);
        }

        params.addBodyParameter("followUpStatus", followUpStatus);
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void sendPotentialProjectsAddForVCData(Context context, String username, String projName, String stsId,
                                                         String zhDesc,
                                                         String indusGroup, String planInvAmount, String teamDesc, String
                                                                 voiceUrl,
                                                         String projManagers, String projTeams, String audioTime, /*String
                                                         isPrivacy,
                                                    String followOnUsers, String followUpStatus,*/String discussion, String date,
                                                         String projScoreList,
                                                         SDRequestCallBack callBack)

    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/proj/addForVCPartner/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        if (!TextUtils.isEmpty(projName))
        {
            params.addBodyParameter("projName", projName);
        }

        if (!TextUtils.isEmpty(stsId))
        {
            params.addBodyParameter("stsId", stsId);
        }

        if (!TextUtils.isEmpty(zhDesc))
        {
            params.addBodyParameter("zhDesc", zhDesc);
        }

        if (!TextUtils.isEmpty(indusGroup))
        {
            params.addBodyParameter("indusGroup", indusGroup);
        }

        if (!TextUtils.isEmpty(planInvAmount))
        {
            params.addBodyParameter("planInvAmount", planInvAmount);
        }

        if (!TextUtils.isEmpty(teamDesc))
        {
            params.addBodyParameter("teamDesc", teamDesc);
        }

        if (!TextUtils.isEmpty(voiceUrl))
        {
            params.addBodyParameter("voiceUrl", voiceUrl);
        }

        if (!TextUtils.isEmpty(projManagers))
        {
            params.addBodyParameter("projManagers", projManagers);
        }

        if (!TextUtils.isEmpty(projTeams))
        {
            params.addBodyParameter("projTeams", projTeams);
        }

        if (!TextUtils.isEmpty(audioTime))
        {
            params.addBodyParameter("audioTime", audioTime);
        }

        /*if (!TextUtils.isEmpty(isPrivacy))
        {
            params.addBodyParameter("isPrivacy", isPrivacy);
        }

        if (!TextUtils.isEmpty(followOnUsers))
        {
            params.addBodyParameter("followOnUsers", followOnUsers);
        }

        params.addBodyParameter("followUpStatus", followUpStatus);*/

        if (!TextUtils.isEmpty(date))
        {
            params.addBodyParameter("date", date);
        }

        if (!TextUtils.isEmpty(discussion))
        {
            params.addBodyParameter("discussion", discussion);
        }

        if (!TextUtils.isEmpty(projScoreList))
        {
            params.addBodyParameter("projScoreList", projScoreList);
        }

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getIndustryListData(Context context, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/get/syscode/queryByType/holder.json?type=induType").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    public static void getThemeListData(Context context, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/get/tag/queryPeTag/holder.json").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    public static void getIndustryListForFinancingData(Context context, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/get/financing/industryList/holder.json").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    public static void getImportantInstitutionData(Context context, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/get/financing/keyAgency/holder.json").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    public static void updatePotentialProjectsEditData(Context context, String username, String projId, String projName, String
            stsId, String zhDesc,
                                                       String indusGroup, String planInvAmount, String teamDesc, String
                                                               comIndus, String projManagers, String projTeams,
                                                       SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/proj/update/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        if (!TextUtils.isEmpty(projId))
        {
            params.addBodyParameter("projId", projId);
        }

        if (!TextUtils.isEmpty(projName))
        {
            params.addBodyParameter("projName", projName);
        }

        if (!TextUtils.isEmpty(stsId))
        {
            params.addBodyParameter("stsId", stsId);
        }

        if (!TextUtils.isEmpty(zhDesc))
        {
            params.addBodyParameter("zhDesc", zhDesc);
        }

        if (!TextUtils.isEmpty(indusGroup))
        {
            params.addBodyParameter("indusGroup", indusGroup);
        }

        if (!TextUtils.isEmpty(planInvAmount))
        {
            params.addBodyParameter("planInvAmount", planInvAmount);
        }

        if (!TextUtils.isEmpty(teamDesc))
        {
            params.addBodyParameter("teamDesc", teamDesc);
        }

        if (!TextUtils.isEmpty(comIndus))
        {
            params.addBodyParameter("comIndu", comIndus);
        }

        if (!TextUtils.isEmpty(projManagers))
        {
            params.addBodyParameter("projManagers", projManagers);
        }

        if (!TextUtils.isEmpty(projTeams))
        {
            params.addBodyParameter("projTeams", projTeams);
        }

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getTrackProgessProjectNameListData(Context context, String username, String projName, SDRequestCallBack
            callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/proj/query/selectList").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        if (!TextUtils.isEmpty(projName))
        {
            params.addBodyParameter("projName", projName);
        }
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void sendTrackProgressAddData(Context context, String createBy, String projId, String note, String noteType,
                                                String audioTime, String validDate, String status,
                                                String origBusId, String busType, String applyUser, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/proj/noteadd/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();

        if (!TextUtils.isEmpty(createBy))
        {
            params.addBodyParameter("createBy", createBy);
        }

        if (!TextUtils.isEmpty(projId))
        {
            params.addBodyParameter("projId", projId);
        }

        if (!TextUtils.isEmpty(note))
        {
            params.addBodyParameter("note", note);
        }

        if (!TextUtils.isEmpty(noteType))
        {
            params.addBodyParameter("noteType", noteType);
        }

        if (!TextUtils.isEmpty(audioTime))
        {
            params.addBodyParameter("audioTime", audioTime);
        }

        if (!TextUtils.isEmpty(validDate))
        {
            params.addBodyParameter("validDate", validDate);
        }

        if (!TextUtils.isEmpty(status))
        {
            params.addBodyParameter("status", status);
        }

        if (!TextUtils.isEmpty(origBusId))
        {
            params.addBodyParameter("origBusId", origBusId);
        }

        if (!TextUtils.isEmpty(busType))
        {
            params.addBodyParameter("busType", busType);
        }

        if (!TextUtils.isEmpty(applyUser))
        {
            params.addBodyParameter("applyUser", applyUser);
        }

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static FileUpload submitVoiceApi(Application application, File voice, FileUpload.UploadListener listener)
    {
        String url = HttpURLUtil.newInstance().append("annex/fileUpload").toString();
        List<SDFileListEntity> fileListEntities = new ArrayList<>();
        if (voice != null)
        {
            SDFileListEntity fileListEntity = new SDFileListEntity();
            fileListEntity.setEntity(application, voice, FileUploadPath.SHARE, SDFileListEntity.VOICE);
            fileListEntities.add(fileListEntity);
        }

        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        return FileUploadUtil.resumableUpload(application, fileListEntities, BusinessType.NO_KEY, null, url, params, listener);
    }

    public static void getScoreRecordListData(Context context, String projId, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/proj/score/list").toString();
        pairs.clear();
        RequestParams params = new RequestParams();


        if (!TextUtils.isEmpty(projId))
        {
            params.addBodyParameter("projId", projId);
        }
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getScoreRecordListForVCGroupData(Context context, String username, String projId, String pageNo, String
            pageSize, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/discussion/queryPage/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();


        params.addBodyParameter("username", username);
        params.addBodyParameter("projId", projId);
        params.addBodyParameter("pageNo", pageNo);
        params.addBodyParameter("pageSize", pageSize);

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getScoreRecordItemListData(Context context, String projId, String scoreDate, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/proj/score/detail").toString();
        pairs.clear();
        RequestParams params = new RequestParams();


        if (!TextUtils.isEmpty(projId))
        {
            params.addBodyParameter("projId", projId);
        }

        if (!TextUtils.isEmpty(scoreDate))
        {
            params.addBodyParameter("scoreDate", scoreDate);
        }

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void updateFollowUpStatus(Context context, String username, String projId, String followUpStatus,
                                            SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/proj/changeFollowStatus/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        if (!TextUtils.isEmpty(projId))
        {
            params.addBodyParameter("projId", projId);
        }

        if (!TextUtils.isEmpty(followUpStatus))
        {
            params.addBodyParameter("followUpStatus", followUpStatus);
        }
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void applyForPermissions(Context context, String username, String projId, String reason,
                                           SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/proj/permission/apply").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        if (!TextUtils.isEmpty(projId))
        {
            params.addBodyParameter("projId", projId);
        }

        if (!TextUtils.isEmpty(reason))
        {
            params.addBodyParameter("reason", reason);
        }
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getInvestTotalData(Context context, String username, String projId, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/proj/invest/investTotal").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        if (!TextUtils.isEmpty(projId))
        {
            params.addBodyParameter("projId", projId);
        }

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getFundInvestData(Context context, String username, String projId, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/proj/invest/fundInvest").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        if (!TextUtils.isEmpty(projId))
        {
            params.addBodyParameter("projId", projId);
        }

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getFollowonData(Context context, String projId, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/get/status/followon/holder.json?projId=" + projId).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    public static void getMyInvestmentNumberData(Context context, String username, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/myWork/query/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getMyInvestmentFollowOnTotalData(Context context, String username, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/myWork/followOnProjTotal/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getMyInvestmentDoneData(Context context, String username, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/myWork/doneTotal/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getMyInvestmentTop3Data(Context context, String username, String queryByDept, String limit,
                                               SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/proj/invest/valutaionTop3").toString();
         pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        if (!TextUtils.isEmpty(queryByDept))
        {
            params.addBodyParameter("queryByDept", queryByDept);
        }

        if (!TextUtils.isEmpty(limit))
        {
            params.addBodyParameter("limit", limit);
        }
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getProjectTop3Data(Context context, String username, String teamType, String pageNo,String pageSize,
                                          String orderBy,SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/proj/invest/queryValutaionTopByPage").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        if (!TextUtils.isEmpty(teamType))
        {
            params.addBodyParameter("teamType", teamType);
        }

        if (!TextUtils.isEmpty(pageNo))
        {
            params.addBodyParameter("pageNo", pageNo);
        }

        if (!TextUtils.isEmpty(pageSize))
        {
            params.addBodyParameter("pageSize", pageSize);
        }

        if (!TextUtils.isEmpty(orderBy))
        {
            params.addBodyParameter("orderBy", orderBy);
        }

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getMyPartnerOneToFourData(Context context, String username, int index, SDRequestCallBack callBack)

    {
        String url = "";
        if (index == 1)
        {
            url = HttpURLUtil.newInstance().append("iceforce/post/myWork/projNotes/holder").toString();
        } else if (index == 2)
        {
            url = HttpURLUtil.newInstance().append("iceforce/post/myWork/potentialProj/holder").toString();
        } else if (index == 3)
        {
            url = HttpURLUtil.newInstance().append("iceforce/post/myWork/recommendedPartnerProj/holder").toString();
        } else if (index == 4)
        {
            url = HttpURLUtil.newInstance().append("iceforce/post/myWork/groupTotal/holder").toString();
        }
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter("username", username);
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }


    public static void getWorkSummaryListData(Context context, String username, String urlFlag, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append(urlFlag).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getFollowOnSummaryDoneData(Context context, String username, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/myWork/followOnProj/done").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getFollowOnSummaryUnDoneData(Context context, String username, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/myWork/followOnProj/unread").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getInvestmentAndFinancingInformationListData(Context context, String username, String pageNo, String
            pageSize, String beginDate, String endDate, String key,
                                                                    String isAll, String group, String status, String round,
                                                                    String industry, String agency, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/financing/queryByPage/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        if (!TextUtils.isEmpty(pageNo))
        {
            params.addBodyParameter("pageNo", pageNo);
        }

        if (!TextUtils.isEmpty(pageSize))
        {
            params.addBodyParameter("pageSize", pageSize);
        }

        if (!TextUtils.isEmpty(beginDate))
        {
            params.addBodyParameter("beginDate", beginDate);
        }

        if (!TextUtils.isEmpty(endDate))
        {
            params.addBodyParameter("endDate", endDate);
        }

        if (!TextUtils.isEmpty(key))
        {
            params.addBodyParameter("key", key);
        }

        if (!TextUtils.isEmpty(isAll))
        {
            params.addBodyParameter("isAll", isAll);
        }

        if (!TextUtils.isEmpty(group))
        {
            params.addBodyParameter("group", group);
        }

        if (!TextUtils.isEmpty(status))
        {
            params.addBodyParameter("status", status);
        }

        if (!TextUtils.isEmpty(round))
        {
            params.addBodyParameter("round", round);
        }


        params.addBodyParameter("industry", industry);


        params.addBodyParameter("agency", agency);

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }


    public static void getResearchTabsData(Context context, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/get/research/industry/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    public static void getResearchGroupListData(Context context, String username, String indusGroup, String pageNo, String
            pageSize, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/research/queryByPage/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        if (!TextUtils.isEmpty(indusGroup))
        {
            params.addBodyParameter("indusGroup", indusGroup);
        }

        if (!TextUtils.isEmpty(pageNo))
        {
            params.addBodyParameter("pageNo", pageNo);
        }

        if (!TextUtils.isEmpty(pageSize))
        {
            params.addBodyParameter("pageSize", pageSize);
        }
        params.addBodyParameter("queryBySolr", "false");
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getResearchGroupListDataByKeyword(Context context, String username, String queryStr, String pageNo,
                                                         String pageSize, String indusGroup, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/research/queryByPage/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        if (!TextUtils.isEmpty(queryStr))
        {
            params.addBodyParameter("queryStr", queryStr);
        }

        if (!TextUtils.isEmpty(pageNo))
        {
            params.addBodyParameter("pageNo", pageNo);
        }

        if (!TextUtils.isEmpty(pageSize))
        {
            params.addBodyParameter("pageSize", pageSize);
        }

        if (!TextUtils.isEmpty(indusGroup))
        {
            params.addBodyParameter("indusGroup", indusGroup);
        }

        params.addBodyParameter("queryBySolr", "false");

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }


    public static void getNewsLetterTabsData(Context context, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/get/newsLetter/industry/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    public static void getNewsLetterGroupListData(Context context, String username, String indusGroup, String docName, String
            pageNo, String
                                                          pageSize, boolean isShow, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/newsLetter/queryByPage/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        if (!TextUtils.isEmpty(indusGroup))
        {
            params.addBodyParameter("indusGroup", indusGroup);
        }

        if (!TextUtils.isEmpty(pageNo))
        {
            params.addBodyParameter("pageNo", pageNo);
        }

        if (!TextUtils.isEmpty(pageSize))
        {
            params.addBodyParameter("pageSize", pageSize);
        }

        if (!TextUtils.isEmpty(docName))
        {
            params.addBodyParameter("docName", docName);
        }


        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, isShow, callBack);
    }

    public static void getGroupData(Context context, String groupId, String docName, final String mPage, String pageSize,
                                    boolean isShow, final
                                    ResCallBack
                                            resCallBack)
    {
        ListHttpHelper.getNewsLetterGroupListData(context, SPUtils.get(context, USER_ACCOUNT, "").toString(), groupId,
                docName, mPage,
                pageSize, isShow, new SDRequestCallBack(NewsLetterItemListBean.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        SDToast.showShort(msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        NewsLetterItemListBean bean = (NewsLetterItemListBean) responseInfo.getResult();
                        resCallBack.reTurnData(bean);
                    }
                });
    }

    public interface ResCallBack
    {
        void reTurnData(NewsLetterItemListBean newsLetterItemListBean);
    }

    public static void getPostInvestmentManagementByManagerListData(Context context, String username, String pageNo, String
            pageSize, String projName, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/financeData/queryByPageForManager/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        if (!TextUtils.isEmpty(pageNo))
        {
            params.addBodyParameter("pageNo", pageNo);
        }

        if (!TextUtils.isEmpty(pageSize))
        {
            params.addBodyParameter("pageSize", pageSize);
        }

        if (!TextUtils.isEmpty(projName))
        {
            params.addBodyParameter("projName", projName);
        }

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getPostInvestmentManagementByManagerForProjectListData(Context context, String username, String pageNo, String
            pageSize, String projName,String teamType, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/financeData/queryByPageForManager/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        if (!TextUtils.isEmpty(pageNo))
        {
            params.addBodyParameter("pageNo", pageNo);
        }

        if (!TextUtils.isEmpty(pageSize))
        {
            params.addBodyParameter("pageSize", pageSize);
        }

        if (!TextUtils.isEmpty(projName))
        {
            params.addBodyParameter("projName", projName);
        }

        if (!TextUtils.isEmpty(teamType))
        {
            params.addBodyParameter("teamType", teamType);
        }

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getPostInvestmentManagementDetailInfoData(Context context, String projId, String username,
                                                                 SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/financeData/queryInvestBasicInfo/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter("projId", projId);
        params.addBodyParameter("username", username);
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getPostInvestmentManagementDetailFinanceData(Context context, String username, String projId,
                                                                    SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/financeData/queryByProjId/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter("username", username);
        params.addBodyParameter("projId", projId);
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getPostInvestmentManagementDetailReportData(Context context, String username, String reportFrequency,
                                                                   String year, String dateStr, String projId, String endDate,
                                                                   SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/indexValue/queryByProjIdAndEndDate/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter("username", username);
        params.addBodyParameter("projId", projId);
        params.addBodyParameter("endDate", endDate);
        params.addBodyParameter("reportFrequency", reportFrequency);
        params.addBodyParameter("year", year);
        params.addBodyParameter("dateStr", dateStr);
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getPostInvestmentManagementDetailChartData(Context context, String projId, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/financeData/queryForChartByProjId/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter("projId", projId);
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getManageFlagData(Context context, String username, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/get/user/queryManagerFlag/holder.json?username=" +
                username).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }


    public static void getManageForInvestedData(Context context, String username, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/get/user/queryTeamProjFlag/holder.json?username=" +
                username).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    public static void updateIAFIStatus(Context context, String username, String projFinancingId, String status,
                                        SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/financing/changeStatus/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        if (!TextUtils.isEmpty(projFinancingId))
        {
            params.addBodyParameter("projFinancingId", projFinancingId);
        }

        if (!TextUtils.isEmpty(status))
        {
            params.addBodyParameter("status", status);
        }

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void sendProcessData(Context context, String username, String busId, String applyUser, String origBusId,
                                       String decision, String approveOpinion, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/proj/permission/process").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        if (!TextUtils.isEmpty(busId))
        {
            params.addBodyParameter("busId", busId);
        }

        if (!TextUtils.isEmpty(applyUser))
        {
            params.addBodyParameter("applyUser", applyUser);
        }

        if (!TextUtils.isEmpty(origBusId))
        {
            params.addBodyParameter("origBusId", origBusId);
        }

        if (!TextUtils.isEmpty(decision))
        {
            params.addBodyParameter("decision", decision);
        }

        if (!TextUtils.isEmpty(approveOpinion))
        {
            params.addBodyParameter("approveOpinion", approveOpinion);
        }

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void updateProjectPrivacy(Context context, String username, String projId, String isPrivacy,
                                            SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/get/proj/privacy/holder.json?username=" + username + "&projId="
                + projId + "&isPrivacy=" + isPrivacy).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    public static void getWorkSummaryDetailNameListData(Context context, String username, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/myWork/team/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter("username", username);

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getWorkSummaryDetailListData(Context context, String username, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/myWork/workDetail/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter("username", username);

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getPIMFPFundListData(Context context, String username, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/fundDeptMapping/queryByDeptId/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter("username", username);
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getPIMFPDetailListData(Context context, String username, String fundId, String pageNo, String pageSize,
                                              String projName,
                                              SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/financeData/queryByPage/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter("username", username);
        params.addBodyParameter("fundId", fundId);
        params.addBodyParameter("pageNo", pageNo);
        params.addBodyParameter("pageSize", pageSize);
        if (!TextUtils.isEmpty(projName))
        {
            params.addBodyParameter("projName", projName);
        }
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }


    //基础资料
    public static void getInvestementBaseInfo(Context context, String projectId, String username, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/get/proj/query/baseInfo.json?projId=" + projectId + "&username=" +
                username).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    //    项目信息
    public static void getInvestementProSiuation(Context context, String projectId, String username, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/get/detail/query/holder.json?projId=" + projectId + "&username=" +
                username).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    //投资方案
    public static void getInvestementProgram(Context context, String projectId, String username, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/get/investplan/queryAll/holder.json?projId=" + projectId +
                "&username=" +
                username).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    //基金投资
    public static void postInvestementFund(Context context, String projectId, String username, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/proj/invest/fundInvest").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter("projId", projectId);
        params.addBodyParameter("username", username);
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    //会议讨论
    public static void postInvestementMeeting(Context context, String projectId, String pageNo, String pageSize,
                                              String username,
                                              SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/meeting/queryPage/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter("projId", projectId);
        params.addBodyParameter("pageNo", pageNo);
        params.addBodyParameter("pageSize", pageSize);
        params.addBodyParameter("username", username);
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    //相关附件
    public static void postInvestementFile(Context context, String objectId, String pageNo, String pageSize,
                                           String username,
                                           SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/file/proj/queryPage").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter("objectId", objectId);
        params.addBodyParameter("pageNo", pageNo);
        params.addBodyParameter("pageSize", pageSize);
        params.addBodyParameter("username", username);
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    //会议讨论
    public static void postInvestementFllowUp(Context context, String projId, String pageNo, String pageSize,
                                              String username,
                                              SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/indexValue/queryPageByProjId/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter("projId", projId);
        params.addBodyParameter("pageNo", pageNo);
        params.addBodyParameter("pageSize", pageSize);
        params.addBodyParameter("username", username);
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    //董事会信息
    public static void postDirectorList(Context context, String projectId, String pageNo, String pageSize,
                                        String username,
                                        SDRequestCallBack callBack)
    {
//        String url = HttpURLUtil.newInstance().append("iceforce/post/board/meeting/queryByPage").toString();
        String url = HttpURLUtil.newInstance().append("iceforce/get/board/meeting/queryByPage.json").append2("?projId=" +
                projectId).append2("&username=" + username).append2("&pageNo=" + pageNo).append2("&pageSize=" + pageSize)
                .toString();
        pairs.clear();

        RequestParams params = new RequestParams();
//        params.addBodyParameter("projId", projectId);
//        params.addBodyParameter("pageNo", pageNo);
//        params.addBodyParameter("pageSize", pageSize);
//        params.addBodyParameter("username", username);
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    //项目详细信息-更新当前版本
    public static void postSituation(Context context, String urlEndString, String projId, String code, String value, String
            username, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/detail/update/" + urlEndString).toString();
        pairs.clear();
        if (!TextUtils.isEmpty(projId))
        {
            pairs.add(new BasicNameValuePair("projId", projId));
        }
        if (!TextUtils.isEmpty(code))
        {
            pairs.add(new BasicNameValuePair("code", code));
        }
        if (!TextUtils.isEmpty(value))
        {
            pairs.add(new BasicNameValuePair("value", value));
        }
        if (!TextUtils.isEmpty(username))
        {
            pairs.add(new BasicNameValuePair("username", username));
        }
        //  pairs = JsonUtils.getInstance().reTurnPair(jsonStr);
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    //投资方案新增
    public static void postEdtBaseInfo(Context context, String jsonStr,
                                       SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/proj/update/holder").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(jsonStr))
        {
            pairs = JsonUtils.getInstance().reTurnPair(jsonStr);
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    //投资方案新增
    public static void postProgram(Context context, String jsonStr,
                                   SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/investplan/add/holder").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(jsonStr))
        {
            pairs = JsonUtils.getInstance().reTurnPair(jsonStr);
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    //获取所有类型
    public static void getProjectType(Context context, boolean isNeedPd, String typeString,
                                      SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/get/syscode/queryByType/holder.json?type=" + typeString)
                .toString();
        RequestParams params = new RequestParams();
//        params.addBodyParameter("", "");
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, isNeedPd, callBack);
    }

    //会议审批
    public static void postMeetingApprove(Context context, String id, String approveOpinion, String username,
                                          SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/meeting/approve/holder").toString();
        RequestParams params = new RequestParams();
        if (StringUtils.notEmpty(id))
            params.addBodyParameter("id", id);
        if (StringUtils.notEmpty(approveOpinion))
            params.addBodyParameter("approveOpinion", approveOpinion);
        if (StringUtils.notEmpty(username))
            params.addBodyParameter("username", username);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    //跟踪2.1
    public static void getFollowReportDate(Context context, String reportFrequency,
                                           SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/get/financeData/queryReportDateByFrequency/holder" +
                ".json?reportFrequency=" + reportFrequency).toString();
        RequestParams params = new RequestParams();
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    //跟踪2.2
    public static void postFollowAssert(Context context, String projId, String endDate,
                                        SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/financeData/assertByProjIdAndEndDate/holder").toString();
        RequestParams params = new RequestParams();
        if (StringUtils.notEmpty(projId))
            params.addBodyParameter("projId", projId);
        if (StringUtils.notEmpty(endDate))
            params.addBodyParameter("endDate", endDate);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    //跟踪2.3
    public static void postFollowByProjIdAndEndDate(Context context, String projId, String reportFrequency, String year, String
            dateStr, String endDate, String username, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/indexValue/queryByProjIdAndEndDate/holder"
        ).toString();
        RequestParams params = new RequestParams();
        if (StringUtils.notEmpty(projId))
            params.addBodyParameter("projId", projId);
        if (StringUtils.notEmpty(reportFrequency))
            params.addBodyParameter("reportFrequency", reportFrequency);
        if (StringUtils.notEmpty(year))
            params.addBodyParameter("year", year);
        if (StringUtils.notEmpty(dateStr))
            params.addBodyParameter("dateStr", dateStr);
        if (StringUtils.notEmpty(endDate))
            params.addBodyParameter("endDate", endDate);
        if (StringUtils.notEmpty(username))
            params.addBodyParameter("username", username);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    //    {
//        "endDate": "2018-09-30",
//            "projId": "5bed60f9420f4416819304b04c6a169a",
//            "username": "yi_zhang",
//            "valueList":[{
//        "indexId": "60f7e2386acf4cbb8397291301cc3efa",
//                "indexValue": "ka2018年度",
//    }]
//    }
    //跟踪2.4
    public static void postFollowAdd(Context context, String reportFrequency, String projId, String endDate, String year,
                                     String dateStr, String
                                             username, String valueList, String
                                             signalFlag,
                                     SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/indexValue/add/holder").toString();
        RequestParams params = new RequestParams();
        if (StringUtils.notEmpty(reportFrequency))
            params.addBodyParameter("reportFrequency", reportFrequency);
        if (StringUtils.notEmpty(projId))
            params.addBodyParameter("projId", projId);
        if (StringUtils.notEmpty(endDate))
            params.addBodyParameter("endDate", endDate);
        if (StringUtils.notEmpty(username))
            params.addBodyParameter("username", username);
        if (StringUtils.notEmpty(signalFlag))
            params.addBodyParameter("signalFlag", signalFlag);
        if (StringUtils.notEmpty(year))
            params.addBodyParameter("year", year);
        if (StringUtils.notEmpty(dateStr))
            params.addBodyParameter("dateStr", dateStr);

        if (StringUtils.notEmpty(valueList))
            params.addQueryStringParameter("valueList", valueList);

        params.setCharset("UTF-8");
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    //跟踪进展
    public static void postFollowUp(Context context, String projId, String pageNo, String pageSize,
                                    SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/proj/notes/holder").toString();
        RequestParams params = new RequestParams();
        if (StringUtils.notEmpty(projId))
            params.addBodyParameter("projId", projId);
        if (StringUtils.notEmpty(pageNo))
            params.addBodyParameter("pageNo", pageNo);
        if (StringUtils.notEmpty(pageSize))
            params.addBodyParameter("pageSize", pageSize);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    //文档库-项目
    public static void postFileLibrary(Context context, String queryString, String pageNo, String pageSize,
                                       String username,
                                       SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/file/userFileWithProj/queryPage").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter("queryString", queryString);
        params.addBodyParameter("pageNo", pageNo);
        params.addBodyParameter("pageSize", pageSize);
        params.addBodyParameter("username", username);
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    //文档库-个人
    public static void postPersonalFileLibrary(Context context, String queryString, String pageNo, String pageSize,
                                               String username,
                                               SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/file/userFileWithoutProj/queryPage").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter("queryString", queryString);
        params.addBodyParameter("pageNo", pageNo);
        params.addBodyParameter("pageSize", pageSize);
        params.addBodyParameter("username", username);
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }


    public static void getProjectSignalFlag(Context context, String projId,
                                            SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/get/financeData/selectSignalFlag/holder" +
                ".json?projId=" + projId).toString();
        RequestParams params = new RequestParams();
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    public static void getProjectNameListData(Context context, String name, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/proj/query/selectAllList").toString();
        RequestParams params = new RequestParams();
        params.addBodyParameter("projName", name);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getProjectListData(Context context, String username, String name, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/proj/query/selectList").toString();
        RequestParams params = new RequestParams();
        params.addBodyParameter("username", username);
        params.addBodyParameter("projName", name);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }


    //文档库-搜索项目
    public static void postFileProjName(Context context, String projName, String pageNo, String pageSize,
                                        String username,
                                        SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/proj/query/allproj").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter("projName", projName);
        params.addBodyParameter("pageNo", pageNo);
        params.addBodyParameter("pageSize", pageSize);
        params.addBodyParameter("username", username);
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    //
    public static void postFileTransToPrj(Context context, String fileId, String objectId, String busType,
                                          String username,
                                          SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/file/transToPrj/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter("fileId", fileId);
        params.addBodyParameter("objectId", objectId);
        params.addBodyParameter("busType", busType);
        params.addBodyParameter("username", username);
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static FileUpload postFileLib(Application application,
                                         String username,
                                         String fileType,
                                         boolean isOriginalImg, List<File> files, List<String> imgPaths, File
                                                 voice, FileUpload.UploadListener listener)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/upload/file/proj/upload").toString();

        List<SDFileListEntity> fileListEntities = toVoiceAndPicAndFile3(application, isOriginalImg, files, imgPaths, voice);
        pairs.clear();
        if (StringUtils.notEmpty(username))
        {
            pairs.add(new BasicNameValuePair("username", username));
        }
//        if (StringUtils.notEmpty(fileType))
//        {
//            pairs.add(new BasicNameValuePair("fileType", fileType));
//        }

        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        return FileUploadUtil.resumableUpload(application, fileListEntities, BusinessType.NO_KEY, null, url, params, true,
                listener);
    }

    public static void sendFeedBackData(Context context, String username, String opinion, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/sys/opinion/add").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        if (!TextUtils.isEmpty(opinion))
        {
            params.addBodyParameter("opinion", opinion);
        }

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void updateTipsFlagData(Context context, String msgId, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/get/msg/read/holder.json?msgId=" + msgId).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }


    public static void getQXBStatus(Context context, String companyName,
                                    SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/get/comp/enterpriseDetail/holder" +
                ".json?name=" + companyName).toString();
        RequestParams params = new RequestParams();
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    //主要人员
    public static void getMajorMember(Context context, String companyName, int pageNo, int pageSize,
                                      SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/get/comp/employees/holder" +
                ".json?name=" + companyName).append2("&pageNo=" + pageNo).append2("&pageSize=" + pageSize).toString();
        RequestParams params = new RequestParams();
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    //股东
    public static void getShareholder(Context context, String companyName, int pageNo, int pageSize,
                                      SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/get/comp/shareholderInfo/holder" +
                ".json?name=" + companyName).append2("&pageNo=" + pageNo).append2("&pageSize=" + pageSize).toString();
        RequestParams params = new RequestParams();
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    public static void updateNeedToDoDoneMsgData(Context context, String username, String origBusId, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/get/proj/notes/doneMsg.json?origBusId=" + origBusId +
                "&username=" + username).toString();
        url = url.replace("|", "%7C");
        pairs.clear();
        RequestParams params = new RequestParams();
        /*params.addBodyParameter("username", username);
        params.addBodyParameter("origBusId", origBusId);*/
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    //诉讼
    public static void getLawsuite(Context context, String companyName, int pageNo, int pageSize,
                                   SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/get/comp/lawsuitInfo/holder" +
                ".json?name=" + companyName).append2("&pageNo=" + pageNo).append2("&pageSize=" + pageSize).toString();
        RequestParams params = new RequestParams();
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    public static void delTrackProgressDataByNoteId(Context context, String noteId, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/proj/notedelete/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter("noteId", noteId);
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void gethazardInfo(Context context, String companyName, int pageNo, int pageSize,
                                     SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/get/comp/riskInfo/holder" +
                ".json?name=" + companyName).append2("&pageNo=" + pageNo).append2("&pageSize=" + pageSize).toString();
        RequestParams params = new RequestParams();
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    //文档库-搜索项目
    public static void postQxbSearch(Context context, String keyWord, String pageNo, String pageSize, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/get/comp/enterpriseList/holder" +
                ".json?keyWord=" + keyWord).append2("&pageNo=" + pageNo).append2("&pageSize=" + pageSize).toString();
        RequestParams params = new RequestParams();
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    //休假汇总
    public static void postAllHolidayList(Context context, String pageNo, String pageSize, String userName, SDRequestCallBack
            callBack)
    {
        String url = HttpURLUtil.newInstance().append("holiday/team").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter("pageNo", pageNo);
        params.addBodyParameter("pageSize", pageSize);
        params.addBodyParameter("userName", userName);
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    //个人年假列表
    public static void postPersonalHolidayList(Context context, String userName, String pageNo, String pageSize,
                                               String leaveType, String signed, SDRequestCallBack
                                                       callBack)
    {
        String url = HttpURLUtil.newInstance().append("api/app/hr/holiday/query/page").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter("pageNo", pageNo);
        params.addBodyParameter("pageSize", pageSize);
        params.addBodyParameter("userName", userName);
        params.addBodyParameter("leaveType", leaveType);
        params.addBodyParameter("signed", signed);
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getPersonalHolidayList(Context context, String userName, String pageNo, String pageSize,
                                              String leaveType, String signed, String yearString, SDRequestCallBack
                                                      callBack)
    {
        String url = HttpURLUtil.newInstance().append("holiday/queryList").append(signed).append2(".json").append2
                ("?leaveType=").append2(leaveType).append2("&userName=").append2(userName).append2("&leaveStart=").append2
                (yearString).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    public static void getFollowProjectDetailForVCGroupDatas(Context context, String username, String projId, SDRequestCallBack
            callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/get/proj/query/vcDetail.json?projId=" + projId +
                "&username=" + username).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    public static void sendVCTrackProgressAddData(Context context, String username, String projId, String note, String noteType,
                                                  String audioTime, String status, String statusType, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/vcStatus/save/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();

        if (!TextUtils.isEmpty(username))
        {
            params.addBodyParameter("username", username);
        }

        if (!TextUtils.isEmpty(projId))
        {
            params.addBodyParameter("projId", projId);
        }

        if (!TextUtils.isEmpty(note))
        {
            params.addBodyParameter("note", note);
        }

        if (!TextUtils.isEmpty(noteType))
        {
            params.addBodyParameter("noteType", noteType);
        }

        if (!TextUtils.isEmpty(audioTime))
        {
            params.addBodyParameter("audioTime", audioTime);
        }

        if (!TextUtils.isEmpty(statusType))
        {
            params.addBodyParameter("statusType", statusType);
        }

        if (!TextUtils.isEmpty(status))
        {
            params.addBodyParameter("status", status);
        }

        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }


    //新增董事会信息
    public static void postAddDirector(Context context, String jsonStr,
                                       SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/board/meeting/add").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(jsonStr))
        {
            pairs = JsonUtils.getInstance().reTurnPair(jsonStr);
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    //编辑董事会信息
    public static void postEditDirector(Context context, String jsonStr,
                                        SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/board/meeting/update").toString();
        pairs.clear();
        if (!TextUtils.isEmpty(jsonStr))
        {
            pairs = JsonUtils.getInstance().reTurnPair(jsonStr);
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    /**
     * 删除APP项目文件
     *
     * @param context
     * @param username
     * @param fileId
     * @param callBack
     */
    public static void getDeleteFileById(Context context, String username, String fileId, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/get/file/delete/holder.json?id=" + fileId).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    public static void getMyProjectsNumberData(Context context, String username, String managerFlag, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/myWork/projNum/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter("username", username);
        params.addBodyParameter("managerFlag", managerFlag);
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getMyWorkItemData(Context context, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/get/myWork/item/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

    public static void getMyWorkReportNumData(Context context, String username, String dateStr, String dataFlag,
                                              SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/myWork/projNumMap/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter("username", username);
        params.addBodyParameter("dateStr", dateStr);
        params.addBodyParameter("dataFlag", dataFlag);
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getMyWorkReportChartData(Context context, String username, String dateStr, String dataFlag, String type,
                                                SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/myWork/chartForPartner/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter("username", username);
        params.addBodyParameter("dateStr", dateStr);
        params.addBodyParameter("dataFlag", dataFlag);
        params.addBodyParameter("type", type);
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getMyWorkReportChartDataForInvestment(Context context, String username, String dateStr, String dataFlag,
                                                             String type, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/myWork/chartForManager/holder").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter("username", username);
        params.addBodyParameter("dateStr", dateStr);
        params.addBodyParameter("dataFlag", dataFlag);
        params.addBodyParameter("type", type);
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getApproveListData(Context context, String username, String pageNo, String pageSize, SDRequestCallBack
            callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/queryByPage").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter("userName", username);
        params.addBodyParameter("pageNo", pageNo);
        params.addBodyParameter("pageSize", pageSize);
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getApproveHolidayListData(Context context, String username, String pageNo, String pageSize, String flag,
                                                 SDRequestCallBack callBack)
    {
        String url;
        if (flag.equals("0"))
        {
            url = HttpURLUtil.newInstance().append("iceforce/post/queryHolidayByPage").toString();
        } else if (flag.equals("1"))
        {
            url = HttpURLUtil.newInstance().append("iceforce/post/queryPendingByPage").toString();
        } else
        {
            url = HttpURLUtil.newInstance().append("iceforce/post/queryTripByPage").toString();
        }
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter("userName", username);
        params.addBodyParameter("pageNo", pageNo);
        params.addBodyParameter("pageSize", pageSize);
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    /*public static void getApprovePendingListData(Context context,String username,String pageNo,String pageSize,
    SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/queryPendingByPage").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter("userName", username);
        params.addBodyParameter("pageNo", pageNo);
        params.addBodyParameter("pageSize", pageSize);
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }

    public static void getApproveTripListData(Context context,String username,String pageNo,String pageSize, SDRequestCallBack
    callBack)
    {
        String url = HttpURLUtil.newInstance().append("iceforce/post/queryTripByPage").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter("userName", username);
        params.addBodyParameter("pageNo", pageNo);
        params.addBodyParameter("pageSize", pageSize);
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.post(url, params, true, callBack);
    }*/

    public static void getApproveDetailData(Context context, String id, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("holiday/getApproveDetail/" + id).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        SDHttpHelper sdHttpHelper = new SDHttpHelper(context);
        sdHttpHelper.get(url, params, true, callBack);
    }

}
