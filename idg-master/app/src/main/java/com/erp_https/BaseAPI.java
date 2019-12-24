package com.erp_https;

import android.app.Application;

import com.chaoxiang.base.utils.SDLogUtil;
import com.entity.SDFileListEntity;
import com.http.FileUpload;
import com.http.HttpURLUtil;
import com.utils.FileUploadPath;

import org.apache.http.NameValuePair;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.http.callback.SDHttpRequestCallBack.application;

/**
 *
 */
public class BaseAPI
{

    protected static List<NameValuePair> pairs = new ArrayList<>();
    protected static HttpURLUtil url = HttpURLUtil.newInstance();
    protected FileUpload upload = null;

    protected static List<SDFileListEntity> toVoiceAndPicAndFile(Application context, boolean isOriginalImg, List<File> files,
                                                                 List<String> imgPaths, File voice)
    {
        List<SDFileListEntity> fileListEntities = new ArrayList<SDFileListEntity>();
        if (files != null && !files.isEmpty())
        {
            for (File file : files)
            {
                SDFileListEntity fileListEntity = new SDFileListEntity();
                fileListEntity.setEntity(context.getApplicationContext(), file, FileUploadPath.SHARE, SDFileListEntity.FILE);
                fileListEntities.add(fileListEntity);
            }
        }

        if (imgPaths != null)
        {
            for (String imgPath : imgPaths)
            {
                SDFileListEntity fileListEntity = new SDFileListEntity();
                fileListEntity.setEntity(context.getApplicationContext(), new File(imgPath), FileUploadPath.SHARE,
                        SDFileListEntity.IMAGE);
                fileListEntity.setIsOriginalImg(isOriginalImg);
                fileListEntities.add(fileListEntity);
            }
        }

        if (voice != null)
        {
            SDFileListEntity fileListEntity = new SDFileListEntity();
            fileListEntity.setEntity(context.getApplicationContext(), voice, FileUploadPath.SHARE, SDFileListEntity.VOICE);
            fileListEntities.add(fileListEntity);
        }
        return fileListEntities;
    }

    protected static List<SDFileListEntity> toVoiceAndPicAndFile2(Application context, boolean isOriginalImg, List<File> files,
                                                                  List<String> imgPaths, File voice)
    {
        List<SDFileListEntity> fileListEntities = new ArrayList<SDFileListEntity>();
        if (files != null && !files.isEmpty())
        {
            for (File file : files)
            {
                SDFileListEntity fileListEntity = new SDFileListEntity();
                fileListEntity.setEntity(context.getApplicationContext(), file, FileUploadPath.SHARE, SDFileListEntity.FILE);
                fileListEntities.add(fileListEntity);
            }
        }
        //提交个人头像
        if (imgPaths != null)
        {
            for (String imgPath : imgPaths)
            {
                SDFileListEntity fileListEntity = new SDFileListEntity();
                SDLogUtil.syso("uploadImg===" + imgPath);
                fileListEntity.setEntity(context.getApplicationContext(), new File(imgPath), FileUploadPath.PERSONAL_ICON,
                        SDFileListEntity.IMAGE);
                fileListEntity.setIsOriginalImg(isOriginalImg);
                fileListEntities.add(fileListEntity);
            }
        }

        if (voice != null)
        {
            SDFileListEntity fileListEntity = new SDFileListEntity();
            fileListEntity.setEntity(context.getApplicationContext(), voice, FileUploadPath.SHARE, SDFileListEntity.VOICE);
            fileListEntities.add(fileListEntity);
        }
        return fileListEntities;
    }

    protected static List<SDFileListEntity> toVoiceAndPicAndFile3(Application context, boolean isOriginalImg, List<File> files,
                                                                  List<String> imgPaths, File voice)
    {
        List<SDFileListEntity> fileListEntities = new ArrayList<SDFileListEntity>();
        if (files != null && !files.isEmpty())
        {
            for (File file : files)
            {
                SDFileListEntity fileListEntity = new SDFileListEntity();
                fileListEntity.setEntity(context.getApplicationContext(), file, FileUploadPath.SHARE, SDFileListEntity.IDG_FILE);
                fileListEntities.add(fileListEntity);
            }
        }

        if (imgPaths != null)
        {
            for (String imgPath : imgPaths)
            {
                SDFileListEntity fileListEntity = new SDFileListEntity();
                fileListEntity.setEntity(context.getApplicationContext(), new File(imgPath), FileUploadPath.SHARE,
                        SDFileListEntity.IDG_FILE);
                fileListEntity.setIsOriginalImg(isOriginalImg);
                fileListEntities.add(fileListEntity);
            }
        }

//        if (voice != null)
//        {
//            SDFileListEntity fileListEntity = new SDFileListEntity();
//            fileListEntity.setEntity(context.getApplicationContext(), voice, FileUploadPath.SHARE, SDFileListEntity.VOICE);
//            fileListEntities.add(fileListEntity);
//        }
        return fileListEntities;
    }

}
