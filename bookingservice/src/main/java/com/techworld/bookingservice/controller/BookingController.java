package com.techworld.bookingservice.controller;

import com.techworld.bookingservice.model.BookingResponse;
import com.techworld.bookingservice.model.FlightBookingRequest;
import com.techworld.bookingservice.model.FlightBookingResponse;
import com.techworld.bookingservice.model.HotelBookingRequest;
import com.techworld.bookingservice.service.BookingService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/bookings")
@Log4j2
public class BookingController {

    private final BookingService flightBookingService;

    private final BookingService hotelBookingService;

    public BookingController(@Qualifier("flightBookingService") BookingService flightBookingService,
                             @Qualifier("hotelBookingService") BookingService hotelBookingService) {
        this.hotelBookingService = hotelBookingService;
        this.flightBookingService = flightBookingService;
    }

    @PostMapping("/flight")
    public BookingResponse createFlightBooking(@RequestBody FlightBookingRequest flightBookingRequest) {
        return (FlightBookingResponse) flightBookingService.createBooking(flightBookingRequest);
    }

    @PostMapping("/hotel")
    public BookingResponse createHotelBooking(@RequestBody HotelBookingRequest hotelBookingRequest) {
        return hotelBookingService.createBooking(hotelBookingRequest);
    }

}
