package newProject.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chaoxiang.base.utils.ScreenUtils;
import com.cxgz.activity.cx.utils.baiduMap.Utils;
import com.injoy.idg.R;
import com.utils.BitmapUtil;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutionException;

import newProject.ApplyListActivity;

import static android.R.attr.width;
import static com.base.BaseApplication.applicationContext;

/**
 * Created by selson on 2018/7/5.
 */

public class ImgAsyncTaskUtils
{
    private ImgAsyncTaskUtils()
    {
    }

    public static class ImgAsyncTaskUtilsHelper
    {
        private static final ImgAsyncTaskUtils imgAsyncTaskUtils = new ImgAsyncTaskUtils();
    }

    public static synchronized ImgAsyncTaskUtils getIntance()
    {
        return ImgAsyncTaskUtilsHelper.imgAsyncTaskUtils;
    }


    public Bitmap getRoundRectBitmap(Bitmap bitmap, int radius)
    {
//        Paint paint = new Paint();
//        paint.setAntiAlias(true);
//        paint.setFilterBitmap(true);
//
//        int bmWidth = bitmap.getWidth();
//        int bmHeight = bitmap.getHeight();
//
//        final RectF rectF = new RectF(0, 0, width, height);
//
//        Canvas canvas = new Canvas(bitmap);
//
//        paint.setXfermode(null);
//        canvas.drawRoundRect(rectF, radius, radius, paint);
//        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
//        canvas.drawBitmap(bitmap, rect, rectF, paint);
        return bitmap;
    }

    public void setImageView(Context context, ImageView view, String urlString)
    {

        class ImageAnsycTask extends AsyncTask<String, Void, byte[]>
        {
            private ImageView mImageView;
            String url;
            Context mContext;
            int viewWidth = 1080;
            int realityHeiht = 1920;

            private void initView()
            {
                DisplayMetrics dm = new DisplayMetrics();
                dm = mContext.getResources().getDisplayMetrics();
                viewWidth = dm.widthPixels - ScreenUtils.dp2px(mContext, 30);
                realityHeiht = (int) (viewWidth * 598 / 1068);
            }

            public ImageAnsycTask(Context context, ImageView imageView)
            {
                mImageView = imageView;
                mContext = context;
                initView();
            }

            @Override
            protected byte[] doInBackground(String... params)
            {
                Bitmap myBitmap = null;
                try
                {
                    myBitmap = Glide.with(mContext)
                            .load(params[0])
                            .asBitmap() //必须
                            .centerCrop()
                            .into(viewWidth, realityHeiht)
                            .get();
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                } catch (ExecutionException e)
                {
                    e.printStackTrace();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                if (myBitmap != null)
                {
                    Bitmap bitmap = roundBottomBitmapByShader(myBitmap, viewWidth, realityHeiht, ScreenUtils.dp2px(mContext, 5));
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] bytes = baos.toByteArray();
                    return bytes;
                } else
                {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(byte[] bytes)
            {
                Glide.with(mContext)
                        .load(bytes)
                        .centerCrop()
                        .override(viewWidth, realityHeiht)
                        .placeholder(R.mipmap.img_default_pic)
                        .error(R.mipmap.img_default_pic)
                        .into(mImageView);
            }
        }

        ImageAnsycTask task = new ImageAnsycTask(context, view);
        task.execute(urlString);
    }

    private Bitmap roundBottomBitmapByShader(Bitmap bitmap, int outWidth, int outHeight, int radius)
    {
        if (bitmap == null)
        {
            throw new NullPointerException("图片处理出错");
        }
        // 初始化缩放比
        float widthScale = outWidth * 1.0f / bitmap.getWidth();
        float heightScale = outHeight * 1.0f / bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.setScale(widthScale, heightScale);

        // 初始化绘制纹理图
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        // 根据控件大小对纹理图进行拉伸缩放处理
        bitmapShader.setLocalMatrix(matrix);

        // 初始化目标bitmap
        Bitmap targetBitmap = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888);

        // 初始化目标画布
        Canvas targetCanvas = new Canvas(targetBitmap);

        // 初始化画笔
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(bitmapShader);

        // 利用画笔绘制底部圆角
        targetCanvas.drawRoundRect(new RectF(0, outHeight - 2 * radius, outWidth, outWidth), radius, radius, paint);

        // 利用画笔绘制顶部上面直角部分
        targetCanvas.drawRect(new RectF(0, 0, outWidth, outHeight - radius), paint);

        return targetBitmap;
    }
}
