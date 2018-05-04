package com.thoughtworks.jingxiMallapi.repository;

import com.thoughtworks.jingxiMallapi.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    //创建新商品
    @Transactional
    Product save(Product product);

    //修改商品信息
    @Modifying
    @Transactional
    @Query("update Product u set u.name = ?2, u.description = ?3, u.price = ?4 where u.id = ?1")
    int updateById(Long id, String name, String description, Integer price);

    //根据商品id查找商品
    Product findProductById(Long id);

    //查找所有商品
    List<Product> findAll();

    //根据name查询
    List<Product> findByName(String name);

    //根据name和描述模糊查询
    List<Product> findByNameAndDescriptionContaining(String name, String description);
}
