package com.app.view;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.app.model.ShipmentType;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class ShipmentTypePdfView extends AbstractPdfView  {

	@SuppressWarnings("unchecked")
	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		// change file name and make it dowloadable
		response.addHeader("Content-Disposition","attachment;filename=Shipment.pdf" );
		
		// data 
		List<ShipmentType> list = (List<ShipmentType>) model.get("list");
		
		// add a paragraph
		Paragraph p = new Paragraph("Welcome to Shipment Data ");
		
		//adding to document
		document.add(p);
		
		// add table for data 
		PdfPTable table = new PdfPTable(6);
		
		table.addCell("Id");
		table.addCell("Mode");
		table.addCell("Code");
		table.addCell("Enable");
		table.addCell("Grade");
		table.addCell("Note");
		
		for(ShipmentType s : list)
		{
			table.addCell(s.getShipmentId().toString());
			table.addCell(s.getShipmentMode());
			table.addCell(s.getShipmentCode());
			table.addCell(s.getEnableShipment());
			table.addCell(s.getShipmentGrade());
			table.addCell(s.getNote());
		}
		
		// add table to document
		document.add(table);
		
		
		//adding date 
		document.add(new Paragraph(new Date().toString()));
	}

	

}
