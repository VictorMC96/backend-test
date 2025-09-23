package mx.ms.parking.controller;

import mx.ms.parking.model.*;
import mx.ms.parking.mutations.Accesos;
import mx.ms.parking.mutations.Vehiculos;
import mx.ms.parking.services.IVehiculoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("")
public class ParkingC {

    @Autowired
    private Accesos accesosMetodos;
    @Autowired
    private Vehiculos vehiculosMetodos;
    @Autowired
    private IVehiculoServices vehiculoServices;

    //Registro de entrada y salida
    @PostMapping(value = "/acceso/save")
    public ResponseEntity<?> save(@RequestBody RequestAcceso acceso) {
        Map<String, Object> response = new HashMap<>();
        System.out.println("acceso-----------" + acceso);

        try {
            return new ResponseEntity<>(accesosMetodos.registrarAcceso(acceso), HttpStatus.OK);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al guardar acceso");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Controladores para vehiculos

    //Dar de alta vehiculos de cualquier tipo
    @PostMapping(value = "/vehiculo/save")
    public ResponseEntity<?> save(@RequestBody RequestVehiculo vehiculo) {
        Map<String, Object> response = new HashMap<>();
        System.out.println("Vehiculo-----------" + vehiculo);

        try {

            return new ResponseEntity<>(vehiculosMetodos.registrarVehiculo(vehiculo), HttpStatus.OK);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al guardar vehiculo");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //Comienza Mes
    @GetMapping(value = "/comienzames")
    public ResponseEntity<?> comienzaMes() {
        Map<String, Object> response = new HashMap<>();

        try {

            vehiculosMetodos.resetTiempoResidentes();
            accesosMetodos.eliminarRegistrosOficial();
            response.put("Proceso Correcto", "Error al procesar");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al procesar");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/residentes/generar-informe-excel")
    public String generarInformeExcel(@RequestParam String archivo) {
        try {
            vehiculoServices.generarInformePagosResidentesExcel(archivo);
            return "Informe Excel generado en: " + archivo;
        } catch (IOException e) {
            e.printStackTrace();
            return "Error al generar el informe Excel: " + e.getMessage();
        }
    }

}
