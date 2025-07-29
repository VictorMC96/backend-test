package com.accesoVehicular.neology.controller;


import com.accesoVehicular.neology.model.TipoVehiculo;
import com.accesoVehicular.neology.service.TipoVehiculoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tipos_vehiculo")
public class TipoVehiculoController {
    private final TipoVehiculoService tipoVehiculoService;

    public TipoVehiculoController(TipoVehiculoService tipoVehiculoService) {
        this.tipoVehiculoService = tipoVehiculoService;
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<TipoVehiculo>> getAllTiposVehiculo() {
        List<TipoVehiculo> tiposVehiculo = this.tipoVehiculoService.getAll();
        if (!tiposVehiculo.isEmpty()) {
            return new ResponseEntity<>(tiposVehiculo, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
