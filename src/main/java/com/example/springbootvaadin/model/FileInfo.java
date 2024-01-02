package com.example.springbootvaadin.model;

public class FileInfo {

	private String fileName;
	private String ObjectId;
	
	
	public FileInfo(String fileName, String objectId) {
		super();
		this.fileName = fileName;
		ObjectId = objectId;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getObjectId() {
		return ObjectId;
	}
	public void setObjectId(String objectId) {
		ObjectId = objectId;
	}
	
	
	
}
