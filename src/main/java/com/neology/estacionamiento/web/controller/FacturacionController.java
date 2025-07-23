package com.neology.estacionamiento.web.controller;

import com.neology.estacionamiento.application.service.IFacturacionService;
import com.neology.estacionamiento.application.service.FacturacionService;
import com.neology.estacionamiento.application.dto.DeudaResidenteInfo;
import com.neology.estacionamiento.web.request.ReporteRequest;
import com.neology.estacionamiento.web.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * Controlador REST para facturación y reportes de residentes
 * Simplificado: solo maneja HTTP y delega a servicios
 */
@RestController
@RequestMapping("/api/facturacion")
@CrossOrigin(origins = "*")
public class FacturacionController {

    private final IFacturacionService facturacionService;

    @Autowired
    public FacturacionController(IFacturacionService facturacionService) {
        this.facturacionService = facturacionService;
    }

    /**
     * Genera el reporte de pagos de residentes
     */
    @GetMapping("/residentes")
    public ResponseEntity<ApiResponse<FacturacionService.ReporteResidentes>> generarReporteResidentes() {
        try {
            FacturacionService.ReporteResidentes reporte = facturacionService.generarReporteResidentes();
            return ResponseEntity.ok(ApiResponse.success("Reporte generado exitosamente", reporte));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error interno del servidor: " + e.getMessage()));
        }
    }

    /**
     * Obtiene la deuda específica de un residente
     */
    @GetMapping("/residente/{placa}")
    public ResponseEntity<ApiResponse<DeudaResidenteInfo>> obtenerDeudaResidente(@PathVariable String placa) {
        try {
            BigDecimal deuda = facturacionService.calcularDeudaResidente(placa);
            DeudaResidenteInfo deudaInfo = new DeudaResidenteInfo(placa, deuda);
            
            return ResponseEntity.ok(ApiResponse.success("Consulta exitosa", deudaInfo));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error en los datos: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error interno del servidor: " + e.getMessage()));
        }
    }

    /**
     * Obtiene el resumen de facturación mensual
     */
    @GetMapping("/resumen")
    public ResponseEntity<ApiResponse<FacturacionService.ResumenFacturacion>> obtenerResumenFacturacion() {
        try {
            FacturacionService.ResumenFacturacion resumen = facturacionService.obtenerResumenFacturacion();
            return ResponseEntity.ok(ApiResponse.success("Resumen generado exitosamente", resumen));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error interno del servidor: " + e.getMessage()));
        }
    }

    /**
     * Genera el reporte de pagos de residentes en formato texto
     */
    @GetMapping("/residentes/texto")
    public ResponseEntity<String> generarReporteResidentesTexto() {
        try {
            FacturacionService.ReporteResidentes reporte = facturacionService.generarReporteResidentes();
            String reporteTexto = reporte.generarReporteTexto();
            
            return ResponseEntity.ok()
                    .header("Content-Type", "text/plain; charset=utf-8")
                    .header("Content-Disposition", "attachment; filename=reporte_residentes.txt")
                    .body(reporteTexto);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al generar el reporte: " + e.getMessage());
        }
    }

    /**
     * Genera el reporte de pagos de residentes con nombre de archivo personalizado
     * Formato exacto según requerimientos del caso de uso
     */
    @GetMapping("/residentes/archivo/{nombreArchivo}")
    public ResponseEntity<String> generarReporteResidentesPersonalizado(@PathVariable String nombreArchivo) {
        try {
            FacturacionService.ReporteResidentes reporte = facturacionService.generarReporteResidentes();
            String reporteTexto = reporte.generarReporteTextoPersonalizado();
            
            // Asegurar que el archivo tenga extensión .txt
            String nombreFinal = nombreArchivo.endsWith(".txt") ? nombreArchivo : nombreArchivo + ".txt";
            
            return ResponseEntity.ok()
                    .header("Content-Type", "text/plain; charset=utf-8")
                    .header("Content-Disposition", "attachment; filename=" + nombreFinal)
                    .body(reporteTexto);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al generar el reporte: " + e.getMessage());
        }
    }

    /**
     * Genera el reporte de pagos de residentes con nombre personalizado (POST)
     * Este endpoint cumple exactamente con el caso de uso:
     * - El empleado especifica el nombre del archivo
     * - Genera formato exacto: "Núm. placa \t Tiempo estacionado (min.) \t Cantidad a pagar"
     */
    @PostMapping("/residentes/generar-informe")
    public ResponseEntity<String> generarInformeResidentes(@RequestBody ReporteRequest request) {
        try {
            if (request.getNombreArchivo() == null || request.getNombreArchivo().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("Error: El nombre del archivo es requerido");
            }

            FacturacionService.ReporteResidentes reporte = facturacionService.generarReporteResidentes();
            String reporteTexto = reporte.generarReporteTextoPersonalizado();
            
            // Asegurar que el archivo tenga extensión .txt
            String nombreFinal = request.getNombreArchivo().endsWith(".txt") 
                ? request.getNombreArchivo() 
                : request.getNombreArchivo() + ".txt";
            
            return ResponseEntity.ok()
                    .header("Content-Type", "text/plain; charset=utf-8")
                    .header("Content-Disposition", "attachment; filename=" + nombreFinal)
                    .body(reporteTexto);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al generar el reporte: " + e.getMessage());
        }
    }
} 