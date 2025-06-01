package com.techworld.bookingservice.external.client;


import com.techworld.bookingservice.exception.BookingServiceException;
import com.techworld.bookingservice.model.PaymentRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@CircuitBreaker(name = "external", fallbackMethod = "paymentServiceFallback")
@FeignClient(contextId = "payment",
        name = "payment-service",
        path = "/v1/api/payments",
        configuration = FeignClientConfig.class)
public interface PaymentService {
    @PostMapping
    Long processPayment(PaymentRequest paymentRequest);

    default Long paymentServiceFallback(PaymentRequest paymentRequest, Throwable t) {
        throw new BookingServiceException("Payment Service is not available", "UNAVAILABLE", 503);
    }
}
