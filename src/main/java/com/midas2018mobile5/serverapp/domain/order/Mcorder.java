package com.midas2018mobile5.serverapp.domain.order;

import com.midas2018mobile5.serverapp.domain.cafe.Cafe;
import com.midas2018mobile5.serverapp.domain.user.userEntity.User;
import com.midas2018mobile5.serverapp.dto.order.OrderDto;
import com.midas2018mobile5.serverapp.error.exception.order.OrderInvalidProcessException;
import com.midas2018mobile5.serverapp.model.DateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by Neon K.I.D on 4/25/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@Entity
@Table(name = "cafe_order")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MCOrder extends DateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uid", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cid", nullable = false)
    private Cafe cafe;

    @Column(nullable = false)
    private int amount;

    @Column
    @Enumerated(EnumType.STRING)
    private MCOrderStatus status;

    @Builder
    public MCOrder(User user, int amount, Cafe cafe) {
        this.user = user;
        this.amount = amount;
        this.cafe = cafe;
        this.status = MCOrderStatus.NOT_PURCHASED;
    }

    public void update(OrderDto.Req dto) {
        this.amount = dto.amount;
    }

    public void setReady() {
        if (!status.equals(MCOrderStatus.PURCHASED))
            throw new OrderInvalidProcessException(id);
        this.status = MCOrderStatus.READY;
    }

    public void setFinish() {
        if (!status.equals(MCOrderStatus.READY))
            throw new OrderInvalidProcessException(id);
        this.status = MCOrderStatus.FINISHED;
    }

    public void setCancel() {
        this.status = MCOrderStatus.CANCELED;
    }

    public void setPurchased() {
        if (!status.equals(MCOrderStatus.NOT_PURCHASED))
            throw new OrderInvalidProcessException(id);
        this.status = MCOrderStatus.PURCHASED;
    }
}
