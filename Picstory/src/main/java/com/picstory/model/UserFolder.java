package com.picstory.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFolder {

	private int folder_num;
	private int user_num;
	private String folder_name;
	private String folder_uploaded;

}
