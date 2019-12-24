package tablayout.view.dialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chaoxiang.base.utils.SDLogUtil;
import com.cxgz.activity.superqq.bean.SDVersionEntity;
import com.http.SDHttpHelper;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.view.HorizontalProgressBarWithNumber;


import java.io.File;

/**
 * @des 版本更新dialog
 */
public class VersionUpdateDialog extends SDNoTileDialog {
    private Context mContext;
    private TextView tvTitle;
    private TextView tvDescription;
    private Button btnCancel;
    private Button btnOk;
    private Button btnInstall;
    private HorizontalProgressBarWithNumber mProgressbar;
    private SDHttpHelper mHttpHelper;
    private SDVersionEntity mEntity;
    private String mTarget;
    private int mCurrentVersion;
    private BtnListener mListener;
    private File file;

    public VersionUpdateDialog(Context context, SDVersionEntity entity, SDHttpHelper httpHelper, String target, int versionCode, BtnListener listener) {
        super(context);
        this.mContext = context;
        this.mHttpHelper = httpHelper;
        this.mEntity = entity;
        this.mTarget = target;
        this.mListener = listener;
        this.mCurrentVersion = versionCode;
        file = new File(target);
        if(file.exists()){
            file.delete();
        }
        setCancelable(false);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.sd_version_update_dialog, null);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvDescription = (TextView) view.findViewById(R.id.tv_description);
        btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        btnOk = (Button) view.findViewById(R.id.btn_ok);
        btnInstall = (Button) view.findViewById(R.id.btn_install);
        mProgressbar = (HorizontalProgressBarWithNumber) view.findViewById(R.id.progressBar);

        //设置标题
        tvTitle.setText(String.valueOf(mEntity.getVersionName()));
        //设置说明
        tvDescription.setText(mEntity.getDescription());

        //判断当前版本是否小于或等于最小版本
        if (mCurrentVersion <= mEntity.getMinCode()) {
            //强制更新
            btnCancel.setVisibility(View.GONE);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //隐藏按钮布局
                    btnCancel.setVisibility(View.GONE);
                    btnOk.setVisibility(View.GONE);

                    //显示进度条
                    mProgressbar.setVisibility(View.VISIBLE);

                    mListener.ok(true, mEntity, mTarget);
                }
            });
        } else {
            btnCancel.setVisibility(View.VISIBLE);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.cancel();
                    dismiss();
                }
            });
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //隐藏按钮布局
                    btnCancel.setVisibility(View.GONE);
                    btnOk.setVisibility(View.GONE);

                    //显示进度条
                    mProgressbar.setVisibility(View.VISIBLE);
                    mListener.ok(false, mEntity, mTarget);
                   /* //判断是wifi情况下才进行下载
                    if (NetworkUtils.isWifi(mContext)) {
                        download();
                    } else {
                        SDToast.showShort("请在连接wifi的情况下进行");
                    }*/
                }
            });
        }
        setContentView(view);
    }

    public void download() {
        mHttpHelper.download(mEntity.getUrlLink(), null, mTarget, new RequestCallBack() {
            @Override
            public void onSuccess(ResponseInfo responseInfo) {
                //显示安装按钮
                btnInstall.setVisibility(View.VISIBLE);
                //隐藏进度条
                mProgressbar.setVisibility(View.GONE);
                //设置安装点击事件
                btnInstall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //打开安装界面
                        openFile(file);
                    }
                });
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
                SDLogUtil.debug("total=" + total);
                SDLogUtil.debug("current=" + current);
                double sum = current / (total * 1.0) * 100;
                mProgressbar.setMax(100);
                mProgressbar.setProgress((int) sum);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                SDLogUtil.error(s);
                SDLogUtil.error(String.valueOf(e.getExceptionCode()));
                if (e.getExceptionCode() == 416 && s.equals("maybe the file has downloaded completely")) {
                    openFile(file);
                }
            }
        });
    }

    public void openFile(File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }

    public interface BtnListener {
        void cancel();

        void ok(boolean resolute, SDVersionEntity entity, String target);
    }
}
