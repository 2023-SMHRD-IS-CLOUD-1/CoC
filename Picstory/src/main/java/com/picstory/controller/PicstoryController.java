package com.picstory.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.picstory.model.User;
import com.picstory.service.PicstoryService;

@RestController
public class PicstoryController {

	@Resource
	private PicstoryService picstoryService;

	// login
	@PostMapping("/login")
	public User login(@RequestBody User user) {

		User loginInfo = picstoryService.login(user);
		if (loginInfo != null) {
			return loginInfo;
		} else {
			return null;
		}

	}

	// 회원가입
	@PostMapping("/joinIn")
	public String MemberJoin(@RequestBody User user) {
		picstoryService.MemberJoin(user);
		System.out.println(user);
		return "";
	}

	// id 중복확인
	@GetMapping("/IdDoubleCheck")
	public Boolean IdDoubleCheck(@RequestParam String user_id) {
		String IdD = picstoryService.IdDoubleCheck(user_id);
		if (IdD != null) {
			return false;
		} else {
			return true;
		}
	}

	// nick 중복확인
	@GetMapping("/nickDoubleCheck")
	public Boolean nickDoubleCheck(@RequestParam String user_nick) {
		String nickD = picstoryService.nickDoubleCheck(user_nick);
		if (nickD != null) {
			return false;
		} else {
			return true;
		}
	}

	// mail 중복확인
	@GetMapping("/mailDoubleCheck")
	public Boolean mailDoubleCheck(@RequestParam String user_mail) {
		System.out.println(user_mail);
		String mailD = picstoryService.mailDoubleCheck(user_mail);
		System.out.println(mailD);
		if (mailD != null) {
			return false;
		} else {
			return true;
		}
	}

}
