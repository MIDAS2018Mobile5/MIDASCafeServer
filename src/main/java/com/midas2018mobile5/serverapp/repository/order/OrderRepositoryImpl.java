package com.midas2018mobile5.serverapp.repository.order;

import com.midas2018mobile5.serverapp.domain.cafe.Cafe;
import com.midas2018mobile5.serverapp.domain.order.Mcorder;
import com.midas2018mobile5.serverapp.domain.order.OrderStatus;
import com.midas2018mobile5.serverapp.domain.order.QMcorder;
import com.midas2018mobile5.serverapp.domain.user.userEntity.User;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * Created by Neon K.I.D on 4/25/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class OrderRepositoryImpl extends QuerydslRepositorySupport implements OrderSupportRepository {
    public OrderRepositoryImpl() {
        super(Mcorder.class);
    }

    @Override
    public Mcorder findNotPurchasedOrder(User user, Cafe menu) {
        final QMcorder order = QMcorder.mcorder;
        final JPQLQuery<Mcorder> query = from(order).where(order.user.eq(user)
                .and(order.cafe.eq(menu))
                .and(order.status.eq(OrderStatus.NOT_PURCHASED)));

        return query.fetchFirst();
    }
}
