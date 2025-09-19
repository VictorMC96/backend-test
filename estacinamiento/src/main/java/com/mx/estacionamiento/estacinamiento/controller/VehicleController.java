package com.mx.estacionamiento.estacinamiento.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mx.estacionamiento.estacinamiento.dto.PaymentReportDTO;
import com.mx.estacionamiento.estacinamiento.dto.VehicleRequest;
import com.mx.estacionamiento.estacinamiento.service.VehicleService;

@RestController
@RequestMapping("/api/vehicles")

public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping("/register")
    public String registerVehicle(@RequestBody VehicleRequest req) {
        vehicleService.registerVehicle(req.getPlate(), req.getType());
        return "Vehículo registrado";
    }

    @PostMapping("/{plate}/entry")
    public String registerEntry(@PathVariable String plate) {
        vehicleService.registerEntry(plate);
        return "Entrada registrada";
    }

    @PostMapping("/{plate}/exit")
    public String registerExit(@PathVariable String plate) {
        double amount = vehicleService.registerExit(plate);
        return "Salida registrada. Monto a pagar: " + amount;
    }

    @GetMapping("/residents/report")
    public List<PaymentReportDTO> reportResidents() {
        return vehicleService.generateResidentReport();
    }

    @PostMapping("/reset-month")
    public String resetMonth() {
        vehicleService.resetMonth();
        return "Datos reseteados para nuevo mes";
    }

}
