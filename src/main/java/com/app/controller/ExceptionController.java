package com.app.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.app.excetion.DocumentNotFoundException;
import com.app.excetion.ShipmentTypeException;

@ControllerAdvice
public class ExceptionController {


	@ExceptionHandler(value = ShipmentTypeException.class)
	public String ShipmentTypeNotFoundException(Model map)
	{
		map.addAttribute("errorMsg","ShipmentType is not found , Please contact Administrator !!!");
		return "AccessDenied";
	}
	
	
	@ExceptionHandler(value = DocumentNotFoundException.class)
	public String DocumentNotFoundException(Model map)
	{
		map.addAttribute("errorMsg","Document is not found , Please contact Administrator !!!");
		return "AccessDenied";
	}
	
}
