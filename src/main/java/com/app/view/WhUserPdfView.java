package com.app.view;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.app.model.Uom;
import com.app.model.WhUser;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class WhUserPdfView extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		// change file name and make it dowloadable
		response.addHeader("Content-Disposition","attachment;filename=WhUser.pdf" );

		// data 
		List<WhUser> list = (List<WhUser>) model.get("list");

		// add a paragraph
		Paragraph p = new Paragraph("Welcome to WhUser Data ");

		//adding to document
		document.add(p);

		// add table for data 
		PdfPTable table = new PdfPTable(8);

		table.addCell("Id");
		table.addCell("Type");
		table.addCell("Code");
		table.addCell("For");
		table.addCell("Email");
		table.addCell("Contact");
		table.addCell("Id type");
		table.addCell("Id Number");

		for(WhUser w : list )
		{
			table.addCell(w.getWhId().toString());
			table.addCell(w.getWhIdType());
			table.addCell(w.getWhCode());
			table.addCell(w.getWhFor());
			table.addCell(w.getWhEmail());
			table.addCell(w.getWhContact());
			table.addCell(w.getWhIdType());
			table.addCell(w.getWhNum());
			
			
		}

		// add table to document
		document.add(table);


		//adding date 
		document.add(new Paragraph(new Date().toString()));
	}



}
