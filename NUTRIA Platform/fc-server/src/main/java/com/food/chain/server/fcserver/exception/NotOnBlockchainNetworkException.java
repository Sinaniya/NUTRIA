package com.food.chain.server.fcserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NotOnBlockchainNetworkException extends RuntimeException {
    private String resourceName;
    private String fieldName;
    private Object fieldValue;

    public NotOnBlockchainNetworkException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("Error while retrieving %s from the Ethereum network," +
                " '%s' is not stored on the network: %s", resourceName, fieldValue, fieldName));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}
