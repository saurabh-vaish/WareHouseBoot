package com.app.view;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.app.model.Shipping;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class ShippingPdfView extends AbstractPdfView{

	@SuppressWarnings("unchecked")
	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		//set file name
		response.setHeader("Content-Disposition", "attachment;filename=Shippings.pdf");


		// read date from model fdata
		List<Shipping> shipping=(List<Shipping>) model.get("shipping");

		//create any element
		document.add(new Paragraph("ALL PURCHASES"));

		PdfPTable pdfPTable=new PdfPTable(8);
		pdfPTable.addCell("ID");
		pdfPTable.addCell("CODE");
		pdfPTable.addCell("SHIPPING REF NUM");
		pdfPTable.addCell("COURIER REF NUM");
		pdfPTable.addCell("COURIER CONTACT NUM");
		pdfPTable.addCell("SALE ORDER CODE");
		pdfPTable.addCell("BILL ADDRESS");
		pdfPTable.addCell("SHIP ADDRESS");
		pdfPTable.addCell("NOTE");

		for(Shipping s:shipping){
			pdfPTable.addCell(s.getShipId().toString());
			pdfPTable.addCell(s.getShipCode());
			pdfPTable.addCell(s.getShipRefNum());
			pdfPTable.addCell(s.getCourRefNum());
			pdfPTable.addCell(s.getCouContdtls());
			pdfPTable.addCell(s.getSaleOrder().getOrderCode());
			pdfPTable.addCell(s.getBillAddr());
			pdfPTable.addCell(s.getShipAddr());
			pdfPTable.addCell(s.getShipDesc());
		}
		document.add(pdfPTable);
		document.add(new Paragraph(new Date().toString()));

	}

}
