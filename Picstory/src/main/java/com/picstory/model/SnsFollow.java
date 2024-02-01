package com.picstory.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SnsFollow {
	private int sns_follow_no;
	private int sns_following;
	private int sns_followed;
	private String sns_followed_at;

}
