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
import com.app.model.Uom;

public class UomExcelView extends AbstractXlsView  {

	@SuppressWarnings("unchecked")
	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		//changing file name
				response.setHeader("Content-Disposition","attachment;filename=Uom.xls");
				
				//reading data from ModelAndView
				List<Uom>list = (List<Uom>)model.get("list"); // casting to list type , downcasting
				
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
		row.createCell(1).setCellValue("TYPE");
		row.createCell(2).setCellValue("CODE");
		row.createCell(3).setCellValue("ENABLED");
		row.createCell(4).setCellValue("META");
		row.createCell(5).setCellValue("SIZE");
		row.createCell(6).setCellValue("NOTE");
		
	}


	private void setBody(Sheet sheet, List<Uom> list) {
		
		int rowId=1;
		
		for(Uom u : list)
		{
			// creating row according to data
			Row row =sheet.createRow(rowId++);
			
			//adding data to cell
			
			row.createCell(0).setCellValue(u.getId());
			row.createCell(1).setCellValue(u.getUomType());
			row.createCell(2).setCellValue(u.getUomCode());
			row.createCell(3).setCellValue(u.getEnableUom());
			row.createCell(4).setCellValue(u.getMetaCode());
			row.createCell(5).setCellValue(u.getAdjSize());
			row.createCell(6).setCellValue(u.getNote());
		}
		
	}

}
