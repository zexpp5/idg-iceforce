package com.utils;

import android.app.Activity;

import com.entity.SDFileListEntity;

/**
 * Created by Administrator on 2015/7/2.
 */
public class OpenFileUtil {

    public static void openFile(Activity context, boolean exit, SDFileListEntity entity) {
        ReadFile read = new ReadFile(context,exit);
        read.openFile(entity);
    }

}
