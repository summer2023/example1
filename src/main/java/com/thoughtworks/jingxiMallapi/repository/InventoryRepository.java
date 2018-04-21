package com.thoughtworks.jingxiMallapi.repository;

import com.thoughtworks.jingxiMallapi.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

        //创建库存
        @Modifying
        @Transactional
        @Query(value = "insert into Inventory(productId,count) values(?1,0)",nativeQuery = true)
        int saveByProductId(Long productId);
        //修改库存信息
        @Modifying
        @Transactional
        @Query("update Inventory u set u.count = ?2 where u.productId = ?1")
        int updateById(Long productId, Integer count);

        //查找所有库存
        List<Inventory> findAll();


}