package com.examen.oodoo.controller;

import com.examen.oodoo.service.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vehiculos")
public class VehiculoController {
    private final VehiculoService vehiculoService;

    @Autowired
    public VehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    @PostMapping("/entrada")
    public String registrarEntrada(@RequestParam String placa) {
        return vehiculoService.registrarEntrada(placa);
    }

    @PostMapping("/salida")
    public String registrarSalida(@RequestParam String placa) {
        vehiculoService.registrarSalida(placa);
        return "Registro de salida exitoso para el vehículo con placa " + placa;
    }

    @PostMapping("/alta/oficial")
    public String darDeAltaVehiculoOficial(@RequestParam String placa) {

        return vehiculoService.darDeAltaVehiculoOficial(placa);
    }

    @PostMapping("/alta/residente")
    public String darDeAltaVehiculoResidente(@RequestParam String placa) {
        return vehiculoService.darDeAltaVehiculoResidente(placa);
    }

}
