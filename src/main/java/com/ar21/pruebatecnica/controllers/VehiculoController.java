package com.ar21.pruebatecnica.controllers;

import com.ar21.pruebatecnica.models.EntradaSalidaModel;
import com.ar21.pruebatecnica.models.VehiculoModel;
import com.ar21.pruebatecnica.services.VehiculoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/acceso-vehiculos")
public class VehiculoController {

    private final VehiculoService vehiculoService;

    public VehiculoController(VehiculoService vehiculoService){
        this.vehiculoService= vehiculoService;
    }

    @PostMapping
    public ResponseEntity<VehiculoModel> registrarVehiculo(@RequestBody VehiculoModel vehiculoModel){

        return new ResponseEntity<VehiculoModel>(vehiculoService.registrarVehiculo(vehiculoModel), HttpStatus.CREATED);

    }

    @PostMapping("/registrarEntrada")
    public ResponseEntity<EntradaSalidaModel> registrarEntrada(@RequestBody EntradaSalidaModel entradaRequest) {
        return new ResponseEntity<EntradaSalidaModel>(vehiculoService.registrarEntrada(entradaRequest),HttpStatus.CREATED);

    }

    @PostMapping("/registrarSalida")
    public ResponseEntity<EntradaSalidaModel> registrarHoraSalida(@RequestParam String placa) {
        return new ResponseEntity<EntradaSalidaModel>(vehiculoService.registarSalida(placa),HttpStatus.CREATED);

    }



}
