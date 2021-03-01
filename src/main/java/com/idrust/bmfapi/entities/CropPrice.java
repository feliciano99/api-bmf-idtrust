package com.idrust.bmfapi.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "crops_quotations")
public class CropPrice extends AbstractEntity {

    @Column(name = "code", nullable = false, updatable = false)
    private String code;

    @Column(name = "price", nullable = false, updatable = false)
    private BigDecimal price;

    @Column(name = "date", nullable = false, updatable = false)
    private String date;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price.setScale(2, RoundingMode.HALF_UP);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
