package com.picstory.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.picstory.model.Photo;
import com.picstory.model.User;
import com.picstory.model.UserFolder;
import com.picstory.model.UserFolderPhoto;
import com.picstory.model.UserTag;
import com.picstory.service.PicstoryService;

@RestController
public class PicstoryController {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Resource
	private PicstoryService picstoryService;

	// login
	@PostMapping("/login")
	public User login(@RequestBody User user) {

		// 요청 값 user를 통해 사용자 로그인 정보 조회하기
		User encodeInfo = picstoryService.loginEncodePwSelect(user);
		System.out.println(encodeInfo);

		if (encodeInfo != null && passwordEncoder.matches(user.getUser_pw(), encodeInfo.getUser_pw())) {
			return encodeInfo;
		} else {
			return null;
		}
	}

	// 결제완료시 premium 변경
	@PostMapping("/payment")
	public String payment(@RequestBody User user) {
		System.out.printf("user : ", user);
		picstoryService.payment(user);
		return "";
	}

	// 회원가입
	@PostMapping("/joinIn")
	public String memberJoin(@RequestBody User user) {
		user.incode(user.getUser_pw());
		picstoryService.memberJoin(user);
		System.out.println("회원가입 데이터 수집 : " + user);
		return "";
	}

	// id 중복확인
	@GetMapping("/idDoubleCheck")
	public Boolean idDoubleCheck(@RequestParam String user_id) {
		String IdD = picstoryService.idDoubleCheck(user_id);
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
		System.out.println("메일 중복확인 수집 : " + user_mail);
		String mailD = picstoryService.mailDoubleCheck(user_mail);
		if (mailD != null) {
			return false;
		} else {
			return true;
		}
	}

	// 마이페이지
	@PostMapping("/myinfo")
	public Map<String, Object> myinfo(@RequestBody User user) {
		Map<String, Object> response = new HashMap<>();
		User myinfo = picstoryService.myinfo(user);
		Integer countPhoto = picstoryService.countPhoto(user);
		List<String> tagList = picstoryService.getTagList(user.getUser_num());

		response.put("myinfo", myinfo);
		response.put("countPhoto", countPhoto);
		response.put("tagList", tagList);

		return response;
	}

	// 정보수정
	@PostMapping("/infoUpdate")
	public String infoUpdate(@RequestBody User user) {
		picstoryService.infoUpdate(user);
		System.out.println("수정" + user);
		return "";
	}

