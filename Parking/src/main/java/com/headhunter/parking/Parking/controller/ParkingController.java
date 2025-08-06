package com.headhunter.parking.Parking.controller;


import com.headhunter.parking.Parking.constants.CatTypeVehicleENUM;
import com.headhunter.parking.Parking.dto.RespuestaServicioDto;
import com.headhunter.parking.Parking.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("parking")
public class ParkingController {

    @Autowired
    private ParkingService parkingService;

    @PostMapping(value = "/registerEntry", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RespuestaServicioDto> registerEntry(@RequestParam String numberPlate) {
        return ResponseEntity.ok(parkingService.addVehicle(numberPlate, null, LocalDateTime.now(), null));
    }

    @PostMapping(value = "/registerExit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RespuestaServicioDto> registerExit(@RequestParam String numberPlate) {

        return ResponseEntity.ok(parkingService.addVehicle(numberPlate, null, null, LocalDateTime.now()));
    }

    @PostMapping(value = "/addOfficial", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RespuestaServicioDto> addOfficial(@RequestParam String numberPlate) {

        return ResponseEntity.ok(parkingService.addVehicle(numberPlate, CatTypeVehicleENUM.OFFICIAL, null, null));
    }

    @PostMapping(value = "/addResident", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RespuestaServicioDto> addResident(@RequestParam String numberPlate) {
        return ResponseEntity.ok(parkingService.addVehicle(numberPlate, CatTypeVehicleENUM.RESIDENT, null, null));
    }

    @PostMapping(value = "/initMonth", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RespuestaServicioDto> initMonth(@RequestParam String numberPlate) {
        return ResponseEntity.ok(parkingService.restartResident());

    }

    @PostMapping(value = "/payResident", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RespuestaServicioDto> payResident(@RequestParam String fileName) {
        return ResponseEntity.ok(parkingService.generateFile(fileName));
    }
}

