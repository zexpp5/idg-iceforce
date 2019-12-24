package com.ui.http;

import android.app.Activity;
import android.app.Application;
import android.text.TextUtils;

import com.chaoxiang.base.config.Config;
import com.chaoxiang.base.utils.DialogUtils;
import com.chaoxiang.base.utils.MD5Util;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.superqq.bean.HighIconBean;
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
import com.ui.http.parameter.ParameterRegister;
import com.ui.http.parameter.ParameterSuDaLogin;
import com.utils.FileUploadUtil;
import com.utils.DialogUtilsIm;

import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.util.List;

import newProject.ui.annual_meeting.RegisterBean;
import yunjing.utils.JsonUtils;

import static com.injoy.idg.R.id.date;


/**
 * User: Selson
 * Date: 2016-11-02
 * FIXME 基础资料的API接口
 */
public class BasicDataHttpHelper extends BaseAPI
{

    private static SDHttpHelper mHttpHelper;

    /**
     * @param name      标题
     * @param remark    内容
     * @param receiveId 发送人ID
     * @return 事务提交
     */
    public static FileUpload findImBusinessWorkSubmit(Application application,
                                                      String name,
                                                      String remark,
                                                      String receiveId,
                                                      boolean isOriginalImg, List<File> files, List<String> imgPaths, File
                                                              voice, FileUpload.UploadListener listener)
    {
        String url = HttpURLUtil.newInstance().append("affair").toString();

        List<SDFileListEntity> fileListEntities = toVoiceAndPicAndFile(application, isOriginalImg, files, imgPaths, voice);
        pairs.clear();

        if (StringUtils.notEmpty(name))
        {
            pairs.add(new BasicNameValuePair("name", name));
        }
        if (!TextUtils.isEmpty(remark))
        {
            pairs.add(new BasicNameValuePair("remark", remark));
        }
        if (!TextUtils.isEmpty(receiveId))
        {
            pairs.add(new BasicNameValuePair("receiveId", receiveId));
        } else
        {
            pairs.add(new BasicNameValuePair("receiveId", ""));
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        return FileUploadUtil.resumableUpload(application, fileListEntities, BusinessType.NO_KEY, null, url, params, listener);
    }

    /**
     * 公司圈提交
     *
     * @param activity
     * @param name
     * @param remark
     * @param receiveId
     * @param annex
     * @param callBack
     */
    public static void findImBusinessWorkSubmit2(Activity activity,
                                                 String name,
                                                 String remark,
                                                 String receiveId,
                                                 String annex,
                                                 SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("ddxAffair").append("save").toString();
        pairs.clear();

        if (!TextUtils.isEmpty(remark))
        {
            pairs.add(new BasicNameValuePair("remark", remark));
        }
        if (!TextUtils.isEmpty(receiveId))
        {
            pairs.add(new BasicNameValuePair("receiveIds", receiveId));
        }
        if (StringUtils.notEmpty(annex))
        {
            pairs.add(new BasicNameValuePair("annex", annex));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    public static FileUpload findImBusinessWorkJournal(Application application,
                                                       String name,
                                                       String remark,
                                                       String receiveId,
                                                       boolean isOriginalImg,
                                                       List<File> files, List<String> imgPaths,
                                                       File voice, FileUpload.UploadListener listener)
    {
        String url = HttpURLUtil.newInstance().append("workLog").toString();

        List<SDFileListEntity> fileListEntities = toVoiceAndPicAndFile(application, isOriginalImg, files, imgPaths, voice);
        pairs.clear();

        if (StringUtils.notEmpty(name))
        {
            pairs.add(new BasicNameValuePair("name", name));
        }
        if (!TextUtils.isEmpty(remark))
        {
            pairs.add(new BasicNameValuePair("remark", remark));
        }
        if (!TextUtils.isEmpty(receiveId))
        {
            pairs.add(new BasicNameValuePair("receiveId", receiveId));
        } else
        {
            pairs.add(new BasicNameValuePair("receiveId", ""));
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        return FileUploadUtil.resumableUpload(application, fileListEntities, BusinessType.NO_KEY, null, url, params, listener);
    }

    public static void findImBusinessWorkJournal2(Activity activity,
                                                  String name,
                                                  String remark,
                                                  String receiveId,
                                                  String annex, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("workLog").toString();
        pairs.clear();

        if (StringUtils.notEmpty(name))
        {
            pairs.add(new BasicNameValuePair("name", name));
        }
        if (!TextUtils.isEmpty(remark))
        {
            pairs.add(new BasicNameValuePair("remark", remark));
        }
        if (!TextUtils.isEmpty(receiveId))
        {
            pairs.add(new BasicNameValuePair("receiveId", receiveId));
        } else
        {
            pairs.add(new BasicNameValuePair("receiveId", ""));
        }
        if (StringUtils.notEmpty(annex))
        {
            pairs.add(new BasicNameValuePair("annex", annex));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }


    /**
     * 借款金额
     *
     * @return
     */
    public static FileUpload findBorrowMoneySubmit(Application application,
                                                   String name,
                                                   String remark,
                                                   String receiveId,
                                                   String money,
                                                   boolean isOriginalImg,
                                                   List<File> files, List<String> imgPaths,
                                                   File voice, FileUpload.UploadListener listener)
    {
        String url = HttpURLUtil.newInstance().append("borrowMoney").toString();

        List<SDFileListEntity> fileListEntities = toVoiceAndPicAndFile(application, isOriginalImg, files, imgPaths, voice);
        pairs.clear();

        if (StringUtils.notEmpty(name))
        {
            pairs.add(new BasicNameValuePair("name", name));
        }
        if (!TextUtils.isEmpty(remark))
        {
            pairs.add(new BasicNameValuePair("remark", remark));
        }
        if (!TextUtils.isEmpty(receiveId))
        {
            pairs.add(new BasicNameValuePair("receiveId", receiveId));
        }
        if (!TextUtils.isEmpty(money))
        {
            pairs.add(new BasicNameValuePair("money", money));
        }


        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        return FileUploadUtil.resumableUpload(application, fileListEntities, BusinessType.NO_KEY, null, url, params, listener);
    }

    public static void findBorrowMoneySubmit2(Activity activity,
                                              String name,
                                              String remark,
                                              String receiveId,
                                              String money,
                                              String annex, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("borrowMoney").toString();
        pairs.clear();

        if (StringUtils.notEmpty(name))
        {
            pairs.add(new BasicNameValuePair("name", name));
        }
        if (!TextUtils.isEmpty(remark))
        {
            pairs.add(new BasicNameValuePair("remark", remark));
        }
        if (!TextUtils.isEmpty(money))
        {
            pairs.add(new BasicNameValuePair("money", money));
        }
        if (!TextUtils.isEmpty(receiveId))
        {
            pairs.add(new BasicNameValuePair("receiveId", receiveId));
        } else
        {
            pairs.add(new BasicNameValuePair("receiveId", ""));
        }
        if (StringUtils.notEmpty(annex))
        {
            pairs.add(new BasicNameValuePair("annex", annex));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    /**
     * 业绩提交
     */
    public static FileUpload findAchievementSubmit(Application application,
                                                   String name,
                                                   String remark,
                                                   String receiveId,
                                                   String money,
                                                   boolean isOriginalImg, List<File> files, List<String> imgPaths, File voice,
                                                   FileUpload.UploadListener listener)
    {
        String url = HttpURLUtil.newInstance().append("performance").toString();

        List<SDFileListEntity> fileListEntities = toVoiceAndPicAndFile(application, isOriginalImg, files, imgPaths, voice);
        pairs.clear();

        if (StringUtils.notEmpty(name))
        {
            pairs.add(new BasicNameValuePair("name", name));
        }
        if (!TextUtils.isEmpty(remark))
        {
            pairs.add(new BasicNameValuePair("remark", remark));
        }
        if (!TextUtils.isEmpty(receiveId))
        {
            pairs.add(new BasicNameValuePair("receiveId", receiveId));
        }
        if (!TextUtils.isEmpty(money))
        {
            pairs.add(new BasicNameValuePair("money", money));
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        return FileUploadUtil.resumableUpload(application, fileListEntities, BusinessType.NO_KEY, null, url, params, listener);
    }

    /**
     * 业绩提交
     **/
    public static void findAchievementSubmit2(Activity activity,
                                              String name,
                                              String remark,
                                              String receiveId,
                                              String money,
                                              String annex, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("performance").toString();

        pairs.clear();

        if (StringUtils.notEmpty(name))
        {
            pairs.add(new BasicNameValuePair("name", name));
        }
        if (!TextUtils.isEmpty(remark))
        {
            pairs.add(new BasicNameValuePair("remark", remark));
        }
        if (!TextUtils.isEmpty(money))
        {
            pairs.add(new BasicNameValuePair("money", money));
        }
        if (!TextUtils.isEmpty(receiveId))
        {
            pairs.add(new BasicNameValuePair("receiveId", receiveId));
        } else
        {
            pairs.add(new BasicNameValuePair("receiveId", ""));
        }
        if (StringUtils.notEmpty(annex))
        {
            pairs.add(new BasicNameValuePair("annex", annex));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    /**
     * 请假提交
     *
     * @param application
     * @param listener
     * @return
     */
    public static FileUpload findLeaveSubmit(Application application,
                                             String jsonString,
                                             boolean isOriginalImg, List<File> files,
                                             List<String> imgPaths,
                                             File voice, FileUpload.UploadListener listener)
    {
        String url = HttpURLUtil.newInstance().append("holiday").toString();
        List<SDFileListEntity> fileListEntities = toVoiceAndPicAndFile(application, isOriginalImg, files, imgPaths, voice);
        pairs.clear();
        //jsonString为参数实体类，-转string的格式
        pairs = JsonUtils.getInstance().reTurnPair(jsonString);

        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        return FileUploadUtil.resumableUpload(application, fileListEntities, BusinessType.NO_KEY, null, url, params, listener);
    }

    //注册 第三部，传公司，卡号，密码等等。
    public static void postRegister3(Activity context, ParameterRegister parameterRegister, SDRequestCallBack callBack)
    {
        mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("register")
                .append("registerCompany")
                .toString();
        pairs.clear();

        if (!TextUtils.isEmpty(parameterRegister.getTelephone()))
        {
            pairs.add(new BasicNameValuePair("telephone", parameterRegister.getTelephone()));
        }

        if (!TextUtils.isEmpty(parameterRegister.getPassword()))
        {
            pairs.add(new BasicNameValuePair("password", MD5Util.MD5(MD5Util.MD5(parameterRegister.getPassword()))));
        }

        if (!TextUtils.isEmpty(parameterRegister.getCompanyName()))
        {
            pairs.add(new BasicNameValuePair("companyName", parameterRegister.getCompanyName()));
        }

        if (!TextUtils.isEmpty(parameterRegister.getUserName()))
        {
            pairs.add(new BasicNameValuePair("userName", parameterRegister.getUserName()));
        }

        if (!TextUtils.isEmpty(parameterRegister.getCardNo()))
        {
            pairs.add(new BasicNameValuePair("cardNo", parameterRegister.getCardNo()));
        }

        if (!TextUtils.isEmpty(parameterRegister.getCardPass()))
        {
            pairs.add(new BasicNameValuePair("cardPass", parameterRegister.getCardPass()));
        }

        if (!TextUtils.isEmpty(parameterRegister.getEmail()))
        {
            pairs.add(new BasicNameValuePair("email", parameterRegister.getEmail()));
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    /**
     * @param context
     * @param parameterSuDaLogin 速达登录验证
     * @param callBack
     */
    public static void postLoginSuDa(Activity context, ParameterSuDaLogin parameterSuDaLogin, final SDRequestCallBack callBack)
    {
        mHttpHelper = new SDHttpHelper(context);
        String url = "http://login.superdata.com.cn/login_post.jsp";
        StringBuffer urlBuffer = new StringBuffer(url);
        urlBuffer
                .append("?email=" + parameterSuDaLogin.getEmail())
                .append("&password=" + parameterSuDaLogin.getPassword())
                .append("&logo=SDWAP&isH5=false")
                .toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.postNotEndAndNotToken(urlBuffer.toString(), params, true, callBack);
    }

    /**
     * 公司通讯录，所有人
     * @param context
     * @param name
     * @param callBack
     */
    public static void postFindFriendList(Activity context, String name, SDRequestCallBack callBack)
    {
        //这里把客服的插入到数据库，接口更改，客服不在原来的地方了。沙比了吧。
        mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("sysuser")
                .append("list")
                .toString();
        pairs.clear();

        if (!TextUtils.isEmpty(name))
        {
            pairs.add(new BasicNameValuePair("name", name));
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, false, callBack);
    }

    //
    public static void post_New_FindFriendList(Activity context, String string1, boolean isShow,SDRequestCallBack callBack)
    {
        mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("sysuser/new/list")
                .toString();
        pairs.clear();

        if (!TextUtils.isEmpty(string1))
        {
            pairs.add(new BasicNameValuePair("string1", string1));
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.get(url, params, isShow, callBack);
    }

    /**
     * 修改信息
     */
    public static void postModifyPersonalInfo(Activity context,
                                              String name,
                                              String sex,
                                              String email,
                                              String telephone,
                                              String icon,
                                              String password,
                                              SDRequestCallBack callBack)
    {
        mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance().append("sysuser").append("update").toString();
        pairs.clear();
        if (StringUtils.notEmpty(name))
        {
            pairs.add(new BasicNameValuePair("name", name));
        }
        if (StringUtils.notEmpty(sex))
        {
            pairs.add(new BasicNameValuePair("sex", sex));
        }
        if (StringUtils.notEmpty(email))
        {
            pairs.add(new BasicNameValuePair("email", email));
        }
        if (StringUtils.notEmpty(telephone))
        {
            pairs.add(new BasicNameValuePair("telephone", telephone));
        }
        if (StringUtils.notEmpty(icon))
        {
            pairs.add(new BasicNameValuePair("icon", icon));
        }

        if (StringUtils.notEmpty(password))
        {
            pairs.add(new BasicNameValuePair("password", password));
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    /**
     * 修改信息
     */
    public static void postModifyPersonalPassword(Activity context, String password,
                                                  SDRequestCallBack callBack)
    {
        mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance().append("sysuser").append("password").toString();
        pairs.clear();
        if (StringUtils.notEmpty(password))
        {
            pairs.add(new BasicNameValuePair("password", password));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }


    /**
     * 注册-验证手机
     */
    public static void postIsHavaPhone(Activity context, String phone, SDRequestCallBack callBack)
    {
        mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("system")
                .append("isHavaPhone")
                .toString();
        pairs.clear();
        if (!TextUtils.isEmpty(phone))
        {
            pairs.add(new BasicNameValuePair("telephone", phone));
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.notTokenPost(url, params, true, callBack);
    }

    /**
     * 注册-获取图形验证码
     */
    public static void getGetCode(Activity context, String phone, SDRequestCallBack callBack)
    {
        mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("system")
                .append("getCode")
                .toString();
        pairs.clear();
        if (!TextUtils.isEmpty(phone))
        {
            pairs.add(new BasicNameValuePair("phone", phone));
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.get(url, params, true, callBack);
    }


    /**
     * 注册-验证图形验证码
     */
    public static void postCheckCode(Activity context, String codeKey, String code, SDRequestCallBack callBack)
    {
        mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("system")
                .append("checkCode")
                .toString();
        pairs.clear();
        if (StringUtils.notEmpty(codeKey))
            pairs.add(new BasicNameValuePair("codeKey", codeKey));
        if (StringUtils.notEmpty(code))
            pairs.add(new BasicNameValuePair("code", code));

        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }


    /**
     * 注册-验证短信验证码
     */
    public static void postSendMessage(Activity context, String phone, SDRequestCallBack callBack)
    {
        mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("system")
                .append("sendMessage")
                .toString();
        pairs.clear();
        if (StringUtils.notEmpty(phone))
            pairs.add(new BasicNameValuePair("phone", phone));

        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    /**
     * 注册-验证短信验证码
     */
    public static void postCheckMessage(Activity context, String phone, String messageCode, SDRequestCallBack callBack)
    {
        mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("system")
                .append("checkMessage")
                .toString();
        pairs.clear();
        if (!TextUtils.isEmpty(phone))
            pairs.add(new BasicNameValuePair("phone", phone));
        if (StringUtils.notEmpty(messageCode))
            pairs.add(new BasicNameValuePair("messageCode", messageCode));

        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    /**
     * \
     * 注册-最后一步验证
     */
    public static void postRegister(Activity context, String telephone, String password, String name, String userName,
                                    SDRequestCallBack callBack)
    {
        mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("system")
                .append("register")
                .toString();
        pairs.clear();
        if (StringUtils.notEmpty(telephone))
            pairs.add(new BasicNameValuePair("telephone", telephone));
        if (StringUtils.notEmpty(password))
            pairs.add(new BasicNameValuePair("password", MD5Util.MD5(MD5Util.MD5(password))));
        if (StringUtils.notEmpty(name))
            pairs.add(new BasicNameValuePair("name", name));
        if (StringUtils.notEmpty(userName))
            pairs.add(new BasicNameValuePair("userName", userName));
        //1=OA版本，2=OA+版本，3=CRM版本
        pairs.add(new BasicNameValuePair("level", Config.VERSION_CODE));
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    //日常会议-每天或者某个月的数据
    public static void postGMeetingMonthOrdayList(Activity context, String type, String date, SDRequestCallBack callBack)
    {
        mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("meet")
                .append("monthOrdayList")
                .append(type)
                .toString();
        pairs.clear();

        if (StringUtils.notEmpty(date))
            pairs.add(new BasicNameValuePair("date", date));

        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, false, callBack);
    }

    //日常会议-我的会议
    public static void postGMeetingMonthList(Activity context, String type, String date, boolean isAll, SDRequestCallBack
            callBack)
    {
        mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("meet")
                .append("list")
                .append("month")
                .toString();
        pairs.clear();

        if (StringUtils.notEmpty(date))
            pairs.add(new BasicNameValuePair("date", date));

        if (isAll)
            pairs.add(new BasicNameValuePair("kind", "all"));

        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    //消息
    public static void postNewsList(Activity context, String isSearch, String search, int page, SDRequestCallBack
            callBack)
    {
        mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("push")
                .append("message")
                .append("holiday")
                .append("list")
                .append(page + "")
                .toString();
        pairs.clear();

        if (StringUtils.notEmpty(isSearch))
            pairs.add(new BasicNameValuePair(Constants.IS_SEARCH_PARAM, Constants.IS_SEARCH_CONTENT));

        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    public static void postProgressList(Activity context, String isSearch, String search, int page, SDRequestCallBack
            callBack)
    {
        mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("push")
                .append("message")
                .append("progress")
                .append("list")
                .append(page + "")
                .toString();
        pairs.clear();

        if (StringUtils.notEmpty(isSearch))
            pairs.add(new BasicNameValuePair(Constants.IS_SEARCH_PARAM, Constants.IS_SEARCH_CONTENT));

        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }


    //消息
    public static void postDelNews(Activity context, String eid, SDRequestCallBack
            callBack)
    {
        mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("push")
                .append("message")
                .append("remove")
                .toString();
        pairs.clear();

        if (StringUtils.notEmpty(eid))
            pairs.add(new BasicNameValuePair("eid", eid));

        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    //年会详情
    public static void postNianInfo(Activity context, SDRequestCallBack
            callBack)
    {
        mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("annual")
                .append("meeting")
                .append("current")
                .append("detail")
                .append("sign")
                .toString();
        pairs.clear();

        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.get(url, params, true, callBack);
    }

    //弹幕发送
    public static void postNianDanmu(Activity context, String eid, String msg, SDRequestCallBack
            callBack)
    {
        mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("/annual/meeting/barrage")
                .append(eid)
                .toString();
        pairs.clear();

        if (StringUtils.notEmpty(msg))
            pairs.add(new BasicNameValuePair("msg", msg));

        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    //签到
    public static void postRegister(final Activity context, String meetId)
    {
        mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("annual")
                .append("meeting")
                .append("signin")
                .toString();
        pairs.clear();

        if (StringUtils.notEmpty(meetId))
            pairs.add(new BasicNameValuePair("meetId", meetId));

        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, new SDRequestCallBack(RegisterBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                DialogUtilsIm.dialogPayFinish(context, "提 示", msg, "确定", "", new DialogUtilsIm
                        .OnYesOrNoListener2()

                {
                    @Override
                    public void onYes()
                    {
                        context.finish();
                    }
                });
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                RegisterBean registerBean = (RegisterBean) responseInfo.getResult();
                if (registerBean.getStatus() == 400)
                {
                    DialogUtilsIm.dialogPayFinish(context, "提 示", registerBean.getMsg(), "确定", "", new DialogUtilsIm
                            .OnYesOrNoListener2()

                    {
                        @Override
                        public void onYes()
                        {
                            context.finish();
                        }
                    });

                } else if (registerBean.getStatus() == 200)
                {
                    DialogUtilsIm.dialogPayFinish(context, "提 示", registerBean.getData().getMyTable(), "确定", "", new DialogUtilsIm
                            .OnYesOrNoListener2()

                    {
                        @Override
                        public void onYes()
                        {
                            context.finish();
                        }

                    });
                }
            }
        });
    }

    //签到
    public static void postHighIcon(final Activity context, String imAccount, final OnYesOrNoListener onYesOrNoListener)
    {
        mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("sysuser/hign/icon")
                .toString();
        pairs.clear();

        if (StringUtils.notEmpty(imAccount))
            pairs.add(new BasicNameValuePair("imAccount", imAccount));

        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, new SDRequestCallBack(HighIconBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                DialogUtils.getInstance().showDialog(context, msg);
                onYesOrNoListener.onNo("");
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                HighIconBean registerBean = (HighIconBean) responseInfo.getResult();
                if (registerBean.getStatus() == 200)
                {
                    onYesOrNoListener.onYes(registerBean.getData());
                } else
                {
                    onYesOrNoListener.onNo("");
                }
            }
        });
    }

    public interface OnYesOrNoListener
    {
        void onYes(String s);

        void onNo(String s);
    }

    //为了给苹果发送推送通知用的。。。

    /**
     * @param context
     * @param type        类型
     * @param text        内容
     * @param toImAccount 对方IM
     */
    public static void postImIosPush(Activity context, String type, String text, String toImAccount)
    {
        mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("/im/chat/push")
                .toString();
        pairs.clear();

        if (StringUtils.notEmpty(type))
            pairs.add(new BasicNameValuePair("type", type));

        if (StringUtils.notEmpty(text))
            pairs.add(new BasicNameValuePair("text", text));

        if (StringUtils.notEmpty(toImAccount))
            pairs.add(new BasicNameValuePair("toImAccount", toImAccount));

        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, new SDRequestCallBack()
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {

            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {

            }
        });
    }

    public static void postMeetingtoCalendar(Activity context, String type, String date, String kind, SDRequestCallBack
            callBack)
    {
        mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("meet")
                .append("monthOrdayList")
                .append(type)
                .toString();
        pairs.clear();

        if (StringUtils.notEmpty(date))
            pairs.add(new BasicNameValuePair("date", date));

        if (StringUtils.notEmpty(kind))
            pairs.add(new BasicNameValuePair("kind", kind));

        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    /**
     * @param context
     * @param type     trave（出差）
     *                 holiday（请假）
     *                 resumption（销假）
     *                 cost（报销）
     * @param callBack
     */
    public static void getPushUnRead(Activity context, String type, SDRequestCallBack callBack)
    {
        mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance()
                .append("sysuser")
                .append("approveNum")
                .append(type)
                .toString();
        pairs.clear();

        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.get(url, params, false, callBack);
    }
}