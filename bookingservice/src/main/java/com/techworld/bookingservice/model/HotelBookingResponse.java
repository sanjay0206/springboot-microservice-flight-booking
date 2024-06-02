package com.techworld.bookingservice.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public final class HotelBookingResponse extends BookingResponse {

    private String hotelName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
