package com.picstory.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Photo {
	private int photo_num;
	private int user_num;
    private String photo_name;
    private String photo_url;
	private String photo_uqloaded;
	private String photo_size;
	private String photo_extension;
	private int length;
	

}
