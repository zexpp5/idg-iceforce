package com.chaoxiang.entity.chat;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "DDX_IMAudioFail".
 */
public class IMAudioFail {

    private Long id;
    private String attachment;
    private String messageId;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public IMAudioFail() {
    }

    public IMAudioFail(Long id) {
        this.id = id;
    }

    public IMAudioFail(Long id, String attachment, String messageId) {
        this.id = id;
        this.attachment = attachment;
        this.messageId = messageId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
