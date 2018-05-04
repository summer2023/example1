package com.thoughtworks.jingxiMallapi.repository;

import com.thoughtworks.jingxiMallapi.entity.LogisticsRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface LogisticsRecordRepository extends JpaRepository<LogisticsRecord, Long> {
    //创建订单
    @Transactional
    LogisticsRecord save(LogisticsRecord logisticsRecord);

    //根据物流订单Id查询订单
    LogisticsRecord findLogisticsRecordById(Long id);

    //根据物流Id和订单id查询订单
    LogisticsRecord findLogisticsRecordByIdAndOrderId(Long id, Long orderId);

    //修改物流发货状态
    @Modifying
    @Transactional
    @Query("update LogisticsRecord u set u.logisticsStatus = 'shipping', u.outboundTime = ?3 where u.id = ?1 and u.orderId = ?2")
    int updateLogisticsStatusWithShipping(Long id, Long orderId, String outboundTime);

    @Modifying
    @Transactional
    @Query("update LogisticsRecord u set u.logisticsStatus = 'signed', u.signedTime = ?3 where u.id = ?1 and u.orderId = ?2")
    int updateLogisticsStatusWithSigned(Long id, Long orderId, String signedTime);
}
