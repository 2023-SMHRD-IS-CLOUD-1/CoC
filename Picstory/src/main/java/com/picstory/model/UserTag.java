package com.picstory.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTag {

	private int user_tag_num;
	private int user_num;
	private String user_tag_name;
	private String user_tag_uploaded;
}
