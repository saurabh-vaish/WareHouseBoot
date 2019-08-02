package com.app.view;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import com.app.model.WhUser;

public class WhUserExcelView extends AbstractXlsView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		
		response.addHeader("Content-Disposition", "attachment;filename=WhUserExcel.xls");
		
		List<WhUser> list = (List<WhUser>) model.get("list");
		
		Sheet sheet = workbook.createSheet("WhUserExcel");
		
		getHead(sheet);
		
		getBody(sheet,list);
	}

	private void getHead(Sheet sheet) {
		
		Row row = sheet.createRow(0);
		
		row.createCell(0).setCellValue("Id");
		row.createCell(1).setCellValue("Type");
		row.createCell(2).setCellValue("Code");
		row.createCell(3).setCellValue("For");
		row.createCell(4).setCellValue("Email");
		row.createCell(5).setCellValue("Contact");
		row.createCell(6).setCellValue("Id Type");
		row.createCell(7).setCellValue("Id Number");
		
	
	}

	private void getBody(Sheet sheet, List<WhUser> list) {
		
		int rowId=1;
		
		for(WhUser w : list)
		{System.out.println(w);
			Row row = sheet.createRow(rowId++);
			
			row.createCell(0).setCellValue(w.getWhId().toString());
			row.createCell(1).setCellValue(w.getWhType());
			row.createCell(2).setCellValue(w.getWhCode());
			row.createCell(3).setCellValue(w.getWhFor());
			row.createCell(4).setCellValue(w.getWhEmail());
			row.createCell(5).setCellValue(w.getWhContact());
			row.createCell(6).setCellValue(w.getWhIdType());
			row.createCell(7).setCellValue(w.getWhNum());
			
		}
		
	}

}
