package com.picstory.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SnsPhoto {
	private int sns_photo_no;
	private int sns_no;
	private String sns_photo_addr;
	private String sns_photo_tag;
	private String sns_photo_descr;
	private int sns_photo_likes;
	private int sns_photo_downs;
	private String sns_photo_posted_at;

}
