package com.neology.estacionamiento.web.controller;

import com.neology.estacionamiento.application.service.IEstacionamientoService;
import com.neology.estacionamiento.application.dto.VehiculoActivoInfo;
import com.neology.estacionamiento.domain.model.Estancia;
import com.neology.estacionamiento.web.request.EntradaRequest;
import com.neology.estacionamiento.web.request.SalidaRequest;
import com.neology.estacionamiento.web.response.ApiResponse;
import com.neology.estacionamiento.web.response.EntradaResponse;
import com.neology.estacionamiento.web.response.SalidaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para la gestión del estacionamiento (entradas y salidas)
 * Simplificado: solo maneja HTTP y delega a servicios
 */
@RestController
@RequestMapping("/api/estacionamiento")
@CrossOrigin(origins = "*")
public class EstacionamientoController {

    private final IEstacionamientoService estacionamientoService;

    @Autowired
    public EstacionamientoController(IEstacionamientoService estacionamientoService) {
        this.estacionamientoService = estacionamientoService;
    }

    /**
     * Registra la entrada de un vehículo al estacionamiento
     */
    @PostMapping("/entrada")
    public ResponseEntity<ApiResponse<EntradaResponse>> registrarEntrada(@RequestBody EntradaRequest request) {
        try {
            // El servicio hace la operación y devuelve la estancia con la hora real
            Estancia estancia = estacionamientoService.registrarEntrada(request.getPlaca());
            
            // Crear respuesta con la hora exacta de la estancia guardada
            EntradaResponse response = new EntradaResponse(
                    "Entrada registrada exitosamente",
                    request.getPlaca(),
                    estancia.getVehiculo().getTipo().name(), // Tipo real del vehículo
                    estancia.getFechaEntrada() // ✅ Hora real de entrada guardada en BD
            );
            
            return ResponseEntity.ok(ApiResponse.success("Entrada registrada exitosamente", response));

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

    /**
     * Registra la salida de un vehículo del estacionamiento
     */
    @PostMapping("/salida")
    public ResponseEntity<ApiResponse<SalidaResponse>> registrarSalida(@RequestBody SalidaRequest request) {
        try {
            // El servicio calcula el importe
            BigDecimal importe = estacionamientoService.registrarSalida(request.getPlaca());
            
            // Obtener información de la estancia para la respuesta
            List<Estancia> historial = estacionamientoService.consultarHistorialVehiculo(request.getPlaca());
            Estancia ultimaEstancia = historial.isEmpty() ? null : historial.get(0);

            SalidaResponse response = new SalidaResponse(
                    "Salida registrada exitosamente",
                    request.getPlaca(),
                    ultimaEstancia != null ? ultimaEstancia.getVehiculo().getTipo().name() : "DESCONOCIDO",
                    ultimaEstancia != null ? ultimaEstancia.getFechaEntrada() : null,
                    ultimaEstancia != null ? ultimaEstancia.getFechaSalida() : null,
                    ultimaEstancia != null ? ultimaEstancia.getMinutosEstancia() : 0,
                    importe
            );
            
            return ResponseEntity.ok(ApiResponse.success("Salida registrada exitosamente", response));

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

    /**
     * Obtiene todos los vehículos actualmente en el estacionamiento
     */
    @GetMapping("/activos")
    public ResponseEntity<ApiResponse<List<VehiculoActivoInfo>>> obtenerVehiculosActivos() {
        try {
            List<Estancia> estanciasActivas = estacionamientoService.obtenerVehiculosActivos();
            
            // Transformar entidades a DTOs
            List<VehiculoActivoInfo> vehiculosActivos = estanciasActivas.stream()
                    .map(VehiculoActivoInfo::new)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success("Consulta exitosa", vehiculosActivos));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error interno del servidor: " + e.getMessage()));
        }
    }

    /**
     * Inicia un nuevo mes (limpia datos según reglas de negocio)
     */
    @PostMapping("/nuevo-mes")
    public ResponseEntity<ApiResponse<String>> iniciarNuevoMes() {
        try {
            estacionamientoService.iniciarNuevoMes();
            String mensaje = "Se han eliminado todas las estancias de vehículos oficiales y reiniciado contadores de residentes a cero";
            return ResponseEntity.ok(ApiResponse.success("Nuevo mes iniciado exitosamente", mensaje));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error interno del servidor: " + e.getMessage()));
        }
    }
} 