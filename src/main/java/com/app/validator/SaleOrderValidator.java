package com.app.validator;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.app.model.SaleOrder;
import com.app.service.ISaleOrderService;

@Component
public class SaleOrderValidator implements Validator{

	@Autowired
	private ISaleOrderService service;
	
	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return SaleOrder.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		//converting to saleOrder
		SaleOrder saleOrder = (SaleOrder) target;
		
	
		System.out.println(saleOrder);
		
		// validation starts
		
		// for order code - input type
		if(!Pattern.matches("[A-Za-z0-9]{2,20}",saleOrder.getOrderCode()))
		{
			errors.rejectValue("orderCode",null,"please enter valid order code, no special chars allowed , max length is 20 chars");
		}
		
		
		if(service.isOrderCodeExits(saleOrder.getOrderCode()) && saleOrder.getSaleId()==null)  // true means already exits , if id is not null means update page or register page 
		{
			errors.rejectValue("orderCode",null,"already exits ");
		}
		
		// for shipment code - dropdown
		if(saleOrder.getShipmentCode()==null || saleOrder.getShipmentCode().getShipmentId()==null)
		{
			errors.rejectValue("shipmentCode",null,"please select Shipment code");  // for integration we will use child class variable name
		}
		
		
		// for order method - dropdown
		if(saleOrder.getCustomer()==null || saleOrder.getCustomer().getWhId()==null)
		{
			errors.rejectValue("customer",null,"please select vendor");
		}
		
		
		// for ref num - input type
		if(!Pattern.matches("[A-Za-z0-9]{2,20}",saleOrder.getRefNum()))
		{
			errors.rejectValue("refNum",null,"please enter valid ref num");
		}


		/*** radio button-- quality check ***/
		
		if(StringUtils.isEmpty(saleOrder.getStockMode()))
		{
			errors.rejectValue("qualityCheck",null ,"please choose one option");
		}
		
		/*** dropdown -- Stock source **/
		
		if(StringUtils.isEmpty(saleOrder.getStockSource()))
		{
			errors.rejectValue("note",null,"please enter note ,max 20 charecters allowed");
		}
		
		
		// for  note - textarea
		if(StringUtils.isEmpty(saleOrder.getNote()) || saleOrder.getNote().length()>20)
		{
			errors.rejectValue("note",null,"please enter note ,max 20 charecters allowed");
		}
		
	}

}
