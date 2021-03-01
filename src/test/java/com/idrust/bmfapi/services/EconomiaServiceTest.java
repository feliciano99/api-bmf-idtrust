package com.idrust.bmfapi.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import com.idrust.bmfapi.dtos.EconomiaDTO;
import com.idrust.bmfapi.exceptions.EconomiaAPIException;
import com.idrust.bmfapi.properties.EconomiaAPIProperties;
import com.idrust.bmfapi.services.EconomiaService;
import com.idrust.bmfapi.services.impl.EconomiaServiceImpl;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EconomiaServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private EconomiaAPIProperties economiaAPIProperties;

    private EconomiaService economiaService;

    final String urlTeste = "URL_TESTE";

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        economiaService = new EconomiaServiceImpl(economiaAPIProperties, restTemplate);
    }

    @Test
    public void shouldRetrieveCorrectValueInReaisFromEconomiaAPI() throws EconomiaAPIException {
        final BigDecimal expectedBid = BigDecimal.valueOf(5.43f);
        final String economiaAPIUrl =  urlTeste;
        final EconomiaDTO economiaDTO = new EconomiaDTO();
        economiaDTO.setBid(expectedBid.floatValue());

        final EconomiaDTO[] economiaDTOs = new EconomiaDTO[]{
                economiaDTO
        };

        when(economiaAPIProperties.getEconomiaAPIUrl()).thenReturn(economiaAPIUrl);
        when(restTemplate.getForObject(economiaAPIUrl, EconomiaDTO[].class)).thenReturn(economiaDTOs);

        final BigDecimal currentUSDQuotation = economiaService.getCurrentUSDQuotationInReais();

        assertEquals(expectedBid, currentUSDQuotation);
        verify(economiaAPIProperties, times(1)).getEconomiaAPIUrl();
        verify(restTemplate, times(1)).getForObject(economiaAPIUrl, EconomiaDTO[].class);
    }

    @Test
    public void shouldThrowExceptionIfEconomiaAPICallThrowsException() {
        when(economiaAPIProperties.getEconomiaAPIUrl()).thenReturn(urlTeste);
        when(restTemplate.getForObject(anyString(), any())).thenThrow(new RuntimeException("Some exception occurred"));

        final Exception expectedException = assertThrows(EconomiaAPIException.class, () -> {
            economiaService.getCurrentUSDQuotationInReais();
        });

        final String expectedMessage = "Erro ao requisitar API de Economia";
        final String actualMessage = expectedException.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(economiaAPIProperties, times(1)).getEconomiaAPIUrl();
        verify(restTemplate, times(1)).getForObject(anyString(), any());
    }

    @Test
    public void shouldThrowExceptionIfEconomiaAPICallReturnsNull() {
        when(economiaAPIProperties.getEconomiaAPIUrl()).thenReturn(urlTeste);
        when(restTemplate.getForObject(anyString(), any())).thenReturn(null);

        final Exception expectedException = assertThrows(EconomiaAPIException.class, () -> {
            economiaService.getCurrentUSDQuotationInReais();
        });

        final String expectedMessage = "A resposta devolvida pela API de economia não é válida";
        final String actualMessage = expectedException.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(economiaAPIProperties, times(1)).getEconomiaAPIUrl();
        verify(restTemplate, times(1)).getForObject(anyString(), any());
    }

    @Test
    public void shouldThrowExceptionIfEconomiaAPICallReturnsEmptyValue() {
        final EconomiaDTO[] economiaDTOs = new EconomiaDTO[]{};

        when(economiaAPIProperties.getEconomiaAPIUrl()).thenReturn(urlTeste);
        when(restTemplate.getForObject(anyString(), any())).thenReturn(economiaDTOs);

        final Exception expectedException = assertThrows(EconomiaAPIException.class, () -> {
            economiaService.getCurrentUSDQuotationInReais();
        });

        final String expectedMessage = "A resposta devolvida pela API de economia não é válida";
        final String actualMessage = expectedException.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(economiaAPIProperties, times(1)).getEconomiaAPIUrl();
        verify(restTemplate, times(1)).getForObject(anyString(), any());
    }

}
