package com.cxgz.activity.cx.msg.email;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.chaoxiang.base.utils.AESUtils;
import com.cxgz.activity.basic.BaseXListViewActivity;
import com.cxgz.activity.cx.dao.MyMenuItem;
import com.cxgz.activity.cx.dao.SDMailDao;
import com.cxgz.activity.db.dao.BaseUserName;
import com.cxgz.activity.home.adapter.CommonAdapter;
import com.cxgz.activity.home.adapter.ViewHolder;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.pop3.POP3Folder;
import com.utils.SPUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.crypto.Cipher;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import tablayout.view.TopMenu;
import yunjing.view.CustomNavigatorBar;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


/**
 * 邮箱列表
 *
 * @author xiaoli
 */
public class MailActivity extends BaseXListViewActivity
{
    private CommonAdapter<BaseUserName.MailMessageEntity> mAdapter;
    private CustomNavigatorBar mNavigatorBar;
    private TopMenu rightMenus;
    private TextView failureHint;//加载失败提示
    private String emailName;
    private String emailPwd;
    private String emailProtocol;
    private String emailHost;
    private int emailPort;
    private List<BaseUserName.MailMessageEntity> mailMessageList = new ArrayList<>();
    private ProgressDialog pd;
    private MailConnectionUtil connectionUtil;
    private SDMailDao mailDao;
    private int count;
    private int limit = 10;//限制加载邮箱的条数
    private boolean isSuccess;

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(android.os.Message msg)
        {
            super.handleMessage(msg);
            if (pd.isShowing())
            {
                pd.dismiss();
            }
            if (isSuccess)
            {
                if (mailMessageList != null && mailMessageList.size() > 0)
                {
                    for (BaseUserName.MailMessageEntity messageEntity : mailMessageList)
                    {
                        try
                        {
                            mailDao.saveMail(messageEntity);
                        } catch (DbException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
                failureHint.setVisibility(View.GONE);
                if (mAdapter == null)
                {
                    mAdapter = new CommonAdapter<BaseUserName.MailMessageEntity>(MailActivity.this, mailMessageList, R.layout
                            .email_manager_item_layout)
                    {
                        @Override
                        public void convert(ViewHolder helper, final BaseUserName.MailMessageEntity item, int position)
                        {
                            try
                            {
                                //放发件人
                                if (item.getSubject() != null)
                                {
                                    helper.getView(R.id.email_item_name).setVisibility(VISIBLE);
                                    helper.setText(R.id.email_item_name, item.getFromAddress());
                                } else
                                {
                                    helper.getView(R.id.email_item_name).setVisibility(GONE);
                                }
                                //内容或者主题
                                if (item.getContent() != null)
                                {
                                    helper.getView(R.id.email_below_item_name).setVisibility(VISIBLE);
                                    helper.setText(R.id.email_below_item_name, item.getSubject());
                                } else
                                {
                                    helper.getView(R.id.email_below_item_name).setVisibility(GONE);
                                }

                                if (item.getSendTime() != null)
                                {
                                    helper.getView(R.id.email_item_time).setVisibility(VISIBLE);
                                    helper.setText(R.id.email_item_time, item.getSendTime());
                                } else
                                {
                                    helper.getView(R.id.email_item_time).setVisibility(GONE);
                                }

                                if (item.getSeenFlag() == 0)
                                {   //未读
                                    helper.getView(R.id.email_item_iamge).setBackgroundResource(R.mipmap
                                            .email_manager_item_close);
                                } else
                                {
                                    helper.getView(R.id.email_item_iamge).setBackgroundResource(R.mipmap.email_manager_item_open);
                                }

                               /* ChangeStatusView csv = helper.getView(R.id.apply_stauts);
                                helper.setText(R.id.time, item.getSendTime());
                                helper.setText(R.id.name, item.getSubject());
                                helper.setVisibility(R.id.tv_maohao, View.GONE);
                                helper.setText(R.id.title, item.getContent());
                                csv.setLeftName("邮箱");
                                if (item.getSeenFlag() == 0)
                                {
                                    csv.setRightName("未阅");
                                    csv.setStatus(ChangeStatusView.STATUS_RED);
                                } else
                                {
                                    csv.setRightName("已阅");
                                    csv.setStatus(ChangeStatusView.STATUS_GREEN);
                                }*/
                                helper.setOnclickListener(R.id.email_item_id, new OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        try
                                        {
                                            item.setSeenFlag(1);
                                            mailDao.saveMail(item);
                                            notifyDataSetChanged();
                                            Intent intent = new Intent(MailActivity.this, MailDetailActivity.class);
                                            intent.putExtra("uid", item.getUid());
                                            startActivity(intent);
                                        } catch (Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    };
                    mListView.setAdapter(mAdapter);
                } else
                {
                    mAdapter.notifyDataSetChanged();
                }
                if (mAdapter.getCount() > limit)
                {
                    mListView.hideFooterView();
                } else
                {
                    mListView.hideFooterView();
                }
                onLoad();
            } else
            {
                failureHint.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    protected void init()
    {
        super.init();
        mNavigatorBar = (CustomNavigatorBar) findViewById(R.id.title_bar);
        mNavigatorBar.setLeftImageOnClickListener(mOnClickListener);
        mNavigatorBar.setLeftTextOnClickListener(mOnClickListener);
        mNavigatorBar.setMidText("邮件管理");
        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(true);
        mNavigatorBar.setRightImageResouce(R.mipmap.icon_public_add);
        mNavigatorBar.setRightImageOnClickListener(mOnClickListener);
        SPUtils.put(MailActivity.this, "mail_visit_count", 0);
        mailDao = new SDMailDao(this);
        initDialog();
        failureHint = (TextView) findViewById(R.id.tv_load_failure);

        List<MyMenuItem> items = new ArrayList<>();
        items.add(new MyMenuItem("添加", R.mipmap.topmenu_add));
        items.add(new MyMenuItem("设置", R.mipmap.gray_settings));
        rightMenus = new TopMenu(this, items);
        rightMenus.setListener(new TopMenu.MenuItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                switch (position)
                {
                    case 0:
                        if (!SPUtils.get(MailActivity.this, SPUtils.EMAIL_NAME, "").equals(""))
                        {
                            Intent intent = new Intent(MailActivity.this, MailSendActivity.class);
                            startActivity(intent);
                        } else
                        {
//                            SDToast.showShort("");
                        }
                        break;
                    case 1:
                        Intent intent = new Intent(MailActivity.this, MailSettingActivity.class);
                        startActivityForResult(intent, 1);
                        break;
                }
            }
        });
        checkMailConfig();
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
            if (v == mNavigatorBar.getRightImage())
            {
                if (rightMenus != null)
                {
                    rightMenus.showAsDropDown(mNavigatorBar.getLl_right());
                }
            }

        }
    };

    /**
     * 检查邮箱配置信息
     */
    private void checkMailConfig()
    {
        //获取本地邮箱配置信息
        getEmailConfig();
        if (TextUtils.isEmpty(emailName) || TextUtils.isEmpty(emailPwd) ||
                TextUtils.isEmpty(emailProtocol) || TextUtils.isEmpty(emailHost) || emailPort <= 0)
        {
            Intent intent = new Intent(MailActivity.this, MailSettingActivity.class);
            startActivityForResult(intent, 1);
        } else
        {
            connectionUtil = new MailConnectionUtil(emailHost, emailPort, emailProtocol, emailName, emailPwd);
            pd.show();
            loadData();
        }
    }

    /**
     * 加载数据
     */
    private void loadData()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    String path = Environment.getExternalStorageDirectory().toString() + "/chaoxiang/email/" + emailName + "/";
                    Folder folder = connectionUtil.connect();
                    if (!folder.isOpen())
                    {
                        folder.open(Folder.READ_ONLY);
                    }
                    count = folder.getMessageCount();
                    List<Message> messageList = new ArrayList<>();
                    for (int i = count; i > 0; i--)
                    {
                        Message msg = folder.getMessage(i);
                        if (msg == null)
                        {
                            continue;
                        }
                        messageList.add(msg);
                        //设置最多只拿40条数据
                        if (count > 40)
                        {
                            if (i < count - 40)
                            {
                                break;
                            }
                        }
                    }
                    Log.e("ERROR", "pop3Folder-count====" + count);
                    if (emailProtocol.equals("pop3"))
                    {
                        POP3Folder pop3Folder = (POP3Folder) folder;
                        //遍历所有邮件
                        for (Message msg : messageList)
                        {
                            String uid = pop3Folder.getUID(msg);
                            Log.e("ERROR", "pop3Folder====" + uid);
                            BaseUserName.MailMessageEntity mailMsg = getMailMessageEntity(path, uid, msg);
                            mailMessageList.add(mailMsg);
                        }

                    } else
                    {
                        IMAPFolder imapFolder = (IMAPFolder) folder;
                        //遍历所有邮件
                        for (Message msg : messageList)
                        {
                            String uid = String.valueOf(imapFolder.getUID(msg));
                            LogUtils.d("imapFolder====" + uid);
                            BaseUserName.MailMessageEntity mailMsg = getMailMessageEntity(path, uid, msg);
                            mailMessageList.add(mailMsg);

                        }
                    }
                    isSuccess = true;
                } catch (Exception e)
                {
                    isSuccess = false;
                    e.printStackTrace();
                } finally
                {
                    try
                    {
                        connectionUtil.close();
                    } catch (MessagingException e)
                    {
                        e.printStackTrace();
                    }
                }
                handler.sendEmptyMessage(0);
            }
        }).start();
    }


    private BaseUserName.MailMessageEntity getMailMessageEntity(String path, String uid, Message msg) throws Exception
    {
        BaseUserName.MailMessageEntity messageEntity = mailDao.findByUid(uid);
        if (messageEntity != null)
        {
            return messageEntity;
        } else
        {
            //进行序列化
            messageEntity = parsertMailMessage(msg, path, uid);
        }
        return messageEntity;
    }

    private BaseUserName.MailMessageEntity parsertMailMessage(Message msg, String path, String uid) throws Exception
    {
        String attachPath = path + emailProtocol + "/" + uid + "/";
        MailReceiver receiverMail = new MailReceiver();
        BaseUserName.MailMessageEntity entity = new BaseUserName.MailMessageEntity();
        entity.setUid(uid);
        entity.setSubject(receiverMail.getSubject((MimeMessage) msg));
        entity.setSeenFlag(0);
        entity.setFromAddress(receiverMail.getFrom((MimeMessage) msg));
        entity.setSendTime(receiverMail.getSendDate((MimeMessage) msg, "yyyy-MM-dd"));
        entity.setcCAddress(receiverMail.getMailAddress((MimeMessage) msg, "cc"));
        //获取邮件正文
        StringBuffer sb = new StringBuffer();
        receiverMail.getMailContent(msg, sb);
        String htmlContent = sb.toString();
//        String ids = null;
//        Map<String, String> imageMap = new HashMap<>();
//
//        if (receiverMail.isContainAttch(msg))
//        {
//            ids = receiverMail.saveAttchMent(msg, attachPath, imageMap);
//            ids = ids.substring(1, ids.length());
//        }
//        Log.e("ERROR","333");
//        if (ids != null && !ids.equals(""))
//        {
//            entity.setAttachFlag(1);
//            entity.setAttachId(ids);
//            entity.setAttachPath(attachPath);
//            //替换cid
//            Set<Map.Entry<String, String>> set = imageMap.entrySet();
//            Iterator<Map.Entry<String, String>> it = set.iterator();
//            while (it.hasNext())
//            {
//                Map.Entry<String, String> entry = it.next();
//                String cid = "cid:" + entry.getKey();
//                String src = "file://" + attachPath + entry.getValue();
//                htmlContent = htmlContent.replace(cid, src);
//            }
//        }
        entity.setContent(htmlContent);
        return entity;
    }


    /**
     * 获取本地邮箱配置信息
     */
    private void getEmailConfig()
    {
        emailName = (String) SPUtils.get(this, SPUtils.EMAIL_NAME, "");
        String pwd = (String) SPUtils.get(this, SPUtils.EMAIL_PWD, "");
        if (pwd != null && !pwd.equals(""))
        {
            emailPwd = AESUtils.des(userId + "!@#", pwd, Cipher.DECRYPT_MODE);
        }
        emailProtocol = (String) SPUtils.get(this, SPUtils.EMAIL_PROTOCOL, "");
        emailHost = (String) SPUtils.get(this, SPUtils.EMAIL_HOST, "");
        emailPort = (int) SPUtils.get(this, SPUtils.EMAIL_PORT, 0);
    }

    private void initDialog()
    {
        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("正在加载...");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        //获取本地邮箱配置信息
        getEmailConfig();
        if (resultCode == RESULT_OK)
        {
            switch (requestCode)
            {
                case 1:
                    if (data != null)
                    {
                        boolean config_success = data.getBooleanExtra("config_success", false);
                        if (config_success)
                        { //配置成功
                            if (mAdapter != null)
                            {
                                mListView.hideFooterView();
                                mAdapter.clear();
                                mAdapter.notifyDataSetChanged();
                            }
                            connectionUtil = new MailConnectionUtil(emailHost, emailPort, emailProtocol, emailName, emailPwd);
                            pd.show();
                            loadData();
                        } else
                        {//没有配置
                            if (mAdapter == null)
                            {
                                finish();
                            }
                        }
                    }
                    break;
            }
        }

    }

    @Override
    public int getXListViewId()
    {
        return R.id.content;
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.sd_im_mail_activity;
    }

    @Override
    public void onRefresh()
    {
        mailMessageList.clear();
        pd.show();
        loadData();
        onLoad();
    }

    @Override
    public void onLoadMore()
    {
        onLoad();
    }
}
