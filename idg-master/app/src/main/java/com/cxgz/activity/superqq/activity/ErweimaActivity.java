package com.cxgz.activity.superqq.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.chaoxiang.base.utils.MD5Util;
import com.cxgz.activity.cx.dao.MyMenuItem;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.injoy.idg.R;
import com.utils.SDToast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import tablayout.view.TopMenu;

/**
 * 二维码生成界面！
 */
public class ErweimaActivity extends BaseActivity
{

    private ImageView iv_erweima;
    private Bitmap bitmap;
    private String imgPath;

    @Override
    protected void init()
    {

        iv_erweima = (ImageView) findViewById(R.id.iv_erweima);
        setLeftBack(R.drawable.folder_back);
        setTitle("二维码邀请");

        List<MyMenuItem> menuItem = new ArrayList<>();
        menuItem.add(new MyMenuItem("保存二维码图片", R.mipmap.topmenu_erweima));
        final TopMenu menu = new TopMenu(this, menuItem);

        menu.setListener(new TopMenu.MenuItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                switch (position)
                {
                    case 0:
                        saveImageToGallery(ErweimaActivity.this, bitmap);
                        break;
                }
            }
        });

        addRightBtn(R.mipmap.menu_add, new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                menu.showCenterPopupWindow(view);
            }
        });
        setBarBackGround(0xff343440);
        String text = getIntent().getStringExtra("text");
        imgPath = MD5Util.MD5(text) + ".jpg";
//
//        File appDir = new File(Environment.getExternalStorageDirectory(), "chaoxiang");
//        File file = new File(appDir, imgPath);
//        if (file.exists())
//        {
//
//        } else
//        {
            generateErweima(text);
//        }
    }

    /**
     * 生成二维码
     */
    private void generateErweima(String text)
    {
        int width = 500;
        int height = 500;
        try
        {
            // 把输入的文本转为二维码
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix martix = writer.encode(text, BarcodeFormat.QR_CODE,
                    width, height);

            System.out.println("w:" + martix.getWidth() + "h:"
                    + martix.getHeight());

            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix;

            bitMatrix = new QRCodeWriter().encode(text,
                    BarcodeFormat.QR_CODE, width, height, hints);

            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++)
            {
                for (int x = 0; x < width; x++)
                {
                    if (bitMatrix.get(x, y))
                    {
                        pixels[y * width + x] = 0xff000000;
                    } else
                    {
                        pixels[y * width + x] = 0xffffffff;
                    }

                }
            }
            bitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);

            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            iv_erweima.setImageBitmap(bitmap);
        } catch (WriterException e)
        {
            e.printStackTrace();
        }
    }

    public void saveImageToGallery(Context context, Bitmap bmp)
    {
        try
        {
            // 首先保存图片
            File appDir = new File(Environment.getExternalStorageDirectory(), "chaoxiang");
            if (!appDir.exists())
            {
                appDir.mkdir();
            }
            File file = new File(appDir, imgPath);

            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            // 其次把文件插入到系统图库
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), imgPath, null);

            // 最后通知图库更新
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file)));
            SDToast.showShort("保存成功，图片存放在" + appDir + "/" + imgPath);
        } catch (Exception e)
        {
            e.printStackTrace();
            SDToast.showShort("保存失败");
        }

    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_erweima;
    }

}
