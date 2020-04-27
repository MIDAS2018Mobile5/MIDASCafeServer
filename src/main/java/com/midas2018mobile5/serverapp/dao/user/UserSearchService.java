package com.midas2018mobile5.serverapp.dao.user;

import com.midas2018mobile5.serverapp.domain.user.userEntity.QUser;
import com.midas2018mobile5.serverapp.domain.user.userEntity.User;
import com.midas2018mobile5.serverapp.dto.user.UserSearchType;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Neon K.I.D on 4/24/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@Service
@Transactional(readOnly = true)
public class UserSearchService extends QuerydslRepositorySupport {
    public UserSearchService() {
        super(User.class);
    }

    public Page<User> search(final UserSearchType type, final String value, final Pageable pageable) {
        final QUser user = QUser.user;
        final JPQLQuery<User> query;

        switch (type) {
            case USERID:
                query = from(user).where(user.userid.stringValue().likeIgnoreCase(value + "%"));
                break;

            case EMAIL:
                query = from(user).where(user.email.value.likeIgnoreCase(value + "%"));
                break;

            case ALL:
                query = from(user).fetchAll();
                break;

            default:
                throw new IllegalArgumentException();
        }

        final List<User> users = getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(users, pageable, query.fetchCount());
    }
}
