package com.mxzf.liyun.domain;

import java.io.Serializable;

/**
 * 
 * @Title: LYFile
 * @Dscription: 文件
 * @author Deleter
 * @date 2017年5月26日 下午6:27:05
 * @version 1.0
 */
public class LYFile implements Serializable {
	private String chatName;
	private String fileName;
	private String filePath;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getChatName() {
		return chatName;
	}

	public void setChatName(String chatName) {
		this.chatName = chatName;
	}

	public LYFile(String chatName, String fileName, String filePath) {
		this.chatName = chatName;
		this.fileName = fileName;
		this.filePath = filePath;
	}

	public LYFile() {
	}
}
