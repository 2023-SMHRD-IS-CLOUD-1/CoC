package com.picstory.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
	private int user_no;
	private String user_id;
	private String user_pw;
	private String user_name;
	private String user_nick;
	private String user_mail;
	private String joined_at;

}
