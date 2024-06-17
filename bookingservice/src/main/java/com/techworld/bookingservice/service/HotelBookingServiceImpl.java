package com.techworld.bookingservice.service;

import com.techworld.bookingservice.entity.BookingStatus;
import com.techworld.bookingservice.entity.FlightBooking;
import com.techworld.bookingservice.entity.HotelBooking;
import com.techworld.bookingservice.event.BookingCompletedEvent;
import com.techworld.bookingservice.external.client.PaymentService;
import com.techworld.bookingservice.model.*;
import com.techworld.bookingservice.repository.HotelBookingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

@Service
@Qualifier("hotelBookingService")
@RequiredArgsConstructor
@Log4j2
public class HotelBookingServiceImpl implements BookingService {

    private final PaymentService paymentService;
    private final PaymentUtil paymentUtil;
    private final HotelBookingRepository hotelBookingRepository;
    private final KafkaTemplate<String, BookingCompletedEvent> kafkaTemplate;

    @Override
    @Transactional(rollbackOn = SQLException.class)
    public HotelBookingResponse createBooking(BookingRequest bookingRequest) {

        log.info("bookingRequest: " + bookingRequest);
        if (!(bookingRequest instanceof HotelBookingRequest)) {
            throw new IllegalArgumentException("Invalid booking type");
        }

        HotelBooking hotelBooking = mapToHotelBooking(bookingRequest);
        hotelBooking = hotelBookingRepository.save(hotelBooking);

        // Do payment
        long paymentId = paymentService.processPayment(paymentUtil.getPaymentRequest(hotelBooking));
        log.info("Payment service call is success with paymentID {} ", paymentId);
        hotelBooking.setStatus(BookingStatus.CONFIRMED.name());

        // Publish booking completed event to Notification Topic
        BookingCompletedEvent bookingCompletedEvent = new BookingCompletedEvent(hotelBooking.getBookingNumber());
        log.info("Sending event to notificationTopic with event {}", bookingCompletedEvent);

        // Send the event using kafka template to notificationTopic
        kafkaTemplate.send("notificationTopic", bookingCompletedEvent);

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

