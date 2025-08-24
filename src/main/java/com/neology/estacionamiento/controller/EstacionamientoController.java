package com.neology.estacionamiento.controller;

import com.neology.estacionamiento.service.EstacionamientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api")
public class EstacionamientoController {
    @Autowired
    private EstacionamientoService estacionamientoService;

    @PostMapping("/entrada")
    public ResponseEntity<String> registrarEntrada(@RequestParam String placa) {
        try {
            estacionamientoService.registrarEntrada(placa);
            return ResponseEntity.ok("Entrada registrada para la placa: " + placa);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/salida")
    public ResponseEntity<String> registrarSalida(@RequestParam String placa) {
        try {
            double costo = estacionamientoService.registrarSalida(placa);
            return ResponseEntity.ok("Salida registrada para la placa: " + placa + ". Costo a pagar: $" + String.format("%.2f", costo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/altaVehiculoOficial")
    public ResponseEntity<String> altaVehiculoOficial(@RequestParam String placa){
        try {
            estacionamientoService.daDeAltaOficial(placa);
            return ResponseEntity.ok().body("Vehículo Oficial alta correcta");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/altaVehiculoResidente")
    public ResponseEntity<String> altaVehiculoResidente(@RequestParam String placa){
        try {
            estacionamientoService.daDeAltaResidente(placa);
            return ResponseEntity.ok().body("Vehículo Residente alta correcta");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/comenzarMes")
    public ResponseEntity<String> comenzarMes() {
        try {
            estacionamientoService.comienzaMes();
            return ResponseEntity.ok().body("Mes reiniciado");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/informes/residentes")
    public ResponseEntity<byte []> generarInformePagosResidentes(@RequestParam String nombreArchivo) {
        try {
            // Genera el archivo en el servidor.
            estacionamientoService.generarInformePagosResidentes(nombreArchivo);

            // Lee el archivo generado como un array de bytes.
            Path path = Paths.get(nombreArchivo);
            byte[] data = Files.readAllBytes(path);

            // Configura las cabeceras HTTP para la descarga.
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombreArchivo + "\"");

            // Retorna la respuesta con el archivo.
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(data.length)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(data);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }

    }
}
