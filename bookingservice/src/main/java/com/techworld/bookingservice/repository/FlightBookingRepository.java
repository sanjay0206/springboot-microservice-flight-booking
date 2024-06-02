package com.techworld.bookingservice.repository;

import com.techworld.bookingservice.entity.FlightBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightBookingRepository extends JpaRepository<FlightBooking, Long> {

}
