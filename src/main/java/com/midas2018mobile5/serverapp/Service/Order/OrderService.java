package com.midas2018mobile5.serverapp.Service.Order;

import com.midas2018mobile5.serverapp.Model.External.Order.Order;
import com.midas2018mobile5.serverapp.Model.External.Order.OrderDto;
import org.springframework.http.ResponseEntity;

public interface OrderService {
    ResponseEntity<?> addOrder(Order order);
    ResponseEntity<?> delOrder(Long id);
    Iterable<Order> getOrderList(OrderDto name);
    ResponseEntity<?> updateOrder(Long id);
}
