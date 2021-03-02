package com.idrust.bmfapi.services;

import java.math.BigDecimal;

import com.idrust.bmfapi.exceptions.CropPriceCalculationException;
import com.idrust.bmfapi.exceptions.QuandlAPIException;

public interface CropService {

    BigDecimal calculateCropPrice(String cropCode, String date) throws CropPriceCalculationException;

}
