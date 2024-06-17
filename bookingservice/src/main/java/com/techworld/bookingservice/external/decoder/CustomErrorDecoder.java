package com.techworld.bookingservice.external.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techworld.bookingservice.exception.BookingServiceException;
import com.techworld.bookingservice.model.ErrorResponse;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String arg0, Response response) {

        log.info("::{}", response.request().url());
        log.info("::{}", response.request().headers());

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            ErrorResponse errorResponse = objectMapper.readValue(response.body().asInputStream(), ErrorResponse.class);

            return new BookingServiceException(errorResponse.getErrorMessage(), errorResponse.getErrorCode(), response.status());

        } catch (IOException e) {
            throw new BookingServiceException("Internal Server Error", "INTERNAL_SERVER_ERROR", 500);
        }
    }

}