	// 이미지 업로드
	@PostMapping("/imageUpload")
	public ResponseEntity<String> imageUpload(@RequestBody Photo data) {
		try {
			System.out.println("컨트롤러에서 받은 : " + data);

			String[] photoNameArray = data.getS3_photo_name().split(",");
			String[] photoUrlsArray = data.getPhoto_url().split(",");
			String[] photoSizesArray = data.getPhoto_size().split(",");
			String[] userPhotoNameArray = data.getUser_photo_name().split(",");

			// 이미지 업로드할 때 태깅
			List<Photo> photoList = new ArrayList<Photo>();
			if (data.getLength() == 1) {
				System.out.println("이미지 1개에여");
				for (int i = 0; i < data.getLength(); i++) {

					for (int j = 0; j < photoNameArray.length; j++) {
						photoNameArray[j] = photoNameArray[j].replace("[", "");
						photoNameArray[j] = photoNameArray[j].replace("]", "");
						photoUrlsArray[j] = photoUrlsArray[j].replace("[", "");
						photoUrlsArray[j] = photoUrlsArray[j].replace("]", "");
						photoSizesArray[j] = photoSizesArray[j].replace("[", "");
						photoSizesArray[j] = photoSizesArray[j].replace("]", "");
						userPhotoNameArray[j] = userPhotoNameArray[j].replace("[", "");
						userPhotoNameArray[j] = userPhotoNameArray[j].replace("]", "");
					}
				}

				for (int j = 0; j < photoUrlsArray.length; j++) {
					Photo photo = new Photo();
					photo.setUser_num(data.getUser_num());
					photo.setS3_photo_name(photoNameArray[j]);
					photo.setPhoto_url(photoUrlsArray[j]);
					photo.setPhoto_size(photoSizesArray[j]);
					photo.setUser_photo_name(userPhotoNameArray[j]);
					picstoryService.imageListUpload(photo);

					// 이미지 업로드할 때 태깅
					Photo photoTmp = new Photo();
					photoTmp.setPhoto_url(photoUrlsArray[j]);
					photoTmp.setPhoto_num(picstoryService.getPhotoNum(photoTmp).getPhoto_num());
					photoList.add(photoTmp);
				}

			} else {
				System.out.println("이미지 여러개에여");
				for (int i = 0; i < data.getLength(); i++) {

					for (int j = 0; j < photoNameArray.length; j++) {
						photoNameArray[j] = photoNameArray[j].replace("[", "");
						photoNameArray[j] = photoNameArray[j].replace("]", "");
						photoUrlsArray[j] = photoUrlsArray[j].replace("[", "");
						photoUrlsArray[j] = photoUrlsArray[j].replace("]", "");
						photoSizesArray[j] = photoSizesArray[j].replace("[", "");
						photoSizesArray[j] = photoSizesArray[j].replace("]", "");
						userPhotoNameArray[j] = userPhotoNameArray[j].replace("[", "");
						userPhotoNameArray[j] = userPhotoNameArray[j].replace("]", "");
					}
					Photo photo = new Photo();
					photo.setUser_num(data.getUser_num());
					photo.setS3_photo_name(photoNameArray[i]);
					photo.setPhoto_url(photoUrlsArray[i]);
					photo.setPhoto_size(photoSizesArray[i]);
					photo.setUser_photo_name(userPhotoNameArray[i]);
					picstoryService.imageListUpload(photo);

					// 이미지 업로드할 때 태깅
					Photo photoTmp = new Photo();
					photoTmp.setPhoto_url(photoUrlsArray[i]);
					photoTmp.setPhoto_num(picstoryService.getPhotoNum(photoTmp).getPhoto_num());
					photoList.add(photoTmp);
				}
			}
			System.out.println("서비스 가기 전 : " + photoList);
			// 이미지 업로드할 때 태깅
			picstoryService.setTag(photoList);
			// 이미지 업로드할 때 특징벡터
			String responseBody = picstoryService.urlVector(photoList, "");

			return ResponseEntity.ok(responseBody); // 특징벡터->리액트

		} catch (Exception e) {
			// 오류가 발생했다면 500 Internal Server Error 응답 반환
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
		}
	}

	// 이미지 다운로드
	@PostMapping("/imageDownload")
	public List<Photo> imageDownload(@RequestBody Photo user_num) {

		List<Photo> storageS3Url = picstoryService.imageDownload(user_num);
		return storageS3Url;

	}

	// 아이디 찾기
	@PostMapping("/selectId")
	public User selectId(@RequestBody User user) {
		User info = picstoryService.selectId(user);
		System.out.printf("받아온 아이디 - Controller : ", info);

		return info;
	}

	// pw 찾기
	@PostMapping("/selectPw")
	public User selectPw(@RequestBody User user) {
		User info = picstoryService.selectPw(user);
		System.out.printf("받아온 비번 - Controller : ", info);
		return info;
	}

	// 좋아요 true
	@PostMapping("/favorTrue")
	public String favorTrue(@RequestBody Photo s3_photo_name) {
		System.out.println("favorTrue" + s3_photo_name);
		picstoryService.favorTrue(s3_photo_name);
		return "";
	}

	// 좋아요 false
	@PostMapping("/favorFalse")
	public String favorFalse(@RequestBody Photo s3_photo_name) {
		System.out.println("favorFalse" + s3_photo_name);
		picstoryService.favorFalse(s3_photo_name);
		return "";
	}

	// 즐겨찾기 폴더
	@PostMapping("/favorPageImgList")
	public List<Photo> favorPageImgList(@RequestBody Photo user_num) {
		System.out.println(user_num);
		List<Photo> favorImgList = picstoryService.favorPageImgList(user_num);
		System.out.println("favorImgList" + favorImgList);
		return favorImgList;
	}

	// 사용자별로 생성된 폴더 삽입 폴더리스트 꺼내오기
	@PostMapping("/folderList")
	public void folderList(@RequestBody UserFolder userFolder) {
		System.out.println(userFolder);
		picstoryService.folderListInsert(userFolder);
	}

	@PostMapping("/folderListSelect")
	public List<UserFolder> folderListSelect(@RequestBody UserFolder userFolder) {
		List<UserFolder> folderListSelectRes = picstoryService.folderListSelect(userFolder);
		return folderListSelectRes;
	}

