package com.thoughtworks.jingxiMallapi.controller;

import com.thoughtworks.jingxiMallapi.entity.LogisticsRecord;
import com.thoughtworks.jingxiMallapi.repository.LogisticsRecordRepository;
import com.thoughtworks.jingxiMallapi.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

@RestController
@EnableAutoConfiguration
@RequestMapping("/logisticsRecords")
public class LogisticsRecordController {
    @Autowired
    private LogisticsRecordRepository logisticsRecordRepository;
    @Autowired
    private OrderRepository orderRepository;

    //根据订单id查找订单
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getLogisticsRecord(@PathVariable Long id) {
        LogisticsRecord logisticsRecord = logisticsRecordRepository.findLogisticsRecordById(id);
        if (logisticsRecord == null) {
            return new ResponseEntity<String>("Cannot find such logisticsRecord with input id: " + id, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<LogisticsRecord>(logisticsRecord, HttpStatus.OK);
        }
    }

    //修改物流状态
    @RequestMapping(value = "/{id}/orders/{orderId}", method = RequestMethod.PUT)
    public ResponseEntity<String> updateOrderStatus(@PathVariable Long id, @PathVariable Long orderId, @RequestParam String logisticsStatus) {
        LogisticsRecord logisticsRecord = logisticsRecordRepository.findLogisticsRecordByIdAndOrderId(id, orderId);
        String nowDate = String.valueOf(new Date(System.currentTimeMillis()));
        if (logisticsRecord == null) {
            return new ResponseEntity<>("Cannot find such logisticsRecord with logisticsId: " + id + "and orderId: " + orderId, HttpStatus.NOT_FOUND);
        }

        if (logisticsStatus.equals("shipping")) {
            if (logisticsRecord.getLogisticsStatus().equals("shipping") || logisticsRecord.getLogisticsStatus().equals("signed")) {
                return new ResponseEntity<>("The logisticsRecord which id is " + id + " is in the state of: " + logisticsRecord.getLogisticsStatus(), HttpStatus.BAD_REQUEST);
            }
            logisticsRecordRepository.updateLogisticsStatusWithShipping(id, orderId, nowDate);
        } else if (logisticsStatus.equals("signed")) {
            if (logisticsRecord.getLogisticsStatus().equals("signed")) {
                return new ResponseEntity<>("The logisticsRecord has already been signed off.", HttpStatus.BAD_REQUEST);
            }
            logisticsRecordRepository.updateLogisticsStatusWithSigned(id, orderId, nowDate);
            orderRepository.updateOrderStatusToFinished(id, "finished", nowDate);
//            unlockInventoriesByOrderId(id);
        }
        return new ResponseEntity<>("success", HttpStatus.NO_CONTENT);
    }
}
