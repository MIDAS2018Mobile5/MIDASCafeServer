package com.midas2018mobile5.serverapp.dao.order;

import com.midas2018mobile5.serverapp.domain.cafe.Cafe;
import com.midas2018mobile5.serverapp.domain.order.Order;
import com.midas2018mobile5.serverapp.domain.user.userEntity.User;
import com.midas2018mobile5.serverapp.dto.order.OrderDto;
import com.midas2018mobile5.serverapp.error.exception.order.OrderNotFoundException;
import com.midas2018mobile5.serverapp.repository.order.OrderRepository;
import com.midas2018mobile5.serverapp.repository.user.UserRepository;
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
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final AmqpTemplate amqpTemplate;

    @Transactional(readOnly = true)
    public Order findById(long id) {
        final Optional<Order> order = orderRepository.findById(id);
        order.orElseThrow(() -> new OrderNotFoundException(id));
        return order.get();
    }

    public Order create(Order order) {
        return orderRepository.save(order);
    }

    public Order findNotPurchased(User user, Cafe menu) {
        return orderRepository.findNotPurchasedOrder(user, menu);
    }

    public Order acceptOrderById(long id) {
        final Order order = findById(id);
        order.setReady();

        amqpTemplate.convertAndSend(order.getUser().getUserid(),
                OrderDto.Event.builder().order(order).build());

        return order;
    }

    public Order purchaseOrderById(long id) {
        final Order order = findById(id);
        order.setPurchased();

        amqpTemplate.convertAndSend(OrderEvent.orderProcessBasket,
                OrderDto.Event.builder().order(order).build());

        return order;
    }

    public Order finishOrderById(long id) {
        final Order order = findById(id);
        order.setFinish();

        amqpTemplate.convertAndSend(order.getUser().getUserid(),
                OrderDto.Event.builder().order(order).build());

        return order;
    }

    public Order cancelOrderById(long id) {
        final Order order = findById(id);
        order.setCancel();
        return order;
    }
}
