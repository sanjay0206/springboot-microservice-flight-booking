package com.techworld.bookingservice.service;

import com.techworld.bookingservice.entity.BookingStatus;
import com.techworld.bookingservice.entity.FlightBooking;
import com.techworld.bookingservice.event.BookingCompletedEvent;
import com.techworld.bookingservice.external.client.FlightService;
import com.techworld.bookingservice.external.client.PaymentService;
import com.techworld.bookingservice.model.*;
import com.techworld.bookingservice.repository.FlightBookingRepository;
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
@RequiredArgsConstructor
@Qualifier("flightBookingService")
@Log4j2
public class FlightBookingServiceImpl implements BookingService {

    private final FlightBookingRepository flightBookingRepository;
    private final FlightService flightService;
    private final PaymentService paymentService;
    private final KafkaTemplate<String, BookingCompletedEvent> kafkaTemplate;

    @Override
    @Transactional(rollbackOn = SQLException.class)
    public FlightBookingResponse createBooking(BookingRequest bookingRequest) {

        log.info("bookingRequest: " + bookingRequest);
        if (!(bookingRequest instanceof FlightBookingRequest)) {
            throw new IllegalArgumentException("Invalid booking type");
        }

        FlightBooking flightBooking = mapToFlightBooking(bookingRequest);
        flightBooking = flightBookingRepository.save(flightBooking);
        log.info("Created booking for user {}", bookingRequest.getPassengerName());
        log.info("booking status is {} ", flightBooking.getStatus());

        // Reserve the seats and check for seat availability and reduce the seats for flight
        flightService.reserveSeats(flightBooking.getFlightNumber(), flightBooking.getSeats());
        log.info("Seats are reserved for booking {}", flightBooking.getFlightNumber());

        // Do payment
        long paymentId = paymentService.processPayment(getPaymentRequest(flightBooking));
        log.info("Payment service call is success with paymentID {} ", paymentId);
        flightBooking.setStatus(BookingStatus.CONFIRMED.name());

        // Publish booking completed event to Notification Topic
        BookingCompletedEvent bookingCompletedEvent = new BookingCompletedEvent(flightBooking.getBookingNumber());
        log.info("Sending event to notificationTopic with event {}", bookingCompletedEvent);

        // Send the event using kafka template to notificationTopic
        kafkaTemplate.send("notificationTopic", bookingCompletedEvent);

        log.info("Flight {} is booked with booking ID: {}", flightBooking.getFlightNumber(), flightBooking.getBookingNumber());
        FlightBookingResponse flightBookingResponse = new FlightBookingResponse();
        BeanUtils.copyProperties(flightBooking, flightBookingResponse);

        return flightBookingResponse;
    }

    private FlightBooking mapToFlightBooking(BookingRequest bookingRequest) {
        FlightBookingRequest flightBookingRequest = (FlightBookingRequest) bookingRequest;

        FlightBooking flightBooking = new FlightBooking();
        flightBooking.setBookingNumber(UUID.randomUUID().toString());
        flightBooking.setFlightNumber(flightBookingRequest.getFlightNumber());
        flightBooking.setBookingDate(LocalDate.now());
        flightBooking.setPassengerName(flightBookingRequest.getPassengerName());
        flightBooking.setAmount(flightBookingRequest.getAmount());
        flightBooking.setPaymentMode(flightBookingRequest.getPaymentMode().name());
        flightBooking.setStatus(BookingStatus.CREATED.name());
        flightBooking.setSeats(flightBookingRequest.getSeats());

        return flightBooking;
    }

    private PaymentRequest getPaymentRequest(FlightBooking flightBooking) {

        return PaymentRequest.builder()
                .bookingId(flightBooking.getId())
                .amount(flightBooking.getAmount())
                .referenceNumber(flightBooking.getBookingNumber())
                .paymentMode(PaymentMode.valueOf(flightBooking.getPaymentMode()))
                .build();
    }

}
