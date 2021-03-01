package com.idrust.bmfpriceapi.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "quandl.api")
public class QuandlAPIProperties {

    private static final String CROP_IDENTIFIER = "{CROP}";
    private static final String DATE_IDENTIFIER = "\\{DATE}";

    private final String url;

    /**
     * @param url O template de URL com o campo de CULTURA e DATA
     */
    public QuandlAPIProperties(String url) {
        this.url = url;
    }

    /**
     * Gera a URL, com a cultura e a data informadas, para a API de cotacões.
     * No caso, o template para a URL é inserido pelo Spring
     * no construtor da classe e dessa forma podemos simplesmente
     * alterar as variáveis do template para gerar uma URL
     * coesa de acesso à API.
     *
     * @param cropCode    A cultura inserida na URL
     * @param date      A data em que se deseja gerar a URL para o preco da cultura
     * @return  A URL gerada para a API de cotacão, com âmbos os parâmetros
     */
    public String getUrlFor(String cropCode, String date) {
        if (cropCode == null || cropCode.trim().isEmpty()) {
            throw new IllegalArgumentException("O argumento {cropCode} é obrigatorio para gerar a URL da API.");
        } else if (date == null || date.trim().isEmpty()) {
            throw new IllegalArgumentException("O argumento {date} é obrigatorio para gerar a URL da API.");
        }

        return this.url.replace(CROP_IDENTIFIER, cropCode).replaceAll(DATE_IDENTIFIER, date);
    }

}
