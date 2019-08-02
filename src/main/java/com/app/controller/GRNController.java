package com.app.controller;

import java.util.Arrays;
import java.util.List;

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

import com.app.model.GoodRecieveNote;
import com.app.model.Item;
import com.app.model.Purchase;
import com.app.model.PurchaseDtl;
import com.app.service.IGRNService;
import com.app.service.IPurchaseService;
import com.app.validator.GRNValidator;
import com.app.view.GRNExcelView;
import com.app.view.GRNPdfView;

@Controller
@RequestMapping("/grn")
public class GRNController {

	@Autowired
	private IGRNService grnService;
	@Autowired
	private IPurchaseService purchaseService;
	@Autowired
	private GRNValidator validator;

	@RequestMapping("/register")
	public String showRegister(ModelMap map) {
		map.addAttribute("goodRecieveNote", new GoodRecieveNote());
		map.addAttribute("purchase", purchaseService.getInvoicedPurchaseOrders("INVOICED"));
		return "GRNRegister";
	}

	
	// * ajax  method for checking duplicate codes
	@RequestMapping("/check")
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
		System.out.println(msg);
		return msg;
	}	


	@RequestMapping(value="/insert",method=RequestMethod.POST)
	public String savePurchase(@ModelAttribute GoodRecieveNote goodRecieveNote,Errors errors,ModelMap map) {

		validator.validate(goodRecieveNote, errors);

		if (errors.hasErrors()) {
			map.addAttribute("message", "please check all fields !!");

		} else {

			//getting child data by id
			Integer orderId = goodRecieveNote.getPurchase().getPurId();
			Purchase purchase=purchaseService.getPurchaseById(orderId);
			purchase.setStatus("RECEIVED");
			purchaseService.updatePurchase(purchase);

			Integer id =grnService.saveGRN(goodRecieveNote);
			map.addAttribute("msg", "GRN is saved with Id :"+ id);
			map.addAttribute("grnId", id);
			map.addAttribute("goodRecieveNote", new GoodRecieveNote());
		}
		map.addAttribute("purchase", purchaseService.getInvoicedPurchaseOrders("INVOICED"));
		return "GRNRegister";
	}

	@RequestMapping("/view")
	public String viewOne(@RequestParam(required=false,defaultValue="0") Integer grnId,ModelMap map) {

		String page=null;
		if (grnId!=0) {
			map.addAttribute("goodRecieveNote", grnService.getGRNById(grnId));
			page="GRNView";
		} else {
			map.addAttribute("goodRecieveNote", grnService.getAllGRNs());
			page = "GRNData";

		}
		return page;
	}

	@RequestMapping("/delete")
	public String deletePurchase(@RequestParam Integer grnId,ModelMap map) {

		grnService.deleteGRN(grnId);
		map.addAttribute("message", "GRN deleted successfully with id :"+grnId+" !!");
		map.addAttribute("goodRecieveNote", grnService.getAllGRNs());
		return "GRNData";
	}

	@RequestMapping("/edit")
	public String editOne(@RequestParam Integer grnId,ModelMap map) {
		map.addAttribute("goodRecieveNote", grnService.getGRNById(grnId));
		map.addAttribute("purchase", purchaseService.getInvoicedPurchaseOrders("INVOICED"));
		return "GRNEdit";
	}

	@RequestMapping(value="/update",method=RequestMethod.POST)
	public String updatePurchase(@ModelAttribute GoodRecieveNote goodRecieveNote,Errors errors,ModelMap map) {

		grnService.updateGRN(goodRecieveNote);
		map.addAttribute("goodRecieveNote", grnService.getAllGRNs());
		return "GRNData";

	}

	@RequestMapping("/excelExport")
	public ModelAndView excelExport(@RequestParam(required=false,defaultValue="0") Integer grnId,ModelMap map) {
		ModelAndView mv=null;
		if (grnId!=0) {
			mv=new ModelAndView(new GRNExcelView(), "goodRecieveNote", Arrays.asList(grnService.getGRNById(grnId)));
		} else {
			mv=new ModelAndView(new GRNExcelView(), "goodRecieveNote", grnService.getAllGRNs());
		}
		return mv;
	}
	@RequestMapping("/pdfExport")
	public ModelAndView pdfExport(@RequestParam(required=false,defaultValue="0") Integer grnId,ModelMap map) {
		ModelAndView mv=null;
		if (grnId!=0) {
			mv=new ModelAndView(new GRNPdfView(), "goodRecieveNote", Arrays.asList(grnService.getGRNById(grnId)));
		} else {
			mv=new ModelAndView(new GRNPdfView(), "goodRecieveNote", grnService.getAllGRNs());
		}
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
			//map.addAttribute("", purchaseDtls.get);
			for (PurchaseDtl purchaseDtl : purchaseDtls) {
				purchaseDtl.setSlno(++count);
			}
		} 
		
		map.addAttribute("nullCount",purchaseService.getNullGrnStatusCount(orderId));

		map.addAttribute("purchaseDtls", purchaseDtls);
	}
	
	@RequestMapping("/viewItems")
	public String viewItems(@RequestParam Integer grnId,ModelMap map) {

		getPurchaseDtls(grnId, map);
		return "GRNItems";
	}

	@RequestMapping("/updateOrderStatus")
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
		return "GRNItems";
	}


}