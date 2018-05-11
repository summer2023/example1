package com.thoughtworks.jingxiMallapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InputProductInvalidException extends RuntimeException{
    public InputProductInvalidException() {
        super("Input product illegal!");
    }
}
