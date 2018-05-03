package com.thoughtworks.jingxiMallapi.repository;

import com.thoughtworks.jingxiMallapi.entity.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface OrderRepository extends JpaRepository<UserOrder, Long> {
    //创建新订单
    @Transactional
    UserOrder save(UserOrder order);

    //根据id查找库存
    UserOrder findUserOrderById(Long id);
}
