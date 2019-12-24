package newProject.mine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.bean.SuperConfigDetailBean;
import yunjing.utils.DisplayUtil;
import yunjing.utils.ToastUtils;
import yunjing.view.CustomNavigatorBar;

public class SuperConfigActivity extends AppCompatActivity {

    @Bind(R.id.title_bar)
    CustomNavigatorBar titleBar;
    @Bind(R.id.stop_tv)
    Button stopTv;
    @Bind(R.id.start_tv)
    Button startTv;
    @Bind(R.id.read_stop_tv)
    Button readStopTv;
    @Bind(R.id.read_start_tv)
    Button readStartTv;

    private String mIsRead="1";
    private String mLocation="1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_config);
        ButterKnife.bind(this);
        titleBar.setMidText("超级配置");
        titleBar.setLeftImageVisible(true);
        titleBar.setRightTextVisible(false);
        titleBar.setLeftImageOnClickListener(mOnClickListener);
        titleBar.setTitleBarBackground(DisplayUtil.mTitleColor);

        stopTv.setOnClickListener(mOnClickListener);
        startTv.setOnClickListener(mOnClickListener);
        readStopTv.setOnClickListener(mOnClickListener);
        readStartTv.setOnClickListener(mOnClickListener);

        getData();

    }

    private void getData() {
        ListHttpHelper.getConfigDetail(SuperConfigActivity.this, new SDRequestCallBack(SuperConfigDetailBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                ToastUtils.show(SuperConfigActivity.this, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                SuperConfigDetailBean bean= (SuperConfigDetailBean) responseInfo.getResult();
                if (bean!=null && bean.getData()!=null){
                    if ( bean.getData().getLocation() == 1) {
                        startTv.setTextColor(0xffffffff);
                        stopTv.setTextColor(0xff506fa6);
                        startTv.setBackgroundResource(R.drawable.shape_blue_press_btn);
                        stopTv.setBackgroundResource(R.drawable.shape_blue_normal_btn);
                    } else {
                        startTv.setTextColor(0xff506fa6);
                        stopTv.setTextColor(0xffffffff);
                        startTv.setBackgroundResource(R.drawable.shape_blue_normal_btn);
                        stopTv.setBackgroundResource(R.drawable.shape_blue_press_btn);
                    }
                    if ( bean.getData().getIsRead() == 1) {
                        readStartTv.setTextColor(0xffffffff);
                        readStopTv.setTextColor(0xff506fa6);
                        readStartTv.setBackgroundResource(R.drawable.shape_blue_press_btn);
                        readStopTv.setBackgroundResource(R.drawable.shape_blue_normal_btn);
                    } else {
                        readStartTv.setTextColor(0xff506fa6);
                        readStopTv.setTextColor(0xffffffff);
                        readStartTv.setBackgroundResource(R.drawable.shape_blue_normal_btn);
                        readStopTv.setBackgroundResource(R.drawable.shape_blue_press_btn);
                    }

                }
            }
        });
    }


    private View.OnClickListener mOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (v == titleBar.getLeftImageView())
            {
                finish();
            }else if (v==stopTv){
                mLocation="2";
                commit(mLocation,"","");
            }else if (v==startTv){
                mLocation="1";
                commit(mLocation,"","");
            }else if (v==readStopTv){
                mIsRead="2";
                commit("",mIsRead,"");
            }else if (v==readStartTv){
                mIsRead="1";
                commit("",mIsRead,"");
            }
        }
    };

    /**
     * 修改
     * @param mLocation 定位
     * @param mIsRead  已阅未阅
     * @param fingerprintLogin 指纹
     */
    public void commit(String mLocation,String mIsRead,String fingerprintLogin ){
        ListHttpHelper.commitConfigDetail(SuperConfigActivity.this, mLocation, mIsRead, fingerprintLogin, new SDRequestCallBack() {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                ToastUtils.show(SuperConfigActivity.this, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                getData();
                ToastUtils.show(SuperConfigActivity.this, "设置成功");
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
