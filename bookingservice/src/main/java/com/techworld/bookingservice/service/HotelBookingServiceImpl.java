package com.techworld.bookingservice.service;

import com.techworld.bookingservice.entity.BookingStatus;
import com.techworld.bookingservice.entity.HotelBooking;
import com.techworld.bookingservice.model.BookingRequest;
import com.techworld.bookingservice.model.HotelBookingRequest;
import com.techworld.bookingservice.model.HotelBookingResponse;
import com.techworld.bookingservice.repository.HotelBookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@Qualifier("hotelBookingService")
@RequiredArgsConstructor
@Log4j2
public class HotelBookingServiceImpl implements BookingService {

    private final HotelBookingRepository hotelBookingRepository;

    @Override
    public HotelBookingResponse createBooking(BookingRequest bookingRequest) {
        log.info("bookingRequest: " + bookingRequest);
        if (!(bookingRequest instanceof HotelBookingRequest)) {
            throw new IllegalArgumentException("Invalid booking type");
        }

        HotelBooking hotelBooking = mapToHotelBooking(bookingRequest);

        // validate Hotel Booking
        hotelBooking = hotelBookingRepository.save(hotelBooking);

        HotelBookingResponse hotelBookingResponse = new HotelBookingResponse();
        BeanUtils.copyProperties(hotelBooking, hotelBookingResponse);

        return hotelBookingResponse;
    }

    private HotelBooking mapToHotelBooking(BookingRequest bookingRequest) {
        HotelBookingRequest hotelBookingRequest = (HotelBookingRequest) bookingRequest;

        HotelBooking hotelBooking = new HotelBooking();

        hotelBooking.setBookingNumber(UUID.randomUUID().toString());
        hotelBooking.setBookingDate(LocalDate.now());

        hotelBooking.setPassengerName(hotelBookingRequest.getPassengerName());
        hotelBooking.setAmount(hotelBookingRequest.getAmount());
        hotelBooking.setPaymentMode(hotelBookingRequest.getPaymentMode().name());
        hotelBooking.setStatus(BookingStatus.CREATED.name());

        hotelBooking.setHotelName(hotelBookingRequest.getHotelName());
        hotelBooking.setCheckInDate(hotelBookingRequest.getCheckInDate());
        hotelBooking.setCheckOutDate(hotelBookingRequest.getCheckOutDate());

        return hotelBooking;
    }

}