	// naver 로그인
	@PostMapping("/naverJoin")
	public Integer naverJoin(@RequestBody User userNaver) {
		Integer user_num_naver = picstoryService.naverSelect(userNaver);
		if (user_num_naver == null || user_num_naver == 0) {
			picstoryService.naverJoin(userNaver);
		}

		return user_num_naver;
	}

	// 회원탈퇴
	@PostMapping("/deleteUser")
	public void deleteUser(@RequestBody User user) {
		picstoryService.deleteUser(user);
	}

	// 사용자 폴더 이름 변경
	@PostMapping("/updateFolderName")
	public void updateFolderName(@RequestBody UserFolder userFolder) {
		picstoryService.updateFolderName(userFolder);
	}

	// 사용자 폴더 삭제
	@PostMapping("/deleteFolder")
	public String deleteFolder(@RequestBody UserFolder userFolder) {
		System.out.println("@@@@@@@@@@@@@@@@@@@@@" + userFolder);
		picstoryService.deleteFolder(userFolder);

		return "success";
	}

	// 태그 생성
	@PostMapping("/createTag")
	public void createTag(@RequestBody UserTag tag) {
		System.out.println("sasdfsaaasdf");
		picstoryService.createTag(tag);
	}

	// 사용자의 커스텀 태그 가져오기
	@PostMapping("/getCustomTag")
	public String[] getCustomTag(@RequestBody User user) {
		String[] result = picstoryService.getCustomTag(user);
		return result;
	}

	// 사용자 프리미엄 여부 불러오기
	@PostMapping("/selectUserPremium")
	public User selectUserPremium(@RequestBody User user) {
		User userPremium = picstoryService.selectUserPremium(user);

		return userPremium;
	}

	// 태그필터링해서 사진 정보 가져오기
	@PostMapping("/loadTaggingPhoto")
	public List<Integer> loadTaggingPhoto(@RequestBody List<String> tagNames) {
		System.out.println("tagNames" + tagNames);
		List<Integer> result = picstoryService.loadTaggingPhoto(tagNames);
		System.out.println(result + "보내준 데이터@@@@");
		return result;
	}

//			
	// 필터링한 사진 정보데이터 가져오기
	@PostMapping("/selectTaggedPhoto")
	public List<Photo> selectTaggedPhoto(@RequestBody Photo photo) {
		List<Photo> storageS3Url = picstoryService.selectTaggedPhoto(photo);
		return storageS3Url;
	}

	// 체크한 사진들 식별번호 가져오기
	@PostMapping("/loadSelectedPhotoNum")
	public List<Integer> loadSelectedPhotoNum(@RequestBody List<String> s3photoname) {

		if (s3photoname.isEmpty()) {
			return null;
		} else {
			List<Integer> photo_num = picstoryService.loadSelectedPhotoNum(s3photoname);
			return photo_num;
		}
	}

	// 체크한 사진 삭제하기
	@PostMapping("/deleteChckedPhoto")
	public void deleteChckedPhoto(@RequestBody List<Integer> photoNum) {
		System.out.println(photoNum + "$$$$#$#$#$#$#$#$#$$#");
		picstoryService.deleteChckedPhoto(photoNum);
	}
  
	// 폴더에 사진 담기 위해 폴더 식별번호 가져오기
	@PostMapping("/addPhotoToFolder")
	public UserFolderPhoto addPhotoToFolder(@RequestBody UserFolder userFolder) {
		UserFolderPhoto folder_num = picstoryService.addPhotoToFolder(userFolder);
		return folder_num;
	}
 
	// 폴더 식별번호와 선택된 사진 식별번호 이용해서 TB_U_F_PHOTO에 저장하기
	@PostMapping("/savePhotoInFolder")
	public int savePhotoInFolder(@RequestBody UserFolderPhoto data) {
		int result = picstoryService.savePhotoInFolder(data.getPhoto_nums(), data.getFolder_num());

		return result; // 어떤 결과를 반환하든 상황에 맞게 설정
	}
	 
	// 폴더 선택했을 때 해당 폴더 식별번호 가져
	@PostMapping("/findFolderNum")
	public List<Photo> findFolderNum(@RequestBody UserFolder userFolder) {
		System.err.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		List<Photo> result = picstoryService.findFolderNum(userFolder);
		if (result == null) {
			return null;
		} else {
			return result;
		}
	}
}
