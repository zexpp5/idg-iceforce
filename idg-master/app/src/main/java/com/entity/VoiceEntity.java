package com.entity;

import java.io.Serializable;

/**
 * 语音
 * @author zjh
 * @date 2015-5-23
 * @desc
 */
@SuppressWarnings("serial")
public class VoiceEntity implements Serializable {
	private String filePath;
	private String fileName;
	private int length;
	
	public VoiceEntity() {
		super();
	}
	
	public VoiceEntity(String filePath, String fileName, int length) {
		super();
		this.filePath = filePath;
		this.fileName = fileName;
		this.length = length;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
}
