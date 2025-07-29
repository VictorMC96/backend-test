package com.accesoVehicular.neology.controller;

import com.accesoVehicular.neology.service.AcumuladoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/gestion")
public class AcumuladoController {
    private final AcumuladoService acumuladoService;

    public AcumuladoController(AcumuladoService acumuladoService) {
        this.acumuladoService = acumuladoService;
    }

    @GetMapping("/comenzarMes")
    public ResponseEntity<String> comenzarMes() {
        try {
            String response = this.acumuladoService.resetAcumulados();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
