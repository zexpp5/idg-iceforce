package com.cxgz.activity.cxim.view;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;

import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.entity.group.IMGroup;
import com.chaoxiang.imsdk.group.IMGroupManager;
import com.cxgz.activity.db.dao.SDAllConstactsDao;
import com.cxgz.activity.db.dao.SDAllConstactsEntity;
import com.cxgz.activity.db.dao.SDUserDao;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.CloseableStaticBitmap;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.injoy.idg.R;
import com.superdata.im.entity.Members;
import com.utils.CachePath;
import com.utils.FileDownloadUtil;
import com.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 *
 */
public class CombineBitmapUtil
{
    private List<MyBitmapEntity> mEntityList;
    private Context context;

    public static CombineBitmapUtil newInstance()
    {
        return new CombineBitmapUtil();
    }

    /**
     * 生成图片
     *
     * @param context
     * @return
     */
    public Bitmap generation(Application context, String groupId)
    {
        this.context = context;
        IMGroup group = null;
        try
        {
            SDUserDao mUserDao = new SDUserDao(context);
            group = IMGroupManager.getInstance().getGroupsFromDB(groupId);
            //有些群组只剩下群主的没有删除，要完善。
            List<Members> groupMember = Members.parseMemberList(group.getMemberStringList());//获取群成员

            int size = groupMember.size();

            if (size >= 9)
            {
                size = 9;
            } else
            {
                Members owner = Members.getMembers(group.getOwner());//获取群成员
                groupMember.add(owner);
                size += 1;
            }
            mEntityList = getBitmapEntitys(size);
            final Bitmap[] mBitmaps = new Bitmap[size];
            // 创建单个人头像
            for (int i = 0; i < size; i++)
            {
                final int tmpI = i;
                String member = "";
                if (StringUtils.notEmpty(groupMember.get(i).getAccount()))
                {
                    member = groupMember.get(i).getAccount();
                } else
                {
                    member = groupMember.get(i).getUserId();
                }

                SDAllConstactsEntity user = SDAllConstactsDao.getInstance()
                        .findAllConstactsByImAccount(member);
//                SDUserEntity user = mUserDao.findUserByImAccount(member);
                int tmp = i / 3;
//                String Path = (Environment.getExternalStorageDirectory() + "/" + CachePath.IMG_CACHE);
                if (user != null && user.getIcon() != null && FileDownloadUtil.getDownloadIP(user.getIcon()) != null)
                {
                    final CountDownLatch latch = new CountDownLatch(1);
                    String uri = FileDownloadUtil.getDownloadIP(user.getIcon());
                    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri)).build();
                    ImagePipeline imagePipeline = Fresco.getImagePipeline();
                    DataSource<CloseableReference<CloseableImage>>
                            dataSource = imagePipeline.fetchDecodedImage(request, this);
                    dataSource.subscribe(new BaseDataSubscriber<CloseableReference<CloseableImage>>()
                                         {
                                             @Override
                                             protected void onNewResultImpl(DataSource<CloseableReference<CloseableImage>>
                                                                                    dataSource)
                                             {
                                                 CloseableReference<CloseableImage> imageReference = dataSource.getResult();
                                                 if (imageReference != null)
                                                 {
                                                     CloseableImage image;
                                                     try
                                                     {
                                                         image = imageReference.get();
                                                         CloseableStaticBitmap cb = (CloseableStaticBitmap) image;
                                                         mBitmaps[tmpI] = cb.getUnderlyingBitmap();
                                                     } finally
                                                     {
                                                         if (imageReference != null)
                                                             imageReference.close();
                                                         latch.countDown();
                                                     }
                                                 }
                                             }

                                             @Override
                                             protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>>
                                                                                  dataSource)
                                             {

                                             }

                                             @Override
                                             public void onFailure(DataSource<CloseableReference<CloseableImage>> dataSource)
                                             {
                                                 super.onFailure(dataSource);
                                                 latch.countDown();
                                             }
                                         },
                            CallerThreadExecutor.getInstance());
                    latch.await();
                }

                if (mBitmaps[tmpI] == null) // 生成默认图
                    mBitmaps[tmpI] = BitmapUtil.getScaleBitmap(context.getResources(), R.mipmap.temp_user_head);

