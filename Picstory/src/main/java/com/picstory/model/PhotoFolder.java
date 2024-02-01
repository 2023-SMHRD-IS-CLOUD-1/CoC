package com.picstory.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhotoFolder {
	private int photo_folder_no;
	private int user_no;
	private String photo_folder_name;
	private String photo_folder_created_at;

}
