package com.techworld.bookingservice.external.client;

import com.techworld.bookingservice.exception.BookingException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@CircuitBreaker(name = "external", fallbackMethod = "flightServiceFallback")
@FeignClient(contextId = "flight", value = "api-gateway", path = "/flight-service/v1/api/flights", configuration = FeignClientConfig.class)
public interface FlightService {
    @PutMapping("/reserveSeats/{id}")
    void reserveSeats(@PathVariable("id") String flightNumber, @RequestParam int seats);

    default void flightServiceFallback(Exception e) {
        throw new BookingException("Flight Service is not available", "UNAVAILABLE", 500);
    }
}
