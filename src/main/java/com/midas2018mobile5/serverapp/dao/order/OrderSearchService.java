package com.midas2018mobile5.serverapp.dao.order;

import com.midas2018mobile5.serverapp.domain.order.Mcorder;
import com.midas2018mobile5.serverapp.domain.order.QMcorder;
import com.midas2018mobile5.serverapp.dto.order.OrderSearchType;
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
public class OrderSearchService extends QuerydslRepositorySupport {
    public OrderSearchService() {
        super(Mcorder.class);
    }

    public Page<Mcorder> search(final OrderSearchType type, final String value, final Pageable pageable) {
        final QMcorder order = QMcorder.mcorder;
        final JPQLQuery<Mcorder> query;

        switch (type) {
            case USERID:
                query = from(order).where(order.user.userid.stringValue().likeIgnoreCase(value + "%"));
                break;

            case STATUS:
                query = from(order).where(order.status.stringValue().likeIgnoreCase(value + "%"));
                break;

            case ALL:
                query = from(order).fetchAll();
                break;

            default:
                throw new IllegalArgumentException();
        }

        final List<Mcorder> orders = getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(orders, pageable, query.fetchCount());
    }
}
