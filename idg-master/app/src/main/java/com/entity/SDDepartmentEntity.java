package com.entity;

import com.cxgz.activity.cxim.bean.SDSortEntity;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

@Table(name="SYS_DEPARTMENT")
public class SDDepartmentEntity extends SDSortEntity implements Serializable
{
	/**
	 * 部门ID
	 */
	@Id(column="dpId")
	@NoAutoIncrement
	private int dpId;
	/**
	 * 部门名称
	 */
	@Column(column="dpName")
	private String dpName;

	@Column(column="dpCode")
	private String dpCode;


	private String workType;//分管部门
	private String splitDp;
	public  boolean isChecked = false;

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean checked) {
		isChecked = checked;
	}

	public void setSplitDp(String splitDp) {
		this.splitDp = splitDp;
	}

	public String getSplitDp() {
		return splitDp;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}

	public String getWorkType() {
		return workType;
	}

	public String getDpCode() {
		return dpCode;
	}

	public void setDpCode(String dpCode) {
		this.dpCode = dpCode;
	}

	public int getDpId()
	{
		return dpId;
	}

	public void setDpId(int dpId)
	{
		this.dpId = dpId;
	}

	public String getDpName()
	{
		return dpName;
	}

	public void setDpName(String dpName)
	{
		this.dpName = dpName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SDDepartmentEntity)) return false;

		SDDepartmentEntity that = (SDDepartmentEntity) o;

		if (getDpId() != that.getDpId()) return false;
		return !(getDpName() != null ? !getDpName().equals(that.getDpName()) : that.getDpName() != null);
	}

	@Override
	public int hashCode() {
		int result = getDpId();
		result = 31 * result + (getDpName() != null ? getDpName().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "SDDepartmentEntity{" +
				"dpId=" + dpId +
				", dpName='" + dpName + '\'' +
				'}';
	}

}
