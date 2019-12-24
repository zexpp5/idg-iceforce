package com.cxgz.activity.cxim.view;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.utils.CachePath;
import com.utils.FileUtil;

import java.util.List;

/**
 * Created by Administrator on 2015/6/26.
 */
public class BitmapTask2 extends AsyncTask<String, Integer, String>
{
    private Application context;
    private SimpleDraweeView iv;
    private int position;
    private Bitmap bm;
    List<String> imAccountList;

    public BitmapTask2(Application context, SimpleDraweeView iv, List<String> imAccountList, int position)
    {
        this.context = context;
        this.iv = iv;
        this.position = position;
        this.imAccountList = imAccountList;
    }

    @Override
    protected String doInBackground(String... params)
    {
        bm = CombineBitmapUtil.newInstance().generation(context, imAccountList, params[0]);
        return FileUtil.getSDFolder() + "/" + CachePath.GROUP_HEADER + "/" + params[0];
    }

    @Override
    protected void onPostExecute(String s)
    {
        try
        {
            if (iv != null && bm != null)
                if (position == (int) iv.getTag())
                {
                    iv.setImageBitmap(bm);
                }
        } catch (NullPointerException e)
        {
            Log.i("NullPointerException", e.getMessage());
        }
    }


}
