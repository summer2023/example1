package com.thoughtworks.jingxiMallapi.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class Inventory {
    @Id
    private Long id;
    private Integer count;
    private Integer lockedCount;

    @OneToOne(targetEntity = Product.class)
    @JoinColumn(name = "id", insertable = false, updatable = false)
    private Product product;

    public Inventory() {
    }

    public Inventory(Long id, Integer count, Integer lockedCount) {
        this.id = id;
        this.count = count;
        this.lockedCount = lockedCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getLockedCount() {
        return lockedCount;
    }

    public void setLockedCount(Integer lockedCount) {
        this.lockedCount = lockedCount;
    }
}
