package com.idrust.bmfpriceapi.properties;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EconomiaAPIProperties {

    @Value("${economia.api.url:https://economia.awesomeapi.com.br/USD-BRL/}")
    private String economiaAPIUrl;

    public String getEconomiaAPIUrl() {
        return economiaAPIUrl;
    }

    public void setEconomiaAPIUrl(String economiaAPIUrl) {
        this.economiaAPIUrl = economiaAPIUrl;
    }
}
