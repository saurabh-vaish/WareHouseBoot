package com.app.validator;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.app.model.User;
import com.app.service.IUserService;

@Component
public class UserValidator implements Validator{

	@Autowired
	private IUserService userService;

	
	@Override
	public boolean supports(Class<?> clazz) {
		// so that only this class can be validate
		return User.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		// downcasting to User
		
		User user = (User) target;
		
		
		/***  validation user ***/
		
		String patternEmail ="^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
		String patternPwd = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,}";
		String phone = "(0|91)?[6-9][0-9]{9}";
		
		
		/*** Text Input -- Name ***/

		if(!Pattern.matches("[A-Za-z ]{2,20}", user.getUserName()))
		{
			errors.rejectValue("userName",null ,"please enter valid name");
		}
		
		
		/*** Text Input -- Email ***/
		
		if(!Pattern.matches(patternEmail, user.getUserEmail()))
		{
			errors.rejectValue("userEmail",null ,"please enter valid email");
		}

		
		/*** Text Input -- Password ***/
		
		if(user.getUserPwd()!=null && !Pattern.matches(patternPwd,user.getUserPwd()))
		{
			errors.rejectValue("userPwd",null ,"password should contain at least one number, one lowercase and one uppercase letter");
		}
		else if(user.getUserPwd()!=null && user.getUserPwd().length()<6)
		{
			errors.rejectValue("userPwd",null ,"pasword should cotain at least 6 charectors");			
		}
		
		
		//check user type (radio button) is empty or not
		if (StringUtils.isEmpty(user.getGender())) {
			errors.rejectValue("gender", null, "please select choose any one !");
		}

		
		
		/*** Text Input --Contact ***/
		
		if(!Pattern.matches(phone, user.getUserMobile()))
		{
			errors.rejectValue("userMobile",null ,"please enter valid mobile number");
		}
		else if (userService.isEmailOrMobileExist("userMobile", user.getUserMobile()))
		{
			errors.rejectValue("userMobile", null, "mobile number already exist !");
		}
		
		
		
		/*** checkbox --  Order Accept ***/
		
		if(user.getRoles().isEmpty() || user.getRoles()==null)
		{
			errors.rejectValue("roles",null ,"choose at least one role");
		}
		
	
	}
 
}
