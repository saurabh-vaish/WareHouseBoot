package com.app.validator;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.app.model.GoodRecieveNote;
import com.app.service.IGRNService;

@Component
public class GRNValidator implements Validator{

	@Autowired
	private IGRNService grnService; 

	public boolean supports(Class<?> clazz) {
		return GoodRecieveNote.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		GoodRecieveNote goodRecieveNote = (GoodRecieveNote)target;


		// checking grn code is empty or not
		if (!StringUtils.hasText(goodRecieveNote.getGrnCode().trim())) {
			errors.rejectValue("grnCode", null, "please enter code !!");
		} else if(!Pattern.matches("[A-Za-z0-9]{2,10}", goodRecieveNote.getGrnCode())){
			errors.rejectValue("grnCode", null, "no special charecters allowed and max limit is 10!!");
		} 

		// checking grn type is empty or not
		if (!StringUtils.hasText(goodRecieveNote.getGrnType().trim())) {
			errors.rejectValue("grnType", null, "please enter type !!");
		} else if(!Pattern.matches("[A-Za-z0-9]{2,10}", goodRecieveNote.getGrnCode())){
			errors.rejectValue("grnType", null, "no special charecters allowed and max limit is 10!!");
		}

		// checking grn description is empty or not
		if (!StringUtils.hasText(goodRecieveNote.getGrnDesc().trim())) {
			errors.rejectValue("grnDesc", null, "please enter description!!");
		} else if( goodRecieveNote.getGrnDesc().length()>100){
			errors.rejectValue("grnDesc", null, "max 100 charecters allowed!!");
		}

		//checking drop down empty
		if (goodRecieveNote.getPurchase()==null || goodRecieveNote.getPurchase().getPurId()==null || goodRecieveNote.getPurchase().getPurId()<=0) {
			errors.rejectValue("purchase", null, "please choose any one !");
		}


	}

}
