package com.ansar.moneymanaer_api.exception;

public class CategoryAlreadyExistException extends RuntimeException {

    public CategoryAlreadyExistException(String message) {
        super(message);
    }
}
