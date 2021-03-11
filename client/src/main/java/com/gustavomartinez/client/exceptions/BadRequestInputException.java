package com.gustavomartinez.client.exceptions;

public class BadRequestInputException extends Exception{

    public BadRequestInputException() {}

    public BadRequestInputException(String message) {
        super(message);
    }
}
