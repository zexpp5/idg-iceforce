package com.cxgz.activity.cx.msg.email;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cxgz.activity.db.dao.BaseUserName;
import com.injoy.idg.R;
import com.cxgz.activity.cx.base.BaseActivity;
import com.cxgz.activity.cx.bean.dao.IconString;
import com.cxgz.activity.cx.dao.MyMenuItem;
import com.cxgz.activity.cx.dao.SDMailDao;
import com.cxgz.activity.cx.utils.FileUtils;
import com.cxgz.activity.cxim.utils.DensityUtil;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;
import com.utils.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import tablayout.view.TopMenu;

/**
 * 邮箱详情
 */
public class MailDetailActivity extends BaseActivity implements TopMenu.MenuItemClickListener
{

    private TextView tv_contact;
    private TextView tv_title, tv_time, tv_cc;
    private WebView wv_content;
    private LinearLayout ll_cc, llFileList;
    private TopMenu menu;
    private static BaseUserName.MailMessageEntity mailMessageEntity;
    private SDMailDao mailDao;

    @Override
    protected void init()
    {
        mailDao = new SDMailDao(this);
        String uid = getIntent().getStringExtra("uid");
        try
        {
            mailMessageEntity = mailDao.findByUid(uid);
        } catch (DbException e)
        {
            e.printStackTrace();
        }
        tvTitle.setText(getString(R.string.email_detail));
        setLeftBack(R.drawable.folder_back);
        tv_contact = (TextView) findViewById(R.id.tv_contact);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_time = (TextView) findViewById(R.id.tv_time);
        wv_content = (WebView) findViewById(R.id.wv_content);
        ll_cc = (LinearLayout) findViewById(R.id.ll_cc);
        tv_cc = (TextView) findViewById(R.id.tv_mail_cc);
        llFileList = (LinearLayout) findViewById(R.id.ll_add_file);
        WebSettings settings = wv_content.getSettings();
        settings.setJavaScriptEnabled(true);


        if (mailMessageEntity != null)
        {
            tv_contact.setText(mailMessageEntity.getFromAddress());
            tv_title.setText(mailMessageEntity.getSubject());
            tv_time.setText(mailMessageEntity.getSendTime());
            String cc = mailMessageEntity.getcCAddress();
            if (cc.equals(""))
            {
                tv_cc.setText("无");
            } else
            {
                tv_cc.setText(cc);
            }
            wv_content.loadDataWithBaseURL(null, mailMessageEntity.getContent(), "text/html", "utf-8", null);
            if (!TextUtils.isEmpty(mailMessageEntity.getAttachId()))
            {
                String[] ids = mailMessageEntity.getAttachId().split(",");
                for (String attachId : ids)
                {
                    LogUtils.d(mailMessageEntity.getAttachPath() + attachId);
                    showAttachFile(mailMessageEntity.getAttachPath() + attachId);
                }
            }
            setRight();
        }
    }

    /**
     * 显示附件
     *
     * @param filePath
     */
    private void showAttachFile(String filePath)
    {
        final File file = new File(filePath);
        FileInputStream fis = null;
        int s = 0;
        try
        {
            fis = new FileInputStream(file);
            s = fis.available();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        final View view = LayoutInflater.from(this).inflate(
                R.layout.sd_im_newmsg_file_item, null, false);
        ((TextView) view.findViewById(R.id.filename)).setText(file.getName());
        ((TextView) view.findViewById(R.id.filesize)).setText("" + FileUtil.formetFileSize(s));
        view.findViewById(R.id.cancel).setVisibility(View.GONE);
        llFileList.setPadding(DensityUtil.dip2px(this, 12),
                DensityUtil.dip2px(this, 5), DensityUtil.dip2px(this, 12),
                DensityUtil.dip2px(this, 5));
        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FileUtils.openFile(file, MailDetailActivity.this);
            }
        });
        llFileList.addView(view);
    }

    private void setRight()
    {
        List<IconString> typelist = new ArrayList<>();
        IconString iconString = new IconString();
        iconString.setValue(getResources().getString(R.string.reply));
        IconString iconString2 = new IconString();
        iconString2.setValue(getResources().getString(R.string.forward));
        typelist.add(iconString);
        typelist.add(iconString2);
        List<MyMenuItem> menuItem = new ArrayList<>();
        menuItem.add(new MyMenuItem("回复", R.mipmap.topmenu_reply));
        menuItem.add(new MyMenuItem("转发", R.mipmap.topmenu_rew));
        menu = new TopMenu(this, menuItem);
        menu.setListener(this);

        addRightBtn(R.mipmap.more, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                menu.showCenterPopupWindow(llRight.getChildAt(0));
            }
        });
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.sd_im_mail_detail_activity;
    }


    @Override
    public void onItemClick(View view, int position)
    {
        String recp;
        if (mailMessageEntity.getFromAddress().contains("<") && mailMessageEntity.getFromAddress().contains(">"))
        {
            recp = mailMessageEntity.getFromAddress().substring(mailMessageEntity.getFromAddress().lastIndexOf("<") + 1, mailMessageEntity.getFromAddress().length() - 1);
        } else
        {
            recp = mailMessageEntity.getFromAddress();
        }
        switch (position)
        {
            case 0:
                Intent intent = new Intent(MailDetailActivity.this,
                        MailSendActivity.class);
                intent.putExtra("type", "reply");
                intent.putExtra("title", mailMessageEntity.getSubject());
                intent.putExtra("recipients", recp);
                intent.putExtra("content", mailMessageEntity.getContent());
                startActivity(intent);
                break;
            case 1:
                Intent intent2 = new Intent(MailDetailActivity.this,
                        MailSendActivity.class);
                intent2.putExtra("type", "forward");
                intent2.putExtra("title", mailMessageEntity.getSubject());
                intent2.putExtra("recipients", recp);
                intent2.putExtra("content", mailMessageEntity.getContent());
                startActivity(intent2);
                break;
        }
    }
}
