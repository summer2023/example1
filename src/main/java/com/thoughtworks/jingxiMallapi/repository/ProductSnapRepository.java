package com.thoughtworks.jingxiMallapi.repository;

import com.thoughtworks.jingxiMallapi.entity.ProductSnap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ProductSnapRepository extends JpaRepository<ProductSnap, Long> {
    //创建新商品
    @Transactional
    ProductSnap save(ProductSnap productSnap);

    //根据订单id查找库存
    List<ProductSnap> findProductSnapByOrderId(Long orderId);
}
