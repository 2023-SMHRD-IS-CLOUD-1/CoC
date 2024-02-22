package com.picstory.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.picstory.model.Photo;
import com.picstory.model.PhotoTag;
import com.picstory.model.User;
import com.picstory.model.UserFolder;

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

	// 이미지 다운로드
	public List<Photo> imageDownload(Photo user_num);

	// 아이디찾기
	public User selectId(User user);

	// 비번찾기
	public User selectPw(User user);
	

	// 이미지 즐겨찾기 o -> 20 
	public void favorTrue(Photo s3_photo_name);

	// 이미지 즐겨찾기 x -> 10
	public void favorFalse(Photo s3_photo_name);

	// 즐겨찾기 이미지 리스트
	public List<Photo> favorPageImgList(Photo user_num);

	// 사용자별 생성 폴더 삽입
	public void folderListInsert(UserFolder userFolder);

	// 사용자별 보유 폴더 조회
	public List<UserFolder> folderListSelect(UserFolder userFolder);
	
	// 태그 추가
	public void addTag(PhotoTag photoTag);

	// user_num으로 사진 정보 전부 불러오기
	public List<Photo> getPhotoInfo(Photo user_num);
	
	// 네이버로 첫 로그인 -> db insert
	public void naverJoin(User userNaver);

	// 네이버 로그인 -> db 조회
	public Integer naverSelect(User userNaver);
	
	// 마이페이지 사진 수
	public Integer countPhoto(User user);

	// 회원탈퇴
	public void deleteUser(User user);


}
