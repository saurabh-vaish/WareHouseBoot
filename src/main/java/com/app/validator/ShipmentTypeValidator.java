package com.app.validator;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.app.model.ShipmentType;

@Component
public class ShipmentTypeValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		// so that only this class can be validate
		return ShipmentType.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		// downcasting to ShipmentType
		
		ShipmentType st = (ShipmentType) target;
		
		
		/***  validation start ***/
		
		
		/*** Dropdown -- ShipmentMode ***/
		
		/// if(st.getShipmentMode()errors==null || st.getShipmentMode().equals(""))
		// or By using StringUtils class Of Spring Framework
		
		if(StringUtils.isEmpty(st.getShipmentMode()))
		{
			errors.rejectValue("shipmentMode",null ,"please choose one mode");
		}
		
		
		/*** Text Input -- ShipmentCode ***/

		if(!Pattern.matches("[A-Z0-9]{2,20}", st.getShipmentCode()))
		{
			errors.rejectValue("shipmentCode",null ,"please enter min 2 max 20 chars in Uppercase");
		}
	
	
		/*** Dropdown -- enable Shipment ***/
		
		if(StringUtils.isEmpty(st.getEnableShipment()))
		{
			errors.rejectValue("enableShipment",null ,"please choose one option");
		}
		
		
		
		/*** radio button-- shipment grade ***/
		
		if(StringUtils.isEmpty(st.getShipmentGrade()))
		{
			errors.rejectValue("shipmentGrade",null ,"please select one grade");
		}
		
		
		/*** Text Input -- note ***/

		if(StringUtils.isEmpty(st.getNote()) || st.getNote().length()>20)
		{
			errors.rejectValue("note",null ,"please enter note ,max 20 charecters allowed");
		}
	
	}
 
}
