//package com.cxgz.activity.cxim.workCircle.widgets.videolist.target;
//
//import android.view.View;
//
//
//
//import java.io.File;
//
//import static com.mikhaellopez.circularprogressbar.R.styleable.CircularProgressBar;
//
//
///**
// * @author Wayne
// */
//public class VideoProgressTarget extends ProgressTarget<String, File>
//{
//    private final CircularProgressBar progress;
//
//    public VideoProgressTarget(VideoLoadTarget target, CircularProgressBar progress)
//    {
//        super(target);
//        this.progress = progress;
//    }
//
//    @Override
//    protected void onConnecting()
//    {
//        progress.setVisibility(View.VISIBLE);
//        progress.setProgress(0);
//    }
//
//    @Override
//    protected void onDownloading(long bytesRead, long expectedLength)
//    {
//        progress.setProgress((int) (100 * bytesRead / expectedLength));
//    }
//
//    @Override
//    protected void onDownloaded()
//    {
//    }
//
//    @Override
//    protected void onDelivered()
//    {
//        progress.setVisibility(View.GONE);
//    }
//}
