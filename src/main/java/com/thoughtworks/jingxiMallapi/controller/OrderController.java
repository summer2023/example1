package com.thoughtworks.jingxiMallapi.controller;

import com.thoughtworks.jingxiMallapi.entity.*;
import com.thoughtworks.jingxiMallapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.Date;
import java.util.List;

@RestController
@EnableAutoConfiguration
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductSnapRepository productSnapRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private LogisticsRecordRepository logisticsRecordRepository;

    //创建新商品
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> saveOrder(@RequestBody List<OrderMsg> orderMsg) {
        UserOrder order = new UserOrder();
        Long orderId = orderRepository.saveAndFlush(order).getId();
        String result = createProductSnaps(orderMsg, orderId);
        if (!result.equals("success")) {
            return new ResponseEntity<String>(result, HttpStatus.BAD_REQUEST);

        }
        HttpHeaders responseHeaders = setLocationInHeaders(orderId);
        order.setTotalPrice(countTotalPrice(orderId));
        orderRepository.save(order);
        lockInventories(orderMsg);
        return new ResponseEntity<UserOrder>(orderRepository.findUserOrderById(orderId), responseHeaders, HttpStatus.CREATED);

    }

    //修改订单状态
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestParam(value = "orderStatus", required = false, defaultValue = "unPaid") String orderStatus) {
        UserOrder order = orderRepository.findUserOrderById(id);
        String nowDate = String.valueOf(new Date(System.currentTimeMillis()));
        if (order == null) {
            return new ResponseEntity<String>("Cannot find such order with input orderId.", HttpStatus.NOT_FOUND);
        }
        if (order.getStatus().equals("paid") || order.getStatus().equals("withDrawn")) {
            return new ResponseEntity<String>("The order which id is " + id + " has already been " + order.getStatus(), HttpStatus.BAD_REQUEST);
        }
        if (orderStatus.equals("paid")) {
            createLogisticsRecord(id);
            orderRepository.updateOrderStatus(id, orderStatus, nowDate, "");
        } else if (orderStatus.equals("withDrawn")) {
            orderRepository.updateOrderStatus(id, orderStatus, "", nowDate);
            unlockInventoriesByOrderId(id);
        }
        return new ResponseEntity<UserOrder>(orderRepository.findUserOrderById(id), HttpStatus.NO_CONTENT);
    }

    //根据订单id查找订单
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getOrder(@PathVariable Long id) {
        UserOrder order = orderRepository.findUserOrderById(id);
        if (order == null) {
            return new ResponseEntity<String>("Cannot find such product with input id: " + id, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<UserOrder>(order, HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<UserOrder> getUserOrders(@RequestParam Long userId) {
        return orderRepository.findByUserId(userId);
    }

    private HttpHeaders setLocationInHeaders(Long orderId) {
        URI location = URI.create("http://192.168.56.1:8083/orders/" + orderId);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(location);
        return responseHeaders;
    }

    private String createProductSnaps(List<OrderMsg> orderMsg, Long orderId) {
        for (OrderMsg msg : orderMsg) {
            Product product = productRepository.findProductById(msg.getProductId());
            if (product == null) {
                return "Cannot find such product with input id: " + msg.getProductId();
            }
            Inventory inventory = inventoryRepository.findInventoryById(msg.getProductId());
            if (inventory.getCount() - inventory.getLockedCount() < msg.getPurchaseCount()) {
                return "There is insufficient inventory for the item which id is: " + msg.getProductId();
            }
            ProductSnap productSnap = new ProductSnap(product.getId(), orderId, product.getName(), product.getDescription(), String.valueOf(product.getPrice()), msg.getPurchaseCount());
            productSnapRepository.save(productSnap);
        }
        return "success";
    }

    private String countTotalPrice(Long orderId) {
        List<ProductSnap> products = productSnapRepository.findProductSnapByOrderId(orderId);
        int totalPrice = 0;
        for (ProductSnap product : products) {
            totalPrice += Integer.parseInt(product.getPurchasePrice()) * product.getPurchaseCount();
        }
        return String.valueOf(totalPrice);
    }

    private void lockInventories(List<OrderMsg> orderMsg) {
        for (OrderMsg msg : orderMsg) {
            inventoryRepository.updateLockedCount(msg.getProductId(), msg.getPurchaseCount());
        }
    }

    private void updateInventoriesAfterPaid(Long orderId) {
        List<ProductSnap> products = productSnapRepository.findProductSnapByOrderId(orderId);
        for (ProductSnap product : products) {
            inventoryRepository.updateCountById(product.getId(), -product.getPurchaseCount());
        }
    }

    private void unlockInventoriesByOrderId(Long orderId) {
        List<ProductSnap> products = productSnapRepository.findProductSnapByOrderId(orderId);
        for (ProductSnap product : products) {
            inventoryRepository.updateLockedCount(product.getId(), -product.getPurchaseCount());
        }
    }

    private void createLogisticsRecord(Long id) {
        LogisticsRecord logisticsRecord = new LogisticsRecord(id, "readyToShip");
        logisticsRecordRepository.save(logisticsRecord);
    }
}
