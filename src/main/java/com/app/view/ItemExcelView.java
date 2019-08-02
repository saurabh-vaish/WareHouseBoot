package com.app.view;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import com.app.model.Item;

public class ItemExcelView  extends AbstractXlsView{

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
	List<Item>	item = (List<Item>) model.get("list");
		
	response.addHeader("CONTENT-DISPOSITION","attachment;filename=Item.xls");
		
	Sheet sheet = workbook.createSheet();	
	
	setHead(sheet);
	
	setBody(sheet,item);
	
	
	}

	private void setHead(Sheet sheet) {
		
		Row row=sheet.createRow(0);
		
		row.createCell(0).setCellValue("Id");
		row.createCell(1).setCellValue("Code");
		row.createCell(2).setCellValue("Dimenstions");
		row.createCell(3).setCellValue("Cost");
		row.createCell(4).setCellValue("Currency");
		row.createCell(5).setCellValue("Uom");
		row.createCell(6).setCellValue("OrderMethod");
		row.createCell(7).setCellValue("Vendor");
		row.createCell(8).setCellValue("Customer");
		row.createCell(9).setCellValue("Note");
		
	}

	private void setBody(Sheet sheet, List<Item> item) {
		
		int rownum=1;
		
		for(Item i:item)
		{
			Row row=sheet.createRow(rownum++);
			
			row.createCell(0).setCellValue(i.getItemId());
			row.createCell(1).setCellValue(i.getItemCode());
			row.createCell(2).setCellValue("L: "+i.getItemLength()+" W: "+i.getItemWidth()+" H: "+i.getItemHeight());
			row.createCell(3).setCellValue(i.getBaseCost());
			row.createCell(4).setCellValue(i.getBaseCurr());
			row.createCell(5).setCellValue(i.getUom().getUomCode());
			row.createCell(6).setCellValue(i.getOrder().getOrderMode());
			row.createCell(7).setCellValue(i.getUser1().getWhCode());
			row.createCell(8).setCellValue(i.getUser2().getWhCode());
			row.createCell(9).setCellValue(i.getNote());
			
		}
		
	}

}
