package com.techworld.flightservice.exception;

import lombok.Data;

@Data
public class FlightServiceException extends RuntimeException {

    private String errorCode;

    public FlightServiceException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}
