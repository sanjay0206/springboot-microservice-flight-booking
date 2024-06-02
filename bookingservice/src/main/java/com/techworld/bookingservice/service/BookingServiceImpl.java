package com.techworld.bookingservice.service;

import com.techworld.bookingservice.entity.Booking;
import com.techworld.bookingservice.entity.BookingStatus;
import com.techworld.bookingservice.event.BookingCompletedEvent;
import com.techworld.bookingservice.external.client.FlightService;
import com.techworld.bookingservice.external.client.PaymentService;
import com.techworld.bookingservice.external.request.PaymentRequest;
import com.techworld.bookingservice.model.BookingRequest;
import com.techworld.bookingservice.model.BookingResponse;
import com.techworld.bookingservice.model.PaymentMode;
import com.techworld.bookingservice.repository.BookingRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.UUID;

@Slf4j
@Service
@Primary
public class BookingServiceImpl implements BookingService {

    int count = 1;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private FlightService flightService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private KafkaTemplate<String, BookingCompletedEvent> kafkaTemplate;

    @Override
    @Transactional(rollbackOn = SQLException.class)
    public String reserveSeats(BookingRequest bookingRequest) {
        log.info("create Booking for user {}", bookingRequest.getPassengerName());

        // Set the flight booking status as created
        Booking booking = Booking
                .builder()
                .passengerName(bookingRequest.getPassengerName())
                .flightNumber(bookingRequest.getFlightNumber())
                .seats(bookingRequest.getSeats())
                .bookingNumber(UUID.randomUUID().toString())
                .amount(bookingRequest.getAmount())
                .paymentMode(bookingRequest.getPaymentMode().name())
                .status(BookingStatus.CREATED.name())
                .build();
        bookingRepository.save(booking);
        log.info("booking status is {} ", booking.getStatus());

        try {
            flightService.reserveSeats(bookingRequest.getFlightNumber(), bookingRequest.getSeats());
            log.info("Seats are reserved for booking {}", bookingRequest.getFlightNumber());
        } catch (Exception e) {
            log.info("Error in calling flight service {}", bookingRequest.getFlightNumber());
            log.error(e.getMessage());
        }

        try {
            // Do payment
            long paymentId = paymentService.processPayment(getPaymentRequest(booking));
            log.info("Payment service call is success {} with paymentID ", paymentId);
            booking.setStatus(BookingStatus.CONFIRMED.name());

            // Publish booking completed event to Notification Topic
            BookingCompletedEvent bookingCompletedEvent = new BookingCompletedEvent(booking.getBookingNumber());
            log.info("Sending event to notificationTopic with event {}", bookingCompletedEvent);

            // Send the event using kafka template to notificationTopic
            kafkaTemplate.send("notificationTopic", bookingCompletedEvent);
            log.info("Booking status is {}", booking.getStatus());
        } catch (Exception e) {
            log.info("Error in calling payment service for Booking Number {}", booking.getBookingNumber());
            log.error(e.getMessage());
        }
        return "Booking Id created Successfully";
    }

    @Override
    public BookingResponse createBooking(BookingRequest bookingRequest) {
        throw new UnsupportedOperationException("Unimplemented method 'createBooking'");
    }

    private PaymentRequest getPaymentRequest(Booking booking) {

        return PaymentRequest.builder()
                .bookingId(booking.getId())
                .amount(booking.getAmount())
                .referenceNumber(booking.getBookingNumber())
                .paymentMode(PaymentMode.valueOf(booking.getPaymentMode()))
                .build();
    }
}