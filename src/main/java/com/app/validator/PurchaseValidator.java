package com.app.validator;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.app.model.Purchase;
import com.app.service.IPurchaseService;


@Component
public class PurchaseValidator implements Validator{

	@Autowired
	private IPurchaseService service;
	
	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return Purchase.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		//converting to purchase
		Purchase purchase = (Purchase) target;
		
	
		System.out.println(purchase);
		
		// validation starts
		
		// for order code - input type
		if(!Pattern.matches("[A-Za-z0-9]{2,20}",purchase.getOrderCode()))
		{
			errors.rejectValue("orderCode",null,"please enter valid order code, no special chars allowed , max length is 20 chars");
		}
		
		
		if(service.isOrderCodeExits(purchase.getOrderCode()) && purchase.getPurId()==null)  // true means already exits , if id is not null means update page or register page 
		{
			errors.rejectValue("orderCode",null,"already exits ");
		}
		
		// for shipment code - dropdown
		if(purchase.getShipmentCode()==null || purchase.getShipmentCode().getShipmentId()==null)
		{
			errors.rejectValue("shipmentCode",null,"please select Shipment code");  // for integration we will use child class variable name
		}
		
		
		// for order method - dropdown
		if(purchase.getVendor()==null || purchase.getVendor().getWhId()==null)
		{
			errors.rejectValue("vendor",null,"please select vendor");
		}
		
		
		// for ref num - input type
		if(!Pattern.matches("[A-Za-z0-9]{2,20}",purchase.getRefNum()))
		{
			errors.rejectValue("refNum",null,"please enter valid ref num");
		}


		/*** radio button-- quality check ***/
		
		if(StringUtils.isEmpty(purchase.getQualityCheck()))
		{
			errors.rejectValue("qualityCheck",null ,"please choose one option");
		}
		
		// for  note - textarea
		if(StringUtils.isEmpty(purchase.getNote()) || purchase.getNote().length()>20)
		{
			errors.rejectValue("note",null,"please enter note ,max 20 charecters allowed");
		}
		
	}

}
