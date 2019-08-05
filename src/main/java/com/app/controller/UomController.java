package com.app.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.model.Uom;
import com.app.service.IUomService;
import com.app.util.UomUtil;
import com.app.validator.UomValidator;
import com.app.view.UomExcelView;
import com.app.view.UomPdfView;

import lombok.extern.log4j.Log4j2;

@Log4j2
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

			log.info(msg);
			return msg;

		}

		
		
		//2-Save
		@RequestMapping(value="/save",method=RequestMethod.POST)
		public String saveUom(@ModelAttribute Uom uom,Errors errors,ModelMap map,RedirectAttributes attr)
		{
			validator.validate(uom, errors);
			
			if(!errors.hasErrors()) 
			{
				Integer id=service.saveUom(uom);
				attr.addFlashAttribute("msg","Uom '"+id+"' saved successfully");
				attr.addFlashAttribute("uomId",id);

				log.info("uom registred successfully");
				
				return "redirect:register";
			}
			else
			{
				map.addAttribute("emsg","Enter Valid Details");
				
				log.info("uom registration failed , error occured");
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
		@RequestMapping("/view/{id}")
		public String showOneRecord(@PathVariable Integer id,ModelMap map)
		{
			map.addAttribute("uom",service.getUomId(id));
			return "UomView";
		}

		
		//4-Delete
		@RequestMapping("/delete/{id}")
		public String deleteById(@PathVariable Integer id,RedirectAttributes map)
		{
			service.deleteUom(id);
			map.addFlashAttribute("msg", "uom "+id+" Deleted Successfully");

			log.info("uom deleted successfully");
			
			return "redirect:all";
			
		}

		
		// 6 -  Update 
		@RequestMapping(value="/update",method=RequestMethod.POST)
		public String updateUom(@ModelAttribute Uom uom,Errors errors,ModelMap map)
		{
					
			validator.validate(uom, errors);

			 if(!errors.hasErrors())
			{
				//call service
				service.updateUom(uom);
				map.addAttribute("msg", "uom '"+uom.getId()+"' update successfully ");
				
				log.info("uom updated successfully");
			}
			else
			{
				map.addAttribute("emsg","Enter Valid Details");
				
				log.info("uom can't be updated ,error occured");
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
		
			log.info("Excel Report Generated");
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
			
			log.info("Pdf Report Generated");
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
			
			log.info("Chart Generated");
			return "UomReports";
		}
		
		
				
		
		
}
