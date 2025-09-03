package com.oscar.gestor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oscar.gestor.service.GestorService;

@RestController
public class GestorController {

	@Autowired 
	GestorService servicio;
	
	@PostMapping("/entrada")
	public void registraEntrada(@RequestParam String placa) {
		servicio.registrarEntrada(placa);
	}
	
	@PostMapping("/salida")
	public void salida(@RequestParam String placa) {
		servicio.registrarSalida(placa);
	}
	
	@PutMapping("/Auto/OFicial")
	public void registarAutoOficial(@RequestParam String placa) {
		servicio.registarAuto(placa, 0);
		
	}
	
	@PutMapping("/Auto/Residente")
	public void registarAutoResidente(@RequestParam String placa) {
		servicio.registarAuto(placa, 0);
	}
	
}
