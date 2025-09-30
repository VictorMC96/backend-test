package com.plh.parking.controller;

import com.plh.parking.model.dto.StayResponseDto;
import com.plh.parking.service.ParkingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping(path = ParkingController.API_PARK)
public class ParkingController {

    public static final String API_PARK = "/api/parking";
    public static final String API_PRK_ENTRY = "/entry/{plate}";
    public static final String API_PRK_EXIT = "/exit/{plate}";

    private final ParkingService parkingService;

    @PostMapping(API_PRK_ENTRY)
    public ResponseEntity<?> saveEntry(@PathVariable String plate) {
        parkingService.registerEntry(plate);
        return ResponseEntity.ok(Map.of("message", "Entrada registrada"));
    }

    @PostMapping(API_PRK_EXIT)
    public ResponseEntity<?> saveExit(@PathVariable String plate) {
        BigDecimal amount = parkingService.registerExit(plate);
        return ResponseEntity.ok(Map.of(
                "message", "Salida registrada",
                "amount", amount
        ));
    }

    @GetMapping()
    public List<StayResponseDto> getStays() {
        return parkingService.getStays();
    }


}
