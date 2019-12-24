package com.cxgz.activity.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.injoy.idg.R;
import com.chaoxiang.base.utils.SDLogUtil;
import com.cxgz.activity.cx.view.photodraweeview.OnPhotoTapListener;
import com.cxgz.activity.cx.view.photodraweeview.OnViewTapListener;
import com.cxgz.activity.cx.view.photodraweeview.PhotoDraweeView;
import com.cxgz.activity.cxim.utils.DensityUtil;
import com.cxgz.activity.db.dao.SDDepartmentDao;
import com.cxgz.activity.db.dao.SDUserDao;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.entity.SDReplyEntity;
import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.cache.common.WriterCallback;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.utils.AnimUtils;
import com.utils.SDToast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author zjh
 * @name SDImgHelper
 * @description 图片操作帮助类
 * @date 2015年4月6日
 */
public class SDImgHelper
{
    private static Context mContext;
    private SDDepartmentDao departmentDao;
    private SDUserDao userDao;

    private SDImgHelper(Context context)
    {
        departmentDao = new SDDepartmentDao(context);
        userDao = new SDUserDao(context);
    }

    private static SDImgHelper imgHelper;

    public static SDImgHelper getInstance(Context context)
    {
        if (imgHelper == null)
        {
            imgHelper = new SDImgHelper(context);
            mContext = context;
        }
        return imgHelper;
    }

    /**
     * @param bigImageUrl
     * @param smallPath
     * @param mPhotoDraweeView
     * @param progressBar
     * @description 加载大图
     */
    public void loadBigImage(final Activity activity, String bigImageUrl, String smallPath, final PhotoDraweeView mPhotoDraweeView, final ProgressBar progressBar)
    {
        Uri smallUri = getUri(smallPath);
        Uri bigUri = getUri(bigImageUrl);

        mPhotoDraweeView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mPhotoDraweeView.getLayoutParams().height = DensityUtil.dip2px(mContext, 80);
        mPhotoDraweeView.getLayoutParams().width = DensityUtil.dip2px(mContext, 80);

        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>()
        {
            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable)
            {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (imageInfo == null || mPhotoDraweeView == null)
                {
                    return;
                }

                mPhotoDraweeView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                mPhotoDraweeView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                mPhotoDraweeView.setAnimation(AnimUtils.getScaleAnimation(300));
                progressBar.setVisibility(View.GONE);
                mPhotoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());

            }

