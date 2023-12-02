package com.example.springbootvaadin.converters;

import java.io.IOException;
import java.net.URISyntaxException;

public interface FileToBase64Converter {

	String convertFileToBase64(String path) throws IOException, URISyntaxException;
}
