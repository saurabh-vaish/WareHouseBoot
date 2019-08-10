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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.model.GoodRecieveNote;
import com.app.model.Purchase;
import com.app.model.PurchaseDtl;
import com.app.service.IGRNService;
import com.app.service.IPurchaseService;
import com.app.validator.GRNValidator;
import com.app.view.GRNExcelView;
import com.app.view.GRNPdfView;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/grn")
public class GRNController {

	@Autowired
	private IGRNService grnService;
	
	@Autowired
	private IPurchaseService purchaseService;
	
	@Autowired
	private GRNValidator validator;

	
	@GetMapping("/register")
	public String showRegister(ModelMap map) {
	
		map.addAttribute("goodRecieveNote", new GoodRecieveNote());
		map.addAttribute("purchase", purchaseService.getInvoicedPurchaseOrders("INVOICED"));
		
		return "GRNRegister";
	}

	
	
	// * ajax  method for checking duplicate codes
	@GetMapping("/check")
	public @ResponseBody String checkGRNCode(@RequestParam("code")String code) //@ResponseBody for ajax call
	{
		String msg="";

		List<GoodRecieveNote> grn = grnService.getAllGRNs();
		for(GoodRecieveNote g:grn)
		{
			if(g.getGrnCode().equals(code))
			{
				System.out.println("yes");
				msg="GRN Code already exits .";
				break;
			}
		}
	
		log.info(msg);
		return msg;
	}	


	
	@PostMapping(value="/insert")
	public String savePurchase(@ModelAttribute GoodRecieveNote goodRecieveNote,Errors errors,ModelMap map,RedirectAttributes attr) {

		validator.validate(goodRecieveNote, errors);

		if (errors.hasErrors()) {
			map.addAttribute("emsg", "please check all fields !!");

			map.addAttribute("purchase", purchaseService.getInvoicedPurchaseOrders("INVOICED"));
			
			log.info("Grn Register Failed !!" );
			return "GRNRegister";
			
		} else {

			//getting child data by id
			Integer orderId = goodRecieveNote.getPurchase().getPurId();
			Purchase purchase=purchaseService.getPurchaseById(orderId);
			purchase.setStatus("RECEIVED");
			purchaseService.updatePurchase(purchase);

			Integer id =grnService.saveGRN(goodRecieveNote);
			attr.addFlashAttribute("msg", "GRN is saved with Id :"+ id);
			attr.addFlashAttribute("grnId", id);
			
			log.info("GRN Registred Successfully ");
			return "redirect:register";
		}
		
	}
	
	

	@GetMapping("/view")
	public String viewOne(@RequestParam(required=false,defaultValue="0") Integer grnId,ModelMap map) {

		String page=null;
		if (grnId!=0) {
			map.addAttribute("goodRecieveNote", grnService.getGRNById(grnId));
			
			map.addAttribute("purchase", purchaseService.getInvoicedPurchaseOrders("INVOICED"));
			page="GRNView";
		} else {
			map.addAttribute("goodRecieveNote", grnService.getAllGRNs());
			page = "GRNData";
		}
		
		return page;
	}
	

	@GetMapping("/delete")
	public String deletePurchase(@RequestParam Integer grnId,ModelMap map) {

		grnService.deleteGRN(grnId);
		map.addAttribute("message", "GRN deleted successfully with id :"+grnId+" !!");
		map.addAttribute("goodRecieveNote", grnService.getAllGRNs());
	
		log.info("GRN Deleted Successfully ");
		return "GRNData";
	}

	
	
	@GetMapping("/edit")
	public String editOne(@RequestParam Integer grnId,ModelMap map) {
		
		map.addAttribute("goodRecieveNote", grnService.getGRNById(grnId));
		map.addAttribute("purchase", purchaseService.getInvoicedPurchaseOrders("INVOICED"));
		
		return "GRNEdit";
	}

	
	
