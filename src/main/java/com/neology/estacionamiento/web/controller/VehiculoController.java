package com.neology.estacionamiento.web.controller;

import com.neology.estacionamiento.application.service.IVehiculoService;
import com.neology.estacionamiento.application.dto.VehiculoInfo;
import com.neology.estacionamiento.domain.model.Vehiculo;
import com.neology.estacionamiento.domain.model.VehiculoOficial;
import com.neology.estacionamiento.domain.model.VehiculoResidente;
import com.neology.estacionamiento.domain.model.TipoVehiculo;
import com.neology.estacionamiento.web.request.RegistroVehiculoRequest;
import com.neology.estacionamiento.web.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controlador REST para la gestión de vehículos (altas, consultas)
 * Simplificado: solo maneja HTTP y delega a servicios
 */
@RestController
@RequestMapping("/api/vehiculos")
@CrossOrigin(origins = "*")
public class VehiculoController {

    private final IVehiculoService vehiculoService;

    @Autowired
    public VehiculoController(IVehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    /**
     * Registra un vehículo oficial
     */
    @PostMapping("/oficial")
    public ResponseEntity<ApiResponse<VehiculoInfo>> registrarVehiculoOficial(@RequestBody RegistroVehiculoRequest request) {
        try {
            VehiculoOficial vehiculo = vehiculoService.registrarVehiculoOficial(request.getPlaca());
            VehiculoInfo vehiculoInfo = new VehiculoInfo(vehiculo);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Vehículo oficial registrado exitosamente", vehiculoInfo));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error en los datos: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error interno del servidor: " + e.getMessage()));
        }
    }

    /**
     * Registra un vehículo de residente
     */
    @PostMapping("/residente")
    public ResponseEntity<ApiResponse<VehiculoInfo>> registrarVehiculoResidente(@RequestBody RegistroVehiculoRequest request) {
        try {
            VehiculoResidente vehiculo = vehiculoService.registrarVehiculoResidente(request.getPlaca());
            VehiculoInfo vehiculoInfo = new VehiculoInfo(vehiculo);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Vehículo de residente registrado exitosamente", vehiculoInfo));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error en los datos: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error interno del servidor: " + e.getMessage()));
        }
    }

    /**
     * Busca un vehículo por su placa
     */
    @GetMapping("/{placa}")
    public ResponseEntity<ApiResponse<VehiculoInfo>> buscarVehiculo(@PathVariable String placa) {
        try {
            Optional<Vehiculo> vehiculo = vehiculoService.buscarPorPlaca(placa);
            
            if (vehiculo.isPresent()) {
                VehiculoInfo vehiculoInfo = new VehiculoInfo(vehiculo.get());
                return ResponseEntity.ok(ApiResponse.success("Vehículo encontrado", vehiculoInfo));
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error en los datos: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error interno del servidor: " + e.getMessage()));
        }
    }

    /**
     * Lista vehículos por tipo
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<ApiResponse<List<VehiculoInfo>>> listarPorTipo(@PathVariable String tipo) {
        try {
            TipoVehiculo tipoVehiculo = TipoVehiculo.valueOf(tipo.toUpperCase());
            List<Vehiculo> vehiculos = vehiculoService.buscarPorTipo(tipoVehiculo);
            
            // Transformar entidades a DTOs
            List<VehiculoInfo> vehiculosInfo = vehiculos.stream()
                    .map(VehiculoInfo::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ApiResponse.success("Consulta exitosa", vehiculosInfo));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Tipo de vehículo inválido: " + tipo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error interno del servidor: " + e.getMessage()));
        }
    }

    /**
     * Lista todos los vehículos
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<VehiculoInfo>>> listarTodos() {
        try {
            List<Vehiculo> vehiculos = vehiculoService.obtenerTodosLosVehiculos();
            
            // Transformar entidades a DTOs
            List<VehiculoInfo> vehiculosInfo = vehiculos.stream()
                    .map(VehiculoInfo::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ApiResponse.success("Consulta exitosa", vehiculosInfo));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error interno del servidor: " + e.getMessage()));
        }
    }

    /**
     * Convierte un vehículo no residente a residente
     */
    @PutMapping("/{placa}/convertir-residente")
    public ResponseEntity<ApiResponse<VehiculoInfo>> convertirAResidente(@PathVariable String placa) {
        try {
            VehiculoResidente vehiculo = vehiculoService.convertirAResidente(placa);
            VehiculoInfo vehiculoInfo = new VehiculoInfo(vehiculo);

            return ResponseEntity.ok(ApiResponse.success("Vehículo convertido a residente exitosamente", vehiculoInfo));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error en los datos: " + e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error("Estado inválido: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error interno del servidor: " + e.getMessage()));
        }
    }
} 