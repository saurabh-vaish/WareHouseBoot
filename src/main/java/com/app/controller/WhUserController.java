package com.app.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.model.WhUser;
import com.app.service.IWhUserService;
import com.app.util.WhUserUtil;
import com.app.validator.WhUserValidator;
import com.app.view.WhUserExcelView;
import com.app.view.WhUserPdfView;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/wh")
public class WhUserController {

	@Autowired
	private IWhUserService service;

	@Autowired
	private ServletContext context;

	@Autowired
	private WhUserUtil util;

	
	@Autowired
	private WhUserValidator validator;
	
	
	
	//1-Register
	@RequestMapping("/register")
	public String showWhUserRegisterPage(ModelMap map)
	{
		// form backing object
		map.addAttribute("whUser",new WhUser());
		return "whUserRegister";
	}


	// * ajax  method for checking duplicate 
	@RequestMapping("/check")
	public @ResponseBody String checkWhUser(@RequestParam("data")String data) //@ResponseBody for ajax call
	{
		String msg="";

		List<WhUser> user = service.getAllWhUsers();
		for(WhUser u : user)
		{
			if(u.getWhCode().equals(data))
			{
				msg="User Code already exits .";
				break;
			}
			if(u.getWhEmail().equals(data))
			{
				msg="User Email already exits .";
				break;
			}
			if(u.getWhContact().equals(data))
			{
				msg="User Contact already exits .";
				break;
			}
			if(u.getWhNum().equals(data))
			{
				msg="Id Number already exits .";
				break;
			}
		}
		log.info(msg);
		return msg;
	}	

	
	
	//2-Save
	@RequestMapping(value="/save",method=RequestMethod.POST)
	public String saveWhUser(@ModelAttribute WhUser whUser,Errors errors,ModelMap map,RedirectAttributes attr)
	{
		log.info("entered into user registration");
		
		validator.validate(whUser, errors);
		
		if(!errors.hasErrors())
		{
			Integer id=service.saveWhUser(whUser);
			attr.addFlashAttribute("msg","WhUser '"+id+"'saved successfully");
			attr.addFlashAttribute("whUserId",id);

			log.info(" User Registred Successfully");
			
			return "redirect:register";
		}
		else
		{
			map.addAttribute("emsg","Enter Valid Details");

			log.info("Registration failed ! Error occured in form");
			
			return "whUserRegister";
		}
	

	}


	
	@RequestMapping("/all")
	public String showAll(ModelMap map)
	{
		map.addAttribute("list",service.getAllWhUsers());
		return "whUserData";
	}

	
	
	// show one record
	@RequestMapping("/view/{id}")
	public String showOneRecord(@PathVariable Integer id,ModelMap map)
	{
		map.addAttribute("whUser",service.getWhUserById(id));
		return "whUserView";
	}

	

	//4-Delete
	@RequestMapping("/delete{id}")
	public String deleteById(@PathVariable Integer id,RedirectAttributes map)
	{
		service.deleteWhUser(id);
		map.addFlashAttribute("msg", id+"Deleted Successfully");
		
		log.info("User Deleted Successfully");
		
		return "redirect:all";

	}


	
	//5 - get update page
	@RequestMapping("/edit/{id}")
	public String getUpdatePage(@PathVariable Integer id, ModelMap map)
	{
		//get data by id and adding to model map
		map.addAttribute("whUser",service.getWhUserById(id));
		
		
		return "whUserEdit";
	}

	
	
	// 6 -  Update 
	@RequestMapping(value="/update",method=RequestMethod.POST)
	public String updateUom(@ModelAttribute WhUser whUser,Errors errors,ModelMap map)
	{
		log.info("Entered into Update User "+whUser);

		validator.validate(whUser, errors);
		
		  if(!errors.hasErrors()) 
		  {
			  //call service
			  service.updateWhUser(whUser);
			  map.addAttribute("msg","WhUser  '"+whUser.getWhId()+"' updated successfully ");
		  }
		  else {
			  map.addAttribute("emsg","Enter Valid Details");
		  }
		 
		
		map.addAttribute("whUser",service.getWhUserById(whUser.getWhId())); // data for view page
		
		log.info("User Updated Successfully");
		
		return "whUserView";
		
	}

	

	//7. export to excel
	@RequestMapping("/excel")
	public ModelAndView getExcel(@RequestParam(value="id",required=false,defaultValue="0")Integer id) // here required is false bcs we are using same method for both
	//when we are sending id in url and when not also it it will work for both
	{
		//creating ModelAndView for sending data to excel view
		ModelAndView m = new ModelAndView();
		//setting view 
		m.setView(new WhUserExcelView());

		if(id==0)//means no id parameter with path
		{
			m.addObject("list",service.getAllWhUsers());  // so getting all rows i.e. export all
		}
		else // means id present with path
		{
			// getting data according to id
			m.addObject("list", Collections.singletonList(service.getWhUserById(id)));  // converting single row object to list
		}
		
		log.info("Excel Report Generated");
		
		return m;

	}



	// 8 . export to pdf
	@RequestMapping("/pdf")
	public ModelAndView getPdf(@RequestParam(value="id",required=false,defaultValue="0")Integer id,ModelMap map) // here required is false bcs we are using same method for both
	//when we are sending id in url and when not also it it will work for both
	{
		//creating ModelAndView for sending data to excel view
		ModelAndView m = new ModelAndView();
		//setting view 
		m.setView(new WhUserPdfView());

		if(id==0) //means no id parameter with path
		{
			m.addObject("list", service.getAllWhUsers());  // so getting all rows i.e. export all
		}
		else // means id present with path
		{
			// getting data according to id
			m.addObject("list",Collections.singletonList(service.getWhUserById(id)) );
		}

		log.info("Pdf Report Generated");
		
		return m;
	}
	

	// 9 pie and bar chart
	@RequestMapping("/chart")
	public String showCharts()
	{
		String path = context.getRealPath("/");
		List<Object[]> data = service.getWhUserTypeCount();
		util.ganeratePie(path, data);
		util.ganerateBar(path,data);
		
		log.info("Charts Generated");
		return "whUserReports";
	}

	
		
	

}
