package com.idrust.bmfapi.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.idrust.bmfapi.dtos.BaseResponse;
import com.idrust.bmfapi.exceptions.CropPriceCalculationException;
import com.idrust.bmfapi.exceptions.EconomiaAPIException;
import com.idrust.bmfapi.exceptions.QuandlAPIException;
import com.idrust.bmfapi.exceptions.ServiceException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerErrorHandler {

	private static final Logger LOGGER = LogManager.getLogger(ControllerErrorHandler.class);
	private final Map<Class<? extends ServiceException>, HttpStatus> statusMap;

	public ControllerErrorHandler() {
		this.statusMap = new HashMap<>();
		this.statusMap.put(ServiceException.class, HttpStatus.INTERNAL_SERVER_ERROR);
		this.statusMap.put(CropPriceCalculationException.class, HttpStatus.INTERNAL_SERVER_ERROR);
		this.statusMap.put(EconomiaAPIException.class, HttpStatus.BAD_GATEWAY);
		this.statusMap.put(QuandlAPIException.class, HttpStatus.BAD_GATEWAY);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public BaseResponse exceptionHandler(Exception exception, HttpServletRequest request) {
		return BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno, por favor contate o suporte.");
	}

	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<BaseResponse> serviceExceptionHandler(ServiceException exception,
			HttpServletRequest request) {
		final HttpStatus status = statusMap.get(exception.getClass());
		return ResponseEntity.status(status).body(BaseResponse.error(status, exception.getMessage()));
	}

	@ExceptionHandler(CropPriceCalculationException.class)
	public ResponseEntity<BaseResponse> cropPriceCalculationExceptionHandler(CropPriceCalculationException exception,
			HttpServletRequest request) {
		final HttpStatus status = statusMap.get(exception.getClass());
		return ResponseEntity.status(status).body(BaseResponse.error(status, exception.getMessage()));
	}

	@ExceptionHandler(EconomiaAPIException.class)
	public ResponseEntity<BaseResponse> economiaAPIExceptionHandler(EconomiaAPIException exception,
			HttpServletRequest request) {
		final HttpStatus status = statusMap.get(exception.getClass());
		return ResponseEntity.status(status).body(BaseResponse.error(status, exception.getMessage()));
	}

	@ExceptionHandler(QuandlAPIException.class)
	public ResponseEntity<BaseResponse> quandlAPIExceptionHandler(QuandlAPIException exception,
			HttpServletRequest request) {
		final HttpStatus status = statusMap.get(exception.getClass());
		return ResponseEntity.status(status).body(BaseResponse.error(status, exception.getMessage()));
	}

}
