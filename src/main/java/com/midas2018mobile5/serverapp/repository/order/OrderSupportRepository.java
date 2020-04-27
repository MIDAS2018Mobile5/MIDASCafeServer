package com.midas2018mobile5.serverapp.repository.order;

import com.midas2018mobile5.serverapp.domain.cafe.Cafe;
import com.midas2018mobile5.serverapp.domain.order.Order;
import com.midas2018mobile5.serverapp.domain.user.userEntity.User;

/**
 * Created by Neon K.I.D on 4/25/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public interface OrderSupportRepository {
    Order findNotPurchasedOrder(User user, Cafe menu);
}
