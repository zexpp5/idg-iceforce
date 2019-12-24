package com.ui.activity.guide;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.EdgeEffectCompat;
import android.util.Log;
import android.view.ViewGroup;


import com.chaoxiang.base.utils.AESUtils;
import com.chaoxiang.base.utils.BackUtils;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cx.base.BaseActivity;
import com.entity.update.UpdateEntity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.superdata.im.utils.SPUtilsTool;
import com.ui.SDLoginActivity;
import com.ui.utils.LoginUtils;
import com.utils.AppUtils;
import com.utils.DialogUtilsIm;
import com.utils.FileUtil;
import com.utils.SPUtils;

import java.io.File;
import java.lang.reflect.Field;

import javax.crypto.Cipher;

import yunjing.http.BaseHttpHelper;

import static com.umeng.socialize.utils.DeviceConfig.context;


public class GuideActivity extends BaseActivity
{
    private ViewPager mViewPager;
    private EdgeEffectCompat mLeftEdge, mRightEdge;
    private int mCurrentVersion;//当前版本
    private String mCurrentVersionName;//当前版本
    private int mCurrentVersionCode;//当前版本

    private String target;
    private File file;

    private boolean autoStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initBeforeCreateView()
    {
        super.initBeforeCreateView();
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0)
        {
            finish();
            return;
        }
    }

    @Override
    protected void init()
    {
        /****************************************
         * 配置viewpager
         */
        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(mainPagerAdapter.getCount());
        mCurrentVersionName = AppUtils.getVersionName(this);
        mCurrentVersionCode = AppUtils.getVersionCode(this);
        try
        {
            Field leftEdgeField = mViewPager.getClass().getDeclaredField("mLeftEdge");
            Field rightEdgeField = mViewPager.getClass().getDeclaredField("mRightEdge");
            if (leftEdgeField != null && rightEdgeField != null)
            {
                leftEdgeField.setAccessible(true);
                rightEdgeField.setAccessible(true);
                mLeftEdge = (EdgeEffectCompat) leftEdgeField.get(mViewPager);
                mRightEdge = (EdgeEffectCompat) rightEdgeField.get(mViewPager);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                Log.e("test", "position: " + position + " positionOffset: " + positionOffset + " positionOffsetPixels: " +
                        positionOffsetPixels);

            }

            @Override
            public void onPageSelected(int position)
            {
                if (position == 0)
                {
                    //getUpdate();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {
                Log.e("test", "state: " + state);
                if (mRightEdge != null && !mRightEdge.isFinished())
                {
                    //到了最后一张并且还继续拖动，出现蓝色限制边条了
                    if (!autoStart)
                    {
                        autoStart = true;
                        autoLogin();
                    }
                }
            }
        });

        mViewPager.setAdapter(mainPagerAdapter);
        getUpdate();
//        toLogin();
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_guide;
    }

    /**
     * **************FragmentPagerAdapter*****************
     */
    public class MainPagerAdapter extends FragmentPagerAdapter
    {
        public int TAB_COUNT = 1;

        public MainPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            return super.instantiateItem(container, position);
        }

        @Override
        public int getCount()
        {
            return TAB_COUNT;
        }

        @Override
        public Fragment getItem(int position)
        {
            switch (position)
            {
                case 0:
                    return GuideFragment.newInstance(R.layout.fragment_guide_0214);
//                case 1:
//                    return GuideFragment.newInstance(R.layout_city.fragment_guide_2);
//                case 2:
//                    return GuideFragment.newInstance(R.layout_city.fragment_guide_3);
//                case 3:
//                    return GuideFragment.newInstance(R.layout_city.fragment_guide_4);
                default:
                    return null;
            }
        }

        @Override
        public int getItemPosition(Object object)
        {
            return PagerAdapter.POSITION_NONE;
        }
    }

    private void getUpdate()
    {
        BaseHttpHelper.updateInfo(mHttpHelper, new SDRequestCallBack(UpdateEntity.class)
        {
            @Override
            public void onCancelled()
            {
                super.onCancelled();
                toLogin();
            }

            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                toLogin();
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                UpdateEntity entity = (UpdateEntity) responseInfo.result;
                if (entity != null)
                {
                    if (StringUtils.empty(entity.getData()))
                    {
                        MyToast.showToast(GuideActivity.this, "版本检查异常");
                        toLogin();
                    }
                    if (StringUtils.notEmpty(entity.getData().getIsOpen()))
                    {
                        if (entity.getData().getIsOpen().equals("true"))
                        {
                            if (StringUtils.empty(entity.getData().getVersionCode()))
                            {
                                toLogin();
                            } else
                            {
                                if (mCurrentVersionCode < (Integer.parseInt(entity.getData().getVersionCode())))
                                {
                                    if (!SPUtilsTool.getVersionUpdate(GuideActivity.this, Integer.parseInt(entity.getData()
                                            .getVersionCode())))
                                    {
                                        showDialog(entity);
                                    } else
                                    {
                                        toLogin();
                                    }
                                } else
                                {
                                    toLogin();
                                }
                            }
                        } else
                        {
                            toLogin();
                        }
                    } else
                    {
                        toLogin();
                    }
                } else
                {
                    toLogin();
                }
            }
        });
    }

    private void showDialog(final UpdateEntity entity)
    {
        DialogUtilsIm.showUpdataVersion(GuideActivity.this, entity.getData().getDescription(), new DialogUtilsIm
                .OnYesOrNoAndCKListener()
        {
            @Override
            public void onYes()
            {
                if (StringUtils.notEmpty(entity.getData().getUrlLink()))
                {
                    download(entity);
                } else
                {
                    MyToast.showToast(GuideActivity.this, "打开出错，请稍候再试!");
                }
            }

            @Override
            public void onNo()
            {
                //强制更新
                if (StringUtils.notEmpty(entity.getData().getStatus()))
                {
                    if (entity.getData().getStatus().equals("1"))
                    {
                        SDLogUtil.debug("版本-强制更新");
                        System.exit(0);
                    }
                    //普通更新
                    else if (entity.getData().getStatus().equals("2"))
                    {
                        SDLogUtil.debug("版本- 普通更新");
                        toLogin();
                    } else
                    {
                        toLogin();
                    }
                } else
                {
                    toLogin();
                }
            }

            @Override
            public void onCheck(boolean isCheck)
            {
                if (isCheck)
                {
                    SPUtils.put(GuideActivity.this, com.chaoxiang.base.utils.SPUtils.VERSION_UPDATE, true);
                }
            }
        });
    }

    private void autoLogin()
    {
        boolean isAutoLogin = (boolean) SPUtils.get(getApplication(), SPUtils.IS_AUTO_LOGIN, false);
        if (isAutoLogin == true)
        {
            String seed = (String) SPUtils.get(GuideActivity.this, SPUtils.AES_SEED, "");
            String userAccount = SPUtils.get(GuideActivity.this, SPUtils.USER_ACCOUNT, "").toString();
            String aesPwd = SPUtils.get(GuideActivity.this, SPUtils.AES_PWD, "").toString();
            String password = AESUtils.des(seed, aesPwd, Cipher.DECRYPT_MODE);
            //权限密码
            LoginUtils.login(GuideActivity.this, mHttpHelper, userDao, userAccount, password, "", isAutoLogin);
        } else
        {
            startActivity(new Intent(GuideActivity.this, SDLoginActivity.class));
            finish();
        }
    }


    private void toLogin()
    {
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (!autoStart)
                {
                    autoStart = true;
                    autoLogin();
                }
            }
        }, 2 * 1000);
    }

    @Override
    public void onBackPressed()
    {
        BackUtils.isBack(GuideActivity.this);
    }


    public void download(UpdateEntity entity)
    {
        if (StringUtils.notEmpty(entity.getData().getUrlLink()))
        {
            if (entity.getData().getUrlLink().indexOf("http") != -1)
            {
                SDLogUtil.debug("版本-下载链接:" + entity.getData().getUrlLink());
                Uri uri = Uri.parse(entity.getData().getUrlLink().toString().trim());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                GuideActivity.this.startActivity(intent);
            } else
            {
                MyToast.showToast(GuideActivity.this, "下载链接为空，请移步应用宝搜索下载");
            }
        }
    }

    public String getTarget(int newVersion)
    {
        target = Environment.getDataDirectory().getAbsolutePath() + "/chaoxiang/versionupdate/";
        if (FileUtil.isSdcardExist())
        {
            target = FileUtil.getSDFolder() + "/chaoxiang/versionupdate/";
        }
        FileUtil.createDirFile(target);
        target += newVersion + ".apk";
        file = new File(target);
        if (file.exists())
        {
            file.delete();
        }
        return target;
    }


    public void openFile(File file)
    {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }
}
