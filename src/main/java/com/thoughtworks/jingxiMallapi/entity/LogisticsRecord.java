package com.thoughtworks.jingxiMallapi.entity;

import javax.persistence.*;

@Entity
public class LogisticsRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderId;
    private String logisticsStatus;
    private String outboundTime;
    private String signedTime;
    private String deliveryMan = "李师傅";

    @OneToOne(targetEntity = UserOrder.class)
    @JoinColumn(name = "orderId", insertable = false, updatable = false)
    private UserOrder userOrder;

    public LogisticsRecord() {
    }

    public LogisticsRecord(Long orderId, String logisticsStatus) {
        this.orderId = orderId;
        this.logisticsStatus = logisticsStatus;
        this.outboundTime = "null";
        this.signedTime = "null";
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

//    public Long getOrderId() {
//        return orderId;
//    }

    public String getLogisticsStatus() {
        return logisticsStatus;
    }

    public void setLogisticsStatus(String logisticsStatus) {
        this.logisticsStatus = logisticsStatus;
    }

    public String getOutboundTime() {
        return outboundTime;
    }

    public void setOutboundTime(String outboundTime) {
        this.outboundTime = outboundTime;
    }

    public String getSignedTime() {
        return signedTime;
    }

    public void setSignedTime(String signedTime) {
        this.signedTime = signedTime;
    }

    public String getDeliveryMan() {
        return deliveryMan;
    }

    public void setDeliveryMan(String deliveryMan) {
        this.deliveryMan = deliveryMan;
    }
}
