/**
 * @all rights reserved -- Beat Technologies
 * @url https://www.beatsoftware.in
 */
package com.happylearning.email;

import javax.mail.MessagingException;

import org.springframework.web.multipart.MultipartFile;

import com.happylearning.model.MailDTO;
import com.happylearning.model.OutlookMailDTO;

public interface EmailService {

	 public void sendTextMail(MailDTO mailDTO) throws MessagingException;
	 
	 public void sendTextAttachmentMail( MailDTO mailDTO, MultipartFile[] files);
	 
	 public void sendTextInlineImageAttachmentMail( MailDTO mailDTO, MultipartFile[] inlineImages, MultipartFile[] attachments);
	 
	 public void sendMailToOutlook(OutlookMailDTO outlookMailDTO) throws MessagingException;
}
