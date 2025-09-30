package com.plh.parking.controller;


import com.plh.parking.model.dto.VehicleDto;
import com.plh.parking.model.dto.VehicleResponseDto;
import com.plh.parking.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping(VehicleCotroller.API_VEHICLE)
public class VehicleCotroller {
    public static final String API_VEHICLE = "/api/vehicle";

    private final VehicleService vehicleService;


    @PostMapping()
    public ResponseEntity<VehicleDto> save(@Valid @RequestBody VehicleDto vehicleDto) {
        vehicleService.saveVehicle(vehicleDto);
        return ResponseEntity.ok(vehicleDto);
    }


    @GetMapping()
    public List<VehicleResponseDto> findAll() {
        return vehicleService.findAll();
    }


}
