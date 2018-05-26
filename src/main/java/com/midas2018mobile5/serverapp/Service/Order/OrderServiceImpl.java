package com.midas2018mobile5.serverapp.Service.Order;

import com.midas2018mobile5.serverapp.Model.External.Order.Order;
import com.midas2018mobile5.serverapp.Model.External.Order.OrderRepository;
import com.midas2018mobile5.serverapp.Model.Internal.ResourceNotFoundException;
import com.midas2018mobile5.serverapp.Model.Internal.ResponseMessage;
import com.midas2018mobile5.serverapp.Model.Internal.errCode.MidasStatus;
import com.midas2018mobile5.serverapp.Model.Internal.errCode.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository or;

    @Override
    public ResponseEntity<?> addOrder(Order order) {
        ResponseError err;
        ResponseEntity<?> response;
        try {
            or.save(order);
            ResponseMessage msg = new ResponseMessage(true);
            response = new ResponseEntity<>(msg, HttpStatus.OK);
        } catch (Exception ex) {
            err = new ResponseError(MidasStatus.INTERNAL_SERVER_ERROR);
            response = new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @Override
    public ResponseEntity<?> delOrder(Long id) {
        Order order = or.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        or.delete(order);
        return ResponseEntity.ok().build();
    }

    @Override
    public Iterable<Order> getOrderList(String name) {
        return or.selectOrder(name);
    }

    @Override
    public ResponseEntity<?> updateOrder(Long id) {
        ResponseError err;
        ResponseEntity<?> response;
        try {
            or.updateOrder(id);
            ResponseMessage msg = new ResponseMessage(true);
            response = new ResponseEntity<>(msg, HttpStatus.OK);
        } catch (Exception ex) {
            err = new ResponseError(MidasStatus.INTERNAL_SERVER_ERROR);
            response = new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}
