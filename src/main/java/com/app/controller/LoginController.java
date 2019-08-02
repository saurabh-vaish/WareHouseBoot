package com.app.controller;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.model.User;
import com.app.service.IUserService;
import com.app.util.EmailUtil;

@Controller
public class LoginController {

	@Autowired
	private IUserService service;

	@Autowired
	private EmailUtil email;
	
	private static LocalDateTime regTime;  // for otp valid time
	
	private static String otp;				

	private String uemail=null;
	
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
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

	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logoutPage (HttpServletRequest request, HttpServletResponse response)
	{
		/*
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null){   
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		 */
		return "redirect:/login?logout=true";
	}
	
	@RequestMapping("/denied")
	public String deniedPage() {
		return "AccessDenied";
	}
	
	
	
	// * ajax  method for checking duplicate emails
	@RequestMapping("/check")
	public @ResponseBody String checkEmail(@RequestParam("email")String email) //@ResponseBody for ajax call
	{
		String msg="";

		//List<User> user = service.getAllUsers(); 
		User user = service.findByUserEmail(email); 
		/*
		 * for(User u : user) { if(u.getUserEmail().equals(email)) {
		 * System.out.println("yes"); msg="Email already exits ."; break; } }
		 */
		
		if(user==null)
		{
			msg="User not found , Enter correct email.";
		}
		System.out.println("msg"+msg);
		return msg;
	}
	
	
	
	
	
	@RequestMapping("/forget")
	public String forgetPassword(@RequestParam String useremail,ModelMap map) {
		
		otp = UUID.randomUUID().toString().substring(1, 6).toUpperCase();  // 5 charecters

		String text = "Hello User ! your OTP is '"+ otp +"' It is valid for 2 minutes only";

		boolean flag= email.sendEmail(useremail,"Forget Password OTP", text); // sending email

		if(flag)
		{
			uemail=useremail;
			map.addAttribute("msg", "Check Email For OTP");
			
			regTime = LocalDateTime.now().plusMinutes(2);  // at the time of registering for otp 
		}
		else
		{
			System.out.println("Email sending failed!! Check your internet connection");
			map.addAttribute("error", "Email sending failed!! Check your internet connection");	
			
			return "userLogin";
		}
		
		return "userOtpVarify";
	}
	
	
		// OTP varification
	
		@RequestMapping(value="/otp",method=RequestMethod.POST)
		public String varifyAndSaveUser(@RequestParam String id,ModelMap map)
		{
			LocalDateTime ld = LocalDateTime.now();
			boolean b = ld.isBefore(regTime);
			
			System.out.println(regTime);
			System.out.println("ld "+ld);
			
			if(!b)   // if user takes time more than 2 minues then expired
			{
				System.out.println("expired");
				map.addAttribute("msg", " OTP Expired");
				map.addAttribute("msgOtp", "true");
				
				return "userLogin";
			}
			else 
			{
				if(id.equals(otp))
				{
					
					System.out.println("otp "+otp);
					
					String tempPass=UUID.randomUUID().toString().substring(0, 6).toUpperCase(); 
					
					String text = "Your temprary password for login . "
							+ "\n  Your user email is: " + uemail
							+ "\n  Your Password is : " + tempPass
							+ "\n  Login to enjoy services";
					
					boolean flag= email.sendEmail(uemail,"Temprary Password", text); // sending email
					
					if(!flag)
					{
						map.addAttribute("msg", "Email sending failed!! Check your internet connection");						
					}
					else {
					
					User user = service.findByUserEmail(uemail); // finding user by email
					user.setUserPwd(tempPass);  // sending temporary password
					service.updatePass(user);   // update user with that password
					
					map.addAttribute("msg","A temprary password has been send to your email ");
					}
					
				}
				else
				{
					System.out.println("invalid");
					map.addAttribute("msg","Invalid OTP");				
				}
				
			}
			
			return "UserLogin";
		}
	
}
