package com.idrust.bmfapi.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import com.idrust.bmfapi.dtos.QuandlCropDTO;
import com.idrust.bmfapi.exceptions.QuandlAPIException;
import com.idrust.bmfapi.properties.QuandlAPIProperties;
import com.idrust.bmfapi.services.QuandlService;
import com.idrust.bmfapi.services.imp.QuandlServiceImp;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class QuandlServiceTeste {

    private static final String URL_TESTE = "URL_TESTE";

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private QuandlAPIProperties quandlAPIProperties;

    private QuandlService quandlService;


    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        quandlService = new QuandlServiceImp(restTemplate, quandlAPIProperties);
    }

    @Test
    public void shouldReturnCropUSDPriceFromCorrectly() throws QuandlAPIException {
        final String cropCode = "SOYBEAN";
        final String date = "2021-02-19";
        final BigDecimal expectedCropPrice = BigDecimal.valueOf(15.8f);

        final QuandlCropDTO.Dataset dataset = new QuandlCropDTO.Dataset();
        dataset.setData(new Object[][]{
                new Object[]{
                        null,
                        expectedCropPrice.doubleValue()
                }
        });

        final QuandlCropDTO quandlCropDTO = new QuandlCropDTO();
        quandlCropDTO.setDataset(dataset);

        when(quandlAPIProperties.getUrlFor(cropCode, date)).thenReturn(URL_TESTE);
        when(restTemplate.getForObject(URL_TESTE, QuandlCropDTO.class)).thenReturn(quandlCropDTO);

        final BigDecimal usdCropPrice = quandlService.getCropPrice(cropCode, date);

        assertEquals(expectedCropPrice, usdCropPrice);
        verify(quandlAPIProperties, times(1)).getUrlFor(cropCode, date);
        verify(restTemplate, times(1)).getForObject(URL_TESTE, QuandlCropDTO.class);
    }

    @Test
    public void shouldThrowExceptionIfQuandlAPICallThrowsException() {
        when(quandlAPIProperties.getUrlFor(anyString(), anyString())).thenReturn(URL_TESTE);
        when(restTemplate.getForObject(anyString(), any())).thenThrow(new RuntimeException("Some exception occurred"));

        final Exception expectedException = assertThrows(QuandlAPIException.class, () -> {
            quandlService.getCropPrice(anyString(), anyString());
        });

        final String expectedMessage = "Erro ao requisitar Quandl API";
        final String actualMessage = expectedException.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(quandlAPIProperties, times(1)).getUrlFor(anyString(), anyString());
    }

    @Test
    public void shouldThrowExceptionIfQuandlAPICallReturnsNull() {
        when(quandlAPIProperties.getUrlFor(anyString(), anyString())).thenReturn(URL_TESTE);
        when(restTemplate.getForObject(anyString(), any())).thenReturn(null);

        final Exception expectedException = assertThrows(QuandlAPIException.class, () -> {
            quandlService.getCropPrice(anyString(), anyString());
        });

        final String expectedMessage = "A resposta devolvida pela API Quandl não é valida";
        final String actualMessage = expectedException.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(quandlAPIProperties, times(1)).getUrlFor(anyString(), anyString());
        verify(restTemplate, times(1)).getForObject(anyString(), any());
    }

}
