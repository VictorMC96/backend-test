package com.neology.app.context.parking.infrastructure.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neology.app.context.amount.domain.Amount;
import com.neology.app.context.parking.application.ParkingRegisterEntry;
import com.neology.app.context.parking.application.ParkingRegisterExit;
import com.neology.app.context.vehicle.application.VehicleOfficialSave;
import com.neology.app.context.vehicle.application.VehicleResidentSave;
import com.neology.app.context.vehicle.domain.Vehicle;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/v1/parking")
@AllArgsConstructor
public class ParkingController {
    private final VehicleOfficialSave vehicleOfficialSave;
    private final VehicleResidentSave vehicleResidentSave;
    private final ParkingRegisterEntry parkingRegisterEntry;
    private final ParkingRegisterExit parkingRegisterExit;

    @PostMapping("/register-vehicle/official")
    public Vehicle createOfficial(@RequestBody @Valid Vehicle vehicle) {
        return vehicleOfficialSave.save(vehicle);
    }

    @PostMapping("/register-vehicle/resident")
    public Vehicle createResident(@RequestBody @Valid Vehicle vehicle) {
        return vehicleResidentSave.save(vehicle);
    }

    @PostMapping("/register-entry/{plate}")
    public void registerEntry(@PathVariable(value = "") String plate) {
        parkingRegisterEntry.run(plate);
    }

    @PostMapping("/register-exit/{plate}")
    public Amount registerExit(@PathVariable(value = "") String plate) {
        return parkingRegisterExit.run(plate);
    }

    @PostMapping("/starting-month")
    public void startingMonth() {
    }

}
