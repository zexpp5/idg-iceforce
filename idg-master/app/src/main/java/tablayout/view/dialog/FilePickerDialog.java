package tablayout.view.dialog;

import android.text.format.DateFormat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cxgz.activity.cx.base.BaseActivity;
import com.cxgz.activity.cx.utils.FileUtils;
import com.cxgz.activity.home.adapter.CommonAdapter;
import com.cxgz.activity.home.adapter.ViewHolder;
import com.entity.SDFileListEntity;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.DbException;
import com.utils.CachePath;
import com.utils.FileDownload;
import com.utils.FileDownloadUtil;
import com.utils.ReadFile;
import com.utils.SDToast;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class FilePickerDialog extends BaseActivity {
    private ListView listView;
    private CommonAdapter<SDFileListEntity> mAdapter;
    /*private DbUtils db;
    private ProgressDialog pd;*/

    @SuppressWarnings("unchecked")
	@Override
    protected void init() {
        /*db = SDDbHelp.createDbUtils(this);*/
        listView = (ListView) findViewById(R.id.lv_file);
        List<SDFileListEntity> lists = (List<SDFileListEntity>) getIntent().getSerializableExtra("list");
        mAdapter = new CommonAdapter<SDFileListEntity>(this, lists, R.layout.list_single) {
            @Override
            public void convert(ViewHolder helper, SDFileListEntity item, int position) {
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
                final SDFileListEntity entity = mAdapter.getItem(position);
                /*openFile(entity);*/
                ReadFile readFile = new ReadFile(FilePickerDialog.this,true );
                readFile.openFile(entity);
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.sd_file_picker_dialog;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }
}
