package com.techworld.flightservice.model;

import lombok.Builder;

import java.time.LocalDate;


// By default, all the fields and method will be private and final
@Builder
public record FlightRequest(
        String flightNumber,
        String origin,
        String destination,
        LocalDate departureDate,
        LocalDate arrivalDate,
        int totalSeats,
        int availableSeats,
        double amount) {

}
