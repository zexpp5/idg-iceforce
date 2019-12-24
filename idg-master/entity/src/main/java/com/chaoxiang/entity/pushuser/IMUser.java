package com.chaoxiang.entity.pushuser;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

/**
 * Entity mapped to table "DDX_IM_USER".
 */
public class IMUser
{

    private Long _id;
    private String attachment;
    private String account;
    private String icon;
    private String name;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public IMUser()
    {
    }

    public IMUser(Long _id)
    {
        this._id = _id;
    }

    public IMUser(Long _id, String attachment, String account)
    {
        this._id = _id;
        this.attachment = attachment;
        this.account = account;
    }

    public IMUser(Long _id, String attachment, String account, String icon, String name)
    {
        this._id = _id;
        this.attachment = attachment;
        this.account = account;
        this.icon = icon;
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public Long get_id()
    {
        return _id;
    }

    public void set_id(Long _id)
    {
        this._id = _id;
    }

    public String getAttachment()
    {
        return attachment;
    }

    public void setAttachment(String attachment)
    {
        this.attachment = attachment;
    }

    public String getAccount()
    {
        return account;
    }

    public void setAccount(String account)
    {
        this.account = account;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}