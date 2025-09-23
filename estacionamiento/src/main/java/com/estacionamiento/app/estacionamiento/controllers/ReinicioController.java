package com.estacionamiento.app.estacionamiento.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.estacionamiento.app.estacionamiento.dto.ResponseDto;
import com.estacionamiento.app.estacionamiento.service.EntradaSalidaService;
import com.estacionamiento.app.estacionamiento.service.TiempoAcumuladoService;

@RestController
@RequestMapping("api/control")
public class ReinicioController {
	
	@Autowired
	TiempoAcumuladoService tiempoAcumuladoService;
	
	@Autowired
	EntradaSalidaService entradaSalidaService;
	
	
	@PostMapping("reiniciaTiempo")
	public ResponseEntity<?> reiniciar(){
		ResponseDto responseDto = tiempoAcumuladoService.reiniciaMinutos("REINICIO");
		entradaSalidaService.deleteByTipo("OFICIAL");
		return ResponseEntity.ok().body(responseDto);
	}

}
