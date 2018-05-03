package com.thoughtworks.jingxiMallapi.controller;

import com.thoughtworks.jingxiMallapi.entity.*;
import com.thoughtworks.jingxiMallapi.repository.InventoryRepository;
import com.thoughtworks.jingxiMallapi.repository.OrderRepository;
import com.thoughtworks.jingxiMallapi.repository.ProductRepository;
import com.thoughtworks.jingxiMallapi.repository.ProductSnapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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

    //创建新商品
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> saveOrder(@RequestBody List<OrderMsg> orderMsg) {
        UserOrder order = new UserOrder();
        Long orderId = orderRepository.saveAndFlush(order).getId();
        ResponseEntity<String> result = createProductSnaps(orderMsg, orderId);
        if (result != null){
            return result;
        }
        order.setTotalPrice(countTotalPrice(orderId));
        URI location = URI.create("http://192.168.56.1:8083/orders/" + orderId);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(location);
        orderRepository.save(order);
        return new ResponseEntity<UserOrder>(orderRepository.findUserOrderById(orderId), responseHeaders, HttpStatus.CREATED);
    }

    //根据订单id查找订单
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getOrder(@PathVariable Long id) {
        UserOrder order = orderRepository.findUserOrderById(id);
        if (order == null) {
            return new ResponseEntity<String>("Cannot find such product with input id: " + id, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<UserOrder>(order, HttpStatus.OK);
    }

    private ResponseEntity<String> createProductSnaps(List<OrderMsg> orderMsg, Long orderId) {
        for (OrderMsg msg : orderMsg) {
            Product product = productRepository.findProductById(msg.getProductId());
            if (product == null) {
                return new ResponseEntity<>("Cannot find such product with input id: " + msg.getProductId(), HttpStatus.BAD_REQUEST);
            }
            Inventory inventory = inventoryRepository.findInventoryById(msg.getProductId());
            if (inventory.getCount() - inventory.getLockedCount() < msg.getPurchaseCount()) {
                return new ResponseEntity<>("There is insufficient inventory for the item which id is: " + msg.getProductId(), HttpStatus.BAD_REQUEST);
            }
            ProductSnap productSnap = new ProductSnap(product.getId(), orderId, product.getName(), product.getDescription(), String.valueOf(product.getPrice()), msg.getPurchaseCount());
            productSnapRepository.save(productSnap);
        }
        return null;
    }

    private String countTotalPrice(Long orderId) {
        List<ProductSnap> products = productSnapRepository.findProductSnapByOrderId(orderId);
        int totalPrice = 0;
        for (ProductSnap product : products) {
            totalPrice += Integer.parseInt(product.getPurchasePrice()) * product.getPurchaseCount();
        }
        return String.valueOf(totalPrice);
    }

    private void lockInventories(List<OrderMsg> orderMsg){

    }
}
