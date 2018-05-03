package com.thoughtworks.jingxiMallapi.entity;

public class OrderMsg {
    private Long productId;
    private Integer purchaseCount;

    public OrderMsg(Long productId, Integer purchaseCount) {
        this.productId = productId;
        this.purchaseCount = purchaseCount;
    }

    public OrderMsg() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getPurchaseCount() {
        return purchaseCount;
    }

    public void setPurchaseCount(Integer purchaseCount) {
        this.purchaseCount = purchaseCount;
    }
}
