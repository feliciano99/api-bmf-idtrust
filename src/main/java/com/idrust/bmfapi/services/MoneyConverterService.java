package com.idrust.bmfapi.services;

public interface MoneyConverterService {

    /**
     * Converte um valor em dolares (USD) para reais (R$)
     *
     * @param usdValue  O valor em dolares a ser convertido
     * @return O valor em reais
     */
    Float convertToReal(Float usdValue);

}
