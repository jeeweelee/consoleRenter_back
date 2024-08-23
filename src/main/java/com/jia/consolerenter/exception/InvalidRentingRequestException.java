package com.jia.consolerenter.exception;

public class InvalidRentingRequestException extends RuntimeException {
    public InvalidRentingRequestException(String message){
        super(message);
    }
}
