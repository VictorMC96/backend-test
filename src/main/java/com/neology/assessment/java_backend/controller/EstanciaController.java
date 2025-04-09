package com.neology.assessment.java_backend.controller;

import com.neology.assessment.java_backend.dto.TipoVehiculoEnum;
import com.neology.assessment.java_backend.dto.request.VehiculoRequest;
import com.neology.assessment.java_backend.entity.Estancia;
import com.neology.assessment.java_backend.service.EstanciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/estancias")
public class EstanciaController {

    @Autowired
    EstanciaService estanciaService;

    @PostMapping("/entradas")
    public ResponseEntity<Estancia> registarEntrada(@RequestBody VehiculoRequest vehiculoRequest) {
       return  ResponseEntity.ok(estanciaService.registarEntrada(vehiculoRequest));
    }

    @PatchMapping("/salidas")
    public ResponseEntity<Estancia> registarSalida(@RequestBody VehiculoRequest vehiculoRequest) {
        return  ResponseEntity.ok(estanciaService.registrarSalida(vehiculoRequest));
    }

    @PatchMapping("/iniciar_mes")
    public ResponseEntity<Void> iniciarMes() {
         estanciaService.iniciarMes();
         return ResponseEntity.ok().build();
    }

    @GetMapping("/reporte_residentes/{nombre_archivo}")
    public ResponseEntity<Resource> getReporteResidentes(@PathVariable("nombre_archivo") String nombreArchivo) throws IOException {
        ByteArrayResource resource = estanciaService.generarReporteResidentes();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ nombreArchivo+ ".csv\"")
                .contentType(MediaType.parseMediaType("text/plain"))
                .contentLength(resource.contentLength())
                .body(resource);
    }
}


