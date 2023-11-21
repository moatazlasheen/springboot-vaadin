package com.example.springbootvaadin.excel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;


import com.example.springbootvaadin.model.Todo;

public interface ExcelGenerationService {
	
	InputStream generateExcel(Collection<Todo> todos) throws IOException;

}
