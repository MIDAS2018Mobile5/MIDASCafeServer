package com.midas2018mobile5.serverapp.repository.user;

import com.midas2018mobile5.serverapp.domain.user.userEntity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

/**
 * Created by Neon K.I.D on 4/24/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public interface UserRepository extends JpaRepository<User, Long>, UserSupportRepositroy, QuerydslPredicateExecutor<User> {
    User findByUserid(String userid);
}