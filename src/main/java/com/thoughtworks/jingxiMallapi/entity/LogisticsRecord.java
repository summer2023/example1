package com.thoughtworks.jingxiMallapi.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class LogisticsRecord {
    @Id
    private Long id;
    private String logisticsStatus;
    private String outboundTime;
    private String signedTime;
    private String deliveryMan = "李师傅";

    @OneToOne(targetEntity = UserOrder.class)
    @JoinColumn(name = "id", insertable = false, updatable = false)
    private UserOrder userOrder;

    public LogisticsRecord() {
    }

    public LogisticsRecord(Long id, String logisticsStatus) {
        this.id = id;
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
