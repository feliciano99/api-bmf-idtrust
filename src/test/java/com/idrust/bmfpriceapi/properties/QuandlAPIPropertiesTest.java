package com.idrust.bmfpriceapi.properties;

import com.idrust.bmfpriceapi.properties.QuandlAPIProperties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuandlAPIPropertiesTest {

    private static QuandlAPIProperties quandlAPIProperties;

    @BeforeAll
    static void setUp() {
        quandlAPIProperties = new QuandlAPIProperties("https://www.quandl.com/api/v3/datasets/CEPEA/{CROP}?column_index=1&start_date={DATE}&end_date={DATE}");
    }

    @Test
    void getUrlForShouldGenerateCorrectURLs() {
        final String expectedURL = "https://www.quandl.com/api/v3/datasets/CEPEA/SOYBEAN?column_index=1&start_date=2021-02-17&end_date=2021-02-17";
        final String actualURL = quandlAPIProperties.getUrlFor("SOYBEAN", "2021-02-17");

        assertEquals(expectedURL, actualURL, "As duas URLs deveriam ser idênticas.");
    }

    @Test
    void getUrlForShouldFailIfCropIsNull() {
        final Exception expectedException = assertThrows(IllegalArgumentException.class, () -> {
            quandlAPIProperties.getUrlFor(null, "2021-02-17");
        });

        final String expectedMessage = "O argumento {cropCode} é obrigatorio para gerar a URL da API.";
        final String actualMessage = expectedException.getMessage();

        assertEquals(expectedMessage, actualMessage, "A mensagem de excecão deveria ser a mesma.");
    }

    @Test
    void getUrlForShouldFailIfCropIsEmpty() {
        final Exception expectedException = assertThrows(IllegalArgumentException.class, () -> {
            quandlAPIProperties.getUrlFor("             ", "2021-02-17");
        });

        final String expectedMessage = "O argumento {cropCode} é obrigatorio para gerar a URL da API.";
        final String actualMessage = expectedException.getMessage();

        assertEquals(expectedMessage, actualMessage, "A mensagem de excecão deveria ser a mesma.");
    }

    @Test
    void getUrlForShouldFailIfDateIsNull() {
        final Exception expectedException = assertThrows(IllegalArgumentException.class, () -> {
            quandlAPIProperties.getUrlFor("SOYBEAN", null);
        });

        final String expectedMessage = "O argumento {date} é obrigatorio para gerar a URL da API.";
        final String actualMessage = expectedException.getMessage();

        assertEquals(expectedMessage, actualMessage, "A mensagem de excecão deveria ser a mesma.");
    }

    @Test
    void getUrlForShouldFailIfDateIsEmpty() {
        final Exception expectedException = assertThrows(IllegalArgumentException.class, () -> {
            quandlAPIProperties.getUrlFor("SOYBEAN", "              ");
        });

        final String expectedMessage = "O argumento {date} é obrigatorio para gerar a URL da API.";
        final String actualMessage = expectedException.getMessage();

        assertEquals(expectedMessage, actualMessage, "A mensagem de excecão deveria ser a mesma.");
    }

}
