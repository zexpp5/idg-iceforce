package com.entity;

import android.text.style.ImageSpan;

import com.cxgz.activity.db.dao.SDUserEntity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SDTopicEntity implements Serializable {

	public SDTopicEntity(){}

	public SDTopicEntity(int id, int companyId, int userId, String userName, String title, String createTime, String lz, SDUserEntity userentity, ImageSpan imageSpan) {
		this.id = id;
		this.companyId = companyId;
		this.userId = userId;
		this.userName = userName;
		this.title = title;
		this.createTime = createTime;
		Lz = lz;
		this.userentity = userentity;
		this.imageSpan = imageSpan;
	}

	private int id;
	private int companyId;
	private int userId;
	private String userName;
	private String title;
	private String createTime;
	private String Lz;
	private SDUserEntity userentity;
	private transient ImageSpan imageSpan;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getcompanyId() {
		return companyId;
	}
	public void setcompanyId(int companyId) {
		this.companyId = companyId;
	}
	public int getuserId() {
		return userId;
	}
	public void setuserId(int userId) {
		this.userId = userId;
	}
	public String getuserName() {
		return userName;
	}
	public void setuserName(String userName) {
		this.userName = userName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getcreateTime() {
		return createTime;
	}
	public void setcreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	public String getlz() {
		return Lz;
	}
	public void setlz(String lz) {
		this.Lz = lz;
	}
	
	public SDUserEntity getuserentity(){
		return userentity;
	}
	
	public void setuserentity(SDUserEntity userentity){
		this.userentity=userentity;
	}

	public ImageSpan getImageSpan() {
		return imageSpan;
	}

	public void setImageSpan(ImageSpan imageSpan) {
		this.imageSpan = imageSpan;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SDTopicEntity)) return false;

		SDTopicEntity that = (SDTopicEntity) o;

		if (getId() != that.getId()) return false;
		if (companyId != that.companyId) return false;
		if (userId != that.userId) return false;
		if (userName != null ? !userName.equals(that.userName) : that.userName != null)
			return false;
		if (getTitle() != null ? !getTitle().equals(that.getTitle()) : that.getTitle() != null)
			return false;
		if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null)
			return false;
		if (Lz != null ? !Lz.equals(that.Lz) : that.Lz != null) return false;
		return !(userentity != null ? !userentity.equals(that.userentity) : that.userentity != null);

	}

	@Override
	public int hashCode() {
		int result = getId();
		result = 31 * result + companyId;
		result = 31 * result + userId;
		result = 31 * result + (userName != null ? userName.hashCode() : 0);
		result = 31 * result + (getTitle() != null ? getTitle().hashCode() : 0);
		result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
		result = 31 * result + (Lz != null ? Lz.hashCode() : 0);
		result = 31 * result + (userentity != null ? userentity.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "SDTopicEntity{" +
				"id=" + id +
				", companyId=" + companyId +
				", userId=" + userId +
				", userName='" + userName + '\'' +
				", title='" + title + '\'' +
				", createTime='" + createTime + '\'' +
				", Lz='" + Lz + '\'' +
				", userentity=" + userentity +
				", imageSpan=" + imageSpan +
				'}';
	}
}
