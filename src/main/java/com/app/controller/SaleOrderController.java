package com.app.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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

import com.app.model.SaleOrder;
import com.app.model.SalesDetails;
import com.app.service.IItemService;
import com.app.service.ISaleOrderService;
import com.app.service.IShipmentTypeService;
import com.app.service.IWhUserService;
import com.app.validator.SaleOrderValidator;
import com.app.view.CustomerInvoicePdfView;

@Controller
@RequestMapping("/sale")
public class SaleOrderController {

	@Autowired
	private ISaleOrderService service;

	@Autowired
	private IShipmentTypeService shservice;
	
	@Autowired
	private IWhUserService whservice;
	
	@Autowired
	private SaleOrderValidator validator;
	
	@Autowired
	private IItemService itemService;
	
	//1. register page
	@RequestMapping("/register")
	public  String showRegister(ModelMap map)
	{
		/**Module Integration start**/

		map.addAttribute("listshipment",shservice.getEnableShipmentIdsAndCodes());
		map.addAttribute("listcustomer",whservice.getAllWhUserByType("Customer"));
		/**Module Integration ends**/

		map.addAttribute("saleOrder",new SaleOrder()); // form backing object
		return "SaleOrderRegister";
	}

	
	// * ajax  method for checking duplicate orders
	@RequestMapping("/check")
	public @ResponseBody String checkOrderCode(@RequestParam("order")String order) //@ResponseBody for ajax call
	{
		String msg="";
		
		  List<SaleOrder> sale = service.getAllSaleOrders();  
		  for(SaleOrder s : sale)
		  {
			  if(s.getOrderCode().equals(order))
			  {
				  System.out.println("yes");
				  msg="Order Code already exits .";
				  break;
			  }
		  }
		 
		  System.out.println(msg);
		  return msg;

	}

	// 2. save 
	@RequestMapping("/save")
	public String saveSaleOrder(@ModelAttribute SaleOrder saleOrder,Errors errors,ModelMap map)
	{
		
		validator.validate(saleOrder, errors);
		
		if(!errors.hasErrors()) // no error
		{
			Integer id = service.saveSaleOrder(saleOrder); 
			map.addAttribute("msg","saleOrder '"+id+"' saved successfully");
			map.addAttribute("saleId",id);
			
			map.addAttribute("saleOrder",new SaleOrder()); // form backing object
	
		}
		else
		{
			map.addAttribute("msg","enter valid details");
		}
		

		map.addAttribute("listshipment",shservice.getEnableShipmentIdsAndCodes());
		map.addAttribute("listcustomer",whservice.getAllWhUserByType("Customer"));
		return "SaleOrderRegister";
	}


	
	// 3. all
	@RequestMapping("/all")
	public String displaySaleOrders(ModelMap map)
	{
		map.addAttribute("list", service.getAllSaleOrders());
		return "SaleOrderData";
	}
	

	//4 . show one record
	@RequestMapping("/view")
	public String showOneRecord(@RequestParam Integer id,ModelMap map)
	{
		map.addAttribute("listshipment",shservice.getEnableShipmentIdsAndCodes());
		map.addAttribute("listcustomer",whservice.getAllWhUserByType("Customer"));
		
		map.addAttribute("saleOrder",service.getSaleOrderById(id));
		return "SaleOrderView";
	}
	
	//5 -Delete
	@RequestMapping("/delete")
	public String deleteById(@RequestParam("id")Integer id,ModelMap map)
	{
		service.deleteSaleOrder(id);
		map.addAttribute("list",service.getAllSaleOrders());
		map.addAttribute("msg", "saleOrder "+id+" Deleted Successfully");
		return "SaleOrderData";

	}		
	
	// 6 -  Update 
	@RequestMapping(value="/update",method=RequestMethod.POST)
	public String updateSaleOrder(@ModelAttribute SaleOrder saleOrder,Errors errors,ModelMap map)
	{

		validator.validate(saleOrder, errors);
		System.out.println(saleOrder);
		if(!errors.hasErrors())
		{
			//call service
			service.updateSaleOrder(saleOrder);
			map.addAttribute("msg", "saleOrder '"+saleOrder.getSaleId()+"' update successfully ");
		}
		else
		{
			map.addAttribute("msg","Enter Valid Details");
		}

		map.addAttribute("saleOrder",service.getSaleOrderById(saleOrder.getSaleId())); // data for saleOrder view page
//		map.addAttribute("listshipment",shservice.getShipementIdsAndCodes());
		map.addAttribute("listshipment",shservice.getEnableShipmentIdsAndCodes());
		map.addAttribute("listcustomer",whservice.getAllWhUserByType("Customer"));
		
		return "SaleOrderView";


	}

	
	
