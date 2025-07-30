package com.accesoVehicular.neology.controller;

import com.accesoVehicular.neology.model.Registro;
import com.accesoVehicular.neology.service.RegistroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/registros")
public class RegistroController {

    private final RegistroService registroService;

    public RegistroController(RegistroService registroService) {
        this.registroService = registroService;
    }

    @GetMapping("/entrada/{placa}")
    public ResponseEntity<String> registrarEntrada(@PathVariable String placa) {
        try {
            boolean resultado = this.registroService.registrarEntrada(placa);
            if (resultado) {
                return ResponseEntity.ok("Registro de entrada exitoso");
            } else {
                return ResponseEntity.badRequest().body("Error al registrar la entrada");
            }
        } catch (Exception e) {
            System.out.println("Error al registrar la entrada: " + e.getMessage());
            return new ResponseEntity<>("Error al registrar la entrada: " + e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/salida/{placa}")
    public ResponseEntity<String> registrarSalida(@PathVariable String placa) {
        try {
            BigDecimal resultado = this.registroService.registrarSalida(placa);
            return ResponseEntity.ok("Registro de salida exitoso, total a pagar: " + resultado);
        } catch (Exception e) {
            System.out.println("Error al registrar la salida: " + e.getMessage());
            return new ResponseEntity<>("Error al registrar la salida: " + e.getMessage(), HttpStatus.CONFLICT);
        }
    }

}
