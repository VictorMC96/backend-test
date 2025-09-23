package com.estacionamiento.app.estacionamiento.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estacionamiento.app.estacionamiento.entities.Vehiculo;
import com.estacionamiento.app.estacionamiento.serviceImp.VehiculoServiceImpl;

@RestController
@RequestMapping("api/vehiculo")
public class VehiculoController {

    @Autowired
    VehiculoServiceImpl serviceVehiculo;

    @PostMapping({"save"})
    public ResponseEntity<?> saveVehiculo(@RequestBody Vehiculo auto){
    	
    	auto = serviceVehiculo.save(auto);
    	return ResponseEntity.status(HttpStatus.CREATED).body(auto);    	
    	
    }
}
