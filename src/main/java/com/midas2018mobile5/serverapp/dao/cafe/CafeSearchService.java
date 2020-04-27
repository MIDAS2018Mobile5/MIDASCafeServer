package com.midas2018mobile5.serverapp.dao.cafe;

import com.midas2018mobile5.serverapp.domain.cafe.Cafe;
import com.midas2018mobile5.serverapp.domain.cafe.QCafe;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Neon K.I.D on 4/25/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@Service
@Transactional(readOnly = true)
public class CafeSearchService extends QuerydslRepositorySupport {
    public CafeSearchService() {
        super(Cafe.class);
    }

    public Page<Cafe> search(final String value, final Pageable pageable) {
        final QCafe cafe = QCafe.cafe;
        final JPQLQuery<Cafe> query = from(cafe).fetchAll();

        final List<Cafe> cafeMenus = getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(cafeMenus, pageable, query.fetchCount());
    }
}
