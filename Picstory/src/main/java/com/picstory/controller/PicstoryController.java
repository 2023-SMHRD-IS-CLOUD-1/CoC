package com.picstory.controller;

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
	@GetMapping("/IdDoubleCheck")
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

			String[] photoNameArray = data.getPhoto_name().split(",");
			String[] photoUrlsArray = data.getPhoto_url().split(",");
			String[] photoSizesArray = data.getPhoto_size().split(",");
			
			

			if (data.getLength() == 1) {
				for (int i = 0; i < data.getLength(); i++) {

					for (int j = 0; j < photoNameArray.length; j++) {
						photoNameArray[j] = photoNameArray[j].replace("[", "");
						photoNameArray[j] = photoNameArray[j].replace("]", "");
						photoUrlsArray[j] = photoUrlsArray[j].replace("[", "");
						photoUrlsArray[j] = photoUrlsArray[j].replace("]", "");
						photoSizesArray[j] = photoSizesArray[j].replace("[", "");
						photoSizesArray[j] = photoSizesArray[j].replace("]", "");
					}

				}

				for (int j = 0; j < photoUrlsArray.length; j++) {
					Photo photo = new Photo();
					photo.setUser_num(data.getUser_num());
					photo.setPhoto_name(photoNameArray[j]);
					photo.setPhoto_url(photoUrlsArray[j]);
					photo.setPhoto_size(photoSizesArray[j]);
					picstoryService.imageListUpload(photo);
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
					}

				}

				for (int j = 0; j <= photoUrlsArray.length - 1; j++) {
					Photo photo = new Photo();
					photo.setUser_num(data.getUser_num());
					photo.setPhoto_name(photoNameArray[j]);
					photo.setPhoto_url(photoUrlsArray[j]);
					photo.setPhoto_size(photoSizesArray[j]);
					picstoryService.imageListUpload(photo);
				}
			}

			return ResponseEntity.ok("File upload successful");
		} catch (Exception e) {
			// 오류가 발생했다면 500 Internal Server Error 응답 반환
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
		}
	}

}
