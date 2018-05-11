package com.thoughtworks.jingxiMallapi.controller;

import com.thoughtworks.jingxiMallapi.entity.*;
import com.thoughtworks.jingxiMallapi.exception.InventoryOutOfBoundException;
import com.thoughtworks.jingxiMallapi.exception.ItemNotFoundException;
import com.thoughtworks.jingxiMallapi.exception.OrderStatusConflictException;
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
    @PostMapping
    public ResponseEntity<?> saveOrder(@RequestBody List<OrderMsg> orderMsg) throws Exception {
        UserOrder order = new UserOrder();
        Long orderId = orderRepository.saveAndFlush(order).getId();
        String result = createProductSnaps(orderMsg, orderId);
        if (result.equals("success")) {
            order.setTotalPrice(countTotalPrice(orderId));
            orderRepository.save(order);
            lockInventories(orderMsg);
        }
        HttpHeaders responseHeaders = setLocationInHeaders(orderId);
        return new ResponseEntity<UserOrder>(orderRepository.findUserOrderById(orderId), responseHeaders, HttpStatus.CREATED);

    }

    //修改订单状态
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UserOrder updateOrderStatus(@PathVariable Long id, @RequestParam(value = "orderStatus", required = false, defaultValue = "unPaid") String orderStatus) throws Exception{
        UserOrder order = orderRepository.findUserOrderById(id);
        if (order == null) {
            throw new ItemNotFoundException("order", id);
        }
        if (!isThisOrderAlreadyBeenPaidOrWithdrawnOrFinished(order, orderStatus)) {
            throw new OrderStatusConflictException(id, order.getStatus());
        }
        if (orderStatus.equals("paid")) {
            createLogisticsRecord(id);
        } else if (orderStatus.equals("withdrawn")) {
            unlockInventoriesByOrderId(id);
        }
        updateOrderStatusByInputState(id, orderStatus);
        return orderRepository.findUserOrderById(id);
    }

    //根据订单id查找订单
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserOrder getOrder(@PathVariable Long id) throws Exception{
        UserOrder order = orderRepository.findUserOrderById(id);
        if (order == null) {
            throw new ItemNotFoundException("product", id);
        }
        return order;

    }

    @GetMapping
    public List<UserOrder> getUserOrders(@RequestParam Long userId) {
        return orderRepository.findByUserId(userId);
    }

    private void updateOrderStatusByInputState(Long id, String orderStatus) {
        String nowDate = String.valueOf(new Date(System.currentTimeMillis()));
        if (orderStatus.equals("paid")) {
            orderRepository.updateOrderStatusWithPaid(id, orderStatus, nowDate);
        } else if (orderStatus.equals("withdrawn")) {
            orderRepository.updateOrderStatusToWithdrawn(id, orderStatus, nowDate);
        }
    }

    private Boolean isThisOrderAlreadyBeenPaidOrWithdrawnOrFinished(UserOrder order, String orderStatus) {
        final boolean isBeenPaidOrWithdrawnOrFinished = order.getStatus().equals("paid") || order.getStatus().equals("withdrawn") || order.getStatus().equals("finished");
        return (!orderStatus.equals("paid") || !isBeenPaidOrWithdrawnOrFinished) && (!orderStatus.equals("withdrawn") || !isBeenPaidOrWithdrawnOrFinished);
    }

    private HttpHeaders setLocationInHeaders(Long orderId) {
        URI location = URI.create("http://192.168.56.1:8083/orders/" + orderId);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(location);
        return responseHeaders;
    }

    private String createProductSnaps(List<OrderMsg> orderMsg, Long orderId) throws ItemNotFoundException, InventoryOutOfBoundException {
        for (OrderMsg msg : orderMsg) {
            Product product = productRepository.findProductById(msg.getProductId());
            if (product == null) {
                throw new ItemNotFoundException("product", msg.getProductId());
            }
            Inventory inventory = inventoryRepository.findInventoryById(msg.getProductId());
            if (inventory.getCount() - inventory.getLockedCount() < msg.getPurchaseCount()) {
                throw new InventoryOutOfBoundException(msg.getProductId());
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

    private void unlockInventoriesByOrderId(Long orderId) {
        List<ProductSnap> products = productSnapRepository.findProductSnapByOrderId(orderId);
        for (ProductSnap product : products) {
            inventoryRepository.updateLockedCount(product.getId(), -product.getPurchaseCount());
        }
    }

    private void createLogisticsRecord(Long orderId) {
        LogisticsRecord logisticsRecord = new LogisticsRecord(orderId, "readyToShip");
        logisticsRecordRepository.save(logisticsRecord);
    }
}
