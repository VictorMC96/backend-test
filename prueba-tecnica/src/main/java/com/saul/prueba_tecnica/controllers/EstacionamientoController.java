package com.saul.prueba_tecnica.controllers;

import com.saul.prueba_tecnica.services.EstacionamientoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/estacionamiento")
public class EstacionamientoController {

    private final EstacionamientoService estacionamientoService;

    public EstacionamientoController(EstacionamientoService estacionamientoService) {
        this.estacionamientoService = estacionamientoService;
    }

    // Alta vehículo oficial
    @PostMapping("/alta/oficial/{placa}")
    public ResponseEntity<?> altaOficial(@PathVariable String placa) {
        estacionamientoService.registrarVehiculoOficial(placa);
        return ResponseEntity.ok("Vehículo oficial registrado");
    }

    // Alta vehículo residente
    @PostMapping("/alta/residente/{placa}")
    public ResponseEntity<?> altaResidente(@PathVariable String placa) {
        estacionamientoService.registrarVehiculoResidente(placa);
        return ResponseEntity.ok("Vehículo residente registrado");
    }

    // Registrar entrada
    @PostMapping("/entrada/{placa}")
    public ResponseEntity<?> registrarEntrada(@PathVariable String placa) {
        estacionamientoService.registrarEntrada(placa);
        return ResponseEntity.ok("Entrada registrada");
    }

    // Registrar salida
    @PostMapping("/salida/{placa}")
    public ResponseEntity<?> registrarSalida(@PathVariable String placa) {
        double monto = estacionamientoService.registrarSalida(placa);
        if (monto > 0) {
            return ResponseEntity.ok("Monto a pagar: $" + monto);
        } else {
            return ResponseEntity.ok("Salida registrada");
        }
    }

    // Comenzar mes
    @PostMapping("/comenzar-mes")
    public ResponseEntity<?> comenzarMes() {
        estacionamientoService.comenzarMes();
        return ResponseEntity.ok("Mes iniciado. Estancias oficiales borradas y residentes reiniciados.");
    }

    // Reporte de residentes
    @GetMapping("/reportes/residentes")
    public ResponseEntity<String> reporteResidentes() {
        estacionamientoService.generarReportePagos();
        return ResponseEntity.ok("Archivo generado correctamente.");
    }
}
