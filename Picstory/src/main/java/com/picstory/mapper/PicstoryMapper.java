package com.picstory.mapper;


import org.apache.ibatis.annotations.Mapper;

import com.picstory.model.User;

@Mapper
public interface PicstoryMapper {

	// login
	public User login(User user);

	

}