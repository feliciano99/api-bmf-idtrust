package com.idrust.bmfpriceapi.exceptions;

public class CropPriceCalculationException extends ServiceException {

    public CropPriceCalculationException(String message) {
        super(message);
    }

    public CropPriceCalculationException(String message, Throwable cause) {
        super(message, cause);
    }
}
