package com.food.chain.server.fcserver.response;

import com.food.chain.server.fcserver.constant.OperationType;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.Map;

public class ErrorResponseBody {

    private Date timestamp;
    private int httpStatus;
    private String resourceName;
    private Long resourceId;
    private String message;
    private OperationType operationType;
    private Map<String, String> errors;

    public ErrorResponseBody(String resourceName, Long resourceId, OperationType operationType, HttpStatus httpStatus, Map<String, String> errors) {
        this.timestamp = new Date();
        this.httpStatus = httpStatus.value();
        this.resourceName = resourceName;
        this.resourceId = resourceId;
        this.operationType = operationType;
        this.message = createMessage(resourceName, operationType);
        this.errors = errors;
    }

    public ErrorResponseBody() {
    }

    private String createMessage(String objectName, OperationType operation) {
        String operationPerformed = "";
        switch (operationType) {
            case CREATE:
                operationPerformed = "creating";
                break;
            case UPDATE:
                operationPerformed = "updating";
                break;
            case DELETE:
                operationPerformed = "deleting";
                break;
            case LOGIN:
                operationPerformed = "logging in";
                break;
            case LOGOUT:
                operationPerformed = "logging out";
                break;
        }
        return String.format("Error while %s %s", operationPerformed, objectName);
    }


    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
