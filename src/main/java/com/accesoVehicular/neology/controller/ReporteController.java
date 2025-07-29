package com.accesoVehicular.neology.controller;

import com.accesoVehicular.neology.service.AcumuladoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reportes")
public class ReporteController {

    private final AcumuladoService acumuladoService;
    public ReporteController(AcumuladoService acumuladoService) {
        this.acumuladoService = acumuladoService;
    }

    @GetMapping("/obtenerPagosResidentes/{nombre_archivo}")
    public ResponseEntity<byte[]> registrarEntrada(@PathVariable String nombre_archivo) {
        try {
            return this.acumuladoService.obtenerPagosResidentes(nombre_archivo);
        } catch (Exception e) {
            System.out.println("Error al generar el reporte: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
