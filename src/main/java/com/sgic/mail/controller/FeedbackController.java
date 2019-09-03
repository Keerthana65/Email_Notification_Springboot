package com.sgic.mail.controller;

import javax.validation.ValidationException;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sgic.mail.feedback.EmailCfg;
import com.sgic.mail.feedback.Feedback;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

	@SuppressWarnings("unused")
	private EmailCfg emailCfg;
	
	public FeedbackController(EmailCfg emailCfg) {
		this.emailCfg=emailCfg;
	}
	
	
	@PostMapping
	public void sendFeedback(@RequestBody Feedback feedback, 
			BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			throw new ValidationException("Feedback is not valid");
		}
		
		// Create a mail Sender
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(this.emailCfg.getHost());
		mailSender.setPort(this.emailCfg.getPort());
		mailSender.setUsername(this.emailCfg.getUsername());
		mailSender.setPassword(this.emailCfg.getPassword());
		
		// Create an email instance 
		SimpleMailMessage mailMessage = new SimpleMailMessage();
//		mailMessage.setFrom("geerthi65@gmail.com");
		mailMessage.setTo(feedback.getEmail());
		mailMessage.setSubject("New Feedback from"+ feedback.getName());
		mailMessage.setText(feedback.getFeedback());
		
		// Send Mail
		mailSender.send(mailMessage);
	}
}
