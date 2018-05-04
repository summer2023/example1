package com.thoughtworks.jingxiMallapi.repository;

import com.thoughtworks.jingxiMallapi.entity.LogisticsRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface LogisticsRecordRepository extends JpaRepository<LogisticsRecord, Long> {
    //创建订单
    @Transactional
    LogisticsRecord save(LogisticsRecord logisticsRecord);
    //查询订单
    LogisticsRecord findLogisticsRecordById(Long id);
}
