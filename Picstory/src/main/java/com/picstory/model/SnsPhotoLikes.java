package com.picstory.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SnsPhotoLikes {
	private int sns_photo_likes_no;
	private int sns_no;
	private int sns_photo_no;
	private String sns_photo_liked_at;

}
