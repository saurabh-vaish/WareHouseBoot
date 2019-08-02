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

import com.app.model.Purchase;
import com.app.model.PurchaseDtl;
import com.app.service.IItemService;
import com.app.service.IPurchaseService;
import com.app.service.IShipmentTypeService;
import com.app.service.IWhUserService;
import com.app.validator.PurchaseValidator;
import com.app.view.VendorInvoicePdfView;

@Controller
@RequestMapping("/purchase")
public class PurchaseController {

	@Autowired
	private IPurchaseService service;

	@Autowired
	private IShipmentTypeService shservice;
	
	@Autowired
	private IWhUserService whservice;
	
	@Autowired
	private PurchaseValidator validator;

	@Autowired
	private IItemService itemService;

	//1. register page
	@RequestMapping("/register")
	public  String showRegister(ModelMap map)
	{
		/**Module Integration start**/

		map.addAttribute("listshipment",shservice.getEnableShipmentIdsAndCodes());
		map.addAttribute("listvendor",whservice.getAllWhUserByType("Vendor"));
		/**Module Integration ends**/

		map.addAttribute("purchase",new Purchase()); // form backing object
		return "PurchaseRegister";
	}

	
	// * ajax  method for checking duplicate orders
	@RequestMapping("/check")
	public @ResponseBody String checkOrderCode(@RequestParam("order")String order) //@ResponseBody for ajax call
	{
		String msg="";
		
		  List<Purchase> pur = service.getAllPurchases();  
		  for(Purchase p : pur)
		  {
			  if(p.getOrderCode().equals(order))
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
	public String savePurchase(@ModelAttribute Purchase purchase,Errors errors,ModelMap map)
	{
		
		validator.validate(purchase, errors);
		
		if(!errors.hasErrors()) // no error
		{
			Integer id = service.savePurchase(purchase); 
			map.addAttribute("msg","purchase '"+id+"' saved successfully");
			map.addAttribute("purId",id);
			map.addAttribute("purchase",new Purchase()); // form backing object

		}
		else
		{
			map.addAttribute("msg","enter valid details");
		}


		map.addAttribute("listshipment",shservice.getEnableShipmentIdsAndCodes());
		map.addAttribute("listvendor",whservice.getAllWhUserByType("Vendor"));
		
		return "PurchaseRegister";
	}

	// 3. all
	@RequestMapping("/all")
	public String displayPurchases(ModelMap map)
	{
		map.addAttribute("list", service.getAllPurchases());
		return "PurchaseData";
	}
	

	//4 . show one record
	@RequestMapping("/view")
	public String showOneRecord(@RequestParam Integer id,ModelMap map)
	{
		//map.addAttribute("listshipment",shservice.getShipementIdsAndCodes());
		map.addAttribute("listshipment",shservice.getEnableShipmentIdsAndCodes());
		map.addAttribute("listvendor",whservice.getAllWhUserByType("Vendor"));
		
		map.addAttribute("purchase",service.getPurchaseById(id));
		return "PurchaseView";
	}
	
	//5 -Delete
	@RequestMapping("/delete")
	public String deleteById(@RequestParam("id")Integer id,ModelMap map)
	{
		service.deletePurchase(id);
		map.addAttribute("list",service.getAllPurchases());
		map.addAttribute("msg", "purchase "+id+" Deleted Successfully");
		return "PurchaseData";

	}		
	
	// 6 -  Update 
	@RequestMapping(value="/update",method=RequestMethod.POST)
	public String updatePurchase(@ModelAttribute Purchase purchase,Errors errors,ModelMap map)
	{

		validator.validate(purchase, errors);
		System.out.println(purchase);
		if(!errors.hasErrors())
		{
			//call service
			service.updatePurchase(purchase);
			map.addAttribute("msg", "purchase '"+purchase.getPurId()+"' update successfully ");
		}
		else
		{
			map.addAttribute("msg","Enter Valid Details");
		}

		map.addAttribute("purchase",service.getPurchaseById(purchase.getPurId())); // data for purchase view page
//		map.addAttribute("listshipment",shservice.getShipementIdsAndCodes());
		map.addAttribute("listshipment",shservice.getAllShipmentTypes());
		map.addAttribute("listvendor",whservice.getAllWhUserByType("Vendor"));
		
		return "PurchaseView";


	}

	
	
	// 7. export to excel 
	@RequestMapping("/excel")
	public ModelAndView showExcel(@RequestParam(value="id",required=false,defaultValue="0")Integer id)
	{
		ModelAndView m = new ModelAndView();
	//	m.setView(new PurchaseExcelView());
		
		if(id==0)
		{
			m.addObject("list",service.getAllPurchases());
		}
		else
		{
			m.addObject("list", Collections.singletonList(service.getPurchaseById(id)));
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
	//	m.setView(new PurchasePdfView());
		
		if(id==0) //means no id parameter with path
		{
			m.addObject("list", service.getAllPurchases());  // so getting all rows i.e. export all
		}
		else // means id present with path
		{
			// getting data according to id
			m.addObject("list",Collections.singletonList(service.getPurchaseById(id)) );
		}
		
		return m;
	}
	
	/***
	 * Child Operations starts here
	 */

	private void getDtlUi(Integer orderId,ModelMap map) {
		Purchase po=service.getPurchaseById(orderId);

		System.out.println(po);
		
		//PO Code to show as Read Only
		map.addAttribute("poId", po.getPurId());
		map.addAttribute("poCode", po.getOrderCode());
		map.addAttribute("poStatus", po.getStatus());
			
		/**
		 * Form Data START
		 */
		//new empty form child
		PurchaseDtl dtl=new PurchaseDtl();
		dtl.setPoHdrId(po.getPurId());
		map.addAttribute("purchaseDtl", dtl);

		System.out.println(dtl);
		
		//display items drop down
		Map<Integer,String> itemsMap=itemService.getItemIdNameCode();
		map.addAttribute("itemsMap", itemsMap);
		/**
		 *  FORM DATA END
		 */
		
		
		List<PurchaseDtl> dtls=po.getDetails();System.out.println("details"+dtls);
		if(dtls==null || dtls.isEmpty()) {
			po.setStatus("OPEN");
			map.addAttribute("poStatus","OPEN");
			service.updatePurchase(po);
		}else {
			//adjust slnos
			int count=0;
			for(PurchaseDtl d:dtls) {
				d.setSlno(++count);
			}
		}

		//all added items to show in table
		map.addAttribute("dtls",dtls );

	}

	/** 1. Show Add Items Pages
	 * 
	 */
	@RequestMapping("/viewItems")
	public String showItemsPage(@RequestParam Integer orderId,ModelMap map) {
		//complete common setup is provided here
		getDtlUi(orderId, map);
		return "PurchaseItems";
	}

	/**
	 * 2. Add Items to PO and update status to PICKING (if items count >=1)
	 * 
	 */
	@RequestMapping(value="/addItem",method=RequestMethod.POST)
	public String addPoItem(@ModelAttribute PurchaseDtl purchaseDtl,ModelMap map) {
		//do form validation PurchaseDtlValidator

		//save child data
		Purchase po=service.getPurchaseById(purchaseDtl.getPoHdrId());
		po.setStatus("PICKING"); // check here status update
		po.getDetails().add(purchaseDtl);
		service.updatePurchase(po);

		//setup Purchase Items JSP Data
		getDtlUi(po.getPurId(), map);
		return "PurchaseItems";
	}

	/**
	 * 3. Delete Item based on dtlId and update status to OPEN if Items Count=0
	 * 
	 */
	@RequestMapping("/removeItem")
	public String deletePoDtl(@RequestParam Integer orderDtlId,@RequestParam Integer orderId,ModelMap map) {
		service.deletePurchaseDtlById(orderDtlId);
		//setup Purchase Items JSP Data
		getDtlUi(orderId, map);
		return "PurchaseItems";
	}

	/**
	 * 4. Confirm Order ie chnage status to ORDERED 
	 */
	@RequestMapping("/updateOrderStatus")
	public String updateOrderConfirm(@RequestParam Integer orderId,@RequestParam String status,ModelMap map) {
		Purchase po=service.getPurchaseById(orderId);
		po.setStatus(status);
		service.updatePurchase(po);
		String page=null;
		if(status.equals("ORDERED")) {
			page="PurchaseItems";
			getDtlUi(orderId, map);
		}else {
			map.addAttribute("list", service.getAllPurchases());
			page="PurchaseData";
		}
		return page;	
	}
	
	/**
	 * 5. Generate Vendor Invoice
	 */
	@RequestMapping("/viewInvoice")
	public ModelAndView generateInvoice(@RequestParam Integer orderId){
		Purchase po=service.getPurchaseById(orderId);
		ModelAndView m=null;
		if(po.getStatus().equals("INVOICED")){
			m=new ModelAndView(new VendorInvoicePdfView(),"po",po);
		}else{
			m=new ModelAndView("PurchaseData","purchase", service.getAllPurchases());
		}
		return m;
	}

}