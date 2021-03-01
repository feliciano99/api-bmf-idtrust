package com.idrust.bmfpriceapi.exceptions;

public class QuandlAPIException extends ServiceException {

    public QuandlAPIException(String message) {
        super(message);
    }

    public QuandlAPIException(String message, Throwable cause) {
        super(message, cause);
    }
}
