package org.food.chain.foodchainbackend.response;


import org.food.chain.foodchainbackend.constant.OperationType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;

@ResponseStatus(value = HttpStatus.OK)
public class SuccessResponseBody {

    private Date timestamp;
    private int httpStatus;
    private String resourceName;
    private Long resourceId;
    private String message;
    private OperationType operationType;
    private String path;
    private String token;

    public SuccessResponseBody(String resourceName, Long resourceId, OperationType operationType, HttpStatus httpStatus, String token) {
        this.timestamp = new Date();
        this.httpStatus = httpStatus.value();
        this.resourceName = resourceName;
        this.resourceId = resourceId;
        this.operationType = operationType;
        this.message = createMessage(resourceName, operationType);
        this.path = createPath(resourceName, operationType);
        this.token = token;
    }

    public SuccessResponseBody() {
    }

    private String createMessage(String objectName, OperationType operation) {
        String operationPerformed = "";
        switch (operationType) {
            case CREATE:
                operationPerformed = "created";
                break;
            case UPDATE:
                operationPerformed = "updated";
                break;
            case DELETE:
                operationPerformed = "deleted";
                break;
            case LOGIN:
                operationPerformed = "logged in";
                break;
            case LOGOUT:
                operationPerformed = "logged out";
                break;
        }
        return String.format("%s is successfully %s", objectName, operationPerformed);
    }

    private String createPath(String resourceName, OperationType operationType) {
        if (operationType == OperationType.CREATE ||
                operationType == OperationType.UPDATE ||
                operationType == OperationType.LOGIN)
            return String.format("/%ss/%s", resourceName, resourceId);
        return "";
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
