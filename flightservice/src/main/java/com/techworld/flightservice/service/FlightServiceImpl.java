package com.techworld.flightservice.service;

import com.techworld.flightservice.entity.Flight;
import com.techworld.flightservice.exception.FlightServiceCustomException;
import com.techworld.flightservice.model.FlightRequest;
import com.techworld.flightservice.model.FlightResponse;
import com.techworld.flightservice.repository.FlightRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;

    @Override
    public FlightResponse createFlight(FlightRequest flightRequest) {

        Flight flight = Flight.builder()
                .flightNumber(flightRequest.flightNumber())
                .origin(flightRequest.origin())
                .destination(flightRequest.destination())
                .departureDate(flightRequest.departureDate())
                .arrivalDate(flightRequest.arrivalDate())
                .totalSeats(flightRequest.totalSeats())
                .availableSeats(flightRequest.availableSeats())
                .amount(flightRequest.amount())
                .build();

        flight = flightRepository.save(flight);

        FlightResponse flightResponse = new FlightResponse();
        BeanUtils.copyProperties(flight, flightResponse);

        log.info("Flight Created {} ", flightResponse.getFlightId());
        return flightResponse;
    }

    @Override
    public List<FlightResponse> getAllFlights() {
        List<Flight> flights = flightRepository.findAll();
        List<FlightResponse> flightResponseList = flights
                .stream()
                .map(this::mapToFlightResponse)
                .collect(Collectors.toList());

        log.info("flightResponseList {} ", flightResponseList);
        return flightResponseList;
    }

    @Override
    public FlightResponse getFlightByNumber(String flightNumber) {
        log.info("Get the flight for flight Number: {}", flightNumber);
        Flight optionalFlight = flightRepository.findByFlightNumber(flightNumber)
                .orElseThrow(
                        () -> new FlightServiceCustomException("Flight with given number not found", "FLIGHT_NOT_FOUND"));

        FlightResponse flightResponse = new FlightResponse();

        // Copy properties from one object to another object using Reflection
        BeanUtils.copyProperties(optionalFlight, flightResponse);
        return flightResponse;
    }

    private FlightResponse mapToFlightResponse(Flight flight) {
        FlightResponse flightResponse = new FlightResponse();
        BeanUtils.copyProperties(flight, flightResponse);
        return flightResponse;
    }

    @Override
    @Transactional
    public void reserveSeats(String flightNumber, int seats) {
        log.info("Reserve seats {} for flight Number: {}", seats, flightNumber);

        Flight flight = flightRepository.findByFlightNumber(flightNumber)
                .orElseThrow(() -> new FlightServiceCustomException("Flight with given id not found", "FLIGHT_NOT_FOUND"));

        if (flight.getTotalSeats() < seats) {
            throw new FlightServiceCustomException("Flights does not have sufficient seats", "INSUFFICIENT_SEATS");
        }

        int totalSeats = flight.getTotalSeats() - seats;
        flight.setTotalSeats(totalSeats);

        flightRepository.save(flight);
        log.info("Flight Seats details updated Successfully");
    }
}
