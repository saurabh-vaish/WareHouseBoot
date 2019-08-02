//package com.app.controller;
//
//import static org.springframework.web.bind.annotation.RequestMethod.POST;
//
//import java.time.LocalDateTime;
//import java.util.Collections;
//import java.util.List;
//import java.util.UUID;
//
//import javax.servlet.ServletContext;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.validation.Errors;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.servlet.ModelAndView;
//
//import com.app.model.User;
//import com.app.service.IUserService;
//import com.app.util.EmailUtil;
//import com.app.util.UserUtil;
//import com.app.validator.UserValidator;
//import com.app.view.UserExcelView;
//import com.app.view.UserPdfView;
//
//@Controller
//@RequestMapping("/userr")
//public class UserController {
//
//	@Autowired
//	private IUserService service;
//	
//	@Autowired
//	private UserValidator validator;
//
//	@Autowired
//	private ServletContext servletContext;
//	
//	@Autowired
//	private UserUtil userUtil;
//	
//	@Autowired
//	private EmailUtil email;
//	
//	private static LocalDateTime regTime;  // for otp valid time
//	
//	private static String otp;				
//
//	private User userOb;
//	
//	//1-Register
//	@RequestMapping("/register")
//	public String showUserRegisterPage(ModelMap map)
//	{
//		// form backing object
//		map.addAttribute("user",new User());
//		
//		return "userRegister";
//	}
//
//	
//		// * ajax  method for checking duplicate emails
//		@RequestMapping("/check")
//		public @ResponseBody String checkEmail(@RequestParam("email")String email) //@ResponseBody for ajax call
//		{
//			String msg="";
//			
//			  List<User> user = service.getAllUsers(); 
//			  for(User u : user)
//			  {
//				  if(u.getUserEmail().equals(email))
//				  {
//					  System.out.println("yes");
//					  msg="Email already exits .";
//					  break;
//				  }
//			  }
//			 System.out.println("msg"+msg);
//			  return msg;
//		}
//		
//		// * ajax  method for checking duplicate numbers
//		@RequestMapping("/checkM")
//		public @ResponseBody String checkMobile(@RequestParam("mobile")String num) //@ResponseBody for ajax call
//		{
//			String msg="";
//
//			List<User> user = service.getAllUsers(); 
//			for(User u : user)
//			{
//				if(u.getUserMobile().equals(num))
//				{
//					System.out.println("yes");
//					msg="Number already exits .";
//					break;
//				}
//			}
//			System.out.println(msg);
//			return msg;
//		}	
//
//	
//
//		//2-Save
//		@RequestMapping(value="/save",method=POST)
//		public String sendMailUser(@ModelAttribute User user,Errors errors,ModelMap map)
//		{
//			validator.validate(user, errors);
//
//			if(errors.hasErrors())
//			{
//				map.addAttribute("msg","Enter Valid User details");
//				return "userRegister";
//			}
//			else
//			{
//				userOb = user;					// string user data 
//
//				System.out.println(userOb);
//				otp = UUID.randomUUID().toString().substring(1, 6).toUpperCase();  // 5 charecters
//
//				String text = "Hello '"+user.getUserName()+"' your OTP is '"+ otp +"' It is valid for 2 minutes only";
//
//				boolean flag= email.sendEmail(user.getUserEmail(),"User Registration OTP", text); // sending email
//
//				if(flag)
//				{
//					map.addAttribute("msg", "Check Email For OTP");
//				}
//				else
//				{
//					System.out.println("Email sending failed!! Check your internet connection");
//					map.addAttribute("msg", "Email sending failed!! Check your internet connection");	
//					
//					return "userRegister";
//				}
//
//
//				regTime = LocalDateTime.now().plusMinutes(2);  // at the time of registering for otp 
//
//				System.out.println(otp);
//				//	System.out.println(regTime);
//
//				
//				return "userOtpVarify";
//			}
//
//		}
//
//
//			
//	// OTP varification
//			
//	@RequestMapping(value="/otp",method=RequestMethod.POST)
//	public String varifyAndSaveUser(@RequestParam String id,ModelMap map)
//	{
//		LocalDateTime ld = LocalDateTime.now();
//		boolean b = ld.isBefore(regTime);
//		
//		System.out.println(regTime);
//		System.out.println("ld "+ld);
//		
//		if(!b)   // if user takes time more than 2 minues then expired
//		{
//			System.out.println("expired");
//			map.addAttribute("msg", " OTP Expired");
//			
//			
//			return "redirect:/userr/register";
//		}
//		else 
//		{
//			if(id.equals(otp))
//			{
//				
//				System.out.println("otp "+otp);
//				
//				System.out.println(userOb);
//				String pass = userOb.getUserPwd();
//				Integer pid=service.saveUser(userOb);
//				map.addAttribute("msg",userOb.getUserName()+"' Registered successfully . Login To continue");
//				
//				
//				// sending mail again 
//				
//				String text = "Welcome '"+userOb.getUserName()+"' you have successfully registered . "
//						+ "\n  Your user name is: " + userOb.getUserEmail()
//						+ "\n  Your Password is : " + pass
//						+ "\n  Login to enjoy services";
//				
//				boolean flag= email.sendEmail(userOb.getUserEmail(),"Welcome to App", text); // sending email
//				
//				if(!flag)
//				{
//					System.out.println("Email sending failed!! Check your internet connection");
//					map.addAttribute("msg", "Email sending failed!! Check your internet connection");						
//				}
//				
//				
//				
//			}
//			else
//			{
//				System.out.println("invalid");
//				map.addAttribute("msg","Invalid OTP");				
//			}
//			
//		}
//		
//		return "redirect:/userr/register";
//	}
//	
//
//	@RequestMapping("/change")
//	public String changePassPage(ModelMap map)
//	{
//		
//		//map.addAttribute("list",service.getAllUsers());
//		return "userChangePassword";
//	}
//
//	
//	@RequestMapping(value="/updatePass",method=POST)
//	public String UpdatePassword(ModelMap map,@RequestParam String npass,@RequestParam String cpass)
//	{
//		if(!npass.equals(cpass))
//		{
//			map.addAttribute("msg","passwords do not match");
//		}
//		else
//		{
//			SecurityContext securityContext = SecurityContextHolder.getContext();
//			String username=null;
//			if(null != securityContext.getAuthentication()){
//				username = securityContext.getAuthentication().getName();
//
//				User user = service.findByUserEmail(username);
//				user.setUserPwd(npass);
//				
//				service.updatePass(user);
//				
//				map.addAttribute("msg","Your password has been updated successfully");
//				
//				
//			}
//			
//			System.out.println(username);
//		}
//		return "userChangePassword";
//	}
//	
//	
//	@RequestMapping("/all")
//	public String showAll(ModelMap map)
//	{
//		map.addAttribute("list",service.getAllUsers());
//		return "userData";
//	}
//
//
//	// show one record
//	@RequestMapping("/view")
//	public String showOneRecord(@RequestParam Integer id,ModelMap map)
//	{
//		map.addAttribute("user",service.getUserById(id));
//		return "UserView";
//	}
//
//
//	//4-Delete
//	@RequestMapping("/delete")
//	public String deleteById(@RequestParam("id")Integer id,ModelMap map)
//	{
//		service.deleteUser(id);
//		map.addAttribute("list",service.getAllUsers());
//		map.addAttribute("msg", "User "+ id+"Deleted Successfully");
//		return "userData";
//
//	}
//
//
//	// 6 -  Update 
//	@RequestMapping(value="/update",method=POST)
//	public String updateUom(@ModelAttribute User user,ModelMap map)
//	{
//			service.updateUser(user);
//			map.addAttribute("msg", "user '"+user.getUserId()+"' updated successfully ");
//
//			map.addAttribute("data",service.getUserById(user.getUserId())); // data for user view page
//			
//		return "UserView";
//		
//	}
//
//
//	//7. export to excel
//	@RequestMapping("/excel")
//	public ModelAndView getExcel(@RequestParam(value="id",required=false,defaultValue="0")Integer id) // here required is false bcs we are using same method for both
//	//when we are sending id in url and when not also it it will work for both
//	{
//		//creating ModelAndView for sending data to excel view
//		ModelAndView m = new ModelAndView();
//		//setting view 
//		m.setView(new UserExcelView());
//
//		if(id==0)//means no id parameter with path
//		{
//			m.addObject("list",service.getAllUsers());  // so getting all rows i.e. export all
//		}
//		else // means id present with path
//		{
//			// getting data according to id
//			m.addObject("list", Collections.singletonList(service.getUserById(id)));  // converting single row object to list
//		}
//
//		return m;
//
//	}
//
//
//
//	// 8 . export to pdf
//	@RequestMapping("/pdf")
//	public ModelAndView getPdf(@RequestParam(value="id",required=false,defaultValue="0")Integer id,ModelMap map) // here required is false bcs we are using same method for both
//	//when we are sending id in url and when not also it it will work for both
//	{
//		//creating ModelAndView for sending data to excel view
//		ModelAndView m = new ModelAndView();
//		//setting view 
//		m.setView(new UserPdfView());
//
//		if(id==0) //means no id parameter with path
//		{
//			m.addObject("list", service.getAllUsers());  // so getting all rows i.e. export all
//		}
//		else // means id present with path
//		{
//			// getting data according to id
//			m.addObject("list",Collections.singletonList(service.getUserById(id)) );
//		}
//
//		return m;
//	}
//
//
//	//10.generate chart
//		@RequestMapping("/report")
//		public String generateChart() {
//
//			String path = servletContext.getRealPath("/");
//			List<Object[]> users=service.getUsersCount();
//			userUtil.generatePieChart(path, users);
//			userUtil.generateBarChart(path, users);
//			return "UserReport";
//		}
//
//}
