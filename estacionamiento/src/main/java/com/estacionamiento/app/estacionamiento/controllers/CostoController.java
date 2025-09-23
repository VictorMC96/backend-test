package com.estacionamiento.app.estacionamiento.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estacionamiento.app.estacionamiento.entities.Costo;
import com.estacionamiento.app.estacionamiento.service.CostoService;

@RestController
@RequestMapping("api/costo")
public class CostoController {

    @Autowired
    CostoService costoService;

    @PostMapping({"save"})
    public ResponseEntity<?> saveCosto(@RequestBody Costo costo){
    	
    	costo = costoService.save(costo);
    	return ResponseEntity.status(HttpStatus.CREATED).body(costo);    	
    	
    }
}
