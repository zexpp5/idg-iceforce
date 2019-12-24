package com.chaoxiang.entity.chat;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "DDX_IM_MESSAGE".
 */
public class IMMessage {

    private Long id;
    private String messageId;
    private Integer type;
    private Byte priority;
    private String _from;
    private String _to;
    private String groupId;
    private Boolean isReaded;
    private String message;
    private String createTime;
    private Long createTimeMillisecond;
    private String readTime;
    private Long readTimeMillisecond;
    private Integer onlineStatus;
    private Integer msgChatType;
    private Integer msgStatus;
    private Integer direct;
    private String attachment;
    private Integer hotTime;
    private Boolean isHotChatVisible;
    private Boolean isHotChat;
    private Boolean isReadStatus;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public IMMessage() {
    }

    public IMMessage(Long id) {
        this.id = id;
    }

    public IMMessage(Long id, String messageId, Integer type, Byte priority, String _from, String _to, String groupId, Boolean isReaded, String message, String createTime, Long createTimeMillisecond, String readTime, Long readTimeMillisecond, Integer onlineStatus, Integer msgChatType, Integer msgStatus, Integer direct, String attachment, Integer hotTime, Boolean isHotChatVisible, Boolean isHotChat, Boolean isReadStatus) {
        this.id = id;
        this.messageId = messageId;
        this.type = type;
        this.priority = priority;
        this._from = _from;
        this._to = _to;
        this.groupId = groupId;
        this.isReaded = isReaded;
        this.message = message;
        this.createTime = createTime;
        this.createTimeMillisecond = createTimeMillisecond;
        this.readTime = readTime;
        this.readTimeMillisecond = readTimeMillisecond;
        this.onlineStatus = onlineStatus;
        this.msgChatType = msgChatType;
        this.msgStatus = msgStatus;
        this.direct = direct;
        this.attachment = attachment;
        this.hotTime = hotTime;
        this.isHotChatVisible = isHotChatVisible;
        this.isHotChat = isHotChat;
        this.isReadStatus = isReadStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Byte getPriority() {
        return priority;
    }

    public void setPriority(Byte priority) {
        this.priority = priority;
    }

    public String get_from() {
        return _from;
    }

    public void set_from(String _from) {
        this._from = _from;
    }

    public String get_to() {
        return _to;
    }

    public void set_to(String _to) {
        this._to = _to;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Boolean getIsReaded() {
        return isReaded;
    }

    public void setIsReaded(Boolean isReaded) {
        this.isReaded = isReaded;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getCreateTimeMillisecond() {
        return createTimeMillisecond;
    }

    public void setCreateTimeMillisecond(Long createTimeMillisecond) {
        this.createTimeMillisecond = createTimeMillisecond;
    }

    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }

    public Long getReadTimeMillisecond() {
        return readTimeMillisecond;
    }

    public void setReadTimeMillisecond(Long readTimeMillisecond) {
        this.readTimeMillisecond = readTimeMillisecond;
    }

    public Integer getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public Integer getMsgChatType() {
        return msgChatType;
    }

    public void setMsgChatType(Integer msgChatType) {
        this.msgChatType = msgChatType;
    }

    public Integer getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(Integer msgStatus) {
        this.msgStatus = msgStatus;
    }

    public Integer getDirect() {
        return direct;
    }

    public void setDirect(Integer direct) {
        this.direct = direct;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public Integer getHotTime() {
        return hotTime;
    }

    public void setHotTime(Integer hotTime) {
        this.hotTime = hotTime;
    }

    public Boolean getIsHotChatVisible() {
        return isHotChatVisible;
    }

    public void setIsHotChatVisible(Boolean isHotChatVisible) {
        this.isHotChatVisible = isHotChatVisible;
    }

    public Boolean getIsHotChat() {
        return isHotChat;
    }

    public void setIsHotChat(Boolean isHotChat) {
        this.isHotChat = isHotChat;
    }

    public Boolean getIsReadStatus() {
        return isReadStatus;
    }

    public void setIsReadStatus(Boolean isReadStatus) {
        this.isReadStatus = isReadStatus;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
