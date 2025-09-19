package com.estacionamiento.parkingsystem.controller;

import com.estacionamiento.parkingsystem.service.EstacionamientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/estacionamiento")
@RequiredArgsConstructor
public class EstacionamientoController {
    private final EstacionamientoService estacionamientoService;

    // link = "http://localhost:8080/api/estacionamiento/entradas/{placa}"
    @PostMapping("/entradas/{placa}")
    public ResponseEntity<String> registrarEntrada(@PathVariable String placa) {
        estacionamientoService.registrarEntrada(placa);
        return ResponseEntity.ok("Entrada registrada para vehículo con placa: " + placa);
    }


    // link = "http://localhost:8080/api/estacionamiento/salidas/{placa}"
    @PostMapping("/salidas/{placa}")
    public ResponseEntity<String> registrarSalida(@PathVariable String placa) {
        double importe = estacionamientoService.registrarSalida(placa);

        if (importe > 0) {
            return ResponseEntity.ok("Salida registrada. Importe a pagar: $" + String.format("%.2f", importe));
        }
        return ResponseEntity.ok("Salida registrada sin costo para el vehículo con placa: " + placa);
    }

    // Dar de alta vehículo oficial
    // link = "http://localhost:8080/api/estacionamiento/vehiculos/oficiales/{placa}"
    @PostMapping("/vehiculos/oficiales/{placa}")
    public ResponseEntity<String> altaOficial(@PathVariable String placa) {
        estacionamientoService.darDeAltaVehiculoOficial(placa);
        return ResponseEntity.ok("Vehículo oficial agregado con placa: " + placa);
    }

    // Dar de alta vehículo residente
    // link = "http://localhost:8080/api/estacionamiento/vehiculos/residentes/{placa}"
    @PostMapping("/vehiculos/residentes/{placa}")
    public ResponseEntity<String> altaResidente(@PathVariable String placa) {
        estacionamientoService.darDeAltaVehiculoResidente(placa);
        return ResponseEntity.ok("Vehículo residente agregado con placa: " + placa);
    }

    // Comenzar mes
    // link = "http://localhost:8080/api/estacionamiento/comienzo-mes"
    @PostMapping("/comienzo-mes")
    public ResponseEntity<String> comenzarMes() {
        estacionamientoService.comenzarMes();
        return ResponseEntity.ok("Se reinició la información de estancias y acumulados.");
    }


    // Generar reporte de residentes
    // link = "http://localhost:8080/api/estacionamiento/reportes/residentes"
    @GetMapping("/reportes/residentes")
    public ResponseEntity<String> reporteResidentes() {
        String reporte = estacionamientoService.generarReporteResidentes();
        return ResponseEntity.ok(reporte);
    }

}
