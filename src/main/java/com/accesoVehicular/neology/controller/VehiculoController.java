package com.accesoVehicular.neology.controller;

import com.accesoVehicular.neology.dto.RegistroVehiculo;
import com.accesoVehicular.neology.model.Vehiculo;
import com.accesoVehicular.neology.service.VehiculoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/vehiculos")
public class VehiculoController {

    private final VehiculoService vehiculoService;

    public VehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Vehiculo>> getAllVehiculos() {
        List<Vehiculo> vehiculos = this.vehiculoService.getAll();
        if(!vehiculos.isEmpty()) {
            return new ResponseEntity<>(vehiculos, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/findAllByTipoVehiculoNombre/{tipoVehiculoNombre}")
    public ResponseEntity<List<Vehiculo>> findAllByTipoVehiculoNombre(@PathVariable String tipoVehiculoNombre) {
        try {
            List<Vehiculo> vehiculos = this.vehiculoService.getByTipoVehiculoNombre(tipoVehiculoNombre);
            if(!vehiculos.isEmpty()) {
                return new ResponseEntity<>(vehiculos, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            System.out.println("Error al buscar vehiculos por tipo: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Uso solo para registrar vehiculos con cualquier tipo que exista en la base de datos, debe mandar el objeto RegistroVehiculo que contiene la placa y el objeto TipoVehiculo
    @PostMapping("/registrar")
    public ResponseEntity<Vehiculo> create(@RequestBody RegistroVehiculo registroVehiculo) {
        try {
            Vehiculo vehiculo = this.vehiculoService.registrarVehiculo(registroVehiculo);
            return new ResponseEntity<>(vehiculo, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println("Error al registrar el vehiculo: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    //Uso solo para registrar vehiculos de tipo "OFICIAL" o "RESIDENTE"
    @PostMapping("/registrarOficial")
    public ResponseEntity<Vehiculo> registrarOficial(@RequestBody String placa) {
        try {
            Vehiculo vehiculo = this.vehiculoService.registrarVehiculo(placa, "OFICIAL");
            return new ResponseEntity<>(vehiculo, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/registrarResidente")
    public ResponseEntity<Vehiculo> registrarResidente(@RequestBody String placa) {
        try {
            Vehiculo vehiculo = this.vehiculoService.registrarVehiculo(placa, "RESIDENTE");
            return new ResponseEntity<>(vehiculo, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
