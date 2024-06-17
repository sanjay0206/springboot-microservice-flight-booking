
package com.techworld.paymentservice.controller;

import com.techworld.paymentservice.model.PaymentRequest;
import com.techworld.paymentservice.model.PaymentResponse;
import com.techworld.paymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public Long processPayment(@RequestBody PaymentRequest paymentRequest) {
        System.out.println("Reaching here to processPayment");
        return paymentService.processPayment(paymentRequest);
    }

    @GetMapping
    public ResponseEntity<PaymentResponse> getPaymentDetailsByBookingNumber(@RequestParam(name = "id") Long id) {
        return new ResponseEntity<>(
                paymentService.getPaymentDetailsByBookingId(id),
                HttpStatus.OK);
    }
}
