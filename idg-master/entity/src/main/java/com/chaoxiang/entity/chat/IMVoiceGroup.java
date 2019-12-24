package com.chaoxiang.entity.chat;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "DDX_IM_VOICE_GROUP".
 */
public class IMVoiceGroup {

    private Long id;
    private String groupId;
    private Long createTime;
    private Long updateTime;
    private Long joinTime;
    private Long time;
    private Boolean isFinish;
    private String attachment;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public IMVoiceGroup() {
    }

    public IMVoiceGroup(Long id) {
        this.id = id;
    }

    public IMVoiceGroup(Long id, String groupId, Long createTime, Long updateTime, Long joinTime, Long time, Boolean isFinish, String attachment) {
        this.id = id;
        this.groupId = groupId;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.joinTime = joinTime;
        this.time = time;
        this.isFinish = isFinish;
        this.attachment = attachment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Long getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(Long joinTime) {
        this.joinTime = joinTime;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Boolean getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(Boolean isFinish) {
        this.isFinish = isFinish;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