            @Override
            public void onFailure(String id, Throwable throwable)
            {
                super.onFailure(id, throwable);
                progressBar.setVisibility(View.GONE);
                SDLogUtil.error("图片无法显示");
                SDToast.showLong("图片无法显示");
            }


        };

        mPhotoDraweeView.setOnPhotoTapListener(new OnPhotoTapListener()
        {
            @Override
            public void onPhotoTap(View view, float x, float y)
            {
                activity.finish();
                activity.overridePendingTransition(R.anim.look_big_img_in, R.anim.look_big_img_out);
            }
        });

        mPhotoDraweeView.setOnViewTapListener(new OnViewTapListener()
        {
            @Override
            public void onViewTap(View view, float x, float y)
            {
                SDLogUtil.debug("onViewTap");
            }
        });

        mPhotoDraweeView.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                SDLogUtil.debug("onLongClick");
                return true;
            }
        });


        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(mPhotoDraweeView.getController())
                .setControllerListener(controllerListener)
                .setLowResImageRequest(ImageRequest.fromUri(smallUri))
                .setImageRequest(ImageRequest.fromUri(bigUri))
                .build();

        mPhotoDraweeView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
        mPhotoDraweeView.setController(controller);
    }

    public void loadSmallImg(String smallImageUrl, final int resId, SimpleDraweeView draweeView)
    {
        if (smallImageUrl != null && !smallImageUrl.equals(""))
        {
            Uri uri = getUri(smallImageUrl);
            int widht = draweeView.getLayoutParams().width;
            int height = draweeView.getLayoutParams().height;
            SDLogUtil.debug("draweeView widht=" + widht);
            SDLogUtil.debug("draweeView height=" + height);
            Drawable d = mContext.getResources().getDrawable(resId);
            ImageRequest request = null;
            if (widht > 0 && height > 0)
            {
                request = ImageRequestBuilder.newBuilderWithSource(uri)
                        .setAutoRotateEnabled(true)
                        .setResizeOptions(new ResizeOptions(widht, height))
                        .build();
            }
            PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(draweeView.getController())
                    .setUri(uri);
            if (request != null)
            {
                controller.setImageRequest(request);
            }
            SDLogUtil.debug("filePath: " + SDImgHelper.getInstance(mContext).getImagePath(smallImageUrl));
            loadSmallImg(getHierarchyBuilder().setFailureImage(d).setPlaceholderImage(d), controller.build(), draweeView);
        } else
        {
            loadSmallImg(resId, draweeView);
        }
    }

    /**
     * 获取uri
     *
     * @param imageUrl
     * @return
     */
    private Uri getUri(String imageUrl)
    {
        if (imageUrl == null)
        {
            return null;
        }
        if (!imageUrl.startsWith("http"))
        {
            return Uri.fromFile(new File(imageUrl));
        }
        SDLogUtil.debug("imgeUrl ==>" + imageUrl);
        return Uri.parse(imageUrl);
    }


    /**
     * 圆角配置
     *
     * @param f
     * @return
     */
    public GenericDraweeHierarchyBuilder getCircleBuilder(float f, int resId)
    {
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(f);
        circleBuilder = getNewHierarchyBuilder();
        circleBuilder.setRoundingParams(roundingParams);
        Drawable d = mContext.getResources().getDrawable(resId);
        circleBuilder.setFailureImage(d).setPlaceholderImage(d);
        return circleBuilder;
    }

    /**
     * 圆角配置
     *
     * @param resId
     * @return
     */
    public GenericDraweeHierarchyBuilder getRoundBuilder(int resId)
    {
        RoundingParams roundingParams = RoundingParams.asCircle();
        circleBuilder = getNewHierarchyBuilder();
//        roundingParams.setBorder(R.color.red, 1.0f);
        roundingParams.setRoundAsCircle(true);
        circleBuilder.setRoundingParams(roundingParams);
        Drawable d = mContext.getResources().getDrawable(resId);
        circleBuilder.setFailureImage(d).setPlaceholderImage(d);
        return circleBuilder;
    }

    private static GenericDraweeHierarchyBuilder builder;
    private static GenericDraweeHierarchyBuilder circleBuilder;

    /**
     * 默认配置
     *
     * @return
     */
    public synchronized GenericDraweeHierarchyBuilder getHierarchyBuilder()
    {
        if (builder == null)
        {
            builder = new GenericDraweeHierarchyBuilder(mContext.getResources());
        }
        builder.setFadeDuration(100);
        return builder;
    }

    public GenericDraweeHierarchyBuilder getNewHierarchyBuilder()
    {
        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(mContext.getResources());
        builder.setFadeDuration(100);
        return builder;
    }

    public void loadSmallImg(GenericDraweeHierarchyBuilder builder, DraweeController controller, SimpleDraweeView draweeView)
    {
        if (controller != null)
        {
            draweeView.setHierarchy(builder.build());
            draweeView.setController(controller);
        }
    }

    public void loadSmallImg(String smallImageUrl, GenericDraweeHierarchyBuilder builder, SimpleDraweeView draweeView)
    {
        if (smallImageUrl != null && !smallImageUrl.equals(""))
        {
            Uri uri = getUri(smallImageUrl);
            draweeView.setHierarchy(builder.build());
            draweeView.setImageURI(uri);
        } else
        {
            loadSmallImg(R.mipmap.load_img_init, draweeView);
        }
    }


    public void loadSmallImg(DraweeController controller, SimpleDraweeView view)
    {
        Drawable d = mContext.getResources().getDrawable(R.mipmap.load_img_init);
        loadSmallImg(getHierarchyBuilder().setFailureImage(d).setPlaceholderImage(d), controller, view);
    }

    /**
     * 画圆角小图
     *
     * @param smallImageUrl
     * @param resId
     * @param circle
     * @param draweeView
     */
    public void loadCircleSmallImg(String smallImageUrl, final int resId, int circle, SimpleDraweeView draweeView)
    {
        if (smallImageUrl != null && !smallImageUrl.equals(""))
        {
            Uri uri = getUri(smallImageUrl);
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder().setOldController(draweeView.getController()).setImageRequest(request).build();
            loadSmallImg(getCircleBuilder(circle, resId), controller, draweeView);
        } else
        {
            loadSmallImg(resId, draweeView);
        }
    }

    public void loadRoundSmallImg(String smallImageUrl, final int resId, SimpleDraweeView draweeView)
    {
        if (smallImageUrl != null && !smallImageUrl.equals(""))
        {
            Uri uri = getUri(smallImageUrl);
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder().setOldController(draweeView.getController()).setImageRequest(request).build();
            loadSmallImg(getRoundBuilder(resId), controller, draweeView);
        } else
        {
            loadSmallImg(resId, draweeView);
        }
    }

    /**
     * 获取图片文件路径
     *
     * @param url
     * @return
     */
    public String getImagePath(String url)
    {
        try
        {
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url)).build();
            CacheKey cacheKey = DefaultCacheKeyFactory.getInstance()
                    .getEncodedCacheKey(request);
            BinaryResource resource = ImagePipelineFactory.getInstance()
                    .getMainDiskStorageCache().getResource(cacheKey);
            final File file = ((FileBinaryResource) resource).getFile();
            return file.getAbsolutePath();
        } catch (Exception e)
        {
            return null;
        }
    }

    public void addBitmapToDisk(String url, final Bitmap bm)
    {
        try
        {
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url)).build();
            CacheKey cacheKey = DefaultCacheKeyFactory.getInstance()
                    .getEncodedCacheKey(request);
            ImagePipelineFactory.getInstance().getMainDiskStorageCache().insert(cacheKey, new WriterCallback()
            {
                @Override
                public void write(OutputStream outputStream) throws IOException
                {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    outputStream.write(baos.toByteArray());
                    outputStream.flush();
                }
            });
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * @param smallImageUrl
     * @param draweeView
     * @description 加载小图
     */
    public void loadSmallImg(String smallImageUrl, SimpleDraweeView draweeView)
    {
        loadSmallImg(smallImageUrl, R.mipmap.load_img_init, draweeView);
    }

    /**
     * @param smallImageUrl
     * @param draweeView
     * @description 加载小图
     */
    public void loadSmallImg(String smallImageUrl, SimpleDraweeView draweeView, ControllerListener controllerListener)
    {
        Uri uri = getUri(smallImageUrl);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setControllerListener(controllerListener)
                .setUri(uri)
                .build();
        loadSmallImg(controller, draweeView);
    }

    /**
     * @param resId
     * @param draweeView
     * @description 加载小图
     */
    public void loadSmallImg(int resId, SimpleDraweeView draweeView)
    {
//        draweeView.getHierarchy().setPlaceholderImage(resId);
        draweeView.setImageResource(resId);
    }


    /**
     * 获取图片并插入EditText
     */
    public ImageSpan insertEditText(TextView view, String str, boolean isNeedBlack, View.OnClickListener listener)
    {
        try
        {
            Bitmap imgBitmap = createBitmap(str.trim());
            if (imgBitmap != null)
            {
                view.setMovementMethod(LinkMovementMethod.getInstance());
                // 根据Bitmap对象创建ImageSpan对象
                ImageSpan imageSpan = new ImageSpan(mContext, imgBitmap);

                // 创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
                SpannableString spannableString = new SpannableString(str);
                // 用ImageSpan对象替换face
                spannableString.setSpan(imageSpan, 0, (str).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                // 将选择的图片追加到EditText中光标所在位置
                int index = view.getSelectionStart(); // 获取光标所在位置
                Editable edit_text = view.getEditableText();
                if (index < 0 || index >= edit_text.length())
                {
                    edit_text.append(spannableString);
                    if (isNeedBlack) edit_text.append(" ");
                } else
                {
                    edit_text.insert(index, spannableString);
                    if (isNeedBlack) edit_text.insert(index + (str).length(), " ");
                }

                setClickSpan(view, imageSpan, listener);
                return imageSpan;
            } else
            {
                Log.i("WorkCircleMainActivity", "插入失败");
                return null;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取图片并插入EditText
     */
    public ImageSpan replaceEditText(TextView view, String str, int start, int end, boolean isNeedBlack, View.OnClickListener listener)
    {
        try
        {
            Bitmap imgBitmap = createBitmap(str);
            if (imgBitmap != null)
            {
                view.setMovementMethod(LinkMovementMethod.getInstance());
                // 根据Bitmap对象创建ImageSpan对象
                ImageSpan imageSpan = new ImageSpan(mContext, imgBitmap);

                // 创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
                SpannableString spannableString = new SpannableString(str);
                // 用ImageSpan对象替换face
                spannableString.setSpan(imageSpan, 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                Editable edit_text = view.getEditableText();
                edit_text.replace(start, end, spannableString);
                if (isNeedBlack) edit_text.insert(start + str.length(), " ");
                setClickSpan(view, imageSpan, listener);
                return imageSpan;
            } else
            {
                Log.i("WorkCircleMainActivity", "插入失败");
                return null;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取图片并插入EditText
     */
    public ImageSpan replaceEditText(TextView view, final SDReplyEntity.At ats, String str, int start)
    {
        try
        {
            Bitmap imgBitmap = createBitmap(str);
            ImageSpan imageSpan;
            if (imgBitmap != null)
            {
                view.setMovementMethod(LinkMovementMethod.getInstance());
                // 根据Bitmap对象创建ImageSpan对象
                imageSpan = new ImageSpan(mContext, imgBitmap);

                if (start <= ats.getStart() && start + view.length() > ats.getStart())
                {
                    // 创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
                    SpannableString spannableString = new SpannableString(str);
                    // 用ImageSpan对象替换face
                    spannableString.setSpan(imageSpan, 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    Editable edit_text = view.getEditableText();
                    edit_text.replace(ats.getStart() - start, ats.getEnd() - start + 1, spannableString);
                    setClickSpan(view, imageSpan, new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            if (ats.getAtuid() > 0)
                            {
//                                IntentUtils.getInstance().startPersonalActivity(mContext, ats.getAtuid());
                            } else
                            {
                                List<SDUserEntity> userEntities;
                                if (ats.getAtuid() == 0)
                                {
                                    userEntities = userDao.findAllUser();
                                } else
                                {
                                    userEntities = departmentDao.findUserByDepartmentId(String.valueOf(-ats.getAtuid()));
                                }
//                                Intent intent = new Intent(mContext, SDContactList.class);
//                                intent.putExtra("type", 1);
//                                intent.putExtra("list", (Serializable) userEntities);
//                                mContext.startActivity(intent);
                            }
                        }
                    });
                }

                return imageSpan;
            } else
            {
                Log.i("WorkCircleMainActivity", "插入失败");
                return null;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 设置点击事件
     */
    private void setClickSpan(TextView view, ImageSpan imageSpan, final View.OnClickListener listener) throws Exception
    {
        Spanned s = (Spanned) view.getText();
        final int start = s.getSpanStart(imageSpan);
        final int end = s.getSpanEnd(imageSpan);
        ClickableSpan click_span = new ClickableSpan()
        {
            @Override
            public void onClick(View widget)
            {
                if (listener != null)
                {
                    listener.onClick(widget);
                }
            }
        };
        ClickableSpan[] click_spans = s.getSpans(start, end, ClickableSpan.class);
        if (click_spans.length != 0)
        {
            for (ClickableSpan c_span : click_spans)
            {
                ((Spannable) s).removeSpan(c_span);
            }
        }
        ((Spannable) s).setSpan(click_span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }


    /**
     * 字符串转换成图片
     *
     * @param str
     * @return
     */
    public Bitmap createBitmap(String str)
    {
        return createBitmap(str, DensityUtil.dip2px(mContext, 23));
    }

    /**
     * 字符串转换成图片
     * @param str
     * @return
     */
    public Bitmap createBitmap(String str, int textHight)
    {
        Paint paint = new Paint();
        paint.measureText(str);
        paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15, mContext.getResources().getDisplayMetrics()));// 字体大小
//		paint.setFakeBoldText(true);
        paint.setColor(Color.rgb(25, 141, 237));
        paint.setAntiAlias(true);
        paint.setTextSkewX(0);// 斜度
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        int w = (int) paint.measureText(str);
//		int h = DensityUtil.dip2px(mContext,rect.height());
        int h = textHight;

        Bitmap bp = Bitmap.createBitmap(w, h, Config.ARGB_8888); // 画布大小
        Canvas c = new Canvas(bp);
        Paint paint2 = new Paint();// 画姓名前边的间隔
        paint2.setColor(Color.TRANSPARENT);
        c.drawRect(2, 2, w - 2, h - 2, paint2);
        Rect targetRect = new Rect(0, 0, w, h);
        FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int baseline = targetRect.top
                + (targetRect.bottom - targetRect.top - fontMetrics.bottom + fontMetrics.top)
                / 2 - fontMetrics.top;
        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
        paint.setTextAlign(Paint.Align.CENTER);
        c.drawText(str, targetRect.centerX(), baseline, paint);// 文字位置
        c.save(Canvas.ALL_SAVE_FLAG);// 保存
        c.restore();
        return bp;
    }

}
