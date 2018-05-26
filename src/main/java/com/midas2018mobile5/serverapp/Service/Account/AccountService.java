package com.midas2018mobile5.serverapp.Service.Account;

import com.midas2018mobile5.serverapp.Model.External.Account.Account;
import com.midas2018mobile5.serverapp.Model.External.Account.AccountAuth;
import com.midas2018mobile5.serverapp.Model.External.Account.AccountDto;
import org.springframework.http.ResponseEntity;

/**
 * Account Repository 에서 제공하는 Query 문을 사용해
 * 오류 처리 등을 포함하여 자주 사용하는 함수를 정의하는 곳.
 *
 * 정의된 함수는 MemberServiceImpl에서 구현된다.
 */
public interface AccountService {

    // ID 고유 값을 이용하여 Account 검색
    Account selectMember(Long id);

    // 사용자 정보 확인
    boolean validMember(AccountAuth account);

    // Account 추가 메소드
    ResponseEntity<?> addMember(AccountDto account);

    // Account 삭제 메소드
    ResponseEntity<?> deleteMember(Long id);

    // 관리자 권한 부여..
    ResponseEntity<?> privilegeMember(String userid);

    // 모든 Account 검색
    Iterable<Account> allMember();
}
