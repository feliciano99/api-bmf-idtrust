package com.idrust.bmfpriceapi.services;

import com.idrust.bmfpriceapi.exceptions.QuandlAPIException;

import java.math.BigDecimal;

public interface QuandlService {

    /**
     * Calcula qual o preco em USD da cultura na data informada
     *
     * @param cropCode  Codigo da cultura que será procurada
     * @param date      Data em que se deseja o preco daquela cultura
     * @return O preco em dolares (USD) da cultura com o codigo informado na data informada
     */
    BigDecimal getCropPrice(String cropCode, String date) throws QuandlAPIException;

}
