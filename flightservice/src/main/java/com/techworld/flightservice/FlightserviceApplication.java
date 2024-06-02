package com.techworld.flightservice;

import com.techworld.flightservice.entity.Flight;
import com.techworld.flightservice.repository.FlightRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.Arrays;

@SpringBootApplication
@EnableDiscoveryClient
public class FlightserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlightserviceApplication.class, args);
        System.out.println("FlightserviceApplication is running...");
    }

    @Bean
    CommandLineRunner commandLineRunner(FlightRepository flightRepository) {
        return args -> {

            // Create sample flights
            Flight flight1 = new Flight(1L, "EK203", "DXB", "SWZ",
                    LocalDate.parse("2024-06-23"), LocalDate.parse("2024-06-23"), 15000.00, 250, 130);
            Flight flight2 = new Flight(2L, "EK204", "DXB", "SWZ",
                    LocalDate.parse("2024-06-22"), LocalDate.parse("2024-06-24"), 18000.00, 250, 200);
            Flight flight3 = new Flight(3L, "EK205", "IND", "DXB",
                    LocalDate.parse("2024-06-25"), LocalDate.parse("2024-06-27"), 10000.00, 250, 150);

            // Save flights to the database
            flightRepository.saveAll(Arrays.asList(flight1, flight2, flight3));

        };
    }
}
