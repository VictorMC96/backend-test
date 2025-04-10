package com.demo.pagos.demo.controller;

import com.demo.pagos.demo.controller.request.VehiculoEntradaRequest;
import com.demo.pagos.demo.services.EstacionamientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/estacionamiento")
@RequiredArgsConstructor
public class EstacionamientoController {

    private final EstacionamientoService servicio;

    @PostMapping("/entrada")
    public ResponseEntity<String> registrarEntrada(@RequestBody VehiculoEntradaRequest vehiculoEntradaRequest) {
        servicio.registrarEntrada(vehiculoEntradaRequest.getPlaca());
        return ResponseEntity.ok("Entrada registrada");
    }

    @PostMapping("/salida")
    public ResponseEntity<String> registrarSalida(@RequestBody VehiculoEntradaRequest vehiculoEntradaRequest) {
        BigDecimal monto = servicio.registrarSalida(vehiculoEntradaRequest.getPlaca());
        return ResponseEntity.ok("Salida registrada - Pago: $" + monto);
    }

    @PostMapping("/alta/oficial")
    public ResponseEntity<String> altaOficial(@RequestBody VehiculoEntradaRequest vehiculoEntradaRequest) {
        servicio.altaOficial(vehiculoEntradaRequest.getPlaca());
        return ResponseEntity.ok("Vehículo oficial registrado");
    }

    @PostMapping("/alta/residente")
    public ResponseEntity<String> altaResidente(@RequestBody VehiculoEntradaRequest vehiculoEntradaRequest) {
        servicio.altaResidente(vehiculoEntradaRequest.getPlaca());
        return ResponseEntity.ok("Vehículo residente registrado");
    }

    @PostMapping("/comenzar-mes")
    public ResponseEntity<String> comenzarMes() {
        servicio.comenzarMes();
        return ResponseEntity.ok("Reinicio de Mes");
    }

    @GetMapping("/pagos-residentes")
    public ResponseEntity<Resource> generarPagosResidentes(
            @RequestParam(defaultValue = "reporte_residentes.txt") String archivo) throws IOException {

        servicio.generarInforme(archivo);
        FileSystemResource resource = new FileSystemResource(archivo);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + archivo)
                .contentType(MediaType.TEXT_PLAIN)
                .body(resource);
    }
}
