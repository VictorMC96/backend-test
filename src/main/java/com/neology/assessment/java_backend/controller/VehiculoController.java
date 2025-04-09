package com.neology.assessment.java_backend.controller;

import com.neology.assessment.java_backend.dto.TipoVehiculoEnum;
import com.neology.assessment.java_backend.dto.request.VehiculoRequest;
import com.neology.assessment.java_backend.entity.Vehiculo;
import com.neology.assessment.java_backend.service.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/vehiculos")
public class VehiculoController {
    @Autowired
    VehiculoService vehiculoService;

    @PostMapping("/residentes")
    public ResponseEntity<Vehiculo> registrarVehiculoResidente(@RequestBody VehiculoRequest vehiculoRequest) {
       return ResponseEntity.ok(vehiculoService.registrarVehiculo(vehiculoRequest, TipoVehiculoEnum.RESIDENTE));
    }

    @PostMapping("/oficiales")
    public ResponseEntity<Vehiculo> registrarVehiculoOficial(@RequestBody VehiculoRequest vehiculoRequest) {
        return ResponseEntity.ok(vehiculoService.registrarVehiculo(vehiculoRequest, TipoVehiculoEnum.OFICIAL));
    }
}
