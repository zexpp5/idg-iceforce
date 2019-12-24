package com.cxgz.activity.cx.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;

import com.chaoxiang.base.utils.SDLogUtil;
import com.cxgz.activity.db.help.SDDbHelp;
import com.cxgz.activity.cx.view.dialog.DownloadDialog;
import com.entity.SDFileListEntity;
import com.entity.administrative.employee.Annexdata;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.SqlInfo;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.utils.CachePath;
import com.utils.FileDownload;
import com.utils.FileDownloadUtil;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2015/7/2.
 */
public class ReadFile
{
    private DbUtils db;
    private DownloadDialog downloadDialog;
    private Activity context;
    private boolean exit;
    private FileDownload download;
    private final int OPEN_FIAL = 1001;
    private final int DOWN_LOAD_SUCCESS = 1002;
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if (downloadDialog.isShowing())
            {
                downloadDialog.dismiss();
            }
            if (msg.what == OPEN_FIAL)
            {
                File file = (File) msg.obj;
                if (file != null && file.exists())
                {
                    file.delete();
                    if (exit)
                    {
                        context.finish();
                    }
                }
            } else if (msg.what == DOWN_LOAD_SUCCESS)
            {
                File file = (File) msg.obj;
                FileUtils.openFile(file, context);
                if (exit) context.finish();
            }
        }
    };

    public ReadFile(Activity context, boolean exit)
    {
        this.context = context;
        this.exit = exit;
        db = SDDbHelp.createDbUtils(context);
        if (downloadDialog == null)
        {
            downloadDialog = new DownloadDialog(context);
            downloadDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
            {
                @Override
                public void onDismiss(DialogInterface dialogInterface)
                {
                    if (download != null)
                    {
                        download.cancel();
                    }
                }
            });
            downloadDialog.setCanceledOnTouchOutside(false);
        }
    }


    /**
     * 打开文件
     *
     * @param entity
     */
    public void openFile(Annexdata entity) {
        if (entity.getAndroidFilePath() != null) {
            File file = new File(entity.getAndroidFilePath());
            if (file.exists()) {
                FileUtils.openFile(file, context);
                context.finish();
                return;
            }
        }
        try {
            SDFileListEntity data = db.findFirst(Selector.from(SDFileListEntity.class).where(WhereBuilder.b("path", "=", entity.getPath())));
            if (data != null) { // 打开文件
                File file = new File(data.getAndroidFilePath());
                if (file != null && file.exists()) {
                    // 文件存在，直接打开
                    FileUtils.openFile(file, context);
                    if (exit) context.finish();

                } else { // 没有文件
                    saveFile(entity);
                }
            } else { // 表中没有此数据，下载完成后保存数据库
                saveFile(entity);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开文件
     *
     * @param entity
     */
    public void openFile(SDFileListEntity entity)
    {
        if (entity.getAndroidFilePath() != null)
        {
            File file = new File(entity.getAndroidFilePath());
            if (file.exists())
            {
                FileUtils.openFile(file, context);
                context.finish();
                return;
            }
        }
        try
        {
            SDFileListEntity data = db.findFirst(Selector.from(SDFileListEntity.class).where(WhereBuilder.b("path", "=", entity.getPath())));
            if (data != null)
            { // 打开文件
                File file = new File(data.getAndroidFilePath());
                if (file != null && file.exists())
                {
                    // 文件存在，直接打开
                    FileUtils.openFile(file, context);
                    if (exit) context.finish();

                } else
                { // 没有文件
                    saveFile(entity);
                }
            } else
            { // 表中没有此数据，下载完成后保存数据库
                saveFile(entity);
            }
        } catch (DbException e)
        {
            e.printStackTrace();
        }
    }

    private void saveFile(final Annexdata entity)
    {
        final File file = addFile(entity);
        downloadDialog.show();
        startOss(file, entity);
    }

    private void startOss(final File file, final Annexdata entity)
    {

        download = FileDownloadUtil.resumableDownload(context.getApplication(), entity.getAndroidFilePath(), entity.getPath(), entity.getAnnexWay(), "", new FileDownload.DownloadListener()
        {
            @Override
            public void onSuccess(String filePath)
            {
                try
                {
                    downloadDialog.showDownloadSuccess();
                    db.execQuery("DELETE FROM workcircle_attach WHERE path='" + entity.getPath() + "'");
                    Cursor cursor = db.execQuery(new SqlInfo("INSERT INTO workcircle_attach (fileSize,real_path,annexWay,src_name,path,showType,type) VALUES (?,?,?,?,?,?,?)",
                            new Object[]{
                                    entity.getFileSize(),
                                    entity.getAndroidFilePath(),
                                    entity.getAnnexWay(),
                                    entity.getSrcName(),
                                    entity.getPath(),
                                    entity.getShowType(),
                                    entity.getType()}
                    ));
                    SDLogUtil.debug("affect:" + cursor.getCount());
                    handler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Message message = Message.obtain();
                            message.obj = file;
                            message.what = DOWN_LOAD_SUCCESS;
                            handler.sendMessage(message);
                        }
                    }, 500);
                } catch (DbException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onProgress(int byteCount, int totalSize)
            {
                downloadDialog.setPercentage(totalSize, byteCount);
            }

            @Override
            public void onFailure(Exception ossException)
            {
                downloadDialog.showDownloadFail();
                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Message message = Message.obtain();
                        message.obj = file;
                        message.what = OPEN_FIAL;
                        handler.sendMessage(message);
                    }
                }, 500);
            }
        });
    }


    private File addFile(Annexdata entity)
    {
        String androidPath;
        String path = DateFormat.format("yyyy_MM_dd", System.currentTimeMillis()).toString();
        androidPath = CachePath.FILE_PATH + File.separator + path + File.separator + entity.getSrcName();
        final File file = createFile(androidPath);
        entity.setAndroidFilePath(file.getPath());
        return file;
    }


    private void saveFile(final SDFileListEntity entity)
    {
        final File file = addFile(entity);
        downloadDialog.show();
        startOss(file, entity);
    }




    private void startOss(final File file, final SDFileListEntity entity)
    {

        download = FileDownloadUtil.resumableDownload(context.getApplication(), entity.getAndroidFilePath(), entity.getPath(), entity.getAnnexWay(), "", new FileDownload.DownloadListener()
        {
            @Override
            public void onSuccess(String filePath)
            {
                try
                {
                    downloadDialog.showDownloadSuccess();
                    db.execQuery("DELETE FROM workcircle_attach WHERE path='" + entity.getPath() + "'");
                    Cursor cursor = db.execQuery(new SqlInfo("INSERT INTO workcircle_attach (fileSize,real_path,annexWay,src_name,path,showType,type) VALUES (?,?,?,?,?,?,?)",
                            new Object[]{
                                    entity.getFileSize(),
                                    entity.getAndroidFilePath(),
                                    entity.getAnnexWay(),
                                    entity.getSrcName(),
                                    entity.getPath(),
                                    entity.getShowType(),
                                    entity.getType()}
                    ));
                    SDLogUtil.debug("affect:" + cursor.getCount());
                    handler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Message message = Message.obtain();
                            message.obj = file;
                            message.what = DOWN_LOAD_SUCCESS;
                            handler.sendMessage(message);
                        }
                    }, 500);
                } catch (DbException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onProgress(int byteCount, int totalSize)
            {
                downloadDialog.setPercentage(totalSize, byteCount);
            }

            @Override
            public void onFailure(Exception ossException)
            {
                downloadDialog.showDownloadFail();
                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Message message = Message.obtain();
                        message.obj = file;
                        message.what = OPEN_FIAL;
                        handler.sendMessage(message);
                    }
                }, 500);
            }
        });
    }

    private File addFile(SDFileListEntity entity)
    {
        String androidPath;
        String path = DateFormat.format("yyyy_MM_dd", System.currentTimeMillis()).toString();
        androidPath = CachePath.FILE_PATH + File.separator + path + File.separator + entity.getSrcName();
        final File file = createFile(androidPath);
        entity.setAndroidFilePath(file.getPath());
        return file;
    }

    /**
     * 创建文件
     *
     * @param path
     * @return
     */
    public static File createFile(String path)
    {
        File file = new File(path);
        for (int i = 0; ; i++)
        {
            if (file.exists())
            { // 存在改名字
                StringBuilder buffer = new StringBuilder(path);
                int laPoint = path.lastIndexOf(".");
                if (laPoint == -1)
                {
                    laPoint = path.length();
                }
                buffer.insert(laPoint, " (" + i + ")");
                file = new File(buffer.toString());
            } else
            { // 不改名字
                if (!file.getParentFile().exists())
                {
                    file.getParentFile().mkdirs();
                } else
                {
                    try
                    {
                        file.createNewFile();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
        return file;
    }

}
