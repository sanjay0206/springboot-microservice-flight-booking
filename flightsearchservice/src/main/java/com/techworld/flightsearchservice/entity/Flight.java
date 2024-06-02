package com.techworld.flightsearchservice.entity;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "flights")
@Builder
public record Flight(
        @Id Long flightId,
        @Indexed(unique = true)
        String flightNumber,
        String origin,
        String destination,
        LocalDate departureDate,
        LocalDate arrivalDate,
        double amount,
        int totalSeats,
        int availableSeats) {
}
