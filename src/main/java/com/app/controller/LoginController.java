package com.app.controller;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.model.User;
import com.app.service.IUserService;
import com.app.util.EmailUtil;

import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
public class LoginController {

	@Autowired
	private IUserService service;

	@Autowired
	private EmailUtil email;
	
	private static LocalDateTime regTime;  // for otp valid time
	
	private static String otp;				

	private String uemail=null;
	
	

	@GetMapping("/home")
	public String showHome()
	{
		return "shared/Menubar";
	}
	
	
	@GetMapping(value = "/login")
	public String loginPage(@RequestParam(value = "error", required = false) String error,
							@RequestParam(value = "logout", required = false) String logout,Model model)
	{
		String errorMessge = null;
		if(error != null) {
			errorMessge = "Username or Password is incorrect !!";
		}
		if(logout != null) {
			errorMessge = "You have been successfully logged out !!";
		}
		
		model.addAttribute("errorMessge", errorMessge);
		return "UserLogin";
	}
	
	

	@GetMapping(value="/logout")
	public String logoutPage (HttpServletRequest request, HttpServletResponse response)
	{
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null){   
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		
		return "redirect:/login?logout=true";
	}
	
	@GetMapping("/denied")
	public String deniedPage() {
		
		return "AccessDenied";
	}
	
	
	
	// * ajax  method for checking duplicate emails
	@GetMapping("/check")
	public @ResponseBody String checkEmail(@RequestParam("email")String email) //@ResponseBody for ajax call
	{
		String msg="";

		User user = service.findByUserEmail(email); 
		
		if(user==null)
		{
			msg="User not found , Enter correct email.";
		}
		
		log.info("msg");
		return msg;
	}
	
	
	
	@GetMapping("/forget")
	public String forgetPassword(@RequestParam String useremail,ModelMap map) {
		
		otp = UUID.randomUUID().toString().substring(1, 6).toUpperCase();  // 5 charecters

		String text = "Hello User ! your OTP is '"+ otp +"' It is valid for 2 minutes only";

		boolean flag= email.sendEmail(useremail,"Forget Password OTP", text); // sending email

		if(flag)
		{
			uemail=useremail;
			map.addAttribute("msg", "Check Email For OTP");
			
			log.info("emil sent for otp");
			
			regTime = LocalDateTime.now().plusMinutes(2);  // at the time of registering for otp 
		}
		else
		{
			log.info("Email sending failed!! ");
			
			map.addAttribute("error", "Email sending failed!! Check your internet connection");	
			
			return "userLogin";
		}
		
		return "userOtpVarify";
	}
	
	
		// OTP varification
	
		@PostMapping(value="/otp")
		public String varifyAndSaveUser(@RequestParam String id,ModelMap map)
		{
			LocalDateTime ld = LocalDateTime.now();
			boolean b = ld.isBefore(regTime);
			
			if(!b)   // if user takes time more than 2 minues then expired
			{
				map.addAttribute("msg", " OTP Expired");
				map.addAttribute("msgOtp", "true");
			
				
				return "userLogin";
			}
			else 
			{
				if(id.equals(otp))
				{
					
				
					String tempPass=UUID.randomUUID().toString().substring(0, 6).toUpperCase(); 
					
					String text = "Your temprary password for login . "
							+ "\n  Your user email is: " + uemail
							+ "\n  Your Password is : " + tempPass
							+ "\n  Login to enjoy services";
					
					boolean flag= email.sendEmail(uemail,"Temprary Password", text); // sending email
					
					if(!flag)
					{
						map.addAttribute("msg", "Email sending failed!! Check your internet connection");	
						
						log.info("email failed for temporary password");
					}
					else {
					
						User user = service.findByUserEmail(uemail); // finding user by email
						user.setUserPwd(tempPass);  // sending temporary password
						service.updatePass(user);   // update user with that password
						
						map.addAttribute("msg","A temprary password has been send to your email ");
						
						log.info("Temparary password send to email");
					}
					
				}
				else
				{
					map.addAttribute("msg","Invalid OTP");				
				}
				
			}
			
			return "UserLogin";
		}
	
}
