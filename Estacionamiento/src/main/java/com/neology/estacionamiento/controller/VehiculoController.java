package com.neology.estacionamiento.controller;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import com.neology.estacionamiento.domain.TipoVehiculo;
import com.neology.estacionamiento.service.EstacionamientoService;

public class VehiculoController {
	
	@RestController
	@RequestMapping("/estacionamiento")
	public class EstacionamientoController {

	    private final EstacionamientoService service;

	    public EstacionamientoController(EstacionamientoService service) {
	        this.service = service;
	    }

	    @PostMapping("/alta")
	    public ResponseEntity<?> altaVehiculo(@RequestParam String placa, @RequestParam TipoVehiculo tipo) {
	        service.altaVehiculo(placa, tipo);
	        return ResponseEntity.ok("Vehículo dado de alta");
	    }

	    @PostMapping("/entrada")
	    public ResponseEntity<?> registrarEntrada(@RequestParam String placa) {
	        service.registrarEntrada(placa);
	        return ResponseEntity.ok("Entrada registrada");
	    }

	    @PostMapping("/salida")
	    public ResponseEntity<?> registrarSalida(@RequestParam String placa) {
	        double cobro = service.registrarSalida(placa);
	        return ResponseEntity.ok("Salida registrada. Cobro: " + cobro);
	    }

	    @PostMapping("/comienzaMes")
	    public ResponseEntity<?> comienzaMes() {
	        service.comienzaMes();
	        return ResponseEntity.ok("Mes iniciado, registros limpiados");
	    }
	}

}
