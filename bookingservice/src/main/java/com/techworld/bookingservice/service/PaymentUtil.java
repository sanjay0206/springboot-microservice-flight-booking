package com.techworld.bookingservice.service;

import com.techworld.bookingservice.entity.Booking;
import com.techworld.bookingservice.model.PaymentMode;
import com.techworld.bookingservice.model.PaymentRequest;
import org.springframework.stereotype.Component;

@Component
public class PaymentUtil {

    public PaymentRequest getPaymentRequest(Booking booking) {

        return PaymentRequest.builder()
                .bookingId(booking.getId())
                .amount(booking.getAmount())
                .referenceNumber(booking.getBookingNumber())
                .paymentMode(PaymentMode.valueOf(booking.getPaymentMode()))
                .build();
    }
}
