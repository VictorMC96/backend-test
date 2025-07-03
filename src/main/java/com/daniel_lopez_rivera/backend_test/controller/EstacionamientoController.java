package com.daniel_lopez_rivera.backend_test.controller;

import com.daniel_lopez_rivera.backend_test.dto.VehiculoDTO;
import com.daniel_lopez_rivera.backend_test.model.Vehiculo;
import com.daniel_lopez_rivera.backend_test.service.EstacionamientoService;
import com.daniel_lopez_rivera.backend_test.util.VehiculoTipo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/estacionamiento")
public class EstacionamientoController {

    @Autowired
    private EstacionamientoService service;

    @PostMapping("/registrar-vehiculo")
    public ResponseEntity<?> registrarVehiculo(@RequestBody VehiculoDTO vehiculo) {
        String placa = vehiculo.getPlaca();
        VehiculoTipo tipo = VehiculoTipo.valueOf(vehiculo.getTipo());
        service.agregarVehiculo(placa, tipo);
        return ResponseEntity.ok("Vehículo agregado como " + tipo);
    }

    @GetMapping("/consultar-vehiculos")
    public List<Vehiculo> consultarVehiculos(){
        return service.consultarVehiculos();
    }

    @PostMapping("/entrada")
    public ResponseEntity<?> registrarEntrada(@RequestBody VehiculoDTO vehiculo) {
        service.registrarEntrada(vehiculo.getPlaca());
        return ResponseEntity.ok("Entrada registrada");
    }

    @PostMapping("/salida")
    public ResponseEntity<?> registrarSalida(@RequestBody VehiculoDTO vehiculo) {
        String result = service.registrarSalida(vehiculo.getPlaca());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/comienza-mes")
    public ResponseEntity<?> reiniciarMes() {
        service.iniciarNuevoMes();
        return ResponseEntity.ok("Mes reiniciado");
    }

    @PostMapping("/reporte-residentes")
    public ResponseEntity<?> report(@RequestParam String nombreFichero) throws IOException, IOException {

       boolean reporteGenerado = service.generarReporte(nombreFichero);
       if (reporteGenerado){
           return ResponseEntity.ok("Informe generado en fichero con nombre: " + nombreFichero);
       } else {
           return new ResponseEntity<>("ERROR al generar el reporte", HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }
}