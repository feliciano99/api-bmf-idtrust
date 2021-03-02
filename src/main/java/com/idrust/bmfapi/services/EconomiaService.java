package com.idrust.bmfapi.services;

import java.math.BigDecimal;

import com.idrust.bmfapi.exceptions.EconomiaAPIException;

public interface EconomiaService {

	BigDecimal getCurrentUSDQuotationInReais() throws EconomiaAPIException;

}
