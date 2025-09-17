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

    // Endpoint para obtener todos los vehículos
    @GetMapping
    public List<Vehiculo> obtenerTodosLosVehiculos() {
        return vehiculoService.obtenerTodos();
    }

    // Endpoint para obtener solo vehículos oficiales
    @GetMapping("/oficiales")
    public List<Vehiculo> obtenerVehiculosOficiales() {
        return vehiculoService.obtenerPorTipo("OFICIAL");
    }

    // Endpoint para obtener un vehículo por ID
    @GetMapping("/{id}")
    public Vehiculo obtenerVehiculoPorId(@PathVariable Long id) {
        return vehiculoService.obtenerPorId(id);
    }

    // Endpoint para crear un nuevo vehículo
    @PostMapping
    public Vehiculo crearVehiculo(@RequestBody Vehiculo vehiculo) {
        return vehiculoService.crearVehiculo(vehiculo);
    }

    // Endpoint para actualizar un vehículo
    @PutMapping("/{id}")
    public Vehiculo actualizarVehiculo(@PathVariable Long id, @RequestBody Vehiculo vehiculo) {
        return vehiculoService.actualizarVehiculo(id, vehiculo);
    }

    // Endpoint para eliminar un vehículo
    @DeleteMapping("/{id}")
    public void eliminarVehiculo(@PathVariable Long id) {
        vehiculoService.eliminarVehiculo(id);
    }
}
