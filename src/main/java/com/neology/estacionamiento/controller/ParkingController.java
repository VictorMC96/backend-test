package com.neology.estacionamiento.controller;

import com.neology.estacionamiento.service.ParkingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parking")
@RequiredArgsConstructor
public class ParkingController {

    private final ParkingService parkingService;

    @PostMapping("/entrada")
    public ResponseEntity<Void> registrarEntrada(@RequestParam String placa) {
        parkingService.registrarEntrada(placa);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/salida")
    public ResponseEntity<Double> registrarSalida(@RequestParam String placa) {
        double monto = parkingService.registrarSalida(placa);
        return ResponseEntity.ok(monto);
    }

    @PostMapping("/alta/oficial")
    public ResponseEntity<Void> altaOficial(@RequestParam String placa) {
        parkingService.altaOficial(placa);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/alta/residente")
    public ResponseEntity<Void> altaResidente(@RequestParam String placa) {
        parkingService.altaResidente(placa);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/comienzaMes")
    public ResponseEntity<Void> comenzarMes() {
        parkingService.comenzarMes();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reporte/residentes")
    public ResponseEntity<String> reporteResidentes() {
        return ResponseEntity.ok(parkingService.generarReporteResidentes());
    }
}
