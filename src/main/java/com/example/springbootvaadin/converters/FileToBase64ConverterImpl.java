package com.example.springbootvaadin.converters;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import org.springframework.stereotype.Service;

@Service
public class FileToBase64ConverterImpl implements FileToBase64Converter{

	@Override
	public String convertFileToBase64(String filePath) throws IOException, URISyntaxException {
		byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
		return Base64.getEncoder().encodeToString(fileBytes);
	}

	@Override
	public String convertResourceToBase64(String resourcePath) throws IOException, URISyntaxException {
		URI resourceURI = this.getClass().getClassLoader().getResource(resourcePath).toURI();
		byte[] fileBytes = Files.readAllBytes(Paths.get(resourceURI));
		return Base64.getEncoder().encodeToString(fileBytes);
	}

	@Override
	public String convertByteArayToBase64(byte[] bytes) {
		return Base64.getEncoder().encodeToString(bytes);
	}

}
