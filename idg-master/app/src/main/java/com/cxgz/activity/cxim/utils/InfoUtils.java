package com.cxgz.activity.cxim.utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.db.dao.SDAllConstactsDao;
import com.cxgz.activity.db.dao.SDAllConstactsEntity;
import com.cxgz.activity.db.dao.SDUserDao;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.utils.SPUtils;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by selson on 2017/11/1.
 * 信息控制工具类
 */
public class InfoUtils
{
    private InfoUtils()
    {
    }

    public static synchronized InfoUtils getInstance()
    {
        return InfoUtilsHelper.infoUtils;
    }

    private static class InfoUtilsHelper
    {
        private static final InfoUtils infoUtils = new InfoUtils();
    }

    /**
     * 设置参数
     */
    public String getParams(String companyId, String imAccount, String userName)
    {
        String cxid = "cx:injoy365.cn";
        String urlString = appendString(companyId) + appendString(imAccount) + appendString(userName) + cxid;
        return urlString;
    }

    public String appendString(String appString)
    {
        String goUrl;
        StringBuilder stringInfo = new StringBuilder();
        goUrl = stringInfo.append(appString) + "&";
        return goUrl;
    }


    public String queryPathByUri(Activity activity, Uri uri)
    {
        String imgPath = null;
        long size = 0;
        String[] proj = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.SIZE};
        Cursor cursor = activity.managedQuery(uri, proj, null, null, null);
        int dataColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        int sizeColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);

        cursor.moveToFirst();
        if (dataColumnIndex == -1)
        {
            return null;
        }
        imgPath = cursor.getString(dataColumnIndex);
        size = cursor.getLong(sizeColumnIndex);

        SDLogUtil.debug("size==" + size);
        SDLogUtil.debug("path==" + imgPath);
        if (Build.VERSION.SDK_INT < 14)
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
        if (size <= 0)
        {
            return null;
        } else
        {
            return imgPath;
        }
    }

    public String parseUri(Uri selectedUri)
    {
        String uriPath = selectedUri.getPath();
        return uriPath.substring(uriPath.indexOf(Environment.getExternalStorageDirectory().getAbsolutePath()));
    }

    /**
     * 比较通讯录等级
     */
    public boolean getRank(Context mContext, String hisImAccount)
    {   
        boolean isShow = false;
        String account = (String) SPUtils.get(mContext, SPUtils.IM_NAME, "");
        if (StringUtils.notEmpty(account))
        {
//            SDUserEntity sdUserEntity = SDUserDao.getInstance().findUserByImAccount(account);
            //替换全局的通讯录
            SDAllConstactsEntity sdUserEntity = SDAllConstactsDao.getInstance()
                    .findAllConstactsByImAccount(account);
//            SDUserEntity hisUserEntity = SDUserDao.getInstance().findUserByImAccount(hisImAccount);
            //替换全局的通讯录
            SDAllConstactsEntity hisUserEntity = SDAllConstactsDao.getInstance()
                    .findAllConstactsByImAccount(hisImAccount);
            if (StringUtils.notEmpty(sdUserEntity) && StringUtils.notEmpty(hisUserEntity))
            {
                String myCode = sdUserEntity.getCode();
                String hisCode = hisUserEntity.getCode();
                if (StringUtils.notEmpty(myCode) && StringUtils.notEmpty(hisCode))
                {
                    isShow = getCode(myCode, hisCode);
                }
            }
        }
        return isShow;
    }

    private boolean getCode(String myCode, String hisCode)
    {
        boolean result = false; // 没任何关系
        String mySubCode = subCode(myCode);
        String otherSubCode = subCode(hisCode);
        if (otherSubCode.startsWith(mySubCode))
        {
            //我是上级
//            result = 1;
            result = true;
        } else if (mySubCode.startsWith(otherSubCode))
        {
            //他是上级
//            result = 2;
            result = false;
        } else if (mySubCode.length() == mySubCode.length())
        {
            //平级
//            result = 3;
            result = true;
        }
        return result;
    }

    private String subCode(String deptCode)
    {
        while (deptCode.endsWith("00"))
        {
            deptCode = deptCode.substring(0, deptCode.length() - 2);
        }
        return deptCode;
    }


}
