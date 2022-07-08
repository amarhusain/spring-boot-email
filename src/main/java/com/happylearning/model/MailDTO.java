package com.happylearning.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailDTO {
	
	private String to;
	private String from;
	private String subject;
	private String text;
}
