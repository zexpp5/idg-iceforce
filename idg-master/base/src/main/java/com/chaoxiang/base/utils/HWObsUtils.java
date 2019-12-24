package com.chaoxiang.base.utils;

import android.content.Context;
import android.util.Log;

import com.chaoxiang.base.config.Config;
import com.obs.services.ObsClient;
import com.obs.services.ObsConfiguration;
import com.obs.services.exception.ObsException;
import com.obs.services.model.HeaderResponse;
import com.obs.services.model.ObjectMetadata;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by selson on 2018/12/5.
 */

public class HWObsUtils
{

    private HWObsUtils()
    {
    }

    private static class HWObsHelper
    {
        private static final HWObsUtils hWObsUtils = new HWObsUtils();
    }

    public static synchronized HWObsUtils getInstance()
    {
        return HWObsHelper.hWObsUtils;
    }

    public void initOBS()
    {
        try
        {
            // 创建ObsClient实例
            ObsClient obsClient = new ObsClient(Config.ak, Config.sk, Config.endPoint);
            obsClient.createBucket("cxim");
            // 使用访问OBS

            // 关闭obsClient

            obsClient.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public String getUrl()
    {
        String tmpAzureUrl = "";
        switch (Config.CONDITION)
        {
            case 0:
                tmpAzureUrl = "https://cxim.obs.myhwclouds.com/";
                break;
            case 1:
                tmpAzureUrl = "https://cxim.obs.myhwclouds.com/";
                break;
            case 2:
                tmpAzureUrl = "https://cxim.obs.myhwclouds.com/";
                break;
        }
        return tmpAzureUrl;
    }

    public HeaderResponse CommonUpload(String userid, File file, String fileType)
    {
        String fileName = file.getName();
        String pathString = file.getAbsolutePath();
        String fileTypeString = fileName.substring(fileName.lastIndexOf("."));
        String uploadFileName = UUID.randomUUID().toString().replace("-", "") + fileTypeString;
        ObsClient obsClient = null;
        HeaderResponse response = null;
        try
        {
            ObsConfiguration config = new ObsConfiguration();
            config.setEndPoint(Config.endPoint);
            config.setSocketTimeout(30000);
            config.setMaxErrorRetry(3);

            // 上传图片
            ObjectMetadata metadata = new ObjectMetadata();
            if (!fileType.equals(""))
            {
//                metadata.setContentType("image/jpeg");
                metadata.setContentType(fileType);
            }
////            对象数据的MD5值必须经过Base64编码。
//            metadata.setContentMd5("your md5 which should be encoded by base64");
////            自定义元数据
//            metadata.addUserMetadata("property1", "property-value1");
//            metadata.getMetadata().put("property2", "property-value2");
            if (StringUtils.notEmpty(userid))
            {
//                metadata.addUserMetadata("userid", userid);
                metadata.getMetadata().put("userid", userid);
            }
            // 创建ObsClient实例
            obsClient = new ObsClient(Config.ak, Config.sk, Config.endPoint);
            // 调用接口进行操作，例如上传对象
            response = obsClient.putObject("cxim", uploadFileName, new File(pathString), metadata);
            SDLogUtil.debug("hw_上传文件：uploadFileName=" + uploadFileName + " - pathString：" + pathString);
            SDLogUtil.debug("hw_上传结果：" + response);
            Log.i("PutObject", response + "");
        } catch (ObsException e)
        {
            response = null;
            Log.e("PutObject", "Response Code: " + e.getResponseCode());
            Log.e("PutObject", "Error Message: " + e.getErrorMessage());
            Log.e("PutObject", "Error Code:       " + e.getErrorCode());
            Log.e("PutObject", "Request ID:      " + e.getErrorRequestId());
            Log.e("PutObject", "Host ID:           " + e.getErrorHostId());
        } finally
        {
            // 关闭obsClient
            if (obsClient != null)
            {
                try
                {
                    obsClient.close();
                } catch (IOException e)
                {
                }
            }
        }
        return response;
    }

}
