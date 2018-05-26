package com.midas2018mobile5.serverapp.Controller;

import com.midas2018mobile5.serverapp.Model.External.Order.Order;
import com.midas2018mobile5.serverapp.Model.External.Order.OrderDto;
import com.midas2018mobile5.serverapp.Service.Order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/order")
public class OrderController {
    @Autowired
    private OrderService orderDAO;

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseEntity<?> add(@Valid @RequestBody Order order) {
        return orderDAO.addOrder(order);
    }

    @RequestMapping(value = "search", method = RequestMethod.POST)
    public Iterable<Order> search(@Valid @RequestBody OrderDto order) {
        return orderDAO.getOrderList(order);
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
        return orderDAO.delOrder(id);
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@PathVariable(value = "id") Long id) {
        return orderDAO.updateOrder(id);
    }
}
