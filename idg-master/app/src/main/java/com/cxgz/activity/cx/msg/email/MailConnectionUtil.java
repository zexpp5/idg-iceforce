package com.cxgz.activity.cx.msg.email;

import android.widget.SimpleAdapter;

import com.cxgz.activity.cx.msg.email.test.GMailAuthenticator;
import com.cxgz.activity.cx.msg.email.test.ReciveOneMail;
import com.injoy.idg.R;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.MimeMessage;

import static com.injoy.idg.R.id.lv;

/**
 * 邮箱连接工具类
 *
 * @author xiaoli
 */
public class MailConnectionUtil
{
    private Store store;
    private Folder folder;
    private Session session;
    private URLName urlname;
    private String host;
    private String protocol;
    private String user;
    private String pwd;
    private int port;

    public MailConnectionUtil(String host, int port, String protocol, String user, String pwd)
    {
        this.host = host;
        this.protocol = protocol;
        this.user = user;
        this.pwd = pwd;
        this.port = port;
    }

    public Folder connect() throws MessagingException
    {
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mc);
        Properties props = new Properties();
        props.setProperty("mail." + protocol + ".socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail." + protocol + ".socketFactory.fallback", "false");
        props.setProperty("mail." + protocol + ".port", String.valueOf(port));
        props.setProperty("mail." + protocol + ".partialfetch", "false");
        props.setProperty("mail." + protocol + ".socketFactory.port", String.valueOf(port));
        props.setProperty("mail." + protocol + ".host", host);
        session = Session.getInstance(props, null);
        urlname = new URLName(protocol, host, port, null, user, pwd);
        store = session.getStore(urlname);
        store.connect();
        folder = store.getFolder("INBOX");
        folder.open(Folder.READ_ONLY);
        return folder;
    }

    public void close() throws MessagingException
    {
        if (folder != null && store != null)
        {
            if (folder.isOpen()) folder.close(false);
            if (store.isConnected()) store.close();
        }
    }
}
