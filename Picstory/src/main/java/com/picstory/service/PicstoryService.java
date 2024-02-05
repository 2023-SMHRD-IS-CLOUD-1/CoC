package com.picstory.service;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.picstory.mapper.PicstoryMapper;
import com.picstory.model.User;

@Service
public class PicstoryService {
	
	@Resource
	private PicstoryMapper picstoryMapper;

	// login 서비스
	public User login(User user) {
		User loginInfo = picstoryMapper.login(user);
		
		return loginInfo;
		
	}

	
	

}
