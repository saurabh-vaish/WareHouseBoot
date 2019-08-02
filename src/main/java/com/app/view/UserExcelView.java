package com.app.view;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import com.app.model.User;

public class UserExcelView extends AbstractXlsView {

	@SuppressWarnings("unchecked")
	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	

		response.addHeader("Content-Disposition", "attachment:UserExcel.xls");
		
		List<User> list = (List<User>) model.get("list");
		
		Sheet sheet = workbook.createSheet("UserExcel");
		
		getHead(sheet);
		
		getBody(sheet,list);
	}

	private void getHead(Sheet sheet) {
		
		Row row = sheet.createRow(0);
		
		row.createCell(0).setCellValue("Id");
		row.createCell(1).setCellValue("ID");
		row.createCell(2).setCellValue("NAME");
		row.createCell(3).setCellValue("GENDER");
		row.createCell(4).setCellValue("EMAIL");
		row.createCell(5).setCellValue("MOBILE");
		row.createCell(6).setCellValue("ROLES");
		
	
	}

	private void getBody(Sheet sheet, List<User> list) {
		
		int rowId=1;
		
		for(User u : list)
		{
			Row row = sheet.createRow(rowId++);
			
			row.createCell(0).setCellValue(u.getUserId());
			row.createCell(1).setCellValue(u.getUserName());
			row.createCell(2).setCellValue(u.getGender());
			row.createCell(3).setCellValue(u.getUserEmail());
			row.createCell(4).setCellValue(u.getUserMobile());
			row.createCell(5).setCellValue(u.getRoles()!=null?u.getRoles().toString():"[]");
			
		}
		
	}

}
