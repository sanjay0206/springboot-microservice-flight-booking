package com.techworld.apigateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @PostMapping("/bookingServiceFallBack")
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String bookingServiceFallBack() {
        return "Booking Service is down!";
    }

    @GetMapping("/flightSearchServiceFallBack")
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String flightSearchServiceFallBack() {
        return "Flight Search Service is down!";
    }

    @GetMapping("/flightServiceFallBack")
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String flightServiceFallBack() {
        return "Flight Service is down!";
    }
}
