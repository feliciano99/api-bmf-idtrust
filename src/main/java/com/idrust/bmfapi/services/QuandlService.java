package com.idrust.bmfapi.services;

import java.math.BigDecimal;

import com.idrust.bmfapi.exceptions.QuandlAPIException;

public interface QuandlService {

	BigDecimal getCropPrice(String cropCode, String date) throws QuandlAPIException;

}
