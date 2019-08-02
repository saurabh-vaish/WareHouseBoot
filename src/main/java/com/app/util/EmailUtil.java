package com.app.util;

import javax.mail.internet.MimeMessage;

import org.hibernate.pretty.MessageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Component
public class EmailUtil {

	
	@Autowired
	private JavaMailSenderImpl sender;
	
	public boolean sendEmail(String to,String subject,String cc[],String bcc[],String text,CommonsMultipartFile file)
	{
		
		boolean flag;
		
		try {
			
				MimeMessage message	= sender.createMimeMessage();
				
				MimeMessageHelper helper =  new  MimeMessageHelper(message, file!=null?true:false);
				
				helper.setFrom("srvjava93@gmail.com");
				helper.setTo(to);
				helper.setSubject(subject);
				helper.setText(text);
				
				//set CC and BCC
				if(cc!=null && cc.length>0)
					helper.setCc(cc);
				if(bcc!=null && bcc.length>0)
					helper.setBcc(bcc);
				
				//add attachment
				if(file!=null)
					helper.addAttachment(file.getOriginalFilename(), file);
				else
					System.out.println("No Attachment found");
				
				//send email
				sender.send(message);
				flag=true;
		
		} catch (Exception e) {
				flag=false;
				e.printStackTrace();
			}
			
			return flag;
	}
	
	
	//send email without attachment
		public boolean sendEmail(String to,String subject,String text) {
			return sendEmail(to, subject,null,null, text, null);
		}
		
		//send email without subject
		public boolean sendEmail(String to,String text) {
			return sendEmail(to, "NO SUBJECT",null,null, text, null);
		}
	
}
