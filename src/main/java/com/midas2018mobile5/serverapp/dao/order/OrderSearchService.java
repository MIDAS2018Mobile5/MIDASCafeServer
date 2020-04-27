package com.midas2018mobile5.serverapp.dao.order;

import com.midas2018mobile5.serverapp.domain.order.Order;
import com.midas2018mobile5.serverapp.domain.order.QOrder;
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
        super(Order.class);
    }

    public Page<Order> search(final String value, final Pageable pageable) {
        final QOrder order = QOrder.order;
        final JPQLQuery<Order> query = from(order).fetchAll();

        final List<Order> orderList = getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(orderList, pageable, query.fetchCount());
    }
}
