package com.happylearning.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OutlookMailDTO {
	/** 
	 * The person who is sending the mail
	 */
	private String username;
	/** 
	 * window password of the person who is sending the mail
	 */
	private String password;
	/** 
	 * receiver of the the mail
	 */
	private String to;
	private String subject;
	private String text;
}
