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

import com.app.model.ShipmentType;
import com.app.service.IShipmentTypeService;
import com.app.util.ShipmentTypeUtil;
import com.app.validator.ShipmentTypeValidator;
import com.app.view.ShipmentTypeExcelView;
import com.app.view.ShipmentTypePdfView;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/shipment")
public class ShipmentTypeController {
	
	@Autowired
	private IShipmentTypeService service;

	// for getting context used in charts
	@Autowired
	private ServletContext context;

	
	@Autowired
	private ShipmentTypeUtil util;
	
	
	// for validation
	@Autowired
	private ShipmentTypeValidator validator;
	
	
	
	
	//1- Register
	@RequestMapping("/register")
	public String showShipmentRegisterPage(ModelMap map)
	{
		//backing of form
		map.addAttribute("shipmentType", new ShipmentType());
		return "ShipmentTypeRegister";
	}
	
	

	// * ajax  method for checking duplicate 
		@RequestMapping("/check")
		public @ResponseBody String checkShipment(@RequestParam("shipmentCode")String shipmentCode) //@ResponseBody for ajax call
		{
			String msg="";
			
			  List<ShipmentType> ship = service.getAllShipmentTypes();
			  for(ShipmentType s : ship)
			  {
				  if(s.getShipmentCode().equals(shipmentCode))
				  {
					  System.out.println("yes");
					  msg="Shipment Code already exits .";
					  break;
				  }
			  }
			  
			log.info(msg);
			
			return msg;
		}	
	
	
		
	//2- Save
	@RequestMapping(value="/save",method=RequestMethod.POST)
	public String saveShipmentType(@ModelAttribute ShipmentType shipmentType,Errors errors,ModelMap map,RedirectAttributes attr)  // Errors must be after @MA
	{
		
		log.info("entered into shipment register");

		validator.validate(shipmentType, errors);
		
		if(!errors.hasErrors()) // if no error
		{
			Integer id=service.saveShipmentType(shipmentType);
			String message="Shipment '"+id+"'saved successfully";
			attr.addFlashAttribute("msg",message);
			attr.addFlashAttribute("shipmentId",id);

			log.info("Shipment Registered Successfully");

			return "redirect:register";
		}
		else
		{
			map.addAttribute("emsg","enter valid details ");			

			log.info("Shipment Registeration Failed !! Error Occurs in forms");
			
			return "ShipmentTypeRegister";
		}
			
		
	}
	
	
	//3- Show ALL
	@RequestMapping("/all")
	public String showAllShipmentType(ModelMap map)
	{
		List<ShipmentType> ob=service.getAllShipmentTypes();
		map.addAttribute("list",ob);
		return "ShipmentTypeData";
	}
	

	
	// show one record
	@RequestMapping("/view/{id}")
	public String showOneRecord(@PathVariable Integer id,ModelMap map)
	{
		map.addAttribute("shipment",service.getShipmentTypeById(id));
		return "ShipmentTypeView";
	}
	
	
	
	//4- Delete
	@RequestMapping("/delete/{id}")
	public String deleteById(@PathVariable Integer id,RedirectAttributes map)
	{
		service.deleteShipmentType(id);
		map.addFlashAttribute("msg", id+"Deleted Successfully");
		
		log.info("Shipment Deleted Successfully !");
		
		return "ShipmentTypeData";
	}
	
	
	
	// 6 -  Update 
	@RequestMapping(value="/update",method=RequestMethod.POST)
	public String updateShipment(@ModelAttribute ShipmentType shipmentType,Errors errors,ModelMap map)
	{
		
		  validator.validate(shipmentType, errors);
		
		  System.out.println(shipmentType);
		  
		  if(!errors.hasErrors()) 
		  {
			  //call service
			  service.updateShipmentType(shipmentType.getShipmentId(),shipmentType);
			  map.addAttribute("msg","Shipment Type '"+shipmentType.getShipmentId()+"' updated successfully ");
		
			  log.info("Shipment Updated Successfully !");
		  }
		  else {
			  map.addAttribute("msg","Enter Valid Details");
		}
		 
		
		map.addAttribute("shipment",service.getShipmentTypeById(shipmentType.getShipmentId())); // data for view page
		
		return "ShipmentTypeView";
		
		
	}
	
	
	//7. export to excel
	@RequestMapping("/excel")
	public ModelAndView getExcel(@RequestParam(value="id",required=false,defaultValue="0")Integer id) // here required is false bcs we are using same method for both
	//when we are sending id in url and when not also it it will work for both
	{
		//creating ModelAndView for sending data to excel view
		ModelAndView m = new ModelAndView();
		//setting view 
		m.setView(new ShipmentTypeExcelView());
		
		if(id==0) //means no id parameter with path
		{
			m.addObject("list", service.getAllShipmentTypes()); // so getting all rows i.e. export all
		}
		else // means id present with path
		{
			// getting data according to id
			m.addObject("list", Collections.singletonList(service.getShipmentTypeById(id)));  // converting single row object to list
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
		m.setView(new ShipmentTypePdfView());
		
		if(id==0) //means no id parameter with path
		{
			m.addObject("list", service.getAllShipmentTypes());  // so getting all rows i.e. export all
		}
		else // means id present with path
		{
			// getting data according to id
			m.addObject("list",Collections.singletonList(service.getShipmentTypeById(id)) );
		}
		
		log.info("Pdf Report Generated");

		return m;
	}
	
	
	// 9. pie and bar chart 
	@RequestMapping("/chart")
	public String showCharts()
	{
		String path=context.getRealPath("/"); // runtime path of server
		System.out.println(path);
		List<Object[]> data = service.getShipmentCountByMode();
		util.ganeratePie(path, data);
		util.ganerateBar(path, data);

		log.info("Charts Generated");
		
		return "ShipmentTypeReports";
	}
	
	
	
}
