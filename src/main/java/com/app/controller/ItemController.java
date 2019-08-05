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

import com.app.model.Item;
import com.app.model.OrderMethod;
import com.app.service.IItemService;
import com.app.service.IOrderMethodService;
import com.app.service.IUomService;
import com.app.service.IWhUserService;
import com.app.validator.ItemValidator;
import com.app.view.ItemExcelView;
import com.app.view.ItemPdfView;

import lombok.extern.log4j.Log4j2;



@Log4j2
@Controller
@RequestMapping("/item")
public class ItemController {

	@Autowired
	private IItemService service;

	@Autowired
	private IUomService uomService;

	@Autowired
	private IOrderMethodService orderService;

	@Autowired
	private IWhUserService whservice;
	
	@Autowired
	private ItemValidator validator;
	
	
	@Autowired
	private ServletContext context;
	
	
	
	//1. regsiter page

	@RequestMapping("/register")
	public  String showRegister(ModelMap map)
	{
		/**Module Integration start**/

		map.addAttribute("listuom",uomService.getAllUoms());
		map.addAttribute("listorder",orderService.getAllOrderMethods());
		map.addAttribute("listvendor",whservice.getAllWhUserByType("Vendor"));
		map.addAttribute("listcustomer",whservice.getAllWhUserByType("Customer"));
		/**Module Integration ends**/

		map.addAttribute("item",new Item()); // form backing object
		return "ItemRegister";
	}
	
	
	// * ajax  method for checking duplicate codes
	@RequestMapping("/check")
	public @ResponseBody String checkOrderCode(@RequestParam("code")String code) //@ResponseBody for ajax call
	{
		String msg="";

		List<Item> item = service.getAllItems();
		for(Item i:item)
		{
			if(i.getItemCode().equals(code))
			{
				System.out.println("yes");
				msg="Item Code already exits .";
				break;
			}
		}
	
		log.info(msg);
		return msg;
	}	


	// 2. save 
	@RequestMapping("/save")
	public String saveItem(@ModelAttribute Item item,Errors errors,ModelMap map,RedirectAttributes attr)
	{
		validator.validate(item, errors);
		
		if(!errors.hasErrors()) // no error
		{
			Integer id = service.saveItem(item);
			attr.addFlashAttribute("msg","Item '"+id+"' saved successfully");
			
			log.info("Item Registred Successfully");
			return "redirect:register";
		}
		else
		{
			map.addAttribute("listuom",uomService.getAllUoms());
			map.addAttribute("listorder",orderService.getAllOrderMethods());
			map.addAttribute("listvendor",whservice.getAllWhUserByType("Vendor"));
			map.addAttribute("listcustomer",whservice.getAllWhUserByType("Customer"));
			
			map.addAttribute("emsg","enter valid details");

			log.info("Item Registration Failed !");
		}

		return "ItemRegister";
	}


	
	// 3. all
	@RequestMapping("/all")
	public String displayItems(ModelMap map)
	{
		map.addAttribute("list", service.getAllItems());
		return "ItemData";
	}
	
	

	//4 . show one record
	@RequestMapping("/view/{id}")
	public String showOneRecord(@PathVariable Integer id,ModelMap map)
	{
		map.addAttribute("listuom",uomService.getAllUoms());
		map.addAttribute("item",service.getItemById(id));
		map.addAttribute("listorder",orderService.getAllOrderMethods());
		map.addAttribute("listvendor",whservice.getAllWhUserByType("Vendor"));
		map.addAttribute("listcustomer",whservice.getAllWhUserByType("Customer"));
		
		return "ItemView";
	}
	
	
	
	//5 -Delete
	@RequestMapping("/delete/{id}")
	public String deleteById(@PathVariable Integer id,ModelMap map)
	{
		service.deleteItem(id);
		map.addAttribute("list",service.getAllItems());
		map.addAttribute("msg", "item "+id+" Deleted Successfully");
		
		log.info("Item deleted successfully");
		return "ItemData";

	}		
	
	
	
	// 6 -  Update 
	@RequestMapping(value="/update",method=RequestMethod.POST)
	public String updateItem(@ModelAttribute Item item,Errors errors,ModelMap map)
	{

		validator.validate(item, errors);

		if(!errors.hasErrors())
		{
			//call service
			service.updateItem(item);
			map.addAttribute("msg", "item '"+item.getItemId()+"' update successfully ");
			
			log.info("Item Updated Successfully");
		}
		else
		{
			map.addAttribute("emsg","Enter Valid Details");

			log.info("Item Updatation Failed");
		}

		map.addAttribute("item",service.getItemById(item.getItemId())); // data for item view page
		map.addAttribute("listuom",uomService.getAllUoms());
		map.addAttribute("listorder",orderService.getAllOrderMethods());
		map.addAttribute("listvendor",whservice.getAllWhUserByType("Vendor"));
		map.addAttribute("listcustomer",whservice.getAllWhUserByType("Customer"));
		
		return "ItemView";


	}

	
	
	// 7. export to excel 
	@RequestMapping("/excel")
	public ModelAndView showExcel(@RequestParam(value="id",required=false,defaultValue="0")Integer id)
	{
		ModelAndView m = new ModelAndView();
		m.setView(new ItemExcelView());
		
		if(id==0)
		{
			m.addObject("list",service.getAllItems());
		}
		else
		{
			m.addObject("list", Collections.singletonList(service.getItemById(id)));
		}
		
		log.info("Excel Report Generated");
		
		return m;
	}
	
	
	// 8 . export to pdf
	@RequestMapping("/pdf")
	public ModelAndView getPdf(@RequestParam(value="id",required=false,defaultValue="0")Integer id) // here required is false bcs we are using same method for both
	//when we are sending id in url and when not also it it will work for both
	{
		//creating ModelAndView for sending data to excel view
		ModelAndView m = new ModelAndView();
		//setting view 
		m.setView(new ItemPdfView());
		
		if(id==0) //means no id parameter with path
		{
			m.addObject("list", service.getAllItems());  // so getting all rows i.e. export all
		}
		else // means id present with path
		{
			// getting data according to id
			m.addObject("list",Collections.singletonList(service.getItemById(id)) );
		}
		
		log.info("Pdf Report Generated");
		return m;
	}
	
	

}
