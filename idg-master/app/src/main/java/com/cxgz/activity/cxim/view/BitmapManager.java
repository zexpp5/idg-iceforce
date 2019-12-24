package com.cxgz.activity.cxim.view;

import android.app.Application;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.injoy.idg.R;
import com.utils.CachePath;
import com.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.cxgz.activity.cxim.utils.MainTopMenuUtils.mContext;
import static com.injoy.idg.R.id.iv;

/**
 *
 */
public class BitmapManager
{
    private static List<String> msgTasks = Collections.synchronizedList
            (new ArrayList<String>());

    /**
     * 移除队列
     *
     * @param url
     */
    public static void remove(String url)
    {
        msgTasks.remove(url);
    }

    /**
     * 开启创建群组头像
     * @param context
     * @param iv
     * @param url
     */
    public static void createGroupIcon(Application context, SimpleDraweeView iv, String url, int position)
    {
        File file = new File(url);
        if (!msgTasks.contains(url) && !file.exists())
        {
            msgTasks.add(url);
            BitmapTask task = new BitmapTask(context, iv, position);
            task.execute(url);
        }
    }

    /**
     * 开启创建语音会议头像
     *
     * @param context
     * @param iv
     * @param
     */
    public static void createVoiceMeetingIcon(Application context, SimpleDraweeView iv, List<String> imAccountList, String
            fileName, int position)
    {
        File file = new File(FileUtil.getSDFolder() + "/" + CachePath.GROUP_HEADER, fileName);
//        if (!msgTasks.contains(fileName) && !file.exists())
//        {
        msgTasks.add(fileName);
        BitmapTask2 task = new BitmapTask2(context, iv, imAccountList, position);
        task.execute(fileName);
//        }
//        else
//        {
//            Glide.with(mContext)
//                    .load(file)
//                    .fitCenter()
//                    .placeholder(R.mipmap.group_icon)
//                    .error(R.mipmap.group_icon)
//                    .crossFade()
//                    .into((SimpleDraweeView) iv);
//        }
    }

    public static void removeDisk(Application app, String groupId)
    {
        String removeUrl = "file://" + FileUtil.getSDFolder() + "/" + CachePath.GROUP_HEADER + "/" + groupId;
        Fresco.getImagePipeline().evictFromDiskCache(Uri.parse(removeUrl));
        File f = new File(FileUtil.getSDFolder() + "/" + CachePath.GROUP_HEADER, groupId);
        if (f.exists())
        {
            f.delete();
            Fresco.getImagePipeline().clearMemoryCaches();
        }
    }

}
