package com.example.springbootvaadin.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.example.springbootvaadin.model.FileInfo;

public interface AttachementService {

	void saveFile(String fileName, InputStream is, long length);

	List<FileInfo> getAllFiles();
	
	InputStream getFileAsResource(String objectId) throws IllegalStateException, IOException;

	void deleteFile(String objectId);

}
