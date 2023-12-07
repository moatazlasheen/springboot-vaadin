package com.example.springbootvaadin.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.example.springbootvaadin.barcode.BarCodeGenerationService;
import com.example.springbootvaadin.converters.FileToBase64Converter;
import com.example.springbootvaadin.model.Todo;
import com.itextpdf.text.DocumentException;

@Service
public class ItextPdfGenerationService implements PDFGenerationService {

	@Autowired
	private SpringTemplateEngine springTemplateEngine;
	
	@Autowired
	private FileToBase64Converter fileToBase64Converter;
	
	@Autowired
	private BarCodeGenerationService barCodeGenerationService;
	
	@Override
	public InputStream generatePDF(Set<Todo> todos) throws IOException, URISyntaxException, DocumentException {
		Context context = new Context();
		context.setVariable("title", "Todos : " + todos.size());
		context.setVariable("todos", todos);
		context.setVariable("image", "data:image/png;base64, " + fileToBase64Converter.convertResourceToBase64("templates/pdf/images/Mercedes-Logo.svg.png"));
		context.setVariable("barcode", "data:image/png;base64, " + fileToBase64Converter.convertByteArayToBase64(barCodeGenerationService.generateBarCode("some text")));
		
		String html = springTemplateEngine.process("pdf/todos.html", context);
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocumentFromString(html);
	    renderer.layout();
	    renderer.createPDF(os);
	    
		
		return new ByteArrayInputStream(os.toByteArray());
		
		
	}

}
