package com.midas2018mobile5.serverapp.Service.Order;

import com.midas2018mobile5.serverapp.Model.External.Order.Order;
import org.springframework.http.ResponseEntity;

public interface OrderService {
    ResponseEntity<?> addOrder(Order order);
    ResponseEntity<?> delOrder(Long id);
    Iterable<Order> getOrderList(String name);
    ResponseEntity<?> updateOrder(Long id);
}
