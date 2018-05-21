package com.midas2018mobile5.serverapp.Service;

import com.midas2018mobile5.serverapp.Model.External.Member;
import org.springframework.http.ResponseEntity;

/**
 * Member Repository 에서 제공하는 Query 문을 사용해
 * 오류 처리 등을 포함하여 자주 사용하는 함수를 정의하는 곳.
 *
 * 정의된 함수는 MemberServiceImpl에서 구현된다.
 */
public interface MemberService {

    // ID 고유 값을 이용하여 Member 검색
    Member selectMember(Long id);

    // 사용자 정보 확인
    boolean validMember(Member member);

    // Member 추가 메소드
    ResponseEntity<?> addMember(Member member);

    // Member 삭제 메소드
    ResponseEntity<?> deleteMember(Long id);

    // 모든 Member 검색
    Iterable<Member> allMember();
}
