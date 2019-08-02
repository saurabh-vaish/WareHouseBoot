package com.app.controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.app.model.Document;
import com.app.service.IDocumentService;

@Controller
@RequestMapping("/docs")
public class DocumentController {

	@Autowired
	private IDocumentService service;

	// get file upload page
	@RequestMapping("/get")
	public String showUploadPage(ModelMap map)
	{
		map.addAttribute("list",service.getFileIdAndName());
		return "document";
	}

	// upload file
	@RequestMapping(value="/upload",method=POST)
	public String uploadFile(@RequestParam CommonsMultipartFile fileData)        // var name should be same as in form 
	{																		//since it is file type so CommonsMultipartFile used
	
		if(fileData!=null)
		{
			Document d =  new Document();
			
			d.setFileName(fileData.getOriginalFilename());  // getting file name
			
			d.setFileData(fileData.getBytes());   // setting file data
			
			service.saveDocument(d);
			
		}
		return "redirect:get";
	}

	
	// download files 
	@RequestMapping("/download")
	public void downloadFile(@RequestParam Integer fid,HttpServletResponse resp) // void bcs it is not showing any page 
	{
		// getting data based on Id
		Document doc = service.getDocumentById(fid);
		
		// constructing Http Response to download data
		resp.addHeader("Content-Disposition", "attachment:filename="+doc.getFileName());   // if want to display not download then comment it
		
		
		try {
			// copy data to response using FileCopyUtils 
			
			FileCopyUtils.copy(doc.getFileData(), resp.getOutputStream());  // (in:byte[] ,out : OutputStream)
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	



}
