package com.entity;

import android.content.Context;

import com.chaoxiang.base.utils.MD5Util;
import com.google.gson.annotations.Expose;
import com.chaoxiang.base.config.Constants;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
import com.utils.SPUtils;

import java.io.File;
import java.io.Serializable;

/**
 * 附件列表
 *
 * @author Amy
 * @date 20150513
 */
@Table(name = "workcircle_attach")
public class SDFileListEntity implements Serializable
{
    /**
     * 图片
     */
    public static final int IMAGE = 0;
    /**
     * 录音
     */
    public static final int VOICE = 1;
    /**
     * 附件
     */
    public static final int FILE = 2;
    /**
     * 头像
     */
    public static final int ICON = 4;
    /**
     * 附件
     */
    public static final int IDG_FILE = 10086;


    /**
     * 工作圈背景
     */
    public static final int IMG_BG = 5;

    @Column(column = "id")
    private long id;

    // 路径(服务器的路径)
    @Column(column = "path")
    @Expose
    private String path;
    // 真实文件名
    @Expose
    private long fileSize;
    /**
     * 小图图片地址
     */
    private String pathSmall;

    private int annexWay;
    /**
     * 1为附件,2为直接显示
     */
    private int showType;
    /**
     * 是否为原图
     */
    private boolean isOriginalImg;

    public int getAnnexWay()
    {
        return annexWay;
    }

    public void setAnnexWay(int annexWay)
    {
        this.annexWay = annexWay;
    }

    @Column(column = "src_name")
    @Expose
    private String srcName;
    // 文件类型
    @Column(column = "type")
    @Expose
    private String type;

    @Column(column = "real_path")
    private String androidFilePath;

    public long getFileSize()
    {
        return fileSize;
    }

    public void setFileSize(long fileSize)
    {
        this.fileSize = fileSize;
    }

    public String getSrcName()
    {
        return srcName;
    }

    public void setSrcName(String srcName)
    {
        this.srcName = srcName;
    }

    public String getAndroidFilePath()
    {
        return androidFilePath;
    }

    public void setAndroidFilePath(String androidFilePath)
    {
        this.androidFilePath = androidFilePath;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String Type)
    {
        this.type = Type;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    /**
     * 设置上传的实体
     *
     * @param context
     * @param file
     * @param uploadPath
     */
    public void setEntity(Context context, File file, String uploadPath, int type)
    {
        String fileName = file.getName();
//        setPath(String.valueOf(SPUtils.get(context, SPUtils.COMPANY_ID, -1)) + "/" + uploadPath + "/" + MD5Util.MD5(fileName)
// + String.valueOf(SPUtils.get(context, SPUtils.COMPANY_ID, -1)) + System.currentTimeMillis());
        setPath(String.valueOf("") + "/" + uploadPath + "/" + MD5Util.MD5(fileName) + String.valueOf("") + System
                .currentTimeMillis());

        String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
        setType(prefix);
        String pf = null;
        if (type == IMAGE)
        {
            pf = Constants.IMAGE_PREFIX;
        } else if (type == VOICE)
        {
            pf = Constants.RADIO_PREFIX;
        } else if (type == FILE)
        {
            pf = Constants.FILE_PREFIX;
        } else if (type == ICON)
        {
            pf = Constants.MINE_ICON_PREFIX;
        } else if (type == IMG_BG)
        {
            pf = Constants.WORK_CIRCLE_PREFIX;
        } else if (type == IDG_FILE)
        {
            pf = Constants.IDG_FILE_TYPE;
        }

        setFileSize(file.length());
        setSrcName(fileName + pf);
        setAndroidFilePath(file.getAbsolutePath());
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public int getShowType()
    {
        return showType;
    }

    public void setShowType(int showType)
    {
        this.showType = showType;
    }

    public String getPathSmall()
    {
        return pathSmall;
    }

    public void setPathSmall(String pathSmall)
    {
        this.pathSmall = pathSmall;
    }

    public boolean isOriginalImg()
    {
        return isOriginalImg;
    }

    public void setIsOriginalImg(boolean isOriginalImg)
    {
        this.isOriginalImg = isOriginalImg;
    }

    @Override
    public String toString()
    {
        return "SDFileListEntity{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", fileSize=" + fileSize +
                ", pathSmall='" + pathSmall + '\'' +
                ", annexWay=" + annexWay +
                ", showType=" + showType +
                ", isOriginalImg=" + isOriginalImg +
                ", srcName='" + srcName + '\'' +
                ", type='" + type + '\'' +
                ", androidFilePath='" + androidFilePath + '\'' +
                '}';
    }
}
