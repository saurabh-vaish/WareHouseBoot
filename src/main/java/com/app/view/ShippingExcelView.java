package com.app.view;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.app.model.Shipping;


public class ShippingExcelView extends AbstractXlsxView{

	@SuppressWarnings("unchecked")
	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		response.setHeader("Content-Disposition", "attachment;filename=SaleOrder.xls");

		
		List<Shipping> shipping=(List<Shipping>) model.get("shipping");
	
		Sheet sheet=workbook.createSheet("Shippings");
		
		setHead(sheet);
		setBody(sheet,shipping);
		
	}

	private void setHead(Sheet sheet) {

		Row row=sheet.createRow(0);
		row.createCell(0).setCellValue("ID");
		row.createCell(1).setCellValue("CODE");
		row.createCell(2).setCellValue("SHIPPING REF NUM");
		row.createCell(3).setCellValue("COURIER REF NUM");
		row.createCell(4).setCellValue("COURIER CONTACT NUM");
		row.createCell(5).setCellValue("SALE ORDER CODE");
		row.createCell(6).setCellValue("BILL ADDRESS");
		row.createCell(7).setCellValue("SHIP ADDRESS");
		row.createCell(8).setCellValue("NOTE");
	}
	
	private void setBody(Sheet sheet, List<Shipping> saleOrder) {

		int rowNumber=1;
		for(Shipping s:saleOrder) {
			Row row=sheet.createRow(rowNumber++);
			row.createCell(0).setCellValue(s.getShipId().toString());
			row.createCell(1).setCellValue(s.getShipCode());
		    row.createCell(2).setCellValue(s.getShipRefNum());
			row.createCell(3).setCellValue(s.getCourRefNum());
			row.createCell(4).setCellValue(s.getCouContdtls());
			row.createCell(5).setCellValue(s.getSaleOrder().getOrderCode());
			row.createCell(6).setCellValue(s.getBillAddr());
			row.createCell(6).setCellValue(s.getShipAddr());
			row.createCell(7).setCellValue(s.getShipDesc());
		}
		
	}

	}
