package com.app.controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST; // static import

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.model.OrderMethod;
import com.app.service.IOrderMethodService;
import com.app.util.OrderMethodUtil;
import com.app.validator.OrderMethodValidator;
import com.app.view.OrderMethodExcelView;
import com.app.view.OrderMethodPdfView;

import lombok.extern.log4j.Log4j2;



@Controller
@RequestMapping("/order")
@Log4j2
public class OrderMethodController {

	@Autowired
	private IOrderMethodService service;
	
	@Autowired
	private ServletContext context;

	@Autowired
	private OrderMethodUtil util;
	
	
	@Autowired
	private OrderMethodValidator validator;
	
	
	// show register page
	@RequestMapping("/register")
	public String getRegisterPage(ModelMap map)
	{
		map.addAttribute("orderMethod",new OrderMethod()); // form backing object
		return "OrderMethodRegister";
	}
	
	

	// * ajax  method for checking duplicate orders
		@RequestMapping("/check")
		public @ResponseBody String checkOrderCode(@RequestParam("order")String order) //@ResponseBody for ajax call
		{
			String msg="";
			
			  List<OrderMethod> om = service.getAllOrderMethods();
			  for(OrderMethod o : om)
			  {
				  if(o.getOrderCode().equals(order))
				  {
					  System.out.println("yes");
					  msg="Order Code already exits .";
					  break;
				  }
			  }
			 
			  log.info(msg);
			  return msg;
		}	
	

	// save order method
	@RequestMapping(value="/save",method=POST)
	public String saveOrderMethod(@ModelAttribute OrderMethod orderMethod,Errors errors,ModelMap map,RedirectAttributes attr)
	{
		validator.validate(orderMethod, errors);
		
		if(!errors.hasErrors())  // no errors
		{
			Integer id = service.saveOrderMethod(orderMethod);
			attr.addFlashAttribute("msg"," Order Method '"+id+"' registered successfully" );  // save data and msg sending to ui
			attr.addFlashAttribute("orderId",id );  // save data and id sending to ui
			
			log.info("OrderMethod Registred Successfully !");
			return "redirect:register";
		}
		else
		{
			map.addAttribute("emsg", "enter valid details");

			log.info("Failed to Register OrderMethod ! ");

			return "OrderMethodRegister";
		}
	}


	// show all data
	@RequestMapping("/all")
	public String getAllData(ModelMap map)
	{
		//adding data to list to send to ui
		map.addAttribute("list", service.getAllOrderMethods());
		return "OrderMethodData";				
	}

	
	// show one record
	@RequestMapping("/view/{id}")
	public String showOneRecord(@PathVariable Integer id,ModelMap map)
	{
		map.addAttribute("order",service.getOrderMethodById(id));
		return "OrderMethodView";
	}
	
	
	
	//delete record
	@RequestMapping("/delete/{id}")
	public String deleteOrder(@PathVariable Integer id,RedirectAttributes map)
	{
		service.deleteOrderMethod(id);
		map.addFlashAttribute("msg", "Order id '"+id+"' deleted successfully ");
		
		log.info("order method deleted successfully !");
		return "redirect:all";
	}


		// 	update order method
		@RequestMapping(value="/update",method=POST)
		public String updateOrderMethod(@ModelAttribute OrderMethod order,Errors errors,ModelMap map)
		{
		
				validator.validate(order, errors);
				
				  
				  if(!errors.hasErrors()) 
				  {
					  //call service
					  service.updateOrderMethod(order);
					  map.addAttribute("msg","Order Method '"+order.getOrderId()+"' updated successfully ");
					  
					  log.info("order method updated successfully !");
				  }
				  else {
					  map.addAttribute("emsg","Enter Valid Details");

					  log.info("failed to update order method !");
				}
				 
				
				map.addAttribute("order",service.getOrderMethodById(order.getOrderId())); // data for view page
				return "OrderMethodView";
		
		}


	// excel export
	@RequestMapping("/excel")
	public ModelAndView getExcelExport(@RequestParam(value="id",required=false,defaultValue="0")Integer id,ModelMap map )
	{
		ModelAndView m = new ModelAndView();
		//setting view
		m.setView(new OrderMethodExcelView());


		if(id==0) //means no id parameter with path
		{
			m.addObject("list", service.getAllOrderMethods());  // so getting all rows i.e. export all
		}
		else // means id present with path
		{
			// getting data according to id
			m.addObject("list",Collections.singletonList(service.getOrderMethodById(id)));
		}

		log.info("Excel report generated");
		return m;
	}



	// pdf export
	@RequestMapping("/pdf")
	public ModelAndView getPdfExport(@RequestParam(value="id",required=false,defaultValue="0")Integer id,ModelMap map )
	{
		ModelAndView m = new ModelAndView();
		//setting view
		m.setView(new OrderMethodPdfView());


		if(id==0) //means no id parameter with path
		{
			m.addObject("list", service.getAllOrderMethods());  // so getting all rows i.e. export all
		}
		else // means id present with path
		{
			// getting data according to id
			m.addObject("list",Collections.singletonList(service.getOrderMethodById(id)));
		}

		log.info("pdf report generated");
		return m;
	}


	//charts 
	@RequestMapping("/chart")
	public String showCharts()
	{
		String path= context.getRealPath("/");
		List<Object[]> data = service.getOrderMethodCountByMode();
		
		util.ganeratePie(path, data);
		util.ganerateBar(path, data);

		log.info("charts generated");
		return "orderMethodReports";
	}

	

		
	
}
