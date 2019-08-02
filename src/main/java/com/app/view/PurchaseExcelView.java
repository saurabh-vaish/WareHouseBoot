package com.app.view;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.app.model.Purchase;

public class PurchaseExcelView extends AbstractXlsxView{

	@SuppressWarnings("unchecked")
	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setHeader("Content-Disposition", "attachment;filename=Purchases.xls");

		
		List<Purchase> purchase=(List<Purchase>) model.get("purchase");
	
		Sheet sheet=workbook.createSheet("Purchases");
		
		setHead(sheet);
		setBody(sheet,purchase);
		
	}

	private void setHead(Sheet sheet) {

		Row row=sheet.createRow(0);
		row.createCell(0).setCellValue("ID");
		row.createCell(1).setCellValue("CODE");
		row.createCell(2).setCellValue("REF NUM");
		row.createCell(3).setCellValue("QC");
		row.createCell(4).setCellValue("STATUS");
		row.createCell(5).setCellValue("NOTE");
		row.createCell(6).setCellValue("VENDOR");
		row.createCell(7).setCellValue("SHIPMENT CODE");
	}
	
	private void setBody(Sheet sheet, List<Purchase> purchase) {

		int rowNumber=1;
		for(Purchase p:purchase) {
			Row row=sheet.createRow(rowNumber++);
			row.createCell(0).setCellValue(p.getPurId().toString());
			row.createCell(1).setCellValue(p.getOrderCode());
		    row.createCell(2).setCellValue(p.getRefNum());
			row.createCell(3).setCellValue(p.getQualityCheck());
			row.createCell(4).setCellValue(p.getStatus());
			row.createCell(5).setCellValue(p.getNote());
			row.createCell(6).setCellValue(p.getVendor().getWhNum());
			row.createCell(7).setCellValue(p.getShipmentCode
					().getShipmentCode());
		}
		
	}

	}
