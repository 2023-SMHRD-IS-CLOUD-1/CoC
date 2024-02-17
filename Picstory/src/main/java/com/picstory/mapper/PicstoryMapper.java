package com.picstory.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.picstory.model.Photo;
import com.picstory.model.User;

@Mapper
public interface PicstoryMapper {

	// login
	public User login(User user);

	// 회원가입
	public void memberJoin(User user);

	// 아이디 중복 체크
	public String idDoubleCheck(String user_id);

	// 닉네임 중복체크
	public String nickDoubleCheck(String user_nick);

	// 이메일 중복체크
	public String mailDoubleCheck(String user_mail);
	
	// 마이페이지
	public User myinfo(User user);

	// 정보수정
	public void infoUpdate(User user);
	
	
	// 이미지 업로드
	public void imageListUpload(Photo photos);

}
