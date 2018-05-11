package com.thoughtworks.jingxiMallapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class OrderStatusConflictException extends RuntimeException {
    public OrderStatusConflictException(Long id, String status) {
        super("The order which id is " + id + " has already been " + status);
    }
}
