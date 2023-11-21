package com.example.springbootvaadin.excel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.example.springbootvaadin.model.Todo;


@Service
public class POIExcelGenerationService implements ExcelGenerationService {

	@Override
	public InputStream generateExcel(final Collection<Todo> todos) throws IOException {
		InputStream in;
		try(final Workbook wb = new XSSFWorkbook()){
			Sheet sheet = wb.createSheet("todos");
			CellStyle cellStyle = wb.createCellStyle();
			cellStyle.setBorderBottom(BorderStyle.DASHED);
			cellStyle.setFillBackgroundColor(IndexedColors.BLACK.getIndex());
			cellStyle.setFillForegroundColor(IndexedColors.BLUE1.getIndex());
			cellStyle.setFillPattern(FillPatternType.SQUARES);
			Font font = wb.createFont();
			font.setBold(true);
			font.setColor(IndexedColors.CORAL.getIndex());
			
			cellStyle.setFont(font);
			
			createHeader(sheet, cellStyle);
			int index = 1;
			for (Todo todo : todos) {
				createTodoRow(sheet, todo, index++, cellStyle);
			}
			
			final ByteArrayOutputStream os = new ByteArrayOutputStream();
			wb.write(os);
			in = new ByteArrayInputStream(os.toByteArray());
			
		}
		
		return in;
		
	}

	private Row createTodoRow(Sheet sheet, Todo todo, int index, CellStyle cellStyle) {
		Row row = sheet.createRow(index);
		createCell(row, 0, todo.getName(), cellStyle);
		createCell(row, 1, todo.getCreator(), cellStyle);
		createCell(row, 2, todo.getCreationDate(), cellStyle);
		createCell(row, 3, todo.isDone(), cellStyle);
		return row;
	}

	private Row createHeader(Sheet sheet, CellStyle cellStyle) {
		Row row = sheet.createRow(0);
		String [] headerNames = {"Name", "Creator", "Creation Date", "Done"};
		for (int i = 0; i < headerNames.length; i++) {
			createCell(row, i, headerNames[i], cellStyle);
		}
		return row;
	}

	private Cell createCell(Row row, int index, Object value, CellStyle cellStyle) {
		Cell cell = row.createCell(index);
		cell.setCellValue(String.valueOf(value));
		cell.setCellStyle(cellStyle);
		
		return cell;
	}

}
