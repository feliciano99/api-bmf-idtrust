package com.idrust.bmfapi.services.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.idrust.bmfapi.dtos.EconomiaDTO;
import com.idrust.bmfapi.exceptions.EconomiaAPIException;
import com.idrust.bmfapi.properties.EconomiaAPIProperties;
import com.idrust.bmfapi.services.EconomiaService;

import java.math.BigDecimal;

@Service
public class EconomiaServiceImpl implements EconomiaService {

    private static final Logger LOGGER = LogManager.getLogger(EconomiaServiceImpl.class);

    private final EconomiaAPIProperties economiaAPIProperties;
    private final RestTemplate restTemplate;

    @Autowired
    public EconomiaServiceImpl(EconomiaAPIProperties economiaAPIProperties, RestTemplate restTemplate) {
        this.economiaAPIProperties = economiaAPIProperties;
        this.restTemplate = restTemplate;
    }

    @Override
    public BigDecimal getCurrentUSDQuotationInReais() throws EconomiaAPIException {
        LOGGER.info("Calculando cotacão do dolar no momento atual");

        EconomiaDTO[] economiaDTOs;

        try {
            economiaDTOs = restTemplate.getForObject(economiaAPIProperties.getEconomiaAPIUrl(), EconomiaDTO[].class);
        } catch(Exception e) {
            throw new EconomiaAPIException("Erro ao requisitar API de Economia", e);
        }

        if (economiaDTOs == null || economiaDTOs.length == 0) {
            throw new EconomiaAPIException("A resposta devolvida pela API de economia não é válida");
        }

        return BigDecimal.valueOf(economiaDTOs[0].getBid());
    }

}
