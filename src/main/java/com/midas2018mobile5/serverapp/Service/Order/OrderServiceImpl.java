package com.midas2018mobile5.serverapp.Service.Order;

import com.midas2018mobile5.serverapp.Model.External.Order.Order;
import com.midas2018mobile5.serverapp.Model.External.Order.OrderDto;
import com.midas2018mobile5.serverapp.Model.External.Order.OrderRepository;
import com.midas2018mobile5.serverapp.Model.Internal.ResourceNotFoundException;
import com.midas2018mobile5.serverapp.Model.Internal.ResponseMessage;
import com.midas2018mobile5.serverapp.Model.Internal.errCode.MidasStatus;
import com.midas2018mobile5.serverapp.Model.Internal.errCode.ResponseError;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    private static final String notiMsg = "주문하신 상품이 준비되었습니다.";
    private static final String topicPrefix = "MIDASCafe";

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
        Iterable<Order> orderList = or.findBybid(id);
        for(Order order : orderList)
            or.delete(order);

        ResponseMessage msg = new ResponseMessage(true);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @Override
    public Iterable<Order> getOrderList(OrderDto order) {
        return or.selectOrder(order.userid);
    }

    @Override
    public ResponseEntity<?> updateOrder(Long id) {
        ResponseError err;
        ResponseEntity<?> response;
        MqttDefaultFilePersistence dataStore = new MqttDefaultFilePersistence(System.getProperty("user.dir"));
        try {
            or.updateOrder(id);

            Order order = or.findOne(id);
            MqttClient notiManager = new MqttClient("tcp://127.0.0.1", "MIDASCafeServer", dataStore);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
            options.setAutomaticReconnect(false);
            notiManager.connect(options);

            notiManager.publish(topicPrefix + order.name, new MqttMessage(notiMsg.getBytes()));
            notiManager.disconnect();

            ResponseMessage msg = new ResponseMessage(true);
            response = new ResponseEntity<>(msg, HttpStatus.OK);
        } catch (Exception ex) {
            err = new ResponseError(MidasStatus.INTERNAL_SERVER_ERROR);
            response = new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}
