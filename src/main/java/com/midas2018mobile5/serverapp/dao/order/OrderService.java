package com.midas2018mobile5.serverapp.dao.order;

import com.midas2018mobile5.serverapp.domain.cafe.Cafe;
import com.midas2018mobile5.serverapp.domain.order.Mcorder;
import com.midas2018mobile5.serverapp.domain.user.userEntity.User;
import com.midas2018mobile5.serverapp.dto.order.OrderDto;
import com.midas2018mobile5.serverapp.error.exception.order.OrderNotFoundException;
import com.midas2018mobile5.serverapp.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by Neon K.I.D on 4/25/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final AmqpTemplate amqpTemplate;

    @Transactional(readOnly = true)
    public Mcorder findById(long id) {
        final Optional<Mcorder> order = orderRepository.findById(id);
        order.orElseThrow(() -> new OrderNotFoundException(id));
        return order.get();
    }

    public Mcorder create(Mcorder order) {
        return orderRepository.save(order);
    }

    public Mcorder findNotPurchased(User user, Cafe menu) {
        return orderRepository.findNotPurchasedOrder(user, menu);
    }

    public Mcorder acceptOrderById(long id) {
        final Mcorder order = findById(id);
        order.setReady();

        amqpTemplate.convertAndSend(order.getUser().getUserid(),
                OrderDto.Event.builder().order(order).build());

        return order;
    }

    public Mcorder purchaseOrderById(long id) {
        final Mcorder order = findById(id);
        order.setPurchased();

        amqpTemplate.convertAndSend(OrderEvent.orderProcessBasket,
                OrderDto.Event.builder().order(order).build());

        return order;
    }

    public Mcorder finishOrderById(long id) {
        final Mcorder order = findById(id);
        order.setFinish();

        amqpTemplate.convertAndSend(order.getUser().getUserid(),
                OrderDto.Event.builder().order(order).build());

        return order;
    }

    public Mcorder cancelOrderById(long id) {
        final Mcorder order = findById(id);
        order.setCancel();
        return order;
    }
}
