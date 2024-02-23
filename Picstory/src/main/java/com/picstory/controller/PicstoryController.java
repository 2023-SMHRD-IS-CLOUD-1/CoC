package com.picstory.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.picstory.model.Photo;
import com.picstory.model.User;
import com.picstory.model.UserFolder;
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
	public String memberJoin(@RequestBody User user) {
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
	public User myinfo(@RequestBody User user) {
		User myinfo = picstoryService.myinfo(user);
		return myinfo;
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
				System.out.println(data);

				String[] photoNameArray = data.getS3_photo_name().split(",");
				String[] photoUrlsArray = data.getPhoto_url().split(",");
				String[] photoSizesArray = data.getPhoto_size().split(",");
				String[] userPhotoNameArray = data.getUser_photo_name().split(",");
				
				// 이미지 업로드할 때 태깅
				List<Photo> photoList = new ArrayList<Photo>();
				if (data.getLength() == 1) {
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
					for (int i = 0; i < data.getLength() + 1; i++) {

						for (int j = 0; j <= photoNameArray.length - 1; j++) {
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

					for (int j = 0; j <= photoUrlsArray.length - 1; j++) {
						Photo photo = new Photo();
						photo.setUser_num(data.getUser_num());
						photo.setS3_photo_name(photoNameArray[j]);
						photo.setPhoto_url(photoUrlsArray[j]);
						photo.setPhoto_size(photoSizesArray[j]);
						photo.setUser_photo_name(userPhotoNameArray[j]);
						picstoryService.imageListUpload(photo);
					}
				}
				// 이미지 업로드할 때 태깅
				picstoryService.url(photoList,"");
				
				return ResponseEntity.ok("File upload successful");
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
		public  List<Photo> favorPageImgList(@RequestBody Photo user_num) {
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

}
