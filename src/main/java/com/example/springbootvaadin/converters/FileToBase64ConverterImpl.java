package com.example.springbootvaadin.converters;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

import org.springframework.stereotype.Service;

@Service
public class FileToBase64ConverterImpl implements FileToBase64Converter{

	@Override
	public String convertFileToBase64(String filePath) throws IOException, URISyntaxException {
		Path path = Path.of(filePath);
		byte[] fileBytes = Files.readAllBytes(path);
		return Base64.getEncoder().encodeToString(fileBytes);
	}

}
