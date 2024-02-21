package com.picstory.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhotoTag {
	private int tag_num;
	private int photo_num;
	private String tag_name;
	private String tag_uploaded;

}
