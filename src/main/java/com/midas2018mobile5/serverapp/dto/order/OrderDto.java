package com.midas2018mobile5.serverapp.dto.order;

import com.midas2018mobile5.serverapp.domain.cafe.Cafe;
import com.midas2018mobile5.serverapp.domain.order.Order;
import com.midas2018mobile5.serverapp.domain.order.OrderStatus;
import com.midas2018mobile5.serverapp.domain.user.userEntity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

/**
 * Created by Neon K.I.D on 4/25/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
public class OrderDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Req {
        @Valid
        public String userid;

        @Valid
        public String menuName;

        @Valid
        public int amount;

        @Builder
        public Req(String userid, String menuName, int amount) {
            this.userid = userid;
            this.menuName = menuName;
            this.amount = amount;
        }

        public Order toEntity(User user, Cafe menu) {
            return Order.builder()
                    .user(user)
                    .cafe(menu)
                    .amount(amount)
                    .build();
        }
    }

    @Getter
    public static class Res {
        private long id;
        private String userid;
        private int amount;
        private String menuName;
        private OrderStatus status;

        public Res(Order order) {
            this.id = order.getId();
            this.userid = order.getUser().getUserid();
            this.amount = order.getAmount();
            this.menuName = order.getCafe().getName();
            this.status = order.getStatus();
        }
    }

    @Getter
    public static class Event {
        private long id;
        private String userid;
        private String menuName;
        private int amount;

        @Builder
        public Event(final Order order) {
            this.id = order.getId();
            this.userid = order.getUser().getUserid();
            this.menuName = order.getCafe().getName();
            this.amount = order.getAmount();
        }
    }
}
