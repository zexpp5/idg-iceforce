package com.cxgz.activity.cx.msg.email;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.chaoxiang.base.utils.SDLogUtil;
import com.entity.SDFileListEntity;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.List;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;


public class MailSender
{
    private class MailAuthenticator extends javax.mail.Authenticator
    {

        private String usermail;
        private String userpass;

        public MailAuthenticator(String usermail, String userpass)
        {
            this.usermail = usermail;
            this.userpass = userpass;
        }

        @Override
        public PasswordAuthentication getPasswordAuthentication()
        {
            return new PasswordAuthentication(usermail, userpass);
        }
    }

    private Context mContext;

    public MailSender(Context mContext)
    {
        this.mContext = mContext;
    }


    public interface Callback
    {
        void onSuccess();

        void onFailed(Exception e);
    }

    private class MailAsycnTask extends AsyncTask<Void, Void, Void>
    {

        private String host;
        private String port;
        private String password;
        private String from;
        private String content;
        private String title;
        // private String addresses;
        private List<String> addresses;
        private List<String> cc;
        private List<SDFileListEntity> files;
        private Callback callback;

        private Exception error;
        private ProgressDialog pd;

        MailAsycnTask(String host, String port, String password, String from, String content, String title,/*String addresses,
        */ List<String> addresses, List<String> cc, List<SDFileListEntity> files, Callback callback)
        {
            this.host = host;
            this.password = password;
            this.port = port;
            this.from = from;
            this.content = content;
            this.title = title;
            this.addresses = addresses;
            this.cc = cc;
            this.files = files;
            this.callback = callback;
            if (pd == null)
            {
                pd = new ProgressDialog(mContext);
                pd.setMessage("正在发送中...");
                pd.setCanceledOnTouchOutside(false);
            }
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
//            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            try
            {
                send(host, port, password, from, content, title, /*addresses*/ addresses, cc, files);
            } catch (Exception e)
            {
                e.printStackTrace();
                error = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            if (pd != null)
                pd.dismiss();
            if (error == null)
            {
                callback.onSuccess();
//                SDToast.showShort(mContext.getString(R.string.save_suceess));
            } else
            {
//                SDToast.showShort(mContext.getString(R.string.send_fails));
                callback.onFailed(error);
            }
        }
    }

    public void startSend(String host, String port, String password, String from, String content, String title,/*String
    addresses,*/ List<String> names, List<String> cc, List<SDFileListEntity> files, Callback callback)
    {
        MailAsycnTask task = new MailAsycnTask(host, port, password, from, content, title, /*addresses*/names, cc, files,
                callback);
        task.execute();
    }

    /**
     * 发送邮件
     *
     * @param host      端口
     * @param from      来自
     * @param password
     * @param content
     * @param title
     * @param addresses
     * @param files
     * @throws Exception
     */
    private void send(String host, String port, String password, String from, String content, String title,/*String
    addresses*/List<String> addresses, List<String> cc, List<SDFileListEntity> files) throws Exception
    {
//        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Properties emailProperties = new Properties();
        emailProperties.put("mail.smtp.auth", "true");
        emailProperties.put("mail.smtp.host", host);
        emailProperties.put("mail.smtp.port", port);
//        emailProperties.put("mail.smtp.socketFactory.port", port);
//        emailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//        emailProperties.put("mail.smtp.socketFactory.fallback", "false");
        emailProperties.put("mail.smtp.starttls.enable", "true");

        String finalString = "";
        Session session = Session.getInstance(emailProperties, new MailAuthenticator(from, password));
//        session.setDebug(true);
        DataHandler handler = new DataHandler(new ByteArrayDataSource(finalString.getBytes(), "text/plain"));
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setDataHandler(handler);

        // add handlers for main MIME types
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mc);

        MimeMultipart multiPart = new MimeMultipart();
        multiPart = addFiles(files, multiPart);

        if (files != null)
        {
            for (SDFileListEntity filePath : files)
            {
                // 添加文件
                BodyPart attachPart = new MimeBodyPart();
                File file = new File(filePath.getAndroidFilePath());
                SDLogUtil.debug("file exists:" + file.exists());
                FileDataSource fds = new FileDataSource(file); //打开要发送的文件
                attachPart.setDataHandler(new DataHandler(fds));
                String fileNameNew = MimeUtility.encodeText(fds.getName(), "utf-8", null);//设置文件名称显示编码，解决乱码问题
                attachPart.setFileName(fileNameNew);
                multiPart.addBodyPart(attachPart);
            }
        }


        BodyPart textBodyPart = new MimeBodyPart();
        textBodyPart.setText(content);
        textBodyPart.setContent(content, "text/html;charset=utf-8");
        multiPart.addBodyPart(textBodyPart);
        SDLogUtil.debug("count:" + multiPart.getCount());
        addCc(cc, message);
        message.addFrom(addAddr(addresses, message)); // Set the from address
        message.addRecipients(Message.RecipientType.TO, addAddr(addresses, message));
        message.setSubject(title);  // 标题
        message.setContent(multiPart, "text/html;charset=utf-8"); // 内容
        message.saveChanges();

        Transport transport = session.getTransport("smtp");
        transport.connect(host, from, password);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    private InternetAddress[] addCc(List<String> cc, MimeMessage message) throws MessagingException
    {
        InternetAddress[] toCc = null;
        if (cc != null)
        {  // 抄送
            toCc = new InternetAddress[cc.size()];
            for (int i = 0; i < toCc.length; i++)
            {
                InternetAddress iaddr = new InternetAddress();
                iaddr.setAddress(cc.get(i));
                toCc[i] = iaddr;
            }
            message.addRecipients(Message.RecipientType.CC, toCc);
        }
        return toCc;
    }

    private InternetAddress[] addAddr(List<String> addresses, MimeMessage message) throws MessagingException
    {
        InternetAddress[] toAddress = null;
        if (addresses != null)
        { // 发送地址
            toAddress = new InternetAddress[addresses.size()];
            for (int i = 0; i < toAddress.length; i++)
            {
                InternetAddress iaddr = new InternetAddress();
                iaddr.setAddress(addresses.get(i));
                toAddress[i] = iaddr;
            }
            message.addRecipients(Message.RecipientType.TO, toAddress);
        }
        return toAddress;
    }

    private MimeMultipart addFiles(List<SDFileListEntity> files, MimeMultipart multiPart) throws MessagingException,
            UnsupportedEncodingException
    {

        return multiPart;
    }
}
