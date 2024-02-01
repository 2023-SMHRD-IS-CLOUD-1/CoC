package com.smhrd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.smhrd.model.Member;
import com.smhrd.service.MemberService;

@RestController
public class MemberController {

	// 원래는 @Autowierd
	@Autowired
	private MemberService memberService;

	@GetMapping("/")
	public List<Member> MemberList() {
		List<Member> resultList = memberService.MemberList();

		return resultList;

//	// MemberJoin.jsp로 이동
//	@RequestMapping("/JoinPage.do")
//	public String JoinPage() {
//		return "MemberJoin";
//	}
//	
//	// 회원가입 기능
//	@RequestMapping("/MemberJoin.do")
//	public String MemberJoin(Member mem) {
//		memberService.MemberJoin(mem);
//		return "MemberJoin";
//	}
//	
//	@ResponseBody
//	@GetMapping("/MemberList")
//	public List<Member> MemberList() {
//		List<Member> resultList = memberService.MemberList();
//		
//		return resultList;
//	}
//	
	}
}
