package com.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/")
public class WelcomeController {

	
	
	@RequestMapping("/home")
	public String showHome()
	{
		return "shared/Menubar";
	}
}
