package com.picstory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.picstory.mapper.PicstoryMapper;

@Service
public class PicstoryService {
	
	@Autowired
	private PicstoryMapper picstoryMapper;

}
