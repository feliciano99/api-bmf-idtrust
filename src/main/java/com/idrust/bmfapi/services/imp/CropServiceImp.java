package com.idrust.bmfapi.services.imp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idrust.bmfapi.entities.CropPrice;
import com.idrust.bmfapi.exceptions.CropPriceCalculationException;
import com.idrust.bmfapi.exceptions.EconomiaAPIException;
import com.idrust.bmfapi.exceptions.QuandlAPIException;
import com.idrust.bmfapi.repositories.CropPriceRepository;
import com.idrust.bmfapi.services.CropService;
import com.idrust.bmfapi.services.EconomiaService;
import com.idrust.bmfapi.services.QuandlService;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CropServiceImp implements CropService {

	private static final Logger LOGGER = LogManager.getLogger(CropServiceImp.class);

	private final EconomiaService economiaService;
	private final QuandlService quandlService;
	private final CropPriceRepository cropPriceRepository;

	@Autowired
	public CropServiceImp(EconomiaService economiaService, QuandlService quandlService,
			CropPriceRepository cropPriceRepository) {
		this.economiaService = economiaService;
		this.quandlService = quandlService;
		this.cropPriceRepository = cropPriceRepository;
	}

	@Override
	public BigDecimal calculateCropPrice(String cropCode, String date) throws CropPriceCalculationException {
		if (cropCode == null || cropCode.trim().isEmpty()) {
			throw new CropPriceCalculationException("O {cropCode} é obrigatorio.");
		} else if (date == null || date.trim().isEmpty()) {
			throw new CropPriceCalculationException("O {date} é obrigatorio.");
		}

		final Optional<CropPrice> previouslyPersisted = this.cropPriceRepository.findByCodeAndDate(cropCode, date);
		if (previouslyPersisted.isPresent()) {
			return previouslyPersisted.get().getPrice();
		}

		return retrieveCropPriceFromAPI(cropCode, date).getPrice();
	}

	private CropPrice retrieveCropPriceFromAPI(String cropCode, String date) throws CropPriceCalculationException {
		final BigDecimal cropPriceInDollars = this.quandlService.getCropPrice(cropCode, date);
		final BigDecimal currentDollarQuotation = this.economiaService.getCurrentUSDQuotationInReais();

		return persistCropPrice(cropCode, date, cropPriceInDollars, currentDollarQuotation);
	}

	private CropPrice persistCropPrice(String cropCode, String date, BigDecimal cropPriceInDollars,
			BigDecimal currentDollarQuotation) {
		final CropPrice cropPrice = new CropPrice();
		cropPrice.setCode(cropCode);
		cropPrice.setDate(date);
		cropPrice.setPrice(cropPriceInDollars.multiply(currentDollarQuotation));

		this.cropPriceRepository.save(cropPrice);

		return cropPrice;
	}

}
