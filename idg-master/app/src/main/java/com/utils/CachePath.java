package com.utils;

import android.os.Environment;

import java.io.File;

public class CachePath {
    public final static String ROOT_FOLDER = "chaoxiang";
    /**
     * 图片缓存目录
     */
    public final static String IMG_CACHE = ROOT_FOLDER + File.separator + "cache" + File.separator + "image";
    public final static String CACHE = ROOT_FOLDER + File.separator + "cache";
    /**
     * 语音缓存目录
     */
    public final static String VOICE_CACHE = ROOT_FOLDER + File.separator + "cache" + File.separator + "voice";
    //	public final static String CAMERA_IMG_PATH = ROOT_FOLDER + File.separator + "photo" + File.separator+"camera";
    public final static String CAMERA_IMG_PATH = "DCIM" + File.separator + "Camera";
    //	/**
//	 * 头像保存地址
//	 */
    public final static String USER_HEADER = ROOT_FOLDER + File.separator + "head";
    public final static String GROUP_HEADER = ROOT_FOLDER + File.separator + "group";
    public final static String IMAGE_CLEAN = ROOT_FOLDER + File.separator + "cleancache";
    public final static String FILE_PATH = Environment.getExternalStorageDirectory() + File.separator + ROOT_FOLDER + File.separator + "cache" + File.separator +  "file";
    public final static String LOG_FILE_PATH = ROOT_FOLDER + File.separator + "log";
}
