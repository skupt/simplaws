package com.example.simplaws.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.InternalServerErrorException;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;

@ControllerAdvice
public class DefaultAdvice {

    private static final String ERROR_CONTENT_START = "An error occured. ";

    @Data
    public class Response {
        private String msg;

        public Response() {
        }

        public Response(String msg) {
            this.msg = ERROR_CONTENT_START + msg;
        }
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<Response> handleException(InternalServerErrorException e) {
        Response response = new Response(e.getMessage() + ". " + e.getCause());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Response> handleException(ResourceNotFoundException e) {
        Response response = new Response(e.getMessage() + ". " + e.getCause());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConditionalCheckFailedException.class)
    public ResponseEntity<Response> handleException(ConditionalCheckFailedException e) {
        Response response = new Response(e.getMessage() + ". " + e.getCause());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DynamoDbException.class)
    public ResponseEntity<Response> handleException(DynamoDbException e) {
        Response response = new Response(e.getMessage() + ". " + e.getCause());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Response> handleException(NullPointerException e) {
        Response response = new Response(e.getMessage() + ". " + e.getCause());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AwsServiceException.class)
    public ResponseEntity<Response> handleException(AwsServiceException e) {
        Response response = new Response(e.getMessage() + ". " + e.getCause());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SdkClientException.class)
    public ResponseEntity<Response> handleException(SdkClientException e) {
        Response response = new Response(e.getMessage() + ". " + e.getCause());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
