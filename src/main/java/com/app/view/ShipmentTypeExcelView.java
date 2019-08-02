package com.app.view;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import com.app.model.ShipmentType;

public class ShipmentTypeExcelView extends AbstractXlsView{

	@SuppressWarnings("unchecked")
	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		//changing file name
		response.setHeader("Content-Disposition","attachment;filename=Shipment.xls");
		
		//reading data from ModelAndView
		List<ShipmentType> list = (List<ShipmentType>)model.get("list"); // casting to list type , downcasting
		
		//creating sheet
		Sheet sheet = workbook.createSheet("shipments");
		
		//create row #0 for header
		setHead(sheet);
		
		// create row #1 onwards for body 
		setBody(sheet,list);
	}

	private void setHead(Sheet sheet) {
		Row row = sheet.createRow(0); // creating row
		// adding data to cell
		row.createCell(0).setCellValue("ID");
		row.createCell(1).setCellValue("MODE");
		row.createCell(2).setCellValue("CODE");
		row.createCell(3).setCellValue("ENABLE");
		row.createCell(4).setCellValue("GRADE");
		row.createCell(5).setCellValue("NOTE");
		
	}

	private void setBody(Sheet sheet, List<ShipmentType> list) {
		int rowId=1;
		
		for(ShipmentType s : list)
		{
			// creating row according to data
			Row row =sheet.createRow(rowId++);
			
			//adding data to cell
			row.createCell(0).setCellValue(s.getShipmentId());
			row.createCell(1).setCellValue(s.getShipmentMode());
			row.createCell(2).setCellValue(s.getShipmentCode());
			row.createCell(3).setCellValue(s.getEnableShipment());
			row.createCell(4).setCellValue(s.getShipmentGrade());
			row.createCell(5).setCellValue(s.getNote());
			
		}
		
	}	

}
