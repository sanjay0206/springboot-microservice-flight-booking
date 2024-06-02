package com.techworld.bookingservice.repository;

import com.techworld.bookingservice.entity.HotelBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelBookingRepository extends JpaRepository<HotelBooking, Long> {

}
