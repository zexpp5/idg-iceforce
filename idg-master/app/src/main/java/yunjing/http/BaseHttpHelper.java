package yunjing.http;

import android.app.Application;
import android.content.Context;

import com.entity.SDFileListEntity;
import com.erp_https.BaseAPI;
import com.http.FileUpload;
import com.http.HttpURLUtil;
import com.http.SDHttpHelper;
import com.http.callback.SDRequestCallBack;
import com.http.config.BusinessType;
import com.lidroid.xutils.http.RequestParams;
import com.utils.FileUploadUtil;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import static com.umeng.socialize.utils.DeviceConfig.context;


/**
 * User: Selson
 * Date: 2016-11-02
 */
public class BaseHttpHelper extends BaseAPI
{
    private static SDHttpHelper mHttpHelper;

    /**
     * 测试用的接口方法
     *
     * @param context
     * @param callBack
     */
    public static void testingHttp(Context context,
                                   SDRequestCallBack callBack)
    {
        SDHttpHelper mHttpHelper = new SDHttpHelper(context);
        String url = null;
        try
        {
            url = "http://192.168.101.73:9090/erp-bs/purchase/share/67/list/1.htm?s_customerName=" + URLEncoder.encode
                    (URLEncoder.encode("广西公司", "UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.get(url, params, true, callBack);
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
//        if (fileListEntities.get(0).getSrcName() != null)
//        {
//            //文件名字 ，提交附件的时候的，云镜不需要这个字段。
//            pairs.add(new BasicNameValuePair("names", fileListEntities.get(0).getSrcName()));
//        }
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        return FileUploadUtil.resumableUpload(application, fileListEntities, BusinessType.NO_KEY, null, url, params, listener);
    }


    /**
     * 通用获取审批下拉列表
     *
     * @param mHttpHelper
     * @param btype       接口文档里的全局业务type
     * @param callBack
     */
    public static void getApprovalList(SDHttpHelper mHttpHelper, int btype, SDRequestCallBack callBack)
    {
        url = HttpURLUtil.newInstance();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url.append("approvalset/getApprovalList").append(btype + "").toString(), params, true, callBack);
    }

    /**
     * 查询客户列表
     * @param mHttpHelper
     * @param business    1 市场业务 2 销售业务     客户事务查销售和市场传-1
     * @param callBack
     */
    public static void getMyCusList(SDHttpHelper mHttpHelper, int business, SDRequestCallBack callBack)
    {
        url = HttpURLUtil.newInstance();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url.append("myCus/list").append(business + "").toString(), params, true, callBack);
    }

    /**
     * 版本更新
     */
    public static void updateInfo(SDHttpHelper mHttpHelper, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("sysversion").append("findAndroid").append("1").toString();//.append("2")
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter(pairs);
        mHttpHelper.get(url, params, false, callBack);
    }

    /**
     * 获取部门所有人
     *
     * @param mHttpHelper
     * @param callBack
     */
    public static void getDepartmentPeople(SDHttpHelper mHttpHelper, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("sysDepartment").append("getDeptUser").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, false, callBack);
    }

    /**
     * 获取下属所有人
     *
     * @param mHttpHelper
     * @param callBack
     */
    public static void getPeople(SDHttpHelper mHttpHelper, SDRequestCallBack callBack)
    {
        String url = HttpURLUtil.newInstance().append("sysUser").append("getUserByLevel").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, false, callBack);
    }

    //下载例子账
    public static void postLoadExample(Context context, SDRequestCallBack callBack)
    {
        mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance().append("case").append("load").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    //清除例子账
    public static void postClearExample(Context context, SDRequestCallBack callBack)
    {
        mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance().append("case").append("clear").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

    //取消显示例子账
    public static void postCancelExample(Context context, SDRequestCallBack callBack)
    {
        mHttpHelper = new SDHttpHelper(context);
        String url = HttpURLUtil.newInstance().append("case").append("cancel").toString();
        pairs.clear();
        RequestParams params = new RequestParams();
        params.addBodyParameter(pairs);
        mHttpHelper.post(url, params, true, callBack);
    }

}