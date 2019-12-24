package com.cxgz.activity.cxim.http;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.entity.group.IMGroup;
import com.chaoxiang.imsdk.group.IMGroupManager;
import com.cxgz.activity.cxim.ui.kefu.KeFuListBean;
import com.cxgz.activity.db.dao.SDUserDao;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.entity.SDFileListEntity;
import com.erp_https.BaseAPI;
import com.http.FileUpload;
import com.http.HttpURLUtil;
import com.http.SDHttpHelper;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.http.config.BusinessType;
import com.chaoxiang.base.config.Constants;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.squareup.okhttp.Request;
import com.utils.FileUploadPath;
import com.utils.FileUploadUtil;

import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import yunjing.utils.JsonUtils;

import static com.http.callback.SDHttpRequestCallBack.application;


/**
 * User: Selson
 * Date: 2016-11-02
 * FIXME IM的API接口
 */
public class ImHttpHelper extends BaseAPI
{

    //
    public static void findAddPopularizeGroup(Context context, String s_name,
                                              SDRequestCallBack callBack)
    {
        SDHttpHelper mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("group")
                .append("user")
                .append("apply")
                .toString();
        pairs.clear();

        RequestParams params = new RequestParams();
//        if (!TextUtils.isEmpty(s_name))
//        {
//            pairs.add(new BasicNameValuePair("s_name", s_name));
//        }
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    /**
     * @param context     我的文档列表
     * @param mHttpHelper
     * @param callBack
     */
    public static void findBusninessFileList(Context context, SDHttpHelper mHttpHelper,
                                             SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("annex").append("findFileCountByType").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    /**
     * @param context     我的文档列表
     * @param mHttpHelper
     * @param callBack
     */
    public static void findFriendsList(Context context, String phone, SDHttpHelper mHttpHelper,
                                       SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("friend").append("s").append(phone).toString();
        pairs.clear();
        pairs.add(new BasicNameValuePair("type", "0"));
        RequestParams params = new RequestParams();
        params.addQueryStringParameter(pairs);
        mHttpHelper.get(url, params, true, callBack);
    }

    /**
     * @param context
     * @param mHttpHelper
     * @param callBack
     */
    public static void findAcceptFriend(Context context, String friendId, String friendImaccount, String accept, SDHttpHelper
            mHttpHelper,
                                        SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("friend").append("accept").toString();
        pairs.clear();
        pairs.add(new BasicNameValuePair("friendId", friendId));
        pairs.add(new BasicNameValuePair("friendImaccount", friendImaccount));
        pairs.add(new BasicNameValuePair("accept", accept));

        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    /**
     * @param context
     * @param cxid
     * @param friendId
     * @param mHttpHelper
     * @param callBack
     */
    public static void findJudgeFriend(Context context, String cxid, String friendId, SDHttpHelper mHttpHelper,
                                       SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("friend").append("judge").toString();
        pairs.clear();
        pairs.add(new BasicNameValuePair("cxid", cxid));
        pairs.add(new BasicNameValuePair("friendId", friendId));
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    /**
     * 发送好友验证
     *
     * @param context
     * @param mHttpHelper
     * @param callBack
     */
    public static void findSendAddFriend(Context context, AddFriendFilter addFriendFilter, SDHttpHelper mHttpHelper,
                                         SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("friend").append("send").toString();
        pairs.clear();
        pairs.add(new BasicNameValuePair("friendImaccount", addFriendFilter.getFriendImaccount()));
        pairs.add(new BasicNameValuePair("friendId", addFriendFilter.getFriendId()));
        pairs.add(new BasicNameValuePair("friendName", addFriendFilter.getFriendName()));
        pairs.add(new BasicNameValuePair("friendIcon", addFriendFilter.getFriendIcon()));
        pairs.add(new BasicNameValuePair("remark", addFriendFilter.getRemark()));
        pairs.add(new BasicNameValuePair("createTime", addFriendFilter.getCreateTime()));

        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    /**
     * 搜索个人信息
     *
     * @param context
     * @param userId
     * @param mHttpHelper
     * @param callBack
     */
    public static void findPersonInfo(Context context, SDHttpHelper mHttpHelper, String userId,
                                      SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("sysUser").append(userId).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.get(url, params, true, callBack);
    }

    /**
     * 通过ImAccount查询
     */
    public static void findPersonInfoByImAccount(Context context, boolean isShow, SDHttpHelper mHttpHelper,
                                                 String imAccount,
                                                 SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("sysuser").append("getSysUser").append(imAccount).toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter(pairs);
        mHttpHelper.get(url, params, isShow, callBack);
    }

    //工作圈接口
    //查询自己和别人发送的工作
    public static void findPageMyWordAndReceive(Context context, int pageNumber,
                                                SDRequestCallBack callBack)
    {
        SDHttpHelper mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("workRecord")
                .append("findPageMyWordAndReceive")
                .append(String.valueOf(pageNumber))
                .toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    //查询自己发送的工作
    public static void findPageMyWord(Context context, int pageNumber,
                                      SDRequestCallBack callBack)
    {
        SDHttpHelper mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("workRecord")
                .append("findPageMyWord")
                .append(String.valueOf(pageNumber))
                .toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    //评论

    /**
     * @param context
     * @param callBack
     * @paraml_bid 业务ID
     * @paraml_btype 业务类型
     * @params_remark 评论内容
     */
    public static void postRecord(Context context, WorkRecordFilter workRecordFilter,
                                  SDRequestCallBack callBack)
    {
        SDHttpHelper mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("workRecord")
                .append("record")
                .toString();
        pairs.clear();

        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(String.valueOf(workRecordFilter.getL_bid())))
        {
            pairs.add(new BasicNameValuePair("l_bid", workRecordFilter.getL_bid()));
        }
        if (!TextUtils.isEmpty(workRecordFilter.getL_btype()))
        {
            pairs.add(new BasicNameValuePair("l_type", workRecordFilter.getL_btype()));
        }
        if (!TextUtils.isEmpty(workRecordFilter.getS_remark()))
        {
            pairs.add(new BasicNameValuePair("s_remark", workRecordFilter.getS_remark()));
        }
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    /**
     * 工作圈-详情
     *
     * @param context
     * @param l_eid    业务ID
     * @param s_btype  业务类型
     * @param callBack
     */
    public static void findRecordDetail(Context context, String s_btype, String l_eid,
                                        SDRequestCallBack callBack)
    {
        SDHttpHelper mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("workRecord")
                .append("findDetail")
                .append(l_eid)
                .toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    //工作圈
    public static FileUpload loadImgSubmit(Application application, String imgPath, FileUpload.UploadListener listener)
    {
        String url = HttpURLUtil.newInstance().append("workRecord").append("file").toString();
        List<SDFileListEntity> files = new ArrayList<>();
        if (!imgPath.equals(""))
        {
            File file = new File(imgPath);
            SDFileListEntity entity = new SDFileListEntity();
            entity.setEntity(application, file, FileUploadPath.FILEPATH_WORK_CIRCLE, SDFileListEntity.IMG_BG);
            files.add(entity);
        }
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        return FileUploadUtil.resumableUpload(application, files, BusinessType.NO_KEY, null, url, params, listener);
    }

    //
    public static void reduceNum(Context context, String groupIdString,
                                 SDRequestCallBack callBack)
    {
        SDHttpHelper mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("group")
                .append("reduceNum")
                .toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(groupIdString))
        {
            pairs.add(new BasicNameValuePair("groupId", groupIdString));
        }
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    /**
     * 提交附件
     */
    public static FileUpload submitFileApi(Application application, String names, boolean isOriginalImg, List<File> files,
                                           List<String> imgPaths, File voice, FileUpload.UploadListener listener)
    {
        String url = HttpURLUtil.newInstance().append("annex").append("fileUpload").toString();
        List<SDFileListEntity> fileListEntities = toVoiceAndPicAndFile2(application, isOriginalImg, files, imgPaths, voice);
        pairs.clear();
        if (fileListEntities.get(0).getSrcName() != null)
        {
            pairs.add(new BasicNameValuePair("names", fileListEntities.get(0).getSrcName()));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        return FileUploadUtil.resumableUpload(application, fileListEntities, BusinessType.NO_KEY, null, url, params, true,
                listener);
    }

    /**
     * 获取部门
     */
    public static void getDept(Context context, String beanJson,
                               SDRequestCallBack callBack)
    {
        SDHttpHelper mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("sysuser")
                .append("dept")
                .append("count")
                .toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    /**
     * 获取部门人数
     */
    public static void getDeptNum(Context context, String beanJson,
                                  SDRequestCallBack callBack)
    {
        SDHttpHelper mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("sysDepartment")
                .append("getDeptCount")
                .toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    /**
     * 获取客服列表
     */
    public static void postKeFuList(Context context, String beanJson,
                                    SDRequestCallBack callBack)
    {
        SDHttpHelper mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("sysuser")
                .append("kefu")
                .append("list")
                .toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    //保存下客服
    public static void postKefuListSave(final Context mContext)
    {
        final SDUserDao mUserDao = new SDUserDao(mContext);
        final List<SDUserEntity> userEntitiesTmpForKefu = new ArrayList<>();

        ImHttpHelper.postKeFuList(mContext, "", new SDRequestCallBack(KeFuListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(mContext, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                KeFuListBean keFuListBean = (KeFuListBean) responseInfo.getResult();
                if (StringUtils.notEmpty(keFuListBean))
                    if (StringUtils.notEmpty(keFuListBean.getData()))
                    {
                        for (int i = 0; i < keFuListBean.getData().size(); i++)
                        {
                            if (keFuListBean.getData().get(i).getUserType() == Constants.USER_TYPE_KEFU)
                            {
                                SDUserEntity sdUserEntity = new SDUserEntity();
                                sdUserEntity.setEid(keFuListBean.getData().get(i).getEid());
                                sdUserEntity.setDeptName(keFuListBean.getData().get(i).getDeptName());
                                sdUserEntity.setName(keFuListBean.getData().get(i).getName());
                                sdUserEntity.setIcon(keFuListBean.getData().get(i).getIcon());
//                                sdUserEntity.setDeptId(keFuListBean.getData().get(i).getDeptId());
                                sdUserEntity.setImAccount(keFuListBean.getData().get(i).getImAccount());
                                sdUserEntity.setJob(keFuListBean.getData().get(i).getJob());
                                sdUserEntity.setUserType(keFuListBean.getData().get(i).getUserType());
                                userEntitiesTmpForKefu.add(sdUserEntity);
                            }
                        }
                        if (StringUtils.notEmpty(userEntitiesTmpForKefu) && userEntitiesTmpForKefu.size() > 0)
                        {
                            for (SDUserEntity userEntity : userEntitiesTmpForKefu)
                            {
                                mUserDao.saveUser(userEntity);
                            }
                        }

                    }
            }
        });
    }

    /**
     * 获取语音会议列表
     */
    public static void postMeetingList(Context context, int page, String s_title, String isSearch,
                                       SDRequestCallBack callBack)
    {
        SDHttpHelper mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("vedioMeet")
                .append("list")
                .append(page + "")
                .toString();
        RequestParams params = new RequestParams();
        if (StringUtils.notEmpty(s_title))
            params.addBodyParameter("s_title", s_title);
        if (StringUtils.notEmpty(isSearch))
            params.addBodyParameter(Constants.IS_SEARCH_PARAM, Constants.IS_SEARCH_CONTENT);
        mHttpHelper.post(url, params, true, callBack);
    }

    /**
     * 获取语音会议添加、修改
     */
    public static void postMeetingAdd(Context context, String jsonString,
                                      SDRequestCallBack callBack)
    {
        SDHttpHelper mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("vedioMeet")
                .append("save")
                .toString();
        pairs.clear();
        pairs = JsonUtils.getInstance().reTurnPair(jsonString);
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    /**
     * 获取语音会议详情
     */
    public static void postMeetingDetail(Context context, String eid,
                                         SDRequestCallBack callBack)
    {
        SDHttpHelper mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("vedioMeet")
                .append("detail")
                .append(eid)
                .toString();
        RequestParams params = new RequestParams();
        mHttpHelper.get(url, params, true, callBack);
    }

    /**
     * 获取语音会议-会议内容列表-总的。
     */
    public static void postMeetingListDetail(Context context, String eid,
                                             SDRequestCallBack callBack)
    {
        SDHttpHelper mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("vedioMeet")
                .append("record")
                .append("list")
                .append(eid)
                .append("all")
                .toString();
        RequestParams params = new RequestParams();
        mHttpHelper.post(url, params, false, callBack);
    }

    /**
     * 获取语音会议-会议内容列表-获取最新的。
     */
    public static void postMeetingNewListDetail(Context context, String eid, String limitId,
                                                SDRequestCallBack callBack)
    {
        SDHttpHelper mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("vedioMeet")
                .append("record")
                .append("list")
                .append(eid)
                .append("forward")
                .toString();
        RequestParams params = new RequestParams();
        if (StringUtils.notEmpty(limitId))
            params.addBodyParameter("limitId", limitId);
        mHttpHelper.post(url, params, false, callBack);
    }

    //发送语音
    public static FileUpload postMeetingSend(Context context, String bid, String ygId, int type, String content, String length,
                                             List<String> imgPaths, File voice,
                                             FileUpload.UploadListener listener)
    {
        List<SDFileListEntity> fileListEntities = toVoiceAndPicAndFile2(application, true, null, imgPaths, voice);
        String url = HttpURLUtil.newInstance()
                .append("vedioMeet")
                .append("record")
                .append("save")
                .toString();

//        RequestParams params = new RequestParams();
//        if (StringUtils.notEmpty(bid))
//            params.addBodyParameter("bid", bid);
//        if (StringUtils.notEmpty(ygId))
//            params.addBodyParameter("ygId", ygId);
//        if (StringUtils.notEmpty(type))
//            params.addBodyParameter("type", type + "");
//        if (type == 1)
//        {
//            if (StringUtils.notEmpty(content))
//                params.addBodyParameter("content", content);
//        }
//
//        if (StringUtils.notEmpty(length))
//            params.addBodyParameter("length", length);

        pairs.clear();
        RequestParams params = new RequestParams();

        if (StringUtils.notEmpty(bid))
            pairs.add(new BasicNameValuePair("bid", bid));
        if (StringUtils.notEmpty(ygId))
            pairs.add(new BasicNameValuePair("ygId", ygId));
        if (StringUtils.notEmpty(type))
            pairs.add(new BasicNameValuePair("type", type + ""));

        if (type == 1)
        {
            if (StringUtils.notEmpty(content))
                pairs.add(new BasicNameValuePair("content", content));
        }

        if (StringUtils.notEmpty(length))
            pairs.add(new BasicNameValuePair("length", length));

        params.addBodyParameter(pairs);
        return FileUploadUtil.resumableUpload(application, fileListEntities, BusinessType.NO_KEY, null, url, params,
                listener);
    }

    /**
     * 结束语音会议。
     */
    public static void postMeetingFinsh(Context context, String eid,
                                        SDRequestCallBack callBack)
    {
        SDHttpHelper mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("vedioMeet")
                .append("isEnd")
                .append(eid)
                .toString();
        RequestParams params = new RequestParams();
        mHttpHelper.post(url, params, false, callBack);
    }

    /**
     * 项目协同列表
     */
    public static void PostProjectChatList(Context context, String eid,
                                           SDRequestCallBack callBack)
    {
        SDHttpHelper mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("project")
                .append("record")
                .append("list")
                .append(eid)
                .append("all")
                .toString();
        RequestParams params = new RequestParams();
        mHttpHelper.post(url, params, false, callBack);
    }

    /**
     * 项目协同最新消息
     */
    public static void PostProjectChatNew(Context context, String eid, String limitId,
                                          SDRequestCallBack callBack)
    {
        SDHttpHelper mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("project")
                .append("record")
                .append("list")
                .append(eid)
                .append("forward")
                .toString();
        RequestParams params = new RequestParams();
        if (StringUtils.notEmpty(limitId))
            params.addBodyParameter("limitId", limitId);
        mHttpHelper.post(url, params, false, callBack);
    }

    //项目协同发送信息
    public static FileUpload postProjectChatSend(Context context, String jsonString,
                                                 List<String> imgPaths, File voice,
                                                 FileUpload.UploadListener listener)
    {
        List<SDFileListEntity> fileListEntities = toVoiceAndPicAndFile2(application, true, null, imgPaths, voice);
        String url = HttpURLUtil.newInstance()
                .append("project")
                .append("record")
                .append("save")
                .toString();

        pairs.clear();
        pairs = JsonUtils.getInstance().reTurnPair(jsonString);
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);

        return FileUploadUtil.resumableUpload(application, fileListEntities, BusinessType.NO_KEY, null, url, params,
                listener);
    }

    /**
     * 获取服务器的群组列表
     */
    public static void getNetData(final Context context, final OnListener onListener)
    {
        IMGroupManager.getInstance().getGroupsFromServer(new IMGroupManager.IMGroupListCallBack()
        {
            @Override
            public void onResponse(List<IMGroup> groups)
            {
                onListener.onRep(groups);
            }

            @Override
            public void onError(Request request, Exception e)
            {
//                MyToast.showToast(SDChatGroupList.this, "刷新群组失败！");
                onListener.onError(e);
            }
        });
    }

    public interface OnListener
    {
        void onRep(List<IMGroup> groups);

        void onError(Exception e);
    }

    /**
     * 小助手
     */
    public static void PostXZS(Context context, String companyName,
                                          SDRequestCallBack callBack)
    {
        SDHttpHelper mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("invoice")
                .append("littleHelper")
                .toString();
        RequestParams params = new RequestParams();
        if (StringUtils.notEmpty(companyName))
            params.addBodyParameter("companyName", companyName);

        mHttpHelper.post(url, params, false, callBack);
    }

    /**
     * 发送短信
     */
    public static void PostUserMessages(Context context, String moblies,
                               SDRequestCallBack callBack)
    {
        SDHttpHelper mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("system")
                .append("batchSendMessage")
                .toString();
        RequestParams params = new RequestParams();
        if (StringUtils.notEmpty(moblies))
            params.addBodyParameter("moblies", moblies);

        mHttpHelper.post(url, params, false, callBack);
    }
}