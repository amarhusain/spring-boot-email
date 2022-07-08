package com.happylearning.controller;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.happylearning.email.EmailService;
import com.happylearning.model.MailDTO;

@RestController
public class EmailController {
	
	@Autowired
	private EmailService emailService;

	@Autowired
	  private ObjectMapper mapper;
	
	@PostMapping(value = "/text-mail")
	public ResponseEntity<Boolean> sendTextMail(@RequestBody MailDTO mailDTO) 
				throws MessagingException, JsonMappingException, JsonProcessingException {

		emailService.sendTextMail(mailDTO);		
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);		
	}
	
	@PostMapping(value = "/text-attachment-mail", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Boolean> sendTextAttachmentMail( 
			@RequestParam String emailData,
			@RequestParam("attachments") MultipartFile[]  attachments) 
				throws MessagingException, JsonMappingException, JsonProcessingException {
		
		MailDTO mailDTO = mapper.readValue(emailData, MailDTO.class);
		emailService.sendTextAttachmentMail(mailDTO, attachments);
		
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);		
	}

	@PostMapping(value = "/text-inlineimage-attachment-mail", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Boolean> sendTextInlineImageAttachmentMail( 
			@RequestParam String emailData,
			@RequestParam("inlineImages") MultipartFile[]  inlineImages,
			@RequestParam("attachments") MultipartFile[]  attachments) 
				throws MessagingException, JsonMappingException, JsonProcessingException {
		
		MailDTO mailDTO = mapper.readValue(emailData, MailDTO.class);
		emailService.sendTextInlineImageAttachmentMail(mailDTO, inlineImages, attachments);
		
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		
	}

}
