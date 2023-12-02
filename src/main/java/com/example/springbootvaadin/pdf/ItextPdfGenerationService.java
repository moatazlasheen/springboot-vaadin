package com.example.springbootvaadin.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.example.springbootvaadin.model.Todo;
import com.itextpdf.html2pdf.HtmlConverter;

@Service
public class ItextPdfGenerationService implements PDFGenerationService {

	@Autowired
	private SpringTemplateEngine springTemplateEngine;
	
	@Override
	public InputStream generatePDF(Set<Todo> todos) throws IOException {
		Context context = new Context();
		context.setVariable("title", "Todos : " + todos.size());
		context.setVariable("todos", todos);
		
		String html = springTemplateEngine.process("pdf/todos.html", context);
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		HtmlConverter.convertToPdf(html, os);
		
		return new ByteArrayInputStream(os.toByteArray());
		
		
	}

}
