package com.thoughtworks.jingxiMallapi.controller;

import com.thoughtworks.jingxiMallapi.entity.LogisticsRecord;
import com.thoughtworks.jingxiMallapi.entity.ProductSnap;
import com.thoughtworks.jingxiMallapi.exception.ItemNotFoundException;
import com.thoughtworks.jingxiMallapi.exception.LogisticsAlreadyShippedOrSignedException;
import com.thoughtworks.jingxiMallapi.repository.InventoryRepository;
import com.thoughtworks.jingxiMallapi.repository.LogisticsRecordRepository;
import com.thoughtworks.jingxiMallapi.repository.OrderRepository;
import com.thoughtworks.jingxiMallapi.repository.ProductSnapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@EnableAutoConfiguration
@RequestMapping("/logisticsRecords")
public class LogisticsRecordController {
    @Autowired
    private LogisticsRecordRepository logisticsRecordRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductSnapRepository productSnapRepository;
    @Autowired
    private InventoryRepository inventoryRepository;

    //根据订单id查找物流
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public LogisticsRecord getLogisticsRecord(@PathVariable Long id) throws Exception {
        LogisticsRecord logisticsRecord = logisticsRecordRepository.findLogisticsRecordById(id);
        if (logisticsRecord == null) {
            throw new ItemNotFoundException("logisticsRecord", id);
        }
        return logisticsRecord;
    }

    //修改物流状态
    @PutMapping("{id}/orders/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateOrderStatus(@PathVariable Long id, @PathVariable Long orderId, @RequestParam String logisticsStatus) throws Exception {
        LogisticsRecord logisticsRecord = logisticsRecordRepository.findLogisticsRecordByIdAndOrderId(id, orderId);
        String nowDate = String.valueOf(new Date(System.currentTimeMillis()));
        if (logisticsRecord == null) {
            throw new ItemNotFoundException("logisticsRecord", "logisticsId", id, "orderId", orderId);
        }
        final boolean isLogisticsAlreadyShippedOrSigned = logisticsRecord.getLogisticsStatus().equals("shipping") || logisticsRecord.getLogisticsStatus().equals("signed");
        if (logisticsStatus.equals("shipping") && isLogisticsAlreadyShippedOrSigned) {
            throw new LogisticsAlreadyShippedOrSignedException(id, logisticsRecord.getLogisticsStatus());
        } else if (logisticsStatus.equals("signed")) {
            String result = checkWhetherCanSignLogistics(logisticsRecord);
            if (!result.equals("success")) {
               throw new LogisticsAlreadyShippedOrSignedException(result);
            }
            updateOrderStatusAndInventories(orderId, nowDate);
        }
        updateLogisticsStatus(logisticsStatus, id, orderId, nowDate);
    }

//    public ResponseEntity<String> updateOrderStatus(@PathVariable Long id, @PathVariable Long orderId, @RequestParam String logisticsStatus) {
//        LogisticsRecord logisticsRecord = logisticsRecordRepository.findLogisticsRecordByIdAndOrderId(id, orderId);
//        String nowDate = String.valueOf(new Date(System.currentTimeMillis()));
//        if (logisticsRecord == null) {
//            return new ResponseEntity<>("Cannot find such logisticsRecord with logisticsId: " + id + "and orderId: " + orderId, HttpStatus.NOT_FOUND);
//        }
//        final boolean isLogisticsAlreadyShippedOrSigned = logisticsRecord.getLogisticsStatus().equals("shipping") || logisticsRecord.getLogisticsStatus().equals("signed");
//        if (logisticsStatus.equals("shipping") && isLogisticsAlreadyShippedOrSigned) {
//            return new ResponseEntity<>("The logisticsRecord which id is " + id + " is in the state of: " + logisticsRecord.getLogisticsStatus(), HttpStatus.BAD_REQUEST);
//        } else if (logisticsStatus.equals("signed")) {
//            String result = checkWhetherCanSignLogistics(logisticsRecord);
//            if (!result.equals("success")) {
//                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
//            }
//            updateOrderStatusAndInventories(orderId, nowDate);
//        }
//        updateLogisticsStatus(logisticsStatus, id, orderId, nowDate);
//        return new ResponseEntity<>("success", HttpStatus.NO_CONTENT);
//    }

    private void updateOrderStatusAndInventories(Long orderId, String nowDate) {
        orderRepository.updateOrderStatusToFinished(orderId, "finished", nowDate);
        updateInventoriesAfterSignedOff(orderId);
    }

    private void updateLogisticsStatus(String logisticsStatus, Long id, Long orderId, String nowDate) {
        if (logisticsStatus.equals("shipping")) {
            logisticsRecordRepository.updateLogisticsStatusWithShipping(id, orderId, nowDate);
        } else if (logisticsStatus.equals("signed")) {
            logisticsRecordRepository.updateLogisticsStatusWithSigned(id, orderId, nowDate);
        }

    }

    private String checkWhetherCanSignLogistics(LogisticsRecord logisticsRecord) {
        if (logisticsRecord.getLogisticsStatus().equals("readyToShip")) {
            return "The logisticsRecord hasn't been shipped yet.";
        }
        if (logisticsRecord.getLogisticsStatus().equals("signed")) {
            return "The logisticsRecord has already been signed off.";
        }
        return "success";
    }

    private void updateInventoriesAfterSignedOff(Long orderId) {
        List<ProductSnap> products = productSnapRepository.findProductSnapByOrderId(orderId);
        for (ProductSnap product : products) {
            inventoryRepository.updateCountById(product.getId(), -product.getPurchaseCount());
            inventoryRepository.updateLockedCount(product.getId(), -product.getPurchaseCount());
        }
    }

}
