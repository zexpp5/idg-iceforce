package newProject.company.vacation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.chaoxiang.base.utils.StringUtils;
import com.injoy.idg.R;
import com.umeng.socialize.UMShareAPI;

import butterknife.Bind;
import butterknife.ButterKnife;
import yunjing.utils.ShareUMUtils;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.StatusTipsView;

import static android.os.Build.VERSION_CODES.HONEYCOMB;
import static android.os.Build.VERSION_CODES.KITKAT;
import static android.view.KeyEvent.KEYCODE_BACK;

public class WebVacationActivity extends Activity
{
    private ShareUMUtils.OnSelectShareToListener mOnSelectShareToListener;
    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    @Bind(R.id.logistic_webview)
    WebView mWebView;
    @Bind(R.id.loading_view)
    StatusTipsView loadingView;
    private String mUrl;
    private String mTitle;
    private boolean mHasShare = false;
    private String mThumb;
    private String mContent;
    private boolean isUrl = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistic_search);
        ButterKnife.bind(this);
        initView();
    }

    private void initView()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            mUrl = bundle.getString("URL");
            mTitle = bundle.getString("TITLE");
            mHasShare = bundle.getBoolean("SHARE");
            mThumb = bundle.getString("THUMB");
            mContent = bundle.getString("CONTENT");
            isUrl = bundle.getBoolean("isUrl", true);
        }
        mNavigatorBar.setLeftImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        mNavigatorBar.setMidText(mTitle);
        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(false);
        mNavigatorBar.setRightSecondImageVisible(false);
        if (mHasShare)
        {
            mNavigatorBar.setRightImageVisible(true);
            mNavigatorBar.setRightImageResouce(R.mipmap.public_share_pic);
            mNavigatorBar.setRightImageOnClickListener(mOnClickListener);
        }

        if (isUrl)
        {
            mWebView.loadUrl(mUrl);
        } else
        {
            mWebView.loadDataWithBaseURL(null, mUrl, "text/html", "UTF-8", null);

            mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
            mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            {
                mWebView.getSettings().setDisplayZoomControls(false);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            {
                mWebView.getSettings().setTextZoom(100);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            try
            {
                mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        } else
        {
            mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }

        mWebView.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        mWebView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        mWebView.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        mWebView.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAppCacheEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        mWebView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                if (url != null && mWebView != null)
                {
                    mWebView.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                super.onPageStarted(view, url, favicon);
                if (loadingView != null)
                {
                    loadingView.showLoading();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                super.onPageFinished(view, url);
                if (loadingView != null)
                {
                    loadingView.hide();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
            {
                super.onReceivedError(view, errorCode, description, failingUrl);
                if (loadingView != null)
                {
                    loadingView.showAccessFail();
                }
            }
        });
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        ButterKnife.unbind(this);
        if (mWebView != null)
        {
            mWebView.clearHistory();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
    }


    private View.OnClickListener mOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            //分享
            if (v == mNavigatorBar.getRightImage())
            {
                ShareUMUtils.showShareDialog(mUrl, "", mTitle, mContent, WebVacationActivity.this,
                        mOnSelectShareToListener, mThumb);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KEYCODE_BACK))
        {
            if (mWebView.canGoBack())
            {
                mWebView.goBack();//返回上一页面
                return true;
            } else
            {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }


}
