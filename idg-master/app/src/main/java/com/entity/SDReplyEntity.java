package com.entity;

import android.text.Spannable;

import com.cxgz.activity.db.dao.SDUserEntity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class SDReplyEntity implements Serializable{
	private String content;
	private List<SDFileListEntity> annex;
	private long rid;
	private String realName;
	private String replyTime;
	private String lz;
	private String uid;
	private SDUserEntity userEntity;
	private List<At> at;


	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getRid() {
		return rid;
	}
	public void setRid(long rid) {
		this.rid = rid;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getReplyTime() {
		return replyTime;
	}
	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}
	public String getLz() {
		return lz;
	}
	public void setLz(String lz) {
		this.lz = lz;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public SDUserEntity getUserEntity() {
		return userEntity;
	}
	public void setUserEntity(SDUserEntity userEntity) {
		this.userEntity = userEntity;
	}
	public List<SDFileListEntity> getAnnex() {
		return annex;
	}
	public void setAnnex(List<SDFileListEntity> annex) {
		this.annex = annex;
	}
	public List<At> getAt() {
		return at;
	}
	public void setAt(List<At> atuid) {
		this.at = atuid;
	}

	public static class At implements Serializable {
		private int atuid;
		private String pos;
		private String name;
		private Spannable context;

		@Override
		public String toString() {
			return "At{" +
					"atuid=" + atuid +
					", pos='" + pos + '\'' +
					", name='" + name + '\'' +
					", context=" + context +
					'}';
		}

		public int getEnd() {
			try {
				JSONArray array = new JSONArray(pos);
				return (int)array.get(1);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return  -1;
		}

		public At(int atuid, String pos, String name) {
			this.atuid = atuid;
			this.pos = pos;
			this.name = name;
		}

		public At(int atuid, String pos) {
			this.atuid = atuid;
			this.pos = pos;
		}

		public At() {

		}


		public int getStart() {
			try {
				JSONArray array = new JSONArray(pos);
				return (int)array.get(0);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return  -1;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Spannable getContext() {
			return context;
		}

		public void setContext(Spannable context) {
			this.context = context;
		}

		public int getAtuid() {
			return atuid;
		}

		public void setAtuid(int atuid) {
			this.atuid = atuid;
		}

		public String getPos() {
			return pos;
		}

		public void setPos(String pos) {
			this.pos = pos;
		}
	}
	@Override
	public String toString() {
		return "SDReplyEntity [content=" + content + ", annex=" + annex
				+ ", rid=" + rid + ", realName=" + realName + ", replyTime="
				+ replyTime + ", lz=" + lz + ", uid=" + uid + ", userEntity="
				+ userEntity + "]";
	}
	
}
