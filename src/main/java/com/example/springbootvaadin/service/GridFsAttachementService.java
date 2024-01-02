package com.example.springbootvaadin.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;

import com.example.springbootvaadin.model.FileInfo;
import com.mongodb.client.gridfs.model.GridFSFile;

@Service
public class GridFsAttachementService implements AttachementService {

	@Autowired
	private GridFsOperations gridFsOperations;
	
	@Override
	public void saveFile(String fileName, InputStream is) {
		gridFsOperations.store(is, fileName);
	}

	@Override
	public List<FileInfo> getAllFiles() {
		List<FileInfo> files = new ArrayList<>();
		gridFsOperations.find(new Query()).forEach(item -> {
			files.add(new FileInfo(item.getFilename(), item.getObjectId().toString()));
		});
		
		return files;
	}

	@Override
	public InputStream getFileAsResource(String objectId) throws IllegalStateException, IOException {
		final GridFSFile gridFSFile = gridFsOperations.findOne(new Query(Criteria.where("_id").is(new ObjectId(objectId))));
		return gridFsOperations.getResource(gridFSFile).getInputStream();
	}

}
