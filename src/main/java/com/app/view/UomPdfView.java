package com.app.view;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.app.model.ShipmentType;
import com.app.model.Uom;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class UomPdfView extends AbstractPdfView  {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		// change file name and make it dowloadable
		response.addHeader("Content-Disposition","attachment;filename=Uom.pdf" );
		
		// data 
		List<Uom> list = (List<Uom>) model.get("list");
		
		// add a paragraph
		Paragraph p = new Paragraph("Welcome to Uom Data ");
		
		//adding to document
		document.add(p);
		
		// add table for data 
		PdfPTable table = new PdfPTable(6);
		
		table.addCell("Id");
		table.addCell("Type");
		table.addCell("Code");
		table.addCell("Enable");
		table.addCell("Meta Code");
		table.addCell("Size");
		table.addCell("Note");
		
		for(Uom u : list )
		{
			table.addCell(u.getId().toString());
			table.addCell(u.getUomType());
			table.addCell(u.getUomCode());
			table.addCell(u.getEnableUom());
			table.addCell(u.getMetaCode());
			table.addCell(u.getAdjSize());
			table.addCell(u.getNote());
		}
		
		// add table to document
		document.add(table);
		
		
		//adding date 
		document.add(new Paragraph(new Date().toString()));
	}

	

}
