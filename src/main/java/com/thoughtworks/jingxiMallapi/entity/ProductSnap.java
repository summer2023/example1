package com.thoughtworks.jingxiMallapi.entity;

import javax.persistence.*;

@Entity
public class ProductSnap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long snapId;
    private Long id;
    private Long orderId;
    private String productName;
    private String productDescription;
    private String purchasePrice;
    private Integer purchaseCount;

    @ManyToOne(targetEntity = UserOrder.class)
    @JoinColumn(name ="orderId", insertable = false, updatable = false)
    private UserOrder order;

    public ProductSnap() {
    }

    public ProductSnap(Long id, Long orderId, String productName, String productDescription, String purchasePrice, Integer purchaseCount) {
        this.id = id;
        this.orderId = orderId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.purchasePrice = purchasePrice;
        this.purchaseCount = purchaseCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(String purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Integer getPurchaseCount() {
        return purchaseCount;
    }

    public void setPurchaseCount(Integer purchaseCount) {
        this.purchaseCount = purchaseCount;
    }


}
