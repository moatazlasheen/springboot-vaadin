package com.example.springbootvaadin.pdf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import com.example.springbootvaadin.model.Todo;

public interface PDFGenerationService {

	InputStream generatePDF(Set<Todo> selectedItems) throws IOException;

}
