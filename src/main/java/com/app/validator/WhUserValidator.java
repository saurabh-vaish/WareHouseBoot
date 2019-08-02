package com.app.validator;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.app.model.WhUser;

@Component
public class WhUserValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		// so that only this class can be validate
		return WhUser.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		// downcasting to WhUser
		
		WhUser wh = (WhUser) target;
		
		
		/***  validation start ***/
		
		
		String patternEmail ="^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
		String phone = "(0|91)?[6-9][0-9]{9}";
		String aadhar = "^\\d{4}\\s\\d{4}\\s\\d{4}$";
		String pancard ="^([a-zA-Z]){5}([0-9]){4}([a-zA-Z]){1}?$";
		String voter = "[A-Z]{3}[0-9]{7}";
		
		/*** radio button-- whType ***/
		
		if(StringUtils.isEmpty(wh.getWhType()))
		{
			errors.rejectValue("whType",null ,"please select one type");
		}
		
		
		/*** Text Input -- whCode ***/
		
		if(!Pattern.matches("[A-Z0-9a-z\\s]{2,20}", wh.getWhCode()))
		{
			errors.rejectValue("whCode",null ,"please enter min 2 max 10 chars ");
		}
		
		/*** Text Input -- whName ***/
		
		if(!Pattern.matches("[A-Z0-9a-z\\s]{2,20}", wh.getWhName()))
		{
			errors.rejectValue("whName",null ,"please enter min 2 max 20 chars , no Special chars allowed ");
		}

		
		/*** Dropdown -- whFor ***/
		
		
		if(StringUtils.isEmpty(wh.getWhFor()))
		{
			errors.rejectValue("whFor",null ,"please choose one option");
		}
		
	
		/*** Text Input -- Email ***/

		if(!Pattern.matches(patternEmail,wh.getWhEmail()))
		{
			errors.rejectValue("whEmail",null ,"please enter valid email");
		}
		
		
		/*** Text Input --Contact ***/
		
		if(!Pattern.matches(phone, wh.getWhContact()))
		{
			errors.rejectValue("whContact",null ,"please enter valid mobile number");
		}

		
		
		/*** Dropdown -- whIdType ***/
		
		if(StringUtils.isEmpty(wh.getWhIdType()))
		{
			errors.rejectValue("whIdType",null ,"please choose one Id");
		}
		
		
		
		/*** Text Input -- note ***/

		if(StringUtils.isEmpty(wh.getWhNum()) )
		{
			errors.rejectValue("whNum",null ,"enter valid Id");
		}
		else if(wh.getWhIdType().equals("Aadhar") && !Pattern.matches(aadhar, wh.getWhNum()))
		{
			errors.rejectValue("whNum",null ,"enter valid aadhar card number ex:-1234 1234 1234");			
		}
		else if(wh.getWhIdType().equals("Pancard") && !Pattern.matches(pancard, wh.getWhNum()))
		{
			errors.rejectValue("whNum",null ,"enter valid pan card number ex:-JKLMX7854N");			
		}
		else if(wh.getWhIdType().equals("VoterId") && !Pattern.matches(voter, wh.getWhNum()))
		{
			errors.rejectValue("whNum",null ,"enter valid voter id card number ex:-IGQ1234567");			
		}
	
	}
 
}
