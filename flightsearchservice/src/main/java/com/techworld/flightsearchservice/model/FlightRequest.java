package com.techworld.flightsearchservice.model;

import lombok.Builder;

import java.time.LocalDate;

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
