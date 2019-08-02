package com.app.validator;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.app.model.Uom;
import com.app.service.IUomService;

@Component
public class UomValidator implements Validator{

	
	@Autowired
	private IUomService service;
	
	@Override
	public boolean supports(Class<?> clazz) {
		// so that only this class can be validate
		return Uom.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		// downcauoming to Uom
		
		Uom uom = (Uom) target;
		
		
		/***  validation uom ***/
		
		
		/*** Dropdown -- Uom Type ***/
		
		if(StringUtils.isEmpty(uom.getUomType()))
		{
			errors.rejectValue("uomType",null ,"please select one type");
		}
		
		
		/*** Text Input -- uomCode ***/

		if(!Pattern.matches("[A-Z0-9]{2,10}", uom.getUomCode()))
		{
			errors.rejectValue("uomCode",null ,"please enter min 2 max 6 chars in Uppercase ");
		} 
		
		

		/*** radio button-- enable Uom ***/
		
		if(StringUtils.isEmpty(uom.getEnableUom()))
		{
			errors.rejectValue("enableUom",null ,"please choose one option");
		}
		

		
		/*** Text Input -- Meta Code ***/
		
		if(!Pattern.matches("[A-Z0-9a-z]{2,10}", uom.getMetaCode()))
		{
			errors.rejectValue("metaCode",null ,"please enter min 2 max 6 chars ");
		}
	

		/*** radio button-- adjust size ***/
		
		if(StringUtils.isEmpty(uom.getAdjSize()))
		{
			errors.rejectValue("adjSize",null ,"please choose one option");
		}
		
		
		
		/*** Text Input -- note ***/

		if(StringUtils.isEmpty(uom.getNote()) || uom.getNote().length()>20)
		{
			errors.rejectValue("note",null ,"please enter note ,max 20 charecters allowed");
		}
	
	}
 
}
