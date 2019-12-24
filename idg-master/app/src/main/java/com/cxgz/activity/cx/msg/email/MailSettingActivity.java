package com.cxgz.activity.cx.msg.email;

import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;

import com.chaoxiang.base.utils.StringUtils;
import com.injoy.idg.R;
import com.chaoxiang.base.utils.AESUtils;
import com.cxgz.activity.cx.base.BaseActivity;
import com.cxgz.activity.cx.bean.dao.MailConfig;
import com.cxgz.activity.cx.dao.SDMailDao;
import com.cxgz.activity.cx.view.BottomMenuView;
import com.cxgz.activity.cx.view.TextAndEditView;
import com.lidroid.xutils.exception.DbException;
import com.utils.SDToast;
import com.utils.SPUtils;

import javax.crypto.Cipher;
import javax.mail.MessagingException;

import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;

/**
 * @author 小黎
 * @date 2015/12/22 19:37
 * @des 邮箱设置
 */
public class MailSettingActivity extends BaseActivity
{
    private TextAndEditView account;
    private TextAndEditView password;
    private CheckBox pop3;
    private CheckBox imap;
    private String protocol;//协议
    private int port;//端口
    private String emailUser;//邮箱用户
    private String emailPwd;//邮箱密码
    private String host;
    private BottomMenuView bottomMenu;
    private MailConnectionUtil connectionUtil;
    private ProgressDialog pd;
    private CustomNavigatorBar mNavigatorBar;

    @Override
    protected void init()
    {
        initDialog();

        mNavigatorBar = (CustomNavigatorBar) findViewById(R.id.title_bar);
        mNavigatorBar.setLeftImageOnClickListener(mOnClickListener);
        mNavigatorBar.setLeftTextOnClickListener(mOnClickListener);
        mNavigatorBar.setMidText("设置邮箱账号");
        mNavigatorBar.setRightTextVisible(false);

        account = (TextAndEditView) findViewById(R.id.tad_mail_account);
        password = (TextAndEditView) findViewById(R.id.tad_mail_password);

        pop3 = (CheckBox) findViewById(R.id.cb_pop3);
        imap = (CheckBox) findViewById(R.id.cb_imap);

        bottomMenu = (BottomMenuView) findViewById(R.id.bottom_menu);

        handleCheckBox();
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (v == mNavigatorBar.getLeftImageView() || v == mNavigatorBar.getLeftText())
            {
                configBack(false);
            }
        }
    };

    /**
     * 初始化dialog
     */
    private void initDialog()
    {
        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage(getString(R.string.running_config));
    }

    /**
     * 处理POP3、IMAP选择框
     */
    private void handleCheckBox()
    {

        if (pop3.isChecked())
        {
            protocol = "pop3";
        } else if (imap.isChecked())
        {
            protocol = "imap";
        }

        pop3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                protocol = "pop3";
                pop3.setChecked(true);
                imap.setChecked(false);
            }
        });

        imap.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                protocol = "imap";
                imap.setChecked(true);
                pop3.setChecked(false);
            }
        });

        //完成按钮
        bottomMenu.setListener(new BottomMenuView.BottomMenuListener()
        {
            @Override
            public void leftBtnClick(View view)
            {
                emailUser = account.getContent();//获取输入的邮箱账号
                emailPwd = password.getContent();//获取输入的邮箱密码

//                emailUser = "512160530@qq.com";
//                emailPwd = "zictdbbqmjcibjch";
//                emailUser = "fundadmin@idgcapital.com";
//                emailPwd = "Kocu4009";

                //非空判断
                if (!TextUtils.isEmpty(emailUser) && !TextUtils.isEmpty(emailPwd))
                {
                    //验证邮箱格式正不正确
                    boolean emailIsOK = StringUtils.isEmail(emailUser);
                    if (emailIsOK)
                    {
                        //设置端口号
                        port = protocol.equals("pop3") ? 995 : 993;
                        //拼接主机地址
                        String temp = protocol.equals("pop3") ? "pop" : "imap";
                        host = temp + "." + emailUser.substring(emailUser.indexOf("@") + 1, emailUser.length());
                        pd.show();
                        new Thread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                try
                                {
                                    if (userId == null)
                                    {
                                        userId = DisplayUtil.getUserInfo(MailSettingActivity.this, 3);
                                    }

                                    host = "imap-mail.outlook.com";
                                    protocol = "imap";
                                    connectionUtil = new MailConnectionUtil(host, port, protocol, emailUser, emailPwd);
                                    connectionUtil.connect();
                                    //连接成功后，保存到本地
                                    SPUtils.put(MailSettingActivity.this, SPUtils.EMAIL_NAME, emailUser);
                                    SPUtils.put(MailSettingActivity.this, SPUtils.EMAIL_PWD, AESUtils.des(userId + "!@#",
                                            emailPwd, Cipher.ENCRYPT_MODE));
                                    SPUtils.put(MailSettingActivity.this, SPUtils.EMAIL_PROTOCOL, protocol);
                                    SPUtils.put(MailSettingActivity.this, SPUtils.EMAIL_HOST, host);
                                    SPUtils.put(MailSettingActivity.this, SPUtils.EMAIL_PORT, port);
                                    SDMailDao dao = new SDMailDao(MailSettingActivity.this);
                                    MailConfig config;
                                    try
                                    {
                                        config = dao.findCurrMailConfig();
                                        if (config != null)
                                        {
                                            config.setAccount(emailUser);
                                            config.setHost(host);
                                            config.setPassword(AESUtils.des(userId + "!@#", emailPwd,Cipher.ENCRYPT_MODE));
                                            config.setPort(port);
                                            config.setProtocal(protocol);
                                        } else
                                        {
                                            config = new MailConfig();
                                            config.setSelected(true);
                                            config.setUserId(Integer.parseInt(userId));
                                            config.setAccount(emailUser);
                                            config.setHost(host);
                                            config.setPassword(AESUtils.des(userId + "!@#", emailPwd,Cipher.ENCRYPT_MODE));
                                            config.setPort(port);
                                            config.setProtocal(protocol);
                                        }
                                        dao.saveConfig(config);
                                    } catch (DbException e)
                                    {
                                        e.printStackTrace();
                                    }
                                    configBack(true);
                                } catch (MessagingException e)
                                {
                                    e.printStackTrace();
                                    runOnUiThread(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            SDToast.showShort("配置邮箱失败!");
                                        }
                                    });
                                } finally
                                {
                                    runOnUiThread(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            pd.dismiss();
                                        }
                                    });
                                    try
                                    {
                                        connectionUtil.close();
                                    } catch (MessagingException e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }).start();
                    } else
                    {
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                SDToast.showShort("请输入正确的邮箱账号！");
                            }
                        });
                    }
                } else
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            SDToast.showShort("邮箱账号或密码不能为空！");
                        }
                    });
                }
            }

            @Override
            public void rightBtnClick(View view)
            {

            }
        });
    }

    /**
     * 对直接按返回键和返回按钮进行处理
     */
    private void configBack(boolean configSuccess)
    {
        Intent intent = new Intent();
        intent.putExtra("config_success", configSuccess);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        configBack(false);
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.mail_setting_layout;
    }
}
