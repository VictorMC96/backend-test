package com.neology.parking.controller;

import com.neology.parking.model.entity.*;
import com.neology.parking.service.VehicleService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping("/official")
    public Vehicle addOfficial(@RequestParam String plate) {
        OfficialVehicle v = new OfficialVehicle();
        v.setPlateNumber(plate);
        return vehicleService.saveVehicle(v);
    }

    @PostMapping("/resident")
    public Vehicle addResident(@RequestParam String plate) {
        ResidentVehicle v = new ResidentVehicle();
        v.setPlateNumber(plate);
        return vehicleService.saveVehicle(v);
    }

    @PostMapping("/reset")
    public void resetMonth() {
        vehicleService.resetMonth();
    }
}
