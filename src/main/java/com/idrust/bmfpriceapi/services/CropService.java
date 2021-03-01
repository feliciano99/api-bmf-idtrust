package com.idrust.bmfpriceapi.services;

import com.idrust.bmfpriceapi.exceptions.CropPriceCalculationException;
import com.idrust.bmfpriceapi.exceptions.QuandlAPIException;

import java.math.BigDecimal;

public interface CropService {

    /**
     * Calcula o preco da cultura na data desejada
     *
     * @param cropCode  Cultura que se deseja o preco
     * @param date      Data para filtrar o período em que se deseja o preco
     * @return  O preco da cultura na data informada (aqui eu estou supondo que o preco devolvido pela API do quandl já
     *          devolve o preco em USD/Kg e daí dentro do servico eu apenas calculei a conversão para R$)
     * @throws CropPriceCalculationException quando qualquer problema ocorre durante o cálculo do preco da cultura
     */
    BigDecimal calculateCropPrice(String cropCode, String date) throws CropPriceCalculationException;

}
