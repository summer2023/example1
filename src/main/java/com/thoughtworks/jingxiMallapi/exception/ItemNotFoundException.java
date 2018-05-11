package com.thoughtworks.jingxiMallapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(String className, Long id) {
        super("Cannot find such " + className + " with id " + id + ".");
    }

    public ItemNotFoundException(String className, String idName1, Long id1, String idName2, Long id2) {
        super(String.format("Cannot find such %s with %s: %d and %s: %d", className, idName1, id1, idName2, id2));
    }
}
