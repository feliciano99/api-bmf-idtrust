package com.idrust.bmfapi.services.imp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.idrust.bmfapi.dtos.QuandlCropDTO;
import com.idrust.bmfapi.exceptions.QuandlAPIException;
import com.idrust.bmfapi.properties.QuandlAPIProperties;
import com.idrust.bmfapi.services.QuandlService;

import java.math.BigDecimal;

@Service
public class QuandlServiceImp implements QuandlService {

	private static final Logger LOGGER = LogManager.getLogger(QuandlServiceImp.class);

	private final RestTemplate restTemplate;
	private final QuandlAPIProperties quandlAPIProperties;

	@Autowired
	public QuandlServiceImp(RestTemplate restTemplate, QuandlAPIProperties quandlAPIProperties) {
		this.restTemplate = restTemplate;
		this.quandlAPIProperties = quandlAPIProperties;
	}

	@Override
	public BigDecimal getCropPrice(String cropCode, String date) throws QuandlAPIException {

		QuandlCropDTO quandlCropDTO;

		try {
			quandlCropDTO = restTemplate.getForObject(quandlAPIProperties.getUrlFor(cropCode, date),
					QuandlCropDTO.class);
		} catch (Exception e) {
			throw new QuandlAPIException("Erro ao requisitar Quandl API");
		}

		if (quandlCropDTO == null) {
			throw new QuandlAPIException("A resposta devolvida pela API Quandl não é valida");
		}

		try {
			return BigDecimal.valueOf(quandlCropDTO.getCropPrice());
		} catch (Exception e) {
			throw new QuandlAPIException("Erro ao tentar desserializar a resposta da API Quandl", e);
		}

	}

}
