package com.estacionamiento.parking.controller;

import com.estacionamiento.parking.service.ParkingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parking")
@RequiredArgsConstructor
public class ParkingController {

    private final ParkingService service;

    @PostMapping("/entrada/{plate}")
    public ResponseEntity<?> entrada(@PathVariable String plate) {
        return ResponseEntity.ok(service.registerEntry(plate));
    }

    @PostMapping("/salida/{plate}")
    public ResponseEntity<?> salida(@PathVariable String plate) {
        return ResponseEntity.ok(service.registerExit(plate));
    }

    @PostMapping("/alta/oficial/{plate}")
    public ResponseEntity<?> altaOficial(@PathVariable String plate) {
        return ResponseEntity.ok(service.registerOfficial(plate));
    }

    @PostMapping("/alta/residente/{plate}")
    public ResponseEntity<?> altaResidente(@PathVariable String plate) {
        return ResponseEntity.ok(service.registerResident(plate));
    }

    @PostMapping("/comienza-mes")
    public ResponseEntity<?> comienzaMes() {
        service.startMonth();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reporte-residentes")
    public ResponseEntity<?> reporte(@RequestParam(defaultValue = "residentes.tsv") String archivo) throws Exception {
        String path = service.generateResidentReport(archivo);
        return ResponseEntity.ok("Reporte generado en: " + path);
    }
}