package com.idrust.bmfapi.dtos;

import java.math.BigDecimal;

public class CropPriceDTO {

    private BigDecimal price;

    public CropPriceDTO(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

}
