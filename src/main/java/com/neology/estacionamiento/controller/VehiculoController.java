package com.neology.estacionamiento.controller;

import com.neology.estacionamiento.dto.PlacaDTO;
import com.neology.estacionamiento.dto.ResponseMensajeDTO;
import com.neology.estacionamiento.model.TipoVehiculo;
import com.neology.estacionamiento.model.Vehiculo;
import com.neology.estacionamiento.service.VehiculoService;
import com.neology.estacionamiento.util.Constantes;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/estacionamiento")
public class VehiculoController {

    private final VehiculoService vehiculoService;

    public VehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    @PostMapping("/registro/entrada")
    public ResponseEntity<ResponseMensajeDTO> entrada(@RequestBody PlacaDTO request) {
        vehiculoService.registrarEntrada(request.getPlaca());
        return ResponseEntity.ok(new ResponseMensajeDTO(Constantes.ENTRADA_REGISTRADA));
    }


   @PostMapping("/registro/salida")
    public ResponseEntity<ResponseMensajeDTO> salida(@RequestBody PlacaDTO request) {
     String resultado = vehiculoService.registrarSalida(request.getPlaca());
     return ResponseEntity.ok(new ResponseMensajeDTO(resultado));
    }
    

   @PostMapping("/vehiculos/alta/oficiales")
   public ResponseEntity<Vehiculo> registrarOficial(@RequestBody PlacaDTO request) {
       Vehiculo vehiculo = Vehiculo.builder()
           .placa(request.getPlaca())
           .tipo(TipoVehiculo.OFICIAL)
           .minutosAcumulados(0) 
           .build();

       return ResponseEntity.ok(vehiculoService.registrarVehiculo(vehiculo, TipoVehiculo.OFICIAL));
   }
   
    @PostMapping("/vehiculos/alta/residentes")
    public ResponseEntity<Vehiculo> registrarResidente(@RequestBody PlacaDTO request) {
        Vehiculo vehiculo = Vehiculo.builder()
            .placa(request.getPlaca())
            .tipo(TipoVehiculo.RESIDENTE)
            .minutosAcumulados(0)
            .build();

        return ResponseEntity.ok(vehiculoService.registrarVehiculo(vehiculo, TipoVehiculo.RESIDENTE));
    }
    
    @GetMapping("/informe/pagos/residentes")
    public ResponseEntity<Map<String, Object>> generaInforme(@RequestParam String nombreArchivo) {

        List<Map<String, Object>> reporte = vehiculoService.generarReporteResidentes();
        String base64 = vehiculoService.generarReporte(reporte, nombreArchivo);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("nombreArchivo", nombreArchivo + ".csv");
        respuesta.put("contenidoBase64", base64);

        return ResponseEntity.ok(respuesta);
    }


    @PostMapping("/comienza-mes")
    public ResponseEntity<ResponseMensajeDTO> resetMes() {
        vehiculoService.reiniciarMes();
        return ResponseEntity.ok(new ResponseMensajeDTO(Constantes.DATOS_REINICIADOS));
    }
}
