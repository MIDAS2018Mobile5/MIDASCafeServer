package com.midas2018mobile5.serverapp.Model.External;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 이 인터페이스에는 JpaRepository에서 기본적으로
 * 제공하는 existbyId 등의 메소드를 포함하여
 *
 * Query 문을 사용해 개별적으로 SQL 쿼리를 메소드로
 * 정의하고자 하는 경우에 사용
 *
 * 결론: 필요한 Query 문을 정의하고자 할 경우에는 이 곳.
 * 단, 한 레포지터리 당 한 테이블용으로만 사용할 것
 */
@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
    @Query(value = "select count(m) from Account m where m.username = :name")
    int existsByMember(@Param(value = "name") String username);

    @Query(value = "select count(m) from Account m where m.username = :name and m.password = :password")
    int existsByMember(@Param(value = "name") String username, @Param(value = "password") String password);
}
