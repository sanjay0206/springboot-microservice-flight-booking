package com.techworld.bookingservice.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookingServiceException extends RuntimeException {

    private String errorCode;
    private int status;

    public BookingServiceException(String message, String errorCode, int status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }

}
