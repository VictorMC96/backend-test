package com.estacionamiento.parking.config;

import com.estacionamiento.parking.service.ParkingService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class StartupData implements CommandLineRunner {

    private final ParkingService service;

    @Override
    public void run(String... args) {

        service.registerOfficial("OFI-123");
        service.registerResident("RES-456");
    }
}