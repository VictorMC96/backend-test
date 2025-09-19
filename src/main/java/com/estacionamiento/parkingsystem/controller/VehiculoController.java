package com.estacionamiento.parkingsystem.controller;

import com.estacionamiento.parkingsystem.model.Vehiculo;
import com.estacionamiento.parkingsystem.service.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/vehiculos")
public class VehiculoController {
    @Autowired
    private VehiculoService vehiculoService;

    // link: "http://localhost:8080/vehiculos"
    @GetMapping
    public List<Vehiculo> obtenerTodosLosVehiculos() {
        return vehiculoService.obtenerTodos();
    }

    // link: "http://localhost:8080/vehiculos/oficiales"
    @GetMapping("/oficiales")
    public List<Vehiculo> obtenerVehiculosOficiales() {
        return vehiculoService.obtenerPorTipo("OFICIAL");
    }

    // link: "http://localhost:8080/vehiculos/{id}"
    @GetMapping("/{id}")
    public Vehiculo obtenerVehiculoPorId(@PathVariable Long id) {
        return vehiculoService.obtenerPorId(id);
    }

    // link: "http://localhost:8080/vehiculos"
    @PostMapping
    public Vehiculo crearVehiculo(@RequestBody Vehiculo vehiculo) {
        return vehiculoService.crearVehiculo(vehiculo);
    }

    // link: "http://localhost:8080/vehiculos/{id}"
    @PutMapping("/{id}")
    public Vehiculo actualizarVehiculo(@PathVariable Long id, @RequestBody Vehiculo vehiculo) {
        return vehiculoService.actualizarVehiculo(id, vehiculo);
    }

    // link: "http://localhost:8080/vehiculos/{id}"
    @DeleteMapping("/{id}")
    public void eliminarVehiculo(@PathVariable Long id) {
        vehiculoService.eliminarVehiculo(id);
    }
}
