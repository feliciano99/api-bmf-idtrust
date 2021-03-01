package com.idrust.bmfpriceapi.controllers;

import com.idrust.bmfpriceapi.dtos.BaseResponse;
import com.idrust.bmfpriceapi.exceptions.CropPriceCalculationException;
import com.idrust.bmfpriceapi.exceptions.EconomiaAPIException;
import com.idrust.bmfpriceapi.exceptions.QuandlAPIException;
import com.idrust.bmfpriceapi.exceptions.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalControllerErrorHandler {

    private static final Logger LOGGER = LogManager.getLogger(GlobalControllerErrorHandler.class);
    private final Map<Class<? extends ServiceException>, HttpStatus> statusMap;

    public GlobalControllerErrorHandler() {
        this.statusMap = new HashMap<>();
        this.statusMap.put(ServiceException.class, HttpStatus.INTERNAL_SERVER_ERROR);
        this.statusMap.put(CropPriceCalculationException.class, HttpStatus.INTERNAL_SERVER_ERROR);
        this.statusMap.put(EconomiaAPIException.class, HttpStatus.BAD_GATEWAY);
        this.statusMap.put(QuandlAPIException.class, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse exceptionHandler(Exception exception, HttpServletRequest request) {
        LOGGER.error("Erro interno", exception);
        return BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno, por favor contate o suporte.");
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<BaseResponse> serviceExceptionHandler(ServiceException exception, HttpServletRequest request) {
        LOGGER.error("Erro na camada de servicos", exception);
        final HttpStatus status = statusMap.get(exception.getClass());
        return ResponseEntity
                .status(status)
                .body(BaseResponse.error(status, exception.getMessage()));
    }

    @ExceptionHandler(CropPriceCalculationException.class)
    public ResponseEntity<BaseResponse> cropPriceCalculationExceptionHandler(CropPriceCalculationException exception,
                                                                             HttpServletRequest request) {
        LOGGER.error("Erro ao calcular preco da cultura", exception);
        final HttpStatus status = statusMap.get(exception.getClass());
        return ResponseEntity
                .status(status)
                .body(BaseResponse.error(status, exception.getMessage()));
    }

    @ExceptionHandler(EconomiaAPIException.class)
    public ResponseEntity<BaseResponse> economiaAPIExceptionHandler(EconomiaAPIException exception,
                                                                    HttpServletRequest request) {
        LOGGER.error("Erro no servico de acesso a API de Economia", exception);
        final HttpStatus status = statusMap.get(exception.getClass());
        return ResponseEntity
                .status(status)
                .body(BaseResponse.error(status, exception.getMessage()));
    }

    @ExceptionHandler(QuandlAPIException.class)
    public ResponseEntity<BaseResponse> quandlAPIExceptionHandler(QuandlAPIException exception, HttpServletRequest request) {
        LOGGER.error("Erro no servico de acesso a API Quandl", exception);
        final HttpStatus status = statusMap.get(exception.getClass());
        return ResponseEntity
                .status(status)
                .body(BaseResponse.error(status, exception.getMessage()));
    }

}
