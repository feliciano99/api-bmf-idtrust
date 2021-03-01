package com.idrust.bmfapi.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.idrust.bmfapi.entities.CropPrice;
import com.idrust.bmfapi.exceptions.CropPriceCalculationException;
import com.idrust.bmfapi.exceptions.EconomiaAPIException;
import com.idrust.bmfapi.exceptions.QuandlAPIException;
import com.idrust.bmfapi.repositories.CropPriceRepository;
import com.idrust.bmfapi.services.CropService;
import com.idrust.bmfapi.services.EconomiaService;
import com.idrust.bmfapi.services.QuandlService;
import com.idrust.bmfapi.services.impl.CropServiceImpl;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CropServiceTest {

    @Mock
    private EconomiaService economiaService;

    @Mock
    private QuandlService quandlService;

    @Mock
    private CropPriceRepository cropPriceRepository;

    private CropService cropService;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        cropService = new CropServiceImpl(economiaService, quandlService, cropPriceRepository);
    }

    @Test
    public void shouldRetrieveCropPriceFromDatabaseWhenItExists() throws EconomiaAPIException, CropPriceCalculationException, QuandlAPIException {
        final CropPrice cropPrice = new CropPrice();
        cropPrice.setCode("SOYBEAN");
        cropPrice.setPrice(BigDecimal.valueOf(50.23));
        cropPrice.setDate("2021-02-18");

        when(cropPriceRepository.findByCodeAndDate(cropPrice.getCode(), cropPrice.getDate())).thenReturn(Optional.of(cropPrice));

        final BigDecimal actualCropPrice = cropService.calculateCropPrice(cropPrice.getCode(), cropPrice.getDate());

        assertEquals(cropPrice.getPrice(), actualCropPrice);
        verify(cropPriceRepository, times(1)).findByCodeAndDate(cropPrice.getCode(), cropPrice.getDate());
        verify(quandlService, times(0)).getCropPrice(anyString(), anyString());
        verify(economiaService, times(0)).getCurrentUSDQuotationInReais();
    }

    @Test
    public void shouldCallAPIsWhenCropPriceDoesNotExist() throws EconomiaAPIException, CropPriceCalculationException, QuandlAPIException {
        final CropPrice cropPrice = new CropPrice();
        cropPrice.setCode("SOYBEAN");
        cropPrice.setPrice(BigDecimal.valueOf(50));
        cropPrice.setDate("2021-02-18");

        when(cropPriceRepository.findByCodeAndDate(anyString(), anyString())).thenReturn(Optional.empty());
        when(quandlService.getCropPrice(cropPrice.getCode(), cropPrice.getDate())).thenReturn(BigDecimal.valueOf(10));
        when(economiaService.getCurrentUSDQuotationInReais()).thenReturn(BigDecimal.valueOf(5));

        final BigDecimal actualCropPrice = cropService.calculateCropPrice(cropPrice.getCode(), cropPrice.getDate());

        assertEquals(cropPrice.getPrice(), actualCropPrice);
        verify(cropPriceRepository, times(1)).findByCodeAndDate(cropPrice.getCode(), cropPrice.getDate());
        verify(quandlService, times(1)).getCropPrice(cropPrice.getCode(), cropPrice.getDate());
        verify(economiaService, times(1)).getCurrentUSDQuotationInReais();
        verify(cropPriceRepository, times(1)).save(any());
    }

    @Test
    public void shouldThrowExceptionIfCropCodeIsNull() {
        final Exception expectedException = assertThrows(CropPriceCalculationException.class, () -> {
            cropService.calculateCropPrice(null, "2021-02-18");
        });

        final String expectedMessage = "O {cropCode} é obrigatorio.";
        final String actualMessage = expectedException.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void shouldThrowExceptionIfCropCodeIsEmpty() {
        final Exception expectedException = assertThrows(CropPriceCalculationException.class, () -> {
            cropService.calculateCropPrice("           ", "2021-02-18");
        });

        final String expectedMessage = "O {cropCode} é obrigatorio.";
        final String actualMessage = expectedException.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void shouldThrowExceptionIfDateIsNull() {
        final Exception expectedException = assertThrows(CropPriceCalculationException.class, () -> {
            cropService.calculateCropPrice("SOYBEAN", null);
        });

        final String expectedMessage = "O {date} é obrigatorio.";
        final String actualMessage = expectedException.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void shouldThrowExceptionIfDateIsEmpty() {
        final Exception expectedException = assertThrows(CropPriceCalculationException.class, () -> {
            cropService.calculateCropPrice("SOYBEAN", "         ");
        });

        final String expectedMessage = "O {date} é obrigatorio.";
        final String actualMessage = expectedException.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

}
