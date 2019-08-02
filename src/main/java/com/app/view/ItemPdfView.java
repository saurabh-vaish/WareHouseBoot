package com.app.view;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.app.model.Item;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class ItemPdfView  extends AbstractPdfView{

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		

		// change file name and make it dowloadable
		response.addHeader("Content-Disposition","attachment;filename=Item.pdf" );
		
		// data 
		List<Item> list = (List<Item>) model.get("list");
		
		// add a paragraph
		Paragraph p = new Paragraph("Welcome to Item Data ");
		
		//adding to document
		document.add(p);
		
		// add table for data 
		PdfPTable table = new PdfPTable(10);
		
		table.addCell("Id");
		table.addCell("Code");
		table.addCell("Dimenstions");
		table.addCell("Cost");
		table.addCell("Currency");
		table.addCell("Uom");
		table.addCell("OrderMethod");
		table.addCell("Vendor");
		table.addCell("Customer");
		table.addCell("Note");
		
		
		for(Item i : list )
		{
			table.addCell(i.getItemId().toString());
			table.addCell(i.getItemCode());
			table.addCell("L: "+i.getItemLength()+" W: "+i.getItemWidth()+" H: "+i.getItemHeight());
			table.addCell(i.getBaseCost());
			table.addCell(i.getBaseCurr());
			table.addCell(i.getUom().getUomCode());
			table.addCell(i.getOrder().getOrderMode());
			table.addCell(i.getUser1().getWhCode());
			table.addCell(i.getUser2().getWhCode());
			table.addCell(i.getNote());
			
		}
		
		// add table to document
		document.add(table);
		
		
		//adding date 
		document.add(new Paragraph(new Date().toString()));
	}
}