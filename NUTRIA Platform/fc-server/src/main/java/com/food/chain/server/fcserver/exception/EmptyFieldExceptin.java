package com.food.chain.server.fcserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EmptyFieldExceptin extends RuntimeException {
    private String resourceName;
    private String fieldName;

    public EmptyFieldExceptin(String resourceName, String fieldName) {
        super(String.format("Error while saving %s, the field: '%s' can't be empty", resourceName, fieldName));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
