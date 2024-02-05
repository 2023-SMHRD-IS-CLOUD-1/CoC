package com.picstory.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.picstory.model.User;
import com.picstory.service.PicstoryService;

@RestController
public class PicstoryController {
	
	@Resource 
	private PicstoryService picstoryServise;
	
	// login
	@PostMapping("/login")
	public User login(@RequestBody User user){
		
		User loginInfo = picstoryServise.login(user);
        if(loginInfo != null) {
        	return loginInfo;
        }else {
        	return null;
        }
        
	}
	
		
		
}
