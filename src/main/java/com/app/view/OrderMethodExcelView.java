package com.app.view;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import com.app.model.OrderMethod;

public class OrderMethodExcelView extends AbstractXlsView {

	@SuppressWarnings("unchecked")
	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		//changing file name
		response.addHeader("Content-Disposition","attachment;filename=OrderMethod.xls" );
		
		//getting data 
		List<OrderMethod> list = (List<OrderMethod>) model.get("list");
		
		//creating sheet
		
		Sheet sheet = workbook.createSheet("OrderMethod");
		

		//create row #0 for header
		setHead(sheet);
		
		// create row #1 onwards for body 
		setBody(sheet,list);
	}

	
	private void setHead(Sheet sheet) {
		
		Row row = sheet.createRow(0); // creating 0th row
		
		row.createCell(0).setCellValue("ID");
		row.createCell(1).setCellValue("MODE");
		row.createCell(2).setCellValue("CODE");
		row.createCell(3).setCellValue("EXECUTE TYPE");
		row.createCell(4).setCellValue("ORDER ACCEPT");
		row.createCell(5).setCellValue("NOTE");
		
	}

	private void setBody(Sheet sheet, List<OrderMethod> list) {
		
		int rowId=1;
		
		for(OrderMethod om :list)
		{

			Row row = sheet.createRow(rowId++); // 1th onwords
			
			row.createCell(0).setCellValue(om.getOrderId().toString());
			row.createCell(1).setCellValue(om.getOrderMode());
			row.createCell(2).setCellValue(om.getOrderCode());
			row.createCell(3).setCellValue(om.getExeType());
			row.createCell(4).setCellValue(om.getOrderAccpet()!=null?
					om.getOrderAccpet().toString():"[]");
			row.createCell(5).setCellValue(om.getOrderNote());
		}
	}

	
}
