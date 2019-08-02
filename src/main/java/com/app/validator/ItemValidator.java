package com.app.validator;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.app.model.Item;


@Component
public class ItemValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return Item.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		//converting to item
		Item item = (Item) target;
		
	
		System.out.println(item);
		
		// validation starts
		
		// for item code - input type
		if(!Pattern.matches("[A-Za-z0-9]{2,20}",item.getItemCode()))
		{
			errors.rejectValue("itemCode",null,"please enter valid item code, no special chars allowed , max length is 20 chars");
		}
		
		
		// for item length - input type
		if(!Pattern.matches("\\d+(\\.\\d{1,8})?",item.getItemLength().toString()))
		{
			errors.rejectValue("itemLength",null,"please enter valid item length");
		}
		
		// for item width - input type
		if(!Pattern.matches("\\d+(\\.\\d{1,8})?",item.getItemWidth().toString()))
		{
			errors.rejectValue("itemWidth",null,"please enter valid item width");
		}
		
		// for item height - input type
		if(!Pattern.matches("\\d+(\\.\\d{1,8})?",item.getItemHeight().toString()))
		{
			errors.rejectValue("itemHeight",null,"please enter valid item height");
		}
		
		// for base cost - input type
		if(!Pattern.matches("\\d+(\\.\\d{1,8})?",item.getBaseCost()))
		{
			errors.rejectValue("baseCost",null,"please enter valid base cost");
		}

		
		// for base currency - dropdown
		if(StringUtils.isEmpty(item.getBaseCurr()))
		{
			errors.rejectValue("baseCurr",null,"please select base currency");
		}
		
		
		// for uom type - dropdown
		if(StringUtils.isEmpty(item.getUom().getId().toString()))
		{
			errors.rejectValue("uom.id",null,"please select uom code");
		}
		
		
		// for order method - dropdown
		if(StringUtils.isEmpty(item.getOrder().getOrderId()))
		{
			errors.rejectValue("order.orderId",null,"please select order order method");
		}
		
		
		// for  note - textarea
		if(StringUtils.isEmpty(item.getNote()) || item.getNote().length()>20)
		{
			errors.rejectValue("note",null,"please enter note ,max 20 charecters allowed");
		}
		
	}

}
