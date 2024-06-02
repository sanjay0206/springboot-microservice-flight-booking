package com.techworld.flightsearchservice.service;

import com.techworld.flightsearchservice.entity.Flight;
import com.techworld.flightsearchservice.model.FlightRequest;
import com.techworld.flightsearchservice.model.FlightSearchRequest;
import com.techworld.flightsearchservice.model.FlightSearchResponse;
import com.techworld.flightsearchservice.repository.FlightSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class FlightSearchServiceImpl implements FlightSearchService {

    private final FlightSearchRepository flightSearchRepository;

    public List<FlightSearchResponse> searchFlights(FlightSearchRequest flightSearchRequest) {
        List<Flight> flights = flightSearchRepository
                .findByOriginAndDestinationAndDepartureDateGreaterThanEqualAndAvailableSeatsGreaterThanEqual(
                        flightSearchRequest.origin(),
                        flightSearchRequest.destination(),
                        flightSearchRequest.travelDate(),
                        flightSearchRequest.passengers());

        List<FlightSearchResponse> flightSearchResponseList = flights
                .stream()
                .map(this::mapToFlightSearchResponse)
                .collect(Collectors.toList());

        log.info("flightSearchResponseList {} ", flightSearchResponseList);
        return flightSearchResponseList;
    }

    public FlightSearchResponse indexFlight(FlightRequest flightRequest) {

        Flight flight = Flight.builder()
                .flightId(flightRequest.flightId())
                .flightNumber(flightRequest.flightNumber())
                .origin(flightRequest.origin())
                .destination(flightRequest.destination())
                .departureDate(flightRequest.departureDate())
                .arrivalDate(flightRequest.arrivalDate())
                .totalSeats(flightRequest.totalSeats())
                .availableSeats(flightRequest.availableSeats())
                .amount(flightRequest.amount())
                .build();

        flight = flightSearchRepository.save(flight);

        FlightSearchResponse flightSearchResponse = new FlightSearchResponse();
        BeanUtils.copyProperties(flight, flightSearchResponse);

        log.info("Flight Indexed {} ", flightSearchResponse.getFlightId());
        return flightSearchResponse;
    }


    private FlightSearchResponse mapToFlightSearchResponse(Flight flight) {
        FlightSearchResponse flightSearchResponse = new FlightSearchResponse();
        BeanUtils.copyProperties(flight, flightSearchResponse);
        return flightSearchResponse;
    }
}
