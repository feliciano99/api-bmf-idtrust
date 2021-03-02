package com.idrust.bmfapi.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "quandl.api")
public class QuandlAPIProperties {

    private static final String CROP_IDENTIFIER = "{CROP}";
    private static final String DATE_IDENTIFIER = "\\{DATE}";

    private final String url;

    public QuandlAPIProperties(String url) {
        this.url = url;
    }

    public String getUrlFor(String cropCode, String date) {
        if (cropCode == null || cropCode.trim().isEmpty()) {
            throw new IllegalArgumentException("O argumento {cropCode} é obrigatorio para gerar a URL da API.");
        } else if (date == null || date.trim().isEmpty()) {
            throw new IllegalArgumentException("O argumento {date} é obrigatorio para gerar a URL da API.");
        }

        return this.url.replace(CROP_IDENTIFIER, cropCode).replaceAll(DATE_IDENTIFIER, date);
    }

}
