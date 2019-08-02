package com.app.view;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.app.model.GoodRecieveNote;



public class GRNExcelView extends AbstractXlsxView{

	@SuppressWarnings("unchecked")
	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		// set file name 
		response.setHeader("Content-Disposition", "attachment;filename=GRN.xls");
		
		// read data from model
		List<GoodRecieveNote> goodRecieveNote= (List<GoodRecieveNote>) model.get("goodRecieveNote");
		// create sheet with name as GRN deatails
		Sheet sheet=workbook.createSheet("GRN Details");
		//set head row as #0
		setHead(sheet);
		//set  body row 
		setBody(sheet,goodRecieveNote);
	}

	private void setHead(Sheet sheet) {
		
		Row row=sheet.createRow(0);
		row.createCell(0).setCellValue("ID");
		row.createCell(1).setCellValue("CODE");
		row.createCell(2).setCellValue("TYPE");
		row.createCell(3).setCellValue("PURCHASE ORDER CODE");
		row.createCell(4).setCellValue("NOTE");
	}

	private void setBody(Sheet sheet, List<GoodRecieveNote> goodRecieveNote) {
		
		int rowCount=1;
		for (GoodRecieveNote g : goodRecieveNote) {
			Row row=sheet.createRow(rowCount++);
			row.createCell(0).setCellValue(g.getGrnId().toString());
			row.createCell(1).setCellValue(g.getGrnCode());
			row.createCell(2).setCellValue(g.getGrnType());
			row.createCell(3).setCellValue(g.getPurchase().getOrderCode());
			row.createCell(4).setCellValue(g.getGrnDesc());
		}
		
	}

}