                mBitmaps[i] = ThumbnailUtils.extractThumbnail(mBitmaps[tmpI], (int) mEntityList
                        .get(tmp).width, (int) mEntityList.get(tmp).width
                );
            }

            Bitmap combineBitmap = BitmapUtil.getCombineBitmaps(mEntityList, mBitmaps);
            for (int i = 0; i < mBitmaps.length; i++)
            {
                mBitmaps[i].recycle();
            }
            //生成头像
            try
            {
                BitmapUtil.saveMyBitmap(context, combineBitmap, groupId);
            } catch (IOException e)
            {
                e.printStackTrace();
                File file = new File(FileUtil.getSDFolder() + "/" + CachePath.GROUP_HEADER, groupId);
                if (file.exists())
                {
                    file.delete();
                }
            }
            BitmapManager.remove(groupId);
            return combineBitmap;
        } catch (Exception e)
        {
            e.printStackTrace();
            BitmapManager.remove(groupId);
            return null;
        }
    }

    /**
     * 生成图片
     *
     * @param context
     * @return userList
     */
    public Bitmap generation(Application context, List<String> userList, String fileName)
    {
        this.context = context;
        try
        {
            SDUserDao mUserDao = new SDUserDao(context);
            //有些群组只剩下群主的没有删除，要完善。
            int size = userList.size();
            if (size > 9)
            {
                size = 9;
            }
            mEntityList = getBitmapEntitys(size);
            final Bitmap[] mBitmaps = new Bitmap[size];
            // 创建单个人头像
            for (int i = 0; i < size; i++)
            {
                final int tmpI = i;
                SDUserEntity user = mUserDao.findUserByUserId(userList.get(i));
                int tmp = i / 3;
                String Path = (Environment.getExternalStorageDirectory() + "/" + CachePath.IMG_CACHE);
//                int annexWay = 0;
//                int url = -1;
                if (user == null || user.getIcon() == null || user.getIcon().equals(""))
                {

                } else
                {
//                    annexWay = Integer.parseInt(SPUtils.get(context, SPUtils.ANNEX_WAY, 0).toString());
//                    url = FileDownloadUtil.getDownloadIP(annexWay, user.getIcon()).hashCode();
                }
                if (user != null && user.getIcon() != null && FileDownloadUtil.getDownloadIP(user.getIcon()) != null)
                {
                    final CountDownLatch latch = new CountDownLatch(1);
                    String uri = FileDownloadUtil.getDownloadIP(user.getIcon());
                    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri)).build();
                    ImagePipeline imagePipeline = Fresco.getImagePipeline();
                    DataSource<CloseableReference<CloseableImage>>
                            dataSource = imagePipeline.fetchDecodedImage(request, this);
                    dataSource.subscribe(new BaseDataSubscriber<CloseableReference<CloseableImage>>()
                                         {
                                             @Override
                                             protected void onNewResultImpl(DataSource<CloseableReference<CloseableImage>>
                                                                                    dataSource)
                                             {
                                                 CloseableReference<CloseableImage> imageReference = dataSource.getResult();
                                                 if (imageReference != null)
                                                 {
                                                     CloseableImage image;
                                                     try
                                                     {
                                                         image = imageReference.get();
                                                         CloseableStaticBitmap cb = (CloseableStaticBitmap) image;
                                                         mBitmaps[tmpI] = cb.getUnderlyingBitmap();
                                                     } finally
                                                     {
                                                         if (imageReference != null)
                                                             imageReference.close();
                                                         latch.countDown();
                                                     }
                                                 }

                                             }

                                             @Override
                                             protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>>
                                                                                  dataSource)
                                             {

                                             }

                                             @Override
                                             public void onFailure(DataSource<CloseableReference<CloseableImage>> dataSource)
                                             {
                                                 super.onFailure(dataSource);
                                                 latch.countDown();
                                             }
                                         },
                            CallerThreadExecutor.getInstance());
                    latch.await();
                }

                if (mBitmaps[tmpI] == null) // 生成默认图
                    mBitmaps[tmpI] = BitmapUtil.getScaleBitmap(context.getResources(), R.mipmap.temp_user_head);

                mBitmaps[i] = ThumbnailUtils.extractThumbnail(mBitmaps[tmpI], (int) mEntityList
                        .get(tmp).width, (int) mEntityList.get(tmp).width
                );
            }

            Bitmap combineBitmap = BitmapUtil.getCombineBitmaps(mEntityList, mBitmaps);
            for (int i = 0; i < mBitmaps.length; i++)
            {
                mBitmaps[i].recycle();
            }
            //生成头像
            try
            {
                BitmapUtil.saveMyBitmap(context, combineBitmap, fileName);
            } catch (IOException e)
            {
                e.printStackTrace();
                File file = new File(FileUtil.getSDFolder() + "/" + CachePath.GROUP_HEADER, fileName);
                if (file.exists())
                {
                    file.delete();
                }
            }
            BitmapManager.remove(fileName);
            return combineBitmap;
        } catch (Exception e)
        {
            e.printStackTrace();
            BitmapManager.remove(fileName);
            return null;
        }
    }

    public static class MyBitmapEntity
    {
        float x;
        float y;
        float width;
        float height;
        static int devide = 1;
        int index = -1;

        @Override
        public String toString()
        {
            return "MyBitmap [x=" + x + ", y=" + y + ", width=" + width
                    + ", height=" + height + ", devide=" + devide + ", index="
                    + index + "]";
        }
    }

    private List<MyBitmapEntity> getBitmapEntitys(int count)
    {
        List<MyBitmapEntity> mList = new LinkedList<MyBitmapEntity>();
        String value = PropertiesUtil.readData(context, String.valueOf(count),
                R.raw.data);
        SDLogUtil.debug("value=>" + value);
        String[] arr1 = value.split(";");
        int length = arr1.length;
        for (int i = 0; i < length; i++)
        {
            String content = arr1[i];
            String[] arr2 = content.split(",");
            MyBitmapEntity entity = null;
            for (int j = 0; j < arr2.length; j++)
            {
                entity = new MyBitmapEntity();
                entity.x = Float.valueOf(arr2[0]);
                entity.y = Float.valueOf(arr2[1]);
                entity.width = Float.valueOf(arr2[2]) - 8;
                entity.height = Float.valueOf(arr2[3]) - 8;
            }
            mList.add(entity);
        }
        return mList;
    }
}
