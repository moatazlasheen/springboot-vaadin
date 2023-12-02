package com.example.springbootvaadin.pdf;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Set;

import com.example.springbootvaadin.model.Todo;
import com.itextpdf.text.DocumentException;

public interface PDFGenerationService {

	InputStream generatePDF(Set<Todo> selectedItems) throws IOException, URISyntaxException, DocumentException;

}
