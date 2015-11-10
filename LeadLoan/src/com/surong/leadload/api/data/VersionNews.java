package com.surong.leadload.api.data;

import java.io.Serializable;

public class VersionNews implements Serializable{
	private int id;
	private String number;
	private String name;
	private String log;
	private String introduction;
	private Long verSize;
	private String createTime;
	private String downloadLink;
	public VersionNews(int id, String number, String name, String log,
			String introduction, Long verSize, String createTime,
			String downloadLink) {
		super();
		this.id = id;
		this.number = number;
		this.name = name;
		this.log = log;
		this.introduction = introduction;
		this.verSize = verSize;
		this.createTime = createTime;
		this.downloadLink = downloadLink;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public Long getVerSize() {
		return verSize;
	}
	public void setVerSize(Long verSize) {
		this.verSize = verSize;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getDownloadLink() {
		return downloadLink;
	}
	public void setDownloadLink(String downloadLink) {
		this.downloadLink = downloadLink;
	}
}