	// 7. export to excel 
	@RequestMapping("/excel")
	public ModelAndView showExcel(@RequestParam(value="id",required=false,defaultValue="0")Integer id)
	{
		ModelAndView m = new ModelAndView();
	//	m.setView(new SaleOrderExcelView());
		
		if(id==0)
		{
			m.addObject("list",service.getAllSaleOrders());
		}
		else
		{
			m.addObject("list", Collections.singletonList(service.getSaleOrderById(id)));
		}
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
	//	m.setView(new SaleOrderPdfView());
		
		if(id==0) //means no id parameter with path
		{
			m.addObject("list", service.getAllSaleOrders());  // so getting all rows i.e. export all
		}
		else // means id present with path
		{
			// getting data according to id
			m.addObject("list",Collections.singletonList(service.getSaleOrderById(id)) );
		}
		
		return m;
	}
	
	
	// child
	
	@RequestMapping("/viewItems")
	public String viewItems(@RequestParam Integer saleOrderId,ModelMap map) {
		getSalesDtls(saleOrderId,map);
		return "SaleItems";
	}

	@RequestMapping(value="/addItem",method=RequestMethod.POST)
	public String addItem(@ModelAttribute SalesDetails salesDetails,ModelMap map) {

		SaleOrder saleOrder=service.getSaleOrderById(salesDetails.getSoHoId());
		saleOrder.setStatus("READY");
		saleOrder.getSalesDetails().add(salesDetails);
		service.updateSaleOrder(saleOrder);
		getSalesDtls(salesDetails.getSoHoId(), map);
		return "SaleItems";
	}

	@RequestMapping("/removeItem")
	public String removeItem(@RequestParam Integer salesDtlsId,@RequestParam Integer saleOrderId,ModelMap map) {

		service.deleteSalesDetailsById(salesDtlsId);
		getSalesDtls(saleOrderId, map);
		return "SaleItems";

	}

	@RequestMapping("/updateOrderStatus")
	public String updateOrderStatus(@RequestParam Integer saleOrderId,@RequestParam String orderStatus,ModelMap map) {
		SaleOrder saleOrder = service.getSaleOrderById(saleOrderId);
		saleOrder.setStatus(orderStatus);
		service.updateSaleOrder(saleOrder);
		String page=null;
		if (orderStatus.equals("CONFIRM")) {
			page="SaleItems";
			getSalesDtls(saleOrderId, map);
		} else {
			map.addAttribute("list", service.getAllSaleOrders());
			page="SaleOrderData";
		}
		return page;
	}
	
	@RequestMapping("/viewInvoice")
	public ModelAndView viewInvoice(@RequestParam Integer saleOrderId) {
		
		ModelAndView m = new ModelAndView(new CustomerInvoicePdfView(), 
				"saleOrder", 
				service.getSaleOrderById(saleOrderId));
		return m;
	}


	private void getSalesDtls(Integer saleOrderId,ModelMap map) {

		SaleOrder saleOrder = service.getSaleOrderById(saleOrderId);

		// add code and status to SaleItem page
		map.addAttribute("saleOrderId", saleOrder.getSaleId());
		map.addAttribute("saleOrderCode", saleOrder.getOrderCode());
		map.addAttribute("orderStatus", saleOrder.getStatus());


		//new child obj
		SalesDetails salesDetails=new SalesDetails();
		salesDetails.setSoHoId(saleOrder.getSaleId());
		//add child obj to ui
		map.addAttribute("salesDetails", salesDetails);

		//add item code and id 
		Map<Integer, String> item = itemService.getItemIdNameCode();
		map.addAttribute("item", item);

		List<SalesDetails> salesDetailsList=saleOrder.getSalesDetails();

		if (salesDetailsList==null || salesDetailsList.isEmpty()) {
			saleOrder.setStatus("OPEN");
			map.addAttribute("orderStatus", "OPEN");
			service.updateSaleOrder(saleOrder);
		} else {
			int count=0;
			for (SalesDetails salesDetails3 : salesDetailsList) {
				salesDetails3.setSlno(++count);
			}

			map.addAttribute("salesDetailsList", salesDetailsList);

		}
	}
}
