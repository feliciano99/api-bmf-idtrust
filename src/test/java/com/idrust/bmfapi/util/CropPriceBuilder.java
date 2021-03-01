package com.idrust.bmfapi.util;

import java.math.BigDecimal;

import com.idrust.bmfapi.entities.CropPrice;

public class CropPriceBuilder {

    private static CropPriceBuilder instance;

    private CropPrice cropPrice;

    public static CropPriceBuilder init() {
        instance = new CropPriceBuilder();
        instance.cropPrice = new CropPrice();
        return instance;
    }

    public CropPriceBuilder id(Long id) {
        instance.cropPrice.setId(id);
        return instance;
    }

    public CropPriceBuilder code(String code) {
        instance.cropPrice.setCode(code);
        return instance;
    }

    public CropPriceBuilder price(double price) {
        instance.cropPrice.setPrice(BigDecimal.valueOf(price));
        return instance;
    }

    public CropPriceBuilder date(String date) {
        instance.cropPrice.setDate(date);
        return instance;
    }

    public CropPrice build() {
        return instance.cropPrice;
    }

}
