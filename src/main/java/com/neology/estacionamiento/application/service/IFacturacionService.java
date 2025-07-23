package com.neology.estacionamiento.application.service;

import java.math.BigDecimal;

/**
 * Interfaz del servicio de facturación
 * Define operaciones de alto nivel para reportes y facturación
 */
public interface IFacturacionService {
    
    /**
     * Genera el reporte completo de pagos de residentes
     * @return Reporte con todos los residentes y sus deudas
     */
    FacturacionService.ReporteResidentes generarReporteResidentes();
    
    /**
     * Calcula la deuda específica de un residente
     * @param placa Placa del vehículo residente
     * @return Importe de la deuda del residente
     * @throws IllegalArgumentException Si la placa es inválida o no es residente
     */
    BigDecimal calcularDeudaResidente(String placa);
    
    /**
     * Obtiene el resumen general de facturación mensual
     * @return Resumen con estadísticas de facturación
     */
    FacturacionService.ResumenFacturacion obtenerResumenFacturacion();
} 