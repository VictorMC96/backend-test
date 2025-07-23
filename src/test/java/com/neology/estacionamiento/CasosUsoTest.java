package com.neology.estacionamiento;

import com.neology.estacionamiento.application.service.*;
import com.neology.estacionamiento.domain.model.*;
import com.neology.estacionamiento.domain.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests completos para todos los casos de uso del sistema
 */
@ExtendWith(MockitoExtension.class)
class CasosUsoTest {

    @Mock private VehiculoRepository vehiculoRepository;
    @Mock private EstanciaRepository estanciaRepository;
    
    @InjectMocks private EstacionamientoService estacionamientoService;
    @InjectMocks private VehiculoService vehiculoService;
    @InjectMocks private FacturacionService facturacionService;

    // =====================================
    // CASO DE USO: "Registra entrada"
    // =====================================
    
    @Test
    void casoUso_RegistrarEntrada_VehiculoNuevo() {
        // Given - Vehículo no existe en el sistema
        String placa = "ABC123";
        when(vehiculoRepository.findByPlaca(placa)).thenReturn(Optional.empty());
        when(vehiculoRepository.save(any(VehiculoNoResidente.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        when(estanciaRepository.save(any(Estancia.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // When - Empleado registra entrada
        Estancia resultado = estacionamientoService.registrarEntrada(placa);

        // Then - Se auto-registra como NO_RESIDENTE y se crea estancia
        assertNotNull(resultado);
        assertEquals(placa, resultado.getVehiculo().getPlaca());
        assertEquals(TipoVehiculo.NO_RESIDENTE, resultado.getVehiculo().getTipo());
        assertEquals(EstadoEstancia.ACTIVA, resultado.getEstado());
        assertNotNull(resultado.getFechaEntrada());
        
        verify(vehiculoRepository).save(any(VehiculoNoResidente.class));
        verify(estanciaRepository).save(any(Estancia.class));
    }

    @Test
    void casoUso_RegistrarEntrada_VehiculoExistente() {
        // Given - Vehículo oficial ya registrado
        String placa = "GOV001";
        VehiculoOficial vehiculo = new VehiculoOficial(placa);
        when(vehiculoRepository.findByPlaca(placa)).thenReturn(Optional.of(vehiculo));
        when(estanciaRepository.save(any(Estancia.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // When - Empleado registra entrada
        Estancia resultado = estacionamientoService.registrarEntrada(placa);

        // Then - Se usa el vehículo existente
        assertNotNull(resultado);
        assertEquals(placa, resultado.getVehiculo().getPlaca());
        assertEquals(TipoVehiculo.OFICIAL, resultado.getVehiculo().getTipo());
        assertEquals(EstadoEstancia.ACTIVA, resultado.getEstado());
        
        verify(vehiculoRepository, never()).save(any()); // No se guarda nuevo vehículo
        verify(estanciaRepository).save(any(Estancia.class));
    }

    @Test
    void casoUso_RegistrarEntrada_ErrorVehiculoYaEnEstacionamiento() {
        // Given - Vehículo con estancia activa
        String placa = "GOV001";
        VehiculoOficial vehiculo = new VehiculoOficial(placa);
        Estancia estanciaActiva = new Estancia(vehiculo);
        vehiculo.agregarEstancia(estanciaActiva);
        
        when(vehiculoRepository.findByPlaca(placa)).thenReturn(Optional.of(vehiculo));

        // When & Then - Debe lanzar excepción
        IllegalStateException ex = assertThrows(IllegalStateException.class, 
            () -> estacionamientoService.registrarEntrada(placa));
        
        assertTrue(ex.getMessage().contains("ya tiene una estancia activa"));
        verify(estanciaRepository, never()).save(any());
    }

    // =====================================
    // CASO DE USO: "Registra salida"
    // =====================================

    @Test
    void casoUso_RegistrarSalida_VehiculoOficial() {
        // Given - Vehículo oficial con estancia activa
        String placa = "GOV001";
        VehiculoOficial vehiculo = new VehiculoOficial(placa);
        Estancia estancia = new Estancia(vehiculo);
        
        when(vehiculoRepository.findByPlaca(placa)).thenReturn(Optional.of(vehiculo));
        when(estanciaRepository.findByVehiculoPlacaAndEstado(placa, EstadoEstancia.ACTIVA))
            .thenReturn(Optional.of(estancia));

        // When - Empleado registra salida
        BigDecimal importe = estacionamientoService.registrarSalida(placa);

        // Then - Importe = 0, estancia finalizada
        assertEquals(BigDecimal.ZERO, importe);
        assertEquals(EstadoEstancia.FINALIZADA, estancia.getEstado());
        assertNotNull(estancia.getFechaSalida());
        assertTrue(estancia.getMinutosEstancia() >= 0);
        
        verify(estanciaRepository).save(estancia);
        verify(vehiculoRepository, never()).save(any()); // Oficiales no acumulan tiempo
    }

    @Test
    void casoUso_RegistrarSalida_VehiculoResidente() {
        // Given - Vehículo residente con estancia activa
        String placa = "RES001";
        VehiculoResidente vehiculo = new VehiculoResidente(placa);
        int tiempoInicialMinutos = 100;
        vehiculo.setTiempoAcumuladoMes(tiempoInicialMinutos); // Ya tenía 100 minutos
        
        // Crear estancia con tiempo simulado
        Estancia estancia = new Estancia(vehiculo);
        // Simular que han pasado 30 minutos configurando fechas manualmente
        java.util.Calendar fechaEntrada = java.util.Calendar.getInstance();
        fechaEntrada.add(java.util.Calendar.MINUTE, -30); // Entrada hace 30 minutos
        estancia.setFechaEntrada(fechaEntrada);
        
        when(vehiculoRepository.findByPlaca(placa)).thenReturn(Optional.of(vehiculo));
        when(estanciaRepository.findByVehiculoPlacaAndEstado(placa, EstadoEstancia.ACTIVA))
            .thenReturn(Optional.of(estancia));

        // When - Empleado registra salida
        BigDecimal importe = estacionamientoService.registrarSalida(placa);

        // Then - Importe = 0 (no paga al salir), tiempo se acumula
        assertEquals(BigDecimal.ZERO, importe);
        assertEquals(EstadoEstancia.FINALIZADA, estancia.getEstado());
        assertNotNull(estancia.getFechaSalida());
        assertTrue(estancia.getMinutosEstancia() >= 0); // Tiempo calculado
        assertTrue(vehiculo.getTiempoAcumuladoMes() >= tiempoInicialMinutos); // Se acumuló tiempo
        
        verify(estanciaRepository).save(estancia);
        verify(vehiculoRepository).save(vehiculo); // Se guarda el tiempo acumulado
    }

    @Test
    void casoUso_RegistrarSalida_VehiculoNoResidente() {
        // Given - Vehículo no residente con estancia activa
        String placa = "NOR001";
        VehiculoNoResidente vehiculo = new VehiculoNoResidente(placa);
        
        // Crear estancia con tiempo simulado (30 minutos)
        Estancia estancia = new Estancia(vehiculo);
        java.util.Calendar fechaEntrada = java.util.Calendar.getInstance();
        fechaEntrada.add(java.util.Calendar.MINUTE, -30); // Entrada hace 30 minutos
        estancia.setFechaEntrada(fechaEntrada);
        
        when(vehiculoRepository.findByPlaca(placa)).thenReturn(Optional.of(vehiculo));
        when(estanciaRepository.findByVehiculoPlacaAndEstado(placa, EstadoEstancia.ACTIVA))
            .thenReturn(Optional.of(estancia));

        // When - Empleado registra salida
        BigDecimal importe = estacionamientoService.registrarSalida(placa);

        // Then - Importe > 0 (paga inmediatamente), estancia finalizada
        assertTrue(importe.compareTo(BigDecimal.ZERO) > 0);
        assertEquals(EstadoEstancia.FINALIZADA, estancia.getEstado());
        assertNotNull(estancia.getFechaSalida());
        assertTrue(estancia.getMinutosEstancia() >= 0); // Tiempo calculado
        assertEquals(importe, estancia.getImportePagado());
        
        verify(estanciaRepository).save(estancia);
        verify(vehiculoRepository, never()).save(any()); // No residentes no acumulan
    }

    // =====================================
    // CASO DE USO: "Da de alta vehículo oficial"
    // =====================================

    @Test
    void casoUso_AltaVehiculoOficial_Exitoso() {
        // Given - Placa válida y no existe
        String placa = "GOV123";
        when(vehiculoRepository.existsByPlaca(placa)).thenReturn(false);
        when(vehiculoRepository.save(any(VehiculoOficial.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // When - Empleado da de alta vehículo oficial
        VehiculoOficial resultado = vehiculoService.registrarVehiculoOficial(placa);

        // Then - Vehículo oficial creado y guardado
        assertNotNull(resultado);
        assertEquals(placa, resultado.getPlaca());
        assertEquals(TipoVehiculo.OFICIAL, resultado.getTipo());
        assertNotNull(resultado.getFechaAlta());
        assertEquals(BigDecimal.ZERO, resultado.calcularImporte(100)); // No paga
        
        verify(vehiculoRepository).save(any(VehiculoOficial.class));
    }

    @Test
    void casoUso_AltaVehiculoOficial_PlacaDuplicada() {
        // Given - Placa ya existe
        String placa = "GOV123";
        when(vehiculoRepository.existsByPlaca(placa)).thenReturn(true);

        // When & Then - Debe lanzar excepción
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> vehiculoService.registrarVehiculoOficial(placa));
        
        assertTrue(ex.getMessage().contains("Ya existe un vehículo registrado"));
        verify(vehiculoRepository, never()).save(any());
    }

    @Test
    void casoUso_AltaVehiculoOficial_PlacaInvalida() {
        // When & Then - Placa vacía
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class,
            () -> vehiculoService.registrarVehiculoOficial(""));
        assertTrue(ex1.getMessage().contains("no puede estar vacía"));

        // When & Then - Placa muy larga  
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class,
            () -> vehiculoService.registrarVehiculoOficial("PLACAMUYLARGA123"));
        assertTrue(ex2.getMessage().contains("más de 10 caracteres"));

        verify(vehiculoRepository, never()).save(any());
    }

    // =====================================
    // CASO DE USO: "Da de alta vehículo de residente"
    // =====================================

    @Test
    void casoUso_AltaVehiculoResidente_Exitoso() {
        // Given - Placa válida y no existe
        String placa = "RES123";
        when(vehiculoRepository.existsByPlaca(placa)).thenReturn(false);
        when(vehiculoRepository.save(any(VehiculoResidente.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // When - Empleado da de alta vehículo residente
        VehiculoResidente resultado = vehiculoService.registrarVehiculoResidente(placa);

        // Then - Vehículo residente creado con tiempo inicial = 0
        assertNotNull(resultado);
        assertEquals(placa, resultado.getPlaca());
        assertEquals(TipoVehiculo.RESIDENTE, resultado.getTipo());
        assertEquals(0, resultado.getTiempoAcumuladoMes());
        assertNotNull(resultado.getFechaAlta());
        assertEquals(BigDecimal.ZERO, resultado.calcularImporte(100)); // No paga por estancia
        
        verify(vehiculoRepository).save(any(VehiculoResidente.class));
    }

    // =====================================
    // CASO DE USO: "Comienza mes"
    // =====================================

    @Test
    void casoUso_ComenzarMes_LimpiaOficialesYReiniciaResidentes() {
        // Given - Estancias de oficiales y residentes con tiempo acumulado
        VehiculoOficial oficial1 = new VehiculoOficial("GOV001");
        VehiculoOficial oficial2 = new VehiculoOficial("GOV002");
        VehiculoResidente residente1 = new VehiculoResidente("RES001");
        VehiculoResidente residente2 = new VehiculoResidente("RES002");
        
        residente1.setTiempoAcumuladoMes(1200); // Tenía 1200 minutos
        residente2.setTiempoAcumuladoMes(800);  // Tenía 800 minutos

        Estancia estanciaOficial1 = new Estancia(oficial1);
        Estancia estanciaOficial2 = new Estancia(oficial2);
        estanciaOficial2.registrarSalida(); // Una finalizada
        
        List<Estancia> estanciasActivas = Arrays.asList(estanciaOficial1);
        List<Estancia> estanciasFinalizadas = Arrays.asList(estanciaOficial2);
        List<Vehiculo> residentes = Arrays.asList(residente1, residente2);

        when(estanciaRepository.findByEstado(EstadoEstancia.ACTIVA)).thenReturn(estanciasActivas);
        when(estanciaRepository.findByEstado(EstadoEstancia.FINALIZADA)).thenReturn(estanciasFinalizadas);
        when(vehiculoRepository.findByTipo(TipoVehiculo.RESIDENTE)).thenReturn(residentes);

        // When - Empleado inicia nuevo mes
        estacionamientoService.iniciarNuevoMes();

        // Then - Estancias oficiales eliminadas, residentes reiniciados
        verify(estanciaRepository).delete(estanciaOficial1); // Activa eliminada
        verify(estanciaRepository).delete(estanciaOficial2); // Finalizada eliminada
        
        assertEquals(0, residente1.getTiempoAcumuladoMes()); // Reiniciado
        assertEquals(0, residente2.getTiempoAcumuladoMes()); // Reiniciado
        
        verify(vehiculoRepository, times(2)).save(any(VehiculoResidente.class));
    }

    // =====================================
    // CASO DE USO: "Pagos de residentes"
    // =====================================

    @Test
    void casoUso_PagosResidentes_GeneraReporteCorrectamente() {
        // Given - Residentes con tiempo acumulado
        VehiculoResidente residente1 = new VehiculoResidente("RES001");
        VehiculoResidente residente2 = new VehiculoResidente("RES002");
        
        residente1.setTiempoAcumuladoMes(1200); // 1200 min = $60.00
        residente2.setTiempoAcumuladoMes(800);  // 800 min = $40.00
        
        List<Vehiculo> residentes = Arrays.asList(residente1, residente2);
        when(vehiculoRepository.findByTipo(TipoVehiculo.RESIDENTE)).thenReturn(residentes);

        // When - Empleado genera reporte de pagos
        FacturacionService.ReporteResidentes reporte = facturacionService.generarReporteResidentes();

        // Then - Reporte contiene datos correctos
        assertNotNull(reporte);
        assertEquals(2, reporte.getItems().size());
        assertEquals(new BigDecimal("100.00"), reporte.getTotalGeneral()); // $60 + $40
        
        // Verificar items individuales
        List<FacturacionService.ItemReporteResidente> items = reporte.getItems();
        FacturacionService.ItemReporteResidente item1 = items.get(0);
        FacturacionService.ItemReporteResidente item2 = items.get(1);
        
        assertEquals("RES001", item1.getPlaca());
        assertEquals(1200, item1.getMinutosEstacionados());
        assertEquals(new BigDecimal("60.00"), item1.getCantidadAPagar());
        
        assertEquals("RES002", item2.getPlaca());
        assertEquals(800, item2.getMinutosEstacionados());
        assertEquals(new BigDecimal("40.00"), item2.getCantidadAPagar());
    }

    @Test
    void casoUso_PagosResidentes_FormatoTextoPersonalizadoCorrecto() {
        // Given - Un residente con tiempo acumulado
        VehiculoResidente residente = new VehiculoResidente("S1234A");
        residente.setTiempoAcumuladoMes(20134); // Como en el ejemplo del requerimiento
        
        List<Vehiculo> residentes = Arrays.asList(residente);
        when(vehiculoRepository.findByTipo(TipoVehiculo.RESIDENTE)).thenReturn(residentes);

        // When - Se genera reporte con formato personalizado
        FacturacionService.ReporteResidentes reporte = facturacionService.generarReporteResidentes();
        String textoPersonalizado = reporte.generarReporteTextoPersonalizado();

        // Then - Formato debe coincidir exactamente con el requerimiento
        assertTrue(textoPersonalizado.contains("Núm. placa\tTiempo estacionado (min.)\tCantidad a pagar"));
        assertTrue(textoPersonalizado.contains("S1234A\t20134\t\t1006.70"));
        assertFalse(textoPersonalizado.contains("REPORTE DE FACTURACIÓN")); // Sin headers extra
        assertFalse(textoPersonalizado.contains("TOTAL GENERAL")); // Sin total en formato personalizado
    }

    @Test
    void casoUso_PagosResidentes_CalculoDeudaIndividual() {
        // Given - Residente específico con tiempo acumulado
        String placa = "RES001";
        VehiculoResidente residente = new VehiculoResidente(placa);
        residente.setTiempoAcumuladoMes(2000); // 2000 min = $100.00
        
        when(vehiculoRepository.findByPlaca(placa)).thenReturn(Optional.of(residente));

        // When - Empleado consulta deuda específica
        BigDecimal deuda = facturacionService.calcularDeudaResidente(placa);

        // Then - Deuda calculada correctamente
        assertEquals(new BigDecimal("100.00"), deuda); // 2000 * $0.05
    }

    @Test
    void casoUso_PagosResidentes_ErrorVehiculoNoResidente() {
        // Given - Vehículo oficial (no residente)
        String placa = "GOV001";
        VehiculoOficial oficial = new VehiculoOficial(placa);
        when(vehiculoRepository.findByPlaca(placa)).thenReturn(Optional.of(oficial));

        // When & Then - Debe lanzar excepción
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> facturacionService.calcularDeudaResidente(placa));
        
        assertTrue(ex.getMessage().contains("no es de tipo residente"));
    }
} 