package com.cxgz.activity.cx.view.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.injoy.idg.R;
import com.chaoxiang.base.utils.SDLogUtil;


import tablayout.view.dialog.SDNoTileDialog;
import tablayout.view.elasticdownloadview.ElasticDownloadView;

/**
 * @author zjh
 */
public class DownloadDialog extends SDNoTileDialog
{
    ElasticDownloadView downloadView;

    public DownloadDialog(Context context)
    {
        super(context);
        init(context);
    }

    private void init(Context context)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.sd_download_dialog, null);
        downloadView = (ElasticDownloadView) view.findViewById(R.id.elastic_download_view);
        setContentView(view);
    }

    @Override
    public void show()
    {
        super.show();
        downloadView.startIntro();
    }

    public void setPercentage(long total, long progress)
    {
        float percentage = (float) (progress / (total * 1.0)) * 100;
        SDLogUtil.debug("total==" + total + ",progress=" + progress + ",percentage=" + percentage);
        downloadView.setProgress(percentage);
    }

    public void showDownloadFail()
    {
        downloadView.fail();
    }

    public void showDownloadSuccess()
    {
        downloadView.success();
    }
}
