package com.neology.estacionamiento;

import com.neology.estacionamiento.domain.model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

/**
 * Tests simples para vehículos y cálculos de tarifas
 */
class VehiculoSimpleTest {

    @Test
    void vehiculoOficial_noPagaImporte() {
        // Given
        VehiculoOficial oficial = new VehiculoOficial("GOV123");
        
        // When
        BigDecimal importe = oficial.calcularImporte(120); // 2 horas
        
        // Then
        assertEquals(BigDecimal.ZERO, importe);
        assertEquals(TipoVehiculo.OFICIAL, oficial.getTipo());
        assertEquals("GOV123", oficial.getPlaca());
    }

    @Test
    void vehiculoResidente_noPagaPorEstancia() {
        // Given
        VehiculoResidente residente = new VehiculoResidente("RES456");
        
        // When - Los residentes no pagan por estancia individual
        BigDecimal importeEstancia = residente.calcularImporte(100); // 100 minutos
        
        // Then - No hay importe por estancia
        assertEquals(BigDecimal.ZERO, importeEstancia);
        assertEquals(TipoVehiculo.RESIDENTE, residente.getTipo());
        
        // But - Pueden calcular tarifa mensual
        BigDecimal tarifaMensual = residente.calcularImporteMensual(100);
        assertEquals(new BigDecimal("5.00"), tarifaMensual); // 100 * $0.05 = $5.00
    }

    @Test
    void vehiculoNoResidente_calculaTarifaCorrecta() {
        // Given
        VehiculoNoResidente noResidente = new VehiculoNoResidente("NOR789");
        
        // When
        BigDecimal importe = noResidente.calcularImporte(10); // 10 minutos
        
        // Then
        // Tarifa: $0.50 por minuto = 10 * 0.50 = $5.00
        assertEquals(new BigDecimal("5.00"), importe);
        assertEquals(TipoVehiculo.NO_RESIDENTE, noResidente.getTipo());
    }

    @Test
    void estancia_registrarSalidaFunciona() {
        // Given
        VehiculoOficial vehiculo = new VehiculoOficial("TEST123");
        Estancia estancia = new Estancia(vehiculo);
        
        // When
        estancia.registrarSalida();
        
        // Then
        assertEquals(EstadoEstancia.FINALIZADA, estancia.getEstado());
        assertNotNull(estancia.getFechaSalida());
        assertTrue(estancia.getMinutosEstancia() >= 0);
    }

    @Test
    void vehiculoResidente_acumulaTiempoCorrectamente() {
        // Given
        VehiculoResidente residente = new VehiculoResidente("RES001");
        assertEquals(0, residente.getTiempoAcumuladoMes()); // Inicial = 0
        
        // When - Acumula tiempo
        residente.acumularTiempoMes(100);
        residente.acumularTiempoMes(50);
        
        // Then
        assertEquals(150, residente.getTiempoAcumuladoMes());
        assertEquals(new BigDecimal("7.50"), residente.calcularDeudaMensual()); // 150 * $0.05
        
        // When - Reinicia mes
        residente.reiniciarTiempoMes();
        
        // Then
        assertEquals(0, residente.getTiempoAcumuladoMes());
        assertEquals(0, residente.calcularDeudaMensual().compareTo(BigDecimal.ZERO));
    }

    @Test
    void estancia_usaCalendarCorrectamente() {
        // Given
        VehiculoOficial vehiculo = new VehiculoOficial("TEST456");
        
        // When
        Estancia estancia = new Estancia(vehiculo);
        
        // Then - Verifica que usa Calendar para fechas
        assertNotNull(estancia.getFechaEntrada());
        assertTrue(estancia.getFechaEntrada().getClass().getSimpleName().equals("GregorianCalendar"));
        assertEquals(EstadoEstancia.ACTIVA, estancia.getEstado());
        
        // When - Registra salida
        estancia.registrarSalida();
        
        // Then - Verifica cálculo con Calendar
        assertNotNull(estancia.getFechaSalida());
        assertTrue(estancia.getFechaSalida().getClass().getSimpleName().equals("GregorianCalendar"));
        assertTrue(estancia.getMinutosEstancia() >= 0);
    }
} 