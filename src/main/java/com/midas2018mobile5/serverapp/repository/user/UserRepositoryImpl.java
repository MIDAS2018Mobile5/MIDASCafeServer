package com.midas2018mobile5.serverapp.repository.user;

import com.midas2018mobile5.serverapp.domain.user.userEntity.User;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * Created by Neon K.I.D on 4/24/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class UserRepositoryImpl extends QuerydslRepositorySupport implements UserSupportRepositroy {
    public UserRepositoryImpl() {
        super(User.class);
    }
}
