package com.app.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.app.model.Purchase;
import com.app.model.Uom;
import com.app.service.IUomService;
import com.app.util.UomUtil;
import com.app.validator.UomValidator;
import com.app.view.UomExcelView;
import com.app.view.UomPdfView;

@Controller
@RequestMapping("/uom")
public class UomController {

	@Autowired
	private IUomService service;

	@Autowired
	private ServletContext context;
	
	@Autowired
	private UomUtil util;
	
	
	@Autowired
	private UomValidator validator;
	
	
		//1-Register
		@RequestMapping("/register")
		public String showUomRegisterPage(ModelMap map)
		{
			// form backing object
			map.addAttribute("uom",new Uom());
			return "UomRegister";
		}
		
		

		// * ajax  method for checking duplicate codes
		@RequestMapping("/check")
		public @ResponseBody String checkCode(@RequestParam("code")String code) //@ResponseBody for ajax call
		{
			String msg="";

			List<Uom> uom = service.getAllUoms();
			for(Uom u : uom)
			{
				if(u.getUomCode().equals(code))
				{
					System.out.println("yes");
					msg="Uom Code already exits .";
					break;
				}
			}

			System.out.println(msg);
			return msg;

		}

		
		
		//2-Save
		@RequestMapping(value="/save",method=RequestMethod.POST)
		public String saveUom(@ModelAttribute Uom uom,Errors errors,ModelMap map)
		{
			validator.validate(uom, errors);
			
			if(!errors.hasErrors()) 
			{
				Integer id=service.saveUom(uom);
				map.addAttribute("msg","Uom '"+id+"' saved successfully");
				map.addAttribute("uomId",id);
				//clear form backing object
				map.addAttribute("uom",new Uom());
			}
			else
			{
				map.addAttribute("msg","Enter Valid Details");
				
			}			
			return "UomRegister";
		}
		
		//3-show all
		@RequestMapping("/all")
		public String showAllUom(ModelMap map)
		{
			List<Uom> ob=service.getAllUoms();
			map.addAttribute("list",ob);
			return "UomData";
		}
		
		//. show one record
		@RequestMapping("/view")
		public String showOneRecord(@RequestParam Integer id,ModelMap map)
		{
			map.addAttribute("uom",service.getUomId(id));
			return "UomView";
		}

		
		//4-Delete
		@RequestMapping("/delete")
		public String deleteById(@RequestParam("id")Integer id,ModelMap map)
		{
			service.deleteUom(id);
			map.addAttribute("list",service.getAllUoms());
			map.addAttribute("msg", "uom "+id+" Deleted Successfully");
			return "UomData";
			
		}

		
		// 6 -  Update 
		@RequestMapping(value="/update",method=RequestMethod.POST)
		public String updateUom(@ModelAttribute Uom uom,Errors errors,ModelMap map)
		{
					
			validator.validate(uom, errors);
			System.out.println(uom);
			if(!errors.hasErrors())
			{
				//call service
				service.updateUom(uom);
				map.addAttribute("msg", "uom '"+uom.getId()+"' update successfully ");
			}
			else
			{
				map.addAttribute("msg","Enter Valid Details");
			}
					
			map.addAttribute("uom",service.getUomId(uom.getId())); // data for uom view page
			return "UomView";
		}

		//7. export to excel
		@RequestMapping("/excel")
		public ModelAndView getExcel(@RequestParam(value="id",required=false,defaultValue="0")Integer id) // here required is false bcs we are using same method for both
		//when we are sending id in url and when not also it it will work for both
		{
			//creating ModelAndView for sending data to excel view
			ModelAndView m = new ModelAndView();
			//setting view 
			m.setView(new UomExcelView());
			
			if(id==0)//means no id parameter with path
			{
				m.addObject("list",service.getAllUoms());  // so getting all rows i.e. export all
			}
			else // means id present with path
			{
				 // getting data according to id
				m.addObject("list", Collections.singletonList(service.getUomId(id)));  // converting single row object to list
			}
				  
			return m;
			
		}
		
		

		// 8 . export to pdf
		@RequestMapping("/pdf")
		public ModelAndView getPdf(@RequestParam(value="id",required=false,defaultValue="0")Integer id,ModelMap map) // here required is false bcs we are using same method for both
		//when we are sending id in url and when not also it it will work for both
		{
			//creating ModelAndView for sending data to excel view
			ModelAndView m = new ModelAndView();
			//setting view 
			m.setView(new UomPdfView());
			
			if(id==0) //means no id parameter with path
			{
				m.addObject("list", service.getAllUoms());  // so getting all rows i.e. export all
			}
			else // means id present with path
			{
				// getting data according to id
				m.addObject("list",Collections.singletonList(service.getUomId(id)) );
			}
			
			return m;
		}
		
		// 9 pie and bar chart
		@RequestMapping("/chart")
		public String showCharts()
		{
			String path = context.getRealPath("/");
			List<Object[]> data = service.getUomCountByUomType();
			util.ganeratePie(path, data);
			util.ganerateBar(path,data);
			return "UomReports";
		}
		
		
				
		
		
}
