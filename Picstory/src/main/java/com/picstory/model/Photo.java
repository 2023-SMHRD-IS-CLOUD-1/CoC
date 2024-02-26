package com.picstory.model;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Photo {
	private int photo_num;
	private int user_num;
    private String s3_photo_name;
    private String user_photo_name;
    private String photo_url;
	private String photo_uploaded;
	private String photo_size;
	private String photo_favor;
	private int length;
	private List<Integer> photo_num_list;
	

}
