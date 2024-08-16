package com.techworld.notificationservice.listener;

import brave.Tracer;
import com.techworld.notificationservice.event.BookingCompletedEvent;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class BookingCompletedEventListener {

    private final ObservationRegistry observationRegistry;
    private final Tracer tracer;

    @KafkaListener(topics = "notificationTopic", groupId = "notificationId")
    public void handleNotification(BookingCompletedEvent bookingCompletedEvent) {
        Observation.createNotStarted("on-message", this.observationRegistry).observe(() -> {
            log.info("Got message <{}>", bookingCompletedEvent);

            log.info("TraceId- {}, Received Notification for Booking - {}",
                    this.tracer.currentSpan().context().traceId(),
                    bookingCompletedEvent.getBookingNumber());

            // send out an email notification
        });
    }
}
