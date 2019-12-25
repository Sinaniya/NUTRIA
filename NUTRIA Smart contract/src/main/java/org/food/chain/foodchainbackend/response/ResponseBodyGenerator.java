package org.food.chain.foodchainbackend.response;

import org.food.chain.foodchainbackend.constant.OperationType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class ResponseBodyGenerator {

    public static ResponseEntity<?> generateSuccessResponceBody(String resourceType, Long resourceId,
                                                                OperationType operationType, HttpStatus httpStatus, String token) {
        return ResponseEntity.ok().body(new SuccessResponseBody(resourceType, resourceId, operationType, httpStatus, token));
    }


    public static ResponseEntity<?> generateErrorResponseBody(String resourceType, Long resourceId,
                                                              OperationType operationType, HttpStatus httpStatus, Map<String, String> errors) {
        return ResponseEntity.badRequest().body(new ErrorResponseBody(resourceType, resourceId, operationType, httpStatus, errors));
    }

}
