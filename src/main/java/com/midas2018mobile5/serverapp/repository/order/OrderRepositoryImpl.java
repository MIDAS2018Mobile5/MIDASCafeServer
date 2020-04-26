package com.midas2018mobile5.serverapp.repository.order;

import com.midas2018mobile5.serverapp.domain.order.Order;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * Created by Neon K.I.D on 4/25/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class OrderRepositoryImpl extends QuerydslRepositorySupport implements OrderSupportRepository {
    public OrderRepositoryImpl() {
        super(Order.class);
    }
}