	@PostMapping(value="/update")
	public String updatePurchase(@ModelAttribute GoodRecieveNote goodRecieveNote,Errors errors,RedirectAttributes map) {

		validator.validate(goodRecieveNote, errors);

		if (errors.hasErrors()) {
			map.addFlashAttribute("emsg", "please check all fields !!");

			log.info("Grn update Failed !!" );
			
		} else {

			grnService.updateGRN(goodRecieveNote);
			map.addFlashAttribute("msg", "updated successfully ");
			
			log.info("GRN Updated Successfully ");
		}
				
		return "redirect:view?grnId="+goodRecieveNote.getGrnId();

	}

	
	@GetMapping("/excelExport")
	public ModelAndView excelExport(@RequestParam(required=false,defaultValue="0") Integer grnId,ModelMap map) {
		ModelAndView mv=null;
		if (grnId!=0) {
			mv=new ModelAndView(new GRNExcelView(), "goodRecieveNote", Arrays.asList(grnService.getGRNById(grnId)));
		} else {
			mv=new ModelAndView(new GRNExcelView(), "goodRecieveNote", grnService.getAllGRNs());
		}
	
		log.info("Excel Report Generated");
		return mv;
	}
	
	
	@GetMapping("/pdfExport")
	public ModelAndView pdfExport(@RequestParam(required=false,defaultValue="0") Integer grnId,ModelMap map) {
		ModelAndView mv=null;
		if (grnId!=0) {
			mv=new ModelAndView(new GRNPdfView(), "goodRecieveNote", Arrays.asList(grnService.getGRNById(grnId)));
		} else {
			mv=new ModelAndView(new GRNPdfView(), "goodRecieveNote", grnService.getAllGRNs());
		}
		
		log.info("Pdf Report Generated");
		return mv;
	}
	
	
	
	private void getPurchaseDtls(Integer grnId,ModelMap map) {

		GoodRecieveNote goodRecieveNote = grnService.getGRNById(grnId);
	
		map.addAttribute("grnId", goodRecieveNote.getGrnId());
		map.addAttribute("grnCode", goodRecieveNote.getGrnCode());
		
		Integer orderId=goodRecieveNote.getPurchase().getPurId();
		map.addAttribute("orderId", orderId);
		map.addAttribute("orderStatus", goodRecieveNote.getPurchase().getPurId());

		List<PurchaseDtl> purchaseDtls = goodRecieveNote.getPurchase().getDetails();

		int count=0;
		if (purchaseDtls!=null && !purchaseDtls.isEmpty()) {

			for (PurchaseDtl purchaseDtl : purchaseDtls) {
				purchaseDtl.setSlno(++count);
			}
		} 
		
		map.addAttribute("nullCount",purchaseService.getNullGrnStatusCount(orderId));

		map.addAttribute("purchaseDtls", purchaseDtls);
	}
	
	
	@GetMapping("/viewItems")
	public String viewItems(@RequestParam Integer grnId,ModelMap map) {

		getPurchaseDtls(grnId, map);
		return "GRNItems";
	}
	

	@GetMapping("/updateOrderStatus")
	public String acceptPurchaseOrders(
			@RequestParam Integer grnId,
			@RequestParam(required=false,defaultValue="0") Integer orderId,
			@RequestParam(required=false,defaultValue="0") Integer orderDtlId,
			@RequestParam String grnStatus,ModelMap map) {


		/*Purchase purchase = purchaseService.getPurchaseById(
				grnService.getGRNById(grnId)
				.getPurchase()
				.getOrderId()
				);*/
		
		if (orderDtlId!=0 && orderId==0) {
			PurchaseDtl purchaseDtl = purchaseService.getPurchaseDtlsById(orderDtlId);
			purchaseDtl.setGrnStatus(grnStatus);
			purchaseService.updatePurchaseDtls(purchaseDtl);
		} else {
			purchaseService.updateAllPurchaseDtlsStatus(grnStatus,orderId);
		}
		//purchase.setOrderStatus(orderStatus);
		//purchaseService.updatePurchase(purchase);
		
		getPurchaseDtls(grnId, map);
		
		log.info("Status Updated");
		return "GRNItems";
	}


}