package com.chaoxiang.base.config;

import android.graphics.Color;
import android.os.Environment;

import java.io.File;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-01-22
 * @desc 配置文件目录
 */
public class Config
{
    //备用     //0 内网 1测试 2正式
    public final static int CONDITION = 2;
    /**
     * 是否开启log
     */
    public final static boolean DEBUG = true;
    //是否显示验证码各种 true为显示
    public final static boolean IS_SHOW_CODE = false;
    //1=超享微OA
    public final static String VERSION_CODE = "1";  //-注册那里。

    public final static String VERSION_NAME = "";  //-登录那里。

    //分享
    public static String weiId = "wx8142d1cbe9e0e02f";
    public static String weiSecret = "373264dc6daa6c1d6b23c688b513f1cb";

    public static String qqId = "";
    public static String qqSecret = "";

    public static String sinaId = "1140947213";
    public static String sinaSecret = "a9eb9540e90e0c12e2ac3c64f2b58abe";
    //华为云

    public static String endPoint = "http://obs.myhwclouds.com";
    public static String ak = "DNZPTHWJRQK22ZOGVHNN";
    public static String sk = "OhbyTGf4gzXuxhc2FdYpC0CXUUX2v1pXyU4ugnmy";
    public static String resultPoint = "https://cxim.obs.myhwclouds.com/";


    //红点

    public static final String channelId = "deflaut";
    public static final String channelName = "com.idg.badger";
    public static boolean isShowBadger = true;
    public static boolean isShowLight = true;
    public static int lightColor = Color.RED;

    /**
     * restful环境
     */
    public static UrlConstant.RestfulEnvironment getRestfulEnvironment()
    {
        //默认测试环境
        UrlConstant.RestfulEnvironment tmpCondition = UrlConstant.RestfulEnvironment.OUTSIDE_TEST_NET;
        switch (Config.CONDITION)
        {
            case 0:
                tmpCondition = UrlConstant.RestfulEnvironment.INSIDE_NET;
                break;
            case 1:
                tmpCondition = UrlConstant.RestfulEnvironment.OUTSIDE_TEST_NET;
                break;
            case 2:
                tmpCondition = UrlConstant.RestfulEnvironment.OUTSIDE_FORMAL_NET;
                break;
        }
        return tmpCondition;
    }

    /* -----------------------缓存目录配置--------------------------  */
    /**
     * 缓存根目录-绝对路径
     * 保存在sd卡的根目录
     */
    public final static String CACHE_ROOT_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
            "cxim";
    /**
     * 图片缓存目录
     */
    public final static String CACHE_IMG_DIR = CACHE_ROOT_DIR + File.separator + "image";
    /**
     * 图片目录缓存大小
     */
    public final static int IMAGE_CACHE_SIZE = 50 * 1024 * 1024;
    /**
     * 语音缓存目录
     */
    public final static String CACHE_VOICE_DIR = CACHE_ROOT_DIR + File.separator + "voice";
    /**
     * 视频缓存目录
     */
    public final static String CACHE_VIDEO_DIR = CACHE_ROOT_DIR + File.separator + "video";
    /**
     * 文件缓存目录
     */
    public final static String CACHE_FILE_DIR = CACHE_ROOT_DIR + File.separator + "file";
    /**
     * 图片临时保存目录
     */
    public final static String CACHE_TEMP_IMG_DIR = CACHE_ROOT_DIR + File.separator + "temp";



     /* -----------------------功能配置--------------------------  */
    /**
     * 最大录音时间
     */


    public final static float MAX_SOUND_RECORD_TIME = 1 * 1000 * 60;
    /**
     * 发送消息超时时间
     */
    public final static int SEND_TIME_OUT = 10;
    /**
     * 重发次数
     */
    public final static int RESEND_COUNT = 2;
    /**
     * 会话缓存大小
     */
    public final static int CONVERSATION_MAX_SIZE = 99;

    /**
     * 检测是否需要发心跳间隔时间
     */
    public final static int HEARTBEAT_INTERVAL = 1 * 60 * 1000;
    /**
     * 触发发起心跳包次数(从0开始)
     */
    public final static int TRIGGER_HEARTBEAT_COUNT = 1;
    /**
     * 触发重连次数(从0开始)
     */
    public final static int TRIGGER_RECONN_COUNT = 1;
    /**
     * 上传失败重发次数
     */
    public static final int UPLOAD_FILE_TRY_COUNT = 3;
    /**
     * 最大上传文件大小
     */
    public static final long MAX_UPLOAD_FILE_SIZE = 20 * 1024 * 1024;
    /**
     * 断定无法连接服务器的次数(从0开始)
     */
    public static final int DISCONN_SERVER_COUNT = 1;
    /**
     * 图片压缩大小(单位kb)
     */
    public static final int COMPRESS_IMAGE_SIZE = 500;
}
