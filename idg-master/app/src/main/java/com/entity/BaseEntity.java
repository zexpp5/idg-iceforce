/**
 * @title:BaseEntity.java
 * TODO包含类名列表
 * Copyright (C) oa.cn All right reserved.
 * @version：v3.0,2015年4月8日
 */
package com.entity;

import com.lidroid.xutils.db.annotation.Id;

import java.io.Serializable;


/**@name BaseEntity
 * @author zjh
 * @date 2015年4月8日
 */
@SuppressWarnings("serial")
public abstract class BaseEntity implements Serializable{
	@Id
	protected int id;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	
}

