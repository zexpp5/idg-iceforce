package com.cxgz.activity.cx.msg.email.test;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Created by selson on 2017/12/18.
 */

public class GMailAuthenticator extends Authenticator
{
    String user;
    String pw;

    public GMailAuthenticator(String username, String password)
    {
        super();
        this.user = username;
        this.pw = password;
    }

    public PasswordAuthentication getPasswordAuthentication()
    {
        return new PasswordAuthentication(user, pw);
    }
}
