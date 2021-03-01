package com.idrust.bmfpriceapi.services;

import com.idrust.bmfpriceapi.exceptions.EconomiaAPIException;

import java.math.BigDecimal;

public interface EconomiaService {

    /**
     * Requisita a API de economia para buscar o valor do dolar no atual momento
     *
     * @return O valor do dolar em reais no atual momento
     * @throws EconomiaAPIException quando ocorre qualquer problema durante a requisicão para a API de Economia
     */
    BigDecimal getCurrentUSDQuotationInReais() throws EconomiaAPIException;

}
