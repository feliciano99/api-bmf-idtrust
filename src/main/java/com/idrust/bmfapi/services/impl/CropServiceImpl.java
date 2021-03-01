package com.idrust.bmfapi.services.impl;

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
public class CropServiceImpl implements CropService {

    private static final Logger LOGGER = LogManager.getLogger(CropServiceImpl.class);

    private final EconomiaService economiaService;
    private final QuandlService quandlService;
    private final CropPriceRepository cropPriceRepository;

    @Autowired
    public CropServiceImpl(EconomiaService economiaService,
                           QuandlService quandlService,
                           CropPriceRepository cropPriceRepository) {
        this.economiaService = economiaService;
        this.quandlService = quandlService;
        this.cropPriceRepository = cropPriceRepository;
    }

    @Override
    public BigDecimal calculateCropPrice(String cropCode, String date) throws CropPriceCalculationException {
        LOGGER.info("Validando argumentos passados para cálcular preco da cultura");
        if (cropCode == null || cropCode.trim().isEmpty()) {
            throw new CropPriceCalculationException("O {cropCode} é obrigatorio.");
        } else if (date == null || date.trim().isEmpty()) {
            throw new CropPriceCalculationException("O {date} é obrigatorio.");
        }

        // tentando recuperar o preco da cultura persistido previamente
        LOGGER.info("Procurando cultura previamente persistida no banco");
        final Optional<CropPrice> previouslyPersisted = this.cropPriceRepository.findByCodeAndDate(cropCode, date);
        if (previouslyPersisted.isPresent()) {
            LOGGER.info("A cultura já estava persistida no banco");
            return previouslyPersisted.get().getPrice();
        }

        LOGGER.info("Buscando preco da cultura na API da BM&F");
        return retrieveCropPriceFromAPI(cropCode, date).getPrice();
    }

    private CropPrice retrieveCropPriceFromAPI(String cropCode, String date) throws CropPriceCalculationException {
        LOGGER.info("Consultando APIs de preco e cotacão do dolar");
        final BigDecimal cropPriceInDollars = this.quandlService.getCropPrice(cropCode, date);
        final BigDecimal currentDollarQuotation = this.economiaService.getCurrentUSDQuotationInReais();

        return persistCropPrice(cropCode, date, cropPriceInDollars, currentDollarQuotation);
    }

    private CropPrice persistCropPrice(String cropCode,
                                       String date,
                                       BigDecimal cropPriceInDollars,
                                       BigDecimal currentDollarQuotation) {
        LOGGER.info("Persistindo preco da cultura buscada");
        final CropPrice cropPrice = new CropPrice();
        cropPrice.setCode(cropCode);
        cropPrice.setDate(date);
        cropPrice.setPrice(cropPriceInDollars.multiply(currentDollarQuotation));

        this.cropPriceRepository.save(cropPrice);

        return cropPrice;
    }

}
