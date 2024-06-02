package com.techworld.flightservice.controller;

import com.techworld.flightservice.model.FlightRequest;
import com.techworld.flightservice.model.FlightResponse;
import com.techworld.flightservice.service.FlightService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/flights")
@RequiredArgsConstructor
@Log4j2
public class FlightController {

    private final FlightService flightService;

    @PostMapping
    public ResponseEntity<FlightResponse> createFlight(@RequestBody FlightRequest flightRequest) {
        log.info("createFlight");
        var flight = flightService.createFlight(flightRequest);
        return new ResponseEntity<>(flight, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FlightResponse>> getAllFlights() {
        log.info("getAllFlights");
        return new ResponseEntity<>(flightService.getAllFlights(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public FlightResponse getFlightByNumber(@PathVariable("id") String flightNumber) {
        log.info("getFlightByNumber");
        return flightService.getFlightByNumber(flightNumber);
    }

    @PutMapping("/reserveSeats/{id}")
    public void reserveSeats(@PathVariable("id") String flightNumber,
                             @RequestParam int seats) {
        log.info("reserveSeats");
        flightService.reserveSeats(flightNumber, seats);
    }

}
