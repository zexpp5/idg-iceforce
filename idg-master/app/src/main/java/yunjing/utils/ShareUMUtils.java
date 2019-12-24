package yunjing.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.StringUtils;
import com.injoy.idg.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import tablayout.view.textview.FontTextView;


/**
 * Created by selson on 2017/9/16.
 */

public class ShareUMUtils
{
    public static void showShareDialog(final String url, final String search, String title, String text, final Activity
            context, final OnSelectShareToListener mOnSelectShareToListener)
    {
        if (StringUtils.empty(title))
            title = "   ";
        if (StringUtils.empty(text))
            text = "   ";
        String shareUrl = null;
        if (null == url)
        {
            MyToast.showToast(context, "分享链接为空");
        } else
        {
            if (StringUtils.notEmpty(search))
            {
                shareUrl =   url + ".htm" + "?" + search;
            } else
            {
                shareUrl =  url + ".htm";
            }
            ShareDialog(shareUrl, title, text, context, mOnSelectShareToListener);
        }
    }

    private static void ShareDialog(final String url, String title, String text, final Activity context, final
    OnSelectShareToListener mOnSelectShareToListener)
    {
        if (TextUtils.isEmpty(title))
            title = "   ";
        if (TextUtils.isEmpty(text))
            text = "   ";
        final Dialog dialog = new Dialog(context);

        UMWeb web = new UMWeb(url);
        web.setTitle(title);
        web.setDescription(text);
        web.setThumb(new UMImage(context, R.mipmap.ic_launcher));

        final ShareAction shareAction = new ShareAction(context)
                .withMedia(web)
                .setCallback(new UMShareListener()
                {
                    @Override
                    public void onStart(SHARE_MEDIA share_media)
                    {

                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media)
                    {
                        Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
                        if (null != mOnSelectShareToListener)
                            mOnSelectShareToListener.onSelectShareToOnResult(share_media);
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable)
                    {
                        Toast.makeText(context, "分享失败", Toast.LENGTH_SHORT).show();
                        if (null != mOnSelectShareToListener)
                            mOnSelectShareToListener.onSelectShareToOnError(share_media, throwable);
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media)
                    {
//                        Toast.makeText(context, "取消分享", Toast.LENGTH_SHORT).show();
                        if (null != mOnSelectShareToListener)
                            mOnSelectShareToListener.onSelectShareToOnCancel(share_media);
                        dialog.dismiss();
                    }
                });

        View contentView = LayoutInflater.from(context).inflate(R.layout.customer_notification_share_dialog, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(contentView);
        dialog.setCanceledOnTouchOutside(true);
        LinearLayout weixin = (LinearLayout) contentView.findViewById(R.id.weixin);
        LinearLayout wxcircle = (LinearLayout) contentView.findViewById(R.id.wxcircle);
       // LinearLayout qq = (LinearLayout) contentView.findViewById(R.id.qq);
        LinearLayout qzone = (LinearLayout) contentView.findViewById(R.id.qzone);

        RelativeLayout share_cancel = (RelativeLayout) contentView.findViewById(R.id.share_cancel);

        weixin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                shareAction.setPlatform(SHARE_MEDIA.WEIXIN).share();
            }
        });

        wxcircle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                shareAction.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).share();
            }
        });


       /*qq.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                shareAction.setPlatform(SHARE_MEDIA.QQ).share();
            }
        });*/

        qzone.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                shareAction.setPlatform(SHARE_MEDIA.SINA).share();
            }
        });

        share_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                if (null != mOnSelectShareToListener)
                    mOnSelectShareToListener.onSelectShareToOnShareDialogCancel();
            }
        });
        dialog.show();

    }

    public static void showShareDialog(final String url, final String search, String title, String text, final Activity
            context, final OnSelectShareToListener mOnSelectShareToListener,String thumb)
    {
        if (StringUtils.empty(title))
            title = "   ";
        if (StringUtils.empty(text))
            text = "   ";
        String shareUrl = null;
        if (null == url)
        {
            MyToast.showToast(context, "分享链接为空");
        } else
        {
            if (StringUtils.notEmpty(search))
            {
                shareUrl =   url + ".htm" + "?" + search;
            } else
            {
                shareUrl =  url + ".htm";
            }
            ShareDialog(shareUrl, title, text, context, mOnSelectShareToListener,thumb);
        }
    }

    private static void ShareDialog(final String url, String title, String text, final Activity context, final
    OnSelectShareToListener mOnSelectShareToListener,String thumb)
    {
        if (TextUtils.isEmpty(title))
            title = "   ";
        if (TextUtils.isEmpty(text))
            text = "   ";
        final MsgDialog dialog = new MsgDialog(context);

        UMWeb web = new UMWeb(url);
        web.setTitle(title);
        web.setDescription(text);
        if (thumb!=null) {
            web.setThumb(new UMImage(context,thumb));

        }else{
            web.setThumb(new UMImage(context, R.mipmap.ic_launcher));
        }

        final ShareAction shareAction = new ShareAction(context)
                .withMedia(web)
                .setCallback(new UMShareListener()
                {
                    @Override
                    public void onStart(SHARE_MEDIA share_media)
                    {

                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media)
                    {
                        Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
                        if (null != mOnSelectShareToListener)
                            mOnSelectShareToListener.onSelectShareToOnResult(share_media);
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable)
                    {
                        Toast.makeText(context, "分享失败", Toast.LENGTH_SHORT).show();
                        if (null != mOnSelectShareToListener)
                            mOnSelectShareToListener.onSelectShareToOnError(share_media, throwable);
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media)
                    {
//                        Toast.makeText(context, "取消分享", Toast.LENGTH_SHORT).show();
                        if (null != mOnSelectShareToListener)
                            mOnSelectShareToListener.onSelectShareToOnCancel(share_media);
                        dialog.dismiss();
                    }
                });

        View contentView = LayoutInflater.from(context).inflate(R.layout.sd_share_picker, null);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(contentView);
        dialog.setCanceledOnTouchOutside(true);
        LinearLayout weixin = (LinearLayout) contentView.findViewById(R.id.weixin);
        LinearLayout wxcircle = (LinearLayout) contentView.findViewById(R.id.wxcircle);
        // LinearLayout qq = (LinearLayout) contentView.findViewById(R.id.qq);
        LinearLayout qzone = (LinearLayout) contentView.findViewById(R.id.qzone);

        FontTextView share_cancel = (FontTextView) contentView.findViewById(R.id.cancel_text);

        weixin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                shareAction.setPlatform(SHARE_MEDIA.WEIXIN).share();
            }
        });

        wxcircle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                shareAction.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).share();
            }
        });


       /*qq.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                shareAction.setPlatform(SHARE_MEDIA.QQ).share();
            }
        });*/

        qzone.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                shareAction.setPlatform(SHARE_MEDIA.SINA).share();
            }
        });

        share_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                if (null != mOnSelectShareToListener)
                    mOnSelectShareToListener.onSelectShareToOnShareDialogCancel();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        if (dialog != null && window != null) {
            window.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams attr = window.getAttributes();
            if (attr != null) {
                attr.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                attr.width = ViewGroup.LayoutParams.MATCH_PARENT;
                attr.gravity = Gravity.BOTTOM;//设置dialog 在布局中的位置

                window.setAttributes(attr);
            }
        }

    }

    public static class MsgDialog extends Dialog {

        public MsgDialog(Context context){
            super(context,R.style.MsgDialog);
        }

    }

    public interface OnSelectShareToListener
    {
        void onSelectShareToOnResult(SHARE_MEDIA share_media);

        void onSelectShareToOnError(SHARE_MEDIA share_media, Throwable throwable);

        void onSelectShareToOnCancel(SHARE_MEDIA share_media);

        void onSelectShareToOnCopay();

        void onSelectShareToOnShareDialogCancel();
    }

}
