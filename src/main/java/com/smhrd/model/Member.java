package com.smhrd.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // getter, setter
@AllArgsConstructor // id, pw, name 매개변수로 받는 생성자 생성
@NoArgsConstructor
public class Member {
	
	private String id;
	private String pw;
	private String nick;
	
}
