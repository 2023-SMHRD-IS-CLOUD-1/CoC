package com.smhrd.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smhrd.mapper.MemberMapper;
import com.smhrd.model.Member;

// 요청 : Controller -> Service -> Interface -> Mapper.xml(sql)
// 결과 : Mapper.xml -> Interface -> Service -> Controller
// Controller : client의 요청 받는 작업, 응답하는 작업에 집중
// Service : DB에서 가져온 데이터를 가공하는 작업에 집중(로직)


@Service
public class MemberService {
	
	@Autowired
	private MemberMapper memberMapper;
	
	public void MemberJoin(Member mem) {
		memberMapper.MemberJoin(mem);
	}

	public List<Member> MemberList() {
		return memberMapper.MemberList();
	}

}
