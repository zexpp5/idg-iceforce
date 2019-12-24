package tablayout.view.dialog;

import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cxgz.activity.cx.base.BaseActivity;
import com.cxgz.activity.cx.utils.ReadFile;
import com.cxgz.activity.home.adapter.CommonAdapter;
import com.cxgz.activity.home.adapter.ViewHolder;
import com.entity.administrative.employee.Annexdata;
import com.injoy.idg.R;

import java.util.List;


public class FileNewPickerDialog extends BaseActivity {
    private ListView listView;
    private CommonAdapter<Annexdata> mAdapter;
    /*private DbUtils db;
    private ProgressDialog pd;*/

    @SuppressWarnings("unchecked")
	@Override
    protected void init() {
        /*db = SDDbHelp.createDbUtils(this);*/
        listView = (ListView) findViewById(R.id.lv_file);
        List<Annexdata> lists = (List<Annexdata>) getIntent().getSerializableExtra("list");
        mAdapter = new CommonAdapter<Annexdata>(this, lists, R.layout.list_single) {
            @Override
            public void convert(ViewHolder helper, Annexdata item, int position) {
                helper.setImageResource(R.id.img, R.drawable.document);
                helper.setText(R.id.txt, item.getSrcName());
                helper.setVisibility(R.id.myCheckBox, View.GONE);
            }
        };
        /*if (pd == null) {
            pd = new ProgressDialog(this);

            pd.setCanceledOnTouchOutside(false);
        }*/
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Annexdata entity = mAdapter.getItem(position);
                /*openFile(entity);*/
                ReadFile readFile = new ReadFile(FileNewPickerDialog.this,true );
                readFile.openFile(entity);
            }
        });
    }

    /**
     * 打开文件
     * @param entity
    private void openFile(SDFileListEntity entity) {
        try {
            SDFileListEntity data = db.findFirst(Selector.from(SDFileListEntity.class).where(WhereBuilder.b("path", "=", entity.getPath())));
            if (data != null) { // 打开文件
                File file = new File(data.getAndroidFilePath());
                if (file != null && file.exists()) {
                    // 文件存在，直接打开
                    FileUtils.openFile(file, FilePickerDialog.this);
                    finish();
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

    private void saveFile(final SDFileListEntity entity) {
        final File file = addFile(entity);
        pd.show();
        startOss(file, entity);
    }


    private void startOss(final File file, final SDFileListEntity entity) {
        FileDownloadUtil.resumableDownload(getApplication(), entity.getAndroidFilePath(), entity.getPath(), entity.getAnnexWay(), "", new FileDownload.DownloadListener() {
            @Override
            public void onSuccess(String filePath) {
                try {
                    pd.dismiss();
                    db.execQuery("delete from workcircle_attach where path='" + entity.getPath() + "'");
                    db.save(entity);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            FileUtils.openFile(file, FilePickerDialog.this);
                            finish();
                        }
                    });
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onProgress(int byteCount, int totalSize) {

            }

            @Override
            public void onFailure(Exception ossException) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        SDToast.showShort("网络错误");
                        finish();
                    }
                });

            }
        });
    }

    private File addFile(SDFileListEntity entity) {
        String androidPath;
        String path = DateFormat.format("yyyy_MM_dd", System.currentTimeMillis()).toString();
        androidPath = CachePath.FILE_PATH + File.separator + path + File.separator + entity.getSrcName();
        final File file = createFile(androidPath);
        entity.setAndroidFilePath(file.getPath());
        return file;
    }

    *//**
     * 创建文件
     *
     * @param
     * @return
     *//*
    private File createFile(String path) {
        File file = new File(path);
        for (int i = 0; ; i++) {
            if (file.exists()) { // 存在改名字
                StringBuilder buffer = new StringBuilder(path);
                int laPoint = path.lastIndexOf(".");
                if (laPoint == -1) {
                    laPoint = path.length();
                }
                buffer.insert(laPoint, " (" + i + ")");
                file = new File(buffer.toString());
            } else { // 不改名字
                if(!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                } else {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
        return file;
    }*/

    @Override
    protected int getContentLayout() {
        return R.layout.sd_file_picker_dialog;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        setResult(RESULT_OK,null);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK, null);
    }
}
