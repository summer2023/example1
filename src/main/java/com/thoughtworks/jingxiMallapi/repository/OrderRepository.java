package com.thoughtworks.jingxiMallapi.repository;

import com.thoughtworks.jingxiMallapi.entity.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<UserOrder, Long> {
    //创建新订单
    @Transactional
    UserOrder save(UserOrder order);

    //根据id查找订单
    UserOrder findUserOrderById(Long id);

    //修改订单状态
    @Modifying
    @Transactional
    @Query("update UserOrder u set u.status = ?2, u.paidTime = ?3 where u.id = ?1")
    int updateOrderStatusWithPaid(Long id, String status, String paidTime);

    @Modifying
    @Transactional
    @Query("update UserOrder u set u.status = ?2, u.withdrawnTime = ?3 where u.id = ?1")
    int updateOrderStatusToWithdrawn(Long id, String status, String withdrawnTime);

    @Modifying
    @Transactional
    @Query("update UserOrder u set u.status = ?2, u.finishTime = ?3 where u.id = ?1")
    int updateOrderStatusToFinished(Long id, String status, String finishTime);

    //根据用户id查找订单
    List<UserOrder> findByUserId(Long userId);

}
