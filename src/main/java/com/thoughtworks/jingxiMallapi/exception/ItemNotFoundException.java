package com.thoughtworks.jingxiMallapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(String className, Long id) {
        super("Cannot find such "+ className +" with id " + id + ".");
    }
}
