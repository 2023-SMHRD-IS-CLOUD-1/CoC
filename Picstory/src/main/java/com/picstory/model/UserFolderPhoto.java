package com.picstory.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFolderPhoto {
	private int f_photo_num;
	private int photo_num;
	private int folder_num;
	private String f_photo_uploaded;
}
