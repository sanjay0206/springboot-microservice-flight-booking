package com.techworld.flightsearchservice.controller;

import com.techworld.flightsearchservice.model.FlightRequest;
import com.techworld.flightsearchservice.model.FlightSearchRequest;
import com.techworld.flightsearchservice.model.FlightSearchResponse;
import com.techworld.flightsearchservice.service.FlightSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/search")
public class FlightSearchController {

    private final FlightSearchService flightSearchService;

    @GetMapping("/flights")
    public ResponseEntity<List<FlightSearchResponse>> searchFlights(@RequestBody FlightSearchRequest flightSearchRequest) {
        return new ResponseEntity<>(flightSearchService.searchFlights(flightSearchRequest), HttpStatus.OK);
    }

    @PostMapping("/indexFlight")
    public ResponseEntity<FlightSearchResponse> indexFlight(@RequestBody FlightRequest flightRequest) {
        var flight = flightSearchService.indexFlight(flightRequest);
        return new ResponseEntity<>(flight, HttpStatus.CREATED);
    }
}
