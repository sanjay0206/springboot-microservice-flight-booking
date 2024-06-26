package com.techworld.flightservice.service;

import com.techworld.flightservice.model.FlightRequest;
import com.techworld.flightservice.model.FlightResponse;

import java.util.List;

public interface FlightService {
    void indexToFlightSearchService(Long flightId, FlightRequest flightRequest);

    FlightResponse createFlight(FlightRequest flightRequest);

    List<FlightResponse> getAllFlights();

    FlightResponse getFlightByNumber(String flightNumber);

    void reserveSeats(String flightNumber, int seats);

}
