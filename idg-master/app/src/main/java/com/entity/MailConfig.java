package com.entity;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by doubihong on 15/11/16.
 */
@Table(name = "MAIL_CONFIG")
public class MailConfig {
    /**
     * host:imap.qq.com
     * password:123456789
     * port:993
     * username:2076659203@qq.com
     * protocal:imap
     */
    @Id(column="_ID")
    private int id;
    @Column(column = "userId")
    private int userId;
    @Column(column = "password")
    private String password;
    @Column(column = "host")
    private String host;
    @Column(column = "port")
    private int port;
    @Column(column = "account")
    private String account;
    @Column(column = "protocal")
    private String protocal;
    @Column(column = "selected")
    private boolean selected;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getProtocal() {
        return protocal;
    }

    public void setProtocal(String protocal) {
        this.protocal = protocal;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
