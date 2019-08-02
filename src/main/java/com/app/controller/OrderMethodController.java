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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.app.model.OrderMethod;
import com.app.service.IOrderMethodService;
import com.app.util.OrderMethodUtil;
import com.app.validator.OrderMethodValidator;
import com.app.view.OrderMethodExcelView;
import com.app.view.OrderMethodPdfView;


@Controller
@RequestMapping("/order")
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
			 System.out.println(msg);
			  return msg;
		}	
	

	// save order method
	@RequestMapping(value="/save",method=POST)
	public String saveOrderMethod(@ModelAttribute OrderMethod orderMethod,Errors errors,ModelMap map)
	{
		validator.validate(orderMethod, errors);
		
		if(!errors.hasErrors())  // no errors
		{
			Integer id = service.saveOrderMethod(orderMethod);
			map.addAttribute("msg"," Order Method '"+id+"' registered successfully" );  // save data and msg sending to ui
			map.addAttribute("orderId",id );  // save data and id sending to ui
			
			map.addAttribute("orderMethod",new OrderMethod()); // clear form backing object
		}
		else
		{
			map.addAttribute("msg", "enter valid details");
		}
		return "OrderMethodRegister";
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
	@RequestMapping("/view")
	public String showOneRecord(@RequestParam Integer id,ModelMap map)
	{
		map.addAttribute("order",service.getOrderMethodById(id));
		return "OrderMethodView";
	}
	
	
	//delete record
	@RequestMapping("/delete")
	public String deleteOrder(@RequestParam("id")Integer id,ModelMap map)
	{
		service.deleteOrderMethod(id);
		map.addAttribute("list", service.getAllOrderMethods());
		map.addAttribute("msg", "Order id '"+id+"' deleted successfully ");
		return "OrderMethodData";
	}


		// 	update order method
		@RequestMapping(value="/update",method=POST)
		public String updateOrderMethod(@ModelAttribute OrderMethod order,Errors errors,ModelMap map)
		{
		
				validator.validate(order, errors);
				
				  System.out.println(order);
				  
				  if(!errors.hasErrors()) 
				  {
					  //call service
					  service.updateOrderMethod(order);
					  map.addAttribute("msg","Order Method '"+order.getOrderId()+"' updated successfully ");
				  }
				  else {
					  map.addAttribute("msg","Enter Valid Details");
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
		return "orderMethodReports";
	}

	

		
	
}
