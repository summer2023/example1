package com.thoughtworks.jingxiMallapi.controller;

import com.thoughtworks.jingxiMallapi.entity.LogisticsRecord;
import com.thoughtworks.jingxiMallapi.repository.LogisticsRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
@RequestMapping("/logisticsRecords")
public class LogisticsRecordController {
    @Autowired
    private LogisticsRecordRepository logisticsRecordRepository;

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
}
