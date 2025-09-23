package com.estacionamiento.app.estacionamiento.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estacionamiento.app.estacionamiento.service.TiempoAcumuladoService;

@RestController
@RequestMapping("api/reporte")
public class TiempoAcumuladoController {

	@Autowired
	TiempoAcumuladoService tiempoAcumuladoService;
	
	@GetMapping("listResidentes")
	public ResponseEntity<?> getAllMunicipios(){
		return ResponseEntity.ok().body(tiempoAcumuladoService.listResidentes());
	}
	
}
