package com.picstory.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Photo {
	private int photo_no;
	private int user_no;
	private String photo_addr;
	private String photo_tag;
	private String photo_posted_at;

}
