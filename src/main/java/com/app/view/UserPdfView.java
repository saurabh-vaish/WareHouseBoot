package com.app.view;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.app.model.User;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class UserPdfView extends AbstractPdfView {

	@SuppressWarnings("unchecked")
	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		response.addHeader("Content-Disposition", "attachment;filename=UserPdf.pdf");
		
		List<User> list = (List<User>) model.get("list");
		
		Paragraph p = new Paragraph("Welcome To User");
		document.add(p);
		
		PdfPTable pdfPTable = new PdfPTable(6);
		pdfPTable.addCell("ID");
		pdfPTable.addCell("NAME");
		pdfPTable.addCell("GENDER");
		pdfPTable.addCell("EMAIL");
		pdfPTable.addCell("MOBILE");
		pdfPTable.addCell("ROLES");
		
		for(User u : list)
		{
			pdfPTable.addCell(u.getUserId().toString());
			pdfPTable.addCell(u.getUserName());
			pdfPTable.addCell(u.getGender());
			pdfPTable.addCell(u.getUserEmail());
			pdfPTable.addCell(u.getUserMobile());
			pdfPTable.addCell(u.getRoles()!=null?u.getRoles().toString():"[]");

		}
		
		document.add(pdfPTable);
		
		document.add(new Paragraph(new Date().toString()));
	}

}
