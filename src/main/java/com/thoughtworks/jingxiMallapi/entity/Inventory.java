package com.thoughtworks.jingxiMallapi.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private Integer count;

    @OneToOne(targetEntity = Product.class)
    @JoinColumn(name = "productId", insertable = false, updatable = false)
    private Product product;

    public Inventory() {
    }

    public Inventory(Long id, Long productId, Integer count) {
        this.id = id;
        this.productId = productId;
        this.count = count;
    }

//    public Long getId() {
//        return id;
//    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}
