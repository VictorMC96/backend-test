package com.backendtest.demo.controller;


import com.backendtest.demo.dto.InformePagoResidente;
import com.backendtest.demo.entity.Vehiculo;
import com.backendtest.demo.service.EstanciaService;
import com.backendtest.demo.service.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehiculos")
public class VehiculoController {

    @Autowired
    private EstanciaService estanciaService;

    @Autowired
    private VehiculoService vehiculoService;

    @PostMapping("/entrada")
    public void registrarEntrada(@RequestBody Vehiculo vehiculo) {
        estanciaService.registrarEntrada(vehiculo);
    }

    //@PostMapping("/salida")
    //public void registrarSalida(@RequestBody Vehiculo vehiculo) {
    //    estanciaService.registrarSalida(vehiculo);
    //}

    @PostMapping("/oficial")
    public void darDeAltaVehiculoOficial(@RequestBody Vehiculo vehiculo) {
        vehiculoService.darDeAltaVehiculoOficial(vehiculo);
    }

    @PostMapping("/residente")
    public void darDeAltaVehiculoResidente(@RequestBody Vehiculo vehiculo) {
        vehiculoService.darDeAltaVehiculoResidente(vehiculo);
    }

    //@PostMapping("/comienza-mes")
    //public void comienzaMes() {
    //    estanciaService.comienzaMes();
    //}

    //@GetMapping("/pagos-residentes")
    //public List<InformePagoResidente> generarInformePagosResidentes() {
    //    return vehiculoService.generarInformePagosResidentes();
    //}/
}
