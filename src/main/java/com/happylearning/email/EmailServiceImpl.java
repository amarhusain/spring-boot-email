/**
 * @all rights reserved -- Beat Technologies
 * @url https://www.beat.org.in
 */
package com.happylearning.email;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.happylearning.model.MailDTO;
import com.happylearning.model.OutlookMailDTO;


@Service
public class EmailServiceImpl implements EmailService{

    @Autowired
    private JavaMailSender emailSender;
	
    // Use to send mail to outlook user
    String SMTP_HOST_NAME = "mail.madarasa.com.in"; // Your organization domain
	String SMTP_AUTH_USER = "XYZUSER"; // Your network id or email
	String SMTP_AUTH_PWD = "*****"; // Your window password 

    @Override
    public void sendTextMail(MailDTO mailDTO) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);

        mimeMessageHelper.setFrom(mailDTO.getFrom());
        mimeMessageHelper.setTo(mailDTO.getTo());
        mimeMessageHelper.setSubject(mailDTO.getSubject());                 
        mimeMessageHelper.setText(mailDTO.getText());
        
        emailSender.send(message);
    }

	@Override
	public void sendTextAttachmentMail(MailDTO mailDTO, MultipartFile[] files) {
		
		MimeMessage message = emailSender.createMimeMessage();
		
		try { 
		    MimeMessageHelper helper = new MimeMessageHelper(message, true);		    
		    helper.setFrom(mailDTO.getFrom());
		    helper.setTo(mailDTO.getTo());
		    helper.setSubject(mailDTO.getSubject());
		    
		    // This mail has 2 part, the body and the attachment
	        MimeMultipart multipart = new MimeMultipart();
	     
	        BodyPart textPart = new MimeBodyPart();
	        // first part (the html)
	        String htmlText = generateHtmlText();
	        textPart.setContent(htmlText, "text/html");
	        // add it to multipart
	        multipart.addBodyPart(textPart);
	
	        /* Adding attachment to mail */ 
		    if(files != null && files.length > 0) {
		    	for(MultipartFile mfile: files) {
		    		BodyPart attachmentPart = new MimeBodyPart();
		    		try {
						addAttachment(convertMultipartFileToFile(mfile), attachmentPart);
						multipart.addBodyPart(attachmentPart);
					} catch (IOException e) {
						e.printStackTrace();
					}
		    	}
		    }	
		    
		    message.setContent(multipart);
		    
	    } catch (MessagingException e) {
			e.printStackTrace();
		}
	    emailSender.send(message);		
	}
	
	@Override
	public void sendTextInlineImageAttachmentMail( MailDTO mailDTO, MultipartFile[] inlineImages, MultipartFile[] attachments) {
		
		
		
		MimeMessage message = emailSender.createMimeMessage();
		// add image to the multipart
        try {
        	
        	 MimeMessageHelper helper = new MimeMessageHelper(message, true);		    
 		    helper.setFrom(mailDTO.getFrom());
 		    helper.setTo(mailDTO.getTo());
 		    helper.setSubject(mailDTO.getSubject());
        	
			 // This mail has 2 part, the BODY and the embedded image
	        MimeMultipart multipart = new MimeMultipart();
	
	        // first part (the html)
	        BodyPart textPart = new MimeBodyPart();
	        String htmlText = generateHtmlText(inlineImages.length);
	        textPart.setContent(htmlText, "text/html");
	        // add it
	        multipart.addBodyPart(textPart);
	
	        /* Add image into mail body */	       	        
	        if(inlineImages != null && inlineImages.length > 0) {
		    	for(int i=0; i<inlineImages.length; i++) {
		    		try {	
		    			 BodyPart inlineImgPart = new MimeBodyPart();
		    			 addImage(convertMultipartFileToFile(inlineImages[i]), i, inlineImgPart);
		    			 // Add inline image to multipart
		    			 multipart.addBodyPart(inlineImgPart);
					} catch (IOException e) {
						e.printStackTrace();
					}
		    	}
		    }	       
	        
	        // Add Attachment into mail body      
	        if(attachments != null && attachments.length > 0) {
	        	for(MultipartFile multipartFile: attachments) {
	        		 BodyPart attachmentPart = new MimeBodyPart();
	        		 try {
	        			 addAttachment(convertMultipartFileToFile(multipartFile), attachmentPart);
	        			// Add attachment to multipart
	        			 multipart.addBodyPart(attachmentPart);
	        		 }catch(IOException e) {
	        			 e.printStackTrace();
	        		 }
	        	}
	        }	
	        
			// put everything together
	        message.setContent(multipart);
	        
	       
	        
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        emailSender.send(message);
		
        
	}
	
    @Override
    public void sendMailToOutlook(OutlookMailDTO outlookMailDTO) throws MessagingException {
    	
    	this.SMTP_AUTH_USER = outlookMailDTO.getUsername();
		this.SMTP_AUTH_PWD = outlookMailDTO.getPassword();
		Properties props = new Properties();
		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.put("mail.smtp.starttls.enable", "true");
		
		Session session = null;
		if(this.SMTP_AUTH_USER != null && !this.SMTP_AUTH_USER.equals("")) {
			props.put("mail.smtp.auth", "true");
			Authenticator auth = new SMTPAuthenticator();
			session = Session.getInstance(props, auth);
		}else {
			session = Session.getInstance(props);
		}
		
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);

        mimeMessageHelper.setFrom(outlookMailDTO.getTo());
        mimeMessageHelper.setSubject(outlookMailDTO.getSubject());                 
        mimeMessageHelper.setText(outlookMailDTO.getText());
        
        emailSender.send(message);
    }

	private void addAttachment(File file, BodyPart bodyPart) {
		  String fileName = file.getName();
		  try {
			  DataSource fds = new FileDataSource(file);
			  bodyPart.setDataHandler(new DataHandler(fds));
			  bodyPart.setFileName(fileName);
			  /**
			   * Below line Tells to Mail API to Add Documents as Mail Attachment
			   */
			  bodyPart.setDisposition(MimeBodyPart.ATTACHMENT);
		    System.out.println("Added a file atachment: {}" +fileName);
		  } catch (MessagingException e) {
			System.out.println("Failed to add a file atachment: {}" +fileName);
			e.printStackTrace();
		  }
	}
	
	private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
		File file = new File(multipartFile.getOriginalFilename());
		file.createNewFile();
		try(FileOutputStream fos = new FileOutputStream(file)){
			fos.write(multipartFile.getBytes());
			fos.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}		
		return file;
	}
	
	private class SMTPAuthenticator extends javax.mail.Authenticator{
		public PasswordAuthentication getPasswordAuthentication() {
			String username = SMTP_AUTH_USER;
			String password = SMTP_AUTH_PWD;
			return new PasswordAuthentication(username, password);
		}
	}
	
	private void addImage(File file, int i, BodyPart bodyPart) {
		  String fileName = file.getName();
		  try {
			  DataSource fds = new FileDataSource(file);
			  bodyPart.setDataHandler(new DataHandler(fds));
		      bodyPart.setHeader("Content-ID", "<image"+i+">");
		      /**
			   * Below line Tells to Mail API to Add Documents as inline part into message
			   */
		      bodyPart.setDisposition(MimeBodyPart.INLINE);
		    System.out.println("Added inline image: {}" +fileName);
		  } catch (MessagingException ex) {
			System.out.println("Failed to add inline image: {}" +fileName);
		  }
	}
	
	private String generateHtmlText(int noOfInlineImages) {
		String timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm aa").format(Calendar.getInstance().getTime());
		
		return "<p style=\"font-size: 22px; margin: 0;\">Greetings,</p>"
				
				 + "<br>"
				 
        + "<p style=\"font-size: 22px; margin-top: 0;\"> Welcome to Madarasa Educational Portal.</p>"
		
        + "<br>"
        
        + "<div> "+getImgCid(noOfInlineImages) +" </div>"
        
		+ "<br>"
        
        + "<p style=\"font-size: 22px; margin-bottom: 0;\"> Explore Lots of Courses. </p>"
        
        + "<br>"
        
        + "<p style=\"font-size: 22px; margin-top: 0;\"> Developed By Madarasa Team</p>"
        
		+ "<br>"
        + "<p style=\"font-size: 16px;\"> Click here to learn more.  <a href= \"http://www.madarasa.com.in\"> Click here </a> .";
	}

	private String getImgCid(int noOfInlineImages) {

		String imgCid = "";
		for(int i=0; i<noOfInlineImages; i++) {
			imgCid += "<img src=\"cid:image"+ i +" \"><br>";
		}
		return imgCid;
	}
	
	private String generateHtmlText() {		
		return "<p style=\"font-size: 22px; margin: 0;\">Greetings,</p>"				
				 + "<br>"				 
        + "<p style=\"font-size: 22px; margin-top: 0;\"> Welcome to Madarasa Educational Portal.</p>"		
        + "<br>"        
        + "<p style=\"font-size: 22px; margin-bottom: 0;\"> Explore Lots of Courses. </p>"        
        + "<br>"        
        + "<p style=\"font-size: 22px; margin-top: 0;\"> Developed By Madarasa Team</p>"        
		+ "<br>"
        + "<p style=\"font-size: 16px;\"> Click here to learn more.  <a href= \"http://www.madarasa.com.in\"> Click here </a> .";
	}

	/*private void addAttachment(File file, BodyPart messageBodyPart) {
		  String fileName = file.getName();
		  try {
			  DataSource fds = new FileDataSource(file);
			  messageBodyPart.setDataHandler(new DataHandler(fds));
			  messageBodyPart.setFileName(fileName);
			  messageBodyPart.setDisposition(MimeBodyPart.ATTACHMENT);
		    System.out.println("Added a file atachment: {}" +fileName);
		  } catch (MessagingException ex) {
			System.out.println("Failed to add a file atachment: {}" +fileName);
		  }
	}*/

	/*private void addImage(File file, int i, BodyPart messageBodyPart) {
		  String fileName = file.getName();
		  try {
			  DataSource fds = new FileDataSource(file);
			  messageBodyPart.setDataHandler(new DataHandler(fds));
		      messageBodyPart.setHeader("Content-ID", "<image"+i+">");
		      messageBodyPart.setDisposition(MimeBodyPart.INLINE);
		    System.out.println("Added a file atachment: {}" +fileName);
		  } catch (MessagingException ex) {
			System.out.println("Failed to add a file atachment: {}" +fileName);
		  }
	}*/
	/*private String generateHtmlText(int noOfImages) {
		return "<!DOCTYPE html> <head><style></style></head> <body> "
				+ ""
				+ ""
				+ ""
				+ "<div>" + getImgCid(noOfImages)+"</div>"
				+ ""
				+ ""
				+ "</body></html>";
				
	}

	private String getImgCid(int num) {
		String img = "";
		for(int i=1; i<=num; i++) {
			img += "<img src=\"cid:image" + i + "\">";
		}
		return img;
	}*/
}
