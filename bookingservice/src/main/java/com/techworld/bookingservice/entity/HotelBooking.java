package com.techworld.bookingservice.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("HOTEL")
public class HotelBooking extends Booking {

    private String hotelName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
