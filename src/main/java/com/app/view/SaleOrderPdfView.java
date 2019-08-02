package com.app.view;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.app.model.SaleOrder;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class SaleOrderPdfView extends AbstractPdfView{

	@SuppressWarnings("unchecked")
	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		//set file name
		response.setHeader("Content-Disposition", "attachment;filename=Purchases.pdf");


		// read date from model fdata
		List<SaleOrder> saleOrder=(List<SaleOrder>) model.get("saleOrder");

		//create any element
		document.add(new Paragraph("ALL PURCHASES"));

		PdfPTable pdfPTable=new PdfPTable(8);
		pdfPTable.addCell("ID");
		pdfPTable.addCell("CODE");
		pdfPTable.addCell("REF NUM");
		pdfPTable.addCell("STOCK MODE");
		pdfPTable.addCell("STOCK SOURCE");
		pdfPTable.addCell("STATUS");
		pdfPTable.addCell("CUSTOMER");
		pdfPTable.addCell("SHIPMENT CODE");
		pdfPTable.addCell("NOTE");

		for(SaleOrder p:saleOrder){
			pdfPTable.addCell(p.getSaleId().toString());
			pdfPTable.addCell(p.getOrderCode());
			pdfPTable.addCell(p.getRefNum());
			pdfPTable.addCell(p.getStockMode());
			pdfPTable.addCell(p.getStockSource());
			pdfPTable.addCell(p.getStatus());
			pdfPTable.addCell(p.getCustomer().getWhCode());
			pdfPTable.addCell(p.getShipmentCode().getShipmentCode());
			pdfPTable.addCell(p.getNote());
		}
		document.add(pdfPTable);
		document.add(new Paragraph(new Date().toString()));

	}

}
