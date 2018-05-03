package com.thoughtworks.jingxiMallapi.repository;

import com.thoughtworks.jingxiMallapi.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

        //创建库存
        @Modifying
        @Transactional
        @Query(value = "insert into Inventory(id,count,lockedCount) values(?1,0,0)",nativeQuery = true)
        int saveByProductId(Long id);
        //修改库存信息
        @Modifying
        @Transactional
        @Query("update Inventory u set u.count = u.count + ?2 where u.id = ?1")
        int updateCountById(Long productId, Integer count);
        //修改锁定库存数量
        @Modifying
        @Transactional
        @Query("update Inventory u set u.lockedCount = u.lockedCount + ?2 where u.id = ?1")
        int updateLockedCount(Long productId, Integer lockedCount);
        //根据商品id查找库存
        Inventory findInventoryById(Long id);
        //查找所有库存
        List<Inventory> findAll();


}