package com.picstory.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.picstory.mapper.PicstoryMapper;
import com.picstory.model.Photo;
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
	public void memberJoin(User user) {
		picstoryMapper.memberJoin(user);
	}

	// id 중복확인
	public String idDoubleCheck(String user_id) {
		String IdD = picstoryMapper.idDoubleCheck(user_id);
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

	// 마이페이지
	public User myinfo(User user) {
		User myinfo = picstoryMapper.myinfo(user);
		System.out.println(myinfo);

		return myinfo;
	}

	// 회원정보수정
	public void infoUpdate(User user) {
		picstoryMapper.infoUpdate(user);

	}

	// 이미지 업로드
	public void imageListUpload(Photo photos) {
		picstoryMapper.imageListUpload(photos);
	}

	// 이미지 다운로드
	public List<Photo> imageDownload(Photo user_num) {
		List<Photo> storageS3Url = picstoryMapper.imageDownload(user_num);
		return storageS3Url;
	}

	// 아이디찾기
	public User selectId(User user) {
		User info = picstoryMapper.selectId(user);
		System.out.printf("Service - 받아온 데이터 : ", info);

		return info;
	}

	// 비밀찾기
	public User selectPw(User user) {
		User info = picstoryMapper.selectPw(user);
		System.out.printf("Service - 받아온 데이터 : ", info);

		return info;
	}

}
