package com.thoughtworks.jingxiMallapi.entity;

import javax.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class UserOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String totalPrice;
    private String status;
    private String createTime;
    private String finishTime;
    private String paidTime;
    private String withdrawnTime;
    private Long userId;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, mappedBy = "order")
    private Set<ProductSnap> purchaseItemList = new HashSet<>();

    public UserOrder() {
        this("0", "unPaid", String.valueOf(new Date(System.currentTimeMillis())), "", "", "", 1L);
    }

    public UserOrder(String totalPrice) {
        this(totalPrice, "unPaid", String.valueOf(new Date(System.currentTimeMillis())), "", "", "", 1L);
    }

    public UserOrder(String totalPrice, String status, String createTime, String finishTime, String paidTime, String withdrawnTime, Long userId) {
        this.totalPrice = totalPrice;
        this.status = status;
        this.createTime = createTime;
        this.finishTime = finishTime;
        this.paidTime = paidTime;
        this.withdrawnTime = withdrawnTime;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        if (status == "paid"){
            this.paidTime = String.valueOf(new Date(System.currentTimeMillis()));
        }
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getPaidTime() {
        return paidTime;
    }

    public void setPaidTime(String paidTime) {
        this.paidTime = paidTime;
    }

    public String getWithdrawnTime() {
        return withdrawnTime;
    }

    public void setWithdrawnTime(String withdrawnTime) {
        this.withdrawnTime = withdrawnTime;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public Set<ProductSnap> getPurchaseItemList() {
        return purchaseItemList;
    }

    public void setPurchaseItemList(Set<ProductSnap> purchaseItemList) {
        this.purchaseItemList = purchaseItemList;
    }
}
