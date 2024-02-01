package com.smhrd.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.smhrd.model.Member;

// DAO 같은 느낌..?
@Mapper
public interface MemberMapper {

	public void MemberJoin(Member mem);

	public List<Member> MemberList();
	
}
