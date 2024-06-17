package com.techworld.bookingservice.model;

import lombok.Data;

@Data
public sealed class BookingRequest permits FlightBookingRequest, HotelBookingRequest {

    private String flightNumber;
    private String passengerName;
    private PaymentMode paymentMode;
    private double amount;
    private int seats;
}
