package com.app.view;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.app.model.OrderMethod;
import com.app.model.Uom;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class OrderMethodPdfView extends AbstractPdfView implements View {

	@SuppressWarnings("unchecked")
	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// make it downloadable and change name
		response.addHeader("Content-Disposition","attachment;filename=OrderMethod.pdf" );
		
		
		//get data
		List<OrderMethod> list = (List<OrderMethod>) model.get("list");
		
		//creating paragraph
		Paragraph p = new Paragraph("OrderMethod Details");
		
		document.add(p);
		
		//creating table
		PdfPTable table = new PdfPTable(6);
		
		table.addCell("Id");
		table.addCell("Mode");
		table.addCell("Code");
		table.addCell("Execution Type");
		table.addCell("Order Accept");
		table.addCell("Note");
		
		for(OrderMethod om : list )
		{
			table.addCell(om.getOrderId().toString());
			table.addCell(om.getOrderMode());
			table.addCell(om.getOrderCode());
			table.addCell(om.getExeType());
			table.addCell(om.getOrderAccpet()!=null?om.getOrderAccpet().toString():"[]");
			table.addCell(om.getOrderNote());
		}
		
		// add table to document
		document.add(table);
		
		
		//adding date 
		document.add(new Paragraph(new Date().toString()));
	}


}
