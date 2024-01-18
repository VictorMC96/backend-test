package com.neology.parkingneology.controller;

import com.neology.parkingneology.model.Parking;
import com.neology.parkingneology.model.Vehicle;
import com.neology.parkingneology.service.ParkingService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@AllArgsConstructor
@RestController
@RequestMapping(value = "/parking")
public class ParkingController {

    private final ParkingService parkingService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/entry/{plate}")
    public Parking registerEntry(@PathVariable("plate") String plate) {
        return parkingService.registerEntry(plate);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/exit/{plate}")
    public Double registerExit(@PathVariable("plate") String plate) {
        Parking parking = parkingService.registerExit(plate);
        return parking.getCost()*parking.getMinutes();
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/official/{plate}")
    public Vehicle registerOfficial(@PathVariable("plate") String plate) {
        return parkingService.registerOfficial(plate);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/resident/{plate}")
    public Vehicle registerResident(@PathVariable("plate") String plate) {
        return parkingService.registerResident(plate);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/start-month")
    public List<Parking> startMonth() {
        return parkingService.startMonth();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/pay/{file-name}")
    public ResponseEntity pay(@PathVariable("file-name") String fileName) {
        String response = parkingService.pay(fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/plain"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + ".txt\"")
                .body(response);

    }


}


