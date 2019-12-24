package newProject.finger;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaoxiang.base.utils.AESUtils;
import com.cxgz.activity.db.dao.SDUserDao;
import com.injoy.idg.R;
import com.ui.SDLoginActivity;
import com.ui.utils.LoginUtils;
import com.utils.SPUtils;

import javax.crypto.Cipher;

import yunjing.view.CustomNavigatorBar;

public class FingerActivity extends FragmentActivity
{
    private CustomNavigatorBar mNavigatorBar;
    private Button mCancelBtn;
    private ImageView mFingerShake;

    private TextView mTryText;
    private int mCount = 5;
    private FingerPrintUtils mFingerUtils;
    private TranslateAnimation mAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger);
        mNavigatorBar = (CustomNavigatorBar) findViewById(R.id.title_bar);
        mNavigatorBar.setLeftImageOnClickListener(mOnClickListener);
        mNavigatorBar.setLeftImageVisible(false);
        mNavigatorBar.setLeftTextOnClickListener(mOnClickListener);
        mNavigatorBar.setLeftTextVisible(false);
        mNavigatorBar.setMidText("指纹验证");
        mNavigatorBar.setRightTextVisible(false);

        mCancelBtn = (Button) findViewById(R.id.finger_cancel);
        mCancelBtn.setOnClickListener(mOnClickListener);
        mTryText = (TextView) findViewById(R.id.try_text);
        mFingerShake = (ImageView) findViewById(R.id.image_finger);
        mAnimation = new TranslateAnimation(0, 5, 0, 0);
        mAnimation.setDuration(800);
        mAnimation.setInterpolator(new CycleInterpolator(10));
        mFingerUtils = new FingerPrintUtils(this);
        mFingerUtils.setFingerPrintListener(new FingerCallBack());
    }


    private View.OnClickListener mOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (v == mNavigatorBar.getLeftImageView() || v == mNavigatorBar.getLeftText())
            {
                finish();
            }
            if (v == mCancelBtn)
            {
                startActivity(new Intent(FingerActivity.this, SDLoginActivity.class));
                finish();
            }

        }
    };

    private class FingerCallBack extends FingerprintManagerCompat.AuthenticationCallback
    {
        //多次识别失败,并且，不能短时间内调用指纹验证
        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString)
        {
            super.onAuthenticationError(errMsgId, errString);
            if (mCount > 1)
            {
                mCount--;
                mTryText.setText("指纹不匹配，还可以尝试" + mCount + "次");
            } else
            {
                mTryText.setText("1分钟后可重试!");
            }
            //实际上1分5秒
            mHandler.sendMessageDelayed(new Message(), 1000 * 65);
        }

        //出错可恢复
        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString)
        {
            super.onAuthenticationHelp(helpMsgId, helpString);
        }

        //识别成功
        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result)
        {
            super.onAuthenticationSucceeded(result);
            mFingerUtils.stopsFingerPrintListener();
            //这里爱咋滴咋滴
            String seed = (String) SPUtils.get(FingerActivity.this, SPUtils.AES_SEED, "");
            String userAccount = SPUtils.get(FingerActivity.this, SPUtils.USER_ACCOUNT, "").toString();
            String password = AESUtils.des(seed, (String) SPUtils.get(FingerActivity.this, SPUtils.AES_PWD, ""), Cipher.DECRYPT_MODE);
            SDUserDao userDao = new SDUserDao(FingerActivity.this);

            LoginUtils.login(FingerActivity.this, null, userDao, userAccount, password, "", false);
        }

        //识别失败
        @Override
        public void onAuthenticationFailed()
        {
            super.onAuthenticationFailed();
            if (mCount > 1)
            {
                mCount--;
                mTryText.setText("指纹不匹配，还可以尝试" + mCount + "次");
            }
            mFingerShake.startAnimation(mAnimation);
        }
    }

    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            if (mFingerUtils != null)
            {
                mFingerUtils.reSetFingerPrintListener(new FingerCallBack());
            }
            mCount = 5;
            if (mTryText != null)
            {
                mTryText.setText("请轻触感应器验证指纹");
            }

        }
    };

}
