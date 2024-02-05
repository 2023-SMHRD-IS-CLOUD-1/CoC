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

	// 회원가입
	public void MemberJoin(User user) {
		picstoryMapper.MemberJoin(user);
	}

	// id 중복확인
	public String IdDoubleCheck(String user_id) {
		String IdD = picstoryMapper.IdDoubleCheck(user_id);
		return IdD;
	}

	// nick 중복확인
	public String nickDoubleCheck(String user_nick) {
		String nickD = picstoryMapper.nickDoubleCheck(user_nick);
		return nickD;
	}

	// mail 중복확인
	public String mailDoubleCheck(String user_mail) {
		String mailD = picstoryMapper.mailDoubleCheck(user_mail);
		return mailD;
	}

}
