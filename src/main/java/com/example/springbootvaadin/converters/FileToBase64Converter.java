package com.example.springbootvaadin.converters;

import java.io.IOException;
import java.net.URISyntaxException;

public interface FileToBase64Converter {

	String convertFileToBase64(String filePath) throws IOException, URISyntaxException;
	String convertResourceToBase64(String resourcePath) throws IOException, URISyntaxException;
	String convertByteArayToBase64(byte[] generateBarCode);
}
