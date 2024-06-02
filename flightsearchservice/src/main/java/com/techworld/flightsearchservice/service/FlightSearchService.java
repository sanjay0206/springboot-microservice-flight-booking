package com.techworld.flightsearchservice.service;

import com.techworld.flightsearchservice.model.FlightRequest;
import com.techworld.flightsearchservice.model.FlightSearchRequest;
import com.techworld.flightsearchservice.model.FlightSearchResponse;

import java.util.List;

public interface FlightSearchService {
    List<FlightSearchResponse> searchFlights(FlightSearchRequest flightSearchRequest);

    FlightSearchResponse indexFlight(FlightRequest flightRequest);
}