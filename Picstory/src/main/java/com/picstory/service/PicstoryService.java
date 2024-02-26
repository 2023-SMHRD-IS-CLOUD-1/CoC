package com.picstory.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.picstory.mapper.PicstoryMapper;
import com.picstory.model.Photo;
import com.picstory.model.PhotoTag;
import com.picstory.model.User;
import com.picstory.model.UserFolder;
import com.picstory.model.UserTag;

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

	// 이미지 즐겨찾기 o -> 20
	public void favorTrue(Photo s3_photo_name) {
		picstoryMapper.favorTrue(s3_photo_name);
	}

	// 이미지 즐겨찾기 x -> 10
	public void favorFalse(Photo s3_photo_name) {
		picstoryMapper.favorFalse(s3_photo_name);

	}

	// 즐겨찾기 이미지 리스트
	public List<Photo> favorPageImgList(Photo user_num) {
		List<Photo> favorImgList = picstoryMapper.favorPageImgList(user_num);
		return favorImgList;

	}

	// 사용자별 생성한 파일 삽입
	public void folderListInsert(UserFolder userFolder) {
		picstoryMapper.folderListInsert(userFolder);
	}

	// 사용자별 보유 폴더 조회
	public List<UserFolder> folderListSelect(UserFolder userFolder) {
		List<UserFolder> folderListSelect = picstoryMapper.folderListSelect(userFolder);
		return folderListSelect;
	}

	// 이미지에 태깅
	public void url(List<Photo> photoList, String user_tag_name) {
		String url = "";
		String[] data = null;
		if (user_tag_name.equals("")) {
			url = "http://172.30.1.72:4000/predict"; // 적절한 호스트와 포트를 사용해야 함
			data = new String[photoList.size()]; // 통신에 보낼 배열을 url 갯수와 크기를 동일하게 지정해 생성
			for (int i = 0; i < photoList.size(); i++) { // 배열에 url 추가
				data[i] = photoList.get(i).getPhoto_url();
			}
		} else {
			url = "http://172.30.1.72:4000/predict2"; // 적절한 호스트와 포트를 사용해야 함
			data = new String[photoList.size() + 1]; // 통신에 보낼 배열을 url 갯수와 크기를 동일하게 지정해 생성
			for (int i = 0; i < photoList.size() - 1; i++) { // 배열에 url 추가
				data[i] = photoList.get(i).getPhoto_url();
			}
			data[photoList.size() - 1] = user_tag_name;
		}

		// HttpClient 객체 생성
		HttpClient httpClient = HttpClientBuilder.create().build();

		// HTTP POST 요청 객체 생성
		HttpPost request = new HttpPost(url);

		// 배열을 JSON 형식으로 변환
		Gson gson = new Gson();
		String json = gson.toJson(data);

		// JSON 데이터를 StringEntity에 설정
		try {
			StringEntity params;
			params = new StringEntity(json);
			request.addHeader("content-type", "application/json");
			request.setEntity(params);
			// POST 요청 보내기
			HttpResponse response = httpClient.execute(request);

			// 서버로부터 받은 응답 확인
			String responseBody = EntityUtils.toString(response.getEntity());
			System.out.println("Response from server: " + responseBody);

			// JSON 문자열을 JsonObject로 파싱
			JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);

			// tagList에 대한 JsonArray 가져오기
			JsonArray tagList = jsonObject.getAsJsonArray("tagList");

			// 이중 배열로 파싱
			String[][] doubleArray = new String[tagList.size()][];

			// 이중 배열로 변환
			for (int i = 0; i < tagList.size(); i++) {
				JsonArray innerArray = tagList.get(i).getAsJsonArray();
				doubleArray[i] = new String[innerArray.size()];

				for (int j = 0; j < innerArray.size(); j++) {
					doubleArray[i][j] = innerArray.get(j).getAsString();
				}
			}
//						 이미지수만큼 반복
			for (int i = 0; i < doubleArray.length - 1; i++) {
				// 태그갯수만큼 반복
				for (int j = 0; j < doubleArray[i].length; j++) {
					if (doubleArray[i][j].equals("yes")) {
						PhotoTag photoTag = new PhotoTag();
						photoTag.setPhoto_num(photoList.get(i).getPhoto_num());
						photoTag.setTag_name(doubleArray[doubleArray.length - 1][j]);
						picstoryMapper.addTag(photoTag);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 이미지벡터
	public String urlVector(List<Photo> photoList, String user_tag_name) {
		String url = "";
		String[] data = null;
		if (user_tag_name.equals("")) {
			url = "http://localhost:4005/predict"; // 적절한 호스트와 포트를 사용해야 함
			data = new String[photoList.size()]; // 통신에 보낼 배열을 url 갯수와 크기를 동일하게 지정해 생성
			System.out.println("받은 사진 갯수 : " + photoList.size());
			for (int i = 0; i < photoList.size(); i++) { // 배열에 url 추가
				data[i] = photoList.get(i).getPhoto_url();
				System.out.println("url : " + data[i]);
			}
			System.out.println("벡터메소드 111111");
		} else {
			url = "http://172.30.1.72:4000/predict2"; // 수정필요
			data = new String[photoList.size() + 1]; // 통신에 보낼 배열을 url 갯수와 크기를 동일하게 지정해 생성
			for (int i = 0; i < photoList.size() - 1; i++) { // 배열에 url 추가
				data[i] = photoList.get(i).getPhoto_url();
			}
			data[photoList.size() - 1] = user_tag_name;
		}
		// HttpClient 객체 생성
		HttpClient httpClient = HttpClientBuilder.create().build();
		// HTTP POST 요청 객체 생성
		HttpPost request = new HttpPost(url);

		// 배열을 JSON 형식으로 변환
		Gson gson = new Gson();
		String json = gson.toJson(data);
		System.out.println("보내기전!!!!!!!!:" + json);

		// JSON 데이터를 StringEntity에 설정
		try {
			StringEntity params;
			params = new StringEntity(json);
			request.addHeader("content-type", "application/json");
			request.setEntity(params);
			// POST 요청 보내기
			HttpResponse response = httpClient.execute(request);
			System.out.println("요청완료!!");

			// 서버로부터 받은 응답 확인(특징벡터)
			String responseBody = EntityUtils.toString(response.getEntity());
			System.out.println("responseBody!!" + responseBody);

			return responseBody;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// url로 photo_num 가져오기
	public Photo getPhotoNum(Photo photo) {
		Photo result = picstoryMapper.getPhotoNum(photo);
		return result;
	}

	// 네이버 첫 로그인 -> db 삽입
	public void naverJoin(User userNaver) {
		picstoryMapper.naverJoin(userNaver);
	}

	// 네이버 로그인 -> db 조회
	public Integer naverSelect(User userNaver) {

		Integer user_num_naver = picstoryMapper.naverSelect(userNaver);
		return user_num_naver;

	}

	// 마이페이지 사진 수
	public Integer countPhoto(User user) {
		Integer countPhoto = picstoryMapper.countPhoto(user);
		System.out.println("사진 수 : " + countPhoto);
		return countPhoto;
	}

	// 회원탈퇴
	public void deleteUser(User user) {
		picstoryMapper.deleteUser(user);

	}

	// 폴더 이름 변경
	public void updateFolderName(UserFolder userFolder) {
		picstoryMapper.updateFolderName(userFolder);
	}

	// 폴더 삭제
	public void deleteFolder(UserFolder userFolder) {
		picstoryMapper.deleteFolder(userFolder);

	}

}
