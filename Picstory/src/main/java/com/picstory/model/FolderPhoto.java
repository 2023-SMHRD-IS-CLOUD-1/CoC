package com.picstory.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FolderPhoto {
	private int folder_photo_no;
	private int photo_folder_no;
	private int photo_no;

}
