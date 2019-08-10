package com.app.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.model.SaleOrder;
import com.app.model.SalesDetails;
import com.app.model.Shipping;
import com.app.service.ISaleOrderService;
import com.app.service.IShippingService;
import com.app.validator.ShippingValidator;
import com.app.view.ShippingExcelView;
import com.app.view.ShippingPdfView;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/shipping")
public class ShippingController {

	
	@Autowired
	private IShippingService shippingService;
	
	@Autowired
	private ISaleOrderService saleOrderService;
	
	@Autowired
	private ShippingValidator validator;

	
	@GetMapping("/register")
	public String showRegister(ModelMap map) {
		
		map.addAttribute("shipping", new Shipping());
		map.addAttribute("saleOrder", saleOrderService.getInvoicedSaleOrders("INVOICED"));
		
		return "ShippingRegister";
	}

	
	
	// * ajax  method for checking duplicate codes
	@GetMapping("/check")
	public @ResponseBody String checkShippingCode(@RequestParam("order")String code) //@ResponseBody for ajax call
	{
		String msg="";

		List<Shipping> sh = shippingService.getAllShippings();
		for(Shipping s:sh)
		{
			if(s.getShipCode().equals(code))
			{
				System.out.println("yes");
				msg="Shipping Code already exits .";
				break;
			}
		}
	
		log.info(msg);
		return msg;
	}	

	
	@PostMapping(value="/insert")
	public String savePurchase(@ModelAttribute Shipping shipping,Errors errors,ModelMap map,RedirectAttributes attr) {

		validator.validate(shipping, errors);

		if (errors.hasErrors()) {
			map.addAttribute("emsg", "please check all fields !!");
		
			map.addAttribute("saleOrder", saleOrderService.getInvoicedSaleOrders("INVOICED"));
			return "ShippingRegister";

		} 
		else {

			//getting child data by id
			Integer saleOrderId = shipping.getSaleOrder().getSaleId();
			SaleOrder saleOrder=saleOrderService.getSaleOrderById(saleOrderId);
			saleOrder.setStatus("SHIPPED");
			saleOrderService.updateSaleOrder(saleOrder);

			Integer id = shippingService.saveShipping(shipping);
			attr.addFlashAttribute("msg", "Shipping is done with Id :"+id);
			attr.addFlashAttribute("shipId", id);

			log.info("Shipping Registred Successfully");
			
			return "redirect:register";
		}
	}

	
	@GetMapping("/view")
	public String viewOne(@RequestParam(required=false,defaultValue="0") Integer shipId,ModelMap map) {

		String page=null;
		
		if (shipId!=0) {
			map.addAttribute("shipping", shippingService.getShippingById(shipId));
			map.addAttribute("saleOrder", saleOrderService.getInvoicedSaleOrders("INVOICED"));
		
			page="ShippingView";
		} else {
			map.addAttribute("shipping", shippingService.getAllShippings());
			page = "ShippingData";

		}
		
		return page;
	}
	
	

	@GetMapping("/delete")
	public String deletePurchase(@RequestParam Integer shipId,ModelMap map) {

		shippingService.deleteShipping(shipId);
		map.addAttribute("message", "Shipping deleted successfully with id :"+shipId+" !!");
		map.addAttribute("shipping", shippingService.getAllShippings());
		
		return "ShippingData";
	}

	

	@PostMapping(value="/update")
	public String updatePurchase(@ModelAttribute Shipping shipping,Errors errors,RedirectAttributes map) {

		
		validator.validate(shipping, errors);

		if (errors.hasErrors()) {
			map.addFlashAttribute("emsg", "please check all fields !!");
		
		} 
		else {

			shippingService.updateShipping(shipping);
			map.addFlashAttribute("msg", "Updated Successfully !");

			log.info("Shipping Updated Successfully");
			
		}
				
		return "redirect:view?shipId="+shipping.getShipId();

	}

	
	
	@GetMapping("/excelExport")
	public ModelAndView excelExport(@RequestParam(required=false,defaultValue="0") Integer shipId,ModelMap map) {
		
		ModelAndView mv=null;
		
		if (shipId!=0) {
			mv=new ModelAndView(new ShippingExcelView(), "shipping", Arrays.asList(shippingService.getShippingById(shipId)));
		}
		else {
			mv=new ModelAndView(new ShippingExcelView(), "shipping", shippingService.getAllShippings());
		}
		
		log.info("Excel Report Generated");
		return mv;
	}
	
	
	@GetMapping("/pdfExport")
	public ModelAndView pdfExport(@RequestParam(required=false,defaultValue="0") Integer shipId,ModelMap map) {
		
		ModelAndView mv=null;
		if (shipId!=0) {
			mv=new ModelAndView(new ShippingPdfView(), "shipping", Arrays.asList(shippingService.getShippingById(shipId)));
		}
		else {
			mv=new ModelAndView(new ShippingPdfView(), "shipping", shippingService.getAllShippings());
		}
		
		log.info("Pdf Report Generated");
		return mv;
	}

	
	private void getSaleDtls(Integer shipId,ModelMap map) {

		Shipping shipping = shippingService.getShippingById(shipId);
		Integer saleOrderId=shipping.getSaleOrder().getSaleId();

		map.addAttribute("shipId", shipping.getShipId());
		map.addAttribute("shipCode", shipping.getShipCode());
		map.addAttribute("saleOrderId", saleOrderId);
		map.addAttribute("orderStatus", shipping.getSaleOrder().getStatus());

		List<SalesDetails> salesDetails = shipping.getSaleOrder().getSalesDetails();

		int count=0;
		if (salesDetails!=null && !salesDetails.isEmpty()) {
			for (SalesDetails salesDetail : salesDetails) {
				salesDetail.setSlno(++count);
			}
		} 

		map.addAttribute("nullCount",saleOrderService.getNullShippingStatusCount(saleOrderId));
		map.addAttribute("salesDetails", salesDetails);
	}

	
	
	@GetMapping("/viewItems")
	public String viewItems(@RequestParam Integer shipId,ModelMap map) {

		getSaleDtls(shipId, map);
		return "ShippingItems";
	}

	
	@GetMapping("/updateOrderStatus")
	public String acceptPurchaseOrders(
			@RequestParam Integer shipId,
			@RequestParam(required=false,defaultValue="0") Integer saleOrderId,
			@RequestParam(required=false,defaultValue="0") Integer salesDtlsId,
			@RequestParam String shipSatus,ModelMap map) {


		if (salesDtlsId!=0 && saleOrderId==0) {
			SalesDetails salesDetails = saleOrderService.getSalesDetailsById(salesDtlsId);
			salesDetails.setShipStatus(shipSatus);;
			saleOrderService.updateSalesDetails(salesDetails);
		} else {
			saleOrderService.updateAllSalesDetailsStatus(shipSatus,saleOrderId);
		}

		getSaleDtls(shipId, map);
		
		log.info("Status Updated");
		return "ShippingItems";
	}


}
