package com.cxgz.activity.cxim;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.imsdk.chat.CXChatManager;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.cxgz.activity.cxim.utils.SDTimerTask;
import com.injoy.idg.R;
import com.superdata.im.entity.CxMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * @date 2016-09-18
 * @desc 查看大图
 */
public class BigCountDownImgActivity extends BaseActivity {
    private ImageView iv;
    public final static String EXTR_PATH = "extr_path";
    public final static String EXTR_MSG = "extr_msg";
    public final static String EXTR_POSITION = "extr_position";
    public static final int RESULT_CODE_HOT_CHAT_DELETE_PICTURE = 26;
    private ProgressBar pb;
    private TextView tv_countdown;
    String path;
    private HashMap<Integer, SDTimerTask> taskHashMap = new HashMap<>();
    private SDTimerTask timerTask = null;
    private CxMessage cxMessage;
    private String msgId;
    private int countTime = 10;
    private int position = 0;

    @Override
    protected void init() {
        addLeftBtn(R.drawable.folder_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BigCountDownImgActivity.this.finish();
            }
        });

        setTitle("图片");


        Bundle bundle = getIntent().getExtras();
        cxMessage = (CxMessage)getIntent().getParcelableExtra(EXTR_MSG);
         //bundle.getParcelable(EXTR_MSG);
        path = bundle.getString(EXTR_PATH);
        position = bundle.getInt(EXTR_POSITION);

        iv = (ImageView) findViewById(R.id.iv);
        pb = (ProgressBar) findViewById(R.id.pb);
        tv_countdown = (TextView) findViewById(R.id.tv_countdown);
        if (StringUtils.notEmpty(path)) {
            getData();
        } else {
            MyToast.showToast(this, "图片地址为空！");
            finish();
        }


    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_big_img;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void getData() {
        Glide.with(this)
                .load(path)
                .fitCenter()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<String, GlideDrawable>() {
                              @Override
                              public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                  pb.setVisibility(View.GONE);
                                  showToast("加载失败");
                                  return false;
                              }

                              @Override
                              public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                  pb.setVisibility(View.GONE);
                                  boolean isStart = cxMessage.isHotChatVisible();
                                  final Message message = delHandler.obtainMessage();
                                  if (isStart) {
                                      int countTime = Integer.parseInt(getValueByKey("burnAfterReadTime", cxMessage.getImMessage().getHeader().getAttachment()));
                                      if (countTime > 0) {//如果大于0就继续倒计时
                                          initMessageTimeTask(cxMessage, countTime, position, message);
                                      } else {//等于0那么就直接删除
                                          message.obj = cxMessage;
                                          message.arg1 = position;
                                          delHandler.sendMessage(message);
                                      }
                                  }else{
                                      initMessageTimeTask(cxMessage, 10, position, message);
                                  }
                                  return false;
                              }
                          }

                )
                .
                        into(iv);
    }

    private static String getValueByKey(String key, Map<String, String> map) {
        boolean isBurnAfterRead = map.containsKey(key);
        if (!isBurnAfterRead) {
            return "0";
        } else {
            return map.get(key);
        }

    }

    /**
     * 创建文本信息倒计时
     */
    private void initMessageTimeTask(final CxMessage cxMessage, final int countTime,
                                     final int position, final Message msg) {

        timerTask = new SDTimerTask(countTime, 1, new SDTimerTask.SDTimerTasKCallback() {
            @Override
            public void startTask() {//开始倒计时

                //倒计时的红点
                tv_countdown.setText(String.valueOf(countTime));
                tv_countdown.setVisibility(View.VISIBLE);
                //删除
                msg.obj = cxMessage;
                msg.arg1 = position;

                cxMessage.setHotChatVisible(true);//开始倒计时了
            }

            @Override
            public void stopTask() {

            }

            @Override
            public void currentTime(int countdownTime) {
                //设置倒计时时间
                tv_countdown.setText(String.valueOf(countdownTime));
                cxMessage.getImMessage().getHeader().getAttachment().put("burnAfterReadTime", countdownTime + "");
                cxMessage.setHotTime(countdownTime);
                CXChatManager.updateMsg(cxMessage);

            }

            @Override
            public void finishTask() {
                BigCountDownImgActivity.this.finish();
                //delHandler.sendMessage(msg);
            }
        });

        timerTask.start();
        taskHashMap.put(position, timerTask);
    }

    //删除该条数据库
    private Handler delHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            CxMessage cxMsg = (CxMessage) msg.obj;
            CXChatManager.delteMsgByMsgIdNoSql(cxMsg);

        }
    };
}
