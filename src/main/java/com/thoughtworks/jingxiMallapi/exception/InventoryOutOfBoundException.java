package com.thoughtworks.jingxiMallapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InventoryOutOfBoundException extends RuntimeException {
    public InventoryOutOfBoundException(Long id) {
        super("There is insufficient inventory for the item which id is: " + id);
    }

}
