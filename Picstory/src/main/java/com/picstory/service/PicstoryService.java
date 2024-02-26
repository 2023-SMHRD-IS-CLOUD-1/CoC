package com.picstory.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.google.gson.reflect.TypeToken;
import com.picstory.mapper.PicstoryMapper;
import com.picstory.model.Photo;
import com.picstory.model.PhotoTag;
import com.picstory.model.User;
import com.picstory.model.UserFolder;
import com.picstory.model.UserFolderPhoto;
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
	
	public User loginEncodePwSelect(User user) {
		User userEncode = picstoryMapper.loginEncodePwSelect(user);
		return userEncode;
		
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

	// 이미지벡터
	public String urlVector(List<Photo> photoList, String user_tag_name) {
		String url = "";
		String[] data = null;
		if (user_tag_name.equals("")) {
			url = "http://52.41.65.59:4005/predict"; // 적절한 호스트와 포트를 사용해야 함
			data = new String[photoList.size()]; // 통신에 보낼 배열을 url 갯수와 크기를 동일하게 지정해 생성
			System.out.println("받은 사진 갯수 : " + photoList.size());
			for (int i = 0; i < photoList.size(); i++) { // 배열에 url 추가
				data[i] = photoList.get(i).getPhoto_url();
				System.out.println("url : " + data[i]);
			}
			System.out.println("벡터메소드 111111");
		} else {
			System.out.println("???안떠야됨");
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
			// System.out.println("요청완료!! 초기응답:" + response );

			// 서버로부터 받은 응답 확인(특징벡터)
			String responseBody = EntityUtils.toString(response.getEntity());
			System.out.println("responseBody!!" + responseBody);

			// Gson 라이브러리를 사용하여 JSON 배열을 List<Map<String, Object>>로 파싱
			java.lang.reflect.Type listType = new TypeToken<ArrayList<Map<String, Object>>>() {
			}.getType();
			List<Map<String, Object>> featuresList = new Gson().fromJson(responseBody, listType);

			// List<Map<String, Object>>를 JSON 객체로 변환
			Map<String, Object> responseMap = new HashMap<>();
			responseMap.put("images", featuresList);

			// 최종 JSON 객체를 String으로 변환하여 반환
			String jsonResponse = new Gson().toJson(responseMap);
			System.out.println("JSON Response to React Client: " + jsonResponse);

			return jsonResponse;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 프리미엄유저의 커스텀 태그 정보 반환
	public String[] getCustomTag(User user) {
		Photo data = new Photo();
		data.setUser_num(user.getUser_num());
		List<UserTag> tmpResult = picstoryMapper.getCustomTag(data);
		String[] result= new String[tmpResult.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = tmpResult.get(i).getUser_tag_name();
		}
		return result;
	}
	
	// 이미지 업로드 시 태그 생성 함수 호출
	public void setTag(List<Photo> photoList) {
		setBasicTag(photoList); // 기본 태그 (기본 10개, 프리미엄유저는 20개)
		setPremiumTag(photoList); // 커스텀 태그
	}

	// 프리미엄 유저 여부 확인 후 커스텀 태그 생성 함수 호출
	private void setPremiumTag(List<Photo> photoList) {
		Photo photo = photoList.get(0);
		User user = picstoryMapper.preminumCheck(photo);
		List<UserTag> userTag = null;
		if (user.getUser_premium().equals("10")) {
			userTag = picstoryMapper.getCustomTag(photo);
			setCustomTag(photoList, userTag);
		}
	}

	// 사용자의 기존 이미지에 커스텀 태그 생성
	private void setCustomTag(List<Photo> photoList, List<UserTag> userTag) {
		String url = "http://172.30.1.72:4000/predictt2"; // 적절한 호스트와 포트를 사용해야 함

		// 이중 배열로 첫 번째 배열에는 url목록을, 두 번째 목록에는 커스탬 태그 목록을 저장한다
		String[][] data = new String[2][];
		data[0] = new String[photoList.size()];
		data[1] = new String[userTag.size()];

		for (int i = 0; i < photoList.size(); i++) { // 배열에 url 추가
			data[0][i] = photoList.get(i).getPhoto_url();
		}
		for (int i = 0; i < userTag.size(); i++) { // 배열에 url 추가
			data[1][i] = userTag.get(i).getUser_tag_name();
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
//		 이미지수만큼 반복
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

	// 기본 태그(10개, 20개) 생성
	private void setBasicTag(List<Photo> photoList) {
		Photo photo = photoList.get(0);
		User user = picstoryMapper.preminumCheck(photo);
		String url = null;
		if (user.getUser_premium().equals("10")) {
			url = "http://172.30.1.72:4010/predictt3";
		} else {
			url = "http://172.30.1.72:4010/predictt4";
		}
		// 이중 배열로 첫 번째 배열에는 url목록을, 두 번째 목록에는 커스탬 태그 목록을 저장한다
		String[] data = new String[photoList.size()];

		for (int i = 0; i < photoList.size(); i++) { // 배열에 url 추가
			data[i] = photoList.get(i).getPhoto_url();
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
//		 이미지수만큼 반복
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

	// url로 photo_num 가져오기
	public Photo getPhotoNum(Photo photo) {
		Photo result = picstoryMapper.getPhotoNum(photo);
		return result;
	}

	// 커스텀 태그 생성 및 기존 이미지 태깅
	public void createTag(UserTag tag) {
		picstoryMapper.createTag(tag);
		setTag(tag);
	}

	// 기존 이미지 커스텀 태깅
	private void setTag(UserTag tag) {
		List<Photo> photoList = picstoryMapper.getPhotoInfo(tag.getUser_num());
		List<UserTag> user_tag = new ArrayList<UserTag>();
		user_tag.add(tag);
		setCustomTag(photoList, user_tag);
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

	// 사용자 프리미엄 여부 조회
	public User selectUserPremium(User user) {
		User userPremium = picstoryMapper.selectUserPremium(user);

		return userPremium;

	}

	// 태그필터링해서 해당하는 포토넘 가져오기
	public List<Integer> loadTaggingPhoto(List<String> tags) {
		List<Integer> result = picstoryMapper.loadTaggingPhoto(tags);

		return result;

	}

	// 필터링한 사진 정보 가져오기
	public List<Photo> selectTaggedPhoto(Photo photo) {
		List<Photo> storageS3Url = picstoryMapper.selectTaggedPhoto(photo);
		System.out.println(storageS3Url + "%%%%%%%%%%%%%%%%%%%%%%%");
		return storageS3Url;
	}

	// 태그 리스트
	public List<String> getTagList(Integer userNum) {
		List<String> tagList = picstoryMapper.getTagList(userNum);
		return tagList;
	}

	// 체크한 사진들 식별번호 가져오기
	public List<Integer> loadSelectedPhotoNum(List<String> s3photoname) {
		List<Integer> photo_num = picstoryMapper.loadSelectedPhotoNum(s3photoname);
		System.out.println(photo_num + "asdasdasdasdasdasdsad");
		return photo_num; 
	}

	public void payment(User user) {

		picstoryMapper.payment(user);
	}
	
	// 체크한 사진 삭제하기 
	public void deleteChckedPhoto(List<Integer> photoNum) {
		picstoryMapper.deleteChckedPhoto(photoNum);
		
	}

	// 폴더에 사진 담기 위해 폴더 식별번호 가져오기
	public UserFolderPhoto addPhotoToFolder(UserFolder userFolder) {
		UserFolderPhoto folder_num = picstoryMapper.addPhotoToFolder(userFolder);
		return folder_num;
	}
	
	// 폴더 식별번호와 선택된 사진 식별번호 이용해서 TB_U_F_PHOTO에 저장하기

	public int savePhotoInFolder(List<Integer> photo_nums, int folder_num) {

        // List<Integer>를 int[]로 변환
        int[] photo_num = photo_nums.stream().mapToInt(Integer::intValue).toArray();

		for (int i = 0; i < photo_nums.size(); i++) {
			UserFolderPhoto list = new UserFolderPhoto();
			list.setPhoto_num(photo_num[i]);
			list.setFolder_num(folder_num);
			
			picstoryMapper.savePhotoInFolder(list);
		}
	    return 0;
	} 
	
	// 폴더 선택했을 때 해당 폴더 식별번호 가져오기
	public List<Photo> findFolderNum(UserFolder userFolder) {
		UserFolder result1 = picstoryMapper.findFolderNum(userFolder);
		List<Photo> result3 = picstoryMapper.findPhotoNums(result1);
		System.out.println("@@@@@가져온 사진 식별번호@@@@ : " + result3);
		if (result3.size() == 0) {
			return null;
		} else {
			List<Photo> result = new ArrayList<>();
			for (int i = 0; i < result3.size(); i++) {
				Photo data = picstoryMapper.findway(result3.get(i));
				result.add(data);
			}

			return result;
		}

	}
}
