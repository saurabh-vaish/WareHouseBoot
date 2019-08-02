package com.app.view;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.app.model.GoodRecieveNote;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class GRNPdfView extends AbstractPdfView{

	@SuppressWarnings("unchecked")
	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		//set file name
		response.setHeader("Content-Disposition", "attachment;filename=GRN.pdf");

		//loadind data from model
		List<GoodRecieveNote> goodRecieveNote=(List<GoodRecieveNote>) model.get("goodRecieveNote");
		//create any element
		document.add(new Paragraph("GRN Details"));

		// create table
		PdfPTable pdfPTable=new PdfPTable(5);
		pdfPTable.addCell("ID");
		pdfPTable.addCell("CODE");
		pdfPTable.addCell("TYPE");
		pdfPTable.addCell("PURCHASE ORDER CODE");
		pdfPTable.addCell("NOTE");

		for(GoodRecieveNote g :goodRecieveNote) {
			pdfPTable.addCell(g.getGrnId().toString());
			pdfPTable.addCell(g.getGrnCode());
			pdfPTable.addCell(g.getGrnType());
			pdfPTable.addCell(g.getPurchase().getOrderCode());
			pdfPTable.addCell(g.getGrnDesc());

		}
		document.add(pdfPTable);
		
		document.add(new Paragraph(new Date().toString()));

	}
}

