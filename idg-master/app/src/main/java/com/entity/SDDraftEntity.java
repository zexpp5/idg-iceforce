package com.entity;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

@SuppressWarnings("serial")
@Table(name="sd_draft")
public class SDDraftEntity extends BaseEntity{
	/**
	 * 用户id
	 */
	@Column(column="user_id")
	private String userId;

	@Column(column = "company_id")
	private String companyId;
	/**
	 * 类型
	 */
	@Column(column="type")
	private int type;
	@Column(column="type_name")
	private String typeName;
	/**
	 * 内容
	 */
	@Column(column="content")
	private String content;
	/**
	 * 数据
	 */
	@Column(column="data")
	private String data;
	/**
	 * 是否已为草稿  1=是,2=不是
	 */
	@Column(column="flag")
	private int flag;
	/**
	 * 是否为提交失败数据 1=是,2=不是
	 */
	@Column(column="is_fail")
	private int isFail;
	/**
	 * 创建时间
	 */
	@Column(column="create_time")
	private long createTime;

	/**
	 * 单号标示
	 */
	@Column(column = "tickets")
	private String tickets;

	/**
	 * 回复的基础数据(类型数据:如业务类型,点评,汇报工作,审批等类型)
	 */
	@Column(column="reply_base_data")
	private String replyBaseData;

	public String getReplyBaseData() {
		return replyBaseData;
	}

	public void setReplyBaseData(String replyBaseData) {
		this.replyBaseData = replyBaseData;
	}

	public static class replyBaseInfo{
		private int replyType;
		private int businessType;
		private int postReplyType;
		private long bid;
		private int position;
		/**
		 * 批复状态 0=同意,1=不同意
		 */
		private int approvalStatus;
		/**
		 * 申请人id
		 */
		private int applyUserId;
		/**
		 * 审批类型
		 */
		private int approvalTypes;

		private long taskId;

		public long getTaskId() {
			return taskId;
		}

		public void setTaskId(long taskId) {
			this.taskId = taskId;
		}

		public int getApprovalTypes() {
			return approvalTypes;
		}

		public void setApprovalTypes(int approvalTypes) {
			this.approvalTypes = approvalTypes;
		}

		public int getApplyUserId() {
			return applyUserId;
		}

		public void setApplyUserId(int applyUserId) {
			this.applyUserId = applyUserId;
		}

		public int getApprovalStatus() {
			return approvalStatus;
		}

		public void setApprovalStatus(int approvalStatus) {
			this.approvalStatus = approvalStatus;
		}

		public int getReplyType() {
			return replyType;
		}

		public void setReplyType(int replyType) {
			this.replyType = replyType;
		}

		public int getBusinessType() {
			return businessType;
		}

		public void setBusinessType(int businessType) {
			this.businessType = businessType;
		}

		public int getPostReplyType() {
			return postReplyType;
		}

		public void setPostReplyType(int postReplyType) {
			this.postReplyType = postReplyType;
		}


		public long getBid() {
			return bid;
		}

		public void setBid(long bid) {
			this.bid = bid;
		}

		public int getPosition() {
			return position;
		}

		public void setPosition(int position) {
			this.position = position;
		}
	}

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public int getIsFail() {
		return isFail;
	}
	public void setIsFail(int isFail) {
		this.isFail = isFail;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getTickets() {
		return tickets;
	}

	public void setTickets(String tickets) {
		this.tickets = tickets;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SDDraftEntity)) return false;

		SDDraftEntity that = (SDDraftEntity) o;

		if (getType() != that.getType()) return false;
		if (getFlag() != that.getFlag()) return false;
		if (getIsFail() != that.getIsFail()) return false;
		if (getCreateTime() != that.getCreateTime()) return false;
		if (getUserId() != null ? !getUserId().equals(that.getUserId()) : that.getUserId() != null)
			return false;
		if (getCompanyId() != null ? !getCompanyId().equals(that.getCompanyId()) : that.getCompanyId() != null)
			return false;
		if (getTypeName() != null ? !getTypeName().equals(that.getTypeName()) : that.getTypeName() != null)
			return false;
		if (getContent() != null ? !getContent().equals(that.getContent()) : that.getContent() != null)
			return false;
		if (getData() != null ? !getData().equals(that.getData()) : that.getData() != null)
			return false;
		if (getTickets() != null ? !getTickets().equals(that.getTickets()) : that.getTickets() != null)
			return false;
		return !(getReplyBaseData() != null ? !getReplyBaseData().equals(that.getReplyBaseData()) : that.getReplyBaseData() != null);

	}

	@Override
	public int hashCode() {
		int result = getUserId() != null ? getUserId().hashCode() : 0;
		result = 31 * result + (getCompanyId() != null ? getCompanyId().hashCode() : 0);
		result = 31 * result + getType();
		result = 31 * result + (getTypeName() != null ? getTypeName().hashCode() : 0);
		result = 31 * result + (getContent() != null ? getContent().hashCode() : 0);
		result = 31 * result + (getData() != null ? getData().hashCode() : 0);
		result = 31 * result + getFlag();
		result = 31 * result + getIsFail();
		result = 31 * result + (int) (getCreateTime() ^ (getCreateTime() >>> 32));
		result = 31 * result + (getTickets() != null ? getTickets().hashCode() : 0);
		result = 31 * result + (getReplyBaseData() != null ? getReplyBaseData().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "SDDraftEntity{" +
				"userId='" + userId + '\'' +
				", companyId='" + companyId + '\'' +
				", type=" + type +
				", typeName='" + typeName + '\'' +
				", content='" + content + '\'' +
				", data='" + data + '\'' +
				", flag=" + flag +
				", isFail=" + isFail +
				", createTime=" + createTime +
				", tickets='" + tickets + '\'' +
				", replyBaseData='" + replyBaseData + '\'' +
				'}';
	}
}
