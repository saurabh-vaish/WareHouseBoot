package com.app.validator;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.app.model.OrderMethod;

@Component
public class OrderMethodValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		// so that only this class can be validate
		return OrderMethod.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		// downcaoding to OrderMethod
		
		OrderMethod od = (OrderMethod) target;
		
		
		/***  validation order Method ***/
		
		
		/*** radio button-- orderMode ***/
		
		if(StringUtils.isEmpty(od.getOrderMode()))
		{
			errors.rejectValue("orderMode",null ,"please select one mode");
		}
		

		/*** Text Input -- OrderCode ***/
		
		if(!Pattern.matches("[A-Z0-9]{2,10}", od.getOrderCode()))
		{
			errors.rejectValue("orderCode",null ,"please enter min 2 max 10 chars in Uppercase");
		}
		
		/*** Dropdown -- execution type ***/
		
		
		if(StringUtils.isEmpty(od.getExeType()))
		{
			errors.rejectValue("exeType",null ,"please select one execution type");
		}
		
		
	
		/*** checkbox --  Order Accept ***/
		
		if(od.getOrderAccpet().isEmpty() || od.getOrderAccpet()==null)
		{
			errors.rejectValue("orderAccpet",null ,"choose at least one option");
		}
		
		
		
		/*** Text Input -- note ***/

		if(StringUtils.isEmpty(od.getOrderNote()) || od.getOrderNote().length()>20)
		{
			errors.rejectValue("orderNote",null ,"please enter note ,max 20 charecters allowed");
		}
	
	}
 
}
