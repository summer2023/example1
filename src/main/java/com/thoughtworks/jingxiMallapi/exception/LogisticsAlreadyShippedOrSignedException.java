package com.thoughtworks.jingxiMallapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LogisticsAlreadyShippedOrSignedException extends RuntimeException{
    public LogisticsAlreadyShippedOrSignedException(Long id, String status) {
        super("The logisticsRecord which id is " + id + " is in the state of: " + status);
    }

    public LogisticsAlreadyShippedOrSignedException(String message) {
        super(message);
    }

}
